import java.util.HashSet;
import java.util.Set;


public class Ex2Savages1 {

	int capacity;
	int pot;
	Cook cook;
	Set<Savage> savages;
	static Ex2Savages1 tribe;
	
	public static void main(String[] args) {
		tribe = new Ex2Savages1();
		tribe.enjoyYourMeal();
	}
	
	public Ex2Savages1() {
		this(8, 100);
	}
	
	public Ex2Savages1(int capacity, int savageNumber) {
		this.capacity = capacity;
		this.cook = new Cook();
		this.cook.refillRequest();
		this.savages = new HashSet<Savage>();
		while(savageNumber > 0) {
			this.savages.add(new Savage());
			savageNumber--;
		}
	}
	
	public void enjoyYourMeal() {
		for(Savage savage : this.savages) {
			savage.start();
		}
	}
	
	public class Savage extends Thread {
		public void run() {
			this.eat();
		}
		
		private void eat() {
			if (pot > 0) {
				synchronized(tribe) {
					System.out.println("Savage enjoyed a meal!");
					pot--;
					System.out.println("Pot contents: "+pot);
				}
			} else {
				this.hungry();
			}
		}
		
		private void hungry() {
			System.out.println("Savage is hungry, but pot is empty!");
			cook.refillRequest();
			this.eat();
		}
	}
	
	public class Cook extends Thread {
		public void run() {
			this.refillPot();
			
		}
		
		private void refillPot() {
			System.out.println("Pot seems to be empty: "+pot);
			assert pot == 0;
			pot = capacity;
			System.out.println("Cook has refilled pot: "+pot);
		}
		
		public void refillRequest() {
			this.refillPot();
		}
	}
}
