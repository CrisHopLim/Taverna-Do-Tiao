package com.taverna.observer;

import com.taverna.model.Item;
import com.taverna.model.Pedido;

public class CozinhaObserver implements PedidoObserver {

    @Override
    public void atualizar(Pedido pedido) {
        System.out.println("[Cozinha] Novo pedido de " + pedido.getCliente().getNome() + ":");
        for (Item item : pedido.getItens()) {
            item.preparar();
        }
    }
}
