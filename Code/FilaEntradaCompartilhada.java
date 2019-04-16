import java.util.Vector;

public class FilaEntradaCompartilhada{
    private Vector<Processo> filaEntrada = new Vector<Processo>();

    public FilaEntradaCompartilhada(){}

    public synchronized Vector getFilaEntrada(){
        return filaEntrada;
    }

    public synchronized Processo removeProcessoDaFilaDeEntrada(){
        return filaEntrada.remove(0);
    
    }
    public synchronized void colocaNaFilaDeEntrada(Processo p){
        filaEntrada.add(p);
    }
}