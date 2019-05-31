
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
import java.util.LinkedList;

public class Memoria {
	private final int tamanho;
	private boolean[] hardProcessosNaMemoria;
	private int espacoLivre;
	private LinkedList<Processo> processosNaMemoria;

	public Memoria(int tamanho) {
		this.tamanho = tamanho;
		hardProcessosNaMemoria= new boolean[tamanho];
		processosNaMemoria = new LinkedList<Processo>();
		espacoLivre = tamanho;
	}

	public synchronized int getEspacoLivre() {
		return espacoLivre;
	}

	public synchronized boolean contemProcessoX(Processo x) {
		return processosNaMemoria.contains(x);
	}

	public void atualizaBurst(Processo p){
		int index = 0;
		for(Processo processo : processosNaMemoria) {
			if(processo.getIdProcesso() == p.getIdProcesso())
				index = processosNaMemoria.indexOf(processo);
		}
		processosNaMemoria.add(index,p);
		processosNaMemoria.remove(index);
	}
	public synchronized void adicionaProcessoFirstFit(Processo p) {
		int tamanhoDoProcesso = p.getTamProcesso();
		espacoLivre -= tamanhoDoProcesso;
		processosNaMemoria.add(p);
		
		int cont = 0;
		int index = 0;
		boolean cabe = false;
		for(int i = 0; i < tamanho; i++){
			index = i;
			for(int j = i; j<=tamanhoDoProcesso; j++){
				if(hardProcessosNaMemoria[j]==false){
					cont++;
				}else{
					break;
				}
			}
			if(cont == tamanhoDoProcesso){
				cabe = true;
				break;
			}else{
				cont=0;
			}
		}
		if(cabe){
			for(int i = index; i < tamanhoDoProcesso; i++){
				hardProcessosNaMemoria[i]=true;
			}
			p.setIndexMemoria(index);
		}
	}

	public synchronized boolean retiraProcessoDaMemoria() {
		boolean result = false;
		Processo p = processosNaMemoria.getFirst();
		if(!p.isEmUso()){
			Date horaCorrente = new Date();
			String tempoCorrente = new SimpleDateFormat("HH:mm:ss").format(horaCorrente);
			System.out.printf(tempoCorrente+" : Swapper retirou o processo %d para liberar espaço na memória, e o enviou ao disco\n",p.getIdProcesso());
			espacoLivre+=p.getTamProcesso();
			processosNaMemoria.remove(p);
			for(int i = p.getIndexMemoria(); i <= p.getTamProcesso(); i++){
				hardProcessosNaMemoria[i]=false;
			}
			p.setIndexMemoria(-1);
			result = true;
			
		}else{
			if(processosNaMemoria.size()>1){
				for(int i  = 1; i <= processosNaMemoria.size();i++){
					p = processosNaMemoria.get(i);
					if(!p.isEmUso()){
						Date horaCorrente = new Date();
						String tempoCorrente = new SimpleDateFormat("HH:mm:ss").format(horaCorrente);
						System.out.printf(tempoCorrente+" : Swapper retirou o processo %d para liberar espaço na memória, e o enviou ao disco\n",p.getIdProcesso());		
						espacoLivre+=p.getTamProcesso();	
						processosNaMemoria.remove(p);
						for(int k = p.getIndexMemoria(); k <= p.getTamProcesso(); k++){
							hardProcessosNaMemoria[k]=false;
						}
						p.setIndexMemoria(-1);
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}
}