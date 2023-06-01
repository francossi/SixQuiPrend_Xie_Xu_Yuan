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
import java.util.Scanner;

public class GUI3 extends Application {
    private List<List<Card>> middleCards; // 存储中间四行卡牌的数据结构
    private VBox middleCardRows; // 中间四行卡牌的容器
    private Player currentPlayer; // 当前玩家
    private HBox currentPlayerHandContainer; // 当前玩家手牌的容器
    private HBox computerPlayerHandContainer;
    private List<Player> players = createPlayers(); // 创建玩家列表
    private Label currentPlayerBullheadsLabel; // 显示当前玩家的牛头数
    private Label computerPlayerBullheadsLabel; // 显示电脑玩家的牛头数

    private Deck deck = new Deck(); // 创建一副牌

    @Override
    public void start(Stage primaryStage) {
        deck.shuffle();

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
        updatePlayerHand(currentPlayer);

        computerPlayerHandContainer = new HBox();
        root.setTop(computerPlayerHandContainer);
        Player computerPlayer = players.get(1); // 假设电脑玩家是列表中的第二个玩家
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

        handleComputerPlayerTurn(); // 让电脑玩家先出牌
    }

    private void dealInitialCards(List<Player> players, List<Card> middleCards) {
        int totalCards = players.size() * 10 + 4; // 总共需要的牌数（玩家手牌数 + 中间四行卡牌数）
        if (deck.getRemainingCardsCount() < totalCards) {
            System.out.println("牌堆中没有足够的牌了！");
            return;
        }

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

    // 处理玩家点击卡牌事件
    private void handleCardClick(Card card) {
        // 确保是当前玩家的回合
        if (currentPlayer != players.get(0)) {
            return;
        }

        int closestRow = findClosestRow(card.getNumber());

        List<Card> closestRowCards = null;
        // 确保 closestRow 有效
        if (closestRow != -1) {
            closestRowCards = middleCards.get(closestRow);
            closestRowCards.add(card);
            updateMiddleCardRows();
        } else {
            System.out.println("No suitable row found for the card");
            Scanner scanner = new Scanner(System.in);
            System.out.println("input number of row you want to add the card please!");
            int choice = scanner.nextInt();
            if (choice <= 4 && choice >= 0) {
                closestRowCards = middleCards.get(choice - 1);
                System.out.println(closestRowCards);
                closestRowCards.clear();
                closestRowCards.add(card);
                updateMiddleCardRows();
            } else {
                System.out.println("invalid input");
            }
        }

        // 从玩家手牌中删除选中的卡牌
        currentPlayer.getHand().remove(card);
        System.out.println("You have played a card: " + card.getNumber());
        updatePlayerHand(currentPlayer);

        // 如果添加后的卡牌数超过5张，玩家需要拿走这行的卡牌
        if (closestRowCards != null && closestRowCards.size() > 5) {
            Card sixthCard = closestRowCards.get(5);

            for (int i = 0; i < 5; i++) {
                Card c = closestRowCards.get(i);
                currentPlayer.setBullheads(currentPlayer.getBullheads() + c.getBullheads());
            }

            closestRowCards.clear();
            closestRowCards.add(sixthCard);
            updateMiddleCardRows();

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                currentPlayerBullheadsLabel.setText(currentPlayer.getName() + "的牛头数: " + currentPlayer.getBullheads());
            }));
            timeline.play();
        }

        // 检查是否游戏结束
        if (currentPlayer.getBullheads() > 66) {
            System.out.println("Game Over, " + currentPlayer.getName() + " lost!");
            System.exit(0);  // 退出程序
        }

        // 切换到电脑玩家的回合
        currentPlayer = players.get(1);
        handleComputerPlayerTurn();
    }

    // 处理电脑玩家回合
    private void handleComputerPlayerTurn() {
        // 确保是电脑玩家的回合
        if (currentPlayer == players.get(0)) {
            return;
        }

        Player computerPlayer = currentPlayer;

        // 随机选择一张牌
        Card selectedCard = computerPlayer.getHand().get((int) (Math.random() * computerPlayer.getHand().size()));

        // 查找与选中的牌值最接近的行
        int closestRow = findClosestRow(selectedCard.getNumber());

        List<Card> closestRowCards;
        // 确保 closestRow 有效
        if (closestRow != -1) {
            closestRowCards = middleCards.get(closestRow);
            closestRowCards.add(selectedCard);
            updateMiddleCardRows();
        } else {
            // 如果没有找到最接近的行，则随机选择一行
            closestRow = (int) (Math.random() * middleCards.size());
            closestRowCards = middleCards.get(closestRow);
            closestRowCards.clear();
            closestRowCards.add(selectedCard);
            updateMiddleCardRows();
        }

        // 从电脑玩家手牌中删除选中的卡牌
        computerPlayer.getHand().remove(selectedCard);
        System.out.println("The computer have played a card: " + selectedCard.getNumber());
        updatePlayerHand(computerPlayer);

        // 如果添加后的卡牌数超过5张，电脑玩家需要拿走这行的卡牌
        if (closestRowCards.size() > 5) {
            Card sixthCard = closestRowCards.get(5);

            for (int i = 0; i < 5; i++) {
                Card c = closestRowCards.get(i);
                computerPlayer.setBullheads(computerPlayer.getBullheads() + c.getBullheads());
            }

            closestRowCards.clear();
            closestRowCards.add(sixthCard);
            updateMiddleCardRows();

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                computerPlayerBullheadsLabel.setText(computerPlayer.getName() + "的牛头数: " + computerPlayer.getBullheads());
            }));
            timeline.play();
        }

        // 切换回玩家的回合
        currentPlayer = players.get(0);
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
        return closestRow;
    }

    // 更新玩家手牌的显示
    private void updatePlayerHand(Player player) {
        HBox playerHandContainer;
        if (player == players.get(0)) {
            playerHandContainer = currentPlayerHandContainer;
        } else {
            playerHandContainer = computerPlayerHandContainer;
        }

        playerHandContainer.getChildren().clear(); // 清除原有的卡牌显示

        // 将玩家手中的卡牌添加到容器中
        if (player == players.get(0)) {
            for (Card card : player.getHand()) {
                Image cardImage = new Image(getClass().getResource("/com/example/sixquiprend_xie_xu_yuan/card/" + card.getNumber() + ".png").toExternalForm());
                ImageView imageView = new ImageView(cardImage);
                imageView.setOnMouseClicked(event -> handleCardClick(card));
                playerHandContainer.getChildren().add(imageView);
            }
        } else {
            for (Card card : player.getHand()) {
                Image cardImage = new Image(getClass().getResource("/com/example/sixquiprend_xie_xu_yuan/card/backside.png").toExternalForm());
                ImageView imageView = new ImageView(cardImage);
                playerHandContainer.getChildren().add(imageView);
            }
        }
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

    public static void main(String[] args) {
        launch(args);
    }
}
