package com.taverna.strategy;

public class HappyHour implements EstrategiaDesconto {

    private static final double PERCENTUAL = 0.20;

    @Override
    public String getDescricao() {
        return "Happy hour (20% off)";
    }

    @Override
    public double calcularDesconto(double subtotal) {
        return subtotal * PERCENTUAL;
    }
}
