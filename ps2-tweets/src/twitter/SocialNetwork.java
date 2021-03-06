package twitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * SocialNetwork provides methods that operate on a social network.
 * 
 * A social network is represented by a Map<String, Set<String>> where map[A] is
 * the set of people that person A follows on Twitter, and all people are
 * represented by their Twitter usernames. Users can't follow themselves. If A
 * doesn't follow anybody, then map[A] may be the empty set, or A may not even exist
 * as a key in the map; this is true even if A is followed by other people in the network.
 * Twitter usernames are not case sensitive, so "ernie" is the same as "ERNie".
 * A username should appear at most once as a key in the map or in any given
 * map[A] set.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class SocialNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     * 
     * @param tweets
     *            a list of tweets providing the evidence, not modified by this
     *            method.
     * @return a social network (as defined above) in which Ernie follows Bert
     *         if and only if there is evidence for it in the given list of
     *         tweets.
     *         One kind of evidence that Ernie follows Bert is if Ernie
     *         @-mentions Bert in a tweet. This must be implemented. Other kinds
     *         of evidence may be used at the implementor's discretion.
     *         All the Twitter usernames in the returned social network must be
     *         either authors or @-mentions in the list of tweets.
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        Map<String, Set<String>> followsGraph = new HashMap<String, Set<String>>();
        for (Tweet t : tweets) {
            String uppercaseAuthor = t.getAuthor().toUpperCase();
            Set<String> mentions = Utils.getMentions(t.getText());
            mentions.remove(uppercaseAuthor); //authors cant mention themselves
            if (!followsGraph.containsKey(uppercaseAuthor)) {
                followsGraph.put(uppercaseAuthor, mentions);
            }
            else {
                followsGraph.get(uppercaseAuthor).addAll(mentions);
            }
        }
        return followsGraph;
    }

    /**
     * Find the people in a social network who have the greatest influence, in
     * the sense that they have the most followers.
     * 
     * @param followsGraph
     *            a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     */
    
    private static Map<String, Integer> getFollowerCounts(Map<String, Set<String>> followsGraph) {
        Map<String, Integer> followerCounts = new HashMap<String, Integer>();
        for (Map.Entry<String, Set<String>> entry : followsGraph.entrySet()) {
            String follower = entry.getKey().toUpperCase();
            Set<String> following = entry.getValue();
            // include an user just in case he never gets mentioned
            if (!followerCounts.containsKey(follower)) {
                followerCounts.put(follower, 0);
            }
            for (String user : following) {
                String uppercaseUser = user.toUpperCase();
                if (followerCounts.containsKey(uppercaseUser)) {
                    followerCounts.put(uppercaseUser, followerCounts.get(uppercaseUser) + 1);
                }
                else {
                    followerCounts.put(uppercaseUser, 1);
                }
            }
        }
        return followerCounts;
    }
    
    /*
     * followerCounts must be nonempty
     * values must be positive.
     */
    private static String getMostFollowedUser(Map<String, Integer> followerCounts) {
        int mostFollowers = -1;
        String mostFollowedUser = "";
        for (Map.Entry<String, Integer> entry : followerCounts.entrySet()) {
            String user = entry.getKey().toUpperCase();
            int numOfFollowers = entry.getValue();
            if (numOfFollowers > mostFollowers) {
                mostFollowers = numOfFollowers;
                mostFollowedUser = user;
            }
        }
        return mostFollowedUser;
    }
    
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        List<String> influenceList = new ArrayList<String>();
        Map<String, Integer> followerCounts = getFollowerCounts(followsGraph);
        // go through map finding the max value, adding it to list, and deleting that element
        while (!followerCounts.isEmpty()) {
            String mostFollowedUser = getMostFollowedUser(followerCounts);
            influenceList.add(mostFollowedUser);
            followerCounts.remove(mostFollowedUser);
        }
        System.out.println(influenceList);
        
        return influenceList;
    }

    /* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}
