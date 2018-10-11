import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Read_File {

    public static StringBuffer read(String filename) {

        StringBuffer text = new StringBuffer();

        try {
            Scanner in = new Scanner(new File(filename));
            while (in.hasNext())
                text.append(in.nextLine());
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        return text;
    }

}
