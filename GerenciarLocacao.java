import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class GerenciarLocacao {

  public void gerenciarLocacao(Cliente cliente, ArrayList<Carro> carro, ArrayList<Caminhao> caminhao, Veiculo veiculos,
      Scanner sc, ArrayList<Locacao> locacao) {
    String conn = "jdbc:postgresql://localhost:5432/Locadora?user=postgres&password=121255qq";

    int opcao = 0;

    do {
      System.out.println("1 - Efetuar locação");
      System.out.println("2 - Alterar locação");
      System.out.println("3 - Listar locações");
      System.out.println("4 - Excluir locação");
      System.out.println("0 - Voltar");
      opcao = sc.nextInt();
      sc.nextLine();
      switch (opcao) {
        case 1:
          efetuarLocacao(cliente, carro, caminhao, sc, locacao, conn);
          break;
        case 2:
          // alterarLocacao(cliente, carro, caminhao, veiculos, sc, locacao, conn);
          break;
        case 3:
          listarLocacao(cliente, carro, caminhao, veiculos, sc, locacao, conn);
          break;
        case 4:
          // excluirLocacao(cliente, carro, caminhao, veiculos, sc, locacao, conn);
          break;
        case 0:
          break;
        default:
          break;
      }

    } while (opcao != 0);
  }

  public void efetuarLocacao(Cliente cliente, ArrayList<Carro> carros, ArrayList<Caminhao> caminhoes,
      Scanner sc, ArrayList<Locacao> locacoes, String conn) {

    LocalDate dataInicio = null;
    LocalDate dataPrevistaDevolucao = null;
    LocalDate dataDevolucao = null;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    try {

      exibirClientes(cliente, conn, sc);
      System.out.println("Digite o CPF/CNPJ do cliente:");
      String cpfcnpj = sc.nextLine();

      // Exibir veículos disponíveis apenas para carros, já que é um único método
      System.out.println("1 - Carro");
      System.out.println("2 - Caminhão");
      System.out.println("0 - Voltar");
      int opcao = sc.nextInt();
      sc.nextLine();

      if (opcao == 1) {

        try (Connection connection = DriverManager.getConnection(conn);
            PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM carro");
            ResultSet resultSet = preparedStatement.executeQuery()) {

          System.out.println("Carros disponíveis para locação:");

          while (resultSet.next()) {
            System.out.println("Modelo: " + resultSet.getString("modelo"));
            System.out.println("Placa: " + resultSet.getString("placa"));
          }
        } catch (Exception e) {
          System.out.println("Ocorreu um erro ao registrar a locação: " + e.getMessage());
        }
      } else if (opcao == 2) {

        try (Connection connection = DriverManager.getConnection(conn);
            PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM caminhao");
            ResultSet resultSet = preparedStatement.executeQuery()) {

          System.out.println("Caminhoes disponíveis para locação:");

          while (resultSet.next()) {
            System.out.println("Modelo: " + resultSet.getString("modelo"));
            System.out.println("Placa: " + resultSet.getString("placa"));
          }
        } catch (Exception e) {
          System.out.println("Ocorreu um erro ao registrar a locação: " + e.getMessage());
        }

      }

      System.out.println("Digite a placa do veículo:");
      String placaVeiculo = sc.nextLine();

      // Verificar se o veículo está alugado
      if (verificarVeiculoAlugado(opcao, placaVeiculo, conn, cpfcnpj)) {
        System.out.println("O veículo já está alugado.");
        return;
      }

      try {

        do {

          System.out.println("Digite a data de início da locação (no formato dd/MM/yyyy):");
          String dataInicioStr = sc.nextLine();
          formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
          // Faça o parse da string para um objeto LocalDate
          dataInicio = LocalDate.parse(dataInicioStr, formatter);
          if (dataInicio.isBefore(LocalDate.now()))
            System.out.println("A data de início deve ser posterior à data atual.");

        } while (dataInicio.isBefore(LocalDate.now()));

        do {

          System.out.println("Digite a data prevista para o término da locação (no formato dd/MM/yyyy):");
          String dataPrevistaStr = sc.nextLine();
          formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
          dataPrevistaDevolucao = LocalDate.parse(dataPrevistaStr, formatter);

          // Verificar se a data de início é anterior à data prevista de devolução
          if (dataInicio.isAfter(dataPrevistaDevolucao)) {
            System.out.println("A data de início deve ser anterior à data prevista de devolução.");
            // return;
          }

        } while (dataPrevistaDevolucao.isBefore(dataInicio));

        do {

          System.out.println("Digite a data de término da locação (no formato dd/MM/yyyy):");
          String dataDevolucaoStr = sc.nextLine();
          formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
          dataDevolucao = LocalDate.parse(dataDevolucaoStr, formatter);

          // Verificar se a data de devolução é anterior à data de início
          if (dataDevolucao.isBefore(dataInicio)) {
            System.out.println("A data de devolução deve ser posterior à data de início.");
            // return;
          }

        } while (dataDevolucao.isBefore(dataInicio));

      } catch (Exception e) {
        System.out.println("Formato de data inválido. Certifique-se de usar o formato dd/MM/yyyy.");
        return;
      }
      long dias = java.time.temporal.ChronoUnit.DAYS.between(dataInicio, dataDevolucao);
      System.out.println(dias);

      System.out.println("Digite o valor da locação:");
      double valor = sc.nextDouble();
      // sc.nextLine();

      double multa = 0;
      double tx = 0.028;
      multa += valor * Math.pow((1 + tx), dias);

      int status = 1;
      sc.nextLine();

      registrarLocacao(cpfcnpj, placaVeiculo, dataInicio, opcao, dataPrevistaDevolucao, dataDevolucao, valor, multa,
          status, conn);

      System.out.println("Locação registrada com sucesso!");

    } catch (Exception e) {
      System.out.println("Ocorreu um erro ao registrar a locação: " + e.getMessage());
    }
  }

  private boolean verificarVeiculoAlugado(int opcao, String placa, String conn, String cpfcnpj) throws SQLException {
    String columnName = (opcao == 1) ? "placacarro" : "placacaminhao";
    String query = "SELECT * FROM locacoes WHERE " + columnName + " = ?";

    try (Connection connection = DriverManager.getConnection(conn);
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, placa);
      // Adicione a condição para o CPF/CNPJ se necessário
      // preparedStatement.setString(2, cpfcnpj);

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        return resultSet.next(); // Retorna true se o veículo estiver alugado
      }
    }
  }

  private void exibirClientes(Cliente cliente, String conn, Scanner sc) throws SQLException {

    try (Connection connection = DriverManager.getConnection(conn);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM fisica");
        ResultSet resultSet = preparedStatement.executeQuery()) {

      System.out.println("Clientes pessoa Fisica disponíveis:");

      while (resultSet.next()) {
        System.out.println("Nome" + resultSet.getString("nome"));
        System.out.println("CPF: " + resultSet.getString("cpfcnpj"));
      }
    }

    System.out.println("Clientes pessoa Juridica disponíveis:");

    try (Connection connection = DriverManager.getConnection(conn);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM juridica");
        ResultSet resultSet = preparedStatement.executeQuery()) {

      while (resultSet.next()) {
        System.out.println("Nome" + resultSet.getString("nome"));
        System.out.println("CNPJ: " + resultSet.getString("cpfcnpj"));
      }
    }
  }

  private void registrarLocacao(String cpfcnpj, String placa, LocalDate dataInicio, int opcao,
      LocalDate dataPrevistaDevolucao, LocalDate dataDevolucao, double valor, double multa,
      int status, String conn) throws SQLException {

    String insertQuery = (opcao == 1 && cpfcnpj.length() == 11)
        ? "INSERT INTO locacoes ( cpf, placacarro, datainicio, dataprevistadevolucao, datadevolucao, preco, multa, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
        : (opcao == 1 && cpfcnpj.length() == 14)
            ? "INSERT INTO locacoes ( cnpj, placacarro, datainicio, dataprevistadevolucao, datadevolucao, preco, multa, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            : (opcao == 2 && cpfcnpj.length() == 11)
                ? "INSERT INTO locacoes ( cpf, placacaminhao, datainicio, dataprevistadevolucao, datadevolucao, preco, multa, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
                : (opcao == 2 && cpfcnpj.length() == 14)
                    ? "INSERT INTO locacoes ( cnpj, placacaminhao, datainicio, dataprevistadevolucao, datadevolucao, preco, multa, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
                    : null;

    if (insertQuery != null) {
      try (Connection connection = DriverManager.getConnection(conn);
          PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
        preparedStatement.setString(1, cpfcnpj);
        preparedStatement.setString(2, placa);
        preparedStatement.setDate(3, java.sql.Date.valueOf(dataInicio));
        preparedStatement.setDate(4, java.sql.Date.valueOf(dataPrevistaDevolucao));
        preparedStatement.setDate(5, java.sql.Date.valueOf(dataDevolucao));
        preparedStatement.setDouble(6, valor);
        preparedStatement.setDouble(7, multa);
        preparedStatement.setInt(8, status);

        int resultado = preparedStatement.executeUpdate();

        if (resultado > 0) {
          // Atualizar o status do veículo para indisponível após a locação
          atualizarStatusVeiculo(status, placa, conn);
        }
      }
    }
  }

  private void atualizarStatusVeiculo(int status, String placa, String conn) throws SQLException {
    try (Connection connection = DriverManager.getConnection(conn);
        PreparedStatement preparedStatement = connection
            .prepareStatement("UPDATE locacoes SET status = ? WHERE placacarro = ? OR placacaminhao = ?")) {
      preparedStatement.setInt(1, status);
      preparedStatement.setString(2, placa);
      preparedStatement.setString(3, placa);
      preparedStatement.executeUpdate();
    }
  }

  public void listarLocacao(Cliente cliente, ArrayList<Carro> carro, ArrayList<Caminhao> caminhao, Veiculo veiculos,
      Scanner sc, ArrayList<Locacao> locacao, String conn) {

    try (Connection connection = DriverManager.getConnection(conn);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM locacoes");
        ResultSet resultSet = preparedStatement.executeQuery()) {

      while (resultSet.next()) {
        System.out.println("CPF/CNPJ: " + resultSet.getString("cpf"));
        System.out.println("Placa: " + resultSet.getString("placacarro"));
        System.out.println("Data de início: " + resultSet.getString("datainicio"));
        System.out.println("Data prevista de devolução: " + resultSet.getString("dataprevistadevolucao"));
        System.out.println("Data de devolução: " + resultSet.getString("datadevolucao"));
        System.out.println("Valor: " + resultSet.getString("preco"));
        System.out.println("Multa: " + resultSet.getString("multa"));
        System.out.println("Status: " + resultSet.getString("status"));
      }
    } catch (Exception e) {
      System.out.println("Ocorreu um erro ao listar as locações: " + e.getMessage());
    }
  }

}
