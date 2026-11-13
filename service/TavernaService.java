package com.taverna.service;

import java.util.ArrayList;
import java.util.List;

import com.taverna.decorator.ComMel;
import com.taverna.decorator.ParaViagem;
import com.taverna.decorator.PorcaoDupla;
import com.taverna.exception.ItemIndisponivelException;
import com.taverna.exception.SaldoInsuficienteException;
import com.taverna.model.Cliente;
import com.taverna.model.Item;
import com.taverna.model.Pedido;
import com.taverna.observer.PedidoObserver;
import com.taverna.observer.PedidoSubject;
import com.taverna.repository.ItemRepository;
import com.taverna.strategy.EstrategiaDesconto;
import com.taverna.strategy.SemDesconto;

public class TavernaService implements PedidoSubject {

    private final ItemRepository repositorio;
    private final List<PedidoObserver> observers = new ArrayList<>();
    private EstrategiaDesconto estrategiaDesconto = new SemDesconto();

    public TavernaService(ItemRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void adicionarObserver(PedidoObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removerObserver(PedidoObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notificarObservers(Pedido pedido) {
        for (PedidoObserver observer : observers) {
            observer.atualizar(pedido);
        }
    }

    public void setEstrategiaDesconto(EstrategiaDesconto estrategiaDesconto) {
        this.estrategiaDesconto = estrategiaDesconto;
    }

    public EstrategiaDesconto getEstrategiaDesconto() {
        return estrategiaDesconto;
    }

    public List<Item> consultarCardapio() {
        return repositorio.listarTodos();
    }

    public void cadastrarItem(Item item) {
        repositorio.adicionar(item);
    }

    public Pedido registrarPedido(Cliente cliente, List<String> entradas)
            throws ItemIndisponivelException, SaldoInsuficienteException {

        Pedido pedido = new Pedido(cliente, estrategiaDesconto);

        for (String entrada : entradas) {
            String[] partes = entrada.split("\\+");
            Item item = repositorio.buscarPorNome(partes[0].trim());
            for (int i = 1; i < partes.length; i++) {
                item = aplicarExtra(item, partes[i].trim());
            }
            pedido.adicionarItem(item);
        }

        List<Item> baixados = new ArrayList<>();
        try {
            for (Item item : pedido.getItens()) {
                item.baixarEstoque(1);
                baixados.add(item);
            }
            cliente.pagar(pedido.calcularTotal());
        } catch (ItemIndisponivelException | SaldoInsuficienteException e) {
            for (Item item : baixados) {
                item.reporEstoque(1);
            }
            throw e;
        }

        notificarObservers(pedido);
        return pedido;
    }

    private Item aplicarExtra(Item item, String extra) throws ItemIndisponivelException {
        switch (extra.toLowerCase()) {
            case "mel":
                return new ComMel(item);
            case "dupla":
                return new PorcaoDupla(item);
            case "viagem":
                return new ParaViagem(item);
            default:
                throw new ItemIndisponivelException(
                    "Extra \"" + extra + "\" não existe. Use: mel, dupla, viagem.");
        }
    }
}
