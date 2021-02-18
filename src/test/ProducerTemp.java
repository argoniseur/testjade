package test;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.util.Logger;

import java.util.ArrayList;

public class ProducerTemp extends Agent {
    private int selling_price;
    private boolean energy_type;
    private int total_book_time;
    private ArrayList<String> consumers_list;

    private Logger myLogger = Logger.getMyLogger(getClass().getName());
    public ProducerTemp() {
    }

    protected void setup() {
        System.out.println("Hello World! My name is "+getLocalName());

        Object args[] = getArguments();

        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Producer");
        sd.setName(getName());
        sd.setOwnership("Prod");
        dfd.setName(getAID());
        dfd.addServices(sd);
        try {
            DFService.register(this,dfd);
            //WaitPingAndReplyBehaviour PingBehaviour = new  WaitPingAndReplyBehaviour(this);
            //addBehaviour(PingBehaviour);
        } catch (FIPAException e) {
            myLogger.log(Logger.SEVERE, "Agent "+getLocalName()+" - Cannot register with DF", e);
            doDelete();
        }
    }
}
