package test;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class Launcher {
    public static void main(String[] args) {
        Runtime runtime = Runtime.instance();
        Profile config = new ProfileImpl("localhost", 8888, null);
        config.setParameter("gui", "true");
        AgentContainer mc = runtime.createMainContainer(config);
        AgentController ac, ap;

        Object [] arg = new Object[3];
        arg[0] = "1";
        arg[1] = "arg2";
        arg[2] = "This is argument 3";

        try {
            ap = mc.createNewAgent("prod", Producer.class.getName(), arg);

            ap.start();
            ac = mc.createNewAgent("cons", Consumer.class.getName(), arg);
            ac.start();
        } catch (StaleProxyException e) { }
    }
}
