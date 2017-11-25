/* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for the static methods of Commands.
 */
public class CommandsTest {

    /**
     * Testing strategy
     * differentiate():
     *  operation = sum, product, both
     *  values = scalars, variables
     */
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    /*
     * differentiate()
     */
    
    private void testDifferentiateHelper(String input, String var, String solution) {
        String output = Commands.differentiate(input, var);
        assertEquals(solution, output);
    }
    
    @Test
    public void testDifferentiateSum() {
        testDifferentiateHelper("x + x + y + 1", "x", "(((1.0 + 1.0) + 0.0) + 0.0)");
    }
    
    @Test
    public void testDifferentiateProduct() {
        testDifferentiateHelper("x * x * y * 3.0", "x", "(x * x * y * 0.0 + 3.0 * (x * x * 0.0 + y * (x * 1.0 + x * 1.0)))");
    }
    
    @Test
    public void testDifferentiateAll() {
        testDifferentiateHelper("x * x + 2 * x", "x", "((x * 1.0 + x * 1.0) + (2.0 * 1.0 + x * 0.0))");
    }
    
    
    // TODO tests for Commands.differentiate() and Commands.simplify()
    
}
