package eiti.sag;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ReceiveAliveBehaviour extends CyclicBehaviour {

	@Override
	public void action() {
		MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchConversationId("MACHINE_ALIVE_MESSAGE"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM));
		
		ACLMessage msg = myAgent.receive(mt);
		if (msg != null) {
			System.out.println("Receive alive message: " + msg.getContent());
		}
		else {
			block();
		} 
	}
}
