import java.util.concurrent.atomic.AtomicInteger;


public class Ex1Case1 {

	private static int MAX = 300000;
	
	public static void main(String[] args) {
		int n = 4;
		if (args.length > 0)
			n = Integer.parseInt(args[0]);
		if (args.length > 1) { // if a second argument is passed, run in 1Proc mode
			System.out.println("Args: "+args[0]+" "+args[1]);
			setSolarisAffinity();
		}
		Ex1Case1 myCase = new Ex1Case1(n);
		long startTime = System.nanoTime();
		myCase.startThreads();
		myCase.printFinalCounterValue();
		long endTime = System.nanoTime();
		long duration = (endTime-startTime)/1000;
		System.out.println("Duration of thread execution: "+duration+"us");
	}
	
	public static void setSolarisAffinity() {
		try {
			// retrieve process id
			String pid_name = java.lang.management.ManagementFactory.getRuntimeMXBean().getName(); 
			String [] pid_array = pid_name.split("@");
			int pid = Integer.parseInt( pid_array[0] );
			// random processor
			int processor = new java.util.Random().nextInt( 32 );
			// Set process affinity to one processor (on Solaris) 
			Process p = Runtime.getRuntime().exec("/usr/sbin/pbind -b "+processor+" "+pid);
			p.waitFor();
		} catch (Exception err) {
			err.printStackTrace(); }
		return;
	}

	private volatile int sharedCounter;
	private int privateCounters[];
	private int threadNumber;
	private CounterThread threadArray[];
	private AtomicInteger  level[];
	private AtomicInteger  victim[];
	
	public Ex1Case1() {
		this(4);
	}
	
	public Ex1Case1(int n) {
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
			while(Ex1Case1.this.sharedCounter < MAX) {
				this.incrementCounter();
			}
			System.out.println("Final counter value of thread "
					+ this.myNumber+":\t"
					+ Ex1Case1.this.privateCounters[this.myNumber]);
		}
		
		private void incrementCounter() {
			this.lockCounter();
			if(Ex1Case1.this.sharedCounter < MAX) {
				Ex1Case1.this.sharedCounter++;
				Ex1Case1.this.privateCounters[this.myNumber]++;
			}
			this.unlockCounter();
		}
		
		private void lockCounter() {
			for (int L = 1; L < Ex1Case1.this.threadNumber; L++) {
				Ex1Case1.this.level[this.myNumber].set(L);
				Ex1Case1.this.victim[L].set(this.myNumber); 
				while 
					(Ex1Case1.this.higherLevelThreadExists(L,this.myNumber)
						&& Ex1Case1.this.victim[L].get() == this.myNumber) {
					yield();
				}
			}
		}
		
		private void unlockCounter() {
			Ex1Case1.this.level[this.myNumber].set(0);
		}
	}

}
