import java.util.Vector;

class EscalonadorRoundRobin implements Runnable {
    private final int timeQuantum;
    private EscalonadorFirstComeFirstServed fcfs;
    private Despachante despachante;
    private Processo p;

    public EscalonadorRoundRobin(int tq, EscalonadorFirstComeFirstServed fcfs) {
        this.timeQuantum = tq;
        this.fcfs = fcfs;
    }

    public int getTimeQuantum() {
        return timeQuantum;
    }

    private synchronized void escalonaProcessoRR() {
        Processo processoEscolhido = fcfs.getFilaProntos().get(0);
        fcfs.removeProcessoDaFilaProntos(processoEscolhido);
        despachante.setProcessoEscolhidoParaExecucao(processoEscolhido);

        System.out.println("Escalonador Round-Robin de CPU escolheu o processo "
                            +processoEscolhido.getIdProcesso()+
                            " , retirou-o da fila de prontos e o encaminhou ao Despachante.");
    }

    @Override
    public void run() {
        escalonaProcessoRR();
        despachante.setTimeQuantumRR(timeQuantum);
        try {
            wait();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        fcfs.addListaProntos(p);
        notify();
    }    
}