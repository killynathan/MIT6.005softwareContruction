package library;

import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * BigLibrary represents a large collection of books that might be held by a city or
 * university library system -- millions of books.
 * 
 * In particular, every operation needs to run faster than linear time (as a function of the number of books
 * in the library).
 */
public class BigLibrary implements Library {

    // TODO: safety from rep exposure argument
    
    /*
     * rep invariant:
     * abstraction function:
     *     the intersection of inLibrary and checkedOut is the empty set
     * abstraction function:
     *     represents the collection of books inLibrary union checkedOut,
     *     where if a book copy is in inLibrary then it is available,
     *     and if a copy is in checkedOut then it is checked out
     * safety from rep exposure:
     *     inLibrary and checkedOut are private and never returned.
     *     when returning a set from from either, I make a devensive copy.
     *     Book is immutable and BookCopy is handled.
     */
    
    private final Map<Book, HashSet<BookCopy>> inLibrary;
    private final Map<Book, HashSet<BookCopy>> checkedOut;
    
    
    public BigLibrary() {
        inLibrary = new HashMap<Book, HashSet<BookCopy>>();
        checkedOut = new HashMap<Book, HashSet<BookCopy>>();
    }
    
    // assert the rep invariant
    private void checkRep() {
        for (HashSet<BookCopy> copies : inLibrary.values()) {
            for (BookCopy bc : copies) {
                if (checkedOut.containsKey(bc.getBook()))
                    assertFalse(checkedOut.get(bc.getBook()).contains(bc));
            }
        }
    }
    
    private void addToLibrary(Map<Book, HashSet<BookCopy>> section, BookCopy copy) {
        if (!section.containsKey(copy.getBook())) {
            section.put(copy.getBook(),  new HashSet<BookCopy>());
        }
        section.get(copy.getBook()).add(copy);
    }

    @Override
    public BookCopy buy(Book book) {
        BookCopy newBookCopy = new BookCopy(book);
        addToLibrary(inLibrary, newBookCopy);
        checkRep();
        return newBookCopy;
    }
    
    @Override
    public void checkout(BookCopy copy) {
        Book book = copy.getBook();
        inLibrary.get(book).remove(copy);
        addToLibrary(checkedOut, copy);
        checkRep();
    }
    
    @Override
    public void checkin(BookCopy copy) {
        Book book = copy.getBook();
        addToLibrary(inLibrary, copy);
        checkedOut.get(book).remove(copy);
        checkRep();
    }
    
    @Override
    public Set<BookCopy> allCopies(Book book) {
        Set<BookCopy> matches = new HashSet<BookCopy>();
        if (inLibrary.containsKey(book)) matches.addAll(inLibrary.get(book));
        if (checkedOut.containsKey(book)) matches.addAll(checkedOut.get(book));
        return matches;
    }

    @Override
    public Set<BookCopy> availableCopies(Book book) {
        Set<BookCopy> matches = new HashSet<BookCopy>();
        if (inLibrary.containsKey(book)) matches.addAll(inLibrary.get(book));
        return matches;
    }
    
    @Override
    public boolean isAvailable(BookCopy copy) {
        if (inLibrary.containsKey(copy.getBook()))
            return inLibrary.get(copy.getBook()).contains(copy);
        return false;
    }
    
    @Override
    public List<Book> find(String query) {
        List<Book> matches = new ArrayList<Book>();
        for (HashSet<BookCopy> copies : inLibrary.values()) {
            for (BookCopy bc : copies) {
                Book book = bc.getBook();
                if (book.getTitle().contains(query) || book.getAuthors().contains(query)) {
                    if (!matches.contains(book)) {
                        matches.add(book);
                    }
                }
            }
        }
        for (HashSet<BookCopy> copies : checkedOut.values()) {
            for (BookCopy bc : copies) {
                Book book = bc.getBook();
                if (book.getTitle().contains(query) || book.getAuthors().contains(query)) {
                    if (!matches.contains(book)) {
                        matches.add(book);
                    }
                }
            }
        }
        Comparator<Book> comp = (Book a, Book b) -> {
            return b.getYear() - a.getYear();
        };
        matches.sort(comp);
        return matches;
        
    }
    
    @Override
    public void lose(BookCopy copy) {
        Book book = copy.getBook();
        if (inLibrary.containsKey(book)) inLibrary.get(book).remove(copy);
        if (checkedOut.containsKey(book))  checkedOut.get(book).remove(copy);
    }

    // uncomment the following methods if you need to implement equals and hashCode,
    // or delete them if you don't
    // @Override
    // public boolean equals(Object that) {
    //     throw new RuntimeException("not implemented yet");
    // }
    // 
    // @Override
    // public int hashCode() {
    //     throw new RuntimeException("not implemented yet");
    // }


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
