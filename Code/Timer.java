import java.util.Vector;

public class Timer implements Runnable{
	private int tempoCpu;
	private FilaEntradaCompartilhada filaEntrada;
	private FilaProntosCompartilhada filaProntos;
	
    public Timer(FilaEntradaCompartilhada fe, FilaProntosCompartilhada fp){
		this.tempoCpu = 0;
		this.filaEntrada = fe;
    	this.filaProntos= fp;
    }
    public synchronized void clock() {
    	this.tempoCpu++;
    	}
    public synchronized int getTempoCpu() {
    	return this.tempoCpu;
    }
    public synchronized void iniciarTemporizadorAte(int temporizador) {
		int ateF = getTempoCpu() + temporizador;
        while (tempoCpu < ateF) {
            clock();
        }
    }
    
	@Override
    public void run() {
		synchronized(this){	
			clock();						
			notify();
		}
	}
}
