public class Carro extends Veiculo {

  private int capacidadePassageiros;
  private int numeroPortas;

  public Carro() {

  }

  public int getCapacidadePassageiros() {
    return capacidadePassageiros;
  }

  public void setCapacidadePassageiros(int capacidadePassageiros) {
    this.capacidadePassageiros = capacidadePassageiros;
  }

  public int getNumeroPortas() {
    return numeroPortas;
  }

  public void setNumeroPortas(int numeroPortas) {
    this.numeroPortas = numeroPortas;
  }

  public Carro(String marca, String modelo, int anoFabricacao, int anoModelo, String placa,
      int capacidadePassageiros, int numeroPortas) {
    super(marca, modelo, anoFabricacao, anoModelo, placa);
    this.capacidadePassageiros = capacidadePassageiros;
    this.numeroPortas = numeroPortas;
  }

}
