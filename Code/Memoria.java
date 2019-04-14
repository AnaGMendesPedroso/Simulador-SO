import java.util.Vector;

public class Memoria {
private int tamanho;
private Vector<Processo> listaProntos;
private int tamanhoLivre;
public Memoria(int tamanho) {
	this.tamanho = tamanho;
	}
public Memoria() {}
public int getTamanho() {
	return tamanho;
}
public void setTamanho(int tamanho) {
	this.tamanho = tamanho;
}
public Vector<Processo> getListaProntos() {
	return listaProntos;
}
public void setListaProntos(Vector<Processo> listaProntos) {
	this.listaProntos = listaProntos;
}
public int getTamanhoLivre() {
	return tamanhoLivre;
}
public void setTamanhoLivre(int tamanhoLivre) {
	this.tamanhoLivre = tamanhoLivre;
}
public void putListaProntos(Processo p) {
	this.listaProntos.add(p);
}

}
