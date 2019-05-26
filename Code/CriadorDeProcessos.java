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

public class CriadorDeProcessos implements Runnable {

	private Executor executor;
	private EscalonadorFirstComeFirstServed fcfs;
	private FilaEntradaCompartilhada filaEntrada;
	private Vector<Processo> filaProcessosOrdenados = new Vector<Processo>();

	public CriadorDeProcessos(Executor e, FilaEntradaCompartilhada filaEntrada, Vector<Processo> fprocessos) {
		this.executor = e;
		this.filaEntrada = filaEntrada;
		this.filaProcessosOrdenados = fprocessos;
	}

	private synchronized void iniciaProcesso(Processo pa) {
		while (executor.getTempoAtualTotal() != pa.getChegada()) {
			synchronized(executor){
				executor.setTempoAtualTotal();
			}
		}
		synchronized (filaEntrada) {
			filaEntrada.colocaNaFilaDeEntrada(pa);
		}
		System.out.println("Criador de processos criou o processo " + pa.getIdProcesso() + " e o colocou na fila de entrada. ");
		filaProcessosOrdenados.remove(pa);
		notificaFCFS();
	}
	public void setFCFS(EscalonadorFirstComeFirstServed fcfs2) {
		this.fcfs = fcfs2;
	}
	private void notificaFCFS(){
		while(!filaEntrada.isEmpty()){
			synchronized(fcfs){
				fcfs.notify();
			}
		}
	}

	public void run() {
		while (filaProcessosOrdenados.size() > 0) {
			Processo pa = filaProcessosOrdenados.firstElement();
			iniciaProcesso(pa);
		}
	}
}