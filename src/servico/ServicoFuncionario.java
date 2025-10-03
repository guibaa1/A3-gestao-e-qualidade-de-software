package servico;

import repositorio.RepositorioFuncionario;
import java.sql.*;
import java.util.Scanner;

public class ServicoFuncionario {
    static Scanner scanner = new Scanner(System.in);

    public static void cadastrarFuncionario() {
        System.out.println("\nCadastro de Funcionário");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("CPF (11 dígitos): ");
        String cpf = scanner.nextLine();

        if (cpf.length() != 11 || !cpf.matches("\\d{11}")) {
            System.out.println("Erro: CPF deve ter exatamente 11 dígitos numéricos!");
            return;
        }
    RepositorioFuncionario.SalvarFuncionario(nome, cpf);

}

    public static void listarFuncionarios() {
        System.out.println("\nLista de Funcionários");

        RepositorioFuncionario.ListarFuncionarios();
    }

    public static void atualizarFuncionario() {
        listarFuncionarios();

        System.out.print("\nInforme o ID do funcionário que deseja atualizar: ");
        int id;
        try {
            id = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Erro: ID inválido!");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();

        RepositorioFuncionario.AtualizarFuncionarioPorId(id);
    }

    public static void removerFuncionario() {
        listarFuncionarios();

        System.out.print("\nInforme o ID do funcionário que deseja remover: ");
        int id;
        try {
            id = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Erro: ID inválido!");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();

        RepositorioFuncionario.DeletarFuncionarPorId(id);
    }
}
