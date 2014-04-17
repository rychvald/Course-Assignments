import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;


public class Ex2DoubleLock implements Lock {

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
		boolean check = true;
		while(check) {
			while(this.lock.get() == 1) {
				continue;
			}
			if (this.lock.compareAndSet(0, 1)) {
				check = false;
			}
		}
		
	}

	@Override
	public void unlock() {
		this.lock.set(0);
	}
	
	public void lockTail() {
		boolean check = true;
		while(check) {
			while(this.lock.get() == 1) {
				continue;
			}
			if (this.lock.compareAndSet(0, 1)) {
				check = false;
			}
		}
		
	}

	public void unlockTail() {
		this.lock.set(0);
	}
	
	private static int EXECUTES = 100000;
	
	public static void main(String[] args) {
		int n = 2;
		if (args.length > 0)
			n = Integer.parseInt(args[0]);
		Ex2DoubleLock myCase = new Ex2DoubleLock(n);
		long startTime = System.nanoTime();
		myCase.startThreads();
		myCase.waitForEnd();
		long endTime = System.nanoTime();
		long duration = (endTime-startTime)/1000;
		System.out.println("Duration of thread execution: "+duration+"us");
	}

	private int threadNumber;
	private EnqThread enqThreadArray[];
	private DeqThread deqThreadArray[];
	private int head, tail; 
	private static int QSIZE = 10;
	private int items[];
	
	public Ex2DoubleLock() {
		this(4);
	}
	
	public Ex2DoubleLock(int n) {
		if(n % 2 == 1)
			return;
		this.lock = new AtomicInteger(0);
		this.head = 0;
		this.tail = 0;
		this.items = new int[QSIZE];
		this.threadNumber = n/2;
		enqThreadArray = new EnqThread[this.threadNumber];
		deqThreadArray = new DeqThread[this.threadNumber];
		for( int i = 0 ; i < this.threadNumber ; i++) {
			enqThreadArray[i] = new EnqThread(EXECUTES);
			deqThreadArray[i] = new DeqThread(EXECUTES);
		}
	}

	public void enq(int x) {
		while (tail - head == QSIZE) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		items[tail % QSIZE] = x;
		this.lockTail();
		tail++;
		this.unlockTail();
	}

	public int deq() {
		while (tail == head) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int item = items[head % QSIZE];
		this.lock();
		head++;
		this.unlock();
		return item;
	}
	
	public void startThreads() {
		for( int i = 0 ; i < this.threadNumber ; i++) {
			this.enqThreadArray[i].start();
			this.deqThreadArray[i].start();
		}
	}
	
	public void waitForEnd() {
		for( int i = 0 ; i < this.threadNumber ; i++) {
			try {
				this.enqThreadArray[i].join();
				this.deqThreadArray[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public class EnqThread extends Thread {
		
		private int executes;
		private int accesses;
		
		public EnqThread(int executes) {
			super();
			this.executes = executes;
			this.accesses = 0;
		}
		
		public void run() {
			for (int i = 0 ; i < this.executes ; i++) {
				Ex2DoubleLock.this.enq(2);
				this.accesses++;
				yield();
			}
			System.out.println("EnqThread accessed "+this.accesses+" times");
		}
	}
	
	public class DeqThread extends Thread {
		
		private int executes;
		private int accesses;
		
		public DeqThread(int executes) {
			super();
			this.executes = executes;
			this.accesses = 0;
		}
		
		public void run() {
			for (int i = 0 ; i < this.executes ; i++) {
				Ex2DoubleLock.this.deq();
				this.accesses++;
				yield();
			}
			System.out.println("DeqThread accessed "+this.accesses+" times");
		}
	}

}
