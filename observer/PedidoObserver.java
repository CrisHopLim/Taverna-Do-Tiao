package com.taverna.observer;

import com.taverna.model.Pedido;

public interface PedidoObserver {
    void atualizar(Pedido pedido);
}
