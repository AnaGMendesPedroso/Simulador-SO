import java.util.Vector;

public class EscalonadorRoundRobin implements Runnable {
	private final int timeQuantum;
	private Despachante despachante;
	private Processo processoEscolhido;
	FilaProntosCompartilhada filaProntos;

	public EscalonadorRoundRobin(int tq, FilaProntosCompartilhada filaProntos, Despachante d) {
		this.timeQuantum = tq;
		this.filaProntos = filaProntos;
		this.despachante = d;
	}

	public int getTimeQuantum() {
		return timeQuantum;
	}

	private synchronized void escalonaProcessoRR() {
		processoEscolhido = filaProntos.getPrimeiroProcesso();
		despachante.setProcessoEscolhidoParaExecucao(processoEscolhido);
		despachante.setTimeQuantumRR(timeQuantum);
		filaProntos.remove(processoEscolhido);
		System.out.println("Escalonador Round-Robin de CPU escolheu o processo " + processoEscolhido.getIdProcesso()
				+ ", retirou-o da fila de prontos e o encaminhou ao Despachante.");
	}

	@Override
	public void run() {
		while (filaProntos.isEmpty()) {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		escalonaProcessoRR();
		synchronized (despachante) {
			despachante.notify();
		}
	}
}
