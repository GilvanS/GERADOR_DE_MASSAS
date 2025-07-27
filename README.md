# Gerador de Massa de Dados Unificado

## 1. Objetivo

Este projeto é uma biblioteca Java robusta e extensível, projetada para gerar massas de dados de teste complexas e realistas. O objetivo é fornecer uma única ferramenta capaz de criar dados consistentes para diversas entidades (como Usuários, Produtos e Artigos), exportando-os para um arquivo `.csv` pronto para ser consumido por projetos de automação de testes, especialmente em cenários de BDD com Cucumber.

A biblioteca foi construída para ser facilmente integrada a outros projetos Maven como uma dependência (JAR).

## 2. Principais Funcionalidades

*   **Geração de Dados Compostos:** Cria uma massa de dados unificada que combina informações de diferentes domínios (usuário, produto, artigo) de forma consistente.
*   **Fonte de Dados Híbrida:** Utiliza uma combinação inteligente de:
    *   **API Externa (`FakerAPI`):** Para obter dados ricos e variados, como nomes e endereços.
    *   **Geradores Locais:** Para dados que exigem regras específicas e alta confiabilidade, como CPF, CNPJ, CEP, senhas customizadas e produtos.
*   **Exportação para CSV "Pronto para Uso":**
    *   Gera um arquivo `.csv` com **ponto e vírgula (`;`)** como delimitador, garantindo a abertura correta no Excel em configurações regionais brasileiras/europeias.
    *   Formata todos os campos como **texto explícito** (`="valor"`), o que previne que o Excel quebre dados numéricos longos (CPF, CNPJ, Cartão) ou remova zeros à esquerda (CEP).
*   **Modo de Anexo (Append):** Não sobrescreve o arquivo a cada execução. Em vez disso, anexa os novos registros ao final do arquivo existente, permitindo a criação de grandes massas de dados ao longo de várias execuções.
*   **Arquitetura Extensível:** Projetado para ser fácil de expandir com novos tipos de dados sem a necessidade de alterar o código existente.

## 3. Arquitetura e Detalhes das Classes

O projeto passou por uma refatoração significativa para adotar padrões de design que promovem clareza, manutenibilidade e extensibilidade.

### 3.1. Estrutura de Modelos (Pacote `model`)

A abordagem de **Composição sobre Herança** foi utilizada para estruturar os dados.

*   `Massa.java`: É a classe principal que agrega todos os outros modelos. Atua como um contêiner, representando uma linha completa de dados a ser gerada.
*   `Usuario.java`, `Produto.java`, `Artigo.java`: São modelos de domínio puros (POJOs). Cada um representa uma entidade de negócio específica e contém apenas os campos relacionados a ela. Todos os campos que devem ser exportados são anotados com `@CsvColumn`.

**Vantagem:** Essa separação segue o **Princípio da Responsabilidade Única (SRP)**, tornando o sistema mais fácil de entender e modificar.

### 3.2. Estrutura de Geradores (Pacote `generators`)

A geração de dados foi descentralizada usando o padrão **Strategy/Factory**.

*   `Gerador<T>`: Uma interface funcional que define o contrato `T gerar()`. Garante que todos os geradores tenham um ponto de entrada comum.
*   `MassaGenerator.java`: Atua como um **Orquestrador (Factory)**. Sua única responsabilidade é instanciar os geradores específicos (`UsuarioGenerator`, etc.), orquestrar a chamada de cada um e montar o objeto `Massa` final. É ele quem faz a única chamada à API externa para otimizar o desempenho.
*   `UsuarioGenerator`, `ProdutoGenerator`, `ArtigoGenerator`: São as implementações **Strategy**. Cada um sabe exatamente como criar seu respectivo modelo (`Usuario`, `Produto`, etc.), aplicando as regras de negócio necessárias (formatação, limpeza de dados, etc.).

**Vantagem:** Para adicionar um novo tipo de dado (ex: `Veiculo`), basta criar o modelo `Veiculo.java` e o `VeiculoGenerator.java` e adicioná-lo ao `MassaGenerator`, sem alterar os geradores existentes. Isso segue o **Princípio Aberto/Fechado (OCP)**.

### 3.3. Geradores Locais e Utilitários

Para dados com regras complexas ou que precisam ser 100% confiáveis, foram criados geradores locais.

*   `DocumentosGenerator.java`: Gera documentos brasileiros válidos (CPF, CNPJ, CEP), incluindo os dígitos verificadores corretos. Garante que os dados de documentos sejam sempre realistas.
*   `PasswordGenerator.java`: Cria senhas customizadas com base em regras de negócio (ex: 3 letras do último nome + 2 do primeiro + 2 dígitos do CPF), aumentando o realismo dos dados de teste.
*   `FakerApiData.java`: É a única classe que se comunica com a API externa. Atua como um **DTO (Data Transfer Object)**, buscando os dados brutos e disponibilizando-os para os outros geradores.
*   `StringUtils.java`: Uma classe utilitária para centralizar operações com texto, como `removerAcentos` e `formatarParaTextoCsv`. Evita a duplicação de código (princípio **DRY**).

### 3.4. Geração de CSV Orientada a Anotações (Pacote `writers`)

A escrita do CSV é a parte mais flexível do projeto.

*   `@CsvColumn(header = "...", order = ...)`: Uma anotação customizada que desacopla a lógica de escrita dos modelos de dados. Ao anotar um campo, você define programaticamente seu cabeçalho e sua posição no arquivo CSV.
*   `CsvWriterService.java`: Utiliza **Java Reflection** para inspecionar a classe `Massa` e seus componentes em tempo de execução. Ele lê as anotações `@CsvColumn`, descobre dinamicamente quais colunas criar, em qual ordem, e como obter os valores de cada campo.

**Vantagem:** Para adicionar, remover ou reordenar uma coluna no CSV, **basta alterar a anotação no modelo**. Nenhuma alteração é necessária no `CsvWriterService`, eliminando uma grande fonte de erros e tornando a manutenção trivial.

## 4. Como Usar como Biblioteca (JAR)

O projeto foi desenhado para ser importado em outras aplicações, como um framework de automação de testes.

### 4.1. Passo 1: Instalar o JAR no Repositório Local

Para que outros projetos Maven em sua máquina possam encontrar esta biblioteca, execute o seguinte comando na pasta raiz do `GeradorDeMassas`:

Este comando compila o projeto, empacota-o em um arquivo JAR e o instala no seu repositório Maven local (geralmente em `C:/Users/SEU_USUARIO/.m2/repository`).

### 4.2. Passo 2: Adicionar como Dependência

No `pom.xml` do projeto que irá consumir o gerador, adicione a seguinte dependência:

### 4.3. Passo 3: Utilizar no Código

Agora, você pode facilmente instanciar e usar o `GeradorService` em qualquer parte do seu outro projeto.

## 5. Tecnologias Utilizadas

*   **Java 11**
*   **Maven:** Gerenciamento de dependências e build.
*   **Lombok:** Redução de código boilerplate (getters, builders).
*   **Apache Commons CSV:** Escrita eficiente e customizada de arquivos CSV.
*   **Apache HttpClient:** Realização de chamadas HTTP para a `FakerAPI`.
*   **Jackson Databind:** Processamento do JSON retornado pela API.