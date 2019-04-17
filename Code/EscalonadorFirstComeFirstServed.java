import java.util.Vector;

class EscalonadorFirstComeFirstServed implements Runnable {

	private Memoria mem;
	private FilaProntosCompartilhada filaProntos;
	private FilaEntradaCompartilhada filaEntrada;

	public EscalonadorFirstComeFirstServed(FilaEntradaCompartilhada filaEntrada, FilaProntosCompartilhada filaProntos,
			Memoria mem) {
		this.filaEntrada = filaEntrada;
		this.mem = mem;
		this.filaProntos = filaProntos;
	}

	@Override
	public void run() {
		synchronized(this){
			while(!filaEntrada.isEmpty()){
				Processo p = filaEntrada.getProcessoPosicao(0);
				filaEntrada.removeProcessoDaFilaDeEntrada(p);
			
				System.out.printf("Escalonador FCFS de longo prazo escolheu o processo %d\n", p.getIdProcesso());
				
				if (mem.getEspacoLivre() >= p.getTamProcesso()) {
					filaProntos.addFilaProntos(p);
					System.out.printf("Escalonador FCFS de longo prazo retirou o processo %d da fila de entrada, colocando-o na fila de prontos\n",p.getIdProcesso());
					filaProntos.printaFilaProntos();
				} else {
					System.out.printf("Escalonador FCFS de longo prazo não retirou o processo %d da fila de entrada porque não há espaço na memória ESPACO LIVRE:%d\n\t TAMPROCESSO:%d\n",p.getIdProcesso(),mem.getEspacoLivre(), p.getTamProcesso());

				}
			}
			notify();
		}
	}
}