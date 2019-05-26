/*Implementado por Ana Gabrielly Mendes Pedroso
	Estrutura: Todo o trabalho foi desenvoldido em Java. Todos os códigos necessários para a execução
		 estão na pasta Simulador-SO/Code/. 
	Para execução: 
	-entre na pasta Code:
		cd Simulador-SO/Code
	-compile os arquivos .java:
		javac *.java
	-execute a classe Executor:
		java Executor
		
	(considere executar esse último passo 2 vezes)
	A saída obtida para a entrada 1000 3 2 1 500 5 15 0 700 1 5 2 600 2 10 está no arquivo saida.txt na pasta Code/
*/
public class Processo {
	private int idProcesso;
	private int tamProcesso;
	private int chegada;
	private int burst;
	private boolean emUso = false;
	private int indexMemoria = -1;

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
			try {
				this.finalize();
			} catch (Throwable e) {
				e.printStackTrace();
			}

		}
	}

	public synchronized boolean isEmUso() {
		return emUso;
	}

	public synchronized void setEmUso(boolean estaEmUso) {
		this.emUso = estaEmUso;
	}

	public int getIndexMemoria() {
		return indexMemoria;
	}

	public void setIndexMemoria(int indexMemoria) {
		this.indexMemoria = indexMemoria;
	}
	
}
