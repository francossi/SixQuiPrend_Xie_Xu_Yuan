package com.example.sixquiprend_xie_xu_yuan;

import java.util.ArrayList;

public class Game {
    private ArrayList<Player> players;
    private Board board;
    private Deck deck;  // 假设这是一个包含所有卡牌的牌堆

    public Game() {
        // 初始化players，board和deck
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
