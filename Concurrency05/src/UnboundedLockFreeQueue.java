import java.util.concurrent.atomic.AtomicReference;


public class UnboundedLockFreeQueue {

	private AtomicReference<Node> head,tail;
	
	public UnboundedLockFreeQueue() {
		this.head = new AtomicReference<Node>(new Node(0));
		this.tail = new AtomicReference<Node>(new Node(0));
		this.head.get().successor = this.tail;
	}
	
	public void enq (Integer i) {
		Node newNode = new Node(i);
		boolean goOn = true;
		while(goOn) {
			Node last = this.tail.get();
			Node next = last.successor.get();
			if (last == this.tail.get()) {
				if (next == null) {
					if (last.successor.compareAndSet(null,newNode)) {
						tail.compareAndSet(last,newNode);
						goOn = false;
					}
				} else {
					tail.compareAndSet(last,next);
				}
			}
		}
	}
	
	public Integer deq() {
		boolean goOn = true;
		Integer retVal = null;
		while (goOn) {
			Node first = this.head.get();
			Node last = this.tail.get();
			Node next = first.successor.get();
			if (first == this.head.get()) {
				if (first == last) {
					if (next == null) {
						System.out.println("Queue empty");
						return retVal;
					}
					tail.compareAndSet(last,next);
				} else {
					retVal = next.value();
					if (this.head.compareAndSet(first,next))
						goOn = false;
				}
			}
		}
		return retVal;
	}
	
	public class Node {
		private int value;
		public AtomicReference<Node> successor;
		
		public Node(int i){
			this.value = i;
			this.successor = new AtomicReference<Node>(null);
		}
		
		public int value() {
			return value;
		}
	}
	
}
