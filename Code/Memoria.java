import java.util.Vector;

public class Memoria{
	private final int tamanho;
	private Vector<Processo> processosNaMemoria = new Vector<Processo>();
	private int espacoLivre;

	public Memoria(int tamanho) {
		this.tamanho = tamanho;
		espacoLivre = tamanho;
	}

	public synchronized int getTamanho() {
		return tamanho;
	}

	public synchronized int getEspacoLivre(){
		return espacoLivre;
	}
	
	public synchronized boolean contemProcessoX(Processo x){
		return this.processosNaMemoria.contains(x);
	}

	public synchronized void adicionaProcessoNaMemoria(Processo p){
		int tamanhoDoProcesso = p.getTamProcesso();
		espacoLivre -= tamanhoDoProcesso;
		processosNaMemoria.add(p);
	}

	public synchronized void retiraProcessoDaMemoria(Processo p ) throws InterruptedException {
		processosNaMemoria.remove(p);
		espacoLivre += p.getTamProcesso();
	}

	public void printaProcessosNaMemoria(){
		System.out.println("\nProcessos na memoria");
		if(!processosNaMemoria.isEmpty()){
			processosNaMemoria.forEach((Processo p)-> System.out.println("\nPID:"));
		}else{
			System.out.println("Memória vazia!");
		}
	}
}