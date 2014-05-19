import java.util.Random;

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
	private AddThread[] addThreadArray;
	private RemoveThread[] removeThreadArray;
	
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
		this.addThreadArray = new AddThread[this.threadNumber];
		this.removeThreadArray = new RemoveThread[this.threadNumber];
		int arraySize = 50000 / this.threadNumber;
		System.out.println("Each thread will get "+arraySize+" numbers");
		for(int i = 0 ; i < this.threadNumber ; i++) {
			this.addThreadArray[i] = new AddThread(this.createNumberArray(arraySize));
			this.removeThreadArray[i] = new RemoveThread(this.createNumberArray(arraySize));
		}
	}
	
	public void startThreads() {
		for( int i = 0 ; i < (this.threadNumber) ; i++) {
			this.addThreadArray[i].start();
			this.removeThreadArray[i].start();
		}
	}
	
	private void waitForEnd() {
		for(int i = 0 ; i < (this.threadNumber) ; i++) {
			try {
				this.addThreadArray[i].join();
				this.removeThreadArray[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public class AddThread extends Thread {
		
		private Integer[] myArray;
		
		public AddThread(Integer[] array) {
			super();
			this.myArray = array;
		}
		
		public void run() {
			for(int i = 0 ; i < myArray.length ; i++){
				this.manipulateList(myArray[i]);
				yield();
			}
			System.out.println("Thread finished adding numbers");
		}
		
		public void manipulateList(Integer i) {
			//System.out.println("Thread is adding number: "+i);
			TesterUnboundedLockBasedQueue.this.queue.enq(i);
		}
	}
	
	public class RemoveThread extends Thread {

		private Integer[] myArray;
		
		public RemoveThread(Integer[] array) {
			super();
			this.myArray = array;
		}
		
		public void run() {
			for(int i = 0 ; i < myArray.length ; i++){
				this.manipulateList(myArray[i]);
				yield();
			}
			System.out.println("Thread finished removing numbers");
		}
		
		public void manipulateList(Integer i) {
			//System.out.println("Thread is removing number: "+i);
			TesterUnboundedLockBasedQueue.this.queue.deq();
		}
	}
}
