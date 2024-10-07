package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class UI {
    GamePanel gamePanel;
    Graphics2D g2;
    Font arial_40;
    public String currentDialog = "";
    public String text = "";
    public int i;
    BufferedImage nextIcon, characterDialogue;
    BufferedImage mainCharacter, mrsToan, mrHoa, catMeme, system, teacher1, npcGirl, catGivingFlower;
    public BufferedImage backgroundDialogue, backgroundClassroom, backgroundLibrary, backgroundLake;
    BufferedImage reelCharacter, surpriseMeme, girlReadingBook;
    public boolean isFinishDialogue;
    int iconX = (int) (207 * GamePanel.scale), iconY = (int) (170 * GamePanel.scale);
    int reelX = 0, reelY = 0, reelwidth = (int) (GamePanel.screenWidth), reelheight = (int) (GamePanel.screenHeight);
    int step = 0;
    boolean reverse;

    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        getImage();
    }

    void getImage() {
        try {
            nextIcon = ImageIO.read(new FileInputStream("res/Dialogue/nextIcon.png"));

            catMeme = ImageIO.read(new FileInputStream("res/DialogueCharacter/CatMeme.png"));
            mainCharacter = ImageIO.read(new FileInputStream("res/DialogueCharacter/MainCharacter.png"));
            mrHoa = ImageIO.read(new FileInputStream("res/DialogueCharacter/MrHoa.png"));
            mrsToan = ImageIO.read(new FileInputStream("res/DialogueCharacter/MrsToan.png"));
            system = ImageIO.read(new FileInputStream("res/DialogueCharacter/System.png"));
            teacher1 = ImageIO.read(new FileInputStream("res/NPC/teacher1/Frame (1).png"));
            npcGirl = ImageIO.read(new FileInputStream("res/DialogueCharacter/NPCGirl.png"));
            surpriseMeme = ImageIO.read(new FileInputStream("res/DialogueCharacter/SurpriseMeme.png"));
            catGivingFlower = ImageIO.read(new FileInputStream("res/DialogueCharacter/CatGivingFlower.png"));
            girlReadingBook = ImageIO.read(new FileInputStream("res/DialogueCharacter/GirlReadingBook.png"));

            backgroundClassroom = ImageIO.read(new FileInputStream("res/DialogueBackground/BackgroundClassroom.png"));
            backgroundLibrary = ImageIO.read(new FileInputStream("res/DialogueBackground/BackgroundLibrary.png"));
            backgroundLake = ImageIO.read(new FileInputStream("res/DialogueBackground/BackgroundLake.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void screenResize() {
        iconX = (int) (207 * GamePanel.scale);
        iconY = (int) (170 * GamePanel.scale);
        reelwidth = (int) (GamePanel.screenWidth);
        reelheight = (int) (GamePanel.screenHeight);
    }

    public void draw(Graphics2D g2) {
        if (!Main.topGameState().equals("GamePlay") && !Main.topGameState().equals("Dialog")
                && !Main.topGameState().equals("GamePlay")
                && !Main.topGameState().equals("Inventory")
                && !Main.topGameState().equals("Dialogue") && !Main.topGameState().equals("Reels"))
            return;
        this.g2 = g2;
        g2.setFont(arial_40);
        g2.setColor(Color.white);
        // if (Main.topGameState().equals("PauseGame")) {
        // drawPauseScreen();
        // }
        if (Main.topGameState().equals("Dialog")) {
            drawDialogScreen();
        }
        if (Main.topGameState().equals("Dialogue")) {
            drawDialogueScreen();
        }
        if (Main.topGameState().equals("GamePlay")) {
            if (gamePanel.player.ButtonInteract && !gamePanel.phone.isDrawPhone) {
                drawInteractButton();
            }
        }
        if (Main.topGameState().equals("Reels")) {
            drawReels();
        }
    }

    // public void drawPauseScreen() {
    // String text = "PAUSED";
    // int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
    // int x = (int) (GamePanel.screenWidth / 2 - length / 2);
    // int y = (int) (GamePanel.screenHeight / 2);
    // g2.drawString(text, x, y);
    // }

    public void drawDialogScreen() {
        int x = 32 * (int) GamePanel.scale;
        int width = (int) (GamePanel.screenWidth - 64 * GamePanel.scale);
        int height = (int) (64 * GamePanel.scale);
        int y = (int) (GamePanel.screenHeight - height - 8 * GamePanel.scale);
        int FontSize = (int) (GamePanel.scale * 9);
        int FontPixel = (int) (GamePanel.scale * 4);
        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, FontSize));
        x += (int) (16 * GamePanel.scale);
        y += (int) (16 * GamePanel.scale);
        String str = "";
        int strSize = 0;
        for (int i = 0; i < currentDialog.length(); ++i) {
            strSize += FontPixel;
            if (currentDialog.charAt(i) == ' ') {
                for (int j = i + 1; j <= currentDialog.length(); ++j)
                    if (j == currentDialog.length() || currentDialog.charAt(j) == ' ') {
                        if (strSize + (j - i - 1) * FontPixel >= width - 36 * GamePanel.scale) {
                            g2.drawString(str, x, y);
                            str = "";
                            y += 40;
                            strSize = 0;
                        }
                        break;
                    }
                if (strSize > 0)
                    str += ' ';
            } else
                str += currentDialog.charAt(i);
        }
        if (strSize > 0)
            g2.drawString(str, x, y);
    }

    public Timer timer = new Timer(40, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            text += currentDialog.charAt(i);
            i++;
            if (i == currentDialog.length()) {
                timer.stop();
                isFinishDialogue = true;
                i = 0;
            }
        }
    });

    public void drawDialogueScreen() {
        if(gamePanel.question.isDisplayQuestion)    return;
        int x = 32 * (int) GamePanel.scale;
        int width = (int) (GamePanel.screenWidth - 64 * GamePanel.scale);
        int height = (int) (64 * GamePanel.scale);
        int y = (int) (GamePanel.screenHeight - height - 8 * GamePanel.scale);
        int FontSize = (int) (GamePanel.scale * 9);
        int FontPixel = (int) (GamePanel.scale * 4);
        drawBackground();
        drawCharacterDialogue();
        drawSubWindow(x, y, width, height);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, FontSize));
        x += (int) (16 * GamePanel.scale);
        y += (int) (16 * GamePanel.scale);
        String str = "";
        int strSize = 0;
        for (int i = 0; i < text.length(); ++i) {
            strSize += FontPixel;
            if (text.charAt(i) == ' ') {
                for (int j = i + 1; j <= text.length(); ++j)
                    if (j == text.length() || text.charAt(j) == ' ') {
                        if (strSize + (j - i - 1) * FontPixel >= width - 36 * GamePanel.scale) {
                            g2.drawString(str, x, y);
                            str = "";
                            y += 40;
                            strSize = 0;
                        }
                        break;
                    }
                if (strSize > 0)
                    str += ' ';
            } else
                str += text.charAt(i);
        }
        if (strSize > 0)
            g2.drawString(str, x, y);
        ++step;
        if (step == 8) {
            if (!reverse) {
                iconX += (int) GamePanel.scale;
                if (iconX >= 209 * GamePanel.scale) {
                    reverse = true;
                }
            } else {
                iconX -= (int) GamePanel.scale;
                if (iconX <= 207 * GamePanel.scale) {
                    reverse = false;
                }
            }
            step = 0;
        }
        if (!gamePanel.question.isDisplayQuestion) {
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, (int) (GamePanel.scale * 4)));
            g2.drawString("Press Space", (int) (182 * GamePanel.scale), (int) (175 * GamePanel.scale));
            g2.drawImage(nextIcon, iconX, iconY, (int) (10 * GamePanel.scale), (int) (7 * GamePanel.scale), null);
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, (int) (GamePanel.scale * 7)));
        }
    }

    public void setDialogueCharacter(String name) {
        if (name.equals("Main Character"))
            characterDialogue = mainCharacter;
        if (name.equals("Mrs Toan"))
            characterDialogue = mrsToan;
        if (name.equals("Mr Hoa"))
            characterDialogue = mrHoa;
        if (name.equals("Cat Meme"))
            characterDialogue = catMeme;
        if (name.equals("System"))
            characterDialogue = system;
        if (name.equals("Empty"))
            characterDialogue = null;
        if (name.equals("Teacher1"))
            characterDialogue = teacher1;
        if (name.equals("NPC Girl"))
            characterDialogue = npcGirl;
    }

    public void setDialogueBackground(String name) {
        if (name.equals("Classroom"))
            backgroundDialogue = backgroundClassroom;
        if (name.equals("Library"))
            backgroundDialogue = backgroundLibrary;
        if (name.equals("Lake"))
            backgroundDialogue = backgroundLake;
        if (name.equals("Empty"))
            backgroundDialogue = null;
    }

    public void setReelsCharacter(String name) {
        step = 0;
        if (name.equals("Surprise Meme")) {
            reelX = 0;
            reelY = 0;
            reelwidth = (int) (GamePanel.screenWidth);
            reelheight = (int) (GamePanel.screenHeight);
            reelCharacter = surpriseMeme;
        }
        if (name.equals("Girl Reading Book")) {
            reelX = (int) (30 * GamePanel.scale);
            reelY = 0;
            reelwidth = (int) (200 * GamePanel.scale);
            reelheight = (int) (205 * GamePanel.scale);
            reelCharacter = girlReadingBook;
        }
        if (name.equals("Cat Giving Flower")) {
            reelX = 0;
            reelY = 0;
            reelwidth = (int) (GamePanel.screenWidth);
            reelheight = (int) (GamePanel.screenHeight);
            reelCharacter = catGivingFlower;
        }
    }

    public void drawCharacterDialogue() {
        int leftX = 0, topY = 0, width = 0, height = 0;
        if (characterDialogue == mrsToan) {
            leftX = (int) (75 * GamePanel.scale);
            topY = (int) (30 * GamePanel.scale);
            width = (int) (110 * GamePanel.scale);
            height = (int) (165 * GamePanel.scale);
        }
        if (characterDialogue == mrHoa) {
            leftX = (int) (50 * GamePanel.scale);
            topY = (int) (35 * GamePanel.scale);
            width = (int) (160 * GamePanel.scale);
            height = (int) (160 * GamePanel.scale);
        }
        if (characterDialogue == mainCharacter) {
            leftX = (int) (75 * GamePanel.scale);
            topY = (int) (45 * GamePanel.scale);
            width = (int) (110 * GamePanel.scale);
            height = (int) (150 * GamePanel.scale);
        }
        if (characterDialogue == catMeme) {
            leftX = (int) (55 * GamePanel.scale);
            topY = (int) (75 * GamePanel.scale);
            width = (int) (140 * GamePanel.scale);
            height = (int) (120 * GamePanel.scale);
        }
        if (characterDialogue == system) {
            leftX = (int) (75 * GamePanel.scale);
            topY = (int) (45 * GamePanel.scale);
            width = (int) (110 * GamePanel.scale);
            height = (int) (150 * GamePanel.scale);
        }
        if (characterDialogue == teacher1) {
            leftX = (int) (75 * GamePanel.scale);
            topY = (int) (45 * GamePanel.scale);
            width = (int) (110 * GamePanel.scale);
            height = (int) (150 * GamePanel.scale);
        }
        if (characterDialogue == npcGirl) {
            leftX = (int) (70 * GamePanel.scale);
            topY = (int) (15 * GamePanel.scale);
            width = (int) (134 * GamePanel.scale);
            height = (int) (200 * GamePanel.scale);
        }
        if (characterDialogue != null) {
            g2.drawImage(characterDialogue, leftX, topY, width, height, null);
        }
        int screenX, screenY;

    }

    public void drawReels() {
        ++step;
        if (step >= 3) {
            step = 0;
            reelX -= (int) (GamePanel.scale / 3);
            reelY -= (int) (GamePanel.scale / 3);
            reelwidth += 2 * (int) (GamePanel.scale / 3);
            reelheight += 2 * (int) (GamePanel.scale / 3);
        }
        g2.drawImage(reelCharacter, reelX, reelY, reelwidth, reelheight, null);
    }

    public void drawBackground() {
        if (backgroundDialogue == null)
            return;
        g2.drawImage(backgroundDialogue, 0, 0, (int) GamePanel.screenWidth, (int) GamePanel.screenHeight, null);
        Color myColor = new Color(45, 39, 39, 190);
        g2.setColor(myColor);
        g2.fillRect(0, 0, (int) GamePanel.screenWidth, (int) GamePanel.screenHeight);
    }

    public void drawInteractButton() {
        String text = "F";
        g2.setColor(Color.BLACK);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, (int) (GamePanel.scale * 4)));
        int x = gamePanel.player.getBoundingBoxX() + gamePanel.player.getBoundingBoxWidth();
        int y = gamePanel.player.getBoundingBoxY();
        int width = (int) ((int) g2.getFontMetrics().getStringBounds(text, g2).getWidth() + 8 * GamePanel.scale);
        int height = (int) (8 * GamePanel.scale);
        drawSubInteractButton((int) (x - 4 * GamePanel.scale), (int) (y - height / 2 - GamePanel.scale), width, height);
        g2.drawString(text, x, y);
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 100);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public void drawSubInteractButton(int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 100);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 25, 25);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x + 2, y + 2, width - 4, height - 4, 25, 25);
    }
}
