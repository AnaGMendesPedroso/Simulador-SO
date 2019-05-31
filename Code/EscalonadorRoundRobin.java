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
public class EscalonadorRoundRobin implements Runnable {
	private final int timeQuantum;
	private Despachante despachante;
	private Processo processoEscolhido;
	private FilaProntosCompartilhada filaProntos;
	private EscalonadorFirstComeFirstServed fcfs;
	private boolean isDead = false;

	public EscalonadorRoundRobin(int tq, FilaProntosCompartilhada filaProntos, Despachante d) {
		this.timeQuantum = tq;
		this.filaProntos = filaProntos;
		this.despachante = d;
		despachante.setTimeQuantumRR(timeQuantum);
	}

	public void setFCFS(EscalonadorFirstComeFirstServed f) {
		this.fcfs = f;
	}

	private void escolheProcessoParaEscalonarRR() {
		processoEscolhido = filaProntos.getPrimeiroProcesso();
		filaProntos.remove(processoEscolhido);
		Date horaCorrente = new Date();
		String tempoCorrente = new SimpleDateFormat("HH:mm:ss").format(horaCorrente);
		System.out.printf(tempoCorrente+" : Escalonador Round-Robin de CPU escolheu o processo %d, retirou-o da fila de prontos e o encaminhou ao Despachante.\n",
				processoEscolhido.getIdProcesso());
	}

	private synchronized void escalonaProcessoRR() {
		escolheProcessoParaEscalonarRR();
		despachante.setConseguiuDespacharProcesso(false);
		despachante.setProcesso(processoEscolhido);
		synchronized (despachante) {
			despachante.notify();
		}

		while (!despachante.getConseguiuDespacharProcesso()) {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if (processoEscolhido.getBurst() > 0) {
			filaProntos.addFilaProntos(processoEscolhido);
			Date horaCorrente = new Date();
			String tempoCorrente = new SimpleDateFormat("HH:mm:ss").format(horaCorrente);
			System.out.println(tempoCorrente+" : Escalonador Round-Robin de CPU adicionou o processo " + processoEscolhido.getIdProcesso()
							+ " a fila de prontos após seu processamento BURST: " + processoEscolhido.getBurst());
		}
		processoEscolhido = null;
	}

	@Override
	public void run() {
		while (!filaProntos.isEmpty() || !fcfs.isDead()) {
			if (!filaProntos.isEmpty()) {
				escalonaProcessoRR();
			}
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		isDead = true;
		//System.out.println("============================ RR MORREU ============================");
	}

	public boolean isDead() {
		return isDead;
	}
	public Processo getProcessoEscolhido() {
		return processoEscolhido;
	}
}
