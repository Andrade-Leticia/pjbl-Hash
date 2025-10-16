
public class EstatisticasLista {
    private int totalElementos;
    private int listasNaoVazias;
    private int maiorLista;
    private double mediaLista;
    private double fatorCarga;

    public EstatisticasLista(int totalElementos, int listasNaoVazias, int maiorLista,
                             double mediaLista, double fatorCarga) {
        this.totalElementos = totalElementos;
        this.listasNaoVazias = listasNaoVazias;
        this.maiorLista = maiorLista;
        this.mediaLista = mediaLista;
        this.fatorCarga = fatorCarga;
    }

    public int getTotalElementos() { return totalElementos; }
    public int getListasNaoVazias() { return listasNaoVazias; }
    public int getMaiorLista() { return maiorLista; }
    public double getMediaLista() { return mediaLista; }
    public double getFatorCarga() { return fatorCarga; }

    @Override
    public String toString() {
        return String.format("Elementos: %d, Listas não vazias: %d, Maior lista: %d, Média: %.2f, Fator carga: %.3f",
                totalElementos, listasNaoVazias, maiorLista, mediaLista, fatorCarga);
    }
}