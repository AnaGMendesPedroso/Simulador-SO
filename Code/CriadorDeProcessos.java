import java.util.Scanner;
import java.util.Vector;

public class CriadorDeProcessos implements Runnable{


	private Vector<Processo> filaEntrada = new Vector<Processo>();
	private int posicaoProcessoQueSeraIniciado = 0;
	private static Scanner scanner = new Scanner(System.in);
	private Timer timer;

	public CriadorDeProcessos(Timer t,Vector<Processo> filaEntrada){
		this.timer = t;
		this.filaEntrada= filaEntrada;
	}

	
	private void iniciaProcessosPorTempoChegada(){
		//while(!filaEntrada.isEmpty()){
			System.out.println("cheguei aqui criador");

			if(timer.getTempoCpu() == filaEntrada.elementAt(0).getChegada()){
				System.out.println("Criador de processos criou o processo "
									+filaEntrada.elementAt(posicaoProcessoQueSeraIniciado).getIdProcesso()+
									" e o colocou na fila de entrada. ");			
				
			}
			filaEntrada.remove(0);
			posicaoProcessoQueSeraIniciado++;
		}
	//}

	public synchronized Vector<Processo> getFilaEntrada(){
		return filaEntrada;
	}
	

	public void run() {
		iniciaProcessosPorTempoChegada();		
	}

}