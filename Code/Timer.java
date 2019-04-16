public class Timer implements Runnable{
	private int tempoCpu;
	private Processo processoDaCpu;
	private int temporizador;
	
    public Timer(){
    	this.tempoCpu = 0;
    	
    }
    public synchronized void clock() {
    	this.tempoCpu++;
    	}
    public synchronized int getTempoCpu() {
    	return this.tempoCpu;
    }
    public synchronized void iniciarTemporizadorAte(int temporizador) {
    	this.temporizador=temporizador;
    }
    public synchronized void decrementaTemporizador(){
    	this.temporizador--;
    }
   public synchronized void reinicia(){
	 this.temporizador=0;  
   }
    
    
	@Override
    public void run() {
		try {
			
			while(this.temporizador>0) {
	    		this.clock();
	    		this.decrementaTemporizador();
	    	}
			this.notifyAll();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
}}
