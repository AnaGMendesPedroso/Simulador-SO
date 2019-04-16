public class Timer implements Runnable{
	private int tempoCpu;
	private int temporizador;
	
    public Timer(){
    	this.tempoCpu = 0;
    	
    }
    public synchronized void tictac() {
    	this.tempoCpu++;
    	}
    public synchronized int getTempoCpu() {
    	return this.tempoCpu;
    }
    public synchronized void decrementa() {
    	 this.temporizador--;
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
	    		this.decrementa();
	    	}
			this.notifyAll();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
}}
