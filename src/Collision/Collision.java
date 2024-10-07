package Collision;

import area.Library.Firstfloor_library;
import entity.Entity;
import entity.Player;
import main.GamePanel;
import main.Main;
import main.UI;
import map.Map;
import sound.SoundManager;
import tile.Tile;
import time.TimeSystem;

public class Collision {
    public CollisionCheck collisionCheck;
    public UI ui;
    GamePanel gamePanel;
    int[] tileIndex;
    int numCollision;
    public Tile interactItem = new Tile();
    Tile[] collisionTile;
    public Boolean checkDialogue = true;
    public Collision(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.ui = gamePanel.ui;
        collisionCheck = new CollisionCheck(gamePanel);
    }

    public void update() {
        numCollision = collisionCheck.numCollision;
        collisionTile = collisionCheck.collisionTile;
        tileIndex = collisionCheck.tileIndex;
        pushDialog();
        boolean isTeleport = false;
        if (numCollision == 1 && (collisionTile[0].Type.equals("Interact") || collisionTile[0].Type.equals("NPC")))
            interactItem = collisionTile[0];
        for (int i = 0; i < numCollision; ++i) {
            if (collisionTile[i].Type.equals("Collected"))
                collectItem(i);
            if (!isTeleport && collisionTile[i].Type.equals("Teleport")) {
                teleport(collisionTile[i].Name);
                isTeleport = true;
                gamePanel.player.ButtonInteract = false;
            }
        }
    }


    void teleport(String name) {
        switch (name) {
            case "Door My Room":
                if (!gamePanel.inventory.isExist("Iphone 100 ProMax")){
                    ui.currentDialog = "Không có điện thoại mà đòi ra ngoài?";
                    Main.pushGameState("Dialog");
                }
                else {
                    gamePanel.section_selection.open(true);
                    SoundManager.playSound("open_door");
                }
                break;
                
            case "Door Classroom":
                gamePanel.d3_5_hallway_secondfloor.open();
                SoundManager.playSound("open_door");
                break;
            
            case "C2_Hallway_entry_stair":
                gamePanel.c2_hallway.open("enter_from_stair");
                SoundManager.playSound("footstep_up_stairs");
                break;    
            
            case "C2_Hall_entry":
                gamePanel.c2_hall.open();
                SoundManager.playSound("open_door");
                break;

            case "C2_Hall_exit":
                gamePanel.c2_hallway.open("enter_from_door");
                SoundManager.playSound("open_door");
                break;

            case "C2_Hallway_exit":
                gamePanel.section_1.open();
                SoundManager.playSound("foot_step");
                break; 

            case "D3_Hallway_entry_stair1":
                gamePanel.d3_hallway.open("enter_from_stair1");
                SoundManager.playSound("footstep_up_stairs");
                break;

            case "D3_Hallway_entry_stair2":
                gamePanel.d3_hallway.open("enter_from_stair2");
                SoundManager.playSound("footstep_up_stairs");
                break;

            case "D3_Hallway_entry_stair3":
                gamePanel.d3_hallway.open("enter_from_stair3");
                SoundManager.playSound("footstep_up_stairs");
                break;      

            case "D3_exit":
                gamePanel.section_2.open();
                SoundManager.playSound("foot_step");
                break; 

            case "D3_stair_down1":
                SoundManager.playSound("footstep_down_stairs");
                if(gamePanel.d3_secondfloor_hallway.curr_floor == 2)
                    gamePanel.d3_hallway.open("enter_from_2nd_floor");

                else{
                    gamePanel.d3_secondfloor_hallway.curr_floor -= 1;
                    gamePanel.d3_secondfloor_hallway.open("enter_from_stair1");
                }
                break;
                
            case "D3_stair_down2":
                SoundManager.playSound("footstep_down_stairs");
                if(gamePanel.d3_secondfloor_hallway.curr_floor == 2)
                    gamePanel.d3_hallway.open("enter_from_stair3");
                else{
                    gamePanel.d3_secondfloor_hallway.curr_floor -= 1;
                    gamePanel.d3_secondfloor_hallway.open("enter_from_stair2");
                }
                break;
                
            case "D3_1stfloor_stair1":
                gamePanel.d3_secondfloor_hallway.curr_floor = 2;
                SoundManager.playSound("footstep_up_stairs");
                gamePanel.d3_secondfloor_hallway.open("enter_from_stair1");
                break;   

            case "D3_1stfloor_stair2":
                gamePanel.d3_secondfloor_hallway.curr_floor = 2;
                gamePanel.d3_secondfloor_hallway.open("enter_from_stair2");
                SoundManager.playSound("footstep_up_stairs");
                break;  
                
            case "D3_stair_up1":
                if(gamePanel.d3_secondfloor_hallway.curr_floor != 5){
                    gamePanel.d3_secondfloor_hallway.curr_floor += 1;
                    SoundManager.playSound("footstep_up_stairs");
                    gamePanel.d3_secondfloor_hallway.open("enter_from_stair1");
                }
                break;

            case "D3_stair_up2":
                if(gamePanel.d3_secondfloor_hallway.curr_floor != 5){
                    gamePanel.d3_secondfloor_hallway.curr_floor += 1;
                    SoundManager.playSound("footstep_up_stairs");
                    gamePanel.d3_secondfloor_hallway.open("enter_from_stair2");
                }
                break;

            case "D3->D3-5_secondfloor_link":
                SoundManager.playSound("foot_step");
                gamePanel.d3_5_hallway_secondfloor.curr_floor = gamePanel.d3_secondfloor_hallway.curr_floor;
                gamePanel.d3_5_hallway_secondfloor.open();
                break;

            case "D3<-D3_5_secondfloor_link":
                SoundManager.playSound("foot_step");
                gamePanel.d3_secondfloor_hallway.curr_floor = gamePanel.d3_5_hallway_secondfloor.curr_floor;
                gamePanel.d3_secondfloor_hallway.open("enter_from_D3_5");
                break;

            case "D3-5_...01":
                if(gamePanel.d3_5_hallway_secondfloor.curr_floor == 3){
                    SoundManager.playSound("open_door");
                    //gamePanel.normalClassroom.loadMap(gamePanel);
                    gamePanel.normalClassroom.open("Vao phong hoc");
                    gamePanel.chapter2.isAtClassMrsToan = true;
                }
                break;
                
            case "Cau thang 1 -> 2":
                gamePanel.secondfloorLibrary.open(Player.checkTile);
                break;

            case "Cau thang 2 -> 1":
               gamePanel.firstfloorLibrary.open(Player.checkTile);
                break;

            case "Cau thang 2 -> 3":
                gamePanel.thirdfloorLibrary.open(Player.checkTile);
                break;

            case "Cau thang 3 -> 2":
                gamePanel.secondfloorLibrary.open(Player.checkTile);
                break;

            case "Cau thang 3 -> 4":
                gamePanel.fourthfloorLibrary.open(Player.checkTile);
                break;

            case "Cau thang 4 -> 3":
                gamePanel.thirdfloorLibrary.open(Player.checkTile);
                break;

            case "Cau thang 4 -> 5":
                gamePanel.fifthfloorLibrary.open(Player.checkTile);
                break;

            case "Cau thang 5 -> 4":
                gamePanel.fourthfloorLibrary.open(Player.checkTile);
                break;

            case "Door Thirdfloor":
                gamePanel.chapter3.isAtLibrary = true;
                gamePanel.library.open(Player.checkTile);
                break;

            case "Door Library":
                gamePanel.thirdfloorLibrary.open(Player.checkTile);
                break;

            case "Library_entry 1":
                gamePanel.firstfloorLibrary.open(Player.checkTile);
                break;

            case "Library_entry 2":
                gamePanel.firstfloorLibrary.open(Player.checkTile);
                break;

            case "Cua ra thu vien":
                gamePanel.section_3.open(Player.checkTile);
                break;

            case "Di ra":
                gamePanel.firstfloorLibrary.open(Player.checkTile);
                break;

            case "Di vao":
               if(!Firstfloor_library.checkStudent) {
                    Dialog("Bạn cần quét thẻ sinh viên tại bàn quét mã");
               }

                if(Firstfloor_library.checkStudent) {
                    gamePanel.firstfloorLibrary.open(Player.checkTile);
                }
                break;


            case "D3-5_...02":
                if(gamePanel.d3_5_hallway_secondfloor.curr_floor == 3){
                    SoundManager.playSound("open_door");
                    gamePanel.normalClassroom_302.loadMap(gamePanel);
                    gamePanel.chapter2.isAtClassMrHoa = true;
                }
                break;

            default:
                break;
        }

    }


    void Dialog(String str) {
        if (Main.topGameState().equals("GamePlay")) {
            gamePanel.ui.currentDialog = str;
            Main.pushGameState("Dialog");
            gamePanel.ui.i = 0;
            gamePanel.ui.timer.setDelay(30);
            gamePanel.ui.isFinishDialogue = false;
            gamePanel.ui.timer.start();
        }
    }

    public boolean isCollision(String str) {
        for (int i = 0; i < numCollision; ++i)
            if (collisionTile[i].Name.equals(str))
                return true;
        return false;
    }

    public void pushDialog() {
        boolean checkPush = true;
        for (int i = 0; i < numCollision; ++i)
            if (collisionTile[i].Type.equals("Teleport") || collisionTile[i].Type.equals("NPC")
                    || collisionTile[i].Type.equals("Obstacle")
                    || collisionTile[i].Name.equals("Desk") || collisionTile[i].Name.equals("My Bed")) {
                checkPush = false;
                break;
            }
        if (!Main.topGameState().equals("Dialog") && !gamePanel.phone.isDrawPhone && checkPush)
            Main.pushGameState("Dialog");

        switch (collisionTile[0].Type) {
            case "Teleport": {
                ui.currentDialog = "Bạn vừa dịch chuyển tới ";
                break;
            }
            case "Collected": {
                ui.currentDialog = "Bạn vừa thu thập ";
                break;
            }
            case "Interact": {
                ui.currentDialog = "Bạn đang tương tác với ";
                break;
            }
            case "Obstacle": {
                ui.currentDialog = "Bạn đang va chạm với ";
                break;
            }
        }
        for (int i = 0; i < numCollision; ++i) {
            if (i > 0 && !collisionTile[i - 1].Name.equals("wall") && !collisionTile[i].Name.equals(""))
                ui.currentDialog += " and ";
            if (!collisionTile[i].Name.equals("wall") && !collisionTile[i].Name.equals("")) {
                if (collisionTile[i].Name.equals("Door My Room"))
                    ui.currentDialog += "Hanoi University of Tạch Môn!";
                else
                    ui.currentDialog += collisionTile[i].Name;
            }
        }
    }

    public void collectItem(int index) {
        gamePanel.inventory.pushToInventory(collisionTile[index]);
        gamePanel.currentMap.deleteTile(tileIndex[index]);
        gamePanel.player.ButtonInteract = false;
    }

    public void scanCollision(Entity entity, Map map) {
        collisionCheck.scanCollision(entity, map);
    }

    public int getNumCollision() {
        return collisionCheck.numCollision;
    }

    public Tile[] getCollisionTile() {
        return collisionCheck.collisionTile;
    }
}
