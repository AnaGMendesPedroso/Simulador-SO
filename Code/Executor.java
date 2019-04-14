import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Executor {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CriadorDeProcessos criador = new CriadorDeProcessos();
        ExecutorService executor = Executors.newCachedThreadPool();

        System.out.println("Entre com tmp n tq (lista de dados de cada processo id tp tc tb)");
        int tamanhoMemoria = scanner.nextInt();
        int qtdProcessos = scanner.nextInt();
        int timeQuantum = scanner.nextInt();

        Memoria memoria = new Memoria(tamanhoMemoria);
    
        while(qtdProcessos > 0){
            executor.execute(criador);
        }
        
        EscalonadorFirstComeFirstServed fcfs = new EscalonadorFirstComeFirstServed(memoria, criador.getFilaEntrada());
        executor.execute(fcfs);

    }    
}