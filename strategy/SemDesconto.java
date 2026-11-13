package com.taverna.strategy;

public class SemDesconto implements EstrategiaDesconto {

    @Override
    public String getDescricao() {
        return "Sem promoção";
    }

    @Override
    public double calcularDesconto(double subtotal) {
        return 0.0;
    }
}
