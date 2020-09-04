import java.util.HashMap;
import java.util.Scanner;

public class q4 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter first string: ");
        String first = input.nextLine();
        System.out.print("Enter second string: ");
        String second = input.nextLine();
        input.close();

        HashMap<Character, Integer> mapFirst = new HashMap<Character, Integer>();
        for (Character c : first.toCharArray()) {
            mapFirst.put(c, 1 + mapFirst.getOrDefault(c, 0));
        }
        HashMap<Character, Integer> mapSecond = new HashMap<Character, Integer>();
        for (Character c : second.toCharArray()) {
            mapSecond.put(c, 1 + mapSecond.getOrDefault(c, 0));
        }
        System.out.print("Anagrams: " + mapFirst.equals(mapSecond));
    }
}