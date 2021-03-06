package test;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.*;

public class Producer extends Agent {
    private Integer renewable;
    private Integer sellprice;
    private ArrayList<AID> consumers = new ArrayList<>();

    protected void setup() {

        Object args[] = getArguments();
        renewable = Integer.valueOf((String)args[0]);
        sellprice = Integer.valueOf((String)args[1]);
        // Register the producer in the yellow pages
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

        // Add the behaviour disconnecting the consumer
        addBehaviour(new ShutConsumerBehaviour());
    }

    protected void takeDown() {
        // Deregister from the yellow pages
        try {
            DFService.deregister(this);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
        System.out.println("Producer "+getAID().getName()+" terminating.");
    }


    /**
     Inner class OfferRequestsServer.
     This is the behaviour used by producer to process the incoming
     questions from agents and send them energy proposals
     */
    private class OfferRequestsServer extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                // CFP Message received. Process it
                String title = msg.getContent();
                ACLMessage reply = msg.createReply();

                if (consumers.size() < 10 && title.equals("Available ?")) {
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
            }
        }
    }

    /**
     Inner class PurchaseOrdersServer.
     This is the behaviour used by the producer to acknoledge the connection to the consumer
     */
    private class PurchaseOrdersServer extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                // ACCEPT_PROPOSAL Message received. Process it
                String title = msg.getContent();
                ACLMessage reply = msg.createReply();

                if (consumers.size() < 10 && title.equals("connect")) {
                    reply.setPerformative(ACLMessage.INFORM);
                    consumers.add(msg.getSender());
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

    /**
     Inner class ShutConsumerBehaviour.
     This is the behaviour used by producer agents to serve incoming
     consumer disconnection notifications
     The producer agent removes the consumer from the consumer list
     */
    private class ShutConsumerBehaviour extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                // ACCEPT_PROPOSAL Message received. Process it
                String title = msg.getContent();
                System.out.println(title);
                if (title.equals("Ciao")){
                    System.out.println(consumers.toString());
                    consumers.remove(msg.getSender());
                    System.out.println(consumers.toString());
                }
            } else {
                block();
            }
        }
    }
}