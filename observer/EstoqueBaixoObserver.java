package com.taverna.observer;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

import com.taverna.decorator.ItemDecorator;
import com.taverna.model.Item;
import com.taverna.model.Pedido;

public class EstoqueBaixoObserver implements PedidoObserver {

    private final int limite;

    public EstoqueBaixoObserver(int limite) {
        this.limite = limite;
    }

    @Override
    public void atualizar(Pedido pedido) {
        Set<Item> verificados = Collections.newSetFromMap(new IdentityHashMap<>());
        for (Item item : pedido.getItens()) {
            Item base = item;
            while (base instanceof ItemDecorator) {
                base = ((ItemDecorator) base).getComponente();
            }
            if (verificados.add(base) && base.getQuantidadeEstoque() <= limite) {
                System.out.println("[Estoque] Atenção: restam apenas " + base.getQuantidadeEstoque()
                    + " unidade(s) de \"" + base.getNome() + "\".");
            }
        }
    }
}
