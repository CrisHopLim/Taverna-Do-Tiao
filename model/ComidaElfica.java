package com.taverna.model;

public class ComidaElfica extends Comida {

    public ComidaElfica(String nome, double preco, int quantidadeEstoque, int tempoPreparoMinutos) {
        super(nome, preco, quantidadeEstoque, tempoPreparoMinutos);
    }

    @Override
    public String getCategoria() {
        return "Comida Élfica";
    }

    @Override
    public void preparar() {
        System.out.println("Preparando " + nome + " envolto em folhas, sobre brasa de carvalho ("
            + getTempoPreparoMinutos() + " min)...");
    }
}
