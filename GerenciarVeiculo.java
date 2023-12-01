import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GerenciarVeiculo {

  public void gerenciarVeiculos(Scanner sc, ArrayList<Carro> carros, ArrayList<Caminhao> caminhoes,
      Veiculo veiculos) {

    String conn = "jdbc:postgresql://localhost:5432/Locadora?user=postgres&password=121255qq";

    int opcao;

    do {

      System.out.println("1 - Cadastrar veiculo");
      System.out.println("2 - Altera veiculo");
      System.out.println("3 - Excluir veiculo");
      System.out.println("4 - Listar veiculo");
      System.out.println("5 - Buscar veiculo");
      System.out.println("0 - Voltar");
      opcao = sc.nextInt();
      sc.nextLine();
      switch (opcao) {
        case 1:
          cadastrarVeiculo(sc, carros, caminhoes, conn);
          break;
        case 2:
          alterarVeiculo(sc, carros, caminhoes, conn);
          break;
        case 3:
          excluirVeiculo(sc, carros, caminhoes, conn);
          break;
        case 4:
          listarVeiculos(sc, carros, caminhoes, conn);
          break;
        case 5:
          buscarVeiculo(sc, carros, caminhoes, conn);
          break;
        case 0:
          System.out.println("Voltando...");
          break;
        default:
          System.out.println("Opcao invalida.");
          break;
      }
    } while (opcao != 0);
  }

  public void cadastrarVeiculo(Scanner sc, ArrayList<Carro> carros, ArrayList<Caminhao> caminhoes, String conn) {

    String sql = null;
    int capacidadePassageiros = 0;
    int numeroPortas = 0;
    float capacidadeCarga = 0;
    int numeroEixos = 0;

    System.out.println("Digite a Marca do veiculo: ");
    String marca = sc.nextLine();

    System.out.println("Digite o Modelo do veiculo: ");
    String modelo = sc.nextLine();

    System.out.println("Digite o Ano de fabricação do veiculo: ");
    int ano = sc.nextInt();
    sc.nextLine();

    System.out.println("Digite o Ano do modelo do veiculo: ");
    int anoModelo = sc.nextInt();
    sc.nextLine();

    System.out.println("Digite a Placa do veiculo: ");
    String placa = sc.nextLine();

    System.out.println("1 - Carro");
    System.out.println("2 - Caminhao");
    int opcao = sc.nextInt();
    sc.nextLine();

    try {
      if (opcao == 1) {
        System.out.println("Digite a capacidade de passageiros");
        capacidadePassageiros = sc.nextInt();
        sc.nextLine();

        System.out.println("Digite o numero de portas");
        numeroPortas = sc.nextInt();
        sc.nextLine();

        sql = "INSERT INTO Carro (marca, modelo, anofabricacao, anomodelo, placa, capacidadepassageiros, numeroportas) VALUES (?, ?, ?, ?, ?, ?, ?)";
      } else if (opcao == 2) {
        System.out.println("Digite a capacidade de carga");
        capacidadeCarga = sc.nextFloat();
        sc.nextLine();

        System.out.println("Digite o numero de eixos");
        numeroEixos = sc.nextInt();
        sc.nextLine();

        sql = "INSERT INTO Caminhao (marca, modelo, ano_fabricacao, ano_modelo, placa, capacidade_carga, numero_eixos) VALUES (?, ?, ?, ?, ?, ?, ?)";
      } else {
        System.out.println("Opcao invalida.");
        return;
      }

      // Abrir uma conexão com o banco de dados.
      try (Connection conexao = DriverManager.getConnection(conn)) {
        // Criar um preparedStatement para executar uma query.
        try (PreparedStatement preparedStatement = conexao.prepareStatement(sql)) {
          // Atribuir valores aos parâmetros conforme capturado do usuário
          preparedStatement.setString(1, marca);
          preparedStatement.setString(2, modelo);
          preparedStatement.setInt(3, ano);
          preparedStatement.setInt(4, anoModelo);
          preparedStatement.setString(5, placa);

          if (opcao == 1) {
            preparedStatement.setInt(6, capacidadePassageiros);
            preparedStatement.setInt(7, numeroPortas);
          } else if (opcao == 2) {
            preparedStatement.setFloat(6, capacidadeCarga);
            preparedStatement.setInt(7, numeroEixos);
          }

          // Executar a consulta
          int resultado = preparedStatement.executeUpdate();

          if (resultado > 0) {
            System.out.println("Veículo cadastrado com sucesso!");
          } else {
            System.out.println("Erro ao cadastrar o veículo.");
          }
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      System.out.println("Erro ao cadastrar o veículo.");
    }
  }

  public void alterarVeiculo(Scanner sc, ArrayList<Carro> carros, ArrayList<Caminhao> caminhoes, String conn) {
    System.out.println("1 - Carro");
    System.out.println("2 - Caminhao");
    System.out.println("0 - Voltar");
    int opcao = sc.nextInt();
    sc.nextLine();

    if (opcao == 0) {
      System.out.println("Voltando...");
      return;
    }

    System.out.println("Digite a placa do veiculo: ");
    String buscarPlaca = sc.nextLine();

    String sqlUpdate = "";
    if (opcao == 1) {
      sqlUpdate = "UPDATE Carro SET marca=?, modelo=?, anofabricacao=?, anomodelo=?, placa=?, capacidadepassageiros=?, numeroportas=? WHERE placa=?";
    } else if (opcao == 2) {
      sqlUpdate = "UPDATE Caminhao SET marca=?, modelo=?, anofabricacao=?, anomodelo=?, placa=?, capacidadecarga=?, numeroeixos=? WHERE placa=?";
    } else {
      System.out.println("Opcao invalida.");
      return;
    }

    try (Connection conexao = DriverManager.getConnection(conn);
        PreparedStatement preparedStatement = conexao.prepareStatement(sqlUpdate)) {

      // Solicitar ao usuário os novos valores
      System.out.println("Digite a nova Marca do veiculo: ");
      String marca = sc.nextLine();

      System.out.println("Digite o novo Modelo do veiculo: ");
      String modelo = sc.nextLine();

      System.out.println("Digite o novo Ano de fabricação do veiculo: ");
      int ano = sc.nextInt();
      sc.nextLine();

      System.out.println("Digite o novo Ano do modelo do veiculo: ");
      int anoModelo = sc.nextInt();
      sc.nextLine();

      System.out.println("Digite a nova Placa do veiculo: ");
      String novaPlaca = sc.nextLine();

      if (!buscarPlaca.equals(novaPlaca)) {
        System.out.println("A placa não pode ser alterada.");
        return;
      }

      if (opcao == 1) {
        System.out.println("Digite a nova capacidade de passageiros");
        int capacidadePassageiros = sc.nextInt();
        sc.nextLine();

        System.out.println("Digite o novo numero de portas");
        int numeroPortas = sc.nextInt();
        sc.nextLine();

        preparedStatement.setInt(6, capacidadePassageiros);
        preparedStatement.setInt(7, numeroPortas);
      } else if (opcao == 2) {
        System.out.println("Digite a nova capacidade de carga");
        float capacidadeCarga = sc.nextFloat();
        sc.nextLine();

        System.out.println("Digite o novo numero de eixos");
        int numeroEixos = sc.nextInt();
        sc.nextLine();

        preparedStatement.setFloat(6, capacidadeCarga);
        preparedStatement.setInt(7, numeroEixos);
      }

      // Atribuir valores aos parâmetros conforme capturado do usuário
      preparedStatement.setString(1, marca);
      preparedStatement.setString(2, modelo);
      preparedStatement.setInt(3, ano);
      preparedStatement.setInt(4, anoModelo);
      preparedStatement.setString(5, novaPlaca);
      preparedStatement.setString(8, buscarPlaca);

      // Executar a atualização
      int resultado = preparedStatement.executeUpdate();

      if (resultado > 0) {
        System.out.println("Veículo alterado com sucesso!");
      } else {
        System.out.println("Veículo não encontrado ou erro ao alterar.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void excluirVeiculo(Scanner sc, ArrayList<Carro> carros, ArrayList<Caminhao> caminhoes, String conn) {
    listarVeiculos(sc, carros, caminhoes, conn);
    System.out.println("1 - Carro");
    System.out.println("2 - Caminhao");
    System.out.println("0 - Voltar");
    int opcao = sc.nextInt();
    sc.nextLine();

    if (opcao == 0) {
      System.out.println("Voltando...");
      return;
    }

    System.out.println("Digite a placa do veiculo: ");
    String buscarPlaca = sc.nextLine();

    String sqlDelete = "";
    if (opcao == 1) {
      sqlDelete = "DELETE FROM Carro WHERE placa=?";
    } else if (opcao == 2) {
      sqlDelete = "DELETE FROM Caminhao WHERE placa=?";
    } else {
      System.out.println("Opcao invalida.");
      return;
    }

    try (Connection conexao = DriverManager.getConnection(conn);
        PreparedStatement preparedStatement = conexao.prepareStatement(sqlDelete)) {

      // Atribuir a placa como parâmetro na consulta SQL
      preparedStatement.setString(1, buscarPlaca);

      // Executar a exclusão
      int resultado = preparedStatement.executeUpdate();

      if (resultado > 0) {
        System.out.println("Veículo excluído com sucesso!");
      } else {
        System.out.println("Veículo não encontrado ou erro ao excluir.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  public void listarVeiculos(Scanner sc, ArrayList<Carro> carros, ArrayList<Caminhao> caminhoes, String conn) {

    System.out.println("1 - Carro");
    System.out.println("2 - Caminhao");
    System.out.println("0 - Voltar");
    int opcao = sc.nextInt();
    sc.nextLine();

    if (opcao == 0) {
      System.out.println("Voltando...");
      return;
    }

    String sql = "";
    if (opcao == 1) {
      sql = "SELECT * FROM Carro";
    } else if (opcao == 2) {
      sql = "SELECT * FROM Caminhao";
    } else {
      System.out.println("Opcao invalida.");
      return;
    }

    try (Connection conexao = DriverManager.getConnection(conn);
        PreparedStatement preparedStatement = conexao.prepareStatement(sql)) {

      // Executar a consulta
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        // Exibir os resultados na tela
        while (resultSet.next()) {
          System.out.println("Marca: " + resultSet.getString("marca"));
          System.out.println("Modelo: " + resultSet.getString("modelo"));
          System.out.println("Ano de fabricação: " + resultSet.getInt("anofabricacao"));
          System.out.println("Ano do modelo: " + resultSet.getInt("anomodelo"));
          System.out.println("Placa: " + resultSet.getString("placa"));

          if (opcao == 1) {
            System.out.println("Capacidade de passageiros: " + resultSet.getInt("capacidadepassageiros"));
            System.out.println("Numero de portas: " + resultSet.getInt("numeroportas"));
          } else if (opcao == 2) {
            System.out.println("Capacidade de carga: " + resultSet.getFloat("capacidadecarga"));
            System.out.println("Numero de eixos: " + resultSet.getInt("numeroeixos"));
          }

          System.out.println();
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void buscarVeiculo(Scanner sc, ArrayList<Carro> carros, ArrayList<Caminhao> caminhoes, String conn) {

    System.out.println("1 - Carro");
    System.out.println("2 - Caminhao");
    System.out.println("0 - Voltar");
    int opcao = sc.nextInt();
    sc.nextLine();

    if (opcao == 0) {
      System.out.println("Voltando...");
      return;
    }

    System.out.println("Digite a placa do veiculo: ");
    String buscarPlaca = sc.nextLine();

    String sql = "";
    if (opcao == 1) {
      sql = "SELECT * FROM Carro WHERE placa=?";
    } else if (opcao == 2) {
      sql = "SELECT * FROM Caminhao WHERE placa=?";
    } else {
      System.out.println("Opcao invalida.");
      return;
    }

    try (Connection conexao = DriverManager.getConnection(conn)) {
      try (PreparedStatement preparedStatement = conexao.prepareStatement(sql)) {
        // Atribuir a placa como parâmetro na consulta SQL
        preparedStatement.setString(1, buscarPlaca);

        // Executar a consulta
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
          // Exibir os resultados na tela
          while (resultSet.next()) {
            if (resultSet.getString("placa").equals(buscarPlaca)) {
              System.out.println("Marca: " + resultSet.getString("marca"));
              System.out.println("Modelo: " + resultSet.getString("modelo"));
              System.out.println("Ano de fabricação: " + resultSet.getInt("anofabricacao"));
              System.out.println("Ano do modelo: " + resultSet.getInt("anomodelo"));
              System.out.println("Placa: " + resultSet.getString("placa"));

              if (opcao == 1) {
                System.out.println("Capacidade de passageiros: " + resultSet.getInt("capacidadepassageiros"));
                System.out.println("Numero de portas: " + resultSet.getInt("numeroportas"));
              } else if (opcao == 2) {
                System.out.println("Capacidade de carga: " + resultSet.getFloat("capacidadecarga"));
                System.out.println("Numero de eixos: " + resultSet.getInt("numeroeixos"));
              }
            }
          }
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
