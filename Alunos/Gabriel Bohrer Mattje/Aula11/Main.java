import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Font;

public class Main {

    public static void main(String[] args) {

        JFrame janela =
                new JFrame("Sistema de Consulta Climática");

        janela.setSize(600, 450);

        janela.setLocationRelativeTo(null);

        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel painelSuperior =
                new JPanel();

        JLabel labelCidade =
                new JLabel("Cidade:");

        JTextField campoCidade =
                new JTextField(20);

        JButton botaoConsultar =
                new JButton("Consultar");

        painelSuperior.add(labelCidade);

        painelSuperior.add(campoCidade);

        painelSuperior.add(botaoConsultar);

        JTextArea areaResultado =
                new JTextArea();

        areaResultado.setEditable(false);

        areaResultado.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        JScrollPane painelRolagem =
                new JScrollPane(areaResultado);

        janela.add(
                painelSuperior,
                BorderLayout.NORTH
        );

        janela.add(
                painelRolagem,
                BorderLayout.CENTER
        );

        botaoConsultar.addActionListener(evento -> {

            String cidade =
                    campoCidade.getText().trim();

            if (cidade.isBlank()) {

                JOptionPane.showMessageDialog(
                        janela,
                        "Digite uma cidade."
                );

                return;
            }

            try {

                ClimaService climaService =
                        new ClimaService();

                Clima clima =
                        climaService.buscarClima(cidade);

                String resultado =
                        "CONSULTA CLIMÁTICA\n"
                                + "====================================\n\n"

                                + "Cidade: "
                                + cidade

                                + "\n\nTemperatura Atual: "
                                + String.format(
                                "%.1f",
                                clima.getTemperaturaAtual()
                        )
                                + " °C"

                                + "\nTemperatura Máxima: "
                                + String.format(
                                "%.1f",
                                clima.getTemperaturaMaxima()
                        )
                                + " °C"

                                + "\nTemperatura Mínima: "
                                + String.format(
                                "%.1f",
                                clima.getTemperaturaMinima()
                        )
                                + " °C"

                                + "\n\nUmidade do Ar: "
                                + String.format(
                                "%.0f",
                                clima.getUmidade()
                        )
                                + "%"

                                + "\nCondição do Tempo: "
                                + clima.getCondicaoTempo()

                                + "\nPrecipitação: "
                                + String.format(
                                "%.1f",
                                clima.getPrecipitacao()
                        )
                                + " mm"

                                + "\nVelocidade do Vento: "
                                + String.format(
                                "%.1f",
                                clima.getVelocidadeVento()
                        )
                                + " km/h"

                                + "\nDireção do Vento: "
                                + obterDirecaoVento(
                                clima.getDirecaoVento()
                        );

                areaResultado.setText(resultado);

            } catch (Exception erro) {

                JOptionPane.showMessageDialog(
                        janela,
                        "Cidade não encontrada :0",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        janela.setVisible(true);
    }

    private static String obterDirecaoVento(double graus) {

        if (graus >= 337.5 || graus < 22.5) {
            return "Norte";
        }

        if (graus < 67.5) {
            return "Nordeste";
        }

        if (graus < 112.5) {
            return "Leste";
        }

        if (graus < 157.5) {
            return "Sudeste";
        }

        if (graus < 202.5) {
            return "Sul";
        }

        if (graus < 247.5) {
            return "Sudoeste";
        }

        if (graus < 292.5) {
            return "Oeste";
        }

        return "Noroeste";
    }
}