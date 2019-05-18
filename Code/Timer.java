public class Timer implements Runnable {
	private int tempoCpu;
	private FilaEntradaCompartilhada filaEntrada;
	private FilaProntosCompartilhada filaProntos;
	private CriadorDeProcessos criador;
	private Despachante despachante;
	private boolean terminouTimeQuantum = false;

	public Timer(FilaEntradaCompartilhada fe, FilaProntosCompartilhada fp) {
		this.tempoCpu = 0;
		this.filaEntrada = fe;
		this.filaProntos = fp;
	}

	public synchronized void clock() {
		tempoCpu++;
		synchronized (filaEntrada) {
			filaEntrada.notify();
		}
	}

	public int getTempoCpu() {
		return this.tempoCpu;
	}

	public synchronized void iniciarTemporizadorAte(int temporizador) {
		int ateF = getTempoCpu() + temporizador;
		while (tempoCpu < ateF) {
			clock();
		}
		terminouTimeQuantum = true;
		synchronized (despachante) {
			despachante.notify();
		}
		terminouTimeQuantum = false;
	}

	public void setCriador(CriadorDeProcessos c) {
		this.criador = c;
	}

	public void setDespachante(Despachante d) {
		this.despachante = d;
	}

	public boolean getTerminouTimeQuantum() {
		return this.terminouTimeQuantum;
	}

	@Override
	public void run() {
		while (true) {
			clock();
		}
	}
}