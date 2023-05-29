package com.example.sixquiprend_xie_xu_yuan;

public class ComputerPlayer extends Player{
    public ComputerPlayer(String name) {
        super(name);


    }

    // 电脑玩家选择卡牌的策略
    public Card chooseCard(Board board) {
        
        // 这里只是一个简单的示例：选择第一张能放入游戏板的卡牌
        for (int i = 0; i < getHand().size(); i++) {
            Card card = getHand().get(i);
            if (board.findSuitableRow(card) != -1) {
                return chooseCard(i);
            }
        }
        // 如果没有找到能放入游戏板的卡牌，就选择第一张卡牌
        return chooseCard(0);
    }
}
