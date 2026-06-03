package com.app;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    private JTextField txtCidade;

    private JTextArea txtResultado;

    private JButton btnBuscar;

    public TelaPrincipal() {

        setTitle("Consulta Climática");

        setSize(600, 400);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel painelSuperior =
                new JPanel();

        painelSuperior.add(
                new JLabel("Cidade:"));

        txtCidade =
                new JTextField(20);

        painelSuperior.add(txtCidade);

        btnBuscar =
                new JButton("Buscar");

        painelSuperior.add(btnBuscar);

        add(painelSuperior,
                BorderLayout.NORTH);

        txtResultado =
                new JTextArea();

        txtResultado.setEditable(false);

        add(new JScrollPane(txtResultado),
                BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> {

            String cidade =
                    txtCidade.getText();

            ClimaService service =
                    new ClimaService();

            Clima clima =
                    service.buscarClima(cidade);

            if (clima != null) {

                txtResultado.setText(

                        "Temperatura Atual: "
                                + clima.getTemperaturaAtual() + " °C\n\n"

                                + "Temperatura Máxima: "
                                + clima.getTemperaturaMaxima() + " °C\n\n"

                                + "Temperatura Mínima: "
                                + clima.getTemperaturaMinima() + " °C\n\n"

                                + "Umidade: "
                                + clima.getUmidade() + "%\n\n"

                                + "Condição: "
                                + clima.getCondicao() + "\n\n"

                                + "Precipitação: "
                                + clima.getPrecipitacao() + " mm\n\n"

                                + "Velocidade do Vento: "
                                + clima.getVelocidadeVento() + " km/h\n\n"

                                + "Direção do Vento: "
                                + clima.getDirecaoVento() + "°");
            }
        });
    }
}