//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package GoogleAPI;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class Audio {
    private static Audio audio;
    private String rul = "https://translate.google.com/translate_tts?ie=UTF-8&tl=";
    private String rul1 = "en";
    private String rul2 = "-TR&client=tw-ob&q=";
    private Audio() {
    }

    public static synchronized Audio getInstance() {
        if (audio == null) {
            audio = new Audio();
        }

        return audio;
    }

    public InputStream getAudio(String text, String languageOutput) throws IOException {
        URL url = new URL(rul + rul1 + rul2+ text.replace(" ", "%20") + "&tl=" + languageOutput);
        URLConnection urlConn = url.openConnection();
        urlConn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        InputStream audioSrc = urlConn.getInputStream();
        return new BufferedInputStream(audioSrc);
    }

    public void play(InputStream sound) throws JavaLayerException {
        (new Player(sound)).play();
    }
}
