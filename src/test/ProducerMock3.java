/*ProducerMock3 : Agent that only sells and under any condition.

To test with LauncherMockp3
 */

package test;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ProducerMock3 extends Agent {
    private Integer renewable;
    private Integer sellprice;
    private Integer nbOfConsumer;

    protected void setup() {

        Object args[] = getArguments();
        renewable = Integer.valueOf((String)args[0]);
        sellprice = Integer.valueOf((String)args[1]);
        nbOfConsumer = 0;
        // Register the book-selling service in the yellow pages
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("energy-trading");
        sd.setName("JADE-energy-trading");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }

        // Add the behaviour serving queries from buyer agents
        addBehaviour(new OfferRequestsServer());

        // Add the behaviour serving purchase orders from buyer agents
        addBehaviour(new PurchaseOrdersServer());
    }

    // Put agent clean-up operations here
    protected void takeDown() {
        // Deregister from the yellow pages
        try {
            DFService.deregister(this);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
        // Printout a dismissal message
        System.out.println("Seller-agent "+getAID().getName()+" terminating.");
    }


    /**
     Inner class OfferRequestsServer.
     This is the behaviour used by Book-seller agents to serve incoming requests
     for offer from buyer agents.
     If the requested book is in the local catalogue the seller agent replies
     with a PROPOSE message specifying the price. Otherwise a REFUSE message is
     sent back.
     */
    private class OfferRequestsServer extends CyclicBehaviour {
        public void action() {/*
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                // CFP Message received. Process it
                String title = msg.getContent();
                ACLMessage reply = msg.createReply();

                if (nbOfConsumer < 10 && title.equals("Available ?")) {
                    // Producer is available
                    System.out.println("Agent "+msg.getSender().getName()+" asks if available");
                    reply.setPerformative(ACLMessage.PROPOSE);
                    byte [] content = new byte[2];
                    content[0] = renewable.byteValue();
                    content[1] = sellprice.byteValue();
                    reply.setByteSequenceContent(content);
                }
                else {
                    // Producer nor available, or message is not a request
                    reply.setPerformative(ACLMessage.REFUSE);
                    reply.setContent("not-available");
                }
                myAgent.send(reply);
            }
            else {
                block();
            }*/
        }
    }

    /**
     Inner class PurchaseOrdersServer.
     This is the behaviour used by Book-seller agents to serve incoming
     offer acceptances (i.e. purchase orders) from buyer agents.
     The seller agent removes the purchased book from its catalogue
     and replies with an INFORM message to notify the buyer that the
     purchase has been sucesfully completed.
     */
    private class PurchaseOrdersServer extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                // ACCEPT_PROPOSAL Message received. Process it
                String title = msg.getContent();
                ACLMessage reply = msg.createReply();

                if (nbOfConsumer < 10 /*&& title.equals("connect")*/) {
                    reply.setPerformative(ACLMessage.INFORM);
                    System.out.println("Connected to agent "+msg.getSender().getName());
                }
                else {
                    // Max consumer reached in the meanwhile
                    reply.setPerformative(ACLMessage.FAILURE);
                    reply.setContent("not-available");
                }
                myAgent.send(reply);
            }
            else {
                block();
            }
        }
    }
}