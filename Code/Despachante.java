public class Despachante extends Thread{
	private Processo processo;
	private Memoria memoria;
	private Timer timer;
	private long timeQuantum;
	private CPU cpu;

	public Despachante(Processo p, long tq) {
		this.processo = p;
		this.timeQuantum = tq;
	}

	private boolean verificaSeProcessoEstaNaMemoria(Processo p){
		boolean resposta = false;
		if(memoria.getProcessosNaMemoria().contains(p)){
			resposta = true;
		}
		return resposta;
	}
	
	
	public void run() {
		while(timer.isUsando());
		System.out.printf("Despachante percebe que o processo %d está na memória%n",processo.getIdProcesso());
		timer.setProcessoDaCpu(processo);
		timer.reinicia();
		System.out.printf("Despachante reiniciou o Timer com tq e liberou a CPU ao processo %d%n",processo.getIdProcesso());
		Thread t = new Thread(timer);
		t.start();
		while(t.isAlive());
		
		
		
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}
	

    
}