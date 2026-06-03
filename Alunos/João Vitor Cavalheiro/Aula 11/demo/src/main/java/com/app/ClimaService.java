package com.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ClimaService {

    private static final String API_KEY = "SUA_CHAVE_AQUI";

    public Clima buscarClima(String cidade) {

        try {

            String endereco =
                    "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"+ cidade+ "?unitGroup=metric&key="+ API_KEY;

            URL url = new URL(endereco);

            HttpURLConnection conexao =
                    (HttpURLConnection) url.openConnection();

            conexao.setRequestMethod("GET");

            BufferedReader leitor =
                    new BufferedReader(
                            new InputStreamReader(
                                    conexao.getInputStream()));

            String linha;

            StringBuilder resposta =
                    new StringBuilder();

            while ((linha = leitor.readLine()) != null) {
                resposta.append(linha);
            }

            leitor.close();

            ObjectMapper mapper =
                    new ObjectMapper();

            JsonNode root =
                    mapper.readTree(resposta.toString());

            JsonNode dia =
                    root.get("days").get(0);

            JsonNode atual =
                    root.get("currentConditions");

            Clima clima = new Clima();

            clima.setTemperaturaAtual(
                    atual.get("temp").asDouble());

            clima.setTemperaturaMaxima(
                    dia.get("tempmax").asDouble());

            clima.setTemperaturaMinima(
                    dia.get("tempmin").asDouble());

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

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}