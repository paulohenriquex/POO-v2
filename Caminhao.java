public class Caminhao extends Veiculo {

  private float capacidadeCarga;
  private int numeroDeEixos;

  public Caminhao() {

  }

  public Caminhao(String marca, String modelo, int anoFabricacao, int anoModelo, String placa, float capacidadeCarga,
      int numeroDeEixos) {
    super(marca, modelo, anoFabricacao, anoModelo, placa);
    this.capacidadeCarga = capacidadeCarga;
    this.numeroDeEixos = numeroDeEixos;
  }

  public int getNumeroDeEixos() {
    return numeroDeEixos;
  }

  public void setNumeroDeEixos(int numeroDeEixos) {
    this.numeroDeEixos = numeroDeEixos;
  }

  public void setCapacidadeCarga(float capacidadeCarga) {
    this.capacidadeCarga = capacidadeCarga;
  }

  public float getCapacidadeCarga() {
    return capacidadeCarga;
  }

}
