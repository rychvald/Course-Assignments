import java.util.concurrent.locks.ReentrantLock;


public class UnboundedLockBasedQueue {

	protected Node head,tail;
	protected volatile ReentrantLock enqLock , deqLock;
	
	public UnboundedLockBasedQueue() {
		this.head = new Node(0);
		this.tail = this.head;
		this.head.successor = this.tail;
		this.enqLock = new ReentrantLock();
		this.deqLock = new ReentrantLock();
	}
	
	public void enq (Integer i) {
		this.enqLock.lock();
		try {
			Node newNode = new Node(i);
			this.tail.successor = newNode;
			this.tail = newNode;
		} finally{
			this.enqLock.unlock();
		}
	}
	
	public Integer deq() {
		Integer retVal = null;
		this.deqLock.lock();
		try {
			if (this.head.successor == this.tail || this.head.successor == null) {
				//System.out.println("Queue empty");	
			} else {
				retVal = this.head.successor.value;
				head = this.head.successor;
			}
			return retVal;
		} finally {
			this.deqLock.unlock();
		}
	}
	
	public class Node {
		public int value;
		public volatile Node successor;
		
		public Node(int i){
			this.value = i;
			this.successor = null;
		}
	}
	
}
