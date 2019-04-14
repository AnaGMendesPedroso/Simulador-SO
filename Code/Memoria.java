import java.util.Vector;

public class Memoria {
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
	public synchronized void adicionaProcessoNaMemoria( Processo p){
		int tamanhoDoProcesso = p.getTamProcesso();
		espacoLivre -= tamanhoDoProcesso;
		processosNaMemoria.add(p);
	}
	public synchronized void retiraProcessoDaMemoria(Processo p ){
		processosNaMemoria.remove(p);
		espacoLivre += p.getTamProcesso();
	}
}
