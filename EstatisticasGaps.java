public class EstatisticasGaps {
    private int menorGap;
    private int maiorGap;
    private double mediaGap;
    private int quantidadeGaps;

    public EstatisticasGaps(int menorGap, int maiorGap, double mediaGap, int quantidadeGaps) {
        this.menorGap = menorGap;
        this.maiorGap = maiorGap;
        this.mediaGap = mediaGap;
        this.quantidadeGaps = quantidadeGaps;
    }

    // Getters
    public int getMenorGap() { return menorGap; }
    public int getMaiorGap() { return maiorGap; }
    public double getMediaGap() { return mediaGap; }
    public int getQuantidadeGaps() { return quantidadeGaps; }

    @Override
    public String toString() {
        return String.format("Gaps: menor=%d, maior=%d, média=%.2f, quantidade=%d",
                menorGap, maiorGap, mediaGap, quantidadeGaps);
    }
}