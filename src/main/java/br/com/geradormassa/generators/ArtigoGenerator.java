package br.com.geradormassa.generators;

import br.com.geradormassa.model.Artigo;
import br.com.geradormassa.utils.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class ArtigoGenerator implements Gerador<Artigo> {

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static final Random random = new Random();

    private final FakerApiData dadosApi;

    // --- TEMPLATES PARA GERAÇÃO DE ARTIGOS TEMÁTICOS (SUPER EXPANDIDO) ---

    private static final String[] CATEGORIAS = {
            // Originais
            "Tecnologia", "Programação", "Universo e Astronomia", "Ciência e Física",
            "Saúde e Bem-Estar", "Educação", "História", "Cultura Pop",
            // Novas Categorias
            "Finanças e Investimentos", "Viagens e Turismo", "Gastronomia e Culinária", "Meio Ambiente e Sustentabilidade", "Esportes"
    };

    private static final String[][] TEMPLATES_TITULO = {
            // Tecnologia
            {"O Futuro da {TEMA}", "Como a {TEMA} Está Moldando o Mundo", "5 Tendências em {TEMA} para Ficar de Olho"},
            // Programação
            {"Guia Completo de {TEMA} para Iniciantes", "Otimizando Performance com {TEMA}", "Por que Aprender {TEMA} em {ANO}?"},
            // Universo e Astronomia
            {"Desvendando os Mistérios de {TEMA}", "O Telescópio James Webb e a Busca por {TEMA}", "A Incrível Jornada até {TEMA}"},
            // Ciência e Física
            {"Uma Introdução à {TEMA}", "Como a {TEMA} Explica o Cotidiano", "Os Limites do Conhecimento em {TEMA}"},
            // Saúde e Bem-Estar
            {"10 Dicas para Melhorar sua {TEMA}", "Os Benefícios da {TEMA} para a Mente", "Alimentação e {TEMA}: Uma Conexão Vital"},
            // Educação
            {"A Revolução da {TEMA} na Sala de Aula", "Métodos Inovadores de {TEMA}", "O Papel da {TEMA} no Desenvolvimento Infantil"},
            // História
            {"A Ascensão e Queda do {TEMA}", "O Legado do {TEMA} na Sociedade Moderna", "Como o {TEMA} Mudou o Curso da História"},
            // Cultura Pop
            {"Análise Profunda de {TEMA}", "O Impacto Cultural de {TEMA}", "Por que {TEMA} Continua Relevante Hoje?"},
            // Finanças e Investimentos (NOVO)
            {"Guia de {TEMA} para Iniciantes", "Como Investir em {TEMA} com Segurança", "Entendendo o Mercado de {TEMA}"},
            // Viagens e Turismo (NOVO)
            {"Roteiro Inesquecível por {TEMA}", "As Melhores Dicas para Viajar para {TEMA}", "O que Fazer em {TEMA}: Um Guia Completo"},
            // Gastronomia e Culinária (NOVO)
            {"A Receita Perfeita de {TEMA}", "A História Surpreendente da {TEMA}", "Segredos da Culinária com {TEMA}"},
            // Meio Ambiente (NOVO)
            {"O Impacto do {TEMA} no Planeta", "Soluções Sustentáveis: Como Combater o {TEMA}", "Um Futuro com {TEMA}: É Possível?"},
            // Esportes (NOVO)
            {"Análise Tática do {TEMA}", "A Carreira Lendária de {TEMA}", "Os Maiores Momentos da História do {TEMA}"}
    };

    private static final String[][] PALAVRAS_CHAVE = {
            // Tecnologia
            {"Inteligência Artificial", "Computação Quântica", "Realidade Aumentada", "Blockchain", "5G"},
            // Programação
            {"Python", "JavaScript", "APIs REST", "Micro-serviços", "Clean Code", "Docker"},
            // Universo e Astronomia
            {"Buracos Negros", "Exoplanetas", "Nebulosas", "Matéria Escura", "Galáxias Distantes"},
            // Ciência e Física
            {"Teoria da Relatividade", "Física Quântica", "Genética", "Neurociência", "Leis de Newton"},
            // Saúde e Bem-Estar
            {"Saúde Mental", "Nutrição Funcional", "Meditação", "Qualidade de Sono", "Atividade Física"},
            // Educação
            {"Aprendizagem Híbrida", "Gamificação", "Inteligência Emocional", "Robótica Educacional", "STEAM"},
            // História
            {"Império Romano", "Antigo Egito", "Revolução Industrial", "Guerra Fria", "Renascimento"},
            // Cultura Pop
            {"Universo Cinematográfico Marvel", "Star Wars", "Animes Clássicos", "A Indústria dos Games", "Séries de TV"},
            // Finanças e Investimentos (NOVO)
            {"Ações da Bolsa", "Criptomoedas", "Renda Fixa", "Fundos Imobiliários", "Planejamento Financeiro"},
            // Viagens e Turismo (NOVO)
            {"Sudeste Asiático", "Europa Central", "Patagônia", "Japão", "Litoral do Nordeste"},
            // Gastronomia e Culinária (NOVO)
            {"Cozinha Italiana", "Fermentação Natural", "Churrasco Americano", "Comida Vegana Gourmet", "Vinhos Franceses"},
            // Meio Ambiente (NOVO)
            {"Aquecimento Global", "Desmatamento", "Poluição Plástica", "Energias Renováveis", "Consumo Consciente"},
            // Esportes (NOVO)
            {"Futebol Europeu", "Basquete da NBA", "Fórmula 1", "Tênis", "MMA"}
    };

    private static final String[] TEMPLATES_CONTEUDO = {
            "Neste artigo, mergulhamos fundo no tópico de {TITULO}. Exploramos como a {CATEGORIA} tem evoluído e o impacto que {PALAVRA_CHAVE} tem em nosso dia a dia. Acompanhe a análise completa e descubra as últimas tendências.",
            "Uma análise detalhada sobre {TITULO}. Abordamos desde os conceitos fundamentais até as aplicações mais avançadas de {PALAVRA_CHAVE}. Se você se interessa por {CATEGORIA}, este conteúdo é essencial para sua leitura.",
            "O que realmente sabemos sobre {TITULO}? Este post desmistifica os principais pontos relacionados a {PALAVRA_CHAVE} no campo da {CATEGORIA}, trazendo insights valiosos e informações atualizadas para entusiastas e profissionais da área."
    };

    public ArtigoGenerator(FakerApiData dadosApi) {
        this.dadosApi = dadosApi;
    }

    @Override
    public Artigo gerar() {
        // 1. Seleciona uma categoria e seus dados correspondentes de forma aleatória
        int indexCategoria = random.nextInt(CATEGORIAS.length);
        String categoria = CATEGORIAS[indexCategoria];
        String[] templatesTituloCategoria = TEMPLATES_TITULO[indexCategoria];
        String[] palavrasChaveCategoria = PALAVRAS_CHAVE[indexCategoria];

        // MELHORIA: A palavra-chave agora é gerada uma única vez para garantir consistência entre título e conteúdo.
        String palavraChave = palavrasChaveCategoria[random.nextInt(palavrasChaveCategoria.length)];

        // 2. Gera o título do artigo usando a palavra-chave única
        String templateTitulo = templatesTituloCategoria[random.nextInt(templatesTituloCategoria.length)];
        String tituloArtigo = templateTitulo
                .replace("{TEMA}", palavraChave)
                .replace("{ANO}", String.valueOf(LocalDateTime.now().getYear()));

        // 3. Gera o conteúdo do artigo usando a MESMA palavra-chave
        String templateConteudo = TEMPLATES_CONTEUDO[random.nextInt(TEMPLATES_CONTEUDO.length)];
        String conteudoArtigo = templateConteudo
                .replace("{TITULO}", tituloArtigo)
                .replace("{CATEGORIA}", categoria)
                .replace("{PALAVRA_CHAVE}", palavraChave);

        // 4. Obtém os dados restantes
        String nomeAutor = StringUtils.removerAcentos(dadosApi.getFullName());
        String dataPublicacao = LocalDateTime.now(ZoneOffset.UTC).format(ISO_FORMATTER);

        return Artigo.builder()
                .tituloArtigo(StringUtils.removerAcentos(tituloArtigo))
                .conteudoArtigo(StringUtils.removerAcentos(conteudoArtigo))
                .nomeAutor(nomeAutor)
                .dataPublicacao(dataPublicacao)
                .build();
    }
}