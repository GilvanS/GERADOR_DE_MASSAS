package br.com.geradormassa.model;

import br.com.geradormassa.writers.CsvColumn;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Produto {

    // ===== ORDENS ATUALIZADAS (incrementadas em 1) =====
    @CsvColumn(header = "NomeProduto", order = 20)
    private String nomeProduto;

    @CsvColumn(header = "Preco", order = 21)
    private String preco;

    @CsvColumn(header = "DescricaoProduto", order = 22)
    private String descricaoProduto;

    @CsvColumn(header = "Quantidade", order = 23)
    private int quantidade;

    @CsvColumn(header = "NomeCategoria", order = 24)
    private String nomeCategoria;

    @CsvColumn(header = "DescricaoCategoria", order = 25)
    private String descricaoCategoria;
}