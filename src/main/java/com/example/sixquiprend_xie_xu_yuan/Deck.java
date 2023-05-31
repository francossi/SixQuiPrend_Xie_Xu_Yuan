package com.example.sixquiprend_xie_xu_yuan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        // 初始化卡牌列表，添加所有卡牌到牌堆
        for (int i = 1; i <= 104; i++) {
            Card card = new Card(i);
            cards.add(card);
        }
    }

    // 洗牌
    public void shuffle() {
        Collections.shuffle(cards);
    }

    // 发牌
    public Card deal() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("牌堆中没有卡牌了");
        }
        return cards.remove(0);
    }

    // 获取剩余卡牌数量
    public int getRemainingCardsCount() {
        return cards.size();
    }

    // 判断牌堆是否为空
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    // 抽牌
    public Card draw() {
        return deal();
    }

}
