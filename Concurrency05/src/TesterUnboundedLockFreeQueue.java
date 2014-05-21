import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class TesterUnboundedLockFreeQueue {
	
	public static void main(String[] args) {
		int n = 2;
		if (args.length > 0)
			n = Integer.parseInt(args[0]);
		System.out.println("Executing with "+n+" threads");
		TesterUnboundedLockFreeQueue myCase = new TesterUnboundedLockFreeQueue(n);
		long startTime = System.nanoTime();
		myCase.startThreads();
		myCase.waitForEnd();
		long endTime = System.nanoTime();
		long duration = (endTime-startTime)/1000;
		System.out.println("Duration of thread execution: "+duration+"us");
	}

	private UnboundedLockFreeQueue queue;
	private int threadNumber;
	private EnqThread[] enqThreadArray;
	private DeqThread[] deqThreadArray;
	private AtomicInteger mySyncPoint = new AtomicInteger(0);
	
	public TesterUnboundedLockFreeQueue() {
		this(2);
	}
	
	public TesterUnboundedLockFreeQueue(int n) {
		assert n%2 == 0;
		this.threadNumber = n/2;
		this.queue = new UnboundedLockFreeQueue();
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
			TesterUnboundedLockFreeQueue.this.mySyncPoint.addAndGet(1);				
			while (TesterUnboundedLockFreeQueue.this.mySyncPoint.get() < TesterUnboundedLockFreeQueue.this.threadNumber*2) {
				Thread.yield();
			}; System.out.println("EnqThread finished waiting for sync.");
			for(int i = 0 ; i < myArray.length ; i++){
				this.manipulateList(myArray[i]);
				Thread.yield();
			}
			System.out.println("Thread finished enqueueing numbers");
		}
		
		public void manipulateList(Integer i) {
			//System.out.println("Thread is enqueueing number: "+i);
			TesterUnboundedLockFreeQueue.this.queue.enq(i);
		}
	}
	
	public class DeqThread extends Thread {
		
		private int size;
		
		public DeqThread(int size) {
			super();
			this.size = size;
		}
		
		public void run() {
			TesterUnboundedLockFreeQueue.this.mySyncPoint.addAndGet(1);				
			while (TesterUnboundedLockFreeQueue.this.mySyncPoint.get() < TesterUnboundedLockFreeQueue.this.threadNumber*2) {
				Thread.yield();
			}; System.out.println("EnqThread finished waiting for sync.");
			for(int i = 0 ; i < this.size ; i++){
				this.manipulateList();
				Thread.yield();
			}
			System.out.println("Thread finished dequeueing numbers");
		}
		
		public void manipulateList() {
			//Integer i;
			TesterUnboundedLockFreeQueue.this.queue.deq();
			//System.out.println("Thread is dequeueing number: "+i);
		}
	}
}
