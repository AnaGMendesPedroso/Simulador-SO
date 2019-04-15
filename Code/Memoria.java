import java.util.Vector;

public class Memoria implements Runnable{
	private final int tamanho;
	private Vector<Processo> processosNaMemoria;
	private int espacoLivre;

	public Memoria(int tamanho) {
		this.tamanho = tamanho;
		espacoLivre = tamanho;
	}

	public int getTamanho() {
		return tamanho;
	}

	public int getEspacoLivre(){
		return espacoLivre;
	}
	
	public Vector getProcessosNaMemoria(){
		return processosNaMemoria;
	}
	public synchronized void adicionaProcessoNaMemoria(Processo p){
		int tamanhoDoProcesso = p.getTamProcesso();
		espacoLivre -= tamanhoDoProcesso;
		processosNaMemoria.add(p);
	}
	public synchronized void retiraProcessoDaMemoria(Processo p ){
		processosNaMemoria.remove(p);
		espacoLivre += p.getTamProcesso();
	}

	@Override
	public void run() {

	}
}
