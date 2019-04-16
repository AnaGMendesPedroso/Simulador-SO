import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Vector;

public class Executor {
    public static void main(String[] args) throws InterruptedException {
        Vector processos = new Vector<Processo>();
        Scanner scanner = new Scanner(System.in);
        ExecutorService executorDeThreads = Executors.newCachedThreadPool();
        FilaProntosCompartilhada filaProntos = new FilaProntosCompartilhada();
        

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
        
        Vector<Processo> filaEntrada =ordenaProcessosPorChegada(processos);

        Timer timer = new Timer();
        executorDeThreads.execute(timer);

        CriadorDeProcessos criador = new CriadorDeProcessos(timer,filaEntrada);
        executorDeThreads.execute(criador);

        Memoria memoria = new Memoria(tamanhoMemoria);
        executorDeThreads.execute(memoria);

        EscalonadorFirstComeFirstServed fcfs = new EscalonadorFirstComeFirstServed(criador.getFilaEntrada(),filaProntos,memoria);
        executorDeThreads.execute(fcfs);

   
        EscalonadorRoundRobin rr = new EscalonadorRoundRobin(timeQuantum, filaProntos);
        executorDeThreads.execute(rr);

        
      
        Despachante despachante = new Despachante(filaProntos);
        executorDeThreads.execute(despachante);
    
        executorDeThreads.shutdown(); // executor encerra o 
                                    // atendimento a novas requisições,
                                    // mas continua executando aquelas que já foram iniciadas
        System.out.println("Término da observação\n");

    } 
    public static Vector<Processo> ordenaProcessosPorChegada(Vector<Processo> filaProcessosPassadosPeloUsuario){
    	 
    	 Vector<Processo> filaProcessosOrdenados = new Vector<Processo>();
		int iterator = filaProcessosPassadosPeloUsuario.size();
		while(iterator > 0){
			
			int caux = filaProcessosPassadosPeloUsuario.elementAt(0).getChegada();
			Processo c = filaProcessosPassadosPeloUsuario.elementAt(0);

			for(int i = 0; i < filaProcessosPassadosPeloUsuario.size(); i++){
				
				if(filaProcessosPassadosPeloUsuario.elementAt(i).getChegada() < caux){
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


}