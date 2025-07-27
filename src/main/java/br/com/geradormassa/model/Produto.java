package br.com.geradormassa.model;

import br.com.geradormassa.writers.CsvColumn;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Produto {

    @CsvColumn(header = "NomeProduto", order = 17)
    private String nomeProduto;

    @CsvColumn(header = "Preco", order = 18)
    private String preco;

    @CsvColumn(header = "DescricaoProduto", order = 19)
    private String descricaoProduto;

    @CsvColumn(header = "Quantidade", order = 20)
    private int quantidade;

    @CsvColumn(header = "NomeCategoria", order = 21)
    private String nomeCategoria;

    @CsvColumn(header = "DescricaoCategoria", order = 22)
    private String descricaoCategoria;
}