package com.example.sixquiprend_xie_xu_yuan;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private ArrayList<Player> players;
    private Board board;
    private Deck deck;  // 假设这是一个包含所有卡牌的牌堆

    public Game() {
        // 初始化players，board和deck
        players = new ArrayList<>();
        // 初始化玩家、游戏板和牌堆
        deck = new Deck();
        deck.shuffle();
        initializePlayers();
        initializeBoard();
    }
    private void initializePlayers() {
        // 创建10名玩家
        for (int i = 0; i < 10; i++) {
            Player player = new Player("Player " + (i + 1));
            players.add(player);
        }

        // 发放10张卡牌给每个玩家
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < players.size(); j++) {
                Card card = deck.deal();
                players.get(j).receiveCard(card);
            }
        }
    }


    private void initializeBoard() {
        // 从牌堆中抽取4张卡牌放到游戏板的第一列
        for (int i = 0; i < 4; i++) {
            Card card = deck.deal();
            board.placeCard(card, i);
        }
    }
    public List<Player> getPlayers() {
        return players;
    }

    public Board getBoard() {
        return board;
    }

    public void start() {
        for (int round = 0; round < 10; round++) {
            playRound();

            // 计算每个玩家的得分
            for (Player player : players) {
                for (Card card : player.getHand()) {
                    player.addScore(card.getBullheads());
                }
            }

            // 清空游戏板并重新发牌
            board.clear();
            dealCards();
        }

        // 显示每个玩家的最终得分
        for (Player player : players) {
            System.out.println(player.getName() + ": " + player.getScore());
        }
    }

    private void playRound() {
        // 在这里实现每一轮的游戏逻辑
    }

    private void dealCards() {
        // 在这里实现发牌的逻辑

    }
}
