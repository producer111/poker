package com.poker.texasapp.model;

public class Card {
    public enum Suit {
        HEARTS, DIAMONDS, CLUBS, SPADES
    }

    public enum Rank {
        TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8),
        NINE(9), TEN(10), JACK(11), QUEEN(12), KING(13), ACE(14);

        private final int value;

        Rank(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private final Suit suit;
    private final Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    public static Card parse(String input) {
        input = input.toUpperCase();
        String[] parts = input.split("-");
        if (parts.length != 2) throw new IllegalArgumentException("形式エラー: " + input);

        Suit suit;
        switch (parts[0]) {
            case "S": suit = Suit.SPADES; break;
            case "H": suit = Suit.HEARTS; break;
            case "D": suit = Suit.DIAMONDS; break;
            case "C": suit = Suit.CLUBS; break;
            default: throw new IllegalArgumentException("スート不明: " + parts[0]);
        }

        Rank rank;
        switch (parts[1]) {
            case "2": rank = Rank.TWO; break;
            case "3": rank = Rank.THREE; break;
            case "4": rank = Rank.FOUR; break;
            case "5": rank = Rank.FIVE; break;
            case "6": rank = Rank.SIX; break;
            case "7": rank = Rank.SEVEN; break;
            case "8": rank = Rank.EIGHT; break;
            case "9": rank = Rank.NINE; break;
            case "10": rank = Rank.TEN; break;
            case "J": rank = Rank.JACK; break;
            case "Q": rank = Rank.QUEEN; break;
            case "K": rank = Rank.KING; break;
            case "A": rank = Rank.ACE; break;
            default: throw new IllegalArgumentException("ランク不明: " + parts[1]);
        }

        return new Card(suit, rank);
    }

    @Override
    public String toString() {
        return suit.name().charAt(0) + "-" + rank.name();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Card)) return false;
        Card other = (Card) obj;
        return this.suit == other.suit && this.rank == other.rank;
    }

    @Override
    public int hashCode() {
        return suit.hashCode() * 31 + rank.hashCode();
    }
}