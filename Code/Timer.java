public class Timer implements Runnable{
	private int tempoCpu;
	private int temporizador;
	private boolean isSet=false;
	
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
    	this.isSet=true;
    }
    public synchronized void decrementaTemporizador(){
    	this.temporizador--;
    }
   public synchronized void reinicia(){
	 this.temporizador=0;  
   }
    
    
	@Override
    public void run() {
		while(this.temporizador>0||!isSet) {
			this.clock();
			this.decrementaTemporizador();
		}
		this.notifyAll();
		
}}
