public class Juridica extends Cliente {

  private String cnpj;

  public Juridica() {

  }

  public Juridica(String nome, String endereco, String telefone, String cnpj) {

    super(nome, endereco, telefone);
    this.cnpj = cnpj;

  }

  public String getCnpj() {
    return cnpj;
  }

  public void setCnpj(String cnpj) {
    this.cnpj = cnpj;
  }

}
