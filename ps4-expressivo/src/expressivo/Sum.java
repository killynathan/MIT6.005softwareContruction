package expressivo;

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
    
    public String toString() {
        return "(" + e1.toString() + " + " + e2.toString() + ")";
    }
    
    @Override
    public boolean equals(Object that) {
        if (!(that instanceof Sum)) return false;
        Sum thatSum = (Sum) that;
        if (!this.e1.equals(thatSum.e2)) return false;
        if (!this.e2.equals(thatSum.e2)) return false;
        return true;
    }
    
    @Override
    public int hashCode() {
        return e1.hashCode() + e2.hashCode();
    }
}
