package expressivo;

/*
 * Abstraction function:
 *  represents a variable of name name
 *  
 * Rep invarariant:
 * 
 * Safety from rep exposure:
 *  name is private and final and never returned
 *  the contructor takes in a string, which is immutable
 *  equals takes in an Object which is never changed or assigned to instance variables
 */

public class Variable implements Expression {
    private final String name;
    
    public Variable(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    @Override
    public boolean equals(Object that) {
        if (!(that instanceof Variable)) return false;
        Variable thatVariable = (Variable) that;
        return this.name == thatVariable.name;
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
