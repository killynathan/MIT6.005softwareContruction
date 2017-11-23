package expressivo;

/*
 * Abstraction Function:
 *  Represents a scalar value of value
 *  
 * Rep Invariant:
 * 
 * Safety from rep exposure:
 *  value is private and final and never returned
 *  the constructor takes in a double which is passed by copy
 *  equals takes in an Object which is never changed or assigned to instance variables
 */

public class Scalar implements Expression {
    private final double value;
    
    public Scalar(double value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return Double.toString(value);
    }
    
    @Override
    public boolean equals(Object that) {
        if (!(that instanceof Scalar)) return false;
        Scalar thatScalar = (Scalar) that;
        return this.value == thatScalar.value;
    }
    
    @Override
    public int hashCode() {
        return (int) value;
    }
}
