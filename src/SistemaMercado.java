import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class SistemaMercado {

    static Scanner scanner = new Scanner(System.in);
    static final String URL = "jdbc:mysql://localhost:3306/PROJETOA3";
    static final String USUARIO = "root";
    static final String SENHA = "root";

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

    static void menuProdutos() {
        int opcao;
        do {
            System.out.println("\n===== MENU PRODUTOS =====");
            System.out.println("1. Cadastrar Produto");
            System.out.println("2. Listar Produtos");
            System.out.println("3. Atualizar Produto");
            System.out.println("4. Remover Produto");
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

    static void cadastrarProduto() {
        System.out.println("\nCadastro de Produto");

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

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA)) {
            String sql = "INSERT INTO Produto (nome, categoria, preco, quantDeEstoque) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, categoria);
            stmt.setDouble(3, preco);
            stmt.setInt(4, estoque);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Produto cadastrado com sucesso!");
            } else {
                System.out.println("Nenhum dado foi inserido.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar produto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    static void listarProdutos() {
        System.out.println("\nLista de Produtos");

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

    static void atualizarProduto() {
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

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA)) {
            String checkSql = "SELECT * FROM Produto WHERE id = ?";
            PreparedStatement checkStmt = conexao.prepareStatement(checkSql);
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Produto com ID " + id + " não encontrado.");
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
                System.out.println("Produto atualizado com sucesso!");
            } else {
                System.out.println("Falha ao atualizar o produto.");
            }
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Erro ao atualizar produto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    static void removerProduto() {
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

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA)) {
            String checkSql = "SELECT nome FROM Produto WHERE id = ?";
            PreparedStatement checkStmt = conexao.prepareStatement(checkSql);
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Produto com ID " + id + " não encontrado.");
                return;
            }

            String nomeRemovido = rs.getString("nome");

            String sql = "DELETE FROM Produto WHERE id = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, id);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Produto removido: " + nomeRemovido);
            } else {
                System.out.println("Falha ao remover o produto.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao remover produto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    static void menuFuncionarios() {
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
                    atualizarFuncionario(); // ADICIONADO
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

    static void cadastrarFuncionario() {
        System.out.println("\nCadastro de Funcionário");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("CPF (11 dígitos): ");
        String cpf = scanner.nextLine();

        if (cpf.length() != 11 || !cpf.matches("\\d{11}")) {
            System.out.println("Erro: CPF deve ter exatamente 11 dígitos numéricos!");
            return;
        }

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA)) {
            String sql = "INSERT INTO Funcionario (nome, cpf) VALUES (?, ?)";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, cpf);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Funcionário cadastrado com sucesso!");
                
                try (PreparedStatement checkStmt = conexao.prepareStatement("SELECT id FROM Funcionario WHERE nome = ? AND cpf = ? ORDER BY id DESC LIMIT 1")) {
                    checkStmt.setString(1, nome);
                    checkStmt.setString(2, cpf);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next()) {
                        int id = rs.getInt("id");
                        Funcionario novoFuncionario = new Funcionario(nome, cpf);
                        System.out.println("ID do funcionário: " + id);
                        novoFuncionario.exibirDetalhes();
                    }
                }
            } else {
                System.out.println("Nenhum dado foi inserido.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar funcionário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    static void listarFuncionarios() {
        System.out.println("\nLista de Funcionários");

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA)) {
            String sql = "SELECT * FROM Funcionario";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");
                Funcionario funcionario = new Funcionario(nome, cpf);
                System.out.println("\n[" + id + "]");
                funcionario.exibirDetalhes();
            }

            if (!hasResults) {
                System.out.println("Nenhum funcionário cadastrado");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar funcionários: " + e.getMessage());
            e.printStackTrace();
        }
    }

    static void atualizarFuncionario() {
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

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA)) {
            String checkSql = "SELECT nome, cpf FROM Funcionario WHERE id = ?";
            PreparedStatement checkStmt = conexao.prepareStatement(checkSql);
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Funcionário com ID " + id + " não encontrado.");
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

            String sql = "UPDATE Funcionario SET nome = ?, cpf = ? WHERE id = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, cpf);
            stmt.setInt(3, id);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Funcionário atualizado com sucesso!");
            } else {
                System.out.println("Falha ao atualizar o funcionário.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar funcionário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    static void removerFuncionario() {
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

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA)) {
            String checkSql = "SELECT nome FROM Funcionario WHERE id = ?";
            PreparedStatement checkStmt = conexao.prepareStatement(checkSql);
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Funcionário com ID " + id + " não encontrado.");
                return;
            }

            String nomeRemovido = rs.getString("nome");

            String sql = "DELETE FROM Funcionario WHERE id = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, id);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Funcionário removido: " + nomeRemovido);
            } else {
                System.out.println("Falha ao remover o funcionário.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao remover funcionário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    static void menuClientes() {
        int opcao;
        do {
            System.out.println("\n===== MENU CLIENTES =====");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Listar Clientes");
            System.out.println("3. Atualizar Cliente"); // ADICIONADO
            System.out.println("4. Remover Cliente");
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
                    atualizarCliente(); // ADICIONADO
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

    static void cadastrarCliente() {
        System.out.println("\nCadastro de Cliente");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("CPF (11 dígitos): ");
        String cpf = scanner.nextLine();

        if (cpf.length() != 11 || !cpf.matches("\\d{11}")) {
            System.out.println("Erro: CPF deve ter exatamente 11 dígitos numéricos!");
            return;
        }

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA)) {
            String sql = "INSERT INTO Cliente (nome, cpf) VALUES (?, ?)";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, cpf);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Cliente cadastrado com sucesso!");
                
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

    static void listarClientes() {
        System.out.println("\nLista de Clientes");

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

    // ADICIONADO: Método para atualizar cliente
    static void atualizarCliente() {
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

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA)) {
            String checkSql = "SELECT nome, cpf FROM Cliente WHERE id = ?";
            PreparedStatement checkStmt = conexao.prepareStatement(checkSql);
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Cliente com ID " + id + " não encontrado.");
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
                System.out.println("Cliente atualizado com sucesso!");
            } else {
                System.out.println("Falha ao atualizar o cliente.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    static void removerCliente() {
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

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA)) {
            String checkSql = "SELECT nome FROM Cliente WHERE id = ?";
            PreparedStatement checkStmt = conexao.prepareStatement(checkSql);
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Cliente com ID " + id + " não encontrado.");
                return;
            }

            String nomeRemovido = rs.getString("nome");

            String sql = "DELETE FROM Cliente WHERE id = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, id);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Cliente removido: " + nomeRemovido);
            } else {
                System.out.println("Falha ao remover o cliente.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao remover cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}