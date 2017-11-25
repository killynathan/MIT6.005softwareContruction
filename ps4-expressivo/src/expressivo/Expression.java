package expressivo;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import lib6005.parser.*;

/**
 * An immutable data type representing a polynomial expression of:
 *   + and *
 *   nonnegative integers and floating-point numbers
 *   variables (case-sensitive nonempty strings of letters)
 * 
 * <p>PS1 instructions: this is a required ADT interface.
 * You MUST NOT change its name or package or the names or type signatures of existing methods.
 * You may, however, add additional methods, or strengthen the specs of existing methods.
 * Declare concrete variants of Expression in their own Java source files.
 */
public interface Expression {
    
    // Datatype definition
    // Expr = Scalar + Variable + Sum(Expr, Expr) + Product(Expr, Expr)
    
    enum ExpressionGrammar {ROOT, SUM, PRODUCT, PRIMITIVE, VARIABLE, NUMBER, WHITESPACE, 
        POTENTIALLYWRAPPEDSUM, POTENTIALLYWRAPPEDPRODUCT, POTENTIALLYWRAPPEDPRIMITIVE};
        
    /**
     * Parse an expression.
     * @param input expression to parse, as defined in the PS1 handout.
     * @return expression AST for the input
     * @throws IllegalArgumentException if the expression is invalid
     */
    public static Expression parse(String input) {
        File grammarFile = new File("./src/expressivo/Expression.g");
        try {
            Parser<ExpressionGrammar> parser = GrammarCompiler.compile(grammarFile, ExpressionGrammar.ROOT);
            ParseTree<ExpressionGrammar> tree = parser.parse(input);
            //tree.display();
            return Utils.buildExpression(tree);
        } catch (UnableToParseException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            throw new IllegalArgumentException("bad input: " + input);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            throw new IllegalArgumentException("bad input");
        }
    }
    
    /**
     * @param the variable to take the derivative in respect to
     * @return an Expression that is the derivative of the current Expression
     */
    public Expression differentiate(String var);
    
    /**
     * @param a map of the environment, where keys are variables and values are scalars.
     * @return a new Expression simplified with variables replaced if inside map
     */
    public Expression simplify(Map<String, Double> env);
    
    /**
     * @return a parsable representation of this expression, such that
     * for all e:Expression, e.equals(Expression.parse(e.toString())).
     */
    @Override 
    public String toString();

    /**
     * @param thatObject any object
     * @return true if and only if this and thatObject are structurally-equal
     * Expressions, as defined in the PS1 handout.
     */
    @Override
    public boolean equals(Object thatObject);
    
    /**
     * @return hash code value consistent with the equals() definition of structural
     * equality, such that for all e1,e2:Expression,
     *     e1.equals(e2) implies e1.hashCode() == e2.hashCode()
     */
    @Override
    public int hashCode();
    
    // TODO more instance methods
    
    /* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires permission of course staff.
     */
}
