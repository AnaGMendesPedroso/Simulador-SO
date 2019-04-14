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
		if(verificaSeProcessoEstaNaMemoria(processo)){
			timer = new Timer();
			cpu = new CPU(processo,timeQuantum, timer);
			cpu.run();
		}
		
		
	}
	

    
}