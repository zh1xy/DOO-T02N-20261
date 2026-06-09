package weather;

import java.io.IOException;
import java.util.Scanner;

public class ClimaApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║         CONSULTA DE CLIMA - Visual Crossing  ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println();

        System.out.print("Informe sua API Key da Visual Crossing: ");
        String apiKey = scanner.nextLine().trim();

        if (apiKey.isEmpty()) {
            System.out.println("[ERRO] API Key não pode ser vazia.");
            System.out.println("Crie uma conta gratuita em: https://www.visualcrossing.com/sign-up");
            scanner.close();
            return;
        }

        Buscar buscar = new Buscar(apiKey);

        boolean continuar = true;
        while (continuar) {
            System.out.println();
            System.out.print("Digite o nome da cidade (ex.: Cascavel,PR,Brazil): ");
            String cidade = scanner.nextLine().trim();

            if (cidade.isEmpty()) {
                System.out.println("[AVISO] Nenhuma cidade informada. Tente novamente.");
                continue;
            }

            System.out.println("\nBuscando dados climáticos para \"" + cidade + "\"...\n");

            try {
                Dados dados = buscar.buscarClima(cidade);
                System.out.println(dados);
            } catch (ClimaException e) {
                System.out.println("[ERRO DA API] " + e.getMessage());
                System.out.println("Verifique se a cidade está escrita corretamente e se sua API Key é válida.");
            } catch (IOException e) {
                System.out.println("[ERRO DE REDE] Não foi possível conectar à API: " + e.getMessage());
                System.out.println("Verifique sua conexão com a internet e tente novamente.");
            }

            System.out.print("\nDeseja consultar outra cidade? (s/n): ");
            String resposta = scanner.nextLine().trim().toLowerCase();
            continuar = resposta.equals("s") || resposta.equals("sim");
        }

        System.out.println("\nEncerrando o aplicativo. Até logo!");
        scanner.close();
    }
}
