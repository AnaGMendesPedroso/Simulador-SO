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
public class EscalonadorFirstComeFirstServed implements Runnable {

	private Memoria mem;
	private FilaProntosCompartilhada filaProntos;
	private FilaEntradaCompartilhada filaEntrada;
	private EscalonadorRoundRobin rr;
	private Swapper swapper;
	private Processo p = null;
	private boolean terminouDeEscalonar = false;
	public boolean solicitaSwaper = false;

	public EscalonadorFirstComeFirstServed(FilaEntradaCompartilhada filaEntrada, FilaProntosCompartilhada filaProntos,
			Memoria mem, Swapper s, EscalonadorRoundRobin rr2) {
		this.filaEntrada = filaEntrada;
		this.filaProntos = filaProntos;
		this.mem = mem;
		this.swapper = s;
		this.rr = rr2;
	}
	public boolean getTerminouDeEscalonar() {
		return this.terminouDeEscalonar;
	}

	private synchronized void escalonaProcesso() {
		terminouDeEscalonar=false;
		synchronized (filaEntrada) {
			p = filaEntrada.pegaPrimeiroProcesso();
		}
		p.setEmUso(true);
		if (mem.getEspacoLivre() >= p.getTamProcesso()) {
			synchronized (filaEntrada) {
				filaEntrada.removeProcessoDaFilaDeEntrada(p);
			}
			System.out.printf("Escalonador FCFS de longo prazo escolheu e retirou o processo %d  da fila de entrada\n",
					p.getIdProcesso());
			solicitaSwaper=true;
			System.out.printf("Escalonador FCFS de longo prazo solicitou que o Swapper traga o processo %d à memoria\n",	p.getIdProcesso());
			swapper.setConseguiuColocarProcessoNaMemoria(false);
			synchronized (swapper) {
				swapper.notify();
				swapper.trazProcessoParaMemoria(false, p);
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
			synchronized (filaProntos) {
				filaProntos.addFilaProntos(p);
			}
			System.out.printf("Escalonador FCFS de longo prazo colocou o processo %d na fila de prontos\n",
						p.getIdProcesso());
			p.setEmUso(false);	
			swapper.setConseguiuColocarProcessoNaMemoria(false);
			solicitaSwaper = false;		
			terminouDeEscalonar=true;
			synchronized (rr) {
				rr.notify();
			}	
		} else {

			System.out.printf(
					"Escalonador FCFS de longo prazo não retirou o processo %d da fila de entrada porque não há espaço na memória!\nESPACO LIVRE MEMÓRIA:%d\n \t TAMPROCESSO:%d\n",
					p.getIdProcesso(), mem.getEspacoLivre(), p.getTamProcesso());
			solicitaSwaper=true;
			System.out.println("Escalonador FCFS de longo prazo solicitou que Swapper retire alguns processos da memória para colocar o processo "
				+ p.getIdProcesso());
				
			swapper.setTerminouDeLimpar(false);
			synchronized(swapper){
				swapper.notify();
				swapper.limpaMemoria(false,p.getTamProcesso());		
			}			
			while(!swapper.isTerminouDeLimpar()){
				synchronized (this) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}			
			}
			swapper.setTerminouDeLimpar(false);
			solicitaSwaper=false;
			escalonaProcesso();
		}
	}

	@Override
	public void run() {		
		while (filaEntrada.isEmpty()) {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		while(!filaEntrada.isEmpty()){
			terminouDeEscalonar = false;
			escalonaProcesso();
		}
		run();
	}
}