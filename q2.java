import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class q2 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter Paragraph: ");
        String paragraph = input.nextLine();
        System.out.print("Enter strings: ");
        String[] wordsToBeReplaced = input.nextLine().split(" ");
        input.close();
        for (String word : wordsToBeReplaced) {
            Pattern p = Pattern.compile("\\b" + word + "\\b");
            Matcher m = p.matcher(paragraph);
            if (m.find()) {
                StringBuilder newString = new StringBuilder();
                newString.append(word.charAt(0));
                for (int i = 1; i < word.length(); i++) {
                    newString.append('*');
                }
                paragraph = m.replaceAll(newString.toString());
            }
        }
        System.out.println("Updated Paragraph: " + paragraph);
    }
}