package area;

import main.GamePanel;
import main.Main;
import map.Map;
import sound.SoundManager;
import tile.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import animation.Animation_player;

import java.io.*;


public class Section_3 extends Map {
    Tile Library, BK_ALUMNI_HOUSE, Tien_Lake, Unknown;
    public Tile library_entry_1, library_entry_2;
    public Tile npcGirl;
    public boolean isDrawNPCGirl = false;
    Animation_player map_exchange_effect1, map_exchange_effect3;
    Tile spawn_point;


    public Section_3(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        SetDefaultValues();
    }

    private void SetDefaultValues(){
        TileLoad();
        ObjectLoad("Section3");
        SetOriginalSize();
        reSizeMap();
    }

    private void SetOriginalSize(){
        background.setWidth(background.image.getWidth());
        background.setHeight(background.image.getHeight());

        width = background.getWidth();
        height = background.getHeight();
    }

    private void ReSize(double scale){
        background.setWidth((int)(background.getWidth() * scale));
        background.setHeight((int)(background.getHeight() * scale));


        map_exchange_effect1.resize(GamePanel.screenWidth / (2 * map_exchange_effect1.getWidth()));
        map_exchange_effect3.resize(GamePanel.screenWidth / (2 * map_exchange_effect3.getWidth()));


        spawn_point.resize(scale);
        for(int i = 0; i < numTileContainer; i++){
            tileContainer[i].resize(scale);

        }

        width = background.getWidth();
        height = background.getHeight();

        SetPlayerPos();

    }

    private void SetPlayerPos(){
        playerX = spawn_point.getLeftX();
        playerY = spawn_point.getTopY();
    }

    private void TileLoad() {
        tileContainer = new Tile[50];

        try {
            BufferedImage bacImage = ImageIO.read(new FileInputStream("res/tile/Section3_demo.png"));
            background = new Tile(new Rectangle(0, 0, bacImage.getWidth(), bacImage.getHeight()), "Background", "", null, bacImage);

        } catch (IOException e) {
            e.printStackTrace();
        }


        spawn_point = new Tile(new Rectangle(850 , 1800 , 23 , 44), "", "", null, null);
        Tien_Lake = new Tile(new Rectangle(1662, 186, 1644, 807), "Tien_Lake", "Obstacle", null, null);
        npcGirl = new Tile(gamePanel, 1120, 80, 6, 16, "NPC Girl", "NPC", "", "res/NPC/NPCGirl/NPCGirl.png",1);


        library_entry_1 = new Tile(new Rectangle(765,1662,55,69),"Library_entry 1","Teleport","Di vao thu vien 1",null);
        library_entry_2 = new Tile(new Rectangle(870,1662,55,72),"Library_entry 2","Teleport","Di vao thu vien 2",null);
        addTile(library_entry_1);
        addTile(library_entry_2);
        addTile(Tien_Lake);
        addTile(npcGirl);
        map_exchange_effect1 = new Animation_player(gamePanel, "res/effect/Map_exchange/type1/frame ", 4, 0.8, new Rectangle((int)(GamePanel.screenWidth / 4), (int)(GamePanel.screenHeight / 2 - GamePanel.screenWidth / 4), (int)(GamePanel.screenWidth / 2), (int)(GamePanel.screenWidth / 2)));
        map_exchange_effect3 = new Animation_player(gamePanel, "res/effect/Map_exchange/type3/frame ", 4, 0.8, new Rectangle((int)(GamePanel.screenWidth / 4), (int)(GamePanel.screenHeight / 2 - GamePanel.screenWidth / 4), (int)(GamePanel.screenWidth / 2), (int)(GamePanel.screenWidth / 2)));

    }

    @Override public void reSizeMap(){
        ReSize(gamePanel.player.getBoundingBoxHeight() * 1.0 * 2605 / (40 * background.getHeight()));
    }


    public void open(String type){
        reSizeMap();
        if(type.equals("Cua ra tu thu vien")) {
            map_exchange_effect = map_exchange_effect3;
            SoundManager.playSound("open_door");
        }

        else{
            map_exchange_effect = map_exchange_effect1;
            SoundManager.playSound("foot_step");
        }
        loadMap(gamePanel);
    }
    public void close(){
        Main.popGameState();
    }

    @Override public void TileDisplay(Graphics2D g2){
        if (isDrawNPCGirl)
            gamePanel.tileManager.draw(g2, npcGirl);
    }

    // Phương thức vẽ map
    // public void draw(Graphics2D g2) {

    //     gamePanel.tileManager.draw(g2, background);
    //     for (int i = 0; i < numTileContainer; ++i)
    //         gamePanel.tileManager.draw(g2, tileContainer[i]);
    // }
}
