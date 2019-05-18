public class Despachante implements Runnable {

	private Processo processo;
	private Memoria memoria;
	private Timer timer;
	private Swapper swapper;
	private int timeQuantum;
	private FilaProntosCompartilhada filaProntos;

	public Despachante(Timer t, Memoria memoria, Swapper s, FilaProntosCompartilhada f) {
		this.timer = t;
		this.memoria = memoria;
		this.swapper = s;
		this.filaProntos = f;
	}

	public void setTimeQuantumRR(int tq) {
		this.timeQuantum = tq;
	}

	private synchronized boolean verificaSeProcessoEstaNaMemoria(Processo p) {
		boolean resposta = false;
		try {
			this.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (memoria.contemProcessoX(p)) {
			resposta = true;
			System.out.println("Despachante percebe que o processo " + p.getIdProcesso() + " está na memória.");
		}
		return resposta;
	}

	public void setProcessoEscolhidoParaExecucao(Processo processoEscolhido) {
		this.processo = processoEscolhido;
	}

	private synchronized void despachaProcesso() {
		if (verificaSeProcessoEstaNaMemoria(this.processo)) {
			if (processo.getBurst() >= timeQuantum) {
				timer.iniciarTemporizadorAte(timeQuantum);
				System.out.println("Despachante reiniciou o Timer com " + timeQuantum + " e liberou a CPU ao processo "
						+ processo.getIdProcesso());
				while (!timer.getTerminouTimeQuantum()) {
					synchronized (this) {
						try {
							this.wait();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				processo.setBurst(processo.getBurst() - timeQuantum);
				filaProntos.addFilaProntos(processo);
			} else if (processo.getBurst() < timeQuantum) {
				timer.iniciarTemporizadorAte(processo.getBurst());
				System.out.println("Despachante reiniciou o Timer com " + timeQuantum + " e liberou a CPU ao processo "
						+ processo.getIdProcesso());
			}
			while (!timer.getTerminouTimeQuantum()) {
				synchronized (this) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			System.out.println("Processo " + processo.getIdProcesso() + " completou sua execução.");
			processo.setBurst(processo.getBurst() - processo.getBurst()); // é pra dar 0
		} else {
			System.out.println("Despachante percebe que o processo " + processo.getIdProcesso()
					+ " está no disco e solicita que o Swapper traga" + processo.getIdProcesso() + "à memória");
			swapper.setProcesso(processo);
			swapper.trazProcessoParaMemoria(processo);
			while (!swapper.getConseguiuColocarProcessoNaMemoria()) {
				synchronized (this) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			despachaProcesso();
		}
	}

	public void run() {
		while (processo == null) {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		despachaProcesso();
	}
}