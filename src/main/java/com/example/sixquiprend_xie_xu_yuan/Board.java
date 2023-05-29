package com.example.sixquiprend_xie_xu_yuan;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private List<ArrayList<Card>> rows;

    public Board() {
        this.rows = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            rows.add(new ArrayList<>());
        }
    }

    // 添加一张卡牌到指定的行
    public void addCardToRow(int rowIndex, Card card) {
        rows.get(rowIndex).add(card);
    }

    // 检查一行是否已满（含有5张卡牌）
    public boolean isRowFull(int rowIndex) {
        return rows.get(rowIndex).size() == 5;
    }

    // 清空一行并返回该行的所有卡牌
    public ArrayList<Card> clearRow(int rowIndex) {
        ArrayList<Card> rowCards = new ArrayList<>(rows.get(rowIndex));
        rows.get(rowIndex).clear();
        return rowCards;
    }

    // 获取指定行的最后一张卡牌的数字
    public int getLastCardNumber(int rowIndex) {
        ArrayList<Card> row = rows.get(rowIndex);
        if (row.isEmpty()) {
            return 0;
        } else {
            return row.get(row.size() - 1).getNumber();
        }
    }

    // 获取所有行的信息
    public List<ArrayList<Card>> getRows() {
        return rows;
    }

    //查找适合放置一张特定卡牌的行
    public int findSuitableRow(Card card) {
        int suitableRowIndex = -1;
        int minDifference = Integer.MAX_VALUE;
        for (int i = 0; i < rows.size(); i++) {
            int lastCardNumber = getLastCardNumber(i);
            if (lastCardNumber < card.getNumber() && card.getNumber() - lastCardNumber < minDifference) {
                minDifference = card.getNumber() - lastCardNumber;
                suitableRowIndex = i;
            }
        }
        return suitableRowIndex;
    }

    //处理当一张卡牌放入一行后超过5张卡牌的情况
    public ArrayList<Card> addCardAndHandleOverflow(int rowIndex, Card card) {
        ArrayList<Card> row = rows.get(rowIndex);
        ArrayList<Card> takenCards = new ArrayList<>();
        if (row.size() == 5) {
            takenCards.addAll(row);
            row.clear();
        }
        row.add(card);
        return takenCards;
    }

    // 清空所有行
    public void clear() {
        for (ArrayList<Card> row : rows) {
            row.clear();
        }
    }
}
