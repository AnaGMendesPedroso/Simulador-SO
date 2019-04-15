import java.util.Vector;

 class EscalonadorFirstComeFirstServed implements Runnable{
	private Memoria mem;
	private Vector<Processo> filaEntrada;
	private Vector<Processo> filaProntos;
	private  EscalonadorRoundRobin rr;
	
	public EscalonadorFirstComeFirstServed(Memoria mem, Vector<Processo> filaEntrada) {
		this.mem = mem;
		this.filaEntrada = filaEntrada;
		this.filaProntos= new Vector<Processo>();
	}
	public EscalonadorFirstComeFirstServed() {
	
	}
	public Memoria getMem() {
		return mem;
	}
	public void setMem(Memoria mem) {
		this.mem = mem;
	}
	public Vector<Processo> getFilaEntrada() {
		return filaEntrada;
	}
	public void setFilaEntrada(Vector<Processo> filaEntrada) {
		this.filaEntrada = filaEntrada;
	}
	//toda vez q ele for chamado ele irá..
	public void run() {
		while(!this.filaEntrada.isEmpty()) {
		
		Processo  primeiro = filaEntrada.remove(0);
		System.out.printf("Escalonador FCFS de longo prazo escolheu o processo %d%n",primeiro.getIdProcesso());
		int tamProcesso = primeiro.getTamProcesso();
		
			
		while(mem.getEspacoLivre()<tamProcesso);
		System.out.printf("Escalonador FCFS de longo prazo retirou o processo %d da fila de entrada, colocando-o na fila de prontos%n",primeiro.getIdProcesso());
			filaProntos.add(primeiro);
			Thread rrsoares = new Thread(rr);
			rrsoares.start();
			while(rrsoares.isAlive());
		}
	}
	public Vector<Processo> getFilaProntos() {
		return filaProntos;
	}
	public void setFilaProntos(Vector<Processo> filaProntos) {
		this.filaProntos = filaProntos;
	}
	public EscalonadorRoundRobin getRr() {
		return rr;
	}
	public void setRr(EscalonadorRoundRobin rr) {
		this.rr = rr;
	}
	public void removeProcesso(Processo p) {
		this.filaProntos.remove(p);
	}
	public void addFilaEntrada(Processo processoDaCpu) {
		this.filaEntrada.add(processoDaCpu);
	}
	

}