package com.poker.texasapp.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poker.texasapp.model.Card;
import com.poker.texasapp.model.Player;
import com.poker.texasapp.service.HandEvaluator;
import com.poker.texasapp.service.MonteCarloSimulator;

@Controller
public class PokerController {

	//最初の指定されていない画面に行く
    @GetMapping("/")
    public String showForm() {
        return "index";
    }
    ///calculateパスに接続したタイミングで処理がされる。
    @PostMapping("/calculate")
    public String calculate(
            @RequestParam String hand,
            @RequestParam(required = false) String board,
            @RequestParam String position,
            @RequestParam int chips,
            @RequestParam int opponents,
            @RequestParam String stage,
            Model model
    ) {
        try {
            List<Card> heroHand = parseCards(hand, 2);
            List<Card> boardCards = new ArrayList<>();
            List<Card> usedBoard = new ArrayList<>();
//Javaのスイッチ構文を活用
            switch (stage) {
                case "preflop":
                    usedBoard = new ArrayList<>();
                    break;
                case "flop":
                    boardCards = parseCards(board, 3, 5);
                    usedBoard = boardCards.subList(0, 3);
                    break;
                case "turn":
                    boardCards = parseCards(board, 4, 5);
                    usedBoard = boardCards.subList(0, 4);
                    break;
                case "river":
                default:
                    boardCards = parseCards(board, 5, 5);
                    usedBoard = boardCards;
                    break;
            }

            // ✅ 重複チェック
            Set<Card> cardSet = new HashSet<>();
            cardSet.addAll(heroHand);
            cardSet.addAll(usedBoard);

            if (cardSet.size() < heroHand.size() + usedBoard.size()) {
                throw new IllegalArgumentException("同じカードが手札または場に存在しています。");
            }

            Player player = new Player("You", position, chips);
            player.setHoleCards(heroHand.get(0), heroHand.get(1));

            List<Card> allCards = new ArrayList<>(heroHand);
            allCards.addAll(usedBoard);

            HandEvaluator.HandRank rank = HandEvaluator.evaluate(allCards);
            double winRate = MonteCarloSimulator.calculateWinRate(heroHand, usedBoard, opponents, 10000);

            model.addAttribute("player", player);
            model.addAttribute("hand", heroHand);
            model.addAttribute("board", usedBoard);
            model.addAttribute("rank", rank);
            model.addAttribute("winRate", String.format("%.2f", winRate));
            model.addAttribute("stage", stage);

            return "result";

        } catch (Exception e) {
            model.addAttribute("error", "入力エラー: " + e.getMessage());
            return "index";
        }
    }

    private List<Card> parseCards(String input, int min, int max) {
        List<Card> cards = new ArrayList<>();

        if (input != null && !input.trim().isEmpty()) {
            String[] tokens = input.trim().split("\\s+");

            for (String token : tokens) {
                token = token.trim();
                if (!token.isEmpty()) {
                    cards.add(Card.parse(token));
                }
            }
        }

        if (cards.size() < min || cards.size() > max) {
            throw new IllegalArgumentException("カードの枚数が不正です（" + min + "〜" + max + "枚）");
        }

        return cards;
    }

    private List<Card> parseCards(String input, int exact) {
        return parseCards(input, exact, exact);
    }
}
