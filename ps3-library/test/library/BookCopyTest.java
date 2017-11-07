package library;

import org.junit.Test;

import library.BookCopy.Condition;

import static org.junit.Assert.*;

import java.util.Arrays;

/**
 * Test suite for BookCopy ADT.
 */
public class BookCopyTest {

    /*
     * Testing strategy
     * ==================
     * 
     * BookCopy():
     *  book with short params
     *  book with long params
     * getBook():
     *  book with short params
     *  book with long params
     * getCondition():
     *  condition = good, damaged
     * setCondition():
     *  condition = good, damaged
     *  parameter = good, damaged
     *  we will just look at changing to diff condition or changing to same condition
     * toString():
     *  book with short params
     *  book with long params
     */
    
    // TODO: put JUnit @Test methods here that you developed from your testing strategy
    @Test
    public void testExampleTest() {
        Book book = new Book("This Test Is Just An Example", Arrays.asList("You Should", "Replace It", "With Your Own Tests"), 1990);
        BookCopy copy = new BookCopy(book);
        assertEquals(book, copy.getBook());
    }
    
    @Test
    public void testGetShortBook() {
        Book book = new Book("a", Arrays.asList("b"), 0);
        BookCopy copy = new BookCopy(book);
        assertEquals(book, copy.getBook());
        copy.setCondition(Condition.GOOD);
        assertEquals(copy.getCondition(), Condition.GOOD);
        
        String bookString = copy.toString();
        assertTrue(bookString.contains("a"));
        assertTrue(bookString.contains("b"));
        assertTrue(bookString.contains("0"));
    }
    
    @Test
    public void testGetLongBook() {
        Book book = new Book("the life of nate", Arrays.asList("a", "b"), 9999);
        BookCopy copy = new BookCopy(book);
        assertEquals(book, copy.getBook());
        copy.setCondition(Condition.DAMAGED);
        assertEquals(copy.getCondition(), Condition.DAMAGED);
        
        String bookString = copy.toString();
        assertTrue(bookString.contains("the life of nate"));
        assertTrue(bookString.contains("a"));
        assertTrue(bookString.contains("b"));
        assertTrue(bookString.contains("9999"));
    }
    
    @Test
    public void testGetConditionGood() {
        Book book = new Book("a", Arrays.asList("b"), 0);
        BookCopy copy = new BookCopy(book);
        copy.setCondition(Condition.GOOD);
        assertEquals(copy.getCondition(), Condition.GOOD);
    }
    
    @Test
    public void testGetConditionDamaged() {
        Book book = new Book("a", Arrays.asList("b"), 0);
        BookCopy copy = new BookCopy(book);
        copy.setCondition(Condition.DAMAGED);
        assertEquals(copy.getCondition(), Condition.DAMAGED);
    }
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
