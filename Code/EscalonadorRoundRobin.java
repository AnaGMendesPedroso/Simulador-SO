import java.util.Vector;

public class EscalonadorRoundRobin implements Runnable {
    private final int timeQuantum;
    private Despachante despachante;
    private Processo processoEscolhido;
    FilaProntosCompartilhada filaProntos;

    public EscalonadorRoundRobin(int tq, FilaProntosCompartilhada filaProntos, Despachante d) {
        this.timeQuantum = tq;
        this.filaProntos = filaProntos;
        this.despachante = d;
    }

    public int getTimeQuantum() {
        return timeQuantum;
    }

    private synchronized void escalonaProcessoRR() throws InterruptedException {

        processoEscolhido = filaProntos.getPrimeiroProcesso();
        System.out.println("\nPRIMEIRO PROCESSO FILA PRONTOS PID: "+processoEscolhido.getIdProcesso());
       
        despachante.setProcessoEscolhidoParaExecucao(processoEscolhido);

        filaProntos.remove(processoEscolhido);
		filaProntos.printaFilaProntos();
        System.out.println("Escalonador Round-Robin de CPU escolheu o processo " + processoEscolhido.getIdProcesso()
                + ", retirou-o da fila de prontos e o encaminhou ao Despachante.");
    }

    @Override
    public void run() {
        synchronized(this){
                try {
                    escalonaProcessoRR();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                despachante.setTimeQuantumRR(timeQuantum);      
                filaProntos.addFilaProntos(processoEscolhido);
            notify();            
        }
    }    
}
