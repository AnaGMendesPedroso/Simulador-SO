import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Vector;

public class Executor {

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
    public static void main(String[] args) throws InterruptedException {
        Vector <Processo> processos = new Vector<Processo>();
        Scanner scanner = new Scanner(System.in);
        ExecutorService executorDeThreads = Executors.newCachedThreadPool();
        FilaProntosCompartilhada filaProntos = new FilaProntosCompartilhada();
        FilaEntradaCompartilhada filaEntrada = new FilaEntradaCompartilhada();

    
        System.out.println("Início da observação\n");
        System.out.println("Entre com tmp n tq (lista de dados de cada processo id tp tc tb)");
        int tamanhoMemoria = scanner.nextInt();
        int qtdProcessos = scanner.nextInt();
        int timeQuantum = scanner.nextInt();
      
        Memoria memoria = new Memoria(tamanhoMemoria);

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
        filaEntrada.printaFilaEntrada();
        filaProntos.printaFilaProntos();

        Timer timer = new Timer(filaEntrada, filaProntos);
        executorDeThreads.execute(timer);
       
        synchronized(timer){
            try{
                System.out.println("\nAguardando TIMER terminar run()...");
                timer.wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }

        CriadorDeProcessos criador = new CriadorDeProcessos(timer,processos,filaEntrada);
        executorDeThreads.execute(criador);
 
        synchronized(criador){
            try{
                System.out.println("\nAguardando CRIADOR terminar run()...");
                criador.wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }

        EscalonadorFirstComeFirstServed fcfs = new EscalonadorFirstComeFirstServed(filaEntrada,filaProntos,memoria);
        executorDeThreads.execute(fcfs);
        synchronized(fcfs){
            try{
                System.out.println("\nguardando FCFS terminar run()...");
                fcfs.wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }

        Despachante despachante = new Despachante(filaProntos,memoria);
        EscalonadorRoundRobin rr = new EscalonadorRoundRobin(timeQuantum, filaProntos,despachante);
        executorDeThreads.execute(rr);
        synchronized(rr){
            try{
                System.out.println("\nAguardando RR terminar run()...");
                rr.wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }          
       
        executorDeThreads.execute(despachante);
        synchronized(despachante){
            try{
                System.out.println("\nAguardando DESPACHANTE terminar run()...");
                despachante.wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }

        executorDeThreads.shutdown(); // executor encerra o 
                                    // atendimento a novas requisições,
                                    // mas continua executando aquelas que já foram iniciadas
        System.out.println("Término da observação\n");

    }
}