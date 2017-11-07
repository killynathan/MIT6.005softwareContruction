package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;


public class FilterTest {
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T15:00:00Z");
    private static final Instant d4 = Instant.parse("2016-02-17T16:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "nate", "i dont like money nathan", d3);
    private static final Tweet tweet4 = new Tweet(4, "nate", "please work kou", d4);
    private static final Tweet tweet5 = new Tweet(5, "nate", "please work kouno", d4);
    private static final Tweet tweet6 = new Tweet(6, "NATE", "please work kouno", d4);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    /*
     *  partition
     *      no tweets
     *      multiple tweets
     *      
     *      no tweets by author
     *      some tweets by author
     *      all tweets by author
     *      
     *      must be case insensitive
     */
    
    @Test
    public void testWrittenByNoTweets() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(), "omi"); 
        assertEquals(0, writtenBy.size());
    }
    
    @Test
    public void testWrittenByMultipleTweetsNoResults() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3), "omi"); 
        assertEquals(0, writtenBy.size());
    }
    
    @Test
    public void testWrittenByMultipleTweetsSingleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");
        
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }
    
    @Test
    public void testWrittenByMultipleTweetsMultipleResults() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3, tweet4), "nate");
        
        assertEquals(2, writtenBy.size());
        assertEquals(tweet3, writtenBy.get(0));
        assertEquals(tweet4, writtenBy.get(1));
    }
    
    @Test
    public void testWrittenByMultipleTweetsAllResultsInOrder() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet3, tweet4), "nate");
        
        assertEquals(2, writtenBy.size());
        assertEquals(tweet3, writtenBy.get(0));
        assertEquals(tweet4, writtenBy.get(1));
    }
    
    @Test
    public void testWrittenByCaseInsensitive() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet6), "nate");
        assertTrue(writtenBy.size() == 1);
    }
    
    /*
     * partitions:
     *  no tweets
     *  multiple tweets
     *  
     *  no tweets in timespan
     *  some tweets in timespan
     *  all tweets in timespan
     *  
     *  timespan start = end
     */
    
    @Test
    public void testInTimespanNoTweets() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(), new Timespan(testStart, testEnd));
        
        assertEquals(inTimespan.size(), 0);
    }
    
    @Test
    public void testInTimespanMultipleTweetsNoResults() {
        Instant testStart = Instant.parse("2017-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2017-02-17T12:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3, tweet4), new Timespan(testStart, testEnd));
        
        assertEquals(inTimespan.size(), 0);
    }
    
    @Test
    public void testInTimespanMultipleTweetsMultipleResults() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));
        
        assertFalse("expected non-empty list", inTimespan.isEmpty());
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, inTimespan.indexOf(tweet1));
    }
    
    @Test
    public void testInTimespanMultipleTweetsAllResults() {
        Instant testStart = Instant.parse("2016-02-17T05:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T20:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3, tweet4), new Timespan(testStart, testEnd));
        
        assertEquals(4, inTimespan.size());
        assertEquals(tweet1, inTimespan.get(0));
        assertEquals(tweet2, inTimespan.get(1));
        assertEquals(tweet3, inTimespan.get(2));
        assertEquals(tweet4, inTimespan.get(3));
    }
    
    @Test
    public void testInTimespanZeroedTimespan() {
        Instant testStart = Instant.parse("2016-02-17T10:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T10:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3, tweet4), new Timespan(testStart, testEnd));
        
        assertEquals(1, inTimespan.size());
        assertEquals(tweet1, inTimespan.get(0));
    }
    
    /*
     * tests for containing
     * partitions:
     *  no tweets
     *  no words
     *  
     *  multiple tweets
     *  multiple words
     *  
     *  no matches
     *  some matches
     *  all matches
     * 
     *  found word but is a substring
     *  one tweet has multiple matches
     */
    
    private boolean testContainingHelper(List<Tweet> tweets, List<String> words, List<Tweet> matchingTweets) {
        List<Tweet> containing = Filter.containing(tweets, words);
        return containing.equals(matchingTweets);
    }
    
    @Test
    public void testContainingNoTweets() {
        assertTrue(testContainingHelper(Arrays.asList(), Arrays.asList("talk"), Arrays.asList()));
    }
    
    @Test
    public void testContainingNoWords() {
        assertTrue(testContainingHelper(Arrays.asList(tweet1, tweet2, tweet3), Arrays.asList(), Arrays.asList()));
    }
    
    @Test
    public void testContainingMultipleTweetsNoResults() {
        assertTrue(testContainingHelper(Arrays.asList(tweet1, tweet2, tweet3, tweet4), Arrays.asList("monster"), Arrays.asList()));
    }
    
    @Test
    public void testContainingMultipleTweetsSomeResults() {
        assertTrue(testContainingHelper(Arrays.asList(tweet1, tweet2, tweet3, tweet4), 
                Arrays.asList("nathan", "kou"), Arrays.asList(tweet3, tweet4)));
    }
    
    @Test
    public void testContaining() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"));
        
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, containing.indexOf(tweet1));
    }
    
    @Test
    public void testContainingWordInSubstring() {
        assertTrue(testContainingHelper(Arrays.asList(tweet5), Arrays.asList("kou"), Arrays.asList()));
    }
    
    @Test
    public void testContainingOneTweetMultipleMatches() {
        assertTrue(testContainingHelper(Arrays.asList(tweet5), Arrays.asList("please", "work"), Arrays.asList(tweet5)));
    }

    /*
     * Warning: all the tests you write here must be runnable against any Filter
     * class that follows the spec. It will be run against several staff
     * implementations of Filter, which will be done by overwriting
     * (temporarily) your version of Filter with the staff's version.
     * DO NOT strengthen the spec of Filter or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Filter, because that means you're testing a stronger
     * spec than Filter says. If you need such helper methods, define them in a
     * different class. If you only need them in this test class, then keep them
     * in this test class.
     */


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}
