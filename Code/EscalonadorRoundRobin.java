import java.util.Vector;

public class EscalonadorRoundRobin extends Thread{
    private long timeQuantum;
    private static Vector<Processo> filaProntos = new Vector<Processo>();
    private Despachante despachante;

    public EscalonadorRoundRobin(long tq, Vector<Processo>filaProntos){
        this.timeQuantum = tq;
        this.filaProntos = filaProntos;
    }
    public long getTimeQuantum() {
        return timeQuantum;
    }
        
}