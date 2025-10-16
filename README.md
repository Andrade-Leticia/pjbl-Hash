# Projeto PJBL - Análise de Desempenho de Tabelas Hash

## Feito por: 
  - Alana da Conceição Queiroz
  - Brenda Gabrielli Barbosa
  - Leticia Maria Maia de Andrade Vieira

## 1. Introdução
Este projeto visa analisar e comparar o desempenho de diferentes implementações e funções de dispersão (hash) em tabelas hash, utilizando 27 combinações de tamanhos de vetor ($M$), conjuntos de dados ($N$) e funções hash. O objetivo é avaliar a eficiência de cada abordagem em termos de tempo de inserção, tempo de busca e número de colisões, sob diferentes fatores de carga.
Estratégias de Tratamento de Colisão:
  - ImplementadasEncadeamento Separado (TabelaHashEncadeamento.java).
  - Rehashing (Sondagem Linear) (TabelaHashSondagemLinear.java).
  - Rehashing (Sondagem Quadrática) (TabelaHashSondagemQuadratica.java).

## 2. Estrutura do Código
Arquivos:
  - ExecutarTestes.java: Orquestra os $27$ testes, gera os conjuntos de dados, mede os tempos de Inserção e Busca, coleta as estatísticas específicas de cada tabela e exibe o relatório final consolidado.
  - FuncoesHash.java: Implementa e unifica as três funções hash a serem comparadas.
  - GeradorDados.java: Responsável por gerar os conjuntos de dados ($N$) de forma consistente, utilizando uma SEED fixa.
  - Registro.java: Classe do objeto a ser inserido (Chave: String de 9 dígitos; Valor: int).
  - ResultadoTestes.java: Classe para armazenar todas as métricas (tempos, colisões, estatísticas, etc.) de uma única execução de teste, facilitando a geração do relatório.
  - EstatisticasGaps.java / EstatisticasLista.java: Classes auxiliares para armazenar métricas específicas de Sondagem e Encademento, respectivamente.

Implementações de Tabela Hash:
  - Endereçamento Aberto com Sondagem Linear (TabelaHashSondagemLinear.java)
  - Endereçamento Aberto com Sondagem Quadrática (TabelaHashSondagemQuadratica.java)
  - Encadeamento Separado (TabelaHashEncadeamento.java)

Funções de Hash:
  - Método da Divisão (hashDivisao)
  - Método da Multiplicação (hashMultiplicacao)
  - Polinomial (hashPolinomial)

Geração de Dados:
  - Criação de conjuntos de dados com 100.000, 1 milhão e 10 milhões de registros (GeradorDados.java). A semente (seed) é fixa para garantir a reprodutibilidade dos testes.

Testes de Desempenho:
  - Execução de um conjunto completo de testes que combina cada tipo de tabela com cada função de hash e diferentes tamanhos de conjunto de dados (ExecutarTestes.java).

Análise de Resultados:
  - Coleta de métricas detalhadas, incluindo tempos de execução, contagem de colisões, análise de listas(para encadeamento) e análise de agrupamentos(gaps) para sondagem(ResultadoTestes.java, EstatisticasGaps.java, EstatisticasLista.java).

Visualização de Dados:
  - Um script em Python (gerar_graficos.py) para gerar gráficos comparativos a partir dos dados coletados nos testes.

## 3. Parâmetros e Escolhas de Implementação
Parâmetro | Detalhe | Classes Envolvidas  
SEED Fixa | 42      | GeradorDados.java
Formato da Chave | String de 9 dígitos (ex: 000123456) | "Registro.java, GeradorDados.java"

# 3.2 Tamanhos do Vetor Hash ($M$) e Conjuntos de Dados ($N$)
Foram selecionados três tamanhos de vetor ($M$) e três tamanhos de conjunto ($N$), garantindo a variação de no mínimo $x10$:
  Tamanho (M) | Valor | Tamanho (N) | Valor
  M1​ | 10.000 | N1​ | 100.000
  M2​ | 100.000 | N2 |1.000.000
  M3 |,1.000.000 | N3​ | 10.000.000
O Fator de Carga ($\alpha = N/M$) varia de $0.10$ até $1000.00$, permitindo analisar o desempenho da Tabela Hash sob diferentes níveis de estresse.

# 3.3 Funções Hash Comparadas
Código | Nome da Função | Estratégia Principal
1 | Divisão (Resto) | h(k)=k(modM)
2 | Multiplicação | Utiliza a constante de Knuth (≈0.61803) para dispersão.
3 | Polinomial | Usa soma ponderada de caracteres (String) e módulo.

  - Tabela Hash com Encadeamento Separado (TabelaHashEncadeamento.java): Estratégia de tratamento de colisões que utiliza listas encadeadas para armazenar múltiplos elementos que mapeiam para o mesmo índice da   tabela. Métrica de Colisão: O contador de colisões é incrementado a cada inserção na mesma posição e a cada passo de iteração dentro da lista encadeada, representando o número de comparações de chaves. Métrica Adicional: Coleta o tamanho das 3 maiores listas encadeadas e a média do tamanho das listas não vazias.
  - Tabela Hash com Sondagem Linear (TabelaHashSondagemLinear.java): Técnica de endereçamento aberto que, em caso de colisão, inspeciona sequencialmente as posições subsequentes da tabela até encontrar um espaço  disponível. Fórmula de Sonda: $h(k, i) = (h'(k) + i) \pmod M$, onde $i$ é o passo. Métrica de Colisão: O contador de colisões é incrementado a cada posição de teste (while) que está ocupada por uma chave     diferente. Métrica Adicional: Análise de Gaps (sequências de posições vazias) para medir a aglomeração primária.
  - Tabela Hash com Sondagem Quadrática (TabelaHashSondagemQuadratica.java): Variação da sondagem que verifica as posições da tabela em incrementos quadráticos a partir do ponto de colisão inicial, visando mitigar o agrupamento primário. Fórmula de Sonda: $h(k, i) = (h'(k) + c_1 \cdot i + c_2 \cdot i^2) \pmod M$ -> Constantes: $c_1 = 1$ e $c_2 = 3$. Métrica de Colisão: Idêntica à Sondagem Linear (incrementada a cada teste de posição). Métrica Adicional: Análise de Gaps para medir a aglomeração secundária (similar à Sondagem Linear, para comparação direta).

## 4. Como Executar e Analisar
# 4.1 Execução do Teste (Java)
  1. Compile todos os arquivos Java
  2. Execute o orquestrador de testes
  3. O programa imprimirá no console um relatório detalhado de todas as 27 combinações, incluindo: Fator de Carga Teórico. Tempo de Inserção (ms) e Busca (ms). Número total de Colisões. Estatísticas específicas (maior lista ou maior gap).

# 4.2 Geração dos Gráficos (Python)
Após coletar os resultados do console Java, utilize o script gerador_graficos.py (ou analise.py):
  1. Instale as dependências: pip install pandas matplotlib seaborn
  2. Execute o script Python: python gerador_graficos.py
  3. O script pedirá a você que insira os dados manualmente (Tipo de Tabela, Função Hash, Fator de Carga, Tempos e Colisões) coletados na etapa 4.1.
  4. Ao finalizar a inserção, ele gerará automaticamente 6 gráficos de análise na pasta graficos/, essenciais para a sua discussão final.

### 5. Análise dos Resultados
  # Tabela 1: Tempos de Inserção (ms)

  # Tabela 2: Tempos de Busca (ms)

  # Tabela 3: Colisões Totais

# 5.1 Gráficos de Desempenho
  - insercao_vs_fator_carga.png: Comparação dos Tempos de Inserção em função do Fator de Carga ($\alpha$) por tipo de tabela.
  - busca_vs_fator_carga.png: Comparação dos Tempos de Busca em função do Fator de Carga ($\alpha$) por tipo de tabela.
  - colisoes_vs_fator_carga.png: Comparação do Número de Colisões em escala logarítmica em função do Fator de Carga ($\alpha$) por tipo de tabela.
  - heatmap_desempenho.png: Matriz de calor da Eficiência (média de Colisões/Elemento) comparando Funções Hash vs. Tipos de Tabela.

## 6. Conclusão e Discussão
# 6.1 Análise do Fator de Carga ($\alpha$)
  - Encadeamento Separado: O Encadeamento mostrou-se a estratégia mais tolerante a altos Fatores de Carga ($\alpha \gg 1$). Nos cenários extremos ($\alpha=100$ e $\alpha=1000$), a inserção e busca foram possíveis, embora o tempo cresça linearmente com $\alpha$, pois a busca degenera para uma busca em lista ligada.
  - Sondagem (Linear e Quadrática): O desempenho das técnicas de Sondagem degradou-se drasticamente conforme $\alpha$ se aproxima de $1.0$. Para $\alpha \ge 1.0$, a Sondagem Linear e Quadrática se tornaram inviáveis ou extremamente lentas, confirmando que a performance dessas abordagens depende estritamente da manutenção de um baixo fator de carga (idealmente $\alpha < 0.7$).

# 6.2 Desempenho das Funções Hash
A análise das colisões, especialmente no Encandeamento (onde $\alpha$ pode ser alto), revelou diferenças na eficácia das funções:
  1. Função Polinomial: Demonstrou consistentemente a menor média de colisões (e, portanto, a maior eficiência) para chaves do tipo String, sugerindo uma distribuição mais uniforme dos índices.
  2. Função Multiplicação (Knuth): Também apresentou resultados robustos, superando a Divisão, especialmente em tamanhos de tabela $M$ não primos, como esperado, evitando problemas de má escolha do módulo.
  3. Função Divisão: Foi funcional, mas gerou um número ligeiramente maior de colisões em comparação com as outras, indicando uma distribuição de dados menos ideal para esta função.

# 6.3 Conclusão Final
O projeto confirma que não existe uma única "melhor" Tabela Hash. A escolha ideal depende do cenário de uso:
  - Se o Fator de Carga não puder ser mantido baixo (muitas inserções em espaço limitado), o Encadeamento Separado é a única solução viável.
  - Se o Fator de Carga for garantidamente baixo ($\alpha \le 0.5$) e a eficiência de memória/cache for crítica, a Sondagem Linear oferece o melhor tempo de execução.
  - Independentemente da estratégia de colisão, o uso de uma função como a Polinomial ou a Multiplicação é crucial para garantir a melhor dispersão dos dados e minimizar colisões, que são o fator primário na degradação da performance (aumento de $O(1)$ para $O(N)$ ou $O(N/M)$).

## 7. Arquivos Para Prova de Autoria

[sem comentarios.zip](https://github.com/user-attachments/files/22939198/sem.comentarios.zip)
