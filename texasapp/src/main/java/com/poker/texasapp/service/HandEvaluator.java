package com.poker.texasapp.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.poker.texasapp.model.Card;
import com.poker.texasapp.model.Card.Rank;

public class HandEvaluator {

    public enum HandRank {
        HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE_OF_A_KIND,
        STRAIGHT, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH
    }

    public static HandRank evaluate(List<Card> cards) {
        if (cards.size() < 5) return HandRank.HIGH_CARD;

        boolean isFlush = isFlush(cards);
        boolean isStraight = isStraight(cards);

        if (isFlush && isStraight) return HandRank.STRAIGHT_FLUSH;
        if (hasOfAKind(cards, 4)) return HandRank.FOUR_OF_A_KIND;
        if (hasFullHouse(cards)) return HandRank.FULL_HOUSE;
        if (isFlush) return HandRank.FLUSH;
        if (isStraight) return HandRank.STRAIGHT;
        if (hasOfAKind(cards, 3)) return HandRank.THREE_OF_A_KIND;
        if (hasTwoPair(cards)) return HandRank.TWO_PAIR;
        if (hasOfAKind(cards, 2)) return HandRank.ONE_PAIR;

        return HandRank.HIGH_CARD;
    }

    private static boolean isFlush(List<Card> cards) {
        Map<Card.Suit, Integer> suitCount = new HashMap<>();
        for (Card c : cards) {
            suitCount.put(c.getSuit(), suitCount.getOrDefault(c.getSuit(), 0) + 1);
        }
        return suitCount.values().stream().anyMatch(count -> count >= 5);
    }

    private static boolean isStraight(List<Card> cards) {
        Set<Integer> values = new HashSet<>();
        for (Card c : cards) {
            int val = c.getRank().getValue();
            values.add(val);
            if (val == 14) values.add(1); // ACE を 1 としても追加
        }

        List<Integer> sorted = new ArrayList<>(values);
        Collections.sort(sorted);

        int consecutive = 1;
        for (int i = 1; i < sorted.size(); i++) {
            if (sorted.get(i) == sorted.get(i - 1) + 1) {
                consecutive++;
                if (consecutive >= 5) return true;
            } else {
                consecutive = 1;
            }
        }
        return false;
    }

    private static boolean hasOfAKind(List<Card> cards, int count) {
        Map<Rank, Integer> rankCount = new HashMap<>();
        for (Card c : cards) {
            rankCount.put(c.getRank(), rankCount.getOrDefault(c.getRank(), 0) + 1);
        }
        return rankCount.values().stream().anyMatch(v -> v == count);
    }

    private static boolean hasTwoPair(List<Card> cards) {
        Map<Rank, Integer> rankCount = new HashMap<>();
        for (Card c : cards) {
            rankCount.put(c.getRank(), rankCount.getOrDefault(c.getRank(), 0) + 1);
        }

        long pairs = rankCount.values().stream().filter(v -> v >= 2).count();
        return pairs >= 2;
    }

    private static boolean hasFullHouse(List<Card> cards) {
        boolean hasThree = false;
        boolean hasTwo = false;

        Map<Rank, Integer> rankCount = new HashMap<>();
        for (Card c : cards) {
            rankCount.put(c.getRank(), rankCount.getOrDefault(c.getRank(), 0) + 1);
        }

        for (int count : rankCount.values()) {
            if (count >= 3) hasThree = true;
            else if (count >= 2) hasTwo = true;
        }

        return hasThree && hasTwo;
    }
}
