import java.util.HashSet;
import java.util.Set;

public class Ex1Sync {

	int counter;
	Set<ThreadA> IncThreads;
	Set<ThreadB> DecThreads;
	static Ex1Sync exercise;
	
	public static void main(String[] args) {
		exercise = new Ex1Sync(8);
		exercise.runThreads();
	}
	
	public Ex1Sync() {
		 this(1,1);
	}
	
	public Ex1Sync(int i) {
		 this(i,i);
	}

	public Ex1Sync(int n, int m) {
		counter = 0;
		this.IncThreads = new HashSet<ThreadA>();
		this.DecThreads = new HashSet<ThreadB>();
		while(n > 0) {
			ThreadA IncThread = new ThreadA();
			this.IncThreads.add(IncThread);
			n--;
		}
		while(m > 0) {
			ThreadB DecThread = new ThreadB();
			this.DecThreads.add(DecThread);
			m--;
		}
	}
	
	public void runThreads() {
		System.out.println("Initial Value of counter: "+counter);
		long startTime = System.nanoTime();
		for(ThreadA IncThread : this.IncThreads) {
			IncThread.start();
		}
		for(ThreadB DecThread : this.DecThreads) {
			DecThread.start();
		}
		System.out.println("Running threads...");
		try {
			for(ThreadA IncThread : this.IncThreads) {
				IncThread.join();
			}
			for(ThreadB DecThread : this.DecThreads) {
				DecThread.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long endTime = System.nanoTime();
		long duration = (endTime-startTime)/1000;
		System.out.println("Final value of counter: "+counter);
		System.out.println("Duration of thread execution: "+duration+"us");
	}
	
	public class ThreadA extends Thread {		
		int myCounter;
		
		public void run() {
			for(int i = 0; i < 100000 ; i++) {
				this.changeCounter();
			}
		}
		
		public void changeCounter() {
			synchronized(exercise) {
				myCounter = counter;
				myCounter++;
				counter = myCounter;
			}
		}
	}
	
	public class ThreadB extends ThreadA {
		public void changeCounter() {
			synchronized(exercise) {
				myCounter = counter;
				myCounter--;
				counter = myCounter;
			}
		}
	}
}
