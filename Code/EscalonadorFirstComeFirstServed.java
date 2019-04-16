import java.util.Vector;

class EscalonadorFirstComeFirstServed implements Runnable {
	private Vector<Processo> filaEntrada;
	private Memoria mem;
	private FilaProntosCompartilhada filaProntos;

	public EscalonadorFirstComeFirstServed(Vector<Processo> filaEntrada,FilaProntosCompartilhada filaProntos, Memoria mem) {
		this.filaEntrada = filaEntrada;
		this.mem = mem;
		this.filaProntos = filaProntos;
	}

	@Override
	public void run() {
		System.out.printf("cheguei aqui fcfs");

		if(!filaProntos.isEmpty()) {
		Processo p = filaEntrada.remove(0);
		
		System.out.printf("Escalonador FCFS de longo prazo escolheu o processo id %d%n", p.getIdProcesso());
		if (mem.getEspacoLivre() < p.getTamProcesso()) {
			filaProntos.addListaProntos(p);
			System.out.printf("Escalonador FCFS de longo prazo retirou o processo id %d da fila de entrada, colocando-o na fila de prontos%n",p.getIdProcesso());
		} else {
			System.out.printf("Escalonador FCFS de longo prazo não retirou o processo id %d da fila de entrada porque não há espaço na memória%d%n",p.getIdProcesso());

		}
		}
		else {
			System.out.printf("Término da observação%n");
		}

	}

}