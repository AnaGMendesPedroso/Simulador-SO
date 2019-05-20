public class Processo {
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

	public int getChegada() {
		return chegada;
	}

	public int getBurst() {
		return burst;
	}

	public void setBurst(int burst) {
		this.burst = burst;
		if (this.burst > 0) {
			System.out.printf("O processo %d foi executado\n", this.idProcesso);
		} else {
			System.out.printf("O processo %d terminou sua execução\n", this.idProcesso);
		}
	}
}
