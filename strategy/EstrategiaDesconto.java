package com.taverna.strategy;

public interface EstrategiaDesconto {

    String getDescricao();

    double calcularDesconto(double subtotal);
}
