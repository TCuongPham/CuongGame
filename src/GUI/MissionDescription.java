package GUI;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.imageio.ImageIO;

import Mouse.MouseManager;
import Mouse.MouseMotion;

import java.io.IOException;
import java.io.StringWriter;

import main.GamePanel;
import main.Main;
import main.UI;
import phone.Phone;
import time.TimeSystem;

public class MissionDescription {
    GamePanel gamePanel;
    String missionDescriptionText = "Làm gì đó đi ní ơi!!!";
    BufferedImage missionIcon;
    BufferedImage newMissionIcon;
    BufferedImage darkMissionIcon;
    BufferedImage darkNewMissionIcon;
    int width, height;
    Phone phone;
    UI ui;
    public Boolean isMissionDescriptionDrawing = false;
    Boolean isNewMission = false;
    Boolean isOnHover = false;
    Boolean isNewMissionNotify = true;

    long startTime;

    public MissionDescription(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.phone = gamePanel.phone;
        this.ui = gamePanel.ui;
        getImage();
        init();
    }

    public void setMissionDescription(String text) {
        if (!missionDescriptionText.equals(text) && text.length() > 0) {
            isNewMission = true;
            startTime = TimeSystem.getCurrentSystemTimeInMilliseconds();
            isNewMissionNotify = true;
        }
        missionDescriptionText = text;
    }

    public void init() {
        width = (int) (GamePanel.screenWidth / 16);
        height = missionIcon.getHeight() * width / missionIcon.getWidth();
    }

    public void getImage() {
        try {
            missionIcon = ImageIO.read(new FileInputStream("res/GUI/mission_icon.png"));
            newMissionIcon = ImageIO.read(new FileInputStream("res/GUI/new_mission_icon.png"));
            darkMissionIcon = ImageIO.read(new FileInputStream("res/GUI/dark_mission_icon.png"));
            darkNewMissionIcon = ImageIO.read(new FileInputStream("res/GUI/dark_new_mission_icon.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public void update() {
        if (!Main.topGameState().equals("GamePlay"))
            return;
        checkClicked();
        checkHoverd();
    }

    public void checkClicked() {
        if (!Main.topGameState().equals("GamePlay"))
            return;
        if (this.phone.isDrawPhone)
            return;
        if (MouseManager.lastReleasedX >= (int) (GamePanel.screenWidth - width - GamePanel.screenWidth / 70)
                && MouseManager.lastReleasedX <= (int) (GamePanel.screenWidth - width - GamePanel.screenWidth / 70)
                        + width
                && MouseManager.lastReleasedY >= (int) (GamePanel.screenHeight / 20)
                && MouseManager.lastReleasedY <= (int) (GamePanel.screenHeight / 20) + height) {
            isNewMission = false;
            if (!missionDescriptionText.equals("")) {
                ui.setDialogueCharacter("Empty");
                ui.setDialogueBackground("Empty");
                Dialogue("Hệ thống: " + missionDescriptionText);
            }
        }
        MouseManager.resetLastReleasedPos();
    }

    public void checkHoverd() {
        if (!Main.topGameState().equals("GamePlay"))
            return;
        if (this.phone.isDrawPhone)
            return;
        isOnHover = false;
        if (MouseMotion.currentX >= (int) (GamePanel.screenWidth - width - GamePanel.screenWidth / 70)
                && MouseMotion.currentX <= (int) (GamePanel.screenWidth - width - GamePanel.screenWidth / 70)
                        + width
                && MouseMotion.currentY >= (int) (GamePanel.screenHeight / 20)
                && MouseMotion.currentY <= (int) (GamePanel.screenHeight / 20) + height) {
            isOnHover = true;
        }
        MouseManager.resetLastReleasedPos();
    }

    void Dialogue(String str) {
        if (Main.topGameState().equals("GamePlay")) {
            ui.currentDialog = str;
            ui.text = str;
            Main.pushGameState("Dialogue");
            isMissionDescriptionDrawing = true;
        }
    }

    public void screenResize() {
        init();
    }

    public void draw(Graphics2D g2) {
        if (!Main.topGameState().equals("GamePlay"))
            return;
        if (isNewMission)
            if (!isOnHover)
                g2.drawImage(newMissionIcon, (int) (GamePanel.screenWidth - width - GamePanel.screenWidth / 70),
                        (int) (GamePanel.screenHeight / 20), width, height, null);
            else
                g2.drawImage(darkNewMissionIcon, (int) (GamePanel.screenWidth - width - GamePanel.screenWidth / 70),
                        (int) (GamePanel.screenHeight / 20), width, height, null);
        else if (!isOnHover)
            g2.drawImage(missionIcon, (int) (GamePanel.screenWidth - width - GamePanel.screenWidth / 70),
                    (int) (GamePanel.screenHeight / 20), width, height, null);
        else
            g2.drawImage(darkMissionIcon, (int) (GamePanel.screenWidth - width - GamePanel.screenWidth / 70),
                    (int) (GamePanel.screenHeight / 20), width, height, null);
        if (isNewMissionNotify) {
            if (TimeSystem.getCurrentSystemTimeInMilliseconds() - startTime <= 2000)
                g2.setColor(Color.WHITE);
            else if (TimeSystem.getCurrentSystemTimeInMilliseconds() - startTime <= 4000)
                g2.setColor(Color.BLACK);
            else if (TimeSystem.getCurrentSystemTimeInMilliseconds() - startTime <= 6500)
                g2.setColor(Color.WHITE);
            else if (TimeSystem.getCurrentSystemTimeInMilliseconds() - startTime <= 9000)
                g2.setColor(Color.BLACK);
            else
                isNewMissionNotify = false;
                g2.setFont(new Font("Arial", Font.ITALIC, (int) (GamePanel.screenWidth / 40)));
                int stringWidth = g2.getFontMetrics().stringWidth("Nhiệm vụ mới!");
            g2.drawString("Nhiệm vụ mới!", (int) (GamePanel.screenWidth - stringWidth - GamePanel.screenWidth / 90),
                    (int) (GamePanel.screenHeight / 20) + height + height / 3);
        }
        // if (!isOnHover) {
        // float percentage = .92f; // 50% bright - change this (or set dynamically) as
        // you feel fit
        // int brightness = (int) (256 - 256 * percentage);
        // g2.setColor(new Color(0, 0, 0, brightness));
        // g2.fillRect((int) (GamePanel.screenWidth - width - GamePanel.screenWidth /
        // 70),
        // (int) (GamePanel.screenHeight / 20), width, height);
        // }

    }
}
