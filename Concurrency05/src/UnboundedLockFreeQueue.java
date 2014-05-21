import java.util.concurrent.atomic.AtomicReference;


public class UnboundedLockFreeQueue {

	private AtomicReference<Node> head,tail;
	
	public UnboundedLockFreeQueue() {
		Node node = new Node(0);
		this.head = new AtomicReference<Node>(node);
		this.tail = new AtomicReference<Node>(node);
	}
	
	public void enq (Integer i) {
		Node newNode = new Node(i);
		boolean goOn = true;
		while(goOn) {
			Thread.yield();
			Node last = this.tail.get();
			Node next = last.successor.get();
			if (last == this.tail.get()) {
				if (next == null) {
					if (last.successor.compareAndSet(next,newNode)) {
						this.tail.compareAndSet(last,newNode);
						goOn = false;
						//System.out.println("Enqueued "+i);
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
			Thread.yield();
			Node first = this.head.get();
			Node last = this.tail.get();
			Node next = first.successor.get();
			if (first == this.head.get()) {
				if (first == last) {
					if (next == null) {
						//System.out.println("Queue empty");
						goOn = false;
					} else {
						tail.compareAndSet(last,next);
					}
				} else {
					retVal = next.value;
					if (this.head.compareAndSet(first,next)) {
						goOn = false;
						//System.out.println("Dequeued "+retVal);
					}
				}
			}
		}
		return retVal;
	}
	
	public class Node {
		public int value;
		public AtomicReference<Node> successor;
		
		public Node(int i){
			this.value = i;
			this.successor = new AtomicReference<Node>(null);
		}
	}
}
