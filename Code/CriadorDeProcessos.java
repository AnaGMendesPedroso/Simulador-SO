import java.util.Vector;

public class CriadorDeProcessos implements Runnable {

	private Timer timer;
	private FilaEntradaCompartilhada filaEntrada;
	private Vector<Processo> filaProcessosOrdenados = new Vector<Processo>();
	private boolean aindaTemProcessoParaCriar = false;
	private Processo pa;

	public CriadorDeProcessos(Timer t, FilaEntradaCompartilhada filaEntrada, Vector<Processo> fprocessos) {
		this.timer = t;
		this.filaEntrada = filaEntrada;
		this.filaProcessosOrdenados = fprocessos;
	}

	private synchronized void iniciaProcesso() {
		synchronized (filaEntrada) {
			filaEntrada.colocaNaFilaDeEntrada(pa);
		}
		System.out.println(
				"Criador de processos criou o processo " + pa.getIdProcesso() + " e o colocou na fila de entrada. ");
		filaProcessosOrdenados.remove(pa);
		verificaSeAindaTemProcessoParaCriar();
	}

	private void verificaSeAindaTemProcessoParaCriar() {
		if (filaProcessosOrdenados.size() > 0) {
			aindaTemProcessoParaCriar = true;
			pa = filaProcessosOrdenados.firstElement();
		} else
			aindaTemProcessoParaCriar = false;
	}

	public void run() {
		verificaSeAindaTemProcessoParaCriar();
		while (aindaTemProcessoParaCriar) {
			while (timer.getTempoCpu() != pa.getChegada()) {
				synchronized (this) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			iniciaProcesso();
			timer.clock();
		}
	}
}