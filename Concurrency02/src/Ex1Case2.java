import java.util.concurrent.atomic.AtomicInteger;


public class Ex1Case2 {

	private static int MAX = 30000;
	
	public static void main(String[] args) {
		Ex1Case2 myCase = new Ex1Case2(4);
		myCase.startThreads();
		myCase.printFinalCounterValue();
	}

	private int sharedCounter;
	private int privateCounters[];
	private int threadNumber;
	private CounterThread threadArray[];
	private AtomicInteger  level[];
	private AtomicInteger  victim[];
	
	public Ex1Case2() {
		this(4);
	}
	
	public Ex1Case2(int n) {
		this.sharedCounter = 0;
		this.threadNumber = n;
		//create thread and thread's counter, initialise them
		threadArray = new CounterThread[this.threadNumber];
		privateCounters = new int[this.threadNumber];
		for( int i = 0 ; i < this.threadNumber ; i++) {
			threadArray[i] = new CounterThread(i);
			privateCounters[i] = 0;
		}
		//initialise victim and level arrays
		this.initPetersonArrays();
	}
	
	private void initPetersonArrays() {
		this.level = new AtomicInteger[this.threadNumber];
		this.victim = new AtomicInteger[this.threadNumber];
		for( int i = 0 ; i < this.threadNumber ; i++) {
			this.level[i] = new AtomicInteger(0);
		}
		for( int i = 0 ; i < this.threadNumber ; i++) {
			this.victim[i] = new AtomicInteger(0);
		}
	}
	
	public void startThreads() {
		for( int i = 0 ; i < this.threadNumber ; i++) {
			this.threadArray[i].start();
		}
	}
	
	private void printFinalCounterValue() {
		int sum = 0;
		for( int i = 0 ; i < this.threadNumber ; i++) {
			try {
				this.threadArray[i].join();
				sum = sum + this.privateCounters[i];
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Counter value: " + this.sharedCounter);
		System.out.println("Sum of counter accesses: " + sum);
	}
	
	public boolean higherLevelThreadExists(int level, int thread) {
		boolean retVal = false;
		for( int i = 0 ; i < this.threadNumber ; i++) {
			if(i != thread && this.level[i].get() >= level) {
				retVal = true;
				break;
			}
		}
		return retVal;
	}
	
	public class CounterThread extends Thread {
		
		private int myNumber;
		
		public CounterThread(int myNumber) {
			super();
			this.myNumber = myNumber;
		}
		
		public void run() {
			while(Ex1Case2.this.sharedCounter < MAX) {
				this.incrementCounter();
			}
			System.out.println("Final counter value of thread "
					+ this.myNumber+":\t"
					+ Ex1Case2.this.privateCounters[this.myNumber]);
		}
		
		private void incrementCounter() {
			this.lockCounter();
			if(Ex1Case2.this.sharedCounter < MAX) {
				Ex1Case2.this.sharedCounter++;
				Ex1Case2.this.privateCounters[this.myNumber]++;
			}
			this.unlockCounter();
		}
		
		private void lockCounter() {
			for (int L = 1; L < Ex1Case2.this.threadNumber; L++) {
				Ex1Case2.this.level[this.myNumber].set(L);
				Ex1Case2.this.victim[L].set(this.myNumber); 
				while 
					(Ex1Case2.this.higherLevelThreadExists(L,this.myNumber)
						&& Ex1Case2.this.victim[L].get() == this.myNumber) {
					yield();
				}
			}
		}
		
		private void unlockCounter() {
			Ex1Case2.this.level[this.myNumber].set(0);
		}
	}

}
