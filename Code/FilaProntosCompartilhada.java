import java.util.Vector;
public class FilaProntosCompartilhada {
	private Vector<Processo> filaProntos;
	public FilaProntosCompartilhada () {
		this.filaProntos= new Vector<Processo>();
	}
	public synchronized void addListaProntos(Processo p) {
		this.filaProntos.add(p);
	}
	public synchronized Vector<Processo> getFilaProntos(){
		return this.filaProntos;
	}
	
	public synchronized void remove(Processo p){
		 this.filaProntos.remove(p);
	}
	public synchronized Processo removePrimeiroProcesso() {
		return this.filaProntos.remove(0);
		}
	public synchronized boolean isEmpty(){
		return this.filaProntos.isEmpty();
	}
}
