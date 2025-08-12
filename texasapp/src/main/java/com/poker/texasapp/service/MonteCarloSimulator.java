package com.poker.texasapp.service;

import java.util.ArrayList;
import java.util.List;

import com.poker.texasapp.model.Card;
import com.poker.texasapp.model.Deck;

public class MonteCarloSimulator {

    public static double calculateWinRate(
        List<Card> heroHand,
        List<Card> communityCards,
        int opponentCount,
        int simulationCount
    ) {
        int heroWins = 0;
        int ties = 0;

        for (int i = 0; i < simulationCount; i++) {
            Deck deck = new Deck();

            // 使用済みカードを除外
            for (Card c : heroHand) deck.removeCard(c);
            for (Card c : communityCards) deck.removeCard(c);

            // 相手プレイヤーの手札をランダムに生成
            List<List<Card>> opponentsHands = new ArrayList<>();
            for (int j = 0; j < opponentCount; j++) {
                List<Card> opp = new ArrayList<>();
                opp.add(deck.drawCard());
                opp.add(deck.drawCard());
                opponentsHands.add(opp);
            }

            // 残りの場カードを補完（最大5枚）
            List<Card> fullBoard = new ArrayList<>(communityCards);
            while (fullBoard.size() < 5) {
                fullBoard.add(deck.drawCard());
            }

            // 自分の役を評価
            List<Card> heroTotal = new ArrayList<>(heroHand);
            heroTotal.addAll(fullBoard);
            HandEvaluator.HandRank heroRank = HandEvaluator.evaluate(heroTotal);

            // 相手との比較
            boolean isTie = false;
            boolean heroBest = true;

            for (List<Card> oppHand : opponentsHands) {
                List<Card> oppTotal = new ArrayList<>(oppHand);
                oppTotal.addAll(fullBoard);
                HandEvaluator.HandRank oppRank = HandEvaluator.evaluate(oppTotal);

                int comp = heroRank.compareTo(oppRank);
                if (comp < 0) {
                    heroBest = false;
                    break;
                } else if (comp == 0) {
                    isTie = true;
                }
            }

            if (heroBest) {
                if (isTie) ties++;
                else heroWins++;
            }
        }

        return (heroWins + ties * 0.5) / simulationCount * 100;
    }
}