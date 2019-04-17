import java.util.Scanner;
import java.util.Vector;

public class CriadorDeProcessos implements Runnable {

	private static Scanner scanner = new Scanner(System.in);
	private Timer timer;
	private FilaEntradaCompartilhada filaEntrada;
	private int posicaoProcesso = 0;

	public CriadorDeProcessos(Timer t, FilaEntradaCompartilhada filaEntrada) {
		this.timer = t;
		this.filaEntrada = filaEntrada;
	}

	private void iniciaProcessosPorTempoChegada() throws InterruptedException {
		Processo pa = filaEntrada.getProcessoPosicao(posicaoProcesso);

		System.out.printf("\n Processo que será iniciado\nPID: %d\ntamanho:%d\nquando chegou:%d\nburst:%d\n",pa.getIdProcesso(), pa.getTamProcesso(), pa.getChegada(), pa.getBurst());
		System.out.println("Tempo CPU:"+timer.getTempoCpu());

		if (timer.getTempoCpu() == pa.getChegada()) {
			System.out.println("Criador de processos criou o processo " + pa.getIdProcesso()
					+ " e o colocou na fila de entrada. ");
			posicaoProcesso++;
		}
		System.out.println("Finalizou iniciaProcesso");

	}

	public void run() {
		synchronized(this){
			int f = filaEntrada.getTamanho();
			while(f>0){
				try {
					iniciaProcessosPorTempoChegada();
					timer.clock();
					f--;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			notify();
		}
		
	}

}