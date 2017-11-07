package twitter;

import static org.junit.Assert.*;

/*
 * Testing Strategy
 * 
 * testGetTimespan
 *  partitions: 
 *      length of list: 0, 1, small, big
 *      range of timespan: 0, small, big
 *      date before epoch and after
 * 
 * test getMentionedUsers
 *  partitions:
 *      '', small text, large text
 *      no mentions
 *      duplicates
 *      @ prefixed valid
 *      @ prefixed invalid
 *      @ suffixed
 *      @ suffixed with bad characters
 */

import java.util.concurrent.ThreadLocalRandom;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {
    
    private static final Instant afterEpochDate1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant afterEpochDate2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant afterEpochDate3 = Instant.parse("2030-02-17T11:00:00Z");
    private static final Instant beforeEpochDate1 = Instant.parse("1960-02-17T12:00:00Z");
    
    private static final String noMentionsText = "is it reasonable to talk about rivest so much?";
    private static final String mentionsText = "filler @_This-is-working1 filler @second_and_last_mention";
    private static final String mentionsText2 = "filler @nate filler";
    private static final String duplicateMentionsText = "filler filler filler filler filler filler filler @nate filler filler filler filler filler filler filler filler filler filler @Nate @NATE";
    private static final String atPrefixedText = "filler !@good bad@bad filler";
    private static final String emptyText = "";
    private static final String incorrectUsernameText = "filler filler @good @!this-is-bad! filler";
    private static final String emptyAtText = "@ filler @ filler @";
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", noMentionsText, afterEpochDate1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", mentionsText, afterEpochDate2);
    private static final Tweet tweet3 = new Tweet(3, "omi", duplicateMentionsText, afterEpochDate3);
    private static final Tweet tweet4 = new Tweet(4, "nate", atPrefixedText, beforeEpochDate1);
    private static final Tweet tweet5 = new Tweet(5, "kou", emptyText, beforeEpochDate1);
    private static final Tweet tweet6 = new Tweet(6, "hi", incorrectUsernameText, beforeEpochDate1);
    private static final Tweet tweet7 = new Tweet(7, "hi", mentionsText2, beforeEpochDate1);
    private static final Tweet tweet8 = new Tweet(8, "hi", emptyAtText, beforeEpochDate1);
    private static final Tweet tweet9 = new Tweet(9, "hi", emptyAtText, Instant.MAX);
    private static final Tweet tweet10 = new Tweet(10, "hi", emptyAtText, Instant.MIN);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    /*
     * tests for getTimespan
     * variables to consider: # of tweets, instant dates
     */
    
    @Test
    public void testGetTimespanEmpty() {
        Timespan timespan = Extract.getTimespan(Arrays.asList());
        assertNotEquals(timespan, null);
    }
    
    @Test
    public void testGetTimespanOneTweet() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
        assertEquals("expected start", afterEpochDate1, timespan.getStart());
        assertEquals("expected end", afterEpochDate1, timespan.getEnd());
    }
    
    @Test
    public void testGetTimespanMultipleTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet2, tweet1, tweet3));
        
        assertEquals("expected start", afterEpochDate1, timespan.getStart());
        assertEquals("expected end", afterEpochDate3, timespan.getEnd());
    }
    
    @Test
    public void testGetTimespanMultipleTweetsAllSameDate() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet5, tweet6, tweet7));
        assertEquals("expected start", beforeEpochDate1, timespan.getStart());
        assertEquals("expected end", beforeEpochDate1, timespan.getEnd());
    }
    
    @Test
    public void testGetTimespanWithMaxAndMinInstants() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet9, tweet10));
        
        assertEquals("expected start", Instant.MIN, timespan.getStart());
        assertEquals("expected end", Instant.MAX, timespan.getEnd());
    }
    
    /*
     * tests getMentionedUsers
     */
    
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    private boolean containsIgnoreCase(Set<String> set, List<String> strings) {
        Set<String> uppercaseSet = new HashSet<String>();
        
        for (String s : set) {
            uppercaseSet.add(s.toUpperCase());
        }
        
        for (String s : strings) {
            if (!uppercaseSet.contains(s.toUpperCase())) {
                return false;
            }
        }
        return true;
    }
    
    @Test
    public void testGetMentionedUsersHasMentions() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1, tweet2, tweet7));
        
        assertEquals(mentionedUsers.size(), 3);
        assertTrue(containsIgnoreCase(mentionedUsers, Arrays.asList("_This-is-working1", "second_and_last_mention", "nate")));
    }
    
    @Test
    public void testGetMentionedUsersAtPrefixed() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet4));
        
        assertEquals(mentionedUsers.size(), 1);
        assertTrue(containsIgnoreCase(mentionedUsers, Arrays.asList("good")));
    }
    
    @Test
    public void testGetMentionedUsersDuplicates() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3));
        
        assertEquals(mentionedUsers.size(), 1);
        assertTrue(containsIgnoreCase(mentionedUsers, Arrays.asList("nate")));
    }
    
    @Test
    public void testGetMentionedUsersEmptyText() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet5));
        
        assertTrue(mentionedUsers.isEmpty());
    }
    
    @Test
    public void testGetMentionedUsersBadUsername() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet6));
        
        assertEquals(mentionedUsers.size(), 1);
        assertTrue(containsIgnoreCase(mentionedUsers, Arrays.asList("good")));
    }
    
    @Test
    public void testGetMentionedUsersEmptyAt() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet8));
        
        assertEquals(mentionedUsers.size(), 0);
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
