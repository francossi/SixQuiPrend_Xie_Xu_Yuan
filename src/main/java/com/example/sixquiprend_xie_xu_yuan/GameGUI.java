package com.example.sixquiprend_xie_xu_yuan;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GameGUI extends Application {
    private List<List<Card>> middleCards; // 存储中间四行卡牌的数据结构
    private VBox middleCardRows; // 中间四行卡牌的容器
    private Player currentPlayer; // 当前玩家
    private HBox currentPlayerHandContainer; // 当前玩家手牌的容器
    HBox computerPlayerHandContainer;
    List<Player> players = createPlayers(); // 创建玩家列表
    private Label currentPlayerBullheadsLabel; //显示牛头数
    private Label computerPlayerBullheadsLabel;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        currentPlayerBullheadsLabel = new Label();
        computerPlayerBullheadsLabel = new Label();
        root.setLeft(currentPlayerBullheadsLabel);
        root.setRight(computerPlayerBullheadsLabel);
        List<Card> initialMiddleCards = new ArrayList<>(); // 存储中间的四行卡牌

        // 发放初始卡牌给玩家和中间的四行卡牌
        dealInitialCards(players, initialMiddleCards);
        middleCards = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            List<Card> row = new ArrayList<>();
            row.add(initialMiddleCards.get(i));
            middleCards.add(row);
        }
        currentPlayer = players.get(0);
        currentPlayerHandContainer = new HBox();
        root.setBottom(currentPlayerHandContainer);
        for (Card card : currentPlayer.getHand()) {
            Image cardImage = new Image(getClass().getResource("/com/example/sixquiprend_xie_xu_yuan/card/" + card.getNumber() + ".png").toExternalForm());
            ImageView imageView = new ImageView(cardImage);
            imageView.setOnMouseClicked(event -> {
                handleCardClick(card); // 处理点击事件
            });
            currentPlayerHandContainer.getChildren().add(imageView);
        }

        computerPlayerHandContainer = new HBox();
        root.setTop(computerPlayerHandContainer);

        Player computerPlayer = players.get(1); // Assuming the computer player is at index 1
        for (Card card : computerPlayer.getHand()) {
            ImageView backCardImageView = new ImageView(new Image(getClass().getResource("/com/example/sixquiprend_xie_xu_yuan/card/backside.png").toExternalForm()));
            computerPlayerHandContainer.getChildren().add(backCardImageView);
        }

        // 创建中间四行卡牌的容器
        middleCardRows = new VBox();
        initialMiddleCards.sort(Comparator.comparingInt(Card::getNumber));
        // 将中间的四行卡牌添加到容器中
        for (List<Card> rowCards : middleCards) {
            HBox rowContainer = new HBox();
            rowContainer.setSpacing(10);

            for (Card card : rowCards) {
                Image cardImage = new Image(getClass().getResource("/com/example/sixquiprend_xie_xu_yuan/card/" + card.getNumber() + ".png").toExternalForm());
                ImageView imageView = new ImageView(cardImage);
                rowContainer.getChildren().add(imageView);
            }

            middleCardRows.getChildren().add(rowContainer);
        }
        root.setCenter(middleCardRows);
        // 将中间四行卡牌的容器添加到游戏界面的适当位置
        Scene scene = new Scene(root, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void dealInitialCards(List<Player> players, List<Card> middleCards) {
        Deck deck = new Deck();
        deck.shuffle();

        for (Player player : players) {
            for (int i = 0; i < 10; i++) {
                Card card = deck.deal();
                player.receiveCard(card);
            }
        }

        for (int i = 0; i < 4; i++) {
            Card card = deck.deal();
            middleCards.add(card);
        }
    }


    private List<Player> createPlayers() {
        List<Player> players = new ArrayList<>();

        // 创建一个玩家
        Player humanPlayer = new Player("玩家1"); // 假设使用Player类，并传入玩家名称
        players.add(humanPlayer);

        // 创建四个电脑玩家
        for (int i = 2; i <= 5; i++) {
            Player computerPlayer = new Player("电脑" + i); // 假设使用Player类，并传入玩家名称
            players.add(computerPlayer);
        }
        return players;
    }
    // 处理卡牌点击事件

    private void handleCardClick(Card card) {


        // 根据选中的卡牌大小与中间四行每行末尾卡牌相比较，选择最接近的那一行放置卡牌
        int cardValue = card.getNumber();
        int closestRow = findClosestRow(cardValue);
        List<Card> closestRowCards = null; // 在这里声明 closestRowCards 变量

        // 确保 closestRow 有效
        if(closestRow != -1) {
            closestRowCards = middleCards.get(closestRow); // 在这里赋值
            closestRowCards.add(card);
            updateMiddleCardRows();

            // 从玩家手牌中删除选中的卡牌
            currentPlayer.getHand().remove(card);
            System.out.println("You have played a card: " + card.getNumber());
            updatePlayerHand(currentPlayer); // 更新玩家手牌的显示
        } else {
            System.out.println("No suitable row found for the card");
        }


            // 如果添加后的卡牌数超过5张，那么玩家需要拿走这行的卡牌
        if (closestRowCards != null && closestRowCards.size() > 5) {  // 注意这里检查了 closestRowCards 是否为 null
            // 保存第六张卡
            Card sixthCard = closestRowCards.get(5);

            // 这里取前5张卡
            for (int i = 0; i < 5; i++) {
                Card c = closestRowCards.get(i);
                currentPlayer.setBullheads(currentPlayer.getBullheads() + c.getBullheads());

            }
            closestRowCards.clear();  // 清空这行的卡牌

            // 把第六张卡作为新的第一张卡
            closestRowCards.add(sixthCard);
            Timeline timeline2 = new Timeline(new KeyFrame(Duration.seconds(1), event1 -> {
                updateMiddleCardRows();
                currentPlayerBullheadsLabel.setText(currentPlayer.getName() + "的牛头数: " + currentPlayer.getBullheads());
            }));
            timeline2.play();

        }

        // 检查是否游戏结束
        if (currentPlayer.getBullheads() > 66) {
            System.out.println("Game Over, " + currentPlayer.getName() + " lost!");
            System.exit(0);  // 退出程序
        }

        handleComputerPlayerTurn();

        if (currentPlayer.getHand().isEmpty()) {
            // 当前玩家手牌打完了，重新给他发10张牌
            Deck deck = new Deck();
            deck.shuffle();
            for (int i = 0; i < 10; i++) {
                if (deck.isEmpty()) {
                    System.out.println("牌堆中没有足够的牌了！");
                    break;
                }
                Card drawnCard = deck.deal();
                currentPlayer.receiveCard(drawnCard);
            }
            updatePlayerHand(currentPlayer); // 更新玩家手牌的显示
        }

    }

    // 处理电脑玩家回合
    private void handleComputerPlayerTurn() {
        Player computerPlayer = players.get(1); // 获取电脑玩家对象

        // 在电脑玩家手牌中随机选择一张卡牌
        Card selectedCard = computerPlayer.getHand().get((int) (Math.random() * computerPlayer.getHand().size()));

        // 将显示正面的牌添加到中间四行的相应位置
        int closestRow = findClosestRow(selectedCard.getNumber());
        List<Card> closestRowCards = null; // 在这里声明 closestRowCards 变量
        Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), event1 -> {
            updateMiddleCardRows();
            updatePlayerHand(computerPlayer);
        }));
        if (closestRow != -1) {
            closestRowCards = middleCards.get(closestRow); // 在这里赋值
            closestRowCards.add(selectedCard);
            timeline1.play();

            // 从电脑玩家手牌中移除选中的卡牌
            computerPlayer.getHand().remove(selectedCard);
            System.out.println("The computer have played a card : "+selectedCard.getNumber()+"\n");

        } else {
            System.out.println("No suitable row found for the card");
        }

        // 如果添加后的卡牌数超过5张，那么电脑需要拿走这行的卡牌
        if (closestRowCards != null && closestRowCards.size() > 5) {  // 注意这里检查了 closestRowCards 是否为 null
            // 保存第六张卡
            Card sixthCard = closestRowCards.get(5);

            // 这里取前5张卡
            for (int i = 0; i < 5; i++) {
                Card c = closestRowCards.get(i);
                computerPlayer.setBullheads(computerPlayer.getBullheads() + c.getBullheads());
            }
            closestRowCards.clear();  // 清空这行的卡牌

            // 把第六张卡作为新的第一张卡
            closestRowCards.add(sixthCard);
            Timeline timeline2 = new Timeline(new KeyFrame(Duration.seconds(5), event1 -> {
                updateMiddleCardRows();
                computerPlayerBullheadsLabel.setText(computerPlayer.getName() + "的牛头数: " + computerPlayer.getBullheads());
            }));
            timeline2.play();
        }


        // 检查是否游戏结束
        if (computerPlayer.getBullheads() > 66) {
            System.out.println("Game Over, " + computerPlayer.getName() + " lost!");
            System.exit(0);  // 退出程序
        }
        if (computerPlayer.getHand().isEmpty()) {
            // 当前玩家手牌打完了，重新给他发10张牌
            Deck deck = new Deck();
            deck.shuffle();
            for (int i = 0; i < 10; i++) {
                if (deck.isEmpty()) {
                    System.out.println("牌堆中没有足够的牌了！");
                    break;
                }
                Card drawnCard = deck.deal();
                computerPlayer.receiveCard(drawnCard);
            }
        }
    }


    private void updatePlayerHand(Player player) {
        HBox playerHandContainer;
        if (player == currentPlayer) {
            playerHandContainer = currentPlayerHandContainer;
        } else {
            playerHandContainer=computerPlayerHandContainer;
        }

        playerHandContainer.getChildren().clear(); // 清除原有的卡牌显示

        // 将玩家手中的卡牌添加到容器中
        if (player==currentPlayer){
            for (Card card : player.getHand()) {
                Image cardImage = new Image(getClass().getResource("/com/example/sixquiprend_xie_xu_yuan/card/" + card.getNumber() + ".png").toExternalForm());
                ImageView imageView = new ImageView(cardImage);
                imageView.setOnMouseClicked(event -> {
                    handleCardClick(card); // 处理点击事件
                });
                playerHandContainer.getChildren().add(imageView);
            }
        }else {
            for (Card card : player.getHand()) {
                Image cardImage = new Image(getClass().getResource("/com/example/sixquiprend_xie_xu_yuan/card/backside.png").toExternalForm());
                ImageView imageView = new ImageView(cardImage);
                playerHandContainer.getChildren().add(imageView);
            }
        }
    }

    // 查找与给定卡牌值最接近的行
    private int findClosestRow(int cardValue) {
        int closestRow = -1;
        int closestDiff = Integer.MAX_VALUE;
        for (int i = 0; i < middleCards.size(); i++) {
            List<Card> rowCards = middleCards.get(i);
            if (!rowCards.isEmpty()) {
                int lastCardValue = rowCards.get(rowCards.size() - 1).getNumber();
                int diff = Math.abs(cardValue - lastCardValue);
                if (diff < closestDiff) {
                    closestDiff = diff;
                    closestRow = i;
                }
            }
        }
        // 确保返回一个有效的行索引，如果没有找到最接近的行，则返回-1
        return closestRow;
    }


    // 更新中间四行卡牌的显示
    private void updateMiddleCardRows() {
        middleCardRows.getChildren().clear(); // 清除原有的卡牌显示

        // 对每一行的卡牌进行显示
        for (List<Card> rowCards : middleCards) {
            HBox rowContainer = new HBox();
            rowContainer.setSpacing(10);

            // 将卡牌添加到行容器中
            for (Card card : rowCards) {
                Image cardImage = new Image(getClass().getResource("/com/example/sixquiprend_xie_xu_yuan/card/" + card.getNumber() + ".png").toExternalForm());
                ImageView imageView = new ImageView(cardImage);
                rowContainer.getChildren().add(imageView);
            }
            middleCardRows.getChildren().add(rowContainer); // 添加行容器到中间四行卡牌的容器
        }
    }

}
