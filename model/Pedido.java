package com.taverna.model;

import java.util.ArrayList;
import java.util.List;

import com.taverna.strategy.EstrategiaDesconto;
import com.taverna.strategy.SemDesconto;

public class Pedido {

    private Cliente cliente;
    private List<Item> itens;
    private EstrategiaDesconto estrategiaDesconto;

    public Pedido(Cliente cliente) {
        this(cliente, new SemDesconto());
    }

    public Pedido(Cliente cliente, EstrategiaDesconto estrategiaDesconto) {
        this.cliente = cliente;
        this.itens = new ArrayList<>();
        this.estrategiaDesconto = estrategiaDesconto;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<Item> getItens() {
        return itens;
    }

    public EstrategiaDesconto getEstrategiaDesconto() {
        return estrategiaDesconto;
    }

    public void adicionarItem(Item item) {
        itens.add(item);
    }

    public double calcularSubtotal() {
        double subtotal = 0.0;
        for (Item item : itens) {
            subtotal += item.getPreco();
        }
        return subtotal;
    }

    public double calcularDesconto() {
        double subtotal = calcularSubtotal();
        return Math.min(estrategiaDesconto.calcularDesconto(subtotal), subtotal);
    }

    public double calcularTotal() {
        return calcularSubtotal() - calcularDesconto();
    }
}
