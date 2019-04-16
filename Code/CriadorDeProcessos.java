import java.util.Scanner;
import java.util.Vector;

public class CriadorDeProcessos implements Runnable{

	private Vector<Processo> filaEntrada = new Vector<Processo>();
	private Vector<Processo> filaProcessosOrdenados = new Vector<Processo>();
	private static Vector<Processo> filaProcessosPassadosPeloUsuario = new Vector<Processo>();
	private int posicaoProcessoQueSeraIniciado = 0;
	private static Scanner scanner = new Scanner(System.in);
	private Timer timer;

	public CriadorDeProcessos(Timer t){
		this.timer = t;
	}

	private void ordenaProcessosPorChegada(){

		int iterator = filaProcessosPassadosPeloUsuario.size();
		while(iterator > 0){
			
			int caux = filaProcessosPassadosPeloUsuario.elementAt(0).getChegada();
			Processo c = filaProcessosPassadosPeloUsuario.elementAt(0);

			for(int i = 0; i < filaProcessosPassadosPeloUsuario.size(); i++){
				
				if(filaProcessosPassadosPeloUsuario.elementAt(i).getChegada() < caux){
					c = filaProcessosPassadosPeloUsuario.elementAt(i);
					caux = filaProcessosPassadosPeloUsuario.elementAt(i).getChegada();
				}
			}

			filaProcessosOrdenados.add(c);
			filaProcessosPassadosPeloUsuario.remove(c);
			iterator--;
		}
	}

	private void iniciaProcessosPorTempoChegada(){
		while(!filaProcessosOrdenados.isEmpty()){
			
			if(timer.getTempoCorrente() == filaProcessosOrdenados.elementAt(posicaoProcessoQueSeraIniciado).getChegada()){
				filaEntrada.add(filaProcessosOrdenados.elementAt(posicaoProcessoQueSeraIniciado));

				System.out.println("Criador de processos criou o processo "
									+filaProcessosOrdenados.elementAt(posicaoProcessoQueSeraIniciado).getIdProcesso()+
									" e o colocou na fila de entrada. ");
				
				posicaoProcessoQueSeraIniciado++;
			}
		}
	}

	public synchronized Vector getFilaEntrada(){
		return filaEntrada;
	}
	public synchronized void removeProcessoFilaEntrada(Processo p) {
		
		if(filaEntrada.contains(p)){
			filaEntrada.remove(p);
			filaProcessosOrdenados.remove(p);
			ordenaProcessosPorChegada();
		
		}else{
			System.out.println("Processo "+p.getIdProcesso()+ " não está na fila de entrada");
		}
	}
	public synchronized void setfilaProcessosPassadosPeloUsuario(Vector<Processo> v){
		v.forEach((Processo p) -> this.filaProcessosPassadosPeloUsuario.add(p));
	}

	public void run() {
		ordenaProcessosPorChegada();
		iniciaProcessosPorTempoChegada();		
	}

}