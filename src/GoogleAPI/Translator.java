//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package GoogleAPI;

import gtranslate.parsing.Parse;
import gtranslate.parsing.ParseTextDetect;
import gtranslate.parsing.ParseTextTranslate;
import gtranslate.text.Text;
import gtranslate.text.TextTranslate;

public class Translator {
    private static Translator translator;

    private Translator() {
    }

    public static synchronized Translator getInstance() {
        if (translator == null) {
            translator = new Translator();
        }

        return translator;
    }

    public void translate(TextTranslate textTranslate) {
        Parse parse = new ParseTextTranslate(textTranslate);
        parse.parse();
    }

    public String translate(String text, String languageInput, String languageOutput) {
        Text input = new Text(text, languageInput);
        TextTranslate textTranslate = new TextTranslate(input, languageOutput);
        Parse parse = new ParseTextTranslate(textTranslate);
        parse.parse();
        return textTranslate.getOutput().getText();
    }

    public String detect(String text) {
        Text input = new Text(text);
        Parse parse = new ParseTextDetect(input);
        parse.parse();
        return input.getLanguage();
    }
}
