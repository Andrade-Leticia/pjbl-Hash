public class ResultadoTestes {
    private String tipoTabela;
    private int tipoFuncaoHash;
    private String nomeFuncao;
    private int tamanhoTabela;
    private int tamanhoConjuntoInt;
    private String tamanhoConjuntoString;
    private long tempoInsercao;
    private long tempoBusca;
    private long colisoes;
    private int elementosEncontrados;
    private int[] maioresListas;
    private EstatisticasLista estatisticasLista;
    private EstatisticasGaps estatisticasGaps;

    public ResultadoTestes(String tipoTabela, int tipoFuncaoHash, String nomeFuncao,
                           int tamanhoTabela, int tamanhoConjuntoInt, String tamanhoConjuntoString) {
        this.tipoTabela = tipoTabela;
        this.tipoFuncaoHash = tipoFuncaoHash;
        this.nomeFuncao = nomeFuncao;
        this.tamanhoTabela = tamanhoTabela;
        this.tamanhoConjuntoInt = tamanhoConjuntoInt;
        this.tamanhoConjuntoString = tamanhoConjuntoString;
    }

    public String getTipoTabela() {
        return tipoTabela;
    }

    public int getTipoFuncaoHash() {
        return tipoFuncaoHash;
    }
    public String getNomeFuncao() {
        return nomeFuncao;
    }

    public int getTamanhoTabela() {
        return tamanhoTabela;
    }

    public String getTamanhoConjunto() {
        return tamanhoConjuntoString;
    }

    public int getTamanhoConjuntoInt() {
        return tamanhoConjuntoInt;
    }

    public long getTempoInsercao() {
        return tempoInsercao;
    }

    public void setTempoInsercao(long tempoInsercao) {
        this.tempoInsercao = tempoInsercao;
    }

    public long getTempoBusca() {
        return tempoBusca;
    }

    public void setTempoBusca(long tempoBusca) {
        this.tempoBusca = tempoBusca;
    }

    public long getColisoes() {
        return colisoes;
    }

    public void setColisoes(long colisoes) {
        this.colisoes = colisoes;
    }

    public int getElementosEncontrados() {
        return elementosEncontrados;
    }

    public void setElementosEncontrados(int elementosEncontrados) {
        this.elementosEncontrados = elementosEncontrados;
    }

    public int[] getMaioresListas() {
        return maioresListas;
    }

    public void setMaioresListas(int[] maioresListas) {
        this.maioresListas = maioresListas;
    }

    public EstatisticasLista getEstatisticasLista() {
        return estatisticasLista;
    }

    public void setEstatisticasLista(EstatisticasLista estatisticasLista) {
        this.estatisticasLista = estatisticasLista;
    }

    public EstatisticasGaps getEstatisticasGaps() {
        return estatisticasGaps;
    }

    public void setEstatisticasGaps(EstatisticasGaps estatisticasGaps) {
        this.estatisticasGaps = estatisticasGaps;
    }

    // Métodos auxiliares para gaps
    public int getMenorGap() {
        return estatisticasGaps != null ? estatisticasGaps.getMenorGap() : 0;
    }

    public int getMaiorGap() {
        return estatisticasGaps != null ? estatisticasGaps.getMaiorGap() : 0;
    }

    public double getMediaGap() {
        return estatisticasGaps != null ? estatisticasGaps.getMediaGap() : 0;
    }

    public String getDescricao() {
        return String.format("%s + %s (T=%d, C=%s)", tipoTabela, nomeFuncao, tamanhoTabela, tamanhoConjuntoString);
    }

    public String getDescricaoCurta() {
        return String.format("%s_%s_T%d_C%d",
                tipoTabela.replace(" ", ""),
                nomeFuncao.substring(0, 3),
                tamanhoTabela,
                tamanhoConjuntoInt);
    }

    @Override
    public String toString() {
        return String.format("%s | %s | T=%d | C=%s | Ins: %dms | Bus: %dms | Col: %d",
                tipoTabela, nomeFuncao, tamanhoTabela, tamanhoConjuntoString,
                tempoInsercao, tempoBusca, colisoes);
    }

    public String toStringDetalhado() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-20s | %-15s | T=%-7d | C=%-10s | Ins: %-6dms | Bus: %-6dms | Col: %-8d",
                tipoTabela, nomeFuncao, tamanhoTabela, tamanhoConjuntoString,
                tempoInsercao, tempoBusca, colisoes));

        if (maioresListas != null && maioresListas.length >= 3) {
            sb.append(String.format(" | Maiores listas: [%d, %d, %d]",
                    maioresListas[0], maioresListas[1], maioresListas[2]));
        }

        if (estatisticasGaps != null) {
            sb.append(String.format(" | Gaps: min=%d, max=%d, avg=%.2f",
                    estatisticasGaps.getMenorGap(), estatisticasGaps.getMaiorGap(),
                    estatisticasGaps.getMediaGap()));
        }

        if (estatisticasLista != null) {
            sb.append(String.format(" | Listas: maior=%d, média=%.2f",
                    estatisticasLista.getMaiorLista(), estatisticasLista.getMediaLista()));
        }

        return sb.toString();
    }

    public String toStringResumido() {
        return String.format("%s + %s: %dms ins, %dms bus, %d colisões",
                tipoTabela, nomeFuncao, tempoInsercao, tempoBusca, colisoes);
    }

    public String toCSV() {
        String maioresListasStr = "N/A";
        if (maioresListas != null && maioresListas.length >= 3) {
            maioresListasStr = String.format("[%d,%d,%d]", maioresListas[0], maioresListas[1], maioresListas[2]);
        }

        return String.format("%s,%s,%d,%s,%d,%d,%d,%s,%d,%d,%.2f,%d,%.3f,%.4f",
                tipoTabela, nomeFuncao, tamanhoTabela, tamanhoConjuntoString,
                tempoInsercao, tempoBusca, colisoes, maioresListasStr,
                getMenorGap(), getMaiorGap(), getMediaGap(),
                elementosEncontrados, getFatorCarga(), getEficiencia());
    }

    public String toCSVSimplificado() {
        double fatorCarga = getFatorCarga();
        String cenario = String.format("T%d_C%d", tamanhoTabela, tamanhoConjuntoInt);

        int maiorLista = 0;
        if (maioresListas != null && maioresListas.length > 0) {
            maiorLista = maioresListas[0];
        }

        return String.format("%s,%s,%s,%.3f,%d,%d,%d,%.4f,%s,%d,%d",
                cenario, tipoTabela, nomeFuncao, fatorCarga,
                tempoInsercao, tempoBusca, colisoes, getEficiencia(),
                isBemSucedido() ? "SUCESSO" : "FALHA", maiorLista, getMaiorGap());
    }

    public boolean isBemSucedido() {
        return elementosEncontrados > 0 && tempoInsercao > 0 && tempoBusca > 0;
    }

    public double getEficiencia() {
        if (colisoes == 0) return 0;
        return (double) colisoes / elementosEncontrados;
    }

    // Fator de carga usando o inteiro
    public double getFatorCarga() {
        return (double) tamanhoConjuntoInt / tamanhoTabela;
    }
}