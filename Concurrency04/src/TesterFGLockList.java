import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class TesterFGLockList {
	
	public static void main(String[] args) {
		int n = 4;
		if (args.length > 0)
			n = Integer.parseInt(args[0]);
		TesterFGLockList myCase = new TesterFGLockList(n);
		long startTime = System.nanoTime();
		myCase.startThreads();
		myCase.waitForEnd();
		long endTime = System.nanoTime();
		long duration = (endTime-startTime)/1000;
		System.out.println("Duration of thread execution: "+duration+"us");
	}

	private FGLockList list;
	private HashSet<Integer> numberSet;
	private int threadNumber;
	private AddThread[] threadArray;
	
	public TesterFGLockList() {
		this(4);
	}
	
	public TesterFGLockList(int n) {
		assert n%2 == 0;
		this.threadNumber = n/2;
		this.numberSet = new HashSet<Integer>();
		this.fillSet(this.numberSet);
		this.createThreads();
	}
	
	private void fillSet(HashSet<Integer> set){
		Random generator = new Random();
		for(int i = 0 ; i < 100000 ; i++) {
			Integer number = generator.nextInt();
			number = number % 101;
			set.add(number);
			System.out.println(i);
		}
	}
	
	private void createThreads() {
		this.threadArray = new AddThread[2*this.threadNumber];
		int setSize = 50000 / this.threadNumber;
		System.out.println("Each thread will get "+setSize+" numbers");
		Iterator<Integer> setIterator = this.numberSet.iterator();
		for(int i = 0 ; i < this.threadNumber ; i=i+2) {
			HashSet<Integer> addThreadSet = new HashSet<Integer>(), removeThreadSet = new HashSet<Integer>();
			for(int h = 0 ; h < setSize ; h++) {
				System.out.println(h);
				assert setIterator.hasNext();
				addThreadSet.add(setIterator.next());
				assert setIterator.hasNext();
				removeThreadSet.add(setIterator.next());
			}
			this.threadArray[i] = new AddThread(addThreadSet);
			this.threadArray[i+1] = new RemoveThread(removeThreadSet);
		}
	}
	
	public void startThreads() {
		for( int i = 0 ; i < (2*this.threadNumber) ; i++) {
			this.threadArray[i].start();
		}
	}
	
	private void waitForEnd() {
		for(int i = 0 ; i < (2*this.threadNumber) ; i++) {
			try {
				this.threadArray[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public class AddThread extends Thread {
		
		private HashSet<Integer> mySet;
		
		public AddThread(HashSet<Integer> set) {
			super();
			this.mySet = set;
		}
		
		public void run() {
			for(Iterator<Integer> i = mySet.iterator() ; i.hasNext();){
				this.manipulateList(i.next());
			}
			System.out.println("Thread finished removing numbers");
		}
		
		private void manipulateList(Integer i) {
			System.out.println("Thread is removing number: "+i);
			TesterFGLockList.this.list.add(i);
		}
	}
	
	public class RemoveThread extends AddThread {

		public RemoveThread(HashSet<Integer> set) {
			super(set);
		}
		
		public void manipulateList(Integer i) {
			System.out.println("Thread is removing number: "+i);
			TesterFGLockList.this.list.remove(i);
		}
	}
}
