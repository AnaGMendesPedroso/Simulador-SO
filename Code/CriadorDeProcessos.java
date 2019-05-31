
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class CriadorDeProcessos implements Runnable {

	private EscalonadorFirstComeFirstServed fcfs;
	private FilaEntradaCompartilhada filaEntrada;
	private Vector<Processo> filaProcessosOrdenados = new Vector<Processo>();
	private boolean isDead = false;
	private final long inicio;
	

	public CriadorDeProcessos(long l, FilaEntradaCompartilhada filaEntrada, Vector<Processo> fprocessos) {
		this.inicio = l;
		this.filaEntrada = filaEntrada;
		this.filaProcessosOrdenados = fprocessos;
	}

	private synchronized void iniciaProcesso(Processo pa) {

		long tempoAtual = (long)System.currentTimeMillis();
		//System.out.println("Atual: "+ tempoAtual);
		while((tempoAtual - inicio) < ((pa.getChegada()*1000))){
			tempoAtual = (long)System.currentTimeMillis();
		}
		synchronized (filaEntrada) {
			filaEntrada.colocaNaFilaDeEntrada(pa);
		}
		Date horaCorrente = new Date();
		String tempoCorrente = new SimpleDateFormat("HH:mm:ss").format(horaCorrente);
		System.out.println(tempoCorrente+ " : Criador de processos criou o processo " + pa.getIdProcesso()
				+ " e o colocou na fila de entrada. ");
		filaProcessosOrdenados.remove(pa);
		notificaFCFS();
	}

	public void setFCFS(EscalonadorFirstComeFirstServed fcfs2) {
		this.fcfs = fcfs2;
	}

	private void notificaFCFS() {
		while (!filaEntrada.isEmpty()) {
			synchronized (fcfs) {
				fcfs.notify();
			}
		}
	}

	public void run() {

		while (filaProcessosOrdenados.size() > 0) {
			Processo pa = filaProcessosOrdenados.firstElement();
			iniciaProcesso(pa);
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		isDead = true;
		//System.out.println("============================ CRIADOR MORREU ============================");
	}

	public boolean isDead() {
		return isDead;
	}
}