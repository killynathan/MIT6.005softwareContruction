/* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for the Expression abstract data type.
 */
public class ExpressionTest {

    /*
     * Testing strategy:
     *  empty, negative scalars
     *  parse():
     *      mixed spacing
     *      many parenthesis
     *      illegals:
     *          unmatched parenthesis
     *          incomplete *
     *          incomplete +
     *          no operator between primitives
     *  toString()/parse():
     *      adding only scalars
     *      adding scalars and variables
     *      multiplying only scalars
     *      multiplying scalars and variables
     *      adding and multiplying scalars and variables
     *      variables = one letter, more than one letter
     *  equals()/hashCode():
     *      different scalars
     *      different variable names
     *      different spacing
     *      different ordering
     *      different operators
     *      different groupings
     *      different decimal accuracy (1 vs 1.00)
     *      different variable name casings
     */
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    private void testToStringAndParseHelper(String input, String correctOutput) {
        Expression expr = Expression.parse(input);
        assertEquals(correctOutput, expr.toString());
    }
    
    private void testToStringAndParseHelper(String input) {
        Expression expr = Expression.parse(input);
        assertEquals(input, expr.toString());
    }
    
    private void testCatchException(String s) {
        Boolean caughtException = false;
        try {
            testToStringAndParseHelper(s);
        } catch(IllegalArgumentException ex) {
            caughtException = true;
        }
        assertTrue(caughtException);
    }
    
    /*
     * parse()
     */
    
    @Test
    public void testParseUnmatchedParenthesis() {
        testCatchException("(1 + Foo");
    }
    
    @Test 
    void testParseIncompleteAddition() {
        testCatchException("1 +");
    }
    
    @Test
    void testParseIncompleteMultiplication() {
        testCatchException(" * bar");
    }
    
    @Test
    void testParseNoOperator() {
        testCatchException("4 Foo");
    }
    
    /*
     * toString()/parse()
     */
    
    @Test
    public void testToStringAndParseDifferentSpacings() {
        testToStringAndParseHelper("1   +1", "(1 + 1)");
    }
    
    @Test
    public void testToStringAndParseManyParenthesis() {
        
    }
    
    @Test
    public void testToStringAndParseAddingScalars() {
        testToStringAndParseHelper("1 + 3 + 4", "((1 + 3) + 4)");
    }
    
    @Test
    public void testToStringAndParseAddingScalarsAndVariables() {
        testToStringAndParseHelper("x + foo + 1 + 5");
    }
    
    @Test
    public void testToStringAndParseMultiplyingScalars() {
        testToStringAndParseHelper("5 * 7 * 2");
    }
    
    @Test
    public void testToStringAndParseMultiplyingScalarsAndVariables() {
        testToStringAndParseHelper("foo * 5 * y * 1");
    }
    
    @Test
    public void testToStringAndParseAll() {
        testToStringAndParseHelper("foo + 6 * bar" + 4);
    }
    
    /*
     * equals()/hashCode()
     */
    
    private void testEqualityHelper(String s1, String s2, Boolean areEqual) {
        Expression expr1 = Expression.parse(s1);
        Expression expr2 = Expression.parse(s2);
        if (areEqual) {
            assertTrue(expr1.equals(expr2));
            assertEquals(expr1.hashCode(), expr2.hashCode());
        }
        else {
            assertFalse(expr1.equals(expr2));
            assertNotEquals(expr1.hashCode(), expr2.hashCode());
        }
    }
    
    @Test
    public void testEqualityDifferentScalars() {
        testEqualityHelper("2 + Foo", "1 + Foo", false);
    }
    
    @Test
    public void testEqualityDifferentVariableNames() {
        testEqualityHelper("1 + Foo", "1 + bar", false);
    }
    
    
    @Test
    public void testEqualityDifferentSpacings() {
        testEqualityHelper("1 +5+  Foo", "1 + 5 + Foo", true);
    }
    
    @Test
    public void testEqualityDifferentOrdering() {
        testEqualityHelper("1 + Foo + 7", "Foo + 1 + 7", true);
    }
    
    @Test
    public void testEqualityDifferentOperators() {
        testEqualityHelper("1 + Foo", "1 * Foo", false);
    }
    
    @Test
    public void testEqualityDifferentGroupings() {
        testEqualityHelper("1 + x", "(1 + x)", true);
    }
    
    @Test
    public void testEqualityDifferentDecimalAccuracy() {
        testEqualityHelper("1.0 + Foo", "1 + Foo", true);
    }
    
    @Test
    public void testEqualityDifferentCasings() {
        testEqualityHelper("1 + Foo", "1 + foo", false);
    }
}
