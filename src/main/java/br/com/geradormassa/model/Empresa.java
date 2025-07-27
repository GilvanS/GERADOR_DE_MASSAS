package br.com.geradormassa.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Empresa {

    private String razaoSocial;
    private String cnpj;

}
