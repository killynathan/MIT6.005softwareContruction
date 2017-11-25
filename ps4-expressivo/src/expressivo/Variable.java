package expressivo;

import java.util.Map;

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
    
    public Expression differentiate(String var) {
        if (var.equals(name)) return new Scalar(1.0);
        else return new Scalar(0.0);
    }
    
    public Expression simplify(Map<String, Double> env) {
        if (env.containsKey(name)) {
            return new Scalar(env.get(name));
        }
        return this;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    @Override
    public boolean equals(Object that) {
        if (!(that instanceof Variable)) return false;
        Variable thatVariable = (Variable) that;
        return this.name.equals(thatVariable.name);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
