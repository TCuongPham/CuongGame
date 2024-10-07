package MainMenu;

import main.Main;
import sound.SoundManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

public class AudioSetting {
    private BufferedImage SettingBackGround, back, exitImg, exitImg1, line, player, comment, comment1, volume, volumeX,
            volume0, volume1, volume2, volume3, mutecmt, mutecmt1;
    private int i;

    public static double volumesliderpointX = 542 * Main.ex;
    public double mute_unmutestring = 163 * Main.ex;
    public static boolean checkenterslider = false;
    public static int checkmute = 1;
    private String check = "", mutestring = "", mutestring1 = "";
    private double numbervolume = 999 * Main.ey;

    static int currVolume = 100;
    Boolean isVolumeChanged = false;

    public AudioSetting() {
        getImage();
    }


    public void getImage() {
        try {
            SettingBackGround = ImageIO.read(new FileInputStream("res/MainmenuImage/settingbackgroundmo.png"));
            back = ImageIO.read(new FileInputStream("res/MainmenuImage/backicon.png"));
            exitImg1 = ImageIO.read(new FileInputStream("res/player/character_move_left (1).png"));
            line = ImageIO.read(new FileInputStream("res/MainmenuImage/straightline.png"));
            comment1 = ImageIO.read(new FileInputStream("res/MainmenuImage/comment.png"));
            player = ImageIO.read(new FileInputStream("res/player/character_move_down (4).png"));
            volume0 = ImageIO.read(new FileInputStream("res/MainmenuImage/zerovolume.png"));
            volume1 = ImageIO.read(new FileInputStream("res/MainmenuImage/lowvolume.png"));
            volume2 = ImageIO.read(new FileInputStream("res/MainmenuImage/mediumvolume.png"));
            volume3 = ImageIO.read(new FileInputStream("res/MainmenuImage/maxvolume.png"));
            volumeX = ImageIO.read(new FileInputStream("res/MainmenuImage/volumemute.png"));
            mutecmt1 = ImageIO.read(new FileInputStream("res/MainmenuImage/comment.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void volumesliderpointX(int i) {
        check = "volumesliderpointclick";
        this.i = i;
    }

    public void checkcomment() {
        check = "checkcomment";
    }

    public void audiorollback() {
        check = null;
    }

    public void buttonReturnEnter() {
        check = "buttonReturnEnter";
    }

    public void muteEnter() {
        check = "muteEnter";
    }

    public void Init() {
        check = "";
        comment = null;
        exitImg = null;
        mutecmt = null;
        mutestring = "";
        numbervolume = 999 * Main.ey;
    }

    public void update() {
        if (check == "volumesliderpointclick") {
            volumesliderpointX = i;
        } else if (check == "checkcomment") {
            comment = comment1;
            numbervolume = 231 * Main.ey;
        } else if (check == "buttonReturnEnter") {
            // System.out.println(check);
            exitImg = exitImg1;
        } else if (check == "muteEnter") {
            mutecmt = mutecmt1;
            mutestring = mutestring1;
        } else {
            comment = null;
            exitImg = null;
            mutecmt = null;
            mutestring = "";
            numbervolume = 999 * Main.ey;
        }
        if (checkmute > 0) {
            if ((int) ((volumesliderpointX - 217 * Main.ex) / (3.6 * Main.ex)) < 34
                    && (int) ((volumesliderpointX - 217 * Main.ex) / (3.6 * Main.ex)) > 0)
                volume = volume1;
            else if ((int) ((volumesliderpointX - 217 * Main.ex) / (3.6 * Main.ex)) >= 34
                    && (int) ((volumesliderpointX - 217 * Main.ex) / (3.6 * Main.ex)) < 67)
                volume = volume2;
            else if ((int) ((volumesliderpointX - 217 * Main.ex) / (3.6 * Main.ex)) >= 67)
                volume = volume3;
            else
                volume = volume0;
            mutestring1 = "Mute";
            mute_unmutestring = 166 * Main.ex;
        } else {
            volume = volumeX;
            mutestring1 = "Unmute";
            mute_unmutestring = 156 * Main.ex;
        }

        if ((int) ((volumesliderpointX - 217) / 3.6) == 100)
            currVolume = (int) ((volumesliderpointX - 217 * Main.ex) / (3.6 * Main.ex));
        else if ((int) ((volumesliderpointX - 217 * Main.ex) / (3.6 * Main.ex)) > 9)
            currVolume = (int) ((volumesliderpointX - 217 * Main.ex) / (3.6 * Main.ex));
        else
            currVolume = (int) ((volumesliderpointX - 217 * Main.ex) / (3.6 * Main.ex));
        currVolume = Math.max(currVolume, 0);
        currVolume = Math.min(currVolume, 100);
        
        if(checkmute == 1)  {
            SoundManager.unmutingVolume();
            SoundManager.setVolume(currVolume);
        }
        else if(checkmute == -1)    SoundManager.mutingVolume();
    }

    public void draw(Graphics2D g2) {
        // System.out.println(this);
        g2.drawImage(SettingBackGround, 0, 0, (int) (768 * Main.ex), (int) (576 * Main.ey), null);
        g2.drawImage(back, (int) (10 * Main.ex), (int) (10 * Main.ey), (int) (40 * Main.ex), (int) (40 * Main.ey), null);
        g2.drawImage(exitImg, (int) (60 * Main.ex), (int) (12 * Main.ey), (int) (40 * Main.ex), (int) (40 * Main.ey), null);
        g2.drawImage(line, (int) (240 * Main.ex), (int) (250 * Main.ey), (int) (360 * Main.ex), (int) (60 * Main.ey), null);
        g2.drawImage(player, (int) volumesliderpointX, (int) (250 * Main.ey), (int) (50 * Main.ex), (int) (50 * Main.ey), null);
        g2.setFont(new Font("Arial", Font.BOLD, (int) (14 * Main.ex)));
        g2.setColor(Color.white);
        g2.drawImage(comment, (int) (volumesliderpointX + 9 * Main.ex), (int) (215 * Main.ey), (int) (30 * Main.ex), (int) (30 * Main.ey), null);
        if ((currVolume) == 100) {
            g2.drawString("" + currVolume,
                    (float) (volumesliderpointX + 12 * Main.ex), (float) numbervolume);
            isVolumeChanged = true;
        } else if (currVolume > 9) {
            g2.drawString("" + currVolume,
                    (float) (volumesliderpointX + 16 * Main.ex), (float) numbervolume);
            isVolumeChanged = true;
        } else {
            g2.drawString("0" + currVolume,
                    (float) (volumesliderpointX + 16 * Main.ex), (float) numbervolume);
            isVolumeChanged = true;
        }
        g2.drawImage(volume, (int) (170 * Main.ex), (int) (254 * Main.ey), (int) (50 * Main.ex), (int) (50 * Main.ey), null);
        g2.drawImage(mutecmt, (int) (153 * Main.ex), (int) (205 * Main.ey), (int) (60 * Main.ex), (int) (50 * Main.ey), null);
        g2.drawString(mutestring, (float) mute_unmutestring, (float) (229 * Main.ey));
    }

}
