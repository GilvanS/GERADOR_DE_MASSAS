package br.com.geradormassa.model;

import br.com.geradormassa.writers.CsvColumn;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Artigo {

    @CsvColumn(header = "TituloArtigo", order = 23)
    private String tituloArtigo;

    @CsvColumn(header = "ConteudoArtigo", order = 24)
    private String conteudoArtigo;

    @CsvColumn(header = "NomeAutor", order = 25)
    private String nomeAutor;

    @CsvColumn(header = "DataPublicacao", order = 26)
    private String dataPublicacao;
}