public class TabelaHashSondagemLinear {
    // Implementação de tabela hash com rehashing (sondagem linear)

    private String[] chaves;
    private int[] valores;
    private int tamanho;

    public TabelaHashSondagemLinear(int tamanho) {
        this.tamanho = tamanho;
        this.chaves = new String[tamanho];
        this.valores = new int[tamanho];
    }
    // Inserir chave e valor
    public void inserir(String chave, int valor) {
        int hash = FuncoesHash.hashPolinomial(chave, tamanho);
        int i = hash;
        int passo = 0;

        // Rehashing linear: (hash + j) % tamanho
        while (chaves[i] != null && !chaves[i].equals(chave)) {
            i = (hash + ++passo) % tamanho;
        }

        chaves[i] = chave;
        valores[i] = valor;
    }

    // Buscar valor pela chave
    public Integer buscar(String chave) {
        int hash = FuncoesHash.hashPolinomial(chave, tamanho);
        int i = hash;
        int passo = 0;

        while (chaves[i] != null) {
            if (chaves[i].equals(chave)) return valores[i];
            i = (hash + ++passo) % tamanho;
        }

        return null;
    }

    // Exibir tabela
    public void imprimirTabela() {
        for (int i = 0; i < tamanho; i++) {
            if (chaves[i] != null)
                System.out.println(i + ": " + chaves[i] + " -> " + valores[i]);
            else
                System.out.println(i + ": null");
        }
    }
}
