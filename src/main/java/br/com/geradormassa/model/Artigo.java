package br.com.geradormassa.model;

import br.com.geradormassa.writers.CsvColumn;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Artigo {

    // ===== ORDENS ATUALIZADAS (incrementadas em 1) =====
    @CsvColumn(header = "TituloArtigo", order = 26)
    private String tituloArtigo;

    @CsvColumn(header = "ConteudoArtigo", order = 27)
    private String conteudoArtigo;

    @CsvColumn(header = "NomeAutor", order = 28)
    private String nomeAutor;

    @CsvColumn(header = "DataPublicacao", order = 29)
    private String dataPublicacao;
}