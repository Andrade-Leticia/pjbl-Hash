public class Registro {
    private String codigoRegistro;
    private int valor;

    public Registro(String codigoRegistro, int valor) {
        this.codigoRegistro = codigoRegistro;
        this.valor = valor;
    }

    public String getCodigoRegistro() {
        return codigoRegistro;
    }

    public int getValor() {
        return valor;
    }
}
