import java.util.Scanner;

import static controlador.ControladorCliente.menuClientes;
import static controlador.ControladorFuncionario.menuFuncionarios;
import static controlador.ControladorProduto.menuProdutos;

public class SistemaMercado {

    static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("   SISTEMA DE GERENCIAMENTO     ");
        System.out.println("         DE MERCADO             ");
        System.out.println("=================================");

        int opcao;
        do {
            System.out.println("\n===== MENU PRINCIPAL =====");
            System.out.println("1. Gerenciar Produtos");
            System.out.println("2. Gerenciar Funcionários");
            System.out.println("3. Gerenciar Clientes");
            System.out.println("0. Sair");
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
                    menuProdutos();
                    break;
                case 2:
                    menuFuncionarios();
                    break;
                case 3:
                    menuClientes();
                    break;
                case 0:
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
        
        scanner.close(); 
    }


















}