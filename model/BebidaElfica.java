package com.taverna.model;

public class BebidaElfica extends Bebida {

    public BebidaElfica(String nome, double preco, int quantidadeEstoque, boolean alcoolica) {
        super(nome, preco, quantidadeEstoque, alcoolica);
    }

    @Override
    public String getCategoria() {
        return "Bebida Élfica";
    }

    @Override
    public void preparar() {
        System.out.println("Servindo " + nome + " em uma taça de cristal com folhas de prata...");
    }
}
