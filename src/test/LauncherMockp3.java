/*ProducerMock3 : Agent that only sells and under any condition.*/

package test;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class LauncherMockp3 {
    public static void main(String[] args) {
        Runtime runtime = Runtime.instance();
        Profile config = new ProfileImpl("localhost", 8888, null);
        config.setParameter("gui", "true");
        AgentContainer mc = runtime.createMainContainer(config);
        AgentController ac, ap;

        Object [] arg = new Object[2];
        arg[0] = "1";
        arg[1] = "30";

        try {
            ap = mc.createNewAgent("prod", ProducerMock3.class.getName(), arg);

            ap.start();
            ac = mc.createNewAgent("cons", ConsumerMock2.class.getName(), null);
            ac.start();
        } catch (StaleProxyException e) { }
    }
}
