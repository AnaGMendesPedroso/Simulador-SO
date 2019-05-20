import java.util.Vector;

public class Memoria {
	private final int tamanho;
	private Vector<Processo> processosNaMemoria = new Vector<Processo>();
	private int espacoLivre;
	private Processo ultimoProcessoAdicionado;

	public Memoria(int tamanho) {
		this.tamanho = tamanho;
		espacoLivre = tamanho;
	}

	public synchronized int getTamanho() {
		return tamanho;
	}

	public synchronized int getEspacoLivre() {
		return espacoLivre;
	}

	public synchronized boolean contemProcessoX(Processo x) {
		return this.processosNaMemoria.contains(x);
	}

	public synchronized void adicionaProcessoNaMemoria(Processo p) {
		int tamanhoDoProcesso = p.getTamProcesso();
		espacoLivre -= tamanhoDoProcesso;
		ultimoProcessoAdicionado= p;
		processosNaMemoria.add(p);
	}

	public void printaProcessosNaMemoria() {
		System.out.println("\nProcessos na memoria");
		if (!processosNaMemoria.isEmpty()) {
			processosNaMemoria.forEach((Processo p) -> System.out.println("\nPID:"));
		} else {
			System.out.println("Memória vazia!");
		}
	}

	public synchronized Processo retiraProcessoDaMemoria() {
		Processo p=null;
		if(processosNaMemoria.firstElement().equals(ultimoProcessoAdicionado)){
			p=processosNaMemoria.lastElement();
			processosNaMemoria.remove(p);
			espacoLivre += p.getTamProcesso();
		}else{
			p = processosNaMemoria.firstElement();
			processosNaMemoria.remove(p);
			espacoLivre += p.getTamProcesso();
		}
		
		return p;
	}
}