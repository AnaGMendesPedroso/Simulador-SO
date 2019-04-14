import java.util.Vector;

 class EscalonadorFirstComeFirstServed implements Runnable{
	private Memoria mem;
	private Vector<Processo> filaEntrada;
	private Vector<Processo> filaProntos;
	private  EscalonadorRoundRobin rr;
	
	public EscalonadorFirstComeFirstServed(Memoria mem, Vector<Processo> filaEntrada) {
		this.mem = mem;
		this.filaEntrada = filaEntrada;
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
		//while(!filaEntrada.isEmpty()) {
		//pegar o primeiro da fila para ser executado
		Processo primeiro = filaEntrada.remove(0);
		//guarda o tamanho dele
		int tamProcesso = primeiro.getTamProcesso();
		// espera pra ver se ninguem ta alterando a memoria
		//(metodo ainda não implementado)
			
		// então verifidca se tem lugar para o processo na memoria
		if(mem.getEspacoLivre()>tamProcesso) {
			// se sim coloca na fila de prontos
			filaProntos.add(primeiro);
			rr.run();
				
		}
		else {
			// se não, wait ddd
		}
		
	//	}	
	}
	

}