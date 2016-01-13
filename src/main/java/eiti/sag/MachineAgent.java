package eiti.sag;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class MachineAgent extends Agent {
	protected void setup() {
		System.out.println("Machine-agent " + getAID().getName() + " is ready.");
		
		Object[] args = getArguments();
		 if (args != null && args.length > 0) {
			 String arg = (String) args[0];
			 System.out.println("Argument: " + arg);
		 }
		 else {
			 // Make the agent terminate immediately
			 System.out.println("No argument");
			 doDelete();
		 }
		 
		 addBehaviour(new TickerBehaviour(this,10000) {			
			@Override
			protected void onTick() {
				System.out.println("Agent " + getAID().getName() + " send alive message");
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.addReceiver(new AID("manager1",AID.ISLOCALNAME));
				msg.setConversationId("MACHINE_ALIVE_MESSAGE");
				msg.setContent(getAID().getName());
				send(msg);
			}
		});
		addBehaviour(new ReceiveProductRequest(getAID().getName()));
		addBehaviour(new ReceiveProduct(getAID().getName()));
	}
	
	protected void takeDown() {
		System.out.println("Machine agent "+getAID().getName()+" terminating.");
	}
}
