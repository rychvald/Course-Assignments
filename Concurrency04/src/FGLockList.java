import java.util.concurrent.locks.ReentrantLock;


public class FGLockList {

	private Node head,tail;
	
	public FGLockList() {
		this.head = new Node(-1);
		this.tail = new Node(101);
		this.head.successor = this.tail;
	}
	
	public void add(int i) {
		Node predecessor = null, successor = null;
		Node newNode = new Node(i);
		try{
			predecessor = this.getPredecessor(i);
			predecessor.lock();
			successor = predecessor.successor;
			successor.lock();
			predecessor.successor = newNode;
			newNode.successor = successor;
		} finally {
			predecessor.unlock();
			successor.unlock();
		}
	}
	
	public void remove(int i) {
		Node predecessor = null, current = null;
		try{
			predecessor = this.getPredecessor(i);
			predecessor.lock();
			current = predecessor.successor;
			current.lock();
			if (current.value() == i) {
				predecessor.successor = current.successor;
				current.successor = null;
			}
		} finally {
			predecessor.unlock();
			current.unlock();
		}
	}
	
	public Node getPredecessor(int i) {
		Node currentNode = this.head;
		Node previousNode = null;
		while (currentNode.value() < i) {
			previousNode = currentNode;
			currentNode = previousNode.successor;
		}
		return previousNode;
	}
	
	public class Node {
		private int value;
		public volatile Node successor;
		private volatile ReentrantLock lock;
		
		public Node(int i){
			this.value = i;
			this.successor = null;
			this.lock = new ReentrantLock();
		}
		
		public int value() {
			return value;
		}
		
		public synchronized void lock() {
			this.lock.lock();
		}
		
		public synchronized void unlock() {
			this.lock.unlock();
		}
	}
	
}
