public class Processo implements Runnable{
	private int idProcesso;
	private int tamProcesso;
	private int chegada;
	private int burst;	

	public Processo(int idProcesso, int tamProcesso, int chegada, int burst) {
			this.idProcesso = idProcesso;
			this.tamProcesso = tamProcesso;
			this.chegada = chegada;
			this.burst = burst;
	
	}
	public int getIdProcesso() {
		return idProcesso;
	}
	public int getTamProcesso() {
		return tamProcesso;
	}
	public void setTamProcesso(int tamProcesso) {
		this.tamProcesso = tamProcesso;
	}
	public int getChegada() {
		return chegada;
	}
	public int getBurst() {
		return burst;
	}
	public void setBurst(int burst) {
		this.burst = burst;
	}

	public void run() {
		System.out.printf("O processo %d foi executado\n", this.idProcesso);
	}
	
	

}
