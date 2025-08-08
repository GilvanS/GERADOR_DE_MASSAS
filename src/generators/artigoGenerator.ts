import { faker } from '@faker-js/faker';
import { Artigo } from '../models/Artigo';
import { FakerApiData } from './fakerApi';

const CATEGORIAS = [
    "Tecnologia", "Programacao", "Universo e Astronomia", "Ciencia e Fisica",
    "Saude e Bem-Estar", "Educacao", "Historia", "Cultura Pop",
    "Financas e Investimentos", "Viagens e Turismo", "Gastronomia e Culinaria", "Meio Ambiente", "Esportes",
    "Teste de Software"
];

const TEMPLATES_TITULO = [
    ["O Futuro do {TEMA}", "Como {TEMA} Esta Moldando o Mundo", "5 Tendencias em {TEMA}"],
    ["Guia Completo de {TEMA}", "Otimizando Performance com {TEMA}", "Porque Aprender {TEMA}"],
    ["Desvendando os Misterios de {TEMA}", "A Busca por {TEMA}", "A Incrivel Jornada ate {TEMA}"],
    ["Uma Introducao a {TEMA}", "Como {TEMA} Explica o Cotidiano", "Os Limites do Conhecimento em {TEMA}"],
    ["10 Dicas para Melhorar sua {TEMA}", "Os Beneficios da {TEMA}", "Alimentacao e {TEMA}"],
    ["A Revolucao da {TEMA} na Sala de Aula", "Metodos Inovadores de {TEMA}", "O Papel da {TEMA}"],
    ["A Ascensao e Queda do {TEMA}", "O Legado do {TEMA} na Sociedade", "Como {TEMA} Mudou a Historia"],
    ["Analise Profunda de {TEMA}", "O Impacto Cultural de {TEMA}", "Porque {TEMA} Continua Relevante"],
    ["Guia de {TEMA} para Iniciantes", "Como Investir em {TEMA}", "Entendendo o Mercado de {TEMA}"],
    ["Roteiro Inesquecivel por {TEMA}", "Dicas para Viajar para {TEMA}", "O que Fazer em {TEMA}"],
    ["A Receita Perfeita de {TEMA}", "A Historia da {TEMA}", "Segredos da Culinaria com {TEMA}"],
    ["O Impacto do {TEMA} no Planeta", "Solucoes Sustentaveis para {TEMA}", "Um Futuro com {TEMA}"],
    ["Analise Tatica do {TEMA}", "A Carreira Lendaria de {TEMA}", "Os Maiores Momentos do {TEMA}"],
    ["Guia Essencial do {TEMA}", "Como Aplicar {TEMA} no seu Projeto", "Os Beneficios do {TEMA} para a Qualidade de Software", "Desmistificando o {TEMA}"]
];

const PALAVRAS_CHAVE = [
    ["Inteligencia Artificial", "Computacao Quantica", "Realidade Aumentada", "Blockchain", "5G"],
    ["Python", "JavaScript", "APIs REST", "Micro-servicos", "Clean Code", "Docker"],
    ["Buracos Negros", "Exoplanetas", "Nebulosas", "Materia Escura", "Galaxias Distantes"],
    ["Teoria da Relatividade", "Fisica Quantica", "Genetica", "Neurociencia", "Leis de Newton"],
    ["Saude Mental", "Nutricao Funcional", "Meditacao", "Qualidade de Sono", "Atividade Fisica"],
    ["Aprendizagem Hibrida", "Gamificacao", "Inteligencia Emocional", "Robotica Educacional", "STEAM"],
    ["Imperio Romano", "Antigo Egito", "Revolucao Industrial", "Guerra Fria", "Renascimento"],
    ["Universo Marvel", "Star Wars", "Animes Classicos", "Industria dos Games", "Series de TV"],
    ["Acoes da Bolsa", "Criptomoedas", "Renda Fixa", "Fundos Imobiliarios", "Planejamento Financeiro"],
    ["Sudeste Asiatico", "Europa Central", "Patagonia", "Japao", "Litoral do Nordeste"],
    ["Cozinha Italiana", "Fermentacao Natural", "Churrasco Americano", "Comida Vegana", "Vinhos Franceses"],
    ["Aquecimento Global", "Desmatamento", "Poluicao Plastica", "Energias Renovaveis", "Consumo Consciente"],
    ["Futebol Europeu", "Basquete da NBA", "Formula 1", "Tenis", "MMA"],
    ["Teste de Fumaça", "Teste de Regressão", "Teste Funcional", "Teste de Caixa Preta", "Teste de Caixa Branca", "Teste de Integração", "Teste de Unidade", "Teste de Performance"]
];

const CONTEUDO_ESPECIFICO = [
    null, null, null, null, null, null, null, null, null, null, null, null, null, // Mantém a estrutura paralela para categorias existentes
    [ // Conteúdo para Teste de Software
        "O Teste de Fumaça, ou Smoke Test, é uma verificação preliminar para garantir que as funcionalidades mais cruciais de uma nova build estão funcionando. Se este teste falhar, a build é rejeitada imediatamente, economizando tempo e recursos.",
        "O Teste de Regressão é executado para garantir que novas alterações no código não quebraram funcionalidades existentes. É um passo vital para manter a estabilidade do software ao longo do tempo.",
        "O Teste Funcional valida o software em relação aos requisitos de negócio. Ele foca no 'o que' o sistema faz, verificando as saídas para um conjunto de entradas sem se preocupar com a estrutura interna do código.",
        "No Teste de Caixa Preta, o testador não tem conhecimento da estrutura interna, design ou implementação do item sendo testado. O foco está em validar a funcionalidade do sistema a partir da perspectiva do usuário final.",
        "O Teste de Caixa Branca, ao contrário do de caixa preta, exige conhecimento da estrutura interna do código. O objetivo é testar os caminhos lógicos, estruturas de dados e o fluxo interno do software.",
        "O Teste de Integração foca em verificar a comunicação e a interação entre diferentes módulos ou serviços de um sistema. O objetivo é expor falhas na interface entre os componentes integrados.",
        "O Teste de Unidade é o nível mais baixo de teste, focando em verificar componentes individuais ou 'unidades' de código, como funções ou métodos, de forma isolada para garantir que funcionem como esperado.",
        "O Teste de Performance avalia a velocidade, responsividade e estabilidade de um sistema sob uma carga de trabalho específica. Ele ajuda a identificar gargalos e a garantir que o software atenda aos requisitos de desempenho."
    ]
];

const TEMPLATES_CONTEUDO_GENERICO = [
    "Este e um artigo sobre {TITULO}. O foco principal e em {PALAVRA_CHAVE} no campo da {CATEGORIA}.",
    "Uma analise sobre o tema {TITULO}. Exploramos a relevancia de {PALAVRA_CHAVE} para a area de {CATEGORIA}.",
    "Introducao ao topico {TITULO}. Discutimos os conceitos basicos de {PALAVRA_CHAVE} aplicados a {CATEGORIA}."
];

function removerAcentos(texto: string): string {
    return texto.normalize("NFD").replace(/[\u0300-\u036f]/g, "");
}

export function gerarArtigo(dadosApi: FakerApiData): Artigo {
    const indexCategoria = faker.number.int({ min: 0, max: CATEGORIAS.length - 1 });
    const categoria = CATEGORIAS[indexCategoria]!;
    const templatesTituloCategoria = TEMPLATES_TITULO[indexCategoria]!;
    const palavrasChaveCategoria = PALAVRAS_CHAVE[indexCategoria]!;
    const templatesConteudoCategoria = CONTEUDO_ESPECIFICO[indexCategoria]; // Pode ser nulo

    const indexPalavraChave = faker.number.int({ min: 0, max: palavrasChaveCategoria.length - 1 });
    const palavraChave = palavrasChaveCategoria[indexPalavraChave]!;
    const templateTitulo = faker.helpers.arrayElement(templatesTituloCategoria);
    const tituloArtigo = templateTitulo.replace("{TEMA}", palavraChave);

    let conteudoArtigo: string;

    if (templatesConteudoCategoria) {
        // Usa o conteúdo específico para a palavra-chave (técnica de teste)
        conteudoArtigo = templatesConteudoCategoria[indexPalavraChave]!;
    } else {
        // Usa o template genérico para as outras categorias
        const templateConteudo = faker.helpers.arrayElement(TEMPLATES_CONTEUDO_GENERICO);
        conteudoArtigo = templateConteudo
            .replace("{TITULO}", tituloArtigo)
            .replace("{CATEGORIA}", categoria)
            .replace("{PALAVRA_CHAVE}", palavraChave);
    }

    const nomeAutor = removerAcentos(`${dadosApi.firstName} ${dadosApi.lastName}`);
    const dataPublicacao = new Date().toISOString();

    return {
        tituloArtigo: removerAcentos(tituloArtigo),
        conteudoArtigo: removerAcentos(conteudoArtigo),
        nomeAutor: nomeAutor,
        dataPublicacao: dataPublicacao,
    };
}