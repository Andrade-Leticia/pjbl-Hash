import matplotlib.pyplot as plt
import seaborn as sns
import pandas as pd
import os

plt.style.use('default')
sns.set_palette("husl")

def coletar_dados_usuario():
    print("📊 Coletor de dados: ")
    print("=" * 50)
    print("Digite os resultados dos seus testes (deixe em branco para parar):\n")

    dados = []

    while True:
        print(f"Registro #{len(dados) + 1}:")

        tipo_tabela = input("Tipo de Tabela (Encadeamento/Sondagem Linear/Sondagem Quadrática): ").strip()
        if not tipo_tabela:
            break

        funcao_hash = input("Função Hash (Divisão/Multiplicação/Polinomial): ").strip()
        if not funcao_hash:
            break

        try:
            fator_carga = float(input("Fator de Carga (ex: 0.10, 0.50, 1.00): "))
            tempo_insercao = int(input("Tempo de Inserção (ms): "))
            tempo_busca = int(input("Tempo de Busca (ms): "))
            colisoes = int(input("Número de Colisões: "))

            eficiencia = colisoes / (fator_carga * 100000) if fator_carga > 0 else 0

            dados.append({
                'TipoTabela': tipo_tabela,
                'FuncaoHash': funcao_hash,
                'FatorCarga': fator_carga,
                'TempoInsercao': tempo_insercao,
                'TempoBusca': tempo_busca,
                'Colisoes': colisoes,
                'Eficiencia': eficiencia
            })

            print("dados adicionados!\n")

        except ValueError:
            print(" Erro: valores numéricos inválidos.\n")
            continue

    return pd.DataFrame(dados)

def grafico_insercao_vs_fator_carga(df):
    plt.figure(figsize=(12, 8))

    for tipo in df['TipoTabela'].unique():
        subset = df[df['TipoTabela'] == tipo].sort_values('FatorCarga')
        plt.plot(subset['FatorCarga'], subset['TempoInsercao'],
                 'o-', linewidth=2, markersize=6, label=tipo)

    plt.xlabel('Fator de Carga (α)', fontsize=12)
    plt.ylabel('Tempo de Inserção (ms)', fontsize=12)
    plt.title('Tempo de Inserção vs Fator de Carga', fontsize=14, fontweight='bold')
    plt.legend()
    plt.grid(True, alpha=0.3)
    plt.tight_layout()
    plt.savefig('graficos/insercao_vs_fator_carga.png', dpi=300, bbox_inches='tight')
    plt.close()

def grafico_busca_vs_fator_carga(df):
    plt.figure(figsize=(12, 8))

    for tipo in df['TipoTabela'].unique():
        subset = df[df['TipoTabela'] == tipo].sort_values('FatorCarga')
        plt.plot(subset['FatorCarga'], subset['TempoBusca'],
                 's-', linewidth=2, markersize=6, label=tipo)

    plt.xlabel('Fator de Carga (α)', fontsize=12)
    plt.ylabel('Tempo de Busca (ms)', fontsize=12)
    plt.title('Tempo de Busca vs Fator de Carga', fontsize=14, fontweight='bold')
    plt.legend()
    plt.grid(True, alpha=0.3)
    plt.tight_layout()
    plt.savefig('graficos/busca_vs_fator_carga.png', dpi=300, bbox_inches='tight')
    plt.close()

def grafico_colisoes_vs_fator_carga(df):
    plt.figure(figsize=(12, 8))

    for tipo in df['TipoTabela'].unique():
        subset = df[df['TipoTabela'] == tipo].sort_values('FatorCarga')
        plt.plot(subset['FatorCarga'], subset['Colisoes'],
                 '^-', linewidth=2, markersize=6, label=tipo)

    plt.xlabel('Fator de Carga (α)', fontsize=12)
    plt.ylabel('Número de Colisões', fontsize=12)
    plt.title('Colisões vs Fator de Carga', fontsize=14, fontweight='bold')
    plt.legend()
    plt.grid(True, alpha=0.3)
    plt.yscale('log')
    plt.tight_layout()
    plt.savefig('graficos/colisoes_vs_fator_carga.png', dpi=300, bbox_inches='tight')
    plt.close()

def grafico_eficiencia_tipo_tabela(df):
    plt.figure(figsize=(10, 6))

    eficiencia_por_tipo = df.groupby('TipoTabela')['Eficiencia'].mean()

    bars = plt.bar(eficiencia_por_tipo.index, eficiencia_por_tipo.values,
                   color=['#FF6B6B', '#4ECDC4', '#45B7D1'])

    for bar in bars:
        height = bar.get_height()
        plt.text(bar.get_x() + bar.get_width()/2., height,
                 f'{height:.4f}', ha='center', va='bottom')

    plt.xlabel('Tipo de Tabela Hash', fontsize=12)
    plt.ylabel('Eficiência (Colisões/Elemento)', fontsize=12)
    plt.title('Eficiência Média por Tipo de Tabela Hash', fontsize=14, fontweight='bold')
    plt.tight_layout()
    plt.savefig('graficos/eficiencia_tipo_tabela.png', dpi=300, bbox_inches='tight')
    plt.close()

def grafico_comparacao_funcoes_hash(df):
    fig, (ax1, ax2, ax3) = plt.subplots(1, 3, figsize=(18, 6))

    tempo_ins = df.groupby('FuncaoHash')['TempoInsercao'].mean()
    ax1.bar(tempo_ins.index, tempo_ins.values, color=['#FF9999', '#66B2FF', '#99FF99'])
    ax1.set_title('Tempo Médio de Inserção', fontweight='bold')
    ax1.set_ylabel('Tempo (ms)')

    tempo_busca = df.groupby('FuncaoHash')['TempoBusca'].mean()
    ax2.bar(tempo_busca.index, tempo_busca.values, color=['#FF9999', '#66B2FF', '#99FF99'])
    ax2.set_title('Tempo Médio de Busca', fontweight='bold')
    ax2.set_ylabel('Tempo (ms)')

    colisoes = df.groupby('FuncaoHash')['Colisoes'].mean()
    ax3.bar(colisoes.index, colisoes.values, color=['#FF9999', '#66B2FF', '#99FF99'])
    ax3.set_title('Colisões Médias', fontweight='bold')
    ax3.set_ylabel('Número de Colisões')

    plt.tight_layout()
    plt.savefig('graficos/comparacao_funcoes_hash.png', dpi=300, bbox_inches='tight')
    plt.close()

def grafico_heatmap_desempenho(df):
    plt.figure(figsize=(10, 8))

    pivot_table = df.pivot_table(values='Eficiencia',
                                 index='TipoTabela',
                                 columns='FuncaoHash',
                                 aggfunc='mean')

    sns.heatmap(pivot_table, annot=True, fmt='.4f', cmap='YlOrRd',
                cbar_kws={'label': 'Eficiência (Colisões/Elemento)'})

    plt.title('Heatmap de Eficiência: Tipo de Tabela vs Função Hash',
              fontsize=14, fontweight='bold')
    plt.tight_layout()
    plt.savefig('graficos/heatmap_desempenho.png', dpi=300, bbox_inches='tight')
    plt.close()

def main():
    """Função principal"""
    print("Gerador de gráficos de tabela Hash")
    print("=" * 40)

    df = coletar_dados_usuario()

    if df.empty:
        print("❌ Nenhum dado foi inserido. Encerrando...")
        return

    print(f"\n📈 Dados coletados: {len(df)} registros")
    print("\nResumo dos dados:")
    print(df[['TipoTabela', 'FuncaoHash', 'FatorCarga', 'TempoInsercao', 'TempoBusca', 'Colisoes']].to_string(index=False))

    os.makedirs('graficos', exist_ok=True)

    print("\n🔄 Gerando gráficos...")

    try:
        grafico_insercao_vs_fator_carga(df)
        grafico_busca_vs_fator_carga(df)
        grafico_colisoes_vs_fator_carga(df)
        grafico_eficiencia_tipo_tabela(df)
        grafico_comparacao_funcoes_hash(df)
        grafico_heatmap_desempenho(df)

        print(f"\ Gráficos gerados na pasta 'graficos/'!")

    except Exception as e:
        print(f" Erro ao gerar gráficos: {e}")

if __name__ == "__main__":
    main()