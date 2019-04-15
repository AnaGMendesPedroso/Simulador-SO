public class Despachante implements Runnable{
	
	private Processo processo;
	private Memoria memoria;
	private Timer timer;
	private int timeQuantum;

	public Despachante() {}

	public void setTimeQuantum(int tq){
		this.setTimeQuantum(tq);
	}
	private boolean verificaSeProcessoEstaNaMemoria(Processo p){
		boolean resposta = false;
		if(memoria.getProcessosNaMemoria().contains(p)){
			resposta = true;
		}
		return resposta;
	}
	
	public void setProcessoEscolhidoParaExecucao(Processo processoEscolhido) {
		this.processo = processoEscolhido;
	}  

	public void run() {
		if(verificaSeProcessoEstaNaMemoria(processo)){
			timer.setTemporizador(0);
			if(processo.getBurst()>timeQuantum){
			timer.iniciarTemporizadorAte(timeQuantum);
			wait();
			processo.setBurst(processo.getBurst()-timeQuantum);
			notify();
			
			}else{
			timer.iniciarTemporizadorAte(processo.getBurst());
			wait();
			processo.setBurst(processo.getBurst()-processo.getBurst()); // é pra dar 0
			notify();
			}
		}
	}

	 
}