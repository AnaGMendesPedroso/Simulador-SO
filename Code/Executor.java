import java.util.Scanner;

public class Executor {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        CriadorDeProcessos criador = new CriadorDeProcessos();
        
        System.out.println("Entre com tmp n tq (lista de dados de cada processo id tp tc tb)");
        int tamanhoMemoria = scanner.nextInt();
        int qtdProcessos = scanner.nextInt();
        int timeQuantum = scanner.nextInt();
        Thread criadorProcessos = new Thread(criador);
        
        Memoria memoria = new Memoria(tamanhoMemoria);
        
        	criador.setQtdP(qtdProcessos);
            criadorProcessos.start();
            
            EscalonadorFirstComeFirstServed fcfs= new EscalonadorFirstComeFirstServed(memoria, criador.getFilaEntrada());      
            EscalonadorRoundRobin rr= new EscalonadorRoundRobin(timeQuantum,fcfs);
            fcfs.setRr(rr);
            criadorProcessos.join();
            Thread fifo = new Thread(fcfs);
            fifo.start();
            
       /* executor.execute(fcfs);
        * 1000 3 2 1 500 5 15 0 700 1 5 2 600 2 10
*/
    }    
}