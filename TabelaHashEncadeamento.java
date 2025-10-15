public class TabelaHashEncadeamento {
    public static class No {
        public String chave;
        public int valor;
        public No proximo;

        public No(String chave, int valor) {
            this.chave = chave;
            this.valor = valor;
            this.proximo = null;
        }
    }

    private No[] tabela;
    private int tamanho;
    private long colisoes = 0;

    public TabelaHashEncadeamento(int tamanho) {
        this.tamanho = tamanho;
        this.tabela = new No[tamanho];
    }

    public No[] getTabela() {
        return tabela;
    }

    public int getTamanho() {
        return tamanho;
    }

    public long getColisoes() {
        return colisoes;
    }

    // Metodo para obter o tamanho de uma lista específica
    public int getTamanhoLista(int indice) {
        if (indice < 0 || indice >= tamanho) {
            return 0;
        }

        int tamanhoLista = 0;
        No atual = tabela[indice];
        while (atual != null) {
            tamanhoLista++;
            atual = atual.proximo;
        }
        return tamanhoLista;
    }

    // Encontrar 3 maiores listas
    public int[] getTresMaioresListas() {
        int[] maiores = new int[3];

        for (int i = 0; i < tamanho; i++) {
            int tamanhoLista = getTamanhoLista(i);

            // Atualizar os 3 maiores
            if (tamanhoLista > maiores[0]) {
                maiores[2] = maiores[1];
                maiores[1] = maiores[0];
                maiores[0] = tamanhoLista;
            } else if (tamanhoLista > maiores[1]) {
                maiores[2] = maiores[1];
                maiores[1] = tamanhoLista;
            } else if (tamanhoLista > maiores[2]) {
                maiores[2] = tamanhoLista;
            }
        }
        return maiores;
    }

    // Calcular estatísticas das listas
    public EstatisticasLista getEstatisticasListas() {
        int totalElementos = 0;
        int listasNaoVazias = 0;
        int maiorLista = 0;

        for (int i = 0; i < tamanho; i++) {
            int tamanhoLista = getTamanhoLista(i);
            if (tamanhoLista > 0) {
                totalElementos += tamanhoLista;
                listasNaoVazias++;
                if (tamanhoLista > maiorLista) {
                    maiorLista = tamanhoLista;
                }
            }
        }

        double mediaLista = listasNaoVazias > 0 ? (double) totalElementos / listasNaoVazias : 0;
        double fatorCarga = (double) totalElementos / tamanho;

        return new EstatisticasLista(totalElementos, listasNaoVazias, maiorLista, mediaLista, fatorCarga);
    }

    // Buscar valor pela chave
    public Integer buscar(String chave) {
        int hash = FuncoesHash.hashPolinomial(chave, tamanho);
        No atual = tabela[hash];

        while (atual != null) {
            if (atual.chave.equals(chave)) {
                return atual.valor;
            }
            atual = atual.proximo;
        }
        return null;
    }

    // Inserção para testes de desempenho
    public void inserir(Registro registro, int tipoFuncaoHash) {
        String chave = registro.getCodigoRegistro();
        int valor = registro.getValor();

        int hash = FuncoesHash.obterHash(chave, tamanho, tipoFuncaoHash);
        No novo = new No(chave, valor);

        if (tabela[hash] == null) {
            tabela[hash] = novo;
        } else {
            colisoes++;

            No atual = tabela[hash];
            while (atual.proximo != null) {
                if (atual.chave.equals(chave)) {
                    atual.valor = valor;
                    return;
                }
                atual = atual.proximo;
                colisoes++;
            }

            if (atual.chave.equals(chave)) {
                atual.valor = valor;
                return;
            }

            atual.proximo = novo;
        }
    }

    // Exibir toda a tabela
    public void imprimirTabela() {
        for (int i = 0; i < tamanho; i++) {
            System.out.print(i + ": ");
            No atual = tabela[i];
            while (atual != null) {
                System.out.print("[" + atual.chave + " -> " + atual.valor + "] -> ");
                atual = atual.proximo;
            }
            System.out.println("null");
        }
    }
}