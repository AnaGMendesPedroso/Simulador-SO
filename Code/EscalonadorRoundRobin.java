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
public class EscalonadorRoundRobin implements Runnable {
	private final int timeQuantum;
	private Despachante despachante;
	private Processo processoEscolhido;
	private FilaProntosCompartilhada filaProntos;
	private EscalonadorFirstComeFirstServed fcfs;
	private boolean temProcesso = false;
	private boolean escolheuProcesso = false;

	public EscalonadorRoundRobin(int tq, FilaProntosCompartilhada filaProntos,
			Despachante d) {
		this.timeQuantum = tq;
		this.filaProntos = filaProntos;
		this.despachante = d;
	}

	public void setFCFS(EscalonadorFirstComeFirstServed f) {
		this.fcfs = f;
	}

	private synchronized void escalonaProcessoRR() {

		processoEscolhido = filaProntos.getPrimeiroProcesso();
		processoEscolhido.setEmUso(true);
		escolheuProcesso = true;
		despachante.setTimeQuantumRR(timeQuantum);
		filaProntos.remove(processoEscolhido);
		System.out.println("Escalonador Round-Robin de CPU escolheu o processo " + processoEscolhido.getIdProcesso()
				+ ", retirou-o da fila de prontos e o encaminhou ao Despachante.");

		synchronized (despachante) {
			despachante.notify();
		}
		despachante.setConseguiuDespacharProcesso(false);
		despachante.despachaProcesso(processoEscolhido);
		while (!despachante.getConseguiuDespacharProcesso()) {			
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
			System.out.println("Escalonador Round-Robin de CPU adicionou o processo "+processoEscolhido.getIdProcesso()+" a fila de prontos após seu processamento BURST: "+processoEscolhido.getBurst());		
		}
		processoEscolhido.setEmUso(false);
		temProcesso=false;
		processoEscolhido=null;		
	}

	@Override
	public void run() {	
		while(!fcfs.getTerminouDeEscalonar()){
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace(); 
				}
			}
			temProcesso=true;
			escalonaProcessoRR();
			escolheuProcesso=false;
		}
		while(!filaProntos.isEmpty()){
			temProcesso=true;
			escalonaProcessoRR();
			escolheuProcesso=false;
		}
		temProcesso=false;
	}

	public synchronized boolean getTemProcesso() {
		return temProcesso;
	}

	public synchronized void setTemProcesso(boolean temProcesso) {
		this.temProcesso = temProcesso;
	}

	public boolean getEscolheuProcesso() {
		return escolheuProcesso;
	}
}
