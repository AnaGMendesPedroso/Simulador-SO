public class Timer implements Runnable{
	private int tempoCpu;
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
    public  void iniciarTemporizadorAte(int temporizador) {
    	this.temporizador=temporizador;
    	while(temporizador>0) {
    		this.clock();
    	}
    	
    }
    public synchronized void decrementaTemporizador(){
    	this.temporizador--;
    }
   public synchronized void reinicia(){
	 this.temporizador=0;  
   }
    
    
	@Override
    public void run() {
	
			clock();
	
		
		
		
}}
