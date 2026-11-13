package com.taverna.observer;

import com.taverna.model.Pedido;

public interface PedidoSubject {
    void adicionarObserver(PedidoObserver observer);

    void removerObserver(PedidoObserver observer);

    void notificarObservers(Pedido pedido);
}
