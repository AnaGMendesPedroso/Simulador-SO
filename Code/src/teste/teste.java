package teste;
import java.util.Scanner;
import java.util.Vector;

import simulador.CriadorProcessos;
public class teste {
	public static void main(String [] args) {
		
		 Vector<Thread> listaEntrada = new Vector<Thread>();
		 Vector<Thread> listaAux = new Vector<Thread>();
		Scanner teclado = new Scanner(System.in);
		
		int tamanhoMemoria;
		int timeQuantum;
		int qtdProcessos;
		System.out.println("Entre com tmp n tq (lista de dados de cada processo id tp tc tb)");
		tamanhoMemoria = teclado.nextInt();
		qtdProcessos = teclado.nextInt();
		timeQuantum = teclado.nextInt();
		
		while(qtdProcessos > 0) {
			 int idProcesso = teclado.nextInt();
			 int tamProcesso = teclado.nextInt();
			 int chegada = teclado.nextInt();
			 int burst = teclado.nextInt();
			 CriadorProcessos p = new CriadorProcessos(idProcesso, tamProcesso, chegada, burst);
			 listaAux.add(p);
			 qtdProcessos--;
		 }
		for(int i = 0; i<listaAux.size();i++) {
			listaAux.get(i).run();
		}
		
	}

}
