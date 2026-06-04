import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import java.net.URI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClimaService {

    private static final String CHAVE_API =
            System.getenv("VISUAL_CROSSING_API_KEY");

    public Clima buscarClima(String cidade)
            throws IOException, InterruptedException {

        String url =
                "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"
                        + cidade
                        + "?unitGroup=metric&key="
                        + CHAVE_API
                        + "&contentType=json";

        HttpClient cliente =
                HttpClient.newHttpClient();

        HttpRequest requisicao =
                HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

        HttpResponse<String> resposta =
                cliente.send(
                        requisicao,
                        HttpResponse.BodyHandlers.ofString()
                );

        System.out.println(resposta.body());

        JsonObject json =
                JsonParser.parseString(
                        resposta.body()
                ).getAsJsonObject();

        JsonObject condicoesAtuais =
                json.getAsJsonObject("currentConditions");

        JsonArray dias =
                json.getAsJsonArray("days");

        JsonObject hoje =
                dias.get(0).getAsJsonObject();

        double temperaturaAtual =
                condicoesAtuais.get("temp").getAsDouble();

        double temperaturaMaxima =
                hoje.get("tempmax").getAsDouble();

        double temperaturaMinima =
                hoje.get("tempmin").getAsDouble();

        double umidade =
                condicoesAtuais.get("humidity").getAsDouble();

        String condicaoTempo =
                condicoesAtuais.get("conditions").getAsString();

        double precipitacao;

        if (condicoesAtuais.get("precip") != null
                && !condicoesAtuais.get("precip").isJsonNull()) {

            precipitacao =
                    condicoesAtuais.get("precip").getAsDouble();

        } else {

            precipitacao = 0.0;
        }

        double velocidadeVento =
                condicoesAtuais.get("windspeed").getAsDouble();

        double direcaoVento =
                condicoesAtuais.get("winddir").getAsDouble();

        return new Clima(
                temperaturaAtual,
                temperaturaMaxima,
                temperaturaMinima,
                umidade,
                condicaoTempo,
                precipitacao,
                velocidadeVento,
                direcaoVento
        );
    }
}