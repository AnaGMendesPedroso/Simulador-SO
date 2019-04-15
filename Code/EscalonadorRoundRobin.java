import java.util.Vector;

 class EscalonadorRoundRobin implements Runnable{
    private int timeQuantum;
    private EscalonadorFirstComeFirstServed fcfs;
    private Despachante despachante;
    private Processo p;
    private Timer tempo;

    public EscalonadorRoundRobin(int tq, EscalonadorFirstComeFirstServed fcfs){
        this.timeQuantum = tq;
        this.fcfs = fcfs;
    }

    public int getTimeQuantum() {
        return timeQuantum;
    }

    private synchronized void escalonaProcessoRR(){
        Processo processoEscolhido = fcfs.getFilaProntos().get(0);
        fcfs.removeProcessoDaFilaProntos(processoEscolhido);
        despachante.setProcessoEscolhidoParaExecucao(processoEscolhido);
                     
    }
    @Override
    public void run() {
        escalonaProcessoRR();
        despachante.setTimeQuantumRR(timeQuantum);
        wait();
        fcfs.voltaProcessoParaFilaProntos(p);
    }    
}