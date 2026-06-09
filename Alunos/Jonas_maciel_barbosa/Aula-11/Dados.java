package weather;

public class Dados {

    private String cidade;
    private String enderecoResolvido;
    private double temperaturaAtual;
    private double temperaturaMaxima;
    private double temperaturaMinima;
    private double humidade;
    private String condicaoTempo;
    private double precipitacao;
    private double velocidadeVento;
    private String direcaoVento;

    public Dados(String cidade, String enderecoResolvido, double temperaturaAtual,
                 double temperaturaMaxima, double temperaturaMinima, double humidade,
                 String condicaoTempo, double precipitacao, double velocidadeVento,
                 String direcaoVento) {
        this.cidade = cidade;
        this.enderecoResolvido = enderecoResolvido;
        this.temperaturaAtual = temperaturaAtual;
        this.temperaturaMaxima = temperaturaMaxima;
        this.temperaturaMinima = temperaturaMinima;
        this.humidade = humidade;
        this.condicaoTempo = condicaoTempo;
        this.precipitacao = precipitacao;
        this.velocidadeVento = velocidadeVento;
        this.direcaoVento = direcaoVento;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEnderecoResolvido() {
        return enderecoResolvido;
    }

    public double getTemperaturaAtual() {
        return temperaturaAtual;
    }

    public double getTemperaturaMaxima() {
        return temperaturaMaxima;
    }

    public double getTemperaturaMinima() {
        return temperaturaMinima;
    }

    public double getHumidade() {
        return humidade;
    }

    public String getCondicaoTempo() {
        return condicaoTempo;
    }

    public double getPrecipitacao() {
        return precipitacao;
    }

    public double getVelocidadeVento() {
        return velocidadeVento;
    }

    public String getDirecaoVento() {
        return direcaoVento;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=================================================\n");
        sb.append("   PREVISÃO DO TEMPO - ").append(cidade.toUpperCase()).append("\n");
        sb.append("=================================================\n");
        sb.append("Endereço resolvido : ").append(enderecoResolvido).append("\n");
        sb.append("-------------------------------------------------\n");
        sb.append(String.format("Temperatura atual  : %.1f °C%n", temperaturaAtual));
        sb.append(String.format("Temperatura máxima : %.1f °C%n", temperaturaMaxima));
        sb.append(String.format("Temperatura mínima : %.1f °C%n", temperaturaMinima));
        sb.append(String.format("Humidade do ar     : %.0f %%%n", humidade));
        sb.append("Condição do tempo  : ").append(condicaoTempo).append("\n");
        if (precipitacao > 0) {
            sb.append(String.format("Precipitação       : %.2f mm%n", precipitacao));
        } else {
            sb.append("Precipitação       : Sem chuva\n");
        }
        sb.append(String.format("Velocidade do vento: %.1f km/h%n", velocidadeVento));
        sb.append("Direção do vento   : ").append(direcaoVento).append("\n");
        sb.append("=================================================\n");
        return sb.toString();
    }
}
