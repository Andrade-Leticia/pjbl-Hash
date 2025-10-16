import java.util.Random;
import java.util.ArrayList;

public class GeradorDados {
    private static final long SEED = 42;

    public static ArrayList<Registro> gerarRegistros(int quantidade) {
        ArrayList<Registro> dados = new ArrayList<>(quantidade);
        Random gerador = new Random(SEED);

        for (int i = 0; i < quantidade; i++) {
            int numAleatorio = gerador.nextInt(1_000_000_000);

            String codigo = String.format("%09d", numAleatorio);

            dados.add(new Registro(codigo, i));
        }

        return dados;
    }
}
