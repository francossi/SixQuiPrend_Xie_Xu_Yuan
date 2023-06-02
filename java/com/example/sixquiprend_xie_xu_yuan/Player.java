package com.example.sixquiprend_xie_xu_yuan;

import java.util.ArrayList;

public class Player {
    private String name;
    private ArrayList<Card> hand;

    private int bullheads;

    private int score;
    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.score = 0;
        bullheads = 0;

    }

    // 接受一张卡牌
    public void receiveCard(Card card) {
        hand.add(card);
    }

    // 选择并移除一张卡牌
    public Card chooseCard(int index) {
        return hand.remove(index);
    }

    // 查看手中的卡牌
    public ArrayList<Card> getHand() {
        return hand;
    }

    // 获取玩家的名字
    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int score){
        this.score += score;
    }
    public int getBullheads() {
        return bullheads;
    }
    public void setBullheads(int bullheads) {
        this.bullheads = bullheads;
    }
}

