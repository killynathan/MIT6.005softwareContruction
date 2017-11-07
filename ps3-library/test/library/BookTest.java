package library;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for Book ADT.
 */
public class BookTest {

    /*
     * Testing strategy
     * ==================
     * 
     * Book():
     *  title len = 1, n
     *  authors len = 1, n
     *  year = 0, n
     * getTitle():
     *  title len = 1, n
     * getAuthors():
     *  list len = 1, n
     * getYear():
     *  year = 0, n
     * toString():
     *  title len = 1, n
     *  authors len = 1, n
     *  year = 0, n
     */
    
    @Test
    public void testBookWithShortParams() {
        Book testBook = new Book("a", Arrays.asList("b"), 0);
        assertEquals("a", testBook.getTitle());
        assertEquals(Arrays.asList("b"), testBook.getAuthors());
        assertEquals(0, testBook.getYear());
        
        String bookString = testBook.toString();
        assertTrue(bookString.contains("a"));
        assertTrue(bookString.contains("b"));
        assertTrue(bookString.contains("0"));
    }
    
    @Test
    public void testBookWithLongParams() {
        Book testBook = new Book("The Life Of Nate", Arrays.asList("nate the great", "omi"), 9999);
        assertEquals("The Life Of Nate", testBook.getTitle());
        List<String> authors = testBook.getAuthors();
        assertEquals(2, authors.size());
        assertTrue(authors.containsAll(Arrays.asList("nate the great", "omi")));
        assertEquals(9999, testBook.getYear());
        
        String bookString = testBook.toString();
        assertTrue(bookString.contains("The Life Of Nate"));
        assertTrue(bookString.contains("nate the great"));
        assertTrue(bookString.contains("omi"));
        assertTrue(bookString.contains("9999"));
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
