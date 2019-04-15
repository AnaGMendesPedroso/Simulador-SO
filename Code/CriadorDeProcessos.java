import java.util.Scanner;
import java.util.Vector;

public class CriadorDeProcessos implements Runnable{
	private static Vector<Processo> filaEntrada = new Vector<Processo>();
	private static Vector<Processo> filaAux = new Vector<Processo>();
	private static Scanner scanner = new Scanner(System.in);

	public CriadorDeProcessos(){
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

	public void setFilaAux(Vector<Processo> v){
		v.forEach((Processo p) -> this.filaAux.add(p));
	}
	public void run() {
		ordenaPorChegada();	
	}


}