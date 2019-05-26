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
public class Despachante implements Runnable {

	private Memoria memoria;
	private Timer temporizador;
	private Swapper swapper;
	private int timeQuantum;
	private EscalonadorRoundRobin rr;
	private boolean conseguiuDespacharProcesso = false;
	public boolean solicitaSwaper = false;
	
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
		System.out.println("Despachante avisa Escalonador RR que processo terminou sua execução");
		synchronized (rr) {
			rr.notify();
		}
	}
	private synchronized boolean verificaSeProcessoEstaNaMemoria(Processo p) {
		boolean resposta = false;
		if (memoria.contemProcessoX(p)) {
			resposta = true;
			System.out.println("Despachante percebe que o processo " + p.getIdProcesso() + " está na memória.");
		}
		return resposta;
	}

	public synchronized void despachaProcesso(Processo processo) {
		conseguiuDespacharProcesso = false;
		if (verificaSeProcessoEstaNaMemoria(processo)) {
			if (processo.getBurst() >= timeQuantum) {
				boolean k = temporizador.isLivre();
				while (!k) {
					synchronized (this) {
						try {
							this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				temporizador.setLivre(false);
				temporizador.setP(processo);
			
				System.out.println("Despachante reiniciou o Timer com " + timeQuantum + " e liberou a CPU ao processo "+ processo.getIdProcesso());
						
				temporizador.simulaExecucao(timeQuantum);
				synchronized(temporizador){
					temporizador.notify();
				}
				while(!temporizador.terminou()){
					synchronized(this){
						try {
							this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				temporizador.setTerminou(false);
				temporizador.setLivre(true);			
				processo.setBurst(processo.getBurst() - timeQuantum);
				conseguiuDespacharProcesso = true;
				notificaRR();
			}else if (processo.getBurst() < timeQuantum) {
				boolean k = temporizador.isLivre();
				while (!k) {
					synchronized (this) {
						try {
							this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				temporizador.setLivre(false);
				temporizador.setP(processo);
				System.out.println("Despachante reiniciou o Timer com " + processo.getBurst() + " e liberou a CPU ao processo "+ processo.getIdProcesso());
				temporizador.simulaExecucao(processo.getBurst());
				synchronized(temporizador){
					temporizador.notify();
				}
				while(!temporizador.terminou()){
					synchronized(this){
						try {
							this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				temporizador.setTerminou(false);
				temporizador.setLivre(true);			
				processo.setBurst(processo.getBurst() - processo.getBurst());
				conseguiuDespacharProcesso = true;
				notificaRR();
			}
			}else{
				System.out.println("Despachante percebe que o processo " + processo.getIdProcesso()
						+ " está no disco e solicita que o Swapper traga " + processo.getIdProcesso() + " à memória");
			
				solicitaSwaper = true;
				swapper.setConseguiuColocarProcessoNaMemoria(false);
				synchronized(swapper){
					swapper.notify();
					swapper.trazProcessoParaMemoria(true, processo);
				}
				while(!swapper.getConseguiuColocarProcessoNaMemoria()){					
					synchronized (this) {
						try {
							this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}			
				System.out.printf("Despachante é avisado pelo Swapper que o processo %d está na memória\n",processo.getIdProcesso());
				swapper.setConseguiuColocarProcessoNaMemoria(false);
				solicitaSwaper = false;
				despachaProcesso(processo);
		}
	}

	public void run() {
		while (!rr.getTemProcesso()) {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean getConseguiuDespacharProcesso() {
		return conseguiuDespacharProcesso;
	}

	public void setConseguiuDespacharProcesso(boolean conseguiuDespacharProcesso) {
		this.conseguiuDespacharProcesso = conseguiuDespacharProcesso;
	}
}