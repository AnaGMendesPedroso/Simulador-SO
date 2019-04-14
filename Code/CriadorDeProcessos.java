import java.util.Scanner;
import java.util.Vector;

public class CriadorDeProcessos {
	private static Vector<Processo> filaEntrada = new Vector<Processo>();
	private static Vector<Processo> filaAux = new Vector<Processo>();
	private static Scanner scanner = new Scanner(System.in);

	public void criaProcesso(int qtdProcessos){
		while(qtdProcessos > 0) {
			int idProcesso = scanner.nextInt();
			int tamProcesso = scanner.nextInt();
			int chegada = scanner.nextInt();
			int burst = scanner.nextInt();
			Processo p = new Processo(idProcesso, tamProcesso, chegada, burst);
			filaAux.add(p);
			qtdProcessos--;
		}
	}
	public void ordenaPorChegada(){

		int iterator = filaAux.size();
		while(iterator > 0){
			int caux = filaAux.elementAt(0).getChegada();
			Processo c = filaAux.elementAt(0);

			for(int i = 0; i < filaAux.size(); i++){
				if(filaAux.elementAt(i).getChegada() < caux){
					c = filaAux.elementAt(i);
					caux = filaAux.elementAt(i).getChegada();
				}
			}
			filaEntrada.add(c);
			filaAux.remove(c);
			iterator--;
		}
	}
	public static void main(String [] args) {
		
		CriadorDeProcessos criador = new CriadorDeProcessos();
		// recebendo os parametros para iniciar
		System.out.println("Entre com tmp n tq (lista de dados de cada processo id tp tc tb)");
		int tamanhoMemoria = scanner.nextInt();
		int qtdProcessos = scanner.nextInt();
		int timeQuantum = scanner.nextInt();
		//criando processos
		criador.criaProcesso(qtdProcessos);
		//ordenando processos
		criador.ordenaPorChegada();
		//cria a classe memoria apenas com o tamanho
		Memoria mem = new Memoria(tamanhoMemoria);
		//cria o escalonador fcfs e com a lista de entrada e a memoria
		EscalonadorFirstComeFirstServed fcfs = new EscalonadorFirstComeFirstServed(mem, filaEntrada);
		//logo ele começa a escalonar
		fcfs.run();
		for(Processo a: filaEntrada) {
			a.run();
		}
		//invoca FCFS
		//FCFS faz: EscalonadorRoundRobin RR = new EscalonadorRoundRobin(timeQuantum,filaProntos);
		
		/*Para saber quem está na fila de entrada:
		filaEntrada.forEach(
			(Processo p)-> System.out.printf(
								"Processo %d :"
								+" %d %d %d \n",
								p.getIdProcesso(),
								p.getTamProcesso(),
								p.getChegada(),
								p.getBurst()
							)
		);
		*/
		}

}

