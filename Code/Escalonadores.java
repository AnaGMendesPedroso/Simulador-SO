import java.util.Vector;

public class Escalonadores {
	public static EscalonadorRoundRobin rr;
	public static EscalonadorFirstComeFirstServed fifo;
	public Escalonadores(long tq,  Memoria mem,Vector<Processo> filaEntrada){
		this.rr= new EscalonadorRoundRobin(tq);
		this.fifo= new  EscalonadorFirstComeFirstServed(mem,filaEntrada);
		fifo.start();
	}
	static class EscalonadorRoundRobin implements Runnable{
	    private long timeQuantum;
	    private Despachante despachante;
	    private Processo p;

	    public EscalonadorRoundRobin(long tq){
	        this.timeQuantum = tq;
	    }
	    public long getTimeQuantum() {
	        return timeQuantum;
	    }

	    private void escalonaProcesso() throws InterruptedException{
	    	//tira o primeiro processo
	    	fifo.join();
	    	p = fifo.getFilaProntos().remove(0);
	     
	        despachante = new Despachante(p, timeQuantum);
	        despachante.run();
	        
	    }
	    @Override
	    public void run() {
	        try {
				escalonaProcesso();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    }
	    
	    
	}
	
	static class EscalonadorFirstComeFirstServed extends Thread{
		private Memoria mem;
		private Vector<Processo> filaEntrada;
		private Vector<Processo> filaProntos;
		private  EscalonadorRoundRobin rr;
		
		public EscalonadorFirstComeFirstServed(Memoria mem, Vector<Processo> filaEntrada) {
			this.mem = mem;
			this.filaEntrada = filaEntrada;
		}
		public EscalonadorFirstComeFirstServed() {
		
		}
		public Memoria getMem() {
			return mem;
		}
		public void setMem(Memoria mem) {
			this.mem = mem;
		}
		public Vector<Processo> getFilaEntrada() {
			return filaEntrada;
		}
		public void setFilaEntrada(Vector<Processo> filaEntrada) {
			this.filaEntrada = filaEntrada;
		}
		public Vector<Processo> getFilaProntos() {
			return filaProntos;
		}
		public void setFilaProntos(Vector<Processo> filaProntos) {
			this.filaProntos = filaProntos;
		}
		//toda vez q ele for chamado ele irá..
		public void run() {
			//while(!filaEntrada.isEmpty()) {
			//pegar o primeiro da fila para ser executado
			Processo primeiro = filaEntrada.remove(0);
			//guarda o tamanho dele
			int tamProcesso = primeiro.getTamProcesso();
			// espera pra ver se ninguem ta alterando a memoria
			//(metodo ainda não implementado)
				
			// então verifidca se tem lugar para o processo na memoria
			if(mem.getEspacoLivre()>tamProcesso) {
				// se sim coloca na fila de prontos
				filaProntos.add(primeiro);
				rr.run();
					
			}
			//else {
				// se não, wait ddd
			//}
			
		//	}	
		}
		

	}
}
