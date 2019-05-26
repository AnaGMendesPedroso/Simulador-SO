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

public class FilaEntradaCompartilhada {
	private Vector<Processo> filaEntrada = new Vector<Processo>();

	public FilaEntradaCompartilhada() {
	}

	public synchronized boolean isEmpty() {
		return this.filaEntrada.isEmpty();
	}

	public synchronized Processo pegaPrimeiroProcesso() {
		return filaEntrada.firstElement();
	}

	public synchronized void removeProcessoDaFilaDeEntrada(Processo x) {
		// System.out.println("Elemento removido PID:"+x.getIdProcesso());
		filaEntrada.remove(x);
	}

	public synchronized void colocaNaFilaDeEntrada(Processo p) {
		filaEntrada.add(p);
	}

	public synchronized int getTamanho() {
		return filaEntrada.size();
	}

	public void printaFilaEntrada() {
		System.out.print("\nFILA ENTRADA:");
		filaEntrada.forEach((Processo pa) -> System.out.printf("\nPID: %d\ntamanho:%d\nquando chegou:%d\nburst:%d\n",
				pa.getIdProcesso(), pa.getTamProcesso(), pa.getChegada(), pa.getBurst()));
		System.out.println();
	}
}