import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Vector;

public class Simulador {
    public static void main(String[] args) {
        Vector processos = new Vector<Processo>();
        Scanner scanner = new Scanner(System.in);
        ExecutorService executorDeThreads = Executors.newCachedThreadPool();


        System.out.println("Entre com tmp n tq (lista de dados de cada processo id tp tc tb)");
        int tamanhoMemoria = scanner.nextInt();
        int qtdProcessos = scanner.nextInt();
        int timeQuantum = scanner.nextInt();
        
        while(qtdProcessos > 0){
            int idProcesso = scanner.nextInt();
            int tamProcesso = scanner.nextInt();
            int chegada = scanner.nextInt();
            int burst = scanner.nextInt();
            Processo p = new Processo(idProcesso, tamProcesso, chegada, burst);
            processos.add(p);
            qtdProcessos--;

        }
        
        CriadorDeProcessos criador = new CriadorDeProcessos();
        criador.setFilaAux(processos);
        executorDeThreads.execute(criador);

        Memoria memoria = new Memoria(scanner.nextInt());
        executorDeThreads.execute(memoria);

                  
        EscalonadorFirstComeFirstServed fcfs = new EscalonadorFirstComeFirstServed();
        executorDeThreads.execute(fcfs);

        EscalonadorRoundRobin rr = new EscalonadorRoundRobin(scanner.nextInt());
        executorDeThreads.execute(rr);
    }    
}