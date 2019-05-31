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

public class EscalonadorFirstComeFirstServed implements Runnable {

	private FilaProntosCompartilhada filaProntos;
	private FilaEntradaCompartilhada filaEntrada;
	private Swapper swapper;
	private Processo p = null;
	private boolean terminouDeEscalonar = false;
	public boolean solicitaSwaper = false;
	private CriadorDeProcessos criador;
	private boolean isDead = false;

	public EscalonadorFirstComeFirstServed(CriadorDeProcessos c, FilaEntradaCompartilhada filaEntrada,
			FilaProntosCompartilhada filaProntos, Swapper s) {
		this.criador = c;
		this.filaEntrada = filaEntrada;
		this.filaProntos = filaProntos;
		this.swapper = s;
	}

	public boolean getTerminouDeEscalonar() {
		return this.terminouDeEscalonar;
	}

	private void escolheProcessoParaEscalonar() {
		terminouDeEscalonar = false;
		synchronized (filaEntrada) {
			p = filaEntrada.pegaPrimeiroProcesso();
		}

		synchronized (filaEntrada) {
			filaEntrada.removeProcessoDaFilaDeEntrada(p);
		}
		Date horaCorrente = new Date();
		String tempoCorrente = new SimpleDateFormat("HH:mm:ss").format(horaCorrente);
		System.out.printf(tempoCorrente+" : Escalonador FCFS de longo prazo escolheu e retirou o processo %d  da fila de entrada\n",p.getIdProcesso());

	}

	private synchronized void escalonaProcesso() {
		escolheProcessoParaEscalonar();	
		solicitaSwaper=true;
		Date horaCorrente = new Date();
		String tempoCorrente = new SimpleDateFormat("HH:mm:ss").format(horaCorrente);
		System.out.printf(tempoCorrente+" : Escalonador FCFS de longo prazo solicitou que o Swapper traga o processo %d à memoria\n",	p.getIdProcesso());
		swapper.setConseguiuColocarProcessoNaMemoria(false);
		swapper.setpFCFS(p);
		synchronized (swapper) {
			swapper.notify();
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
		System.out.printf(tempoCorrente+" : Escalonador FCFS de longo prazo colocou o processo %d na fila de prontos\n",
					p.getIdProcesso());

		swapper.setConseguiuColocarProcessoNaMemoria(false);
		solicitaSwaper = false;		
	}

	@Override
	public void run() {	
		synchronized (this) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		while(!filaEntrada.isEmpty() || !criador.isDead()){			
			if(!filaEntrada.isEmpty()){
				escalonaProcesso();
			}
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		isDead = true;
		//System.out.println("============================ FCFS MORREU ============================");
	}

	public boolean isDead() {
		return isDead;
	}
}