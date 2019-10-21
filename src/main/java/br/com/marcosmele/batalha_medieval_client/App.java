package br.com.marcosmele.batalha_medieval_client;

import java.util.List;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {
	
	private static Scanner keyboard = new Scanner(System.in);
	
	public static void main(String[] args) throws Exception {
		
		keyboard = new Scanner(System.in);
		
		StringBuilder sb = new StringBuilder();
		sb.append("Olá, bem vindo(a) ao melhor jogo de batalha medieval do Brasil.");
		sb.append("\r\nDigite seu nickname e vamos iniciar uma nova partida");
		sb.append("\r\n-------------------------------------------------------");
		
		System.out.println(sb.toString());
		String nickname = keyboard.next();
		
		Batalha batalha = new Batalha(nickname);
		
		batalha.iniciar();
		int heroi;
		if(batalha.getProximoPasso().equals("heroi")) {
			do {
				sb = new StringBuilder();
				sb.append("\r\n-------------------------------------------------------");
				sb.append("\r\nAgora escolha seu herói de acordo com as opções abaixo: ");
				List<String> herois = batalha.herois();
				for (int i = 0; i < herois.size(); i++) {
					sb.append("\r\n " + herois.get(i) + " (" + (i+1) + ")");
				}
				sb.append("\r\n-------------------------------------------------------");
				System.out.println(sb.toString());
			
				heroi = keyboard.nextInt();
			} while(!batalha.heroiValido(heroi-1));
			
			batalha.escolher(heroi-1);
		}
		
		System.out.println("\r\n-------------------------------------------------------");
		System.out.println("Iniciando a próxima rodada...");
		
		do {
			
			if(batalha.getProximoPasso().equals("iniciativa")) {
				System.out.println("Realizando iniciativa...");
		
				batalha.iniciativa();
			
				System.out.println("Você tirou..." + batalha.getIniciativaHeroi());
				Thread.sleep(1000);
				System.out.println("Seu oponente tirou..." + batalha.getIniciativaMonstro());
				Thread.sleep(1000);
				System.out.println("Somando capacidade dos personagens...");
				Thread.sleep(1000);
				System.out.println("Você.." + batalha.isIniciativaVencedor());
				System.out.println("\r\n-------------------------------------------------------");
			}
			
			if(batalha.getProximoPasso().equals("ataque")) {
			
				batalha.ataque();
				
				System.out.println("\r\n-------------------------------------------------------");
				System.out.println("Realizando ataque...");
				System.out.println("Você tirou..." + batalha.getDadoAtaqueHeroi());
				Thread.sleep(1000);
				System.out.println("Seu oponente tirou..." + batalha.getDadoAtaqueMonstro());
				Thread.sleep(1000);
				System.out.println("Somando capacidade dos personagens...");
				Thread.sleep(1000);
				System.out.println("O ataque foi um..." + batalha.getResumoAtaque());
				Thread.sleep(1000);
				System.out.println("Calculando dano...");
				Thread.sleep(1000);
				System.out.println("Total do dano..." + batalha.getTotalDano() + " pontos de vida");
				Thread.sleep(1000);
				System.out.println("\r\n-------------------------------------------------------");
				System.out.println("Resumo: ");
				System.out.println("Seus pontos de vida: " + batalha.getVidaHeroi());
				System.out.println("Seus inimigo: " + batalha.getVidaMonstro());
				System.out.println("\r\n-------------------------------------------------------");
			}
		
		} while(batalha.getVidaHeroi() != 0 && batalha.getVidaMonstro() != 0);
		
		if(batalha.getVidaHeroi() == 0) {
			System.out.println("Você perdeu...");
		} else if(batalha.getVidaMonstro() == 0) {
			System.out.println("VOCÊ VENCEU!!!");
		}
		
		System.out.println("\r\n-------------------------------------------------------");
		System.out.println("Veja como está o nosso ranking.");
		batalha.ranking().forEach(posicao->{
			System.out.println(posicao);
		});
		
		keyboard.close();
	}
	
}
