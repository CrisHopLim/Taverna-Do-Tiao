package com.taverna;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.taverna.exception.ItemIndisponivelException;
import com.taverna.exception.SaldoInsuficienteException;
import com.taverna.factory.CozinhaElficaFactory;
import com.taverna.factory.CozinhaFactory;
import com.taverna.factory.CozinhaTavernaFactory;
import com.taverna.model.Cliente;
import com.taverna.model.Item;
import com.taverna.model.Pedido;
import com.taverna.repository.ItemRepository;
import com.taverna.service.TavernaService;
import com.taverna.singleton.CaixaRegistradora;
import com.taverna.strategy.BanqueteDoViajante;
import com.taverna.strategy.HappyHour;
import com.taverna.strategy.SemDesconto;

public class Menus {

    private final Scanner scanner;
    private final TavernaService taverna;
    private final List<Cliente> clientes = new ArrayList<>();

    public Menus(Scanner scanner, TavernaService taverna) {
        this.scanner = scanner;
        this.taverna = taverna;
    }

    public static void popularCardapio(ItemRepository repositorio) {
        CozinhaFactory casa = new CozinhaTavernaFactory();
        repositorio.adicionar(casa.criarBebida("Cerveja escura", 8.50, 20, true));
        repositorio.adicionar(casa.criarBebida("Hidromel", 12.00, 10, true));
        repositorio.adicionar(casa.criarBebida("Suco de maçã", 5.00, 15, false));
        repositorio.adicionar(casa.criarComida("Ensopado de javali", 18.00, 8, 20));
        repositorio.adicionar(casa.criarComida("Pão com queijo", 6.00, 12, 5));

        CozinhaFactory elfica = new CozinhaElficaFactory();
        repositorio.adicionar(elfica.criarBebida("Vinho de Valfenda", 22.00, 6, true));
        repositorio.adicionar(elfica.criarComida("Pão de lembas", 9.00, 10, 3));
    }

    private CozinhaFactory escolherCozinha() {
        System.out.print("Cozinha (1 - Taverna / 2 - Élfica): ");
        String opcao = scanner.nextLine().trim();
        if (opcao.equals("1")) {
            return new CozinhaTavernaFactory();
        } else if (opcao.equals("2")) {
            return new CozinhaElficaFactory();
        }
        return null;
    }

    public void exibirMenu() {
        System.out.println("\n--- Menu ---");
        System.out.println("1 - Ver cardápio");
        System.out.println("2 - Cadastrar cliente");
        System.out.println("3 - Fazer pedido");
        System.out.println("4 - Ver caixa da taverna");
        System.out.println("5 - Cadastrar novo item no cardápio");
        System.out.println("6 - Adicionar saldo a um cliente");
        System.out.println("7 - Definir promoção da casa");
        System.out.println("0 - Sair");
        System.out.print("Escolha: ");
    }

    public void exibirCardapio() {
        System.out.println("\n--- Cardápio ---");
        for (Item item : taverna.consultarCardapio()) {
            System.out.println(item);
        }
        System.out.println("Extras: + mel (R$ 2,00) | + dupla (+80%, 2 unidades) | + viagem (R$ 1,50)");
        System.out.println("Promoção atual: " + taverna.getEstrategiaDesconto().getDescricao());
    }

    public void cadastrarCliente() {
        System.out.print("Nome do cliente: ");
        String nome = scanner.nextLine().trim();
        System.out.print("Saldo inicial (moedas): ");
        double saldo;
        try {
            saldo = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido, usando saldo 0.");
            saldo = 0;
        }
        clientes.add(new Cliente(nome, saldo));
        System.out.println("Cliente cadastrado com sucesso!");
    }

    public void fazerPedido() {
        if (clientes.isEmpty()) {
            System.out.println("Cadastre um cliente primeiro (opção 2).");
            return;
        }

        System.out.println("Clientes: ");
        for (int i = 0; i < clientes.size(); i++) {
            System.out.println(i + " - " + clientes.get(i));
        }
        System.out.print("Escolha o índice do cliente: ");
        int indice;
        try {
            indice = Integer.parseInt(scanner.nextLine().trim());
            Cliente cliente = clientes.get(indice);

            System.out.println("Itens separados por vírgula; extras com \"+\" (ex.: Hidromel + mel, Ensopado de javali + dupla)");
            System.out.print("Itens desejados: ");
            String linha = scanner.nextLine();
            List<String> nomesItens = new ArrayList<>();
            for (String parte : linha.split(",")) {
                nomesItens.add(parte.trim());
            }

            try {
                Pedido pedido = taverna.registrarPedido(cliente, nomesItens);
                System.out.println("\n--- Resumo do pedido ---");
                for (Item item : pedido.getItens()) {
                    System.out.printf(" - %s: R$ %.2f%n", item.getNome(), item.getPreco());
                }
                System.out.printf("Subtotal: R$ %.2f%n", pedido.calcularSubtotal());
                if (pedido.calcularDesconto() > 0) {
                    System.out.printf("Desconto (%s): -R$ %.2f%n",
                        pedido.getEstrategiaDesconto().getDescricao(), pedido.calcularDesconto());
                }
                System.out.printf("Pedido concluído! Total: R$ %.2f. Saldo restante: %.2f%n",
                    pedido.calcularTotal(), cliente.getSaldo());
            } catch (ItemIndisponivelException | SaldoInsuficienteException e) {
                System.out.println("Não foi possível concluir o pedido: " + e.getMessage());
            }

        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println("Índice de cliente inválido.");
        }
    }

    public void cadastrarItem() {
        CozinhaFactory cozinha = escolherCozinha();
        if (cozinha == null) {
            System.out.println("Cozinha inválida. Cadastro cancelado.");
            return;
        }
        System.out.print("Tipo do item (bebida/comida): ");
        String tipo = scanner.nextLine().trim();
        System.out.print("Nome do item: ");
        String nome = scanner.nextLine().trim();

        try {
            System.out.print("Preço: ");
            double preco = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Quantidade em estoque: ");
            int estoque = Integer.parseInt(scanner.nextLine().trim());

            Item item;
            if (tipo.equalsIgnoreCase("bebida")) {
                System.out.print("É alcoólica? (s/n): ");
                boolean alcoolica = scanner.nextLine().trim().equalsIgnoreCase("s");
                item = cozinha.criarBebida(nome, preco, estoque, alcoolica);
            } else if (tipo.equalsIgnoreCase("comida")) {
                System.out.print("Tempo de preparo (minutos): ");
                int tempoPreparo = Integer.parseInt(scanner.nextLine().trim());
                item = cozinha.criarComida(nome, preco, estoque, tempoPreparo);
            } else {
                System.out.println("Tipo inválido. Use \"bebida\" ou \"comida\".");
                return;
            }

            taverna.cadastrarItem(item);
            System.out.println("Item cadastrado com sucesso na " + cozinha.getNomeCozinha() + "!");

        } catch (NumberFormatException e) {
            System.out.println("Valor numérico inválido. Cadastro cancelado.");
        }
    }

    public void adicionarSaldo() {
        if (clientes.isEmpty()) {
            System.out.println("Cadastre um cliente primeiro (opção 2).");
            return;
        }

        System.out.println("Clientes: ");
        for (int i = 0; i < clientes.size(); i++) {
            System.out.println(i + " - " + clientes.get(i));
        }
        System.out.print("Escolha o índice do cliente: ");
        try {
            int indice = Integer.parseInt(scanner.nextLine().trim());
            Cliente cliente = clientes.get(indice);

            System.out.print("Valor a adicionar: ");
            double valor = Double.parseDouble(scanner.nextLine().trim());

            try {
                cliente.adicionarSaldo(valor);
                System.out.printf("Saldo atualizado! Novo saldo de %s: %.2f%n", cliente.getNome(), cliente.getSaldo());
            } catch (IllegalArgumentException e) {
                System.out.println("Não foi possível adicionar saldo: " + e.getMessage());
            }

        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println("Entrada inválida.");
        }
    }

    public void definirPromocao() {
        System.out.println("Promoção atual: " + taverna.getEstrategiaDesconto().getDescricao());
        System.out.println("1 - Sem promoção");
        System.out.println("2 - Happy hour (20% off)");
        System.out.println("3 - Banquete do Viajante (R$ 10 off acima de R$ 50)");
        System.out.print("Escolha: ");
        switch (scanner.nextLine().trim()) {
            case "1":
                taverna.setEstrategiaDesconto(new SemDesconto());
                break;
            case "2":
                taverna.setEstrategiaDesconto(new HappyHour());
                break;
            case "3":
                taverna.setEstrategiaDesconto(new BanqueteDoViajante());
                break;
            default:
                System.out.println("Opção inválida. Promoção mantida.");
                return;
        }
        System.out.println("Promoção definida: " + taverna.getEstrategiaDesconto().getDescricao());
    }

    public void exibirCaixa() {
        CaixaRegistradora caixa = CaixaRegistradora.getInstance();
        System.out.printf("\nTotal arrecadado: R$ %.2f%n", caixa.getTotalArrecadado());
        System.out.println("Histórico de vendas:");
        for (String venda : caixa.getHistoricoVendas()) {
            System.out.println(" - " + venda);
        }
    }
}
