import java.util.Vector;
public class FilaProntosCompartilhada {
	private Vector<Processo> listaProntos;
	public FilaProntosCompartilhada () {
		this.listaProntos= new Vector<Processo>();
	}
	public synchronized void addListaProntos(Processo p) {
		this.listaProntos.add(p);
	}
	public synchronized Vector<Processo> getFilaProntos(){
		return this.listaProntos;
	}
	
	public synchronized void remove(Processo p){
		 this.listaProntos.remove(p);
	}
}
