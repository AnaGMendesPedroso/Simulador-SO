import java.util.Vector;

public class FilaEntradaCompartilhada {
	private Vector<Processo> filaEntrada = new Vector<Processo>();

	public FilaEntradaCompartilhada() {
	}

	public synchronized boolean isEmpty() {
		return this.filaEntrada.isEmpty();
	}

	public synchronized Processo pegaPrimeiroProcesso() {
		return filaEntrada.firstElement();
	}

	public synchronized void removeProcessoDaFilaDeEntrada(Processo x) {
		// System.out.println("Elemento removido PID:"+x.getIdProcesso());
		filaEntrada.remove(x);
		ordenaFilaEntrada();
	}

	public synchronized void colocaNaFilaDeEntrada(Processo p) {
		filaEntrada.add(p);
	}

	private void ordenaFilaEntrada() {
		Vector<Processo> aux = new Vector<Processo>();
		int iterator = filaEntrada.size();
		int i = 1;
		while (iterator > 0 && i < filaEntrada.size()) {
			aux.add(filaEntrada.elementAt(i));
			i++;
			iterator--;
		}
		filaEntrada.clear();
		aux.forEach((Processo x) -> filaEntrada.add(x));
	}

	public synchronized int getTamanho() {
		return filaEntrada.size();
	}

	public void printaFilaEntrada() {
		System.out.print("\nFILA ENTRADA:");
		filaEntrada.forEach((Processo pa) -> System.out.printf("\nPID: %d\ntamanho:%d\nquando chegou:%d\nburst:%d\n",
				pa.getIdProcesso(), pa.getTamProcesso(), pa.getChegada(), pa.getBurst()));

	}
}