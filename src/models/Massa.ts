import { Artigo } from "./Artigo";
import { Produto } from "./Produto";
import { Usuario } from "./Usuario";

export interface Massa {
    usuario: Usuario;
    produto: Produto;
    artigo: Artigo;
}
