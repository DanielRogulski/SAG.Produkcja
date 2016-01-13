package eiti.sag;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class SendProcuctRequest extends Behaviour {
	private String agentName;
	private int step = 0;
	private int repliesCnt = 0;
	private int bestTime;
	private MessageTemplate mt;
	private AID bestMachine;
	
	public SendProcuctRequest(String agentName) {
		this.agentName = agentName;
	}
	
	@Override
	public void action() {
		switch (step) {
		case 0:
			System.out.println("[" + agentName + "] Send product request");
			ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
			cfp.addReceiver(new AID("machine1",AID.ISLOCALNAME));
			cfp.addReceiver(new AID("machine2",AID.ISLOCALNAME));
			cfp.addReceiver(new AID("machine3",AID.ISLOCALNAME));
			cfp.setContent("product1");
			cfp.setConversationId("SEND_PROCUCT_REQUEST");
			cfp.setReplyWith("cfp"+System.currentTimeMillis()); // Unique value
			myAgent.send(cfp);
			// Prepare the template to get proposals
			mt = MessageTemplate.and(MessageTemplate.MatchConversationId("SEND_PROCUCT_REQUEST"),
					MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
			step = 1;
			break;
		case 1:
			ACLMessage reply = myAgent.receive(mt);
			if (reply != null) {
				// Reply received
				if (reply.getPerformative() == ACLMessage.PROPOSE) {
					int time = Integer.parseInt(reply.getContent());
					System.out.println("[" + agentName + "] Receive response from: " + reply.getSender().getName() + " , time = " + time);
					if (bestMachine == null || time < bestTime) {
						bestTime = time;
						bestMachine = reply.getSender();
					}
				}
				if (reply.getPerformative() == ACLMessage.REFUSE) {
					System.out.println("[" + agentName + "] Receive Refuse massage");
				}
				repliesCnt++;
				if (repliesCnt >= 3) {
					step = 2; 
				}
			}
			else {
				block();
			}
			break;
		case 2:
			System.out.println("[" + agentName + "] Send product to " + bestMachine.getName());
			ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
			order.addReceiver(bestMachine);
			order.setContent("product");
			order.setConversationId("SEND_PROCUCT");
			order.setReplyWith("order"+System.currentTimeMillis());
			myAgent.send(order);
			// Prepare the template to get the purchase order reply
			//mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
			//		MessageTemplate.MatchInReplyTo(order.getReplyWith()));
			step = 3;
			break;
		}
	}

	@Override
	public boolean done() {
		return (step == 3 || (step == 2 && bestMachine == null));
	}

}
