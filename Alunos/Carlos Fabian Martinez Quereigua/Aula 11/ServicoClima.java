package servico;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import modelo.Clima;
import util.ConfiguracaoApi;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ServicoClima {

    public Clima consultarClima(String cidade)
            throws IOException, InterruptedException {

        String cidadeFormatada =
                URLEncoder.encode(cidade, StandardCharsets.UTF_8);

        String url =
                ConfiguracaoApi.URL_BASE
                        + cidadeFormatada
                        + "?unitGroup=metric"
                        + "&include=current"
                        + "&key=" + ConfiguracaoApi.CHAVE_API
                        + "&contentType=json";

        HttpClient cliente = HttpClient.newHttpClient();

        HttpRequest requisicao = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> resposta =
                cliente.send(requisicao,
                        HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();

        JsonNode raiz = mapper.readTree(resposta.body());

        JsonNode atual = raiz.get("currentConditions");
        JsonNode hoje = raiz.get("days").get(0);

        Clima clima = new Clima();

        clima.setTemperaturaAtual(
                atual.get("temp").asDouble());

        clima.setTemperaturaMaxima(
                hoje.get("tempmax").asDouble());

        clima.setTemperaturaMinima(
                hoje.get("tempmin").asDouble());

        clima.setUmidade(
                atual.get("humidity").asDouble());

        clima.setCondicao(
                atual.get("conditions").asText());

        clima.setPrecipitacao(
                atual.get("precip").asDouble());

        clima.setVelocidadeVento(
                atual.get("windspeed").asDouble());

        clima.setDirecaoVento(
                atual.get("winddir").asDouble());

        return clima;
    }
}