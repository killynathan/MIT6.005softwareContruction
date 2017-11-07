package warmup;

import java.util.HashSet;
import java.util.Set;

public class Quadratic {

    /**
     * Check if a double is a whole number
     * @param a number you want to check
     * @return true if a is a whole number
     */
    public static boolean isWholeNumber(double a) {
        return a == (int) a;
    }
    
    /**
     * Find the integer roots of a quadratic equation, ax^2 + bx + c = 0.
     * @param a coefficient of x^2
     * @param b coefficient of x
     * @param c constant term.  Requires that a, b, and c are not ALL zero.
     * @return all integers x such that ax^2 + bx + c = 0.
     */
    public static Set<Integer> roots(int a, int b, int c) {
        long longA = (long) a;
        long longB = (long) b;
        long longC = (long) c;
        
        Set<Integer> roots = new HashSet<>();
        
        // if a is 0 we will divide by 0 so use formula x = - c / b
        if (a == 0) {
            long negativeC = longC * -1;
            double root = negativeC / longB;
            if (isWholeNumber(root)) 
                roots.add((int) root);
            return roots;
        }
        
        // get values
        long negativeB = longB * -1;
        double squareRoot = Math.sqrt(longB * longB - 4 * longA * longC);
        long twoA = 2 * longA;
        
        double rootFromPlus = (negativeB + squareRoot) / twoA;
        
        double rootFromMinus = (negativeB - squareRoot) / twoA;
        
        
        // add to set if integer (not real or complex)
        if (isWholeNumber(rootFromPlus))
            roots.add((int) rootFromPlus);
        if (isWholeNumber(rootFromMinus))
            roots.add((int) rootFromMinus);
        
        return roots;
    }

    
    /**
     * Main function of program.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("For the equation x^2 - 4x + 3 = 0, the possible solutions are:");
        Set<Integer> result = roots(1, -4, 3);
        System.out.println(result);
    }

    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}
