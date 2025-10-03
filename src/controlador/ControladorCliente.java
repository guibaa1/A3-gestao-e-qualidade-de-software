package controlador;
import java.util.Scanner;

import static servico.ServicoCliente.*;

public class ControladorCliente {
    static Scanner scanner = new Scanner(System.in);
   public static void menuClientes() {
        int opcao;
        do {
            System.out.println("\n===== MENU CLIENTES =====");
            System.out.println("1. Cadastrar entidade.Cliente");
            System.out.println("2. Listar Clientes");
            System.out.println("3. Atualizar entidade.Cliente"); // ADICIONADO
            System.out.println("4. Remover entidade.Cliente");
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
                    cadastrarCliente();
                    break;
                case 2:
                    listarClientes();
                    break;
                case 3:
                    atualizarCliente();
                    break;
                case 4:
                    removerCliente();
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
