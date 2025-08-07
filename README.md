# Gerador de Massa de Dados Unificado (Versão Node.js)

## 1. Objetivo

Este projeto é uma ferramenta robusta e extensível, projetada para gerar massas de dados de teste complexas e realistas. O objetivo é fornecer um único script capaz de criar dados consistentes para diversas entidades (como Usuários, Produtos e Artigos), exportando-os para um arquivo `.csv` pronto para ser consumido por projetos de automação de testes.

Esta versão representa uma migração completa da base de código original em Java/Maven para uma stack moderna com **Node.js e TypeScript**.

## 2. Principais Funcionalidades

*   **Geração de Dados Compostos:** Cria uma massa de dados que combina informações de diferentes domínios (usuário, produto, artigo).
*   **Fonte de Dados Híbrida e Resiliente:**
    *   Tenta buscar dados ricos de uma **API Externa (`FakerAPI`)**.
    *   Em caso de falha na API, utiliza um **mecanismo de fallback** que gera dados localmente com a biblioteca `@faker-js/faker`, garantindo que a aplicação nunca pare por problemas de rede.
*   **Exportação para CSV:** Gera um arquivo `.csv` com **ponto e vírgula (`;`)** como delimitador, ideal para configurações regionais brasileiras/europeias.
*   **Modo de Anexo (Append):** Não sobrescreve o arquivo a cada execução. Em vez disso, anexa os novos registros ao final do arquivo existente, permitindo a criação de grandes massas de dados ao longo de várias execuções.
*   **Ambiente de Testes Configurado:** Utiliza **Jest** para testes unitários e de integração, garantindo a confiabilidade do código.

---

## 3. Migração: Java/Maven para Node.js/TypeScript

O projeto foi totalmente reescrito para aproveitar os benefícios do ecossistema Node.js. Abaixo estão as principais mudanças e vantagens.

### 3.1. Principais Mudanças Tecnológicas

| Funcionalidade | Stack Antiga (Java) | Stack Nova (Node.js) |
| :--- | :--- | :--- |
| **Linguagem/Runtime** | Java 11 / JVM | TypeScript / Node.js |
| **Gerenciador de Pacotes** | Maven (`pom.xml`) | NPM (`package.json`) |
| **Executor de Tarefas** | `mvn compile exec` | `npm start` (com `tsx`) |
| **Geração de Dados** | Bibliotecas Java customizadas | `@faker-js/faker` |
| **Chamadas HTTP** | Apache HttpClient | `axios` |
| **Escrita de CSV** | Apache Commons CSV | `csv-writer` + `csv-stringify` |
| **Testes** | JUnit | `jest` + `ts-jest` |
| **Estrutura de Dados** | Classes (POJOs) | Interfaces (`.ts`) |

### 3.2. Benefícios da Migração

*   **Simplicidade e Leveza:** A configuração do projeto é centralizada no `package.json`, e a execução não requer uma JVM, tornando o ambiente de desenvolvimento mais simples e rápido.
*   **Performance para I/O:** A natureza assíncrona e não-bloqueante do Node.js é ideal para tarefas como esta, que envolvem chamadas de rede (API) e operações de arquivo (escrita do CSV).
*   **Ecossistema Moderno:** O ecossistema NPM oferece acesso a um vasto repositório de pacotes modernos e bem mantidos, como o `@faker-js/faker` e o `axios`.
*   **Melhor Experiência de Desenvolvimento (DX):** TypeScript combina a segurança da tipagem estática com os recursos mais recentes do JavaScript. Ferramentas como `tsx` permitem a execução direta de arquivos TypeScript sem uma etapa de compilação manual explícita, agilizando o ciclo de desenvolvimento.

---

## 4. Como Usar

### 4.1. Pré-requisitos

*   [Node.js](https://nodejs.org/) (versão 18 ou superior)
*   [NPM](https://www.npmjs.com/) (geralmente instalado com o Node.js)

### 4.2. Instalação

Na raiz do projeto, instale as dependências:
```bash
npm install
```

### 4.3. Gerando a Massa de Dados

Para executar o gerador, use o comando:
```bash
npm start
```
Os dados serão gerados e salvos (ou anexados) no arquivo `output/massaDeDados.csv`.

Você pode alterar a quantidade de registros a serem gerados por execução modificando a constante `QUANTIDADE_REGISTROS` no arquivo `src/index.ts`.

### 4.4. Executando os Testes

Para validar a integridade do código e garantir que tudo está funcionando como esperado, execute a suíte de testes:
```bash
npm test
```

## 5. Estrutura do Projeto

```
.
├── output/               # Diretório onde o arquivo CSV é gerado
└── src/
    ├── generators/       # Lógica para gerar dados (usuário, produto, etc.)
    ├── models/           # Interfaces TypeScript que definem a estrutura dos dados
    ├── services/         # Orquestração da lógica de geração
    ├── tests/            # Arquivos de teste do Jest
    ├── writers/          # Lógica para escrever o arquivo CSV
    └── index.ts          # Ponto de entrada da aplicação
```
