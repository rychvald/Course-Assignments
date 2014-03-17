import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;


public class Ex2Savages2 {

	int capacity;
	volatile int pot;
	Cook cook;
	Set<Savage> savages;
	ReentrantLock lock;
	static Ex2Savages2 tribe;
	
	public static void main(String[] args) {
		tribe = new Ex2Savages2();
		tribe.enjoyYourMeal();
	}
	
	public Ex2Savages2() {
		this(8, 100);
	}
	
	public Ex2Savages2(int capacity, int savageNumber) {
		lock = new ReentrantLock();
		this.capacity = capacity;
		this.cook = new Cook();
		this.savages = new HashSet<Savage>();
		while(savageNumber > 0) {
			this.savages.add(new Savage());
			savageNumber--;
		}
	}
	
	public void enjoyYourMeal() {
		this.cook.start();
		for(Savage savage : this.savages) {
			savage.start();
		}
	}
	
	public class Savage extends Thread {
		private Integer ticketNumber;
		
		public void run() {
			while (true) {
				this.eat();
			}
		}
		
		private void eat() {
			lock.lock();
			if (pot > 0) {
				if (this.ticketNumber == null) {
					this.ticketNumber = pot;
				}
				if (this.ticketNumber == pot) {
					System.out.println("Savage "+this.ticketNumber+" enjoyed a meal!");
					pot--;
					System.out.println("Pot contents: "+pot);
				}
				lock.unlock();
				yield();
			} else {
				lock.unlock();
				this.hungry();
			}
		}
		
		private void hungry() {
			System.out.println("Savage is hungry, but pot is empty!");
			cook.refillRequest();
			yield();
			this.eat();
		}
	}
	
	public class Cook extends Thread {
		private volatile boolean refill = true;
		
		public void run() {
			while(true) {
				if(this.refill)
					this.refillPot();
				yield();
			}
		}
		
		private void refillPot() {
			synchronized(tribe) {	
				lock.lock();
				System.out.println("Pot seems to be empty: "+pot);
				assert pot == 0;
				pot = capacity;
				this.refill = false;
				System.out.println("Cook has refilled pot: "+pot);
				lock.unlock();
			}
		}
		
		public synchronized void refillRequest() {
			this.refill = true;
		}
	}
}
