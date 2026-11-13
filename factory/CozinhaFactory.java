package com.taverna.factory;

import com.taverna.model.Bebida;
import com.taverna.model.Comida;

public interface CozinhaFactory {

    String getNomeCozinha();

    Bebida criarBebida(String nome, double preco, int estoque, boolean alcoolica);

    Comida criarComida(String nome, double preco, int estoque, int tempoPreparoMinutos);
}
