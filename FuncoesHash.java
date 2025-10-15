import java.lang.Math;

public class FuncoesHash {
    // Contém três funções hash diferentes implementadas manualmente

    private static int obterChaveInt(String chave) {
        return Math.abs(chave.hashCode());
    }

    // Função hash 1: resto da divisão
    public static int hashDivisao(String chave, int tamanhoTabela) {
        int chaveInt = obterChaveInt(chave);
        int indice = chaveInt % tamanhoTabela;
        return (indice < 0) ? indice + tamanhoTabela : indice;
    }

    // Função hash 2: metodo da multiplicação (Knuth)
    public static int hashMultiplicacao(String chave, int tamanhoTabela) {
        int chaveInt = obterChaveInt(chave);
        double A = 0.6180339887; // constante sugerida por Knuth
        double parteFracionaria = (chaveInt * A) % 1;
        int indice = (int) (tamanhoTabela * parteFracionaria);
        return (indice < 0) ? indice + tamanhoTabela : indice;
    }

    // Função hash 3: polinomial (para Strings)
    public static int hashPolinomial(String chave, int tamanhoTabela) {
        int hash = 0;
        for (int i = 0; i < chave.length(); i++) {
            hash = (31 * hash + chave.charAt(i)) % Integer.MAX_VALUE; // evita overflow
        }

        int indice = hash % tamanhoTabela;
        if (indice < 0) {
            indice += tamanhoTabela;
        }
        return indice;
    }

    // Metodo para obter o índice hash
    public static int obterHash(String chave, int tamanhoTabela, int tipoFuncaoHash) {
        switch (tipoFuncaoHash) {
            case 1:
                return hashDivisao(chave, tamanhoTabela);
            case 2:
                return hashMultiplicacao(chave, tamanhoTabela);
            case 3:
            default:
                return hashPolinomial(chave, tamanhoTabela);
        }
    }
}