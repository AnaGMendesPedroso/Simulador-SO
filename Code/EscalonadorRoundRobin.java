import java.util.Vector;

class EscalonadorRoundRobin implements Runnable {
    private final int timeQuantum;
    private Despachante despachante;
    private Processo p;
    FilaProntosCompartilhada filaProntos;
    public EscalonadorRoundRobin(int tq, FilaProntosCompartilhada filaProntos ) {
        this.timeQuantum = tq;
        this.filaProntos = filaProntos;
    }

    public int getTimeQuantum() {
        return timeQuantum;
    }

    private synchronized void escalonaProcessoRR() {
    	
        Processo processoEscolhido = filaProntos.removePrimeiroProcesso();
        despachante.setProcessoEscolhidoParaExecucao(processoEscolhido);

        System.out.println("Escalonador Round-Robin de CPU escolheu o processo "
                            +processoEscolhido.getIdProcesso()+
                            ", retirou-o da fila de prontos e o encaminhou ao Despachante.");
    }

    @Override
    public void run() {
		System.out.println("cheguei aqui RR");

        escalonaProcessoRR();
        despachante.setTimeQuantumRR(timeQuantum);
      
        filaProntos.addListaProntos(p);
    }    
}