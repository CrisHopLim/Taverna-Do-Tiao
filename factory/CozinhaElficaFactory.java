package com.taverna.factory;

import com.taverna.model.Bebida;
import com.taverna.model.BebidaElfica;
import com.taverna.model.Comida;
import com.taverna.model.ComidaElfica;

public class CozinhaElficaFactory implements CozinhaFactory {

    @Override
    public String getNomeCozinha() {
        return "Cozinha Élfica";
    }

    @Override
    public Bebida criarBebida(String nome, double preco, int estoque, boolean alcoolica) {
        return new BebidaElfica(nome, preco, estoque, alcoolica);
    }

    @Override
    public Comida criarComida(String nome, double preco, int estoque, int tempoPreparoMinutos) {
        return new ComidaElfica(nome, preco, estoque, tempoPreparoMinutos);
    }
}
