package Dictionary;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class Dic {
    public Map<String,String> Data=new HashMap<>();
    public ArrayList<String> Word = new ArrayList();
    public void readData(String URL) {
        //TODO : lấy từ từ data file text
        try {

            BufferedReader reader = new BufferedReader(new FileReader(URL));
            String line, word, def;
            int wordsNum = 0;
            while ((line = reader.readLine()) != null) {
                //System.out.printf("%s\n----------------------\n", line);
                int index = line.indexOf("<html>");
                int index2 = line.indexOf("<ul>");

                if (index2 != -1 && index > index2) {
                    index = index2;
                }

                if (index != -1) {
                    word = line.substring(0, index);

                    word = word.trim();

                    //
                    // word = word.toLowerCase();

                    def = line.substring(index);

                    //def = "<html>" + def + "</html>";

                    Word.add(word);
                    Data.put(word, def);

                    wordsNum++;
                }
            }
            reader.close();

            System.out.println(wordsNum + " words");


        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }

    }
}
