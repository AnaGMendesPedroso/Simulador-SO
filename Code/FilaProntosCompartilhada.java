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
import java.util.Vector;

public class FilaProntosCompartilhada {
	private Vector<Processo> filaProntos;

	public FilaProntosCompartilhada() {
		this.filaProntos = new Vector<Processo>();
	}

	public synchronized void addFilaProntos(Processo p) {
		this.filaProntos.add(p);
	}

	public synchronized Vector<Processo> getFilaProntos() {
		return this.filaProntos;
	}

	public synchronized Processo getPrimeiroProcesso() {
		return filaProntos.firstElement();
	}

	public synchronized void remove(Processo p) {
		this.filaProntos.remove(p);
	}

	public synchronized boolean isEmpty() {
		return this.filaProntos.isEmpty();
	}

	public void printaFilaProntos() {
		System.out.print("\nFILA PRONTOS:");
		filaProntos.forEach((Processo pa) -> System.out.printf("\nPID: %d\ntamanho:%d\nquando chegou:%d\nburst:%d\n",
				pa.getIdProcesso(), pa.getTamProcesso(), pa.getChegada(), pa.getBurst()));
		System.out.println();
	}
}
