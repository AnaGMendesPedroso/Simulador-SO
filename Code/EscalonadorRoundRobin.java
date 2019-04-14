import java.util.Vector;

public class EscalonadorRoundRobin implements Runnable{
    private long timeQuantum;
    private EscalonadorFirstComeFirstServed fcfs;
    private Despachante despachante;
    private Processo p;

    public EscalonadorRoundRobin(long tq, EscalonadorFirstComeFirstServed fcfs){
        this.timeQuantum = tq;
        this.fcfs = fcfs;
    }
    public long getTimeQuantum() {
        return timeQuantum;
    }

    private void escalonaProcesso(){
        p = fcfs.getFilaProntos().firstElement();
        fcfs.removeProcesso(p);
        despachante = new Despachante(p, timeQuantum);
        despachante.run();
    }
    @Override
    public void run() {
        escalonaProcesso();
    }
    
    
}