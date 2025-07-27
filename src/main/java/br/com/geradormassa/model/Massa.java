package br.com.geradormassa.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Massa {
    // A classe Massa agora compõe os outros modelos, organizando o código.
    private Usuario usuario;
    private Produto produto;
    private Artigo artigo;
}