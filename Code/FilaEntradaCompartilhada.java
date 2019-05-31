/*Implementado por Ana Gabrielly Mendes Pedroso
	Estrutura: Todo o trabalho foi desenvoldido em Java. Todos os códigos necessários para a execução
		 estão na pasta Simulador-SO/Code/. 
	Para execução: 
	-entre na pasta Code:
		cd Simulador-SO/Code
	-compile os arquivos .java e execute a classe Executor :
		javac *.java && java Executor
		
	(considere executar esse último passo 2 vezes)
	A saída obtida para a entrada 
	1000 3 2 1 500 5 15 0 700 1 5 2 600 2 10
	 está no arquivo saida.txt na pasta Code/
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
		//Date horaCorrente = new Date();
		//String tempoCorrente = new SimpleDateFormat("HH:mm:ss").format(horaCorrente);
		//System.out.println(tempoCorrente+" : Elemento removido PID:"+x.getIdProcesso());
		filaEntrada.remove(x);
	}

	public synchronized void colocaNaFilaDeEntrada(Processo p) {
		filaEntrada.add(p);
	}

	public synchronized int getTamanho() {
		return filaEntrada.size();
	}
}