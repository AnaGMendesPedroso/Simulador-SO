import java.util.Vector;

public class EscalonadorRoundRobin implements Runnable {
	private final int timeQuantum;
	private Despachante despachante;
	private Timer timer;
	private Processo processoEscolhido;
	private FilaProntosCompartilhada filaProntos;
	private Vector<Processo> processos;
	

	public EscalonadorRoundRobin(int tq, FilaProntosCompartilhada filaProntos, Despachante d, Timer t,  Vector<Processo> p) {
		this.timeQuantum = tq;
		this.filaProntos = filaProntos;
		this.despachante = d;
		this.timer = t;
		this.processos = p;
	}

	public int getTimeQuantum() {
		return timeQuantum;
	}
	private int calculaBurstTotal(){
		int burstTotal = 0;
		for(int i = 0; i< processos.size(); i++){
			burstTotal += processos.get(i).getBurst();
		}
		return burstTotal;
	}

	private synchronized void escalonaProcessoRR() {
		processoEscolhido = filaProntos.getPrimeiroProcesso();		
		despachante.setTimeQuantumRR(timeQuantum);
		filaProntos.remove(processoEscolhido);
		despachante.setProcessoEscolhidoParaExecucao(processoEscolhido);
		System.out.println("Escalonador Round-Robin de CPU escolheu o processo " + processoEscolhido.getIdProcesso()
				+ ", retirou-o da fila de prontos e o encaminhou ao Despachante.");
		synchronized(despachante){
			despachante.notify();
		}
		while (!despachante.getTerminouProcessamento()) {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if(processoEscolhido.getBurst()>0){
			filaProntos.addFilaProntos(processoEscolhido);
			System.out.println("Escalonador Round-Robin de CPU adicionou o processo "+processoEscolhido.getIdProcesso()+" a fila de prontos após seu processamento");		
		}		
	}

	@Override
	public void run() {
		while(calculaBurstTotal()>0){
			while (filaProntos.isEmpty()) {
				synchronized (this) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			while(!filaProntos.isEmpty()){
				escalonaProcessoRR();
				timer.clock();
			}
			calculaBurstTotal();
		}
	}
}
