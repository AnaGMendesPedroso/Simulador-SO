import java.util.Vector;

class EscalonadorFirstComeFirstServed implements Runnable {
	private FilaEntrada listaEntrada;
	private Memoria mem;
	private ListaProntosCompartilhada listaProntos;

	public EscalonadorFirstComeFirstServed(FilaEntrada listaEntrada,ListaProntosCompartilhada listaProntos, Memoria mem) {
		this.listaEntrada = listaEntrada;
		this.mem = mem;
		this.listaProntos = listaProntos;
	}

	@Override
	public void run() {
		//mem.wait();
		if(!listaProntos.getFilaProntos().isEmpty()) {
		Processo p = filaEntrada.removePrimeiro();
		
		System.out.printf("Escalonador FCFS de longo prazo escolheu o processo id %d%n", p.getIdProcesso());
		if (mem.getEspacoLivre() < p.getTamProcesso()) {
			listaProntos.addListaProntos(p);
			System.out.printf("Escalonador FCFS de longo prazo retirou o processo id %d da fila de entrada, colocando-o na fila de prontos%n",p.getIdProcesso());
			this.notifyAll();
		} else {
			System.out.printf("Escalonador FCFS de longo prazo não retirou o processo id %d da fila de entrada porque não há espaço na memória%d%n",p.getIdProcesso());

		}
		}
		else {
			System.out.printf("Término da observação%n");
		}

	}

}