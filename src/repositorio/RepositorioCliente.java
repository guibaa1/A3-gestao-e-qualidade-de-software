package repositorio;
import entidade.Cliente;

import java.sql.*;
import java.util.Scanner;

import static db.BancoDeDados.*;

public class RepositorioCliente {
    static Scanner scanner = new Scanner(System.in);

    public static void salvarCliente(String nome, String cpf) {
        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA)) {
            String sql = "INSERT INTO Cliente (nome, cpf) VALUES (?, ?)";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, cpf);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("entidade.Cliente cadastrado com sucesso!");

                // MELHORADO: Buscar e exibir o cliente cadastrado
                try (PreparedStatement checkStmt = conexao.prepareStatement("SELECT id FROM Cliente WHERE nome = ? AND cpf = ? ORDER BY id DESC LIMIT 1")) {
                    checkStmt.setString(1, nome);
                    checkStmt.setString(2, cpf);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next()) {
                        int id = rs.getInt("id");
                        Cliente novoCliente = new Cliente(nome, cpf);
                        System.out.println("ID do cliente: " + id);
                        novoCliente.exibirDetalhes();
                    }
                }
            } else {
                System.out.println("Nenhum dado foi inserido.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void listarClientes(){
        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA)) {
            String sql = "SELECT * FROM Cliente";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");
                Cliente cliente = new Cliente(nome, cpf); // CORRIGIDO: Agora funciona
                System.out.println("\n[" + id + "]");
                cliente.exibirDetalhes(); // MELHORADO: Usar método exibirDetalhes()
            }

            if (!hasResults) {
                System.out.println("Nenhum cliente cadastrado");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void atualizarClientePorId(int id){
        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA)) {
            String checkSql = "SELECT nome, cpf FROM Cliente WHERE id = ?";
            PreparedStatement checkStmt = conexao.prepareStatement(checkSql);
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("entidade.Cliente com ID " + id + " não encontrado.");
                return;
            }

            String nomeAtual = rs.getString("nome");
            String cpfAtual = rs.getString("cpf");

            System.out.print("Novo nome (" + nomeAtual + "): ");
            String nome = scanner.nextLine();
            if (nome.trim().isEmpty()) nome = nomeAtual;

            System.out.print("Novo CPF (" + cpfAtual + "): ");
            String cpf = scanner.nextLine();
            if (cpf.trim().isEmpty()) cpf = cpfAtual;

            if (cpf.length() != 11 || !cpf.matches("\\d{11}")) {
                System.out.println("Erro: CPF deve ter exatamente 11 dígitos numéricos!");
                return;
            }

            String sql = "UPDATE Cliente SET nome = ?, cpf = ? WHERE id = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, cpf);
            stmt.setInt(3, id);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("entidade.Cliente atualizado com sucesso!");
            } else {
                System.out.println("Falha ao atualizar o cliente.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void removerClientePorId(int id){
        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA)) {
            String checkSql = "SELECT nome FROM Cliente WHERE id = ?";
            PreparedStatement checkStmt = conexao.prepareStatement(checkSql);
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("entidade.Cliente com ID " + id + " não encontrado.");
                return;
            }

            String nomeRemovido = rs.getString("nome");

            String sql = "DELETE FROM Cliente WHERE id = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, id);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("entidade.Cliente removido: " + nomeRemovido);
            } else {
                System.out.println("Falha ao remover o cliente.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao remover cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
