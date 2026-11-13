package com.taverna.strategy;

public class BanqueteDoViajante implements EstrategiaDesconto {

    private static final double VALOR_MINIMO = 50.00;
    private static final double DESCONTO = 10.00;

    @Override
    public String getDescricao() {
        return "Banquete do Viajante (R$ 10 off acima de R$ 50)";
    }

    @Override
    public double calcularDesconto(double subtotal) {
        return subtotal >= VALOR_MINIMO ? DESCONTO : 0.0;
    }
}
