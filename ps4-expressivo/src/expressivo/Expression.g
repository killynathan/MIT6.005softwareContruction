/* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */

// grammar Expression;

/*
 *
 * You should make sure you have one rule that describes the entire input.
 * This is the "start rule". Below, "root" is the start rule.
 *
 * For more information, see the parsers reading.
 */
root ::= potentiallyWrappedSum | potentiallyWrappedProduct | potentiallyWrappedPrimitive;
@skip whitespace{
	potentiallyWrappedSum ::= sum | '(' sum ')' | '(' potentiallyWrappedSum ')';
	potentiallyWrappedProduct ::= product | '(' product ')' | '(' potentiallyWrappedProduct ')';
	potentiallyWrappedPrimitive ::= primitive | '(' primitive ')' | '(' potentiallyWrappedPrimitive ')';
	sum ::= (product | potentiallyWrappedPrimitive) ('+' (product | potentiallyWrappedPrimitive))+;
	product ::= potentiallyWrappedPrimitive ('*' potentiallyWrappedPrimitive)+;
	primitive ::=  '(' sum ')' | '(' product ')' | number | variable;
}
number ::= [0-9]* ('.' [0-9]+)?;
variable ::= [a-zA-Z]+;

whitespace ::= [ \t\r\n]+;
