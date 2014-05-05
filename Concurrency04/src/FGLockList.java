import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;


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
	
	public Node getPredecessor(int i) {
		System.out.println("Getting predecessor for "+i);
		Node currentNode = this.head;
		Node previousNode = null;
		while (currentNode.value() < i) {
			previousNode = currentNode;
			currentNode = previousNode.successor;
		}
		return previousNode;
	}
	
	public boolean remove(int i) {
		Node predecessor = null, current = null;
		System.out.println("Removing node "+i);
		try{
			predecessor = this.head;
			predecessor.lock();
			current = this.head.successor;
			current.lock();
			while(current.value() <= i && current != this.tail) {
				if (current.value() == i) {
					predecessor.successor = current.successor;
					return true;
				}
				current.unlock();
				predecessor.unlock();
			}
			return false;
		} finally {
			predecessor.unlock();
			current.unlock();
		}
	}
	
	public class Node implements Lock{
		private int value;
		public volatile Node successor;
		AtomicInteger lock;
		
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
