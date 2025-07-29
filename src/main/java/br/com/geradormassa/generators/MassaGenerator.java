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
        Artigo artigoOriginal = artigoGenerator.gerar();

        // 4. Garante a consistência dos dados, fazendo com que o autor do artigo
        //    seja o mesmo que o nome completo do usuário gerado.
        Artigo artigoCorrigido = Artigo.builder()
                .tituloArtigo(artigoOriginal.getTituloArtigo())
                .conteudoArtigo(artigoOriginal.getConteudoArtigo())
                .nomeAutor(usuario.getNomeCompleto()) // <-- CORREÇÃO APLICADA
                .dataPublicacao(artigoOriginal.getDataPublicacao())
                .build();

        // 5. Compõe o objeto Massa final com o artigo corrigido
        return Massa.builder()
                .usuario(usuario)
                .produto(produto)
                .artigo(artigoCorrigido)
                .build();
    }
}