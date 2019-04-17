import java.util.Vector;
public class FilaProntosCompartilhada {
	private Vector<Processo> filaProntos;
	public FilaProntosCompartilhada () {
		this.filaProntos= new Vector<Processo>();
	}
	public synchronized void addFilaProntos(Processo p) {
		this.filaProntos.add(p);
	}
	public synchronized Vector<Processo> getFilaProntos(){
		return this.filaProntos;
	}
	
	public synchronized Processo getPrimeiroProcesso(){
		return filaProntos.get(0);
	}
	public synchronized void remove(Processo p){
		 this.filaProntos.remove(p);
	}
	public synchronized Processo removePrimeiroProcesso() {
		Processo primeiroProcesso = filaProntos.firstElement();
		this.filaProntos.remove(primeiroProcesso);
		ordenaFilaProntos();
		return primeiroProcesso;
		
	}
	public synchronized boolean isEmpty(){
		return this.filaProntos.isEmpty();
	}

	private void ordenaFilaProntos(){
		Vector<Processo> aux = new Vector<Processo>();
		int iterator = filaProntos.size();
		int i = 1;
		while(iterator>0 && i < filaProntos.size()){
			aux.add(filaProntos.elementAt(i));
			i++;
			iterator--;
		}
		filaProntos.clear();
		aux.forEach((Processo x)->filaProntos.add(x));
	}
	public void printaFilaProntos(){
        System.out.print("\nFILA PRONTOS:");
		filaProntos.forEach((Processo pa)-> System.out.printf("\nPID: %d\ntamanho:%d\nquando chegou:%d\nburst:%d\n", 
                pa.getIdProcesso(), pa.getTamProcesso(), pa.getChegada(), pa.getBurst()));

	}
}
