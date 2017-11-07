package library;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test suite for Library ADT.
 */
@RunWith(Parameterized.class)
public class LibraryTest {

    /*
     * Note: all the tests you write here must be runnable against any
     * Library class that follows the spec.  JUnit will automatically
     * run these tests against both SmallLibrary and BigLibrary.
     */

    /**
     * Implementation classes for the Library ADT.
     * JUnit runs this test suite once for each class name in the returned array.
     * @return array of Java class names, including their full package prefix
     */
    @Parameters(name="{0}")
    public static Object[] allImplementationClassNames() {
        return new Object[] { 
            "library.SmallLibrary", 
            "library.BigLibrary"
        }; 
    }

    /**
     * Implementation class being tested on this run of the test suite.
     * JUnit sets this variable automatically as it iterates through the array returned
     * by allImplementationClassNames.
     */
    @Parameter
    public String implementationClassName;    

    /**
     * @return a fresh instance of a Library, constructed from the implementation class specified
     * by implementationClassName.
     */
    public Library makeLibrary() {
        try {
            Class<?> cls = Class.forName(implementationClassName);
            return (Library) cls.newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    /*
     * Testing strategy
     * ==================
     * 
     * buy():
     *  # of total books = 0, n
     *  # of copies of book = 0, n
     * checkout():
     *  # of available copies (including itself) = 1, n
     *  # of checked out copies = 0, n
     * checkin():
     *  # of available copies = 0, n
     *  # of checked out copies = 1, n
     * isAvailable():
     *  # of available copies = 0, n
     *  # of checked out copies = 0, n
     * allCopies():
     *  # of available copies = 0, n
     *  # of checked out copies = 0, n
     * availableCopies():
     *  # of available copies = 0, n
     *  # of checked out copies = 0, n
     * find():
     *  # of checked out matches = 0, n
     *  # of available matches = 0, n
     *  same authors and title different date
     *  multiple copies with same book
     * lose():
     *  state of copy = available, checked out, lost
     */
    
    private Book book1 = new Book("a", Arrays.asList("red"), 2000);
    private Book book2 = new Book("filler b filler", Arrays.asList("blue"), 2001);
    private Book book3 = new Book("a", Arrays.asList("red"), 2002);
    private Book book4 = new Book("c", Arrays.asList("purple", "yellow"), 2002);
    
    // TODO: put JUnit @Test methods here that you developed from your testing strategy
    @Test
    public void testExampleTest() {
        Library library = makeLibrary();
        Book book = new Book("This Test Is Just An Example", Arrays.asList("You Should", "Replace It", "With Your Own Tests"), 1990);
        assertEquals(Collections.emptySet(), library.availableCopies(book));
    }
    
    @Test
    public void testCheckoutAndCheckinSingleCopy() {
        Library library = makeLibrary();
        BookCopy copy1 = library.buy(book1);
        assertTrue(library.isAvailable(copy1)); // (0, 1)
        library.checkout(copy1);
        assertFalse(library.isAvailable(copy1)); // (1, 0)
        library.checkin(copy1);
        assertTrue(library.isAvailable(copy1)); // (0, 1)
    }
    
    @Test
    public void testCheckoutAndCheckinMultipleCopies() {
        Library library = makeLibrary();
        BookCopy copy1a = library.buy(book1);
        BookCopy copy1b =  library.buy(book1);
        BookCopy copy2 = library.buy(book2);
        library.checkout(copy1a);
        assertTrue(library.isAvailable(copy1b));
        library.checkout(copy1b);
        library.checkout(copy2);
        assertFalse(library.isAvailable(copy1a));
        assertFalse(library.isAvailable(copy1b));
        assertFalse(library.isAvailable(copy2));
        library.checkin(copy1a);
        library.checkin(copy1b);
        library.checkin(copy2);
        assertTrue(library.isAvailable(copy1a));
        assertTrue(library.isAvailable(copy1b));
        assertTrue(library.isAvailable(copy2));
    }
    
    @Test
    public void testgetCopies() {
        Library library = makeLibrary();
        assertEquals(library.availableCopies(book1).size(), 0);
        assertEquals(library.allCopies(book1).size(), 0);
        BookCopy copy1a = library.buy(book1);
        BookCopy copy1b = library.buy(book1);
        BookCopy copy2 = library.buy(book2);
        
        Set<BookCopy> availableCopies = library.availableCopies(book1);
        Set<BookCopy> allCopies = library.allCopies(book1);
        assertTrue(availableCopies.containsAll(Arrays.asList(copy1a, copy1b)));
        assertTrue(allCopies.containsAll(Arrays.asList(copy1a, copy1b)));
        assertTrue(availableCopies.size() == 2);
        assertTrue(allCopies.size() == 2);
        
        library.checkout(copy1b);
        availableCopies = library.availableCopies(book1);
        allCopies = library.allCopies(book1);
        assertTrue(availableCopies.containsAll(Arrays.asList(copy1a)));
        assertTrue(allCopies.containsAll(Arrays.asList(copy1a, copy1b)));
        assertEquals(1, availableCopies.size());
        assertEquals(2, allCopies.size());
        
        library.checkout(copy1a);
        availableCopies = library.availableCopies(book1);
        allCopies = library.allCopies(book1);
        assertEquals(availableCopies.size(), 0);
        assertTrue(allCopies.containsAll(Arrays.asList(copy1a, copy1b)));
        assertTrue(allCopies.size() == 2);
    }
    
    
    @Test
    public void testFind() {
        Library library = makeLibrary();
        
        assertEquals(0, library.find("testing").size());
        
        BookCopy copy1 = library.buy(book1);
        BookCopy copy2 = library.buy(book2);
        BookCopy copy3 = library.buy(book3);
        BookCopy copy4 = library.buy(book4);
        
        List<Book> matches = library.find("b");
        assertEquals(Arrays.asList(book2), matches);
        
        matches = library.find("purple");
        assertEquals(Arrays.asList(book4), matches);
        
        matches = library.find("red");
        assertEquals(Arrays.asList(book3, book1), matches);
    }
    
    @Test
    public void testLose() {
        Library library = makeLibrary();
        BookCopy copy1 = library.buy(book1);
        library.lose(copy1);
        assertEquals(library.allCopies(book1).size(), 0);
        assertFalse(library.isAvailable(copy1));
        
        library.lose(copy1);
        assertEquals(library.allCopies(book1).size(), 0);
        assertFalse(library.isAvailable(copy1));
        
        BookCopy copy2 = library.buy(book2);
        library.checkout(copy2);
        library.lose(copy2);
        assertEquals(library.allCopies(book2).size(), 0);
        assertFalse(library.isAvailable(copy2));
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
