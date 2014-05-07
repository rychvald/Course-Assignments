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
		if (this.contains(i))
			return;
		Node predecessor = null, successor = null;
		Node newNode = new Node(i);
		newNode.lock();
		try{
			predecessor = this.head;
			predecessor.lock();
			successor = predecessor.successor;
			successor.lock();
			while(successor.value() < i && successor != this.tail) {
				predecessor.unlock();
				predecessor = successor;
				successor = successor.successor;
				successor.lock();
			}
			if (successor.value() != i) {
				newNode.successor = successor;
				predecessor.successor = newNode;
			}
		} finally {
			if (predecessor != null)
				predecessor.unlock();
			if (successor != null)
				successor.unlock();
			if (newNode != null)
				newNode.unlock();
		}
	}
	
	public boolean remove(int i) {
		if(!this.contains(i))
			return false;
		Node predecessor = null, current = null;
		//System.out.println("Removing node "+i);
		boolean retVal = false;
		try{
			predecessor = this.head;
			predecessor.lock();
			current = predecessor.successor;
			current.lock();
			while(current.value() <= i && current != this.tail) {
				if (current.value() == i) {
					predecessor.successor = current.successor;
					retVal = true;
				}
				predecessor.unlock();
				predecessor = current;
				current = current.successor;
				current.lock();
			}
			return retVal;
		} finally {
			if (current != null)
				current.unlock();
			if (predecessor != null)
				predecessor.unlock();
		}
	}
	
	public boolean contains(int i) {
		Node current = this.head;
		boolean retVal = false;
		if (current.successor == this.tail || current.successor == null)
			return false;
		while(current.successor != this.tail) {
			if (current.value() == i) {
				retVal = true;
			}
			current = current.successor;
		}
		return retVal;
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
