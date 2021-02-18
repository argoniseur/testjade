package test;

import jade.core.Agent;

public class ConsumerTemp extends Agent {
    private int utility;
    private int consumption_time;

    public ConsumerTemp() {
    }

    protected void setup() {
        System.out.println("Hello World! My name is "+getLocalName());

        Object args[] = getArguments();
        for (int i = 0; i < args.length; i++){
            System.out.println("Argument " + i
                    + " : " + args[i].toString());
        }
    }
}
