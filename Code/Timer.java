public class Timer implements Runnable {
	private int tempoCpu;
	private CriadorDeProcessos criador;
	private Despachante despachante;
	private boolean terminouProcessamento = false;	

	public Timer() {
		this.tempoCpu = 0;
	}

	public synchronized void clock() {
		tempoCpu++;
		synchronized (criador) {
			criador.notify();
		}
	}

	public int getTempoCpu() {
		return this.tempoCpu;
	}

	public void iniciarTemporizadorAte(int temporizador, int id) {	
		int ateF = tempoCpu + temporizador;
		while (tempoCpu < ateF) {
			//System.out.println("Entrou while tempoCpu: "+ tempoCpu+ "até: "+ateF);
			clock();
		}
		System.out.println("Timer informa ao Despachante que o processo " + id
		+ " atualmente em execução precisa ser retirado da CPU");
		terminouProcessamento=true;
		synchronized (despachante) {
			despachante.notify();
		}
	}

	public void setDespachante(Despachante d) {
		this.despachante = d;
	}

	public void setCriador(CriadorDeProcessos c) {
		this.criador = c;
	}
	public boolean getTerminouProcessamento() {
		return terminouProcessamento;
	}

	public void setTerminouProcessamento(boolean terminouProcessamento) {
		this.terminouProcessamento = terminouProcessamento;
	}
	@Override
	public void run() {
		clock();
	}
}