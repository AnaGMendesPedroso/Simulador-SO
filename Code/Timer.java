public class Timer implements Runnable{
	private int tempoInicial;
	private EscalonadorRoundRobin rr;
	private Processo processoDaCpu;
	private long timeQuantum;
	private boolean usando;
    public Timer(long tq){
    	this.timeQuantum= tq;
    	this.usando=false;
    	this.tempoInicial = 0;
    	
    }
    public void setRR(EscalonadorRoundRobin rr) {
    	this.rr=rr;
    }
    public void ticTac() {
    	this.tempoInicial++;
    }
	public void reinicia() {
		this.tempoInicial = 0;
	}
	public EscalonadorRoundRobin getRr() {
		return rr;
	}
	public Processo getProcessoDaCpu() {
		return processoDaCpu;
	}
	public void setProcessoDaCpu(Processo processoDaCpu) {
		this.processoDaCpu = processoDaCpu;
	}
	public long getTimeQuantum() {
		return timeQuantum;
	}
	public void setTimeQuantum(int timeQuantum) {
		this.timeQuantum = timeQuantum;
	}
	public boolean isUsando() {
		return usando;
	}
	public void setUsando(boolean usando) {
		this.usando = usando;
	}
	@Override
    public void run() {
		System.out.printf("Timer informa ao Escalonador Round-Robin de CPU que o processo %d atualmente em execução precisa ser retirado da CPU%n",processoDaCpu.getIdProcesso());
    	int tempoExecutado=0;
		while(tempoInicial<timeQuantum||processoDaCpu.getBurst()==0) {
    		this.ticTac();
    		int novoBurst = processoDaCpu.getBurst()-1;
    		processoDaCpu.setBurst(novoBurst);
    		this.setUsando(true);
    		tempoExecutado++;
    	}
		System.out.printf("O processo %d foi executado por %d de tempo na cpu%n",processoDaCpu.getIdProcesso(),tempoExecutado);
    	
    	rr.voltaPraLista(processoDaCpu);
    	this.setUsando(false);

    }

    
}