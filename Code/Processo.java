import java.util.Comparator;

public class Processo extends Thread{
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
	public void setIdProcesso(int idProcesso) {
		this.idProcesso = idProcesso;
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
	public void setChegada(int chegada) {
		this.chegada = chegada;
	}
	public int getBurst() {
		return burst;
	}
	public void setBurst(int burst) {
		this.burst = burst;
	}

	public void run() {
		System.out.printf("Criador de processos criou o processo %d e o colocou na fila de entrada \n", idProcesso);
	}
	
	

}
