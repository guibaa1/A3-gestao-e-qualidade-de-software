package servico;

import repositorio.RepositorioProduto;
import java.sql.*;
import java.util.Scanner;

public class ServicoProduto {
    static Scanner scanner = new Scanner(System.in);
   public static void cadastrarProduto() {
        System.out.println("\nCadastro de entidade.Produto");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Categoria: ");
        String categoria = scanner.nextLine();

        System.out.print("Preço: R$");
        double preco;
        try {
            preco = scanner.nextDouble();
        } catch (Exception e) {
            System.out.println("Erro: Preço inválido!");
            scanner.nextLine();
            return;
        }

        System.out.print("Quantidade em estoque: ");
        int estoque;
        try {
            estoque = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Erro: Quantidade inválida!");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();

        // ADICIONADO: Validações
        if (preco < 0) {
            System.out.println("Erro: O preço não pode ser negativo!");
            return;
        }
        if (estoque < 0) {
            System.out.println("Erro: A quantidade em estoque não pode ser negativa!");
            return;
        }

       RepositorioProduto.SalvarUsuario(nome, categoria, preco, estoque);

    }

   public static void listarProdutos() {
        System.out.println("\nLista de Produtos");

        RepositorioProduto.ListarProduto();
    }

   public static void atualizarProduto() {
        listarProdutos();

        System.out.print("\nInforme o ID do produto que deseja atualizar: ");
        int id;
        try {
            id = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Erro: ID inválido!");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();

        RepositorioProduto.AtualizarProdutoPorId(id);
    }

   public static void removerProduto() {
        listarProdutos();

        System.out.print("\nInforme o ID do produto que deseja remover: ");
        int id;
        try {
            id = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Erro: ID inválido!");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();

        RepositorioProduto.RemoverProdutoPorId(id);
    }

}


