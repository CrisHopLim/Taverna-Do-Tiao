package com.taverna.decorator;

import com.taverna.model.Item;

public class ParaViagem extends ItemDecorator {

    public ParaViagem(Item componente) {
        super(componente);
    }

    @Override
    public String getNome() {
        return componente.getNome() + " [para viagem]";
    }

    @Override
    public double getPreco() {
        return componente.getPreco() + 1.50;
    }

    @Override
    public void preparar() {
        componente.preparar();
        System.out.println("   ...embrulhando em couro para a estrada.");
    }
}
