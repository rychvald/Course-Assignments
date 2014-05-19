import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class UnboundedLockBasedQueue {

	private Node head,tail;
	private ReentrantLock enqLock , deqLock;
	
	public UnboundedLockBasedQueue() {
		this.head = new Node(-1);
		this.tail = new Node(101);
		this.head.successor = this.tail;
		this.enqLock = new ReentrantLock();
		this.deqLock = new ReentrantLock();
	}
	
	public void enq (Integer i) {
		this.enqLock.lock();
		try {
			Node newNode = new Node(i);
			tail.successor = newNode;
			tail = newNode;
		} finally{
			this.enqLock.unlock();
		}
	}
	
	public Integer deq() {
		return null;
	}
	
	public class Node implements Lock{
		private int value;
		public volatile Node successor;
		private AtomicInteger lock;
		
		public Node(int i){
			this.value = i;
			this.successor = null;
			this.lock = new AtomicInteger(0);
		}
		
		public int value() {
			return value;
		}
		
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
	}
	
}
