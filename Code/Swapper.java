
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
public class Swapper implements Runnable {
	private Memoria mem;
	private ExecutorService executor;
	private EscalonadorFirstComeFirstServed fcfs;
	private Despachante despachante;
	private boolean conseguiuColocarProcessoNaMemoria = false;
	private Processo pDespachante = null;
	private Processo pFCFS = null;
	boolean isDead = false;


	public Swapper(Memoria m, ExecutorService e) {
		this.mem = m;
		this.executor = e;
	}

	public void setFCFS(EscalonadorFirstComeFirstServed f) {
		this.fcfs = f;
	}

	public void setDespachante(Despachante despachante2) {
		this.despachante = despachante2;
	}

	public synchronized void limpaMemoria(Processo atual) {
		while (mem.getEspacoLivre() < atual.getTamProcesso()) {
			boolean t = mem.retiraProcessoDaMemoria();
			while (!t) {
				t = mem.retiraProcessoDaMemoria();
			}
		}
	}
	public synchronized void trazProcessoParaMemoria(Processo atual){
		conseguiuColocarProcessoNaMemoria = false;
		if(!mem.contemProcessoX(atual)){
			if (mem.getEspacoLivre() >= atual.getTamProcesso()) {
				Date horaCorrente = new Date();
				String tempoCorrente = new SimpleDateFormat("HH:mm:ss").format(horaCorrente);
				System.out.printf(tempoCorrente+" : Swapper percebe que há espaço para o processo %d na memória\n", atual.getIdProcesso());
				mem.adicionaProcessoFirstFit(atual);
				System.out.printf(tempoCorrente+" : Swapper traz o processo %d do disco e o coloca na memória\n", atual.getIdProcesso());
				conseguiuColocarProcessoNaMemoria = true;
			} else {
				Date horaCorrente = new Date();
				String tempoCorrente = new SimpleDateFormat("HH:mm:ss").format(horaCorrente);
				System.out.printf(tempoCorrente+" : Swapper percebe que não há espaço para o processo %d na memória\n", atual.getIdProcesso());
				limpaMemoria(atual);
				trazProcessoParaMemoria(atual);
			}
		}
		// Date horaCorrente = new Date();
		// String tempoCorrente = new SimpleDateFormat("HH:mm:ss").format(horaCorrente);
		// System.out.printf(tempoCorrente+" : Processo %d já está na memória\n", atual.getIdProcesso());
	}

	@Override
	public void run() {
		while (!despachante.isDead() || !fcfs.isDead()) {
			if(despachante.solicitaSwaper &&  pDespachante!=null ){
				trazProcessoParaMemoria(pDespachante);
				notificaDespachante();
				pDespachante = null;
			}
			if(fcfs.solicitaSwaper && pFCFS!=null){
				trazProcessoParaMemoria(pFCFS);
				notificaFCFS();
				pFCFS = null;
			}
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		isDead= true;
		synchronized(executor){
			executor.notify();
		}
		//System.out.println("============================ SWAPPER MORREU ============================");
	}
	private void notificaDespachante(){
		if(conseguiuColocarProcessoNaMemoria){
			Date horaCorrente = new Date();
			String tempoCorrente = new SimpleDateFormat("HH:mm:ss").format(horaCorrente);
			System.out.printf(tempoCorrente+" : Swapper avisa ao Despachante que o processo "+pDespachante.getIdProcesso()+" está na memória");
			synchronized (despachante) {
				despachante.notify();
			}
		}
	}
	private void notificaFCFS(){	
		if(conseguiuColocarProcessoNaMemoria){
			Date horaCorrente = new Date();
			String tempoCorrente = new SimpleDateFormat("HH:mm:ss").format(horaCorrente);
			System.out.printf(tempoCorrente+" : Swapper avisa o Escalonador FCFS de longo prazo que o processo %d está na memória\n",pFCFS.getIdProcesso());
			synchronized (fcfs) {
				fcfs.notify();
			}
		}
	}

	public boolean getConseguiuColocarProcessoNaMemoria() {
		return conseguiuColocarProcessoNaMemoria;
	}

	public void setConseguiuColocarProcessoNaMemoria(boolean conseguiuColocarProcessoNaMemoria) {
		this.conseguiuColocarProcessoNaMemoria = conseguiuColocarProcessoNaMemoria;
	}

	public void setpDespachante(Processo pDespachante) {
		this.pDespachante = pDespachante;
	}

	public void setpFCFS(Processo pFCFS) {
		this.pFCFS = pFCFS;
	}
}