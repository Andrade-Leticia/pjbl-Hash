import java.lang.Math;

public class FuncoesHash {

    private static int obterChaveInt(String chave) {
        return Math.abs(chave.hashCode());
    }


    public static int hashDivisao(String chave, int tamanhoTabela) {
        int chaveInt = obterChaveInt(chave);
        int indice = chaveInt % tamanhoTabela;
        return (indice < 0) ? indice + tamanhoTabela : indice;
    }

    public static int hashMultiplicacao(String chave, int tamanhoTabela) {
        int chaveInt = obterChaveInt(chave);
        double A = 0.6180339887;
        double parteFracionaria = (chaveInt * A) % 1;
        int indice = (int) (tamanhoTabela * parteFracionaria);
        return (indice < 0) ? indice + tamanhoTabela : indice;
    }

    public static int hashPolinomial(String chave, int tamanhoTabela) {
        int hash = 0;
        for (int i = 0; i < chave.length(); i++) {
            hash = (31 * hash + chave.charAt(i)) % Integer.MAX_VALUE;
        }

        int indice = hash % tamanhoTabela;
        if (indice < 0) {
            indice += tamanhoTabela;
        }
        return indice;
    }

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