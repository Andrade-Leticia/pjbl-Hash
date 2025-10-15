public class TabelaHashSondagemQuadratica {
    // Implementação de tabela hash com rehashing (sondagem quadrática)

    private String[] chaves;
    private int[] valores;
    private int tamanho;
    private final int c1 = 1;
    private final int c2 = 3;
    private long colisoes = 0;

    public TabelaHashSondagemQuadratica(int tamanho) {
        this.tamanho = tamanho;
        this.chaves = new String[tamanho];
        this.valores = new int[tamanho];
    }

    // retornar o número de colisões
    public long getColisoes() {
        return colisoes;
    }

    // Inserir chave e valor
    public void inserir(String chave, int valor) {
        int hash = FuncoesHash.hashPolinomial(chave, tamanho);
        int i = hash;
        int j = 0;

        // Rehashing quadrático: (hash + c1*j + c2*j^2) % tamanho
        while (chaves[i] != null && !chaves[i].equals(chave)) {
            j++;
            i = (hash + c1 * j + c2 * j * j) % tamanho;
        }

        chaves[i] = chave;
        valores[i] = valor;
    }

        // Inserção para testes de desempenho 
    public void inserir(Registro registro, int tipoFuncaoHash) {
        String chave = registro.getCodigoRegistro();
        int valor = registro.getValor();

        // Usa o método centralizado para obter o hash
        int hash = FuncoesHash.obterHash(chave, tamanho, tipoFuncaoHash);
        int i = hash;
        int j = 0; // 'j' é o contador de tentativas/passos

        while (chaves[i] != null && !chaves[i].equals(chave)) {
            j++;

            // Cálculo de rehashing quadrático. Usamos long no cálculo para evitar overflow.
            long calculo = (long)hash + (long)c1 * j + (long)c2 * j * j;

            int novoIndice = (int) (calculo % tamanho);

            if (novoIndice < 0) {
                novoIndice += tamanho;
            }
            i = novoIndice;

            colisoes++; // contador incrementado a cada sondagem

            if (j >= tamanho) {
                return;
            }
        }

        chaves[i] = chave;
        valores[i] = valor;
    }

    // Buscar valor pela chave
    public Integer buscar(String chave) {
        int hash = FuncoesHash.hashPolinomial(chave, tamanho);
        int i = hash;
        int j = 0;

        while (chaves[i] != null) {
            if (chaves[i].equals(chave)) return valores[i];
            j++;
            i = (hash + c1 * j + c2 * j * j) % tamanho;
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
