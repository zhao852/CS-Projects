import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatFilter {

    private String file1;

    public ChatFilter(String badWordsFileName) {
        file1 = badWordsFileName;
    }

    public String filter(String msg) {

        List<String> list = new ArrayList<>();
        File badwordstxt = new File(file1);
        try(BufferedReader br = new BufferedReader(new FileReader(badwordstxt))) {
            String line = "";
            while (line != null) {
                line = br.readLine();
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String x = "";
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.get(i).length(); j++) {
                x = x + "*";
            }
            msg = msg.replaceAll("(?i)" + list.get(i), x);
            x = "";
        }
        return msg;
    }
}