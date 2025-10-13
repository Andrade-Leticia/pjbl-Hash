public class TabelaHashEncadeamento {
    // Implementação de tabela hash usando encadeamento separado

    // Estrutura de nó para lista encadeada
    private static class No {
        String chave;
        int valor;
        No proximo;

        public No(String chave, int valor) {
            this.chave = chave;
            this.valor = valor;
            this.proximo = null;
        }
}
    private No[] tabela;
    private int tamanho;

    public TabelaHashEncadeamento(int tamanho) {
        this.tamanho = tamanho;
        this.tabela = new No[tamanho];
    }

    // Inserir par chave-valor
    public void inserir(String chave, int valor) {
        int hash = FuncoesHash.hashPolinomial(chave, tamanho);
        No novo = new No(chave, valor);

        // Se posição estiver vazia, insere direto
        if (tabela[hash] == null) {
            tabela[hash] = novo;
        } else {
            // Encadeia no final da lista
            No atual = tabela[hash];
            while (atual.proximo != null) {
                if (atual.chave.equals(chave)) {
                    atual.valor = valor; // atualiza se já existir
                    return;
                }
                atual = atual.proximo;
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