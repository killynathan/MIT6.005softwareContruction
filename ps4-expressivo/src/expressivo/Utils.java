package expressivo;

import java.util.ArrayList;

import expressivo.Expression.ExpressionGrammar;
import lib6005.parser.ParseTree;

public class Utils {
    
    public static Expression buildExpression(ParseTree<ExpressionGrammar> node) {
        switch(node.getName()) {
        case ROOT:
            return buildExpression(node.children().get(0));
        case POTENTIALLYWRAPPEDSUM:
        case POTENTIALLYWRAPPEDPRODUCT:
        case POTENTIALLYWRAPPEDPRIMITIVE:
        case PRIMITIVE:
            return buildExpression(getNonWhitespaceChildren(node).get(0));
        case NUMBER:
            return new Scalar(Double.parseDouble(node.getContents()));
        case VARIABLE:
            return new Variable(node.getContents());
        case SUM:
            boolean first = true;
            Expression result = null;
            for (ParseTree<ExpressionGrammar> child: getNonWhitespaceChildren(node)) {
                if (first) {
                    result = buildExpression(child);
                    first = false;
                } else {
                    result = new Sum(result, buildExpression(child));
                }
            }
            return result;
        case PRODUCT:
            boolean _first = true;
            Expression _result = null;
            for (ParseTree<ExpressionGrammar> child: getNonWhitespaceChildren(node)) {
                if (_first) {
                    _result = buildExpression(child);
                    _first = false;
                } else {
                    _result = new Product(_result, buildExpression(child));
                }
            }
            return _result;
        }
        throw new RuntimeException("You should never reach here: " + node);
            
    }
    
    /*
     * Gets the non-whitespace child of node.
     * Assumes node has a none-whitespace child
     */
    private static ArrayList<ParseTree<ExpressionGrammar>> getNonWhitespaceChildren(ParseTree<ExpressionGrammar> node) {
        ArrayList<ParseTree<ExpressionGrammar>> nonWhitespaceChildren = new ArrayList<ParseTree<ExpressionGrammar>>();
        for (ParseTree<ExpressionGrammar> child: node.children()) {
            if (child.getName() != ExpressionGrammar.WHITESPACE) {
                nonWhitespaceChildren.add(child);
            }
        }
        return nonWhitespaceChildren;
    }
}
