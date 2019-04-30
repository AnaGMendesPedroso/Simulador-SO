import java.util.Scanner;
import java.util.Vector;
// import java.util.concurrent.locks.ReentrantLock; 

public class CriadorDeProcessos implements Runnable {

	private static Scanner scanner = new Scanner(System.in);
	private Timer timer;
	private FilaEntradaCompartilhada filaEntrada;
	private Vector<Processo> filaProcessosOrdenados = new Vector<Processo>();
	private int 

	public CriadorDeProcessos(Timer t, Vector<Processo> fp, FilaEntradaCompartilhada filaEntrada) {
		this.timer = t;
		this.filaProcessosOrdenados = fp;
		this.filaEntrada = filaEntrada;
	}

	private synchronized void iniciaProcessosPorTempoChegada() throws InterruptedException {
		Processo pa = filaProcessosOrdenados.firstElement();

		System.out.printf("\n\tProcesso que será iniciado\nPID: %d\ntamanho:%d\nquando chegou:%d\nburst:%d\n",pa.getIdProcesso(), pa.getTamProcesso(), pa.getChegada(), pa.getBurst());
		System.out.println("Tempo CPU:"+timer.getTempoCpu());
		
		if (timer.getTempoCpu() == pa.getChegada()) {
			filaEntrada.colocaNaFilaDeEntrada(pa);
			System.out.println("Criador de processos criou o processo " + pa.getIdProcesso()
					+ " e o colocou na fila de entrada. ");
			filaProcessosOrdenados.remove(pa);
			notifyAll();
		}
		System.out.println("Finalizou iniciaProcesso");

	}

	public void run() {
		synchronized(this){
			int f = filaProcessosOrdenados.size();
			while(f>0){
				try {
					iniciaProcessosPorTempoChegada();
					timer.clock();
					f--;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
		
	}

}