package com.poker.texasapp.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Card> holeCards; // 手札2枚
    private String position; // BTN, SB, BB, UTGなど
    private int chips; // チップ（スタック）

    public Player(String name, String position, int chips) {
        this.name = name;
        this.position = position;
        this.chips = chips;
        this.holeCards = new ArrayList<>();
    }

    public void setHoleCards(Card card1, Card card2) {
        holeCards.clear();
        holeCards.add(card1);
        holeCards.add(card2);
    }

    public List<Card> getHoleCards() {
        return new ArrayList<>(holeCards);
    }

    public String getPosition() {
        return position;
    }

    public int getChips() {
        return chips;
    }

    public void setChips(int chips) {
        this.chips = chips;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "[" + name + " (" + position + ") チップ: " + chips + " 手札: " + holeCards + "]";
    }
}