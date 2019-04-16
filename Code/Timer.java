public class Timer implements Runnable{
	private int tempoTotal;
	private Processo processoDaCpu;
	private int temporizador;
	
    public Timer(){
    	this.tempoTotal = 0;
    	
    }
    public synchronized void tictac() {
    	this.tempoTotal++;
    	}
    public synchronized int getTempoTotal() {
    	return this.tempoTotal;
    }
    public synchronized void iniciarTemporizadorAte(int temporizador) {
    	this.temporizador=temporizador;
    }
   public synchronized void reinicia(){
	 this.temporizador=0;  
   }
    
    
	@Override
    public void run() {
		try {
			this.wait();
			while(this.temporizador>0) {
	    		this.tictac();
	    		this.tempoTotal++;
	    		this.temporizador--;
	    	}
			this.notifyAll();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
}
	public int getTempoCorrente() {
		return 0;
	}}