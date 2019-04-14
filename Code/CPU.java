public class CPU implements Runnable{
   private Processo p;
   private long timeQuantum;
   private Timer timer;

    public CPU(Processo p, long timeQuantum, Timer timer){
        this.p = p;
        this.timeQuantum = timeQuantum;
        this.timer = timer;
   }

    @Override
    public void run() {
        
    }

}