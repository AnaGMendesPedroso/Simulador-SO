import java.util.Vector;

 class EscalonadorRoundRobin implements Runnable{
    private long timeQuantum;
    private EscalonadorFirstComeFirstServed fcfs;
    private Despachante despachante;
    private Processo p;
    private Timer tempo;

    public EscalonadorRoundRobin(long tq){
        this.timeQuantum = tq;
    }
    public EscalonadorRoundRobin(long tq, EscalonadorFirstComeFirstServed fcfs){
        this.timeQuantum = tq;
        this.fcfs = fcfs;
        this.tempo= new Timer(tq);
        this.tempo.setRR(this);
    }
    public long getTimeQuantum() {
        return timeQuantum;
    }

    private void escalonaProcesso(){
        p = fcfs.getFilaProntos().firstElement();
        fcfs.removeProcesso(p);
        System.out.printf("Escalonador Round-Robin de CPU escolheu o processo %d, retirou-o da fila de prontos e o encaminhou ao Despachante%n",p.getIdProcesso());
        despachante = new Despachante(p, timeQuantum);
        despachante.setTimer(tempo);
        despachante.run();
       
    }
    @Override
    public void run() {
        escalonaProcesso();
        while(despachante.isAlive());
    }
	public void voltaPraLista(Processo processoDaCpu) {
		if(processoDaCpu.getBurst()>0) {
			fcfs.addFilaEntrada(processoDaCpu);
		}	
		else {
			System.out.printf("Processo %d terminou sua execução%n",p.getIdProcesso());
			
		}
	}
    
    
}