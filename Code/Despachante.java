public class Despachante extends Thread{
	private Processo p;
	private Memoria mem;

	public Despachante(Processo p, Memoria mem) {
	// recebe um processo e a memoria
		this.p = p;
		this.mem= mem;
	}

	public Processo getP() {
		return p;
	}

	public void setP(Processo p) {
		this.p = p;
	}
	
	public void run() {
		// se o processo estiver na memoria
		if(mem.getListaProntos().contains(p)) {
			// reinicia timer
			//libera a cpu para o processo
		}else {
			// se nao esta na memoria então esta no disco
			// chama o swaper  pra pegar o processo no disco
			// espera ele pegar
			// libera a cpu
		}
		
	}
	

    
}