public class Despachante implements Runnable {

	private Processo processo;
	private Memoria memoria;
	private Timer timer;
	private Swapper swapper;
	private int timeQuantum;
	private EscalonadorRoundRobin rr;
	private boolean terminouProcessamento = false;

	public Despachante(Timer t, Memoria memoria, Swapper s) {
		this.timer = t;
		this.memoria = memoria;
		this.swapper = s;
	}
	public void setTimeQuantumRR(int tq) {
		this.timeQuantum = tq;
	}
	public boolean getTerminouProcessamento(){
		return this.terminouProcessamento;
	}
	public void setRR(EscalonadorRoundRobin rr2) {
		this.rr = rr2;
	}
	public void setProcessoEscolhidoParaExecucao(Processo processoEscolhido) {
		this.processo = processoEscolhido;
	}

	private synchronized boolean verificaSeProcessoEstaNaMemoria(Processo p) {
		boolean resposta = false;
		if (memoria.contemProcessoX(p)) {
			resposta = true;
			System.out.println("Despachante percebe que o processo " + p.getIdProcesso() + " está na memória.");
		}
		return resposta;
	}

	public synchronized void despachaProcesso() {
		if (verificaSeProcessoEstaNaMemoria(processo)) {
			if (processo.getBurst() >= timeQuantum) {
				terminouProcessamento=false;
				
				System.out.println("Despachante reiniciou o Timer com " + timeQuantum + " e liberou a CPU ao processo "
						+ processo.getIdProcesso());
				
						timer.iniciarTemporizadorAte(timeQuantum,processo.getIdProcesso());
				
						while (!timer.getTerminouProcessamento()) {
					synchronized (this) {
						try {
							System.out.println("despachante esperando timer...");
							this.wait();
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}				
				}
				timer.setTerminouProcessamento(false);
				processo.setBurst(processo.getBurst() - timeQuantum);
				terminouProcessamento=false;
				synchronized (rr) {
					rr.notify();
				}
			} else if (processo.getBurst() < timeQuantum) {
				terminouProcessamento=false;
				System.out.println("Despachante reiniciou o Timer com " + timeQuantum + " e liberou a CPU ao processo "
						+ processo.getIdProcesso());
				timer.iniciarTemporizadorAte(processo.getBurst(),processo.getIdProcesso());
				while (!timer.getTerminouProcessamento()) {
					synchronized (this) {
						try {
							this.wait();
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}
				timer.setTerminouProcessamento(false);
				processo.setBurst(processo.getBurst() - processo.getBurst());
				terminouProcessamento=false;
				synchronized (rr) {
					rr.notify();
				}
			}
		} else {
			System.out.println("Despachante percebe que o processo " + processo.getIdProcesso()
					+ " está no disco e solicita que o Swapper traga " + processo.getIdProcesso() + " à memória");
			swapper.setProcesso(processo);
			swapper.setQuemPediu(true);
			swapper.trazProcessoParaMemoria(processo);
			while (!swapper.getConseguiuColocarProcessoNaMemoria()) {
				synchronized (this) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			System.out.println(
					"Swapper avisa ao Despachante que o processo " + processo.getIdProcesso() + "está na memória");
			
			System.out.printf("Despachante é avisado pelo Swapper que o processo %d está na memória\n",
					processo.getIdProcesso());
			despachaProcesso();
		}
	}

	public void run() {
		while (processo==null) {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		//while()
		despachaProcesso();
		timer.clock();
	}
}