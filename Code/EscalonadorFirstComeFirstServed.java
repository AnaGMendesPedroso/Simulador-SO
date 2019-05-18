import java.util.Vector;

class EscalonadorFirstComeFirstServed implements Runnable {

	private Memoria mem;
	private FilaProntosCompartilhada filaProntos;
	private FilaEntradaCompartilhada filaEntrada;
	private Swapper swapper;
	private EscalonadorRoundRobin rr;
	private Processo p = null;
	private boolean temProcessoParaEscalonar = false;

	public EscalonadorFirstComeFirstServed(FilaEntradaCompartilhada filaEntrada, FilaProntosCompartilhada filaProntos,
			Memoria mem, Swapper s, EscalonadorRoundRobin r) {
		this.filaEntrada = filaEntrada;
		this.filaProntos = filaProntos;
		this.mem = mem;
		this.swapper = s;
		this.rr = r;
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (swapper.getConseguiuColocarProcessoNaMemoria()) {
				synchronized (filaProntos) {
					filaProntos.addFilaProntos(p);
				}
				System.out.printf("Escalonador FCFS de longo prazo colocou %d na fila de prontos\n", p.getIdProcesso());
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
					// TODO Auto-generated catch block
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
		while (filaEntrada.isEmpty()) {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		escalonaProcesso();
	}
}