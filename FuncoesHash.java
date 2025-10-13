public class FuncoesHash {
    // Contém três funções hash diferentes implementadas manualmente

    // Função hash 1: resto da divisão
    public static int hashDivisao(int chave, int tamanhoTabela) {
        return chave % tamanhoTabela;
    }

    // Função hash 2: método da multiplicação (Knuth)
    public static int hashMultiplicacao(int chave, int tamanhoTabela) {
        double A = 0.6180339887; // constante sugerida por Knuth
        double parteFracionaria = (chave * A) % 1;
        return (int) (tamanhoTabela * parteFracionaria);
    }

    // Função hash 3: polinomial (para Strings)
    public static int hashPolinomial(String chave, int tamanhoTabela) {
        int hash = 0;
        for (int i = 0; i < chave.length(); i++) { //ver se .lenght ta liberado pq em outros trabalhos ele deixou pra manipular strings
            hash = (31 * hash + chave.charAt(i)) % tamanhoTabela;
        }
        return hash;
    }
}
