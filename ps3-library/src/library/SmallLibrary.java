package library;

import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** 
 * SmallLibrary represents a small collection of books, like a single person's home collection.
 */
public class SmallLibrary implements Library {

    // This rep is required! 
    // Do not change the types of inLibrary or checkedOut, 
    // and don't add or remove any other fields.
    // (BigLibrary is where you can create your own rep for
    // a Library implementation.)

    // rep
    private Set<BookCopy> inLibrary;
    private Set<BookCopy> checkedOut;
    
    // rep invariant:
    //    the intersection of inLibrary and checkedOut is the empty set
    //
    // abstraction function:
    //    represents the collection of books inLibrary union checkedOut,
    //      where if a book copy is in inLibrary then it is available,
    //      and if a copy is in checkedOut then it is checked out

    // safety from rep exposure argument:
    //    inLibrary and checkedOut are never returned and are private. 
    //    Book is immutable so it can be freely passed in and out. 
    //    BookCopy is handled so it can be freely passed in and out.
    
    
    public SmallLibrary() {
        inLibrary = new HashSet<BookCopy>();
        checkedOut = new HashSet<BookCopy>();
        checkRep();
    }
    
    // assert the rep invariant
    private void checkRep() {
        for (BookCopy bc : inLibrary) {
            assertFalse(checkedOut.contains(bc));
        }
    }

    @Override
    public BookCopy buy(Book book) {
        BookCopy newBookCopy = new BookCopy(book);
        inLibrary.add(newBookCopy);
        checkRep();
        return newBookCopy;
    }
    
    @Override
    public void checkout(BookCopy copy) {
        inLibrary.remove(copy);
        checkedOut.add(copy);
        checkRep();
    }
    
    @Override
    public void checkin(BookCopy copy) {
        checkedOut.remove(copy);
        inLibrary.add(copy);
        checkRep();
    }
    
    @Override
    public boolean isAvailable(BookCopy copy) {
        return inLibrary.contains(copy);
    }
    
    @Override
    public Set<BookCopy> allCopies(Book book) {
        Set<BookCopy> copies = new HashSet<BookCopy>();
        for (BookCopy bc : inLibrary) {
            if (bc.getBook().equals(book)) {
                copies.add(bc);
            }
        }
        for (BookCopy bc : checkedOut) {
            if (bc.getBook().equals(book)) {
                copies.add(bc);
            }
        }
        checkRep();
        return copies;
    }
    
    @Override
    public Set<BookCopy> availableCopies(Book book) {
        Set<BookCopy> copies = new HashSet<BookCopy>();
        for (BookCopy bc : inLibrary) {
            if (bc.getBook().equals(book)) {
                copies.add(bc);
            }
        }
        checkRep();
        return copies;
    }
    
    

    @Override
    public List<Book> find(String query) {
        List<Book> matches = new ArrayList<Book>();
        for (BookCopy bc : inLibrary) {
            Book book = bc.getBook();
            if (book.getTitle().contains(query) || book.getAuthors().contains(query)) {
                if (!matches.contains(book)) {
                    matches.add(book);
                }
            }
        }
        for (BookCopy bc : checkedOut) {
            Book book = bc.getBook();
            if (book.getTitle().contains(query) || book.getAuthors().contains(query)) {
                if (!matches.contains(book)) {
                    matches.add(book);
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
        inLibrary.remove(copy);
        checkedOut.remove(copy);
        checkRep();
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
