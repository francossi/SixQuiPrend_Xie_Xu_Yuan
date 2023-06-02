package com.example.sixquiprend_xie_xu_yuan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck {
    private List<Card> cards;
    private Random random;

    public Deck() {
        cards = new ArrayList<>();
        for (int i = 1; i <= 104; i++) {
            Card card = new Card(i);
            cards.add(card);
        }
        random = new Random();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card deal() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("牌堆中没有卡牌了");
        }
        int randomIndex = random.nextInt(cards.size());
        return cards.remove(randomIndex);
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
