

import java.util.Hashtable;

public class Parsing {
    /**
     * Reads string and splits every time there is a comma `,`. 
     * The limit of split is 4. 
     * @param str (String): string that needs to be split
     */
    public static String[] split(String str) {
        /**
         * Every time there is a comma, the string is split. 
         * There are 4 splits. 
         * Each section after the comma is stored in the array
         */
        return (str.split(",", 4));
    }

    public static void main(String args[]) {
        String str = "ABR,Aberdeen Regional Airport,Aberdeen,SD";
        
        for (String word : split(str)) 
			System.out.println(word);
    }
}
