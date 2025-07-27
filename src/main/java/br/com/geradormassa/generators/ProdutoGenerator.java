package br.com.geradormassa.generators;

import br.com.geradormassa.model.Produto;
import br.com.geradormassa.utils.StringUtils;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * Gera dados de produtos de forma local e confiável, implementando a interface Gerador.
 * Agora, toda a lógica de criação de um produto, incluindo quantidade e descrição,
 * está centralizada aqui.
 */
public class ProdutoGenerator implements Gerador<Produto> { // 1. Implementa a interface

    private static final Random random = new Random();
    // Formata o preço com ponto, para consistência.
    private static final DecimalFormat df = new DecimalFormat("0.00");

    private static final String[] NOMES_PRODUTO = {
            "Smartphone", "Notebook", "Fone de Ouvido", "Smartwatch", "Tablet", "Camera Digital",
            "Teclado Mecanico", "Mouse Gamer", "Monitor 4K", "Cadeira Gamer"
    };

    private static final String[] CATEGORIAS = {
            "Eletronicos", "Informatica", "Acessorios", "Fotografia", "Perifericos", "Games"
    };

    private static final String[] ADJETIVOS = {
            "Moderno", "Potente", "Sem Fio", "Ultra-fino", "Profissional", "RGB", "Ergonomica"
    };

    // 2. O construtor agora é público (o construtor padrão, que é público).
    public ProdutoGenerator() {}

    /**
     * Gera um objeto Produto completo com dados aleatórios e consistentes.
     * @return Um objeto {@link Produto} preenchido.
     */
    @Override // 3. Implementa o método da interface
    public Produto gerar() {
        String categoria = CATEGORIAS[random.nextInt(CATEGORIAS.length)];
        String nomeBase = NOMES_PRODUTO[random.nextInt(NOMES_PRODUTO.length)];
        String adjetivo = ADJETIVOS[random.nextInt(ADJETIVOS.length)];
        String nomeProduto = nomeBase + " " + adjetivo;

        String descricao = "Um excelente " + nomeBase.toLowerCase() + " " + adjetivo.toLowerCase() +
                " da categoria " + categoria + ", ideal para suas necessidades.";

        // Gera um preço entre 50.00 e 4999.99
        double precoDouble = 50.0 + (4950.0 * random.nextDouble());
        String preco = df.format(precoDouble).replace(",", "."); // Garante ponto como separador

        // Gera uma quantidade aleatória e a descrição da categoria
        int quantidade = random.nextInt(100) + 1;
        String descricaoCategoria = "Descricao para a categoria " + categoria;

        return Produto.builder()
                .nomeProduto(StringUtils.removerAcentos(nomeProduto))
                .preco(preco)
                .descricaoProduto(StringUtils.removerAcentos(descricao))
                .quantidade(quantidade)
                .nomeCategoria(StringUtils.removerAcentos(categoria))
                .descricaoCategoria(StringUtils.removerAcentos(descricaoCategoria))
                .build();
    }
}