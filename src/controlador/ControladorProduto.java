package controlador;

import java.util.Scanner;

import static servico.ServicoProduto.*;


public class ControladorProduto {
        static Scanner scanner = new Scanner(System.in);
        public static void menuProdutos() {

        int opcao;
        do {
            System.out.println("\n===== MENU PRODUTOS =====");
            System.out.println("1. Cadastrar entidade.Produto");
            System.out.println("2. Listar Produtos");
            System.out.println("3. Atualizar entidade.Produto");
            System.out.println("4. Remover entidade.Produto");
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
                    cadastrarProduto();
                    break;
                case 2:
                    listarProdutos();
                    break;
                case 3:
                    atualizarProduto();
                    break;
                case 4:
                    removerProduto();
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
