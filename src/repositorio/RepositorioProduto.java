package repositorio;

import entidade.Produto;
import java.sql.*;
import java.util.Scanner;

import static db.BancoDeDados.*;

public class RepositorioProduto {


    static Scanner scanner = new Scanner(System.in);

    public static void SalvarUsuario(String nome, String categoria, double preco, int estoque) {
        // Implementar lógica de salvar produto no repositório


        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA)) {
            String sql = "INSERT INTO Produto (nome, categoria, preco, quantDeEstoque) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, categoria);
            stmt.setDouble(3, preco);
            stmt.setInt(4, estoque);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("entidade.Produto cadastrado com sucesso!");
            } else {
                System.out.println("Nenhum dado foi inserido.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar produto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void ListarProduto(){
        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA)) {
            String sql = "SELECT * FROM Produto";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String categoria = rs.getString("categoria");
                double preco = rs.getDouble("preco");
                int quantDeEstoque = rs.getInt("quantDeEstoque");
                Produto produto = new Produto(nome, categoria, preco, quantDeEstoque);
                System.out.println(id + " -> " + produto);
            }

            if (!hasResults) {
                System.out.println("Nenhum produto cadastrado");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar produtos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void AtualizarProdutoPorId(int id){
        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA)) {
            String checkSql = "SELECT * FROM Produto WHERE id = ?";
            PreparedStatement checkStmt = conexao.prepareStatement(checkSql);
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("entidade.Produto com ID " + id + " não encontrado.");
                return;
            }

            String nomeAtual = rs.getString("nome");
            String categoriaAtual = rs.getString("categoria");
            double precoAtual = rs.getDouble("preco");
            int estoqueAtual = rs.getInt("quantDeEstoque");

            System.out.print("Novo nome (" + nomeAtual + "): ");
            String nome = scanner.nextLine();
            if (nome.trim().isEmpty()) nome = nomeAtual;

            System.out.print("Nova categoria (" + categoriaAtual + "): ");
            String categoria = scanner.nextLine();
            if (categoria.trim().isEmpty()) categoria = categoriaAtual;

            System.out.print("Novo preço (R$" + precoAtual + "): ");
            String precoStr = scanner.nextLine();
            double preco = precoStr.trim().isEmpty() ? precoAtual : Double.parseDouble(precoStr);

            System.out.print("Nova quantidade (" + estoqueAtual + "): ");
            String estoqueStr = scanner.nextLine();
            int estoque = estoqueStr.trim().isEmpty() ? estoqueAtual : Integer.parseInt(estoqueStr);

            if (preco < 0) {
                System.out.println("Erro: O preço não pode ser negativo!");
                return;
            }
            if (estoque < 0) {
                System.out.println("Erro: A quantidade em estoque não pode ser negativa!");
                return;
            }

            String sql = "UPDATE Produto SET nome = ?, categoria = ?, preco = ?, quantDeEstoque = ? WHERE id = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, categoria);
            stmt.setDouble(3, preco);
            stmt.setInt(4, estoque);
            stmt.setInt(5, id);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("entidade.Produto atualizado com sucesso!");
            } else {
                System.out.println("Falha ao atualizar o produto.");
            }
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Erro ao atualizar produto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void RemoverProdutoPorId(int id){
        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA)) {
            String checkSql = "SELECT nome FROM Produto WHERE id = ?";
            PreparedStatement checkStmt = conexao.prepareStatement(checkSql);
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("entidade.Produto com ID " + id + " não encontrado.");
                return;
            }

            String nomeRemovido = rs.getString("nome");

            String sql = "DELETE FROM Produto WHERE id = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, id);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("entidade.Produto removido: " + nomeRemovido);
            } else {
                System.out.println("Falha ao remover o produto.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao remover produto: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
