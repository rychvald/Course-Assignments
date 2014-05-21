import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class TesterUnboundedLockBasedQueue {
	
	public static void main(String[] args) {
		int n = 2;
		if (args.length > 0)
			n = Integer.parseInt(args[0]);
		System.out.println("Executing with "+n+" threads");
		TesterUnboundedLockBasedQueue myCase = new TesterUnboundedLockBasedQueue(n);
		long startTime = System.nanoTime();
		myCase.startThreads();
		myCase.waitForEnd();
		long endTime = System.nanoTime();
		long duration = (endTime-startTime)/1000;
		System.out.println("Duration of thread execution: "+duration+"us");
	}

	private UnboundedLockBasedQueue queue;
	private int threadNumber;
	private EnqThread[] enqThreadArray;
	private DeqThread[] deqThreadArray;
	private AtomicInteger mySyncPoint = new AtomicInteger(0);
	
	public TesterUnboundedLockBasedQueue() {
		this(2);
	}
	
	public TesterUnboundedLockBasedQueue(int n) {
		assert n%2 == 0;
		this.threadNumber = n/2;
		this.queue = new UnboundedLockBasedQueue();
		this.createThreads();
	}
	
	private Integer[] createNumberArray(int size){
		Random generator = new Random();
		Integer[] intArray = new Integer[size];
		for(int i = 0 ; i < size ; i++) {
			Integer number = generator.nextInt();
			intArray[i] = number;
			//System.out.println(i);
		}
		return intArray;
	}
	
	private void createThreads() {
		this.enqThreadArray = new EnqThread[this.threadNumber];
		this.deqThreadArray = new DeqThread[this.threadNumber];
		int arraySize = 50000 / this.threadNumber;
		System.out.println("Each thread will get "+arraySize+" numbers");
		for(int i = 0 ; i < this.threadNumber ; i++) {
			this.enqThreadArray[i] = new EnqThread(this.createNumberArray(arraySize));
			this.deqThreadArray[i] = new DeqThread(arraySize);
		}
	}
	
	public void startThreads() {
		for( int i = 0 ; i < (this.threadNumber) ; i++) {
			this.enqThreadArray[i].start();
			this.deqThreadArray[i].start();
		}
	}
	
	private void waitForEnd() {
		for(int i = 0 ; i < (this.threadNumber) ; i++) {
			try {
				this.enqThreadArray[i].join();
				this.deqThreadArray[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public class EnqThread extends Thread {
		
		private Integer[] myArray;
		
		public EnqThread(Integer[] array) {
			super();
			this.myArray = array;
		}
		
		public void run() {
			TesterUnboundedLockBasedQueue.this.mySyncPoint.addAndGet(1);				
			while (TesterUnboundedLockBasedQueue.this.mySyncPoint.get() < TesterUnboundedLockBasedQueue.this.threadNumber*2) {
				
			}; System.out.println("EnqThread finished waiting for sync.");
			for(int i = 0 ; i < myArray.length ; i++){
				this.manipulateList(myArray[i]);
				yield();
			}
			System.out.println("Thread finished adding numbers");
		}
		
		public void manipulateList(Integer i) {
			//System.out.println("Thread is enqueueing number: "+i);
			TesterUnboundedLockBasedQueue.this.queue.enq(i);
		}
	}
	
	public class DeqThread extends Thread {
		
		private int size;
		
		public DeqThread(int size) {
			super();
			this.size = size;
		}
		
		public void run() {
			TesterUnboundedLockBasedQueue.this.mySyncPoint.addAndGet(1);				
			while (TesterUnboundedLockBasedQueue.this.mySyncPoint.get() < TesterUnboundedLockBasedQueue.this.threadNumber*2) {
				
			}; System.out.println("EnqThread finished waiting for sync.");
			for(int i = 0 ; i < this.size ; i++){
				this.manipulateList();
				yield();
			}
			System.out.println("Thread finished removing numbers");
		}
		
		public void manipulateList() {
			//Integer i;
			TesterUnboundedLockBasedQueue.this.queue.deq();
			//System.out.println("Thread is dequeueing number: "+i);
		}
	}
}
