import java.util.Vector;

class EscalonadorFirstComeFirstServed implements Runnable {
	private Vector<Processo> listaEntrada;
	private Memoria mem;
	private Vector<Processo> listaProntos;

	public EscalonadorFirstComeFirstServed(Vector<Processo> listaEntrada, Memoria mem) {
		this.listaEntrada = listaEntrada;
		this.mem = mem;
		this.listaProntos = new Vector<Processo>();
	}

	public synchronized void addListaProntos(Processo p) {
		this.listaProntos.add(p);
	}

	public synchronized void removeProcessoDaFilaProntos(Processo p) {
		this.listaProntos.remove(p);
	}

	public synchronized Processo escalonaFifo() {
		return this.listaEntrada.remove(0);
	}

	@Override
	public void run() {
		try {
			mem.wait();
			Processo p = this.escalonaFifo();
			System.out.printf("Escalonador FCFS de longo prazo escolheu o processo id %d%n", p.getIdProcesso());
			if (mem.getEspacoLivre() < p.getChegada()) {
				this.addListaProntos(p);
				System.out.printf("Escalonador FCFS de longo prazo retirou o processo id %d da fila de entrada, colocando-o na fila de prontos%n",p.getIdProcesso());
				this.notifyAll();
			} else {
				System.out.printf("Escalonador FCFS de longo prazo não retirou o processo id %d da fila de entrada porque não há espaço na memória%d%n",p.getIdProcesso());

			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}