
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
			predecessor.successor = current.successor;
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
		private volatile boolean locked;
		
		public Node(int i){
			this.value = i;
			this.successor = null;
			this.locked = false;
		}
		
		public int value() {
			return value;
		}
		
		public synchronized void lock() {
			while(this.locked) {
				Thread.yield();
			}
			this.locked = true;
		}
		
		public synchronized void unlock() {
			this.locked = false;
		}
	}
	
}
