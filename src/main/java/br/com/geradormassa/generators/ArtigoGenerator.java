package br.com.geradormassa.generators;

import br.com.geradormassa.model.Artigo;
import br.com.geradormassa.utils.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class ArtigoGenerator implements Gerador<Artigo> {

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private final FakerApiData dadosApi;

    // Recebe os dados da API para não fazer uma nova chamada
    public ArtigoGenerator(FakerApiData dadosApi) {
        this.dadosApi = dadosApi;
    }

    @Override
    public Artigo gerar() {
        String nomeAutor = StringUtils.removerAcentos(dadosApi.getFullName());
        String dataPublicacao = LocalDateTime.now(ZoneOffset.UTC).format(ISO_FORMATTER);

        return Artigo.builder()
                .tituloArtigo(StringUtils.removerAcentos(dadosApi.getArticleTitle()))
                .conteudoArtigo(StringUtils.removerAcentos(dadosApi.getArticleContent()))
                .nomeAutor(nomeAutor)
                .dataPublicacao(dataPublicacao)
                .build();
    }
}