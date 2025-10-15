import java.util.Random;
import java.util.ArrayList;

public class GeradorDados {
    // Seed fixa: Garante a identidade dos conjuntos de dados
    private static final long SEED = 42;

    public static ArrayList<Registro> gerarRegistros(int quantidade) {
        ArrayList<Registro> dados = new ArrayList<>(quantidade);
        // Usa a SEED para o gerador
        Random gerador = new Random(SEED);

        for (int i = 0; i < quantidade; i++) {
            // Gera número de 0 a 999.999.999 (9 dígitos)
            int numAleatorio = gerador.nextInt(1_000_000_000);

            // Formata para ter 9 dígitos
            String codigo = String.format("%09d", numAleatorio);

            dados.add(new Registro(codigo, i));
        }

        return dados;
    }
}
