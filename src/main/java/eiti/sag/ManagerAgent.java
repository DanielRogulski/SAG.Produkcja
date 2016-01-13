package eiti.sag;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class ManagerAgent extends Agent {
	protected void setup() {
		System.out.println("Manager-agent "+getAID().getName()+" is ready.");
		
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
		 
		 addBehaviour(new ReceiveAliveBehaviour());
		 addBehaviour(new TickerBehaviour(this,30000) {			
				@Override
				protected void onTick() {
					addBehaviour(new SendProcuctRequest(getAID().getName()));
				}
			});
	}
	
	protected void takeDown() {
		System.out.println("Manager agent "+getAID().getName()+" terminating.");
	}
}
