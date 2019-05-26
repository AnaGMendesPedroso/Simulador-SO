public class Timer implements Runnable {
	private Despachante despachante;
	private Processo p;
	private boolean livre = true;
	private Executor executor;
	private boolean terminou = false;

	public Timer (Executor e){
		this.executor = e;
	}
	public void setDespachante(Despachante d){
		this.despachante = d;
	}
	public boolean isLivre() {
		return livre;
	}

	public void setLivre(boolean livre) {
		this.livre = livre;
		synchronized(despachante){
			despachante.notify();
		}
	}
	public boolean terminou() {
		return terminou;
	}

	public void setTerminou(boolean terminou) {
		this.terminou = terminou;
	}
	public synchronized void setP(Processo p) {
		this.p = p;
	}

	public synchronized void simulaExecucao(int t) {
		while (livre) {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		int tempo = t+executor.getTempoAtualTotal();
		while(executor.getTempoAtualTotal() < tempo){
			executor.setTempoAtualTotal();
		}
		System.out.println("Timer informa ao Despachante que o processo " + p.getIdProcesso()
				+ " atualmente em execução precisa ser retirado da CPU");
		terminou=true;
		synchronized(despachante){
			despachante.notify();
		}
	}
	@Override
	public void run() {
		while(livre){
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}