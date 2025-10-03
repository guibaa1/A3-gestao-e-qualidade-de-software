package servico;

import repositorio.RepositorioCliente;
import java.sql.*;
import java.util.Scanner;


public class ServicoCliente {
    static Scanner scanner = new Scanner(System.in);
    public static void cadastrarCliente() {
        System.out.println("\nCadastro de entidade.Cliente");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("CPF (11 dígitos): ");
        String cpf = scanner.nextLine();

        if (cpf.length() != 11 || !cpf.matches("\\d{11}")) {
            System.out.println("Erro: CPF deve ter exatamente 11 dígitos numéricos!");
            return;
        }

        RepositorioCliente.salvarCliente(nome, cpf);
    }

    public static void listarClientes() {
        System.out.println("\nLista de Clientes");

        RepositorioCliente.listarClientes();
    }

    public static void atualizarCliente() {
        listarClientes();

        System.out.print("\nInforme o ID do cliente que deseja atualizar: ");
        int id;
        try {
            id = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Erro: ID inválido!");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();

        RepositorioCliente.atualizarClientePorId(id);
    }

    public static void removerCliente() {
        listarClientes();

        System.out.print("\nInforme o ID do cliente que deseja remover: ");
        int id;
        try {
            id = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Erro: ID inválido!");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();

        RepositorioCliente.removerClientePorId(id);
    }
}
