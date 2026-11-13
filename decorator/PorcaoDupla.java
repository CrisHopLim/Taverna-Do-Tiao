package com.taverna.decorator;

import com.taverna.exception.ItemIndisponivelException;
import com.taverna.model.Item;

public class PorcaoDupla extends ItemDecorator {

    public PorcaoDupla(Item componente) {
        super(componente);
    }

    @Override
    public String getNome() {
        return componente.getNome() + " (porção dupla)";
    }

    @Override
    public double getPreco() {
        return componente.getPreco() * 1.8;
    }

    @Override
    public void baixarEstoque(int quantidade) throws ItemIndisponivelException {
        componente.baixarEstoque(quantidade * 2);
    }

    @Override
    public void reporEstoque(int quantidade) {
        componente.reporEstoque(quantidade * 2);
    }

    @Override
    public void preparar() {
        componente.preparar();
        System.out.println("   ...caprichando em dobro!");
    }
}
