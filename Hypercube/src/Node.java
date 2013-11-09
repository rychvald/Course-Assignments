
public class Node {
	
	public Boolean firstdigit , seconddigit , thirddigit;
	public Boolean[] xNeighbour , yNeighbour , zNeighbour;
	
	public Node(boolean  thirddigit, boolean seconddigit , boolean firstdigit) {
		this.firstdigit = firstdigit;
		this.seconddigit = seconddigit;
		this.thirddigit = thirddigit;
		Boolean[] xNeighbour={this.thirddigit,this.seconddigit,!this.firstdigit};
		Boolean[] yNeighbour={this.thirddigit,!this.seconddigit,this.firstdigit};
		Boolean[] zNeighbour={!this.thirddigit,this.seconddigit,this.firstdigit};
		this.xNeighbour = xNeighbour;
		this.yNeighbour = yNeighbour;
		this.zNeighbour = zNeighbour;
	}
	
	public void sendPacketTo ( boolean thirddigit , boolean seconddigit , boolean firstdigit ) {
		Node destination = new Node(thirddigit , seconddigit , firstdigit);
		System.out.println("Packet with destination " + destination.toString() + " reached " + this.toString());
		if(firstdigit != this.firstdigit) {
			Node nextNode = new Node(this.thirddigit , this.seconddigit , !this.firstdigit);
			System.out.println("Packet with destination " + destination.toString() + " sent to " + nextNode.toString());
			nextNode.sendPacketTo(thirddigit , seconddigit , firstdigit);
		} else if(seconddigit != this.seconddigit) {
			Node nextNode = new Node(this.thirddigit , !this.seconddigit , this.firstdigit);
			System.out.println("Packet with destination " + destination.toString() + " sent to " + nextNode.toString());
			nextNode.sendPacketTo(thirddigit , seconddigit , firstdigit);
		} else if(thirddigit != this.thirddigit) {
			Node nextNode = new Node(!this.thirddigit , this.seconddigit , this.firstdigit);
			System.out.println("Packet with destination " + destination.toString() + " sent to " + nextNode.toString());
			nextNode.sendPacketTo(thirddigit , seconddigit , firstdigit);
		} else {
			this.processPacket(thirddigit , seconddigit , firstdigit);
		}
	}
	
	public void processPacket( boolean thirddigit , boolean seconddigit , boolean firstdigit ) {
		Node destination = new Node(thirddigit , seconddigit , firstdigit);
		System.out.println("Packet with destination " + destination.toString() + " processed by final node " + this.toString());
	}
	
	public String toString() {
		int thirddigit , seconddigit , firstdigit;
		thirddigit = this.thirddigit ? 1 : 0;
		seconddigit = this.seconddigit ? 1 : 0;
		firstdigit = this.firstdigit ? 1 : 0;
		StringBuilder sb = new StringBuilder();
		sb.append(thirddigit);
		sb.append(seconddigit);
		sb.append(firstdigit);
		
		return sb.toString();
	}
}
