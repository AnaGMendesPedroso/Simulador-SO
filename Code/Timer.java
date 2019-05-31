import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;

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

public class Timer implements Runnable {
	private Despachante despachante;
	private Processo p = null;
	private boolean terminou = false;
	private int tempo = 0;
	boolean isDead = false;

	public void setDespachante(Despachante d){
		this.despachante = d;
	}

	public boolean terminou() {
		return terminou;
	}

	public void setTerminou(boolean terminou) {
		this.terminou = terminou;
	}
	public void setP(Processo p2) {
		this.p = p2;
	}

	public synchronized void simulaExecucao() {
		try {
			Thread.currentThread().sleep(tempo*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Date horaCorrente = new Date();
		String tempoCorrente = new SimpleDateFormat("HH:mm:ss").format(horaCorrente);
		System.out.println(tempoCorrente+" : Timer informa ao Despachante que o processo " + p.getIdProcesso()
				+ " atualmente em execução precisa ser retirado da CPU");
		terminou=true;
		p.setEmUso(false);
		despachante.setProcesso(p);
		synchronized(despachante){
			despachante.notify();
		}
	}
	@Override
	public void run() {
		while(!despachante.isDead()){
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(p!= null){
				simulaExecucao();
				p = null;
			}
		}
		isDead = true;
		//System.out.println("============================ TIMER MORREU ============================");
		
	}

	/**
	 * @param tempo the tempo to set
	 */
	public void setTempo(int tempo) {
		this.tempo = tempo;
	}
}