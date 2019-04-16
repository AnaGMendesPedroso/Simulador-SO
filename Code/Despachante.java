public class Despachante implements Runnable {

	private Processo processo;
	private Memoria memoria;
	private Timer timer;
	private int timeQuantum;

	public Despachante() {
	}

	public void setTimeQuantumRR(int tq) {
		this.timeQuantum = tq;
	}

	private boolean verificaSeProcessoEstaNaMemoria(Processo p) {
		boolean resposta = false;
		if (memoria.getProcessosNaMemoria().contains(p)) {
			resposta = true;
			System.out.println("Despachante percebe que o processo " + p.getIdProcesso() + " está na memória.");
		}
		return resposta;
	}

	public void setProcessoEscolhidoParaExecucao(Processo processoEscolhido) {
		this.processo = processoEscolhido;
	}

	public void run() {
		if (verificaSeProcessoEstaNaMemoria(processo) && processo.getBurst() >= timeQuantum) {
			timer.iniciarTemporizadorAte(timeQuantum);
			System.out.println("Despachante reiniciou o Timer com " + timeQuantum + " e liberou a CPU ao processo "
					+ processo.getIdProcesso());

			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			processo.setBurst(processo.getBurst() - timeQuantum);
			notify();

		}
		else if (verificaSeProcessoEstaNaMemoria(processo) && processo.getBurst() < timeQuantum) {
			timer.iniciarTemporizadorAte(processo.getBurst());
			
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			processo.setBurst(processo.getBurst()-processo.getBurst()); // é pra dar 0
			notify();
		}else{
			System.out.println("Despachante percebe que o processo "
								+processo.getIdProcesso()+
								" está no disco e solicita que o Swapper traga"
								+processo.getIdProcesso()+
								"à memória");
		}
	}
}