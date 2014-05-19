import java.util.concurrent.locks.ReentrantLock;


public class UnboundedLockBasedQueue {

	private Node head,tail;
	private ReentrantLock enqLock , deqLock;
	
	public UnboundedLockBasedQueue() {
		this.head = new Node(0);
		this.tail = new Node(0);
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
		Integer retVal = null;
		this.deqLock.lock();
		try {
			if (head.successor == null) {
				System.out.println("Queue empty");	
			} else {
				retVal = head.successor.value();
				head = head.successor;
			}
			return retVal;
		} finally {
			this.deqLock.unlock();
		}
	}
	
	public class Node {
		private int value;
		public volatile Node successor;
		
		public Node(int i){
			this.value = i;
			this.successor = null;
		}
		
		public int value() {
			return value;
		}
	}
	
}
