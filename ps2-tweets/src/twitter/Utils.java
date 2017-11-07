package twitter;

import java.util.HashSet;
import java.util.Set;

public class Utils {
    
    public static boolean isValidUsernameChar(char c) {
        if (c == '_' || c == '-') return true;
        else if (Character.isLetter(c)) return true;
        else if (Character.isDigit(c)) return true;
        else return false;
    }
    
    public static Set<String> getMentions(String text) {
        Set<String> mentions = new HashSet<String>();
        for (int index = text.indexOf('@'); index >= 0; index = text.indexOf('@', index + 1)) {
            String username = "";
            if (index == 0 || !isValidUsernameChar(text.charAt(index - 1))) { // check prefix of @
                int j = index + 1; // start with char after @ and find the username
                while (j < text.length() && isValidUsernameChar(text.charAt(j))) {
                    username += text.charAt(j);
                    j++;
                }
                // found a mention!!
                if (username != "") {
                    mentions.add(username.toUpperCase());
                }
            }
        }
        return mentions;
    }
    
}
