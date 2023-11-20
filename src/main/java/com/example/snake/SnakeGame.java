package com.example.snake;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class SnakeGame extends Application {
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 600;
    private static final int BLOCK_SIZE = 20;
    private static final int GAME_SPEED = 200_000_000;

    private Direction snakeDirection = Direction.RIGHT;
    private List<Rectangle> snake = new ArrayList<>();
    private Rectangle food;
    private boolean gameOver = false;
    private long lastUpdate = 0;

    @Override
    public void start(Stage primaryStage) {
        // Menu Scene
        VBox menuLayout = new VBox(10);
        Button playButton = new Button("Play as Guest");
        Button loginButton = new Button("Login");
        Button createAccountButton = new Button("Create Account");

        menuLayout.getChildren().addAll(playButton, loginButton, createAccountButton);
        Scene menuScene = new Scene(menuLayout, 300, 250);

        // Set up button actions
        playButton.setOnAction(e -> startGame(primaryStage));
        loginButton.setOnAction(e -> {/* Implement login logic */});
        createAccountButton.setOnAction(e -> {/* Implement account creation logic */});

        primaryStage.setTitle("Snake Game");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    private void startGame(Stage primaryStage) {
        Pane gameRoot = new Pane();
        Scene gameScene = new Scene(gameRoot, WINDOW_WIDTH, WINDOW_HEIGHT);

        initSnake(gameRoot);
        spawnFood(gameRoot);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate > GAME_SPEED) {
                    if (!gameOver) {
                        updateGame(gameRoot);
                    }
                    lastUpdate = now;
                }
            }
        }.start();

        gameScene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    if (snakeDirection != Direction.DOWN) snakeDirection = Direction.UP;
                    break;
                case DOWN:
                    if (snakeDirection != Direction.UP) snakeDirection = Direction.DOWN;
                    break;
                case LEFT:
                    if (snakeDirection != Direction.RIGHT) snakeDirection = Direction.LEFT;
                    break;
                case RIGHT:
                    if (snakeDirection != Direction.LEFT) snakeDirection = Direction.RIGHT;
                    break;
            }
        });

        primaryStage.setScene(gameScene);
    }

    private void initSnake(Pane root) {
        Rectangle head = new Rectangle(BLOCK_SIZE, BLOCK_SIZE, Color.GREEN);
        head.setTranslateX(WINDOW_WIDTH / 2);
        head.setTranslateY(WINDOW_HEIGHT / 2);
        snake.add(head);
        root.getChildren().add(head);
    }

    private void spawnFood(Pane root) {
        int maxX = WINDOW_WIDTH / BLOCK_SIZE - 1;
        int maxY = WINDOW_HEIGHT / BLOCK_SIZE - 1;
        int foodX = (int) (Math.random() * maxX) * BLOCK_SIZE;
        int foodY = (int) (Math.random() * maxY) * BLOCK_SIZE;

        food = new Rectangle(BLOCK_SIZE, BLOCK_SIZE, Color.RED);
        food.setTranslateX(foodX);
        food.setTranslateY(foodY);

        root.getChildren().add(food);
    }

    private void updateGame(Pane root) {
        for (int i = snake.size() - 1; i >= 1; i--) {
            Rectangle tail = snake.get(i);
            Rectangle front = snake.get(i - 1);

            tail.setTranslateX(front.getTranslateX());
            tail.setTranslateY(front.getTranslateY());
        }

        Rectangle head = snake.get(0);
        switch (snakeDirection) {
            case UP:
                head.setTranslateY(head.getTranslateY() - BLOCK_SIZE);
                break;
            case DOWN:
                head.setTranslateY(head.getTranslateY() + BLOCK_SIZE);
                break;
            case LEFT:
                head.setTranslateX(head.getTranslateX() - BLOCK_SIZE);
                break;
            case RIGHT:
                head.setTranslateX(head.getTranslateX() + BLOCK_SIZE);
                break;
        }

        // Check collisions
        if (head.getBoundsInParent().intersects(food.getBoundsInParent())) {
            growSnake(root);
            spawnFood(root);  // Relocate the food
        } else if (checkSelfCollision() || checkWallCollision(head)) {
            gameOver = true;
        }
    }

    private void checkCollisions(Pane root) {
        Rectangle head = snake.get(0);

        // Collision with food
        if (head.getBoundsInParent().intersects(food.getBoundsInParent())) {
            food.setFill(Color.BLACK); // Hide the food
            growSnake(root);
            spawnFood(root);
        }

        // Collision with self or walls
        if (checkSelfCollision() || checkWallCollision(head)) {
            gameOver = true;
        }
    }

    private boolean checkSelfCollision() {
        Rectangle head = snake.get(0);
        for (int i = 1; i < snake.size(); i++) {
            if (head.getBoundsInParent().intersects(snake.get(i).getBoundsInParent())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkWallCollision(Rectangle head) {
        return head.getTranslateX() < 0 || head.getTranslateX() >= WINDOW_WIDTH
                || head.getTranslateY() < 0 || head.getTranslateY() >= WINDOW_HEIGHT;
    }

    private void growSnake(Pane root) {
        Rectangle tail = new Rectangle(BLOCK_SIZE, BLOCK_SIZE, Color.GREEN);
        Rectangle last = snake.get(snake.size() - 1);
        tail.setTranslateX(last.getTranslateX());
        tail.setTranslateY(last.getTranslateY());

        snake.add(tail);
        root.getChildren().add(tail);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
}
