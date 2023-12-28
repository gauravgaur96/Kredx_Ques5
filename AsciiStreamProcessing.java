import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sproc {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Sproc <command>");
            return;
        }

        String command = args[0];

        switch (command) {
            case "search":
                if (args.length < 2) {
                    System.out.println("Usage: java Sproc search <search_string>");
                    return;
                }
                search(System.in, args[1]);
                break;
            case "update":
                if (args.length < 4) {
                    System.out.println("Usage: java Sproc update <start_range> <end_range> <update_string>");
                    return;
                }
                update(System.in, args[1], args[2], args[3]);
                break;
            default:
                System.out.println("Invalid command");
        }
    }

    private static void search(java.io.InputStream inputStream, String searchString) {
        Scanner scanner = new Scanner(inputStream);
        int lineNum = 1;
        List<String> rangeStrings = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Pattern pattern = Pattern.compile(searchString);
            Matcher matcher = pattern.matcher(line);

            while (matcher.find()) {
                int startCol = matcher.start() + 1;
                int endCol = matcher.end();
                rangeStrings.add(lineNum + "," + startCol + ":" + lineNum + "," + endCol);
            }

            lineNum++;
        }

        for (String range : rangeStrings) {
            System.out.println(range);
        }
    }

    private static void update(java.io.InputStream inputStream, String startRange, String endRange, String updateString) {
        Scanner scanner = new Scanner(inputStream);
        int lineNum = 1;
        StringBuilder result = new StringBuilder();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (lineNum >= Integer.parseInt(startRange.split(",")[0]) && lineNum <= Integer.parseInt(endRange.split(",")[0])) {
                int startCol = Integer.parseInt(startRange.split(",")[1]);
                int endCol = Integer.parseInt(endRange.split(",")[1]);
                int lineLength = line.length();

                if (startCol > lineLength || endCol > lineLength) {
                    System.err.println("Invalid range for line " + lineNum);
                    return;
                }

                result.append(line, 0, startCol - 1).append(updateString).append(line, endCol, line.length());
            } else {
                result.append(line);
            }

            if (lineNum < Integer.parseInt(endRange.split(",")[0])) {
                result.append("\n");
            }

            lineNum++;
        }

        System.out.println(result.toString());
    }
}
