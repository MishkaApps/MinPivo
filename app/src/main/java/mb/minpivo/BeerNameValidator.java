package mb.minpivo;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by mbolg on 15.11.2017.
 */

public class BeerNameValidator {
    private static Character[] LATIN_CHARS = new Character[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static Character[] CYRILLIC_CHARS = new Character[]{'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я'};
    private static Character[] DIGITS = new Character[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',};
    private static Character[] OTHER_CHARS = new Character[]{'-', '\"', '\'', ',', ' '};

    public static boolean check(String name) {

        if (name.trim().length() == 0)
            return false;

        name = name.toLowerCase();
        ArrayList<Character> allowedLatinChars = new ArrayList<>(Arrays.asList(LATIN_CHARS));
        ArrayList<Character> allowedCyrillicChars = new ArrayList<>(Arrays.asList(CYRILLIC_CHARS));
        ArrayList<Character> allowedOtherChars = new ArrayList<>(Arrays.asList(OTHER_CHARS));
        ArrayList<Character> allowedDigits = new ArrayList<>(Arrays.asList(DIGITS));

        ArrayList<Character> allAllowedChars = new ArrayList<>();
        allAllowedChars.addAll(allowedLatinChars);
        allAllowedChars.addAll(allowedCyrillicChars);
        allAllowedChars.addAll(allowedOtherChars);
        allAllowedChars.addAll(allowedDigits);

        for (char ch : name.toCharArray()) {
            if (allAllowedChars.contains(ch))
                continue;
            return false;
        }

        return true;
    }
}
