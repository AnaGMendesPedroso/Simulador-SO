import java.util.Vector;

public class EscalonadorFirstComeFirstServed implements Runnable {

	private Memoria mem;
	private FilaProntosCompartilhada filaProntos;
	private FilaEntradaCompartilhada filaEntrada;
	private EscalonadorRoundRobin rr;
	private Swapper swapper;
	private Timer timer;
	private Vector<Processo> processos;
	private Processo p = null;

	public EscalonadorFirstComeFirstServed(FilaEntradaCompartilhada filaEntrada, FilaProntosCompartilhada filaProntos,
			Memoria mem, Swapper s, EscalonadorRoundRobin rr2, Timer t, Vector<Processo> p) {
		this.filaEntrada = filaEntrada;
		this.filaProntos = filaProntos;
		this.mem = mem;
		this.swapper = s;
		this.rr = rr2;
		this.timer = t;
		this.processos = p;
	}
	private int calculaBurstTotal(){
		int burstTotal = 0;
		for(int i = 0; i< processos.size(); i++){
			burstTotal += processos.get(i).getBurst();
		}
		return burstTotal;
	}

	private synchronized void escalonaProcesso() {
		synchronized (filaEntrada) {
			p = filaEntrada.pegaPrimeiroProcesso();
		}
		if (mem.getEspacoLivre() >= p.getTamProcesso()) {
			synchronized (filaEntrada) {
				filaEntrada.removeProcessoDaFilaDeEntrada(p);
			}
			System.out.printf("Escalonador FCFS de longo prazo escolheu e retirou o processo %d  da fila de entrada\n",
					p.getIdProcesso());
			swapper.setProcesso(p);
			swapper.setQuemPediu(false);
			synchronized (swapper) {
				swapper.notify();
				System.out.printf(
						"Escalonador FCFS de longo prazo solicitou que o Swapper traga o processo %d à memoria\n",
						p.getIdProcesso());
			}
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			if (swapper.getConseguiuColocarProcessoNaMemoria()) {
				System.out.printf("Swapper avisa o Escalonador FCFS de longo prazo que o processo %d está na memória\n",
						p.getIdProcesso());
				synchronized (filaProntos) {
					filaProntos.addFilaProntos(p);
				}
				System.out.printf("Escalonador FCFS de longo prazo colocou o processo %d na fila de prontos\n",
						p.getIdProcesso());
				// filaProntos.printaFilaProntos();
			}
		} else {
			System.out.printf(
					"Escalonador FCFS de longo prazo não retirou o processo %d da fila de entrada porque não há espaço na memória!\nESPACO LIVRE MEMÓRIA:%d\n \tTAMPROCESSO:%d\n",
					p.getIdProcesso(), mem.getEspacoLivre(), p.getTamProcesso());
			synchronized (swapper) {
				swapper.limpaMemoria(p.getTamProcesso());
			}
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println(
					"Escalonador FCFS de longo prazo solicitou que Swapper retire alguns processos da memória para colocar o processo "
							+ p.getIdProcesso());
			escalonaProcesso();
		}
	}

	@Override
	public void run() {
		while(calculaBurstTotal()>0){
			while (filaEntrada.isEmpty()) {
				synchronized (this) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			while(!filaEntrada.isEmpty()){
				escalonaProcesso();
				timer.clock();
				synchronized (rr) {
					rr.notify();
				}
			}
			calculaBurstTotal();		
		}
	}
}