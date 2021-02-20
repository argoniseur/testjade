/* CustomerMock2 : Agent that buys energy immediately
from all producers without comparing.
 * To test with LauncherMock2 */

package test;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class LauncherMock2 {
    public static void main(String[] args) {
        Runtime runtime = Runtime.instance();
        Profile config = new ProfileImpl("localhost", 8888, null);
        config.setParameter("gui", "true");
        AgentContainer mc = runtime.createMainContainer(config);
        AgentController ac, ap, ap2;

        Object [] arg = new Object[2];
        arg[0] = "1";
        arg[1] = "30";

        try {
            ap = mc.createNewAgent("prod", Producer.class.getName(), arg);

            ap.start();
            ap2 = mc.createNewAgent("prod2", Producer.class.getName(), arg);

            ap2.start();
            ac = mc.createNewAgent("cons", ConsumerMock2.class.getName(), null);
            ac.start();
        } catch (StaleProxyException e) { }
    }
}
