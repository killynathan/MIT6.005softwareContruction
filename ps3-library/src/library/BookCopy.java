package library;

/**
 * BookCopy is a mutable type representing a particular copy of a book that is held in a library's
 * collection.
 */
public class BookCopy {

    // TODO: rep
    
    // TODO: rep invariant
    // TODO: abstraction function
    // TODO: safety from rep exposure argument
    
    /*
     * Rep invariant:
     * 
     * Abstraction function:
     *  represents a book copy with book and condition
     *  
     * Safety from Rep Exposure:
     *  Condition is an enum which is immutable so it 
     *  can be passed to and from BookCopy.
     *  Book is immutable so the same applies.
     *  Only way to modify BookCopy is through setCondition(),
     *  which is allowed.
     */
    
    private Condition condition;
    private Book book;
    
    public static enum Condition {
        GOOD, DAMAGED
    };
    
    /**
     * Make a new BookCopy, initially in good condition.
     * @param book the Book of which this is a copy
     */
    public BookCopy(Book book) {
        this.book = book;
        condition = Condition.GOOD;
        checkRep();
    }
    
    // assert the rep invariant
    private void checkRep() {
        
    }
    
    /**
     * @return the Book of which this is a copy
     */
    public Book getBook() {
        return book;
    }
    
    /**
     * @return the condition of this book copy
     */
    public Condition getCondition() {
        return condition;
    }

    /**
     * Set the condition of a book copy.  This typically happens when a book copy is returned and a librarian inspects it.
     * @param condition the latest condition of the book copy
     */
    public void setCondition(Condition condition) {
        this.condition = condition;
    }
    
    /**
     * @return human-readable representation of this book that includes book.toString()
     *    and the words "good" or "damaged" depending on its condition
     */
    public String toString() {
        return "" + book.toString() + " in " + condition.toString().toLowerCase() + " condition.";
    }

//     uncomment the following methods if you need to implement equals and hashCode,
//     or delete them if you don't
//    
//     @Override
//     public boolean equals(Object that) {
//         throw new RuntimeException("not implemented yet");
//     }
     
//     
//     @Override
//     public int hashCode() {
//         throw new RuntimeException("not implemented yet");
//     }


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
