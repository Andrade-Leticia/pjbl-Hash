public class TabelaHashSondagemQuadratica {
    private String[] chaves;
    private int[] valores;
    private boolean[] ocupado;
    private int tamanho;
    private final int c1 = 1;
    private final int c2 = 3;
    private long colisoes = 0;
    private int elementosInseridos = 0;
    private int elementosNaoInseridos = 0;

    public TabelaHashSondagemQuadratica(int tamanho) {
        this.tamanho = tamanho;
        this.chaves = new String[tamanho];
        this.valores = new int[tamanho];
        this.ocupado = new boolean[tamanho];
    }

    public String[] getChaves() {
        return chaves;
    }

    public int[] getValores() {
        return valores;
    }

    public boolean[] getOcupado() {
        return ocupado;
    }

    public int getTamanho() {
        return tamanho;
    }

    public long getColisoes() {
        return colisoes;
    }

    public int getElementosInseridos() {
        return elementosInseridos;
    }

    public int getElementosNaoInseridos() {
        return elementosNaoInseridos;
    }

    public void inserir(Registro registro, int tipoFuncaoHash) {
        if (elementosInseridos >= tamanho) {
            // Tabela cheia
            return;
        }

        String chave = registro.getCodigoRegistro();
        int valor = registro.getValor();

        int hash = FuncoesHash.obterHash(chave, tamanho, tipoFuncaoHash);
        int i = hash;
        int j = 0;

        while (ocupado[i] && !chaves[i].equals(chave)) {
            j++;
            // Cálculo quadrático CORRIGIDO - usando módulo para evitar overflow
            int novoIndice = (hash + c1 * j + c2 * j * j) % tamanho;
            if (novoIndice < 0) {
                novoIndice += tamanho;
            }
            i = novoIndice;

            colisoes++;

            if (j >= tamanho) {
                return;
            }
        }

        if (!ocupado[i]) {
            elementosInseridos++;
        }
        chaves[i] = chave;
        valores[i] = valor;
        ocupado[i] = true;
    }

    // Busca corrigida - precisa saber qual função hash usar
    public Integer buscar(String chave, int tipoFuncaoHash) {
        int hash = FuncoesHash.obterHash(chave, tamanho, tipoFuncaoHash);
        int i = hash;
        int j = 0;

        while (j < tamanho) {
            if (!ocupado[i]) {
                break;
            }
            if (chaves[i] != null && chaves[i].equals(chave)) {
                return valores[i];
            }
            j++;
            int novoIndice = (hash + c1 * j + c2 * j * j) % tamanho;
            if (novoIndice < 0) {
                novoIndice += tamanho;
            }
            i = novoIndice;
        }
        return null;
    }

    // Analisar gaps
    public EstatisticasGaps analisarGaps() {
        int menorGap = Integer.MAX_VALUE;
        int maiorGap = Integer.MIN_VALUE;
        int totalGaps = 0;
        int countGaps = 0;

        int gapAtual = 0;
        boolean encontrouOcupado = false;

        for (int i = 0; i < tamanho; i++) {
            if (ocupado[i]) {
                if (encontrouOcupado && gapAtual > 0) {
                    if (gapAtual < menorGap) menorGap = gapAtual;
                    if (gapAtual > maiorGap) maiorGap = gapAtual;
                    totalGaps += gapAtual;
                    countGaps++;
                }
                gapAtual = 0;
                encontrouOcupado = true;
            } else {
                gapAtual++;
            }
        }

        if (menorGap == Integer.MAX_VALUE) menorGap = 0;
        if (maiorGap == Integer.MIN_VALUE) maiorGap = 0;

        double mediaGap = countGaps > 0 ? (double) totalGaps / countGaps : 0;

        return new EstatisticasGaps(menorGap, maiorGap, mediaGap, countGaps);
    }

    public double getFatorCarga() {
        return (double) elementosInseridos / tamanho;
    }
}
