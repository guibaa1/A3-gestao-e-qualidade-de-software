package controlador;

import java.util.Scanner;

import static servico.ServicoFuncionario.*;

public class ControladorFuncionario {
    static Scanner scanner = new Scanner(System.in);
    public static void menuFuncionarios() {
        int opcao;
        do {
            System.out.println("\n===== MENU FUNCIONÁRIOS =====");
            System.out.println("1. Cadastrar Funcionário");
            System.out.println("2. Listar Funcionários");
            System.out.println("3. Atualizar Funcionário"); // ADICIONADO
            System.out.println("4. Remover Funcionário");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Erro: Opção inválida!");
                scanner.nextLine();
                opcao = -1;
            }
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarFuncionario();
                    break;
                case 2:
                    listarFuncionarios();
                    break;
                case 3:
                    atualizarFuncionario();
                    break;
                case 4:
                    removerFuncionario();
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }
}
