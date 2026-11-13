package com.taverna.decorator;

import com.taverna.model.Item;

public class ComMel extends ItemDecorator {

    public ComMel(Item componente) {
        super(componente);
    }

    @Override
    public String getNome() {
        return componente.getNome() + " + mel";
    }

    @Override
    public double getPreco() {
        return componente.getPreco() + 2.00;
    }

    @Override
    public void preparar() {
        componente.preparar();
        System.out.println("   ...regando com mel silvestre.");
    }
}
