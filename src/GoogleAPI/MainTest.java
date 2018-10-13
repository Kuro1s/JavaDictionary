package GoogleAPI;
import javazoom.jl.decoder.JavaLayerException;
import java.io.IOException;
import java.io.InputStream;

public class MainTest {
    public static void main(String[] args) throws IOException, JavaLayerException {
        Audio audio = Audio.getInstance();
        InputStream sound = audio.getAudio("hello i am hoang", Language.VIETNAMESE);
        audio.play(sound);
        String str =GoogleTranslate.translate("vi","en","xin chào");
        System.out.println(str);
    }
}
