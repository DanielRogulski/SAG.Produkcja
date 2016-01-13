package eiti.sag;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ReceiveProduct extends CyclicBehaviour {
	private String agentName;
	
	public ReceiveProduct(String agentName) {
		this.agentName = agentName;
	}
	
	@Override
	public void action() {
		MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchConversationId("SEND_PROCUCT"),
				MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL));
		
		ACLMessage msg = myAgent.receive(mt);
		if (msg != null) {
			String product = msg.getContent();
			System.out.println("[" + agentName + "] Receive product: " + product + " from " + msg.getSender().getName());
		}
		else {
			block();
		}

	}
}
