package weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Buscar {

    private static final String BASE_URL =
            "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";

    private final String apiKey;

    public Buscar(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("API Key não pode ser nula ou vazia.");
        }
        this.apiKey = apiKey;
    }

    public Dados buscarClima(String cidade) throws IOException, ClimaException {
        String cidadeCodificada = URLEncoder.encode(cidade, StandardCharsets.UTF_8);
        String urlStr = BASE_URL + cidadeCodificada + "/today"
                + "?unitGroup=metric"
                + "&include=current,days"
                + "&key=" + apiKey
                + "&contentType=json";

        String jsonResposta = realizarRequisicao(urlStr);
        return interpretarJson(cidade, jsonResposta);
    }

    private String realizarRequisicao(String urlStr) throws IOException, ClimaException {
        HttpURLConnection conexao = null;
        try {
            URL url = URI.create(urlStr).toURL();
            conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");
            conexao.setConnectTimeout(10_000);
            conexao.setReadTimeout(10_000);
            conexao.setRequestProperty("Accept", "application/json");

            int statusCode = conexao.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                String mensagemErro = lerStream(new BufferedReader(
                        new InputStreamReader(conexao.getErrorStream(), StandardCharsets.UTF_8)));
                throw new ClimaException("Erro na API (HTTP " + statusCode + "): " + mensagemErro);
            }

            return lerStream(new BufferedReader(
                    new InputStreamReader(conexao.getInputStream(), StandardCharsets.UTF_8)));
        } finally {
            if (conexao != null) {
                conexao.disconnect();
            }
        }
    }

    private String lerStream(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        String linha;
        while ((linha = reader.readLine()) != null) {
            sb.append(linha);
        }
        return sb.toString();
    }

    private Dados interpretarJson(String cidade, String json) throws ClimaException {
        try {
            String enderecoResolvido = extrairValorString(json, "resolvedAddress");

            String blocoAtual = extrairBloco(json, "currentConditions");
            double temperaturaAtual = extrairValorDouble(blocoAtual, "temp");
            double humidade = extrairValorDouble(blocoAtual, "humidity");
            String condicaoTempo = traduzirCondicao(extrairValorString(blocoAtual, "conditions"));
            double precipitacao = extrairValorDoubleOpcional(blocoAtual, "precip");
            double velocidadeVento = extrairValorDouble(blocoAtual, "windspeed");
            double grausVento = extrairValorDouble(blocoAtual, "winddir");
            String direcaoVento = grausParaDirecao(grausVento);

            String primeiroDia = extrairPrimeiroDia(json);
            double tempMax = extrairValorDouble(primeiroDia, "tempmax");
            double tempMin = extrairValorDouble(primeiroDia, "tempmin");

            return new Dados(cidade, enderecoResolvido, temperaturaAtual,
                    tempMax, tempMin, humidade, condicaoTempo,
                    precipitacao, velocidadeVento, direcaoVento);

        } catch (ClimaException e) {
            throw e;
        } catch (Exception e) {
            throw new ClimaException("Falha ao interpretar resposta da API: " + e.getMessage());
        }
    }

    private String extrairValorString(String json, String chave) throws ClimaException {
        String padrao = "\"" + chave + "\"";
        int idx = json.indexOf(padrao);
        if (idx == -1) {
            throw new ClimaException("Campo não encontrado no JSON: " + chave);
        }
        int inicio = json.indexOf('"', idx + padrao.length() + 1);
        int fim = json.indexOf('"', inicio + 1);
        if (inicio == -1 || fim == -1) {
            throw new ClimaException("Valor inválido para o campo: " + chave);
        }
        return json.substring(inicio + 1, fim);
    }

    private double extrairValorDouble(String json, String chave) throws ClimaException {
        String padrao = "\"" + chave + "\":";
        int idx = json.indexOf(padrao);
        if (idx == -1) {
            throw new ClimaException("Campo numérico não encontrado no JSON: " + chave);
        }
        int inicio = idx + padrao.length();
        while (inicio < json.length() && json.charAt(inicio) == ' ') {
            inicio++;
        }
        int fim = inicio;
        while (fim < json.length()
                && (Character.isDigit(json.charAt(fim))
                || json.charAt(fim) == '.'
                || json.charAt(fim) == '-')) {
            fim++;
        }
        String valorStr = json.substring(inicio, fim).trim();
        if (valorStr.isEmpty()) {
            throw new ClimaException("Valor numérico vazio para o campo: " + chave);
        }
        return Double.parseDouble(valorStr);
    }

    private double extrairValorDoubleOpcional(String json, String chave) {
        try {
            String padrao = "\"" + chave + "\":";
            int idx = json.indexOf(padrao);
            if (idx == -1) return 0.0;
            int inicio = idx + padrao.length();
            while (inicio < json.length() && json.charAt(inicio) == ' ') inicio++;
            if (json.startsWith("null", inicio)) return 0.0;
            int fim = inicio;
            while (fim < json.length()
                    && (Character.isDigit(json.charAt(fim))
                    || json.charAt(fim) == '.'
                    || json.charAt(fim) == '-')) {
                fim++;
            }
            String valorStr = json.substring(inicio, fim).trim();
            return valorStr.isEmpty() ? 0.0 : Double.parseDouble(valorStr);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private String extrairBloco(String json, String chave) throws ClimaException {
        String padrao = "\"" + chave + "\":{";
        int inicio = json.indexOf(padrao);
        if (inicio == -1) {
            throw new ClimaException("Bloco JSON não encontrado: " + chave);
        }
        inicio = json.indexOf('{', inicio + padrao.length() - 1);
        int profundidade = 0;
        int i = inicio;
        while (i < json.length()) {
            char c = json.charAt(i);
            if (c == '{') profundidade++;
            else if (c == '}') {
                profundidade--;
                if (profundidade == 0) return json.substring(inicio, i + 1);
            }
            i++;
        }
        throw new ClimaException("Bloco JSON malformado: " + chave);
    }

    private String extrairPrimeiroDia(String json) throws ClimaException {
        String padrao = "\"days\":[";
        int idx = json.indexOf(padrao);
        if (idx == -1) {
            throw new ClimaException("Array 'days' não encontrado no JSON.");
        }
        int inicio = json.indexOf('{', idx + padrao.length());
        if (inicio == -1) {
            throw new ClimaException("Objeto de dia não encontrado no JSON.");
        }
        int profundidade = 0;
        int i = inicio;
        while (i < json.length()) {
            char c = json.charAt(i);
            if (c == '{') profundidade++;
            else if (c == '}') {
                profundidade--;
                if (profundidade == 0) return json.substring(inicio, i + 1);
            }
            i++;
        }
        throw new ClimaException("Objeto do primeiro dia malformado.");
    }

    private String grausParaDirecao(double graus) {
        String[] direcoes = {
            "Norte", "Norte-Nordeste", "Nordeste", "Leste-Nordeste",
            "Leste", "Leste-Sudeste", "Sudeste", "Sul-Sudeste",
            "Sul", "Sul-Sudoeste", "Sudoeste", "Oeste-Sudoeste",
            "Oeste", "Oeste-Noroeste", "Noroeste", "Norte-Noroeste"
        };
        int indice = (int) Math.round(graus / 22.5) % 16;
        return direcoes[indice];
    }

    private String traduzirCondicao(String condicaoEn) {
        if (condicaoEn == null || condicaoEn.isBlank()) return "Não disponível";

        String lower = condicaoEn.toLowerCase();

        if (lower.contains("thunderstorm")) return "Tempestade com Raios";
        if (lower.contains("rain") && lower.contains("snow")) return "Chuva com Neve";
        if (lower.contains("heavy rain")) return "Chuva Forte";
        if (lower.contains("light rain")) return "Chuva Fraca";
        if (lower.contains("rain")) return "Chuva";
        if (lower.contains("drizzle")) return "Garoa";
        if (lower.contains("snow")) return "Neve";
        if (lower.contains("sleet")) return "Chuva de Granizo";
        if (lower.contains("hail")) return "Granizo";
        if (lower.contains("fog")) return "Nevoeiro";
        if (lower.contains("mist")) return "Névoa";
        if (lower.contains("haze")) return "Neblina";
        if (lower.contains("overcast")) return "Nublado";
        if (lower.contains("partly cloudy")) return "Parcialmente Nublado";
        if (lower.contains("cloudy")) return "Parcialmente Nublado";
        if (lower.contains("clear")) return "Céu Limpo";
        if (lower.contains("sunny")) return "Ensolarado";
        if (lower.contains("wind")) return "Ventoso";
        if (lower.contains("dust")) return "Poeira";
        if (lower.contains("smoke")) return "Fumaça";

        return condicaoEn;
    }
}
