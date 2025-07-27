package br.com.geradormassa.generators;

import br.com.geradormassa.model.Artigo;
import br.com.geradormassa.model.Massa;
import br.com.geradormassa.model.Produto;
import br.com.geradormassa.model.Usuario;

/**
 * Orquestra a geração da massa de dados completa, utilizando geradores específicos
 * para cada parte do modelo. Padrão Factory/Builder.
 */
public class MassaGenerator implements Gerador<Massa> {

    // O método estático antigo foi removido para favorecer a injeção de dependência
    // e uma arquitetura mais limpa baseada em objetos.

    @Override
    public Massa gerar() {
        // 1. Ponto único de chamada para a API externa
        FakerApiData dadosApi = FakerApiData.gerarDados();

        // 2. Instancia e utiliza os geradores específicos, passando os dados necessários
        Gerador<Usuario> usuarioGenerator = new UsuarioGenerator(dadosApi);
        Gerador<Produto> produtoGenerator = new ProdutoGenerator(); // Não depende da API
        Gerador<Artigo> artigoGenerator = new ArtigoGenerator(dadosApi);

        // 3. Orquestra a geração de cada parte
        Usuario usuario = usuarioGenerator.gerar();
        Produto produto = produtoGenerator.gerar();
        Artigo artigo = artigoGenerator.gerar();

        // 4. Compõe o objeto Massa final
        return Massa.builder()
                .usuario(usuario)
                .produto(produto)
                .artigo(artigo)
                .build();
    }
}