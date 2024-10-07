package main;

import Keyboard.KeyboardManager;
import Mouse.MouseManager;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import GUI.MissionDescription;

public class KeyHandler implements KeyListener {

    GamePanel gamePanel;
    MissionDescription missionDescription;
    public boolean upPressed, downPressed, leftPressed, rightPressed, isInteract, isPhonePressed;

    public KeyHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.missionDescription = gamePanel.missionDescription;
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyboardManager.getKey("UP")) {
            upPressed = true;
        }
        if (code == KeyboardManager.getKey("DOWN")) {
            downPressed = true;
        }
        if (code == KeyboardManager.getKey("LEFT")) {
            leftPressed = true;
        }
        if (code == KeyboardManager.getKey("RIGHT")) {
            rightPressed = true;
        }
        if (code == KeyboardManager.getKey("PAUSE")) {

            if (Main.topGameState().equals("PauseGame")) {
                Main.popGameState();
            } else if (Main.topGameState().equals("GamePlay") && !gamePanel.phone.isDrawPhone)
                Main.pushGameState("PauseGame");
            else if (Main.topGameState().equals("Inventory"))
                Main.popGameState();
        }
        if (code == KeyboardManager.getKey("INVENTORY")) {
            if (Main.topGameState().equals("Inventory"))
                Main.popGameState();
            else {
                if ((Main.topGameState().equals("GamePlay") || Main.topGameState().equals("Dialog") || Main.topGameState().equals("Dialogue")) && !gamePanel.phone.isDrawPhone) {
                    MouseManager.isRightMouseClick = false;
                    MouseManager.isLeftMouseClick = false;
                    if (Main.topGameState().equals("Dialog")) {
                        Main.popGameState();
                        isInteract = false;
                    }
                    Main.pushGameState("Inventory");
                }
            }
        }
        // if(code == KeyboardManager.getKey("PHONE")){
        // isPhonePressed = true;
        // }
    }

    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyboardManager.getKey("UP")) {
            upPressed = false;
        }
        if (code == KeyboardManager.getKey("DOWN")) {
            downPressed = false;
        }
        if (code == KeyboardManager.getKey("LEFT")) {
            leftPressed = false;
        }
        if (code == KeyboardManager.getKey("RIGHT")) {
            rightPressed = false;
        }
        if (code == KeyboardManager.getKey("PHONE")) {
            isPhonePressed = true;
        }
        if ((KeyboardManager.getTypeKey(code).equals("INTERACT")
                || KeyboardManager.getTypeKey(code).equals("NEXTDIALOGUE") || MouseManager.isLeftMouseClick)
                && !gamePanel.phone.isDrawPhone) {
            if (Main.topGameState().equals("Dialog")) {
                gamePanel.ui.text = "";
                Main.popGameState();
                isInteract = false;
            } else if (KeyboardManager.getTypeKey(code).equals("INTERACT"))
                isInteract = true;
        }
        if (KeyboardManager.getTypeKey(code).equals("NEXTDIALOGUE")) {
            if (Main.topGameState().equals("Dialogue")) {
                if (!gamePanel.question.isDisplayQuestion) {
                    if (!gamePanel.ui.isFinishDialogue) {
                        gamePanel.ui.timer.setDelay(0);
                        gamePanel.ui.isFinishDialogue = true;
                    } else {
                        Main.popGameState();
                        gamePanel.ui.text = "";
                        gamePanel.ui.timer.stop();
                        if (this.missionDescription != null && this.missionDescription.isMissionDescriptionDrawing) {
                            this.missionDescription.isMissionDescriptionDrawing = false;
                        } else
                            ++gamePanel.currentChapter.completedAct;

                    }
                }
            }
        }

    }

}
