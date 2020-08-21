import java.util.HashMap;
import java.util.Scanner;

public class q1 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter String: ");
        String string = input.nextLine();
        System.out.print("Enter SubString: ");
        String subString = input.nextLine();
        input.close();
        if (string.length() < subString.length()) {
            System.out.println(0);
        } else {
            HashMap<Character, Integer> mapSubString = new HashMap<Character, Integer>();
            for (Character c : subString.toCharArray()) {
                mapSubString.put(c, 1 + mapSubString.getOrDefault(c, 0));
            }
            HashMap<Character, Integer> mapTemp = new HashMap<Character, Integer>();
            int start = 0, count = 0;
            for (int i = 0; i < string.length(); i++) {
                if (i >= subString.length()) {
                    mapTemp.put(string.charAt(start), mapTemp.get(string.charAt(start)) - 1);
                    if (mapTemp.get(string.charAt(start)) == 0) {
                        mapTemp.remove(string.charAt(start));
                    }
                    start++;
                }
                mapTemp.put(string.charAt(i), 1 + mapTemp.getOrDefault(string.charAt(i), 0));
                if (mapTemp.equals(mapSubString)) {
                    count++;
                }
            }
            System.out.println('\n' + "Number of times: " + count);

        }

    }
}

