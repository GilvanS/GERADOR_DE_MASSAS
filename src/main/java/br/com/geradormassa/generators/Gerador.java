package br.com.geradormassa.generators;

/**
 * Interface funcional para qualquer classe que gera um tipo de dado.
 * @param <T> O tipo de objeto que será gerado.
 */
@FunctionalInterface
public interface Gerador<T> {
    T gerar();
}