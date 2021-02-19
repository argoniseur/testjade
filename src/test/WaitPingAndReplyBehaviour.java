package test;

import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

    public class WaitPingAndReplyBehaviour extends CyclicBehaviour {

        private Logger myLogger = Logger.getMyLogger(getClass().getName());
        private Agent agent;

        public WaitPingAndReplyBehaviour(Agent a) {
            super(a);
        }

        public void action() {
            ACLMessage  msg = myAgent.receive();
            if(msg != null){
                ACLMessage reply = msg.createReply();

                if(msg.getPerformative()== ACLMessage.REQUEST){
                    String content = msg.getContent();
                    if ((content != null) && (content.indexOf("ping") != -1)){
                        myLogger.log(Logger.INFO, "Agent "+agent.getLocalName()+" - Received PING Request from "+msg.getSender().getLocalName());
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setContent("pong");
                    }
                    else{
                        myLogger.log(Logger.INFO, "Agent "+agent.getLocalName()+" - Unexpected request ["+content+"] received from "+msg.getSender().getLocalName());
                        reply.setPerformative(ACLMessage.REFUSE);
                        reply.setContent("( UnexpectedContent ("+content+"))");
                    }

                }
                else {
                    myLogger.log(Logger.INFO, "Agent "+agent.getLocalName()+" - Unexpected message ["+ACLMessage.getPerformative(msg.getPerformative())+"] received from "+msg.getSender().getLocalName());
                    reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                    reply.setContent("( (Unexpected-act "+ACLMessage.getPerformative(msg.getPerformative())+") )");
                }
                agent.send(reply);
            }
            else {
                block();
            }
        }
    }