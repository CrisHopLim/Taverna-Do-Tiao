package com.taverna.decorator;

import com.taverna.exception.ItemIndisponivelException;
import com.taverna.model.Item;

public abstract class ItemDecorator extends Item {

    protected final Item componente;

    protected ItemDecorator(Item componente) {
        super(componente.getNome(), componente.getPreco(), componente.getQuantidadeEstoque());
        this.componente = componente;
    }

    public Item getComponente() {
        return componente;
    }

    @Override
    public String getNome() {
        return componente.getNome();
    }

    @Override
    public double getPreco() {
        return componente.getPreco();
    }

    @Override
    public int getQuantidadeEstoque() {
        return componente.getQuantidadeEstoque();
    }

    @Override
    public String getCategoria() {
        return componente.getCategoria();
    }

    @Override
    public void baixarEstoque(int quantidade) throws ItemIndisponivelException {
        componente.baixarEstoque(quantidade);
    }

    @Override
    public void reporEstoque(int quantidade) {
        componente.reporEstoque(quantidade);
    }

    @Override
    public void preparar() {
        componente.preparar();
    }
}
