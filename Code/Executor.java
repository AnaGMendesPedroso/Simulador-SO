/*Implementado por Ana Gabrielly Mendes Pedroso
	Estrutura: Todo o trabalho foi desenvoldido em Java. Todos os códigos necessários para a execução
		 estão na pasta Simulador-SO/Code/. 
	Para execução: 
	-entre na pasta Code:
		cd Simulador-SO/Code
	-compile os arquivos .java:
		javac *.java
	-execute a classe Executor:
		java Executor
	-Após a mensagem "Término da observação" tecle Ctrl+C para sair da execução
	(considere executar esse último passo 2 vezes)
	A saída obtida para a entrada:
	 1000 3 2 1 500 5 15 0 700 1 5 2 600 2 10
	  está no arquivo saida.txt na pasta Code/
*/
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Vector;

public class Executor {
	private int tempoAtualTotal = 0;

	public static void main(String[] args) {
		Vector<Processo> processos = new Vector<Processo>();
		Scanner scanner = new Scanner(System.in);
		ExecutorService executorDeThreads = Executors.newCachedThreadPool();
		FilaProntosCompartilhada filaProntos = new FilaProntosCompartilhada();
		FilaEntradaCompartilhada filaEntrada = new FilaEntradaCompartilhada();
		Executor e = new Executor();

		System.out.println("Início da observação\n");
		System.out.println("Entre com tmp n tq (lista de dados de cada processo id tp tc tb)");
		int tamanhoMemoria = scanner.nextInt();
		int qtdProcessos = scanner.nextInt();
		int timeQuantum = scanner.nextInt();

		while (qtdProcessos > 0) {
			int idProcesso = scanner.nextInt();
			int tamProcesso = scanner.nextInt();
			int chegada = scanner.nextInt();
			int burst = scanner.nextInt();
			Processo p = new Processo(idProcesso, tamProcesso, chegada, burst);
			processos.add(p);
			qtdProcessos--;
		}
		scanner.close();
		System.out.println();
		processos = ordenaProcessosPorChegada(processos);

		Timer temporizador = new Timer(e);
		Memoria memoria = new Memoria(tamanhoMemoria);
		CriadorDeProcessos criador = new CriadorDeProcessos(e, filaEntrada, processos);
		Swapper swapper = new Swapper(memoria);
		Despachante despachante = new Despachante(temporizador, memoria, swapper);
		EscalonadorRoundRobin rr = new EscalonadorRoundRobin(timeQuantum, filaProntos, despachante);
		EscalonadorFirstComeFirstServed fcfs = new EscalonadorFirstComeFirstServed(filaEntrada, filaProntos, memoria,
				swapper, rr);

		temporizador.setDespachante(despachante);
		criador.setFCFS(fcfs);
		swapper.setFCFS(fcfs);
		swapper.setDespachante(despachante);
		despachante.setRR(rr);
		rr.setFCFS(fcfs);

		executorDeThreads.execute(despachante);
		executorDeThreads.execute(swapper);
		executorDeThreads.execute(rr);
		executorDeThreads.execute(fcfs);
		executorDeThreads.execute(criador);
		executorDeThreads.execute(temporizador);

		executorDeThreads.shutdown();
		try {
			executorDeThreads.awaitTermination(3, TimeUnit.SECONDS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		if (executorDeThreads.isShutdown()) {
			System.out.println("Término da observação\n");
		}
	}
	
	public static Vector<Processo> ordenaProcessosPorChegada(Vector<Processo> filaProcessosPassadosPeloUsuario) {

		Vector<Processo> filaProcessosOrdenados = new Vector<Processo>();
		int iterator = filaProcessosPassadosPeloUsuario.size();
		while (iterator > 0) {

			int caux = filaProcessosPassadosPeloUsuario.elementAt(0).getChegada();
			Processo c = filaProcessosPassadosPeloUsuario.elementAt(0);

			for (int i = 0; i < filaProcessosPassadosPeloUsuario.size(); i++) {

				if (filaProcessosPassadosPeloUsuario.elementAt(i).getChegada() < caux) {
					c = filaProcessosPassadosPeloUsuario.elementAt(i);
					caux = filaProcessosPassadosPeloUsuario.elementAt(i).getChegada();
				}
			}

			filaProcessosOrdenados.add(c);
			filaProcessosPassadosPeloUsuario.remove(c);
			iterator--;
		}
		return filaProcessosOrdenados;
	}

	public synchronized int getTempoAtualTotal() {
		return tempoAtualTotal;
	}

	public  synchronized void setTempoAtualTotal() {
		this.tempoAtualTotal += 1;
	}
}