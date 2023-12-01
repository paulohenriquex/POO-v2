import java.time.LocalDate;
import java.util.ArrayList;

public class Locacao {

  private Cliente cliente;
  private ArrayList<Veiculo> veiculos;
  private LocalDate dataInicio;
  private LocalDate dataPrevistaDevolucao;
  private LocalDate dataDevolucao;
  private double valor;
  private double multa;
  private int status;

  public Cliente getCliente() {
    return cliente;
  }

  public void setCliente(Cliente cliente) {
    this.cliente = cliente;
  }

  public ArrayList<Veiculo> getVeiculos() {
    return veiculos;
  }

  public void setVeiculos(ArrayList<Veiculo> veiculos) {
    this.veiculos = veiculos;
  }

  public LocalDate getDataInicio() {
    return dataInicio;
  }

  public void setDataInicio(LocalDate dataInicio) {
    this.dataInicio = dataInicio;
  }

  public LocalDate getDataPrevistaDevolucao() {
    return dataPrevistaDevolucao;
  }

  public void setDataPrevistaDevolucao(LocalDate dataPrevistaDevolucao) {
    this.dataPrevistaDevolucao = dataPrevistaDevolucao;
  }

  public LocalDate getDataDevolucao() {
    return dataDevolucao;
  }

  public void setDataDevolucao(LocalDate dataDevolucao) {
    this.dataDevolucao = dataDevolucao;
  }

  public double getValor() {
    return valor;
  }

  public void setValor(double valor) {
    this.valor = valor;
  }

  public double getMulta() {
    return multa;
  }

  public void setMulta(double multa) {
    this.multa = multa;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public Locacao() {

  }

  public Locacao(Cliente cliente, ArrayList<Veiculo> veiculos, LocalDate dataInicio, LocalDate dataPrevistaDevolucao,
      LocalDate dataDevolucao, double valor, double multa, int status) {

    this.cliente = cliente;
    this.veiculos = veiculos;
    this.dataInicio = dataInicio;
    this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    this.dataDevolucao = dataDevolucao;
    this.valor = valor;
    this.multa = multa;
    this.status = status;

  }

}
