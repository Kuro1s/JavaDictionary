//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package gtranslate.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class WebUtils {
    public WebUtils() {
    }

    public static String source(String urlSite) {
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(urlSite);
            URLConnection urlConn = url.openConnection();
            urlConn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            Reader reader = new InputStreamReader(urlConn.getInputStream(), "utf-8");
            BufferedReader br = new BufferedReader(reader);

            int byteRead;
            while((byteRead = br.read()) != -1) {
                result.append((char)byteRead);
            }
        } catch (MalformedURLException var7) {
            var7.printStackTrace();
        } catch (IOException var8) {
            var8.printStackTrace();
        }

        return result.toString();
    }
}
