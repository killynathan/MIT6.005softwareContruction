package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SocialNetworkTest {

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "nate", "filler @omi filler @kou", d1);
    private static final Tweet tweet2 = new Tweet(2, "nate", "filler @killynathan filler", d1);
    private static final Tweet tweet3 = new Tweet(3, "omi", "filler", d1);
    private static final Tweet tweet4 = new Tweet(4, "chan", "filler @omi", d1);
    private static final Tweet tweet5 = new Tweet(5, "CHAN", "@killynathan", d1);
    private static final Tweet tweet6 = new Tweet(6, "CHAN", "@killynathan filler @chan filler @chan filler", d1);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    private static Set<String> setToLowercase(Set<String> set) {
        Set<String> result = new HashSet<String>();
        for (String str: set) {
            result.add(str.toLowerCase());
        }
        return result;
    }
    
    private static Map<String, Set<String>> graphToLowercase(Map<String, Set<String>> graph) {
        Map<String, Set<String>> result = new HashMap<String, Set<String>>();
        for (Map.Entry<String, Set<String>> entry : graph.entrySet()) {
            String lowercaseKey = (entry.getKey()).toLowerCase();
            Set<String> lowercaseValue = setToLowercase(entry.getValue());
            result.put(lowercaseKey, lowercaseValue);
        }
        return result;
    }

    private static boolean graphEqualsIgnoreCase(Map<String, Set<String>> graph1, Map<String, Set<String>> graph2) {
        Map<String, Set<String>> graph1Lowercase = graphToLowercase(graph1);
        Map<String, Set<String>> graph2Lowercase = graphToLowercase(graph2);
        return graph1Lowercase.equals(graph2Lowercase);
    }
    
    /*
     * testGuessFollowsGraph 
     * variables to consider: number of tweets, number of authors, number of mentions, number of mentioned
     * 
     * we will consider:
     *  empty tweets and multiple tweets
     *  one author and multiple authors
     *  no mentions and multiple mentions
     *  when testing mentions, we will only more than one authors as "multiple tweets from one author"
     *  is sufficient to cover one author.
     *  
     * partition:
     * empty tweets
     * multiple tweets from one author
     * multiple tweets from more than one authors
     * multiple tweets mentions no users
     * multiple tweets mentions more than one users
     * 
     * spcial cases:
     * same user different casings
     * containing own user
     */
    
    private static boolean testGuessFollowsGraphHelper(List<Tweet> tweets, Map<String, Set<String>> correctGraph) {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        return graphEqualsIgnoreCase(correctGraph, followsGraph);
    }
    
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    
    @Test
    public void testGuessFollowsGraphOneAuthorMultipleMentions() {
        Map<String, Set<String>> correctGraph = new HashMap<String, Set<String>>();
        correctGraph.put("nate", new HashSet<>(Arrays.asList("omi", "kou", "killynathan")));
        assertTrue(testGuessFollowsGraphHelper(Arrays.asList(tweet1, tweet2), correctGraph));
    }
    
    
    @Test
    public void testGuessFollowsGraphMultipleAuthorsMultipleMentions() {
        Map<String, Set<String>> correctGraph = new HashMap<String, Set<String>>();
        correctGraph.put("nate", new HashSet<>(Arrays.asList("killynathan")));
        correctGraph.put("chan", new HashSet<>(Arrays.asList("omi")));
        assertTrue(testGuessFollowsGraphHelper(Arrays.asList(tweet2, tweet4), correctGraph));
    }
    
    @Test
    public void testGuessFollowsGraphOneAuthorCasedDifferently() {
        Map<String, Set<String>> correctGraph = new HashMap<String, Set<String>>();
        correctGraph.put("chan", new HashSet<>(Arrays.asList("omi","killynathan")));
        assertTrue(testGuessFollowsGraphHelper(Arrays.asList(tweet4, tweet5), correctGraph));
    }
    
    @Test
    public void testGuessFollowsGraphMentionsThemselves() {
        Map<String, Set<String>> correctGraph = new HashMap<String, Set<String>>();
        correctGraph.put("chan", new HashSet<>(Arrays.asList("killynathan")));
        assertTrue(testGuessFollowsGraphHelper(Arrays.asList(tweet6), correctGraph));
    }
    
    /*
     * testInfluencers
     * variables to consider: number of users, number of followers
     * 
     * special cases:
     *  includes users with no mentions that do the following
     */
    
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }
    
    @Test
    public void testInfluencersMultipleUsersMultipleMentions() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("nate", new HashSet<>(Arrays.asList("omi", "killynathan")));
        followsGraph.put("omi", new HashSet<>(Arrays.asList("killynathan")));
        followsGraph.put("killynathan", new HashSet<>(Arrays.asList("omi", "nate")));
        followsGraph.put("chan", new HashSet<>(Arrays.asList("killynathan")));
        List<String> influenceList = SocialNetwork.influencers(followsGraph);
        List<String> influenceListLowercase = new ArrayList<String>();
        for (String str : influenceList) {
            influenceListLowercase.add(str.toLowerCase());
        }
        assertTrue(influenceListLowercase.equals(Arrays.asList("killynathan", "omi", "nate", "chan")));
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
    
    public static void main(String[] args) {
        Set<String> test = new HashSet<String>(Arrays.asList("TEST", "testing123", "AbCd"));
        Set<String> test2 = new HashSet<String>(Arrays.asList("TEST", "TESTING123", "abcD"));
        Map<String, Set<String>> graph = new HashMap<String, Set<String>>();
        graph.put("nate", test);
        Map<String, Set<String>> graph2 = new HashMap<String, Set<String>>();
        graph2.put("NATE", test2);
        System.out.println(graphEqualsIgnoreCase(graph, graph2));
    }
    
}