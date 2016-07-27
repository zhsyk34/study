import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Archimedes on 2016/07/22.
 */
public class TestIO {
    public static void main(String[] args) throws IOException {
        InputStream inputStream = new BufferedInputStream(System.in);

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        System.out.println(inputStream);
        reader.close();
        System.out.println(inputStream);
    }
}
