package visual;

import modelo.Clima;
import servico.ServicoClima;

import javax.swing.*;
import java.awt.*;

public class TelaClima extends JFrame {

    private JTextField campoCidade;
    private JButton botaoBuscar;

    private JLabel lblTemperaturaAtual;
    private JLabel lblTemperaturaMaxima;
    private JLabel lblTemperaturaMinima;
    private JLabel lblUmidade;
    private JLabel lblCondicao;
    private JLabel lblPrecipitacao;
    private JLabel lblVelocidadeVento;
    private JLabel lblDirecaoVento;

    public TelaClima() {

        setTitle("Consulta de Clima");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel painelSuperior = new JPanel();

        campoCidade = new JTextField(20);
        botaoBuscar = new JButton("Buscar");

        painelSuperior.add(new JLabel("Cidade:"));
        painelSuperior.add(campoCidade);
        painelSuperior.add(botaoBuscar);

        add(painelSuperior, BorderLayout.NORTH);

        JPanel painelCentral = new JPanel();
        painelCentral.setLayout(new GridLayout(8, 1));

        lblTemperaturaAtual = new JLabel("Temperatura Atual:");
        lblTemperaturaMaxima = new JLabel("Temperatura Máxima:");
        lblTemperaturaMinima = new JLabel("Temperatura Mínima:");
        lblUmidade = new JLabel("Umidade:");
        lblCondicao = new JLabel("Condição:");
        lblPrecipitacao = new JLabel("Precipitação:");
        lblVelocidadeVento = new JLabel("Velocidade do Vento:");
        lblDirecaoVento = new JLabel("Direção do Vento:");

        painelCentral.add(lblTemperaturaAtual);
        painelCentral.add(lblTemperaturaMaxima);
        painelCentral.add(lblTemperaturaMinima);
        painelCentral.add(lblUmidade);
        painelCentral.add(lblCondicao);
        painelCentral.add(lblPrecipitacao);
        painelCentral.add(lblVelocidadeVento);
        painelCentral.add(lblDirecaoVento);

        add(painelCentral, BorderLayout.CENTER);

        botaoBuscar.addActionListener(e -> buscarClima());
    }

    private void buscarClima() {

        try {

            String cidade = campoCidade.getText();

            ServicoClima servico = new ServicoClima();

            Clima clima = servico.consultarClima(cidade);

            lblTemperaturaAtual.setText(
                    "Temperatura Atual: "
                            + clima.getTemperaturaAtual() + " °C");

            lblTemperaturaMaxima.setText(
                    "Temperatura Máxima: "
                            + clima.getTemperaturaMaxima() + " °C");

            lblTemperaturaMinima.setText(
                    "Temperatura Mínima: "
                            + clima.getTemperaturaMinima() + " °C");

            lblUmidade.setText(
                    "Umidade: "
                            + clima.getUmidade() + "%");

            lblCondicao.setText(
                    "Condição: "
                            + clima.getCondicao());

            lblPrecipitacao.setText(
                    "Precipitação: "
                            + clima.getPrecipitacao() + " mm");

            lblVelocidadeVento.setText(
                    "Velocidade do Vento: "
                            + clima.getVelocidadeVento() + " km/h");

            lblDirecaoVento.setText(
                    "Direção do Vento: "
                            + clima.getDirecaoVento() + "°");

        } catch (Exception erro) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao consultar o clima.\nVerifique o nome da cidade.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}