import java.util.Scanner;

/**
 * Created by Archimedes on 2016/07/22.
 */
public class TestConsole {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String line = scanner.nextLine();
            System.out.println(line);
            if ("exit".equals(line)) {
                break;
            }
        }
    }
}
