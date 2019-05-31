import java.text.SimpleDateFormat;
import java.util.Date;

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

public class Despachante implements Runnable {

	private Memoria memoria;
	private Timer temporizador;
	private Swapper swapper;
	private int timeQuantum;
	private EscalonadorRoundRobin rr;
	private boolean conseguiuDespacharProcesso = false;
	public boolean solicitaSwaper = false;
	boolean isDead = false;
	private Processo processo = null;
	
	public Despachante(Timer t, Memoria memoria, Swapper s) {
		this.temporizador = t;
		this.memoria = memoria;
		this.swapper = s;
	}

	public void setTimeQuantumRR(int tq) {
		this.timeQuantum = tq;
	}

	public void setRR(EscalonadorRoundRobin rr2) {
		this.rr = rr2;
	}

	private void notificaRR(){
		Date horaCorrente = new Date();
		String tempoCorrente = new SimpleDateFormat("HH:mm:ss").format(horaCorrente);
		System.out.println(tempoCorrente+" : Despachante avisa Escalonador RR que processo terminou sua execução");
		synchronized (rr) {
			rr.notify();
		}
	}
	private synchronized boolean verificaSeProcessoEstaNaMemoria(Processo p) {
		boolean resposta = false;
		if (memoria.contemProcessoX(p)) {
			resposta = true;
			Date horaCorrente = new Date();
			String tempoCorrente = new SimpleDateFormat("HH:mm:ss").format(horaCorrente);
			System.out.println(tempoCorrente+" : Despachante percebe que o processo " + p.getIdProcesso() + " está na memória.");
		}
		return resposta;
	}

	public synchronized void despachaProcesso() {
		conseguiuDespacharProcesso = false;
		int tempo = 0;
		if (verificaSeProcessoEstaNaMemoria(processo)) {
			temporizador.setP(processo);
			if (processo.getBurst() >= timeQuantum) {
				tempo = timeQuantum;
			}else{
				tempo = processo.getBurst();
			}
			temporizador.setTempo(tempo);
			
			synchronized(temporizador){
				temporizador.notify();
			}
			Date horaCorrente = new Date();
			String tempoCorrente = new SimpleDateFormat("HH:mm:ss").format(horaCorrente);
			System.out.println(tempoCorrente+" : Despachante reiniciou o Timer com " + tempo + " e liberou a CPU ao processo "+ processo.getIdProcesso());
			synchronized(this){
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			processo.setBurst(processo.getBurst() - tempo);
			memoria.atualizaBurst(processo);
			conseguiuDespacharProcesso = true;
			notificaRR();
		}else{
			Date horaCorrente = new Date();
			String tempoCorrente = new SimpleDateFormat("HH:mm:ss").format(horaCorrente);
			System.out.println(tempoCorrente+" : Despachante percebe que o processo " + processo.getIdProcesso()
						+ " está no disco e solicita que o Swapper traga " + processo.getIdProcesso() + " à memória");
			
				
				swapper.setConseguiuColocarProcessoNaMemoria(false);
				swapper.setpDespachante(processo);
				solicitaSwaper = true;

				while(!swapper.getConseguiuColocarProcessoNaMemoria()){					
					synchronized (this) {
						try {
							this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}			
				System.out.printf("\n"+tempoCorrente+" : Despachante é avisado pelo Swapper que o processo %d está na memória\n",processo.getIdProcesso());
				swapper.setConseguiuColocarProcessoNaMemoria(false);
				solicitaSwaper = false;
				despachaProcesso();
		}
	}

	public void run() {
		while (!rr.isDead()) {
			if(processo!= null){
				despachaProcesso();
				processo=null;
			}
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		isDead = true;
		//System.out.println("============================ DESPACHANTE MORREU ============================");
		synchronized(temporizador){
			temporizador.notify();
		}
	}

	public boolean isDead(){
		return isDead;
	}
	public boolean getConseguiuDespacharProcesso() {
		return conseguiuDespacharProcesso;
	}

	public void setConseguiuDespacharProcesso(boolean conseguiuDespacharProcesso) {
		this.conseguiuDespacharProcesso = conseguiuDespacharProcesso;
	}

	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo2) {
		this.processo = processo2;
	}
}