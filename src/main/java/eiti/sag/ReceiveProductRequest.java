package eiti.sag;

import java.util.Random;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ReceiveProductRequest extends CyclicBehaviour {
	private String agentName;
	
	public ReceiveProductRequest(String agentName) {
		this.agentName = agentName;
	}

	@Override
	public void action() {
		MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchConversationId("SEND_PROCUCT_REQUEST"),
				MessageTemplate.MatchPerformative(ACLMessage.CFP));
		
		ACLMessage msg = myAgent.receive(mt);
		if (msg != null) {
			String product = msg.getContent();
			ACLMessage reply = msg.createReply();
			System.out.println("[" + agentName + "] Receive product request: " + product  + " from " + msg.getSender().getName());
			
			Random generator = new Random();
			if (generator.nextBoolean() == true) {
				Integer time = generator.nextInt(1000);
				reply.setPerformative(ACLMessage.PROPOSE);
				reply.setContent(String.valueOf(time.intValue()));
			}
			else {
				// The requested book is NOT available.
				reply.setPerformative(ACLMessage.REFUSE);
				reply.setContent("not-available");
			}
			myAgent.send(reply);
		}
		else {
			block();
		}

	}

}
