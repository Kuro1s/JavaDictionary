package bin.Internet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
public class InternetConnected {
    public static boolean IsConnecting() throws InterruptedException, IOException {
 /*
        Process p1 = java.lang.Runtime.getRuntime().exec("ping -n 1 www.google.com");
        int returnVal = p1.waitFor();

 return  returnVal==0 ? true : false;
 */

        Socket sock = new Socket();
        InetSocketAddress addr = new InetSocketAddress("www.google.com", 80);

        try {
            sock.connect(addr, 1000);

            return true;
        } catch (Exception e) {

            return false;
        } finally {
            try {
                sock.close();
            } catch (Exception e) {

            }
        }
    }
}
