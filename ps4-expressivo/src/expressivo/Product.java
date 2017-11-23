package expressivo;

/*
 * Abstraction function:
 *  represents an expression of e1 * e2
 *  
 * Rep invariant:
 * 
 * Safety from rep exposure:
 *  Expressions are immutable
 *  constructor takes in two Expressions, which are immutable
 *  equals takes in an Object which is never changed or assigned to instance variables
 */

public class Product implements Expression {
    private final Expression e1;
    private final Expression e2;
    
    public Product(Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
    }
    
    @Override
    public String toString() {
//        return "(" + e1.toString() + ") * (" + e2.toString() + ")";
        return e1.toString() + " * " + e2.toString();
    }
    
    @Override
    public boolean equals(Object that) {
        if (!(that instanceof Product)) return false;
        Product thatProduct = (Product) that;
        if (!this.e1.equals(thatProduct.e2)) return false;
        if (!this.e2.equals(thatProduct.e2)) return false;
        return true;
    }
    
    @Override
    public int hashCode() {
        return e1.hashCode() + e2.hashCode();
    }
}
