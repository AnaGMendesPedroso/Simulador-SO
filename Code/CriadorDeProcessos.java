import java.util.Scanner;
import java.util.Vector;

public class CriadorDeProcessos {
	public static void main(String [] args) {
		
		Vector<Processo> listaEntrada = new Vector<Processo>();
		Vector<Processo> listaAux = new Vector<Processo>();
		Scanner teclado = new Scanner(System.in);
		
		System.out.println("Entre com tmp n tq (lista de dados de cada processo id tp tc tb)");
		int tamanhoMemoria = teclado.nextInt();
		int qtdProcessos = teclado.nextInt();
		int timeQuantum = teclado.nextInt();
		
		while(qtdProcessos > 0) {
			 int idProcesso = teclado.nextInt();
			 int tamProcesso = teclado.nextInt();
			 int chegada = teclado.nextInt();
			 int burst = teclado.nextInt();
			 Processo p = new Processo(idProcesso, tamProcesso, chegada, burst);
			 listaAux.add(p);
			 qtdProcessos--;
		 }
		 System.out.println("Processos em LAUX:");
		 listaAux.forEach((Processo p)-> System.out.printf("%d %d %d %d \n",p.getIdProcesso(),p.getTamProcesso(),p.getChegada(),p.getBurst()));
		
		int iterator = listaAux.size();
		while(iterator > 0){
			int caux = listaAux.elementAt(0).getChegada();
			Processo c = listaAux.elementAt(0);

			for(int i = 0; i < listaAux.size(); i++){
				if(listaAux.elementAt(i).getChegada() < caux){
					c = listaAux.elementAt(i);
					caux = listaAux.elementAt(i).getChegada();
				}
			}
			listaEntrada.add(c);
			//System.out.printf("Add c em LENTRADA: %d %d %d %d \n",c.getIdProcesso(),c.getTamProcesso(),c.getChegada(),c.getBurst());
			System.out.println("Processos emm LENTRDA:");
		 	listaEntrada.forEach((Processo p)-> System.out.printf("%d %d %d %d \n",p.getIdProcesso(),p.getTamProcesso(),p.getChegada(),p.getBurst()));
			
			
			 listaAux.remove(c);
			//System.out.printf("Remove c em LAUX: %d %d %d %d \n",c.getIdProcesso(),c.getTamProcesso(),c.getChegada(),c.getBurst());
			System.out.println("Processos em LAUX:");
			listaAux.forEach((Processo p)-> System.out.printf("%d %d %d %d \n",p.getIdProcesso(),p.getTamProcesso(),p.getChegada(),p.getBurst()));
			iterator--;
		}
		
		/*System.out.println("Processos emm LENTRDA:");
		 listaEntrada.forEach((Processo p)-> System.out.printf("%d %d %d %d \n",p.getIdProcesso(),p.getTamProcesso(),p.getChegada(),p.getBurst()));
		*/
	
		}

}
