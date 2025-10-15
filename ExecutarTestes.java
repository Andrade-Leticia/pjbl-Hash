import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExecutarTestes {

    private static final int T1 = 10000;
    private static final int T2 = 100000;
    private static final int T3 = 1000000;

    private static final int C1 = 100000;
    private static final int C2 = 1000000;
    private static final int C3 = 10000000;

    public static void main(String[] args) {
        executarTestesCompletos();
    }

    public static void executarTestesCompletos() {
        // Geração dos dados
        System.out.println("Passo 3: Gerando conjuntos de dados com SEED fixa...");
        ArrayList<Registro> dadosC1 = GeradorDados.gerarRegistros(C1);
        ArrayList<Registro> dadosC2 = GeradorDados.gerarRegistros(C2);
        ArrayList<Registro> dadosC3 = GeradorDados.gerarRegistros(C3);

        // Estruturas para armazenar resultados
        ArrayList<ResultadoTestes> resultados = new ArrayList<>();

        int[] tamanhosTabela = {T1, T2, T3};

        ArrayList<Registro>[] conjuntosDados = new ArrayList[3];
        conjuntosDados[0] = dadosC1;
        conjuntosDados[1] = dadosC2;
        conjuntosDados[2] = dadosC3;

        String[] nomesConjuntos = {"100.000", "1 Milhão", "10 Milhões"};
        String[] nomesFuncoes = {"Divisão", "Multiplicação", "Polinomial"};

        System.out.println("Iniciando testes completos (Passos 4, 5 e 6)...\n");

        for (int tamT : tamanhosTabela) {
            for (int c = 0; c < conjuntosDados.length; c++) {
                ArrayList<Registro> dados = conjuntosDados[c];
                double fatorCargaTeorico = (double) dados.size() / tamT;

//                // Testar TODAS as combinações
//                if (fatorCargaTeorico > 1.0) {
//                    System.out.printf("⚠️  AVISO: Tabela=%d, Conjunto=%s (fator carga = %.2f) - Sondagem pode falhar!\n",
//                            tamT, nomesConjuntos[c], fatorCargaTeorico);
//                }

                System.out.printf("\n=== Tabela=%d | Conjunto=%s | Fator Carga Teórico: %.2f ===\n",
                        tamT, nomesConjuntos[c], fatorCargaTeorico);

                for (int f = 1; f <= 3; f++) {
                    String nomeFuncao = nomesFuncoes[f-1];
                    System.out.println("\n--- Função Hash: " + nomeFuncao + " ---");

                    // TESTE DE ENCADENAMENTO
                    TabelaHashEncadeamento tabE = new TabelaHashEncadeamento(tamT);
                    ResultadoTestes resE = executarTesteCompleto("Encadeamento", tabE, dados, f, tamT, nomesConjuntos[c], nomeFuncao);
                    resultados.add(resE);

                    // TESTE DE SONDAGEM LINEAR
                    TabelaHashSondagemLinear tabSL = new TabelaHashSondagemLinear(tamT);
                    ResultadoTestes resSL = executarTesteCompleto("Sondagem Linear", tabSL, dados, f, tamT, nomesConjuntos[c], nomeFuncao);
                    resultados.add(resSL);

                    // TESTE DE SONDAGEM QUADRÁTICA
                    TabelaHashSondagemQuadratica tabSQ = new TabelaHashSondagemQuadratica(tamT);
                    ResultadoTestes resSQ = executarTesteCompleto("Sondagem Quadrática", tabSQ, dados, f, tamT, nomesConjuntos[c], nomeFuncao);
                    resultados.add(resSQ);
                }
            }
        }

        // Gerar relatório final
        gerarRelatorio(resultados);
    }

    private static ResultadoTestes executarTesteCompleto(String tipoTabela, Object tabela,
                                                         ArrayList<Registro> dados, int tipoFuncaoHash,
                                                         int tamanhoTabela, String tamanhoConjunto, String nomeFuncao) {
        ResultadoTestes resultado = new ResultadoTestes(tipoTabela, tipoFuncaoHash, nomeFuncao,
                tamanhoTabela, dados.size(), tamanhoConjunto);

        System.out.println("  📊 Testando " + tipoTabela + "...");

        // INSERÇÃO
        long tempoInsercao = medirInsercao(tabela, dados, tipoFuncaoHash);
        resultado.setTempoInsercao(tempoInsercao);

        // BUSCA
        long tempoBusca = medirBusca(tabela, dados, tipoFuncaoHash, resultado);
        resultado.setTempoBusca(tempoBusca);

        // ANÁLISES
        if (tabela instanceof TabelaHashEncadeamento) {
            TabelaHashEncadeamento tab = (TabelaHashEncadeamento) tabela;
            resultado.setColisoes(tab.getColisoes());

            int[] maioresListas = tab.getTresMaioresListas();
            resultado.setMaioresListas(maioresListas);

            EstatisticasLista estatisticas = tab.getEstatisticasListas();
            resultado.setEstatisticasLista(estatisticas);

            System.out.printf("    ✅ Encadeamento: %d colisões, %dms ins, %dms bus, maior lista: %d\n",
                    tab.getColisoes(), tempoInsercao, tempoBusca, maioresListas[0]);

        } else if (tabela instanceof TabelaHashSondagemLinear) {
            TabelaHashSondagemLinear tab = (TabelaHashSondagemLinear) tabela;
            resultado.setColisoes(tab.getColisoes());

            EstatisticasGaps gaps = tab.analisarGaps();
            resultado.setEstatisticasGaps(gaps);

            double fatorCargaReal = tab.getElementosInseridos() / (double) tab.getTamanho();
            int naoInseridos = tab.getElementosNaoInseridos();

            System.out.printf("    ✅ Sondagem Linear: %d colisões, %dms ins, %dms bus, maior gap: %d, fator carga real: %.3f, não inseridos: %d\n",
                    tab.getColisoes(), tempoInsercao, tempoBusca, gaps.getMaiorGap(),
                    fatorCargaReal, naoInseridos);

        } else if (tabela instanceof TabelaHashSondagemQuadratica) {
            TabelaHashSondagemQuadratica tab = (TabelaHashSondagemQuadratica) tabela;
            resultado.setColisoes(tab.getColisoes());

            EstatisticasGaps gaps = tab.analisarGaps();
            resultado.setEstatisticasGaps(gaps);

            double fatorCargaReal = tab.getElementosInseridos() / (double) tab.getTamanho();
            int naoInseridos = tab.getElementosNaoInseridos();

            System.out.printf("    ✅ Sondagem Quadrática: %d colisões, %dms ins, %dms bus, maior gap: %d, fator carga real: %.3f, não inseridos: %d\n",
                    tab.getColisoes(), tempoInsercao, tempoBusca, gaps.getMaiorGap(),
                    fatorCargaReal, naoInseridos);
        }

        return resultado;
    }

    // Metodo para medir inserção
    private static long medirInsercao(Object tabela, ArrayList<Registro> dados, int tipoFuncaoHash) {
        long startTime = System.nanoTime();

        if (tabela instanceof TabelaHashEncadeamento) {
            TabelaHashEncadeamento tab = (TabelaHashEncadeamento) tabela;
            for (Registro r : dados) tab.inserir(r, tipoFuncaoHash);
        } else if (tabela instanceof TabelaHashSondagemLinear) {
            TabelaHashSondagemLinear tab = (TabelaHashSondagemLinear) tabela;
            for (Registro r : dados) tab.inserir(r, tipoFuncaoHash);
        } else if (tabela instanceof TabelaHashSondagemQuadratica) {
            TabelaHashSondagemQuadratica tab = (TabelaHashSondagemQuadratica) tabela;
            for (Registro r : dados) tab.inserir(r, tipoFuncaoHash);
        }

        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000000; // ms
    }

    // Metodo para medir busca de todos os elementos
    private static long medirBusca(Object tabela, ArrayList<Registro> dados, int tipoFuncaoHash, ResultadoTestes resultado) {
        long startTime = System.nanoTime();

        int encontrados = 0;
        for (Registro r : dados) {
            String chave = r.getCodigoRegistro();
            Integer valor = null;

            if (tabela instanceof TabelaHashEncadeamento) {
                valor = ((TabelaHashEncadeamento) tabela).buscar(chave);
            } else if (tabela instanceof TabelaHashSondagemLinear) {
                valor = ((TabelaHashSondagemLinear) tabela).buscar(chave, tipoFuncaoHash);
            } else if (tabela instanceof TabelaHashSondagemQuadratica) {
                valor = ((TabelaHashSondagemQuadratica) tabela).buscar(chave, tipoFuncaoHash);
            }

            if (valor != null) {
                encontrados++;
            }
        }

        long endTime = System.nanoTime();
        resultado.setElementosEncontrados(encontrados);
        return (endTime - startTime) / 1000000; // ms
    }

    // gerar relatório
    private static void gerarRelatorio(ArrayList<ResultadoTestes> resultados) {
        System.out.println("\n" + "=".repeat(120));
        System.out.println("📊 Análise completa de desempenho: ");
        System.out.println("=".repeat(120));

        // 1. RESUMO EXECUTIVO
        System.out.println("\n1. 📈 RESUMO EXECUTIVO:");
        gerarResumoExecutivo(resultados);

        // 2. COMPARAÇÃO POR TIPO DE TABELA HASH
        System.out.println("\n2. 🔄 COMPARAÇÃO POR TIPO DE TABELA HASH:");
        compararPorTipoTabela(resultados);

        // 3. COMPARAÇÃO POR FUNÇÃO HASH
        System.out.println("\n3. 🎯 COMPARAÇÃO POR FUNÇÃO HASH:");
        compararPorFuncaoHash(resultados);

        // 4. COMPARAÇÃO POR FATOR DE CARGA
        System.out.println("\n4. ⚖️  COMPARAÇÃO POR FATOR DE CARGA:");
        compararPorFatorCarga(resultados);

        // 5. ANÁLISE DETALHADA
        System.out.println("\n5. 🔍 ANÁLISE DETALHADA POR CENÁRIO:");
        exibirAnaliseDetalhada(resultados);

        System.out.println("\n✅ execução concluída!");
        System.out.println("📊 Para gerar os gráficos com base nos resultados, use o arquivo python.");
    }

    private static void gerarResumoExecutivo(ArrayList<ResultadoTestes> resultados) {
        ResultadoTestes melhorInsercao = resultados.get(0);
        ResultadoTestes melhorBusca = resultados.get(0);
        ResultadoTestes menorColisoes = resultados.get(0);

        for (ResultadoTestes res : resultados) {
            if (res.getTempoInsercao() < melhorInsercao.getTempoInsercao()) {
                melhorInsercao = res;
            }
            if (res.getTempoBusca() < melhorBusca.getTempoBusca()) {
                melhorBusca = res;
            }
            if (res.getColisoes() < menorColisoes.getColisoes()) {
                menorColisoes = res;
            }
        }

        System.out.printf("   • Melhor inserção: %s (%d ms)\n",
                melhorInsercao.getDescricao(), melhorInsercao.getTempoInsercao());
        System.out.printf("   • Melhor busca: %s (%d ms)\n",
                melhorBusca.getDescricao(), melhorBusca.getTempoBusca());
        System.out.printf("   • Menor colisões: %s (%d colisões)\n",
                menorColisoes.getDescricao(), menorColisoes.getColisoes());
    }

    private static void compararPorTipoTabela(ArrayList<ResultadoTestes> resultados) {
        Map<String, ArrayList<ResultadoTestes>> porTipo = new HashMap<>();
        porTipo.put("Encadeamento", new ArrayList<>());
        porTipo.put("Sondagem Linear", new ArrayList<>());
        porTipo.put("Sondagem Quadrática", new ArrayList<>());

        for (ResultadoTestes res : resultados) {
            porTipo.get(res.getTipoTabela()).add(res);
        }

        for (Map.Entry<String, ArrayList<ResultadoTestes>> entry : porTipo.entrySet()) {
            String tipo = entry.getKey();
            ArrayList<ResultadoTestes> lista = entry.getValue();

            long mediaInsercao = 0, mediaBusca = 0, mediaColisoes = 0;
            for (ResultadoTestes res : lista) {
                mediaInsercao += res.getTempoInsercao();
                mediaBusca += res.getTempoBusca();
                mediaColisoes += res.getColisoes();
            }

            if (!lista.isEmpty()) {
                mediaInsercao /= lista.size();
                mediaBusca /= lista.size();
                mediaColisoes /= lista.size();

                System.out.printf("   • %s: %dms ins, %dms bus, %d colisões (média)\n",
                        tipo, mediaInsercao, mediaBusca, mediaColisoes);
            }
        }
    }

    private static void compararPorFuncaoHash(ArrayList<ResultadoTestes> resultados) {
        Map<String, ArrayList<ResultadoTestes>> porFuncao = new HashMap<>();
        porFuncao.put("Divisão", new ArrayList<>());
        porFuncao.put("Multiplicação", new ArrayList<>());
        porFuncao.put("Polinomial", new ArrayList<>());

        for (ResultadoTestes res : resultados) {
            porFuncao.get(res.getNomeFuncao()).add(res);
        }

        for (Map.Entry<String, ArrayList<ResultadoTestes>> entry : porFuncao.entrySet()) {
            String funcao = entry.getKey();
            ArrayList<ResultadoTestes> lista = entry.getValue();

            long mediaInsercao = 0, mediaBusca = 0, mediaColisoes = 0;
            for (ResultadoTestes res : lista) {
                mediaInsercao += res.getTempoInsercao();
                mediaBusca += res.getTempoBusca();
                mediaColisoes += res.getColisoes();
            }

            if (!lista.isEmpty()) {
                mediaInsercao /= lista.size();
                mediaBusca /= lista.size();
                mediaColisoes /= lista.size();

                System.out.printf("   • %s: %dms ins, %dms bus, %d colisões (média)\n",
                        funcao, mediaInsercao, mediaBusca, mediaColisoes);
            }
        }
    }

    private static void compararPorFatorCarga(ArrayList<ResultadoTestes> resultados) {
        Map<String, ArrayList<ResultadoTestes>> porFatorCarga = new HashMap<>();

        for (ResultadoTestes res : resultados) {
            double fator = res.getFatorCarga();
            String categoria;

            if (fator < 0.1) categoria = "α<0.10";
            else if (fator < 0.3) categoria = "α=0.10-0.30";
            else if (fator < 0.5) categoria = "α=0.30-0.50";
            else if (fator < 0.7) categoria = "α=0.50-0.70";
            else if (fator < 0.9) categoria = "α=0.70-0.90";
            else categoria = "α≥0.90";

            if (!porFatorCarga.containsKey(categoria)) {
                porFatorCarga.put(categoria, new ArrayList<>());
            }
            porFatorCarga.get(categoria).add(res);
        }

        for (Map.Entry<String, ArrayList<ResultadoTestes>> entry : porFatorCarga.entrySet()) {
            String fator = entry.getKey();
            ArrayList<ResultadoTestes> lista = entry.getValue();

            long mediaInsercao = 0, mediaBusca = 0, mediaColisoes = 0;
            for (ResultadoTestes res : lista) {
                mediaInsercao += res.getTempoInsercao();
                mediaBusca += res.getTempoBusca();
                mediaColisoes += res.getColisoes();
            }

            if (!lista.isEmpty()) {
                mediaInsercao /= lista.size();
                mediaBusca /= lista.size();
                mediaColisoes /= lista.size();

                System.out.printf("   • %s: %dms ins, %dms bus, %d colisões (média)\n",
                        fator, mediaInsercao, mediaBusca, mediaColisoes);
            }
        }
    }

    private static void exibirAnaliseDetalhada(ArrayList<ResultadoTestes> resultados) {
        for (ResultadoTestes res : resultados) {
            System.out.println("   " + res.toStringDetalhado());
        }
    }
}