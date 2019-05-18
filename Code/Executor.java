import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Vector;
//Entrada: 1000 3 2 1 500 5 15 0 700 1 5 2 600 2 10 

public class Executor {
	// private static
	public static void main(String[] args) {
		Vector processos = new Vector<Processo>();
		Scanner scanner = new Scanner(System.in);
		ExecutorService executorDeThreads = Executors.newCachedThreadPool();
		FilaProntosCompartilhada filaProntos = new FilaProntosCompartilhada();
		FilaEntradaCompartilhada filaEntrada = new FilaEntradaCompartilhada();

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

		processos = ordenaProcessosPorChegada(processos);

		Timer timer = new Timer(filaEntrada, filaProntos);
		CriadorDeProcessos criador = new CriadorDeProcessos(timer, filaEntrada, processos);
		Memoria memoria = new Memoria(tamanhoMemoria);
		Swapper swapper = new Swapper(memoria);
		Despachante despachante = new Despachante(timer, memoria, swapper, filaProntos);
		EscalonadorRoundRobin rr = new EscalonadorRoundRobin(timeQuantum, filaProntos, despachante);
		EscalonadorFirstComeFirstServed fcfs = new EscalonadorFirstComeFirstServed(filaEntrada, filaProntos, memoria,
				swapper, rr);
		swapper.setFCFS(fcfs);
		swapper.setDespachante(despachante);
		timer.setDespachante(despachante);
		timer.setCriador(criador);
		// despachante.setRR(rr);

		executorDeThreads.execute(timer);
		executorDeThreads.execute(criador);
		executorDeThreads.execute(fcfs);
		executorDeThreads.execute(swapper);
		executorDeThreads.execute(rr);
		executorDeThreads.execute(despachante);

		executorDeThreads.shutdown(); // executor encerra o
		// atendimento a novas requisições,
		// mas continua executando aquelas que já foram iniciadas
		if (executorDeThreads.isTerminated()) {
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
		// filaProcessosOrdenados.forEach((Processo p) ->
		// System.out.println(p.getIdProcesso()));
		return filaProcessosOrdenados;
	}
}