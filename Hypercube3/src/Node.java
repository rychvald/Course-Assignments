
public class Node {
	
	public Integer firstdigit , seconddigit , thirddigit;
	
	public Node(Integer  thirddigit, Integer seconddigit , Integer firstdigit) {
		this.firstdigit = firstdigit;
		this.seconddigit = seconddigit;
		this.thirddigit = thirddigit;
	}
	
	public void sendPacketTo ( Integer thirddigit , Integer seconddigit , Integer firstdigit ) {
		Node destination = new Node(thirddigit , seconddigit , firstdigit);
		System.out.println("Packet with destination " + destination.toString() + " reached " + this.toString());
		Node nextNode = null;
		if(!firstdigit.equals(this.firstdigit)) { 
			nextNode = new Node(this.thirddigit , this.seconddigit , firstdigit);
			System.out.println("Packet with destination " + destination.toString() + " sent to " + nextNode.toString());
			nextNode.sendPacketTo(thirddigit , seconddigit , firstdigit);
		} else if(!seconddigit.equals(this.seconddigit)) {
			nextNode = new Node(this.thirddigit , seconddigit , this.firstdigit);
			System.out.println("Packet with destination " + destination.toString() + " sent to " + nextNode.toString());
			nextNode.sendPacketTo(thirddigit , seconddigit , firstdigit);
		} else if(!thirddigit.equals(this.thirddigit)) {
			nextNode = new Node(thirddigit , this.seconddigit , this.firstdigit);
			System.out.println("Packet with destination " + destination.toString() + " sent to " + nextNode.toString());
			nextNode.sendPacketTo(thirddigit , seconddigit , firstdigit);
		} else {
			this.processPacket(thirddigit , seconddigit , firstdigit);
		}
	}
	
	public void processPacket( Integer thirddigit , Integer seconddigit , Integer firstdigit ) {
		Node destination = new Node(thirddigit , seconddigit , firstdigit);
		System.out.println("Packet with destination " + destination.toString() + " processed by final node " + this.toString());
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.thirddigit);
		sb.append(this.seconddigit);
		sb.append(this.firstdigit);
		return sb.toString();
	}
}
