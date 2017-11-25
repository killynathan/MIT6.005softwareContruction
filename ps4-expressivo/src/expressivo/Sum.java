package expressivo;

import java.util.Map;

/*
 * Abstraction function:
 *  represents an expression of e1 + e2
 *  
 * Rep invariant:
 * 
 * Safety from rep exposure:
 *  Expressions are immutable
 *  constructor takes in two Expressions, which are immutable
 *  equals takes in an Object which is never changed or assigned to instance variables
 */

public class Sum implements Expression {
    private final Expression e1;
    private final Expression e2;
    
    public Sum(Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
    }
    
    public Expression differentiate(String var) {
        return new Sum(e1.differentiate(var), e2.differentiate(var));
    }
    
    public Expression simplify(Map<String, Double> env) {
        Expression simplified_e1 = e1.simplify(env);
        Expression simplified_e2 = e2.simplify(env);
        if (isDouble(simplified_e1.toString()) && isDouble(simplified_e2.toString())) {
            return new Scalar(Double.parseDouble(simplified_e1.toString()) 
                            + Double.parseDouble(simplified_e2.toString()));
        } else {
            return new Sum(simplified_e1, simplified_e2);
        }
    }
    
    /**
     * 
     * @param s string
     * @return true if s is a string consisting of only a double. false otherwise
     */
    private boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }
    
    public String toString() {
        return "(" + e1.toString() + " + " + e2.toString() + ")";
    }
    
    @Override
    public boolean equals(Object that) {
        if (!(that instanceof Sum)) return false;
        Sum thatSum = (Sum) that;
        if (!this.e1.equals(thatSum.e1)) return false;
        if (!this.e2.equals(thatSum.e2)) return false;
        return true;
    }
    
    @Override
    public int hashCode() {
        return e1.hashCode() + e2.hashCode();
    }
}
