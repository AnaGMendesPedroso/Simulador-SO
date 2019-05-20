import java.util.Vector;

public class CriadorDeProcessos implements Runnable {

	private Timer timer;
	private EscalonadorFirstComeFirstServed fcfs;
	private FilaEntradaCompartilhada filaEntrada;
	private Vector<Processo> filaProcessosOrdenados = new Vector<Processo>();

	public CriadorDeProcessos(Timer t, FilaEntradaCompartilhada filaEntrada, Vector<Processo> fprocessos) {
		this.timer = t;
		this.filaEntrada = filaEntrada;
		this.filaProcessosOrdenados = fprocessos;
	}

	private synchronized void iniciaProcesso(Processo pa) {
		while (timer.getTempoCpu() != pa.getChegada()) {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		synchronized (filaEntrada) {
			filaEntrada.colocaNaFilaDeEntrada(pa);
		}
		System.out.println(
				"Criador de processos criou o processo " + pa.getIdProcesso() + " e o colocou na fila de entrada. ");
		filaProcessosOrdenados.remove(pa);
		synchronized(fcfs){
			fcfs.notify();
		}
	}

	public void setFCFS(EscalonadorFirstComeFirstServed fcfs2) {
		this.fcfs = fcfs2;
	}

	public void run() {
		while (filaProcessosOrdenados.size() > 0) {
			Processo pa = filaProcessosOrdenados.firstElement();
			iniciaProcesso(pa);
		}
	}
}