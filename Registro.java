public class Registro {
    // Campo para armazenar o código de 9 dígitos.
    private String codigoRegistro; 
    private int valor;             

    public Registro(String codigoRegistro, int valor) {
        this.codigoRegistro = codigoRegistro;
        this.valor = valor;
    }

     // Retorna o código de 9 dígitos do registro, usado como a CHAVE na Tabela Hash.
    public String getCodigoRegistro() {
        return codigoRegistro;
    }

    //Retorna o valor/dado associado.
    public int getValor() {
        return valor;
    }
}
