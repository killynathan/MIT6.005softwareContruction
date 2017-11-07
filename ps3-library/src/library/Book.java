package library;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

/**
 * Book is an immutable type representing an edition of a book -- not the physical object, 
 * but the combination of words and pictures that make up a book.  Each book is uniquely
 * identified by its title, author list, and publication year.  Alphabetic case and author 
 * order are significant, so a book written by "Fred" is different than a book written by "FRED".
 */
public class Book {
    
    private final String title;
    private final List<String> authors;
    private final int year;
    
    /*
     * Rep Invariant:
     *  Title must contain at least one non-space character.
     *  Year must be nonnegative.
     *  Authors must contain at least one valid name (at least
     *  one non-space character).
     *  
     * Abstraction Function:
     *  Represents a book with title, authors, and publication date.
     *  
     * Safety from Rep Exposure:
     *  All fields are private.
     *  Title and year are immutable.
     *  Authors is mutable so we will use defensive copying for
     *  the Book() constructor and getAuthors(). The elements 
     *  inside authors are Strings so they are immutable
     *  
     */
    
    /**
     * Make a Book.
     * @param title Title of the book. Must contain at least one non-space character.
     * @param authors Names of the authors of the book.  Must have at least one name, and each name must contain 
     * at least one non-space character.
     * @param year Year when this edition was published in the conventional (Common Era) calendar.  Must be nonnegative. 
     */
    public Book(String title, List<String> authors, int year) {
        this.title = title;
        this.authors = new ArrayList<String>(authors);
        this.year = year;
        checkRep();
    }
    
    // assert the rep invariant
    private void checkRep() {
        assertTrue(title != null && authors != null);
        assertTrue(title.trim().length() > 0);
        assertTrue(year >= 0);
        assertTrue(authors.size() > 0);
        for (String author : authors) {
            assertTrue(author.trim().length() > 0);
        }
    }
    
    /**
     * @return the title of this book
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * @return the authors of this book
     */
    public List<String> getAuthors() {
        return new ArrayList<String>(authors);
    }

    /**
     * @return the year that this book was published
     */
    public int getYear() {
        return year;
    }

    /**
     * @return human-readable representation of this book that includes its title,
     *    authors, and publication year
     */
    public String toString() {
        String bookString = title + " written by " + authors + " published " + year;
        checkRep();
        return bookString;
    }

    @Override
    public boolean equals(Object that) {
        if (that == null || !(that instanceof Book)) {
            return false;
        }
        boolean isEquals = true;
        Book otherBook = (Book)that;
        if (!this.title.equals(otherBook.getTitle())) isEquals = false;
        if (!this.authors.equals(otherBook.getAuthors())) isEquals = false;
        if (this.year != otherBook.getYear()) isEquals = false;
        checkRep();
        return isEquals;
    }
     
    @Override
    public int hashCode() {
        return title.hashCode() + year;
    }



    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
