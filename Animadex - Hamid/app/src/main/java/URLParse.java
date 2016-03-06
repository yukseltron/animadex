import java.net.*;
import java.io.*;
import java.util.Scanner;

class URLParse
{
    public static final String base_url = "http://40.76.35.232/find/";

    public static String get_json_string(String image_url)
    {
        try {
            URL url = new URL(base_url + image_url);
            Scanner s = new Scanner(url.openStream());
            return s.nextLine();
        }
        catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void main(String args[])
    {
        String meerkat = "https://upload.wikimedia.org/wikipedia/commons/9/9a/Meerkat_(Suricata_suricatta)_Tswalu.jpg";
        String json = get_json_string(meerkat);
        System.out.println(json);
    }
}