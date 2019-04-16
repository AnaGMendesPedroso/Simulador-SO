import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Vector;

public class Executor {
    public static void main(String[] args) {
        Vector processos = new Vector<Processo>();
        Scanner scanner = new Scanner(System.in);
        ExecutorService executorDeThreads = Executors.newCachedThreadPool();
        boolean fcfsTerminouDeEscalonar = false;


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

        Timer timer = new Timer();
        executorDeThreads.execute(timer);

        CriadorDeProcessos criador = new CriadorDeProcessos(timer);
        criador.setfilaProcessosPassadosPeloUsuario(processos);
        executorDeThreads.execute(criador);

        Memoria memoria = new Memoria(scanner.nextInt());
        executorDeThreads.execute(memoria);

        EscalonadorFirstComeFirstServed fcfs = new EscalonadorFirstComeFirstServed(criador.getFilaEntrada(), memoria);
        executorDeThreads.execute(fcfs);

        int tq = scanner.nextInt();

        while (!fcfsTerminouDeEscalonar) {
            try {
                executorDeThreads.wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            fcfsTerminouDeEscalonar = true;
        }
        EscalonadorRoundRobin rr = new EscalonadorRoundRobin(tq, fcfs);
        executorDeThreads.execute(rr);

        try {
            executorDeThreads.wait();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Despachante despachante = new Despachante();
        executorDeThreads.execute(despachante);
    
       executorDeThreads.shutdown(); // executor encerra o 
                                    // atendimento a novas requisições,
                                    // mas continua executando aquelas que já foram iniciadas
        System.out.println("Término da observação\n");

    }    

}