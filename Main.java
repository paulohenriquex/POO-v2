import java.util.Scanner;
import java.util.ArrayList;

public class Main {

  public static void main(String[] args) {

    Scanner sc = new Scanner(System.in);
    int opcao = 0;

    do {

      GerenciarCliente gerCliente = new GerenciarCliente();
      GerenciarVeiculo gerVeiculo = new GerenciarVeiculo();
      // GerenciarAgenda gerAgenda = new GerenciarAgenda();
      // GerenciarCaixa gerCaixa = new GerenciarCaixa();
      GerenciarLocacao gerLocacao = new GerenciarLocacao();
      ArrayList<Carro> carro = new ArrayList<Carro>();
      ArrayList<Caminhao> caminhao = new ArrayList<Caminhao>();
      Veiculo veiculos = new Veiculo();
      ArrayList<Locacao> locacao = new ArrayList<Locacao>();
      ArrayList<Fisica> fisica = new ArrayList<Fisica>();
      ArrayList<Juridica> juridica = new ArrayList<Juridica>();
      Cliente cliente = new Cliente();

      System.out.println("1 - Cliente");
      System.out.println("2 - Veiculo");
      System.out.println("3 - Locacao");
      System.out.println("4 - Agenda");
      System.out.println("5 - Caixa");
      System.out.println("0 - Sair");
      System.out.println("Teste");
      opcao = sc.nextInt();
      sc.nextLine();

      switch (opcao) {
        case 1:
          gerCliente.gerenciarCliente(sc, cliente, fisica, juridica);
          break;
        case 2:
          gerVeiculo.gerenciarVeiculos(sc, carro, caminhao, veiculos);
          break;
        case 3:
          gerLocacao.gerenciarLocacao(cliente, carro, caminhao, veiculos, sc, locacao);
          break;
        case 4:
          break;
        case 5:
          break;
        case 0:
          break;
        default:
          System.out.println("Opcao invalida.");
          break;
      }
    } while (opcao != 0);

    sc.close();
  }

}
