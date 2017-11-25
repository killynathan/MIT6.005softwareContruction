/* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

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
     *   differentiate():
     *      values = scalars, variables, all
     *      operations = sum, product, all
     *   simplify():
     *      values = scalars, variables, all
     *      operations = sum, product, all
     *      # of variables filled = none, some, all
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
    public void testParseIncompleteAddition() {
        testCatchException("1 +");
    }
    
    @Test
   public  void testParseIncompleteMultiplication() {
        testCatchException(" * bar");
    }
    
    @Test
    public void testParseNoOperator() {
        testCatchException("4 Foo");
    }
    
    /*
     * toString()/parse()
     */
    
    @Test
    public void testToStringAndParseDifferentSpacings() {
        testToStringAndParseHelper("1   +1", "(1.0 + 1.0)");
    }
    
    @Test
    public void testToStringAndParseManyParenthesis() {
        testToStringAndParseHelper("(((1+1)))", "(1.0 + 1.0)");
    }
    
    @Test
    public void testToStringAndParseAddition() {
        testToStringAndParseHelper("x + foo + 1 + 5", "(((x + foo) + 1.0) + 5.0)");
    }
    
    @Test
    public void testToStringAndParseMultiplication() {
        testToStringAndParseHelper("foo * 5 * y * 1", "foo * 5.0 * y * 1.0");
    }
    
    @Test
    public void testToStringAndParseAll() {
        testToStringAndParseHelper("foo + 6 * bar + 4", "((foo + 6.0 * bar) + 4.0)");
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
            //assertNotEquals(expr1.hashCode(), expr2.hashCode()); //hashcodes are allowd to be the same
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
        testEqualityHelper("1 + Foo + 7", "Foo + 1 + 7", false);
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
        testEqualityHelper("1.0 * Foo", "1 * Foo", true);
    }
    
    @Test
    public void testEqualityDifferentCasings() {
        testEqualityHelper("1 * Foo", "1 * foo", false);
    }
    
    /**
     * differentiate()
     */
    
    private void testDifferentiateHelper(String var, String input, String correctOutput) {
        Expression input_expr = Expression.parse(input);
        Expression derivative = input_expr.differentiate(var);
        Expression correct_expr = Expression.parse(correctOutput);
        assertEquals(derivative, correct_expr);
    }
    
    @Test
    public void testDifferentiateSum() {
        testDifferentiateHelper("x", "1 + x + x + Foo", "0.0 + 1.0 + 1.0 + 0.0");
    }
    
    @Test
    public void testDifferentiateProduct() {
        testDifferentiateHelper("x", "x * x * y * 3.0", "(x * x * y * 0.0 + 3.0 * (x * x * 0.0 + y * (x * 1.0 + x * 1.0)))");
    }
    
    @Test
    public void testDifferentiateAll() {
        testDifferentiateHelper("x", "x * x + 2 * x", "((x * 1.0 + x * 1.0) + (2.0 * 1.0 + x * 0.0))");
    }
    
    /**
     * simplify()
     */
    private void testSimplify(String input, String solution, Map<String, Double> env) {
        Expression input_expr = Expression.parse(input);
        Expression simplified_expr = input_expr.simplify(env);
        Expression solution_expr = Expression.parse(solution);
        assertEquals(solution_expr, simplified_expr);
    }
    
    @Test
    public void testSimplifyAllVariablesFilled() {
        Map<String, Double> env = new HashMap<String, Double>();
        env.put("x", 1.0);
        env.put("y", 2.0);
        
        Expression input_expr = Expression.parse("x + y + 10");
        Expression simplified_expr = input_expr.simplify(env);
        Scalar solution_expr = new Scalar(13.0);
        assertEquals(solution_expr, simplified_expr);
    }
    
    @Test
    public void testSimplifyPartiallyFilled() {
        Map<String, Double> env = new HashMap<String, Double>();
        env.put("x", 5.0);
        testSimplify("x + y * (5 + 2)", "(5.0 + y * 7.0)", env);
    }
    
    @Test
    public void testSimplifiedNoneFilled() {
        Map<String, Double> env = new HashMap<String, Double>();
        env.put("z", 5.0);
        testSimplify("x + y * (5 + 2)", "(x + y * 7.0)", env);
    }
}
