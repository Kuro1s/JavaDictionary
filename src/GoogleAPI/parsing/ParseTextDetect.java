//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package gtranslate.parsing;

import gtranslate.text.Text;
import gtranslate.utils.WebUtils;

public class ParseTextDetect implements Parse {
    private StringBuilder url;
    private Text input;

    public ParseTextDetect(Text input) {
        this.input = input;
    }

    public void appendURL() {
        this.url = new StringBuilder("http://www.google.com/uds/GlangDetect?");
        this.url.append("v=1.0&");
        this.url.append("q=" + this.input.getLanguage().replace(" ", "%20"));
    }

    public void parse() {
        this.appendURL();
        String result = WebUtils.source(this.url.toString());
        this.input.setLanguage(result.split(",")[0].split(":")[2].replace("\"", ""));
    }
}
