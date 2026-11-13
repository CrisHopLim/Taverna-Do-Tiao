package com.taverna.factory;

import com.taverna.model.Bebida;
import com.taverna.model.Comida;

public class CozinhaTavernaFactory implements CozinhaFactory {

    @Override
    public String getNomeCozinha() {
        return "Cozinha da Taverna";
    }

    @Override
    public Bebida criarBebida(String nome, double preco, int estoque, boolean alcoolica) {
        return new Bebida(nome, preco, estoque, alcoolica);
    }

    @Override
    public Comida criarComida(String nome, double preco, int estoque, int tempoPreparoMinutos) {
        return new Comida(nome, preco, estoque, tempoPreparoMinutos);
    }
}
