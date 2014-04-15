import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;


public class CASLock implements Lock {

	@Override
	public void lockInterruptibly() throws InterruptedException {
		// TODO Auto-generated method stub

	}

	@Override
	public Condition newCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean tryLock() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tryLock(long arg0, TimeUnit arg1)
			throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}
	
	AtomicInteger lock;

	@Override
	public void lock() {
		while (!this.lock.compareAndSet(0, 1)) {
			continue;
		}
	}

	@Override
	public void unlock() {
		this.lock.set(0);
	}
	
	private static int MAX = 300000;
	
	public static void main(String[] args) {
		int n = 4;
		if (args.length > 0)
			n = Integer.parseInt(args[0]);
		CASLock myCase = new CASLock(n);
		long startTime = System.nanoTime();
		myCase.startThreads();
		myCase.printFinalCounterValue();
		long endTime = System.nanoTime();
		long duration = (endTime-startTime)/1000;
		System.out.println("Duration of thread execution: "+duration+"us");
	}

	private volatile int sharedCounter;
	private int privateCounters[];
	private int threadNumber;
	private CounterThread threadArray[];
	
	public CASLock() {
		this(4);
	}
	
	public CASLock(int n) {
		this.lock = new AtomicInteger(0);
		this.sharedCounter = 0;
		this.threadNumber = n;
		threadArray = new CounterThread[this.threadNumber];
		privateCounters = new int[this.threadNumber];
		for( int i = 0 ; i < this.threadNumber ; i++) {
			threadArray[i] = new CounterThread(i);
			privateCounters[i] = 0;
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
	
	public class CounterThread extends Thread {
		
		private int myNumber;
		
		public CounterThread(int myNumber) {
			super();
			this.myNumber = myNumber;
		}
		
		public void run() {
			while(CASLock.this.sharedCounter < MAX) {
				this.incrementCounter();
				yield();
			}
			System.out.println("Final counter value of thread "
					+ this.myNumber+":\t"
					+ CASLock.this.privateCounters[this.myNumber]);
		}
		
		private void incrementCounter() {
			CASLock.this.lock();
			if(CASLock.this.sharedCounter < MAX) {
				CASLock.this.sharedCounter++;
				CASLock.this.privateCounters[this.myNumber]++;
			}
			CASLock.this.unlock();
		}
	}

}
