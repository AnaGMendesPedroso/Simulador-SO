import java.util.Scanner;
import java.util.Vector;

public class CriadorDeProcessos implements Runnable{
	private static Vector<Processo> filaEntrada = new Vector<Processo>();
	private static Vector<Processo> filaAux = new Vector<Processo>();
	private static Scanner scanner = new Scanner(System.in);

	public void criaProcesso(){
		int idProcesso = scanner.nextInt();
		int tamProcesso = scanner.nextInt();
		int chegada = scanner.nextInt();
		int burst = scanner.nextInt();
		Processo p = new Processo(idProcesso, tamProcesso, chegada, burst);
		filaAux.add(p);
	}
	public void ordenaPorChegada(){

		int iterator = filaAux.size();
		while(iterator > 0){
			int caux = filaAux.elementAt(0).getChegada();
			Processo c = filaAux.elementAt(0);

			for(int i = 0; i < filaAux.size(); i++){
				if(filaAux.elementAt(i).getChegada() < caux){
					c = filaAux.elementAt(i);
					caux = filaAux.elementAt(i).getChegada();
				}
			}
			filaEntrada.add(c);
			filaAux.remove(c);
			iterator--;
		}
	}

	public Vector getFilaEntrada(){
		return filaEntrada;
	}
	public void run() {
		criaProcesso();
		ordenaPorChegada();
	}

}

