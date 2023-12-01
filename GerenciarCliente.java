import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class GerenciarCliente {

  public void gerenciarCliente(Scanner sc, Cliente cliente, ArrayList<Fisica> fisica, ArrayList<Juridica> juridica) {

    // String URL = "jdbc:postgresql://localhost:5432/Locadora";
    // String USUARIO = "postgres";
    // String SENHA = "121255qq";

    String conn = "jdbc:postgresql://localhost:5432/Locadora?user=postgres&password=121255qq";
    // Carregar o driver JDBC do PostgreSQL
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    int opcao;
    do {
      System.out.println("1 - Cadastrar cliente");
      System.out.println("2 - Editar cliente");
      System.out.println("3 - Excluir cliente");
      System.out.println("4 - Listar cliente");
      System.out.println("5 - Buscar cliente");
      System.out.println("0 - Voltar");
      opcao = sc.nextInt();
      sc.nextLine();

      if (opcao == 0) {
        break;
      }

      switch (opcao) {
        case 1:
          cadastrarCliente(sc, conn);
          break;
        case 2:
          alterarCliente(sc, fisica, juridica, conn);
          break;
        case 3:
          excluirCliente(sc, conn);
          break;
        case 4:
          listarClientes(sc, conn);
          break;
        case 5:
          buscarCliente(sc, conn);
          break;
        default:
          System.out.println("Opção inválida.");
          break;
      }

    } while (opcao != 0);
  }

  public void cadastrarCliente(Scanner sc, String conn) {
    LocalDate dataNascimento = null;
    String sql = null;

    try (Connection connection = DriverManager.getConnection(conn)) {

      System.out.println("Digite o nome/razao social do cliente: ");
      String nome = sc.nextLine();

      System.out.println("Digite o endereço: ");
      String endereco = sc.nextLine();

      System.out.println("Digite o CPF/CNPJ do cliente: ");
      String cpfCnpj = sc.nextLine();

      System.out.println("Digite o telefone do cliente: ");
      String telefone = sc.nextLine();

      if (cpfCnpj.length() == 11) {

        System.out.println("Digite a data de nascimento (no formato dd/MM/yyyy): ");
        String dataNascimentoStr = sc.nextLine();

        // Utilize um DateTimeFormatter para fazer o parsing da data
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dataNascimento = LocalDate.parse(dataNascimentoStr, formatter);

        sql = "INSERT INTO Fisica (nome, endereco, telefone, cpfCnpj, dataNascimento) VALUES (?, ?, ?, ?,?)";
      } else if (cpfCnpj.length() == 14) {

        sql = "INSERT INTO Juridica (nome, endereco, telefone, cpfCnpj) VALUES (?, ?, ?, ?)";
      } else {
        System.out.println("CPF/CNPJ inválido.");
        return; // Encerrar o método se o CPF/CNPJ for inválido
      }

      try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        // Atribuir valores aos parâmetros
        preparedStatement.setString(1, nome);
        preparedStatement.setString(2, endereco);
        preparedStatement.setString(3, telefone);

        // Atribuir a data de nascimento ou razão social, dependendo do tamanho do
        // CPF/CNPJ
        if (cpfCnpj.length() == 11) {
          preparedStatement.setString(4, cpfCnpj);
          preparedStatement.setDate(5, Date.valueOf(dataNascimento));
        } else if (cpfCnpj.length() == 14) {
          preparedStatement.setString(4, cpfCnpj);
        }

        // Executar a consulta
        int resultado = preparedStatement.executeUpdate();

        if (resultado > 0) {
          System.out.println("Cliente cadastrado com sucesso!");
        } else {
          System.out.println("Erro ao cadastrar o cliente.");
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void alterarCliente(Scanner sc, ArrayList<Fisica> fisica, ArrayList<Juridica> juridica, String conn) {
    String sql = null;
    String nome = null;
    String endereco = null;
    String telefone = null;
    LocalDate dataNascimento = null;
    String razaoSocial = null;
    java.sql.Date sqlDate = null;

    System.out.println("Digite o CPF/CNPJ do cliente que deseja editar: ");
    String cpfCnpj = sc.nextLine();

    // Verificar se o CPF/CNPJ existe no banco de dados
    if (!clienteExiste(cpfCnpj, conn)) {
      System.out.println("Cliente com CPF/CNPJ informado não encontrado.");
      return;
    } else {
      if (cpfCnpj.length() == 11) {
        System.out.println("Digite o nome do cliente: ");
        nome = sc.nextLine();

        System.out.println("Digite o endereço: ");
        endereco = sc.nextLine();

        System.out.println("Digite o telefone do cliente: ");
        telefone = sc.nextLine();

        System.out.println("Digite a data de nascimento (no formato dd/MM/yyyy): ");
        String dataNascimentoStr = sc.nextLine();

        // Utilize um DateTimeFormatter para fazer o parsing da data
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dataNascimento = LocalDate.parse(dataNascimentoStr, formatter);
        sqlDate = java.sql.Date.valueOf(dataNascimento);

        sql = "UPDATE Fisica SET nome = ?, endereco = ?, telefone = ?, dataNascimento = ? WHERE cpfCnpj = ?";
      } else if (cpfCnpj.length() == 14) {
        System.out.println("Digite a razão social do cliente: ");
        razaoSocial = sc.nextLine();

        System.out.println("Digite o endereço: ");
        endereco = sc.nextLine();

        System.out.println("Digite o telefone do cliente: ");
        telefone = sc.nextLine();

        sql = "UPDATE Juridica SET nome = ?, endereco = ?, telefone = ? WHERE cpfCnpj = ?";
      } else {
        System.out.println("CPF/CNPJ inválido.");
        return;
      }
    }

    try (Connection connection = DriverManager.getConnection(conn);
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      // Setar os parâmetros com base nos valores coletados
      if (cpfCnpj.length() == 11) {
        preparedStatement.setString(1, nome);
        preparedStatement.setString(2, endereco);
        preparedStatement.setString(3, telefone);
        preparedStatement.setDate(4, sqlDate);
        preparedStatement.setString(5, cpfCnpj);
      } else if (cpfCnpj.length() == 14) {
        preparedStatement.setString(1, razaoSocial);
        preparedStatement.setString(2, endereco);
        preparedStatement.setString(3, telefone);
        preparedStatement.setString(4, cpfCnpj);
      }

      // Executar a atualização
      int linhasAfetadas = preparedStatement.executeUpdate();
      if (linhasAfetadas > 0) {
        System.out.println("Informações do cliente atualizadas com sucesso!");
      } else {
        System.out.println("Falha ao atualizar as informações do cliente.");
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private boolean clienteExiste(String cpfCnpj, String conn) {

    if (cpfCnpj.length() == 11) {

      String sql = "SELECT COUNT(*) FROM Fisica WHERE cpfCnpj = ?  ";

      try (Connection connection = DriverManager.getConnection(conn);
          PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

        preparedStatement.setString(1, cpfCnpj);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
          if (resultSet.next()) {
            int count = resultSet.getInt(1);
            return count > 0;
          }
        }

      } catch (SQLException e) {
        e.printStackTrace();
      }

    } else if (cpfCnpj.length() == 14) {

      String sql = "SELECT COUNT(*) FROM Juridica WHERE cpfCnpj = ?";

      try (Connection connection = DriverManager.getConnection(conn);
          PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

        preparedStatement.setString(1, cpfCnpj);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
          if (resultSet.next()) {
            int count = resultSet.getInt(1);
            return count > 0;
          }
        }

      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    return false;
  }

  public void excluirCliente(Scanner sc, String conn) {
    System.out.println("Digite o CPF/CNPJ do cliente que deseja excluir: ");
    String cpfCnpj = sc.nextLine();

    String sql = null;

    if (cpfCnpj.length() == 11) {
      sql = "DELETE FROM Fisica WHERE cpfCnpj = ?";
    } else if (cpfCnpj.length() == 14) {
      sql = "DELETE FROM Juridica WHERE cpfCnpj = ?";
    } else {
      System.out.println("CPF/CNPJ inválido.");
      return;
    }

    try (Connection connection = DriverManager.getConnection(conn);
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      preparedStatement.setString(1, cpfCnpj);

      int linhasAfetadas = preparedStatement.executeUpdate();

      if (linhasAfetadas > 0) {
        System.out.println("Cliente excluído com sucesso!");
      } else {
        System.out.println("Cliente não encontrado ou falha ao excluir.");
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void listarClientes(Scanner sc, String conn) {

    System.out.println("1 - Pessoa Física");
    System.out.println("2 - Pessoa Jurídica");
    System.out.println("0 - Voltar");
    int opcao = sc.nextInt();
    sc.nextLine();

    if (opcao == 0) {
      return;
    }

    String sql = null;

    try (Connection connection = DriverManager.getConnection(conn);
        Statement statement = connection.createStatement()) {

      if (opcao == 1) {
        sql = "SELECT * FROM Fisica";
      } else if (opcao == 2) {
        sql = "SELECT * FROM Juridica";
      } else {
        System.out.println("Opção inválida.");
        return;
      }

      try (ResultSet resultSet = statement.executeQuery(sql)) {
        while (resultSet.next()) {
          if (opcao == 1) {
            System.out.println("Nome: " + resultSet.getString("nome"));
            System.out.println("Endereço: " + resultSet.getString("endereco"));
            System.out.println("Telefone: " + resultSet.getString("telefone"));
            System.out.println("Data de nascimento: " + resultSet.getDate("dataNascimento"));
            System.out.println("CPF: " + resultSet.getString("cpfCnpj"));
          } else if (opcao == 2) {
            System.out.println("Nome: " + resultSet.getString("nome"));
            System.out.println("Endereço: " + resultSet.getString("endereco"));
            System.out.println("Telefone: " + resultSet.getString("telefone"));
            System.out.println("CNPJ: " + resultSet.getString("cpfCnpj"));
          }
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void buscarCliente(Scanner sc, String conn) {

    System.out.println("1 - Pessoa Física");
    System.out.println("2 - Pessoa Jurídica");
    System.out.println("0 - Voltar");
    int opcao = sc.nextInt();
    sc.nextLine();

    if (opcao == 0) {
      return;
    }

    String sql = null;

    try (Connection connection = DriverManager.getConnection(conn);
        Statement statement = connection.createStatement()) {

      if (opcao == 1) {
        sql = "SELECT * FROM Fisica";
      } else if (opcao == 2) {
        sql = "SELECT * FROM Juridica";
      } else {
        System.out.println("Opção inválida.");
        return;
      }

      try (ResultSet resultSet = statement.executeQuery(sql)) {
        while (resultSet.next()) {
          if (opcao == 1) {

            System.out.println("Digite o CPF do cliente que deseja buscar: ");
            String cpf = sc.nextLine();

            if (cpf.equals(resultSet.getString("cpfCnpj"))) {
              System.out.println("Nome: " + resultSet.getString("nome"));
              System.out.println("Endereço: " + resultSet.getString("endereco"));
              System.out.println("Telefone: " + resultSet.getString("telefone"));
              System.out.println("Data de nascimento: " + resultSet.getDate("dataNascimento"));
              System.out.println("CPF: " + resultSet.getString("cpfCnpj"));
            } else if (opcao == 2) {
              if (cpf.equals(resultSet.getString("cpfCnpj"))) {

                System.out.println("Nome: " + resultSet.getString("nome"));
                System.out.println("Endereço: " + resultSet.getString("endereco"));
                System.out.println("Telefone: " + resultSet.getString("telefone"));
                System.out.println("CNPJ: " + resultSet.getString("cpfCnpj"));
              }
            }
          }
        }

      } catch (SQLException e) {
        e.printStackTrace();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

}
