
public class Ex1NoSync {

	int counter;
	ThreadA IncThread;
	ThreadB DecThread;
	
	public static void main(String[] args) {
		Ex1NoSync exercise = new Ex1NoSync();
		exercise.runThreads();
	}
	
	Ex1NoSync() {
		counter = 0;
		IncThread = new ThreadA();
		DecThread = new ThreadB();
	}
	
	public void runThreads() {
		System.out.println("Initial Value of counter: "+counter);
		long startTime = System.nanoTime();
		IncThread.start();
		DecThread.start();
		System.out.println("Running threads...");
		try {
			IncThread.join();
			DecThread.join();
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
				myCounter = counter;
				myCounter++;
				counter = myCounter;
			}
		}
	}
	
	public class ThreadB extends Thread {
		int myCounter;
		
		public void run() {
			for(int i = 0; i < 100000 ; i++) {
				myCounter = counter;
				myCounter--;
				counter = myCounter;
			}
		}
	}
}
