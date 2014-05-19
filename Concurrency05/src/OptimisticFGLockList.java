import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class OptimisticFGLockList {

	private Node head,tail;
	
	public OptimisticFGLockList() {
		this.head = new Node(-1);
		this.tail = new Node(101);
		this.head.successor = this.tail;
	}
	
	public void add(int i) {
		if(this.contains(i))
			return;
		Node predecessor = null, successor = null;
		Node newNode = new Node(i);
		newNode.lock();
		boolean goOn = true;
		//System.out.println("Adding node "+i);
		try{
			while(goOn) {
				predecessor = this.head;
				successor = predecessor.successor;
				while(successor.value() < i) {
					predecessor = successor;
					successor = successor.successor;
				}
				if (successor.value() == i)
					return;
				predecessor.lock();
				successor.lock();
				if (this.validate(predecessor , successor))
						goOn = false;
				else {
					predecessor.unlock();
					successor.unlock();
				}
			}
			if (predecessor.successor.value() > i) {
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
		if (!this.contains(i))
			return false;
		Node predecessor = null, current = null;
		//System.out.println("Removing node "+i);
		boolean goOn = true , retVal = false;
		while(goOn) {
			predecessor = this.head;
			current = predecessor.successor;
			while(current.value() <= i) {
				if (current.value() >= i) {
					break;
				}
				predecessor = current;
				current = current.successor;
			}
			if (current.value() >= i)
				goOn = false;
		}
		if (current.value() != i)
			return false;
		try{
			predecessor.lock();
			current.lock();
			if (this.validate(predecessor , current)) {
				if (current.value() == i) {
					predecessor.successor = current.successor;
					retVal = true;
				}
			} 
			return retVal;
		} finally {
			if (predecessor != null)
				predecessor.unlock();
			if (current != null)
				current.unlock();
		}
	}
	
	private boolean validate (Node predecessor , Node current) {
		Node node = this.head;
		while(node.value() <= predecessor.value()) {
			if (node == predecessor)
				return (predecessor.successor == current);
			node = node.successor;
		}
		return false;
	}
	
	public boolean contains(int i) {
		Node current = this.head;
		boolean retVal = false;
		while(current != this.tail) {
			if (current.value() == i) {
				retVal = true;
				break;
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