package com.taverna;

import java.util.Scanner;

import com.taverna.observer.CozinhaObserver;
import com.taverna.observer.EstoqueBaixoObserver;
import com.taverna.repository.EstoqueRepository;
import com.taverna.repository.ItemRepository;
import com.taverna.service.TavernaService;
import com.taverna.singleton.CaixaRegistradora;

public class Main {

    public static void main(String[] args) {
        ItemRepository repositorio = new EstoqueRepository();
        Menus.popularCardapio(repositorio);

        TavernaService taverna = new TavernaService(repositorio);

        taverna.adicionarObserver(new CozinhaObserver());
        taverna.adicionarObserver(CaixaRegistradora.getInstance());
        taverna.adicionarObserver(new EstoqueBaixoObserver(3));

        Scanner scanner = new Scanner(System.in);
        Menus menus = new Menus(scanner, taverna);

        System.out.println("==== Bem-vindo à Taverna do Tião ====");

        boolean rodando = true;
        while (rodando) {
            menus.exibirMenu();
            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1": menus.exibirCardapio(); break;
                case "2": menus.cadastrarCliente(); break;
                case "3": menus.fazerPedido(); break;
                case "4": menus.exibirCaixa(); break;
                case "5": menus.cadastrarItem(); break;
                case "6": menus.adicionarSaldo(); break;
                case "7": menus.definirPromocao(); break;
                case "0":
                    rodando = false;
                    System.out.println("Até a próxima, viajante!");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }

        scanner.close();
    }
}
