
package entity;

import Collision.Collision;
import main.*;
import animation.Animation_player;

import java.awt.*;
import java.security.PublicKey;

import static java.lang.Math.max;
import static java.lang.Math.min;

import map.Map;
import sound.SoundManager;
import tile.Tile;
import tile.TileManager;

public class Player extends Entity {

    GamePanel gamepanel;
    KeyHandler keyhandler;
    Collision collision;
    public boolean collision_collect;
    UI ui;
    public int screenX, screenY;
    boolean leftBorder, rightBorder, topBorder, bottomBorder;
    public boolean ButtonInteract;

    public Animation_player animation_player_stand_RIGHT;
    public Animation_player animation_player_stand_LEFT;
    public Animation_player animation_player_stand_FRONT;
    public Animation_player animation_player_stand_BACK;

    public Animation_player animation_player_UP;
    public Animation_player animation_player_DOWN;
    public Animation_player animation_player_RIGHT;
    public Animation_player animation_player_LEFT;

    public Animation_player curr_animation_player;
    TileManager tileManager;
    public String checkNameTile, checkDescriptionTile,checkInteractTile;
    public static String checkTile = "0", checkTile2 = "0";

    public Player(GamePanel gamepanel, KeyHandler keyhandler, TileManager tilemanager, UI ui) {
        this.gamepanel = gamepanel;
        this.keyhandler = keyhandler;
        this.collision = gamepanel.collision;
        this.tileManager = tilemanager;
        this.ui = ui;
        setDefaultValues();
    }

    public void setDefaultValues() {
        leftBorder = false;
        rightBorder = false;
        bottomBorder = false;
        topBorder = false;
        ButtonInteract = false;
        collision_collect = false;

        hitArea = new Rectangle();
        boundingBox = new Rectangle();
        boundingBox.width = (int) (32 * GamePanel.scale);
        boundingBox.height = (int) (32 * GamePanel.scale);
        hitArea.x = boundingBox.width / 4;
        hitArea.y = boundingBox.height / 3 * 2;
        hitArea.width = boundingBox.width / 2;
        hitArea.height = boundingBox.height / 3;
        screenX = (int) (GamePanel.screenWidth / 2 - boundingBox.width / 2);
        screenY = (int) (GamePanel.screenHeight / 2 - boundingBox.height / 2);
        boundingBox.x = screenX;
        boundingBox.y = screenY;
        speed = 5;
        direction = "stand";
        speedSlant = 4;

        animation_player_stand_RIGHT = new Animation_player(gamepanel, "res/player/character_stand_right ", 3, 0.5,
                boundingBox);
        animation_player_stand_LEFT = new Animation_player(gamepanel, "res/player/character_stand_left ", 3, 0.5,
                boundingBox);
        animation_player_stand_FRONT = new Animation_player(gamepanel, "res/player/character_stand_front ", 3, 0.5,
                boundingBox);
        animation_player_stand_BACK = new Animation_player(gamepanel, "res/player/character_stand_back ", 3, 0.5,
                boundingBox);
        animation_player_UP = new Animation_player(gamepanel, "res/player/character_move_up ", 4, 0.8, boundingBox);
        animation_player_DOWN = new Animation_player(gamepanel, "res/player/character_move_down ", 4, 0.8, boundingBox);
        animation_player_RIGHT = new Animation_player(gamepanel, "res/player/character_move_right ", 4, 0.8,
                boundingBox);
        animation_player_LEFT = new Animation_player(gamepanel, "res/player/character_move_left ", 4, 0.8, boundingBox);

        curr_animation_player = animation_player_stand_RIGHT;
    }

    public void reSize() {
        int prev_bound_width = boundingBox.width;
        boundingBox.width = (int) (32 * GamePanel.scale);
        boundingBox.height = (int) (32 * GamePanel.scale);
        hitArea.x = boundingBox.width / 4;
        hitArea.y = boundingBox.height / 3 * 2;
        hitArea.width = boundingBox.width / 2;
        hitArea.height = boundingBox.height / 3;
        screenX = (int) (GamePanel.screenWidth / 2 - boundingBox.width / 2);
        screenY = (int) (GamePanel.screenHeight / 2 - boundingBox.height / 2);
        boundingBox.x = screenX;
        boundingBox.y = screenY;
        speed = (int) (5 * GamePanel.scale / 3);
        speedSlant = (int) (4 * GamePanel.scale / 3);
        mapX = (int) (mapX * boundingBox.width * 1.0 / prev_bound_width);
        mapY = (int) (mapY * boundingBox.width * 1.0 / prev_bound_width);
    }

    // =============================================================================================================================================
    public void update() {
        if (!(Main.topGameState().equals("GamePlay")) || gamepanel.phone.isDrawPhone)
            return;
        int countPressed = 0;
        if (keyhandler.upPressed)
            ++countPressed;
        if (keyhandler.downPressed)
            ++countPressed;
        if (keyhandler.leftPressed)
            ++countPressed;
        if (keyhandler.rightPressed)
            ++countPressed;
        int newMapX = mapX, newMapY = mapY;
        if (countPressed > 0) {
            if (ButtonInteract)
                ButtonInteract = false;
            collision_collect = false;
            checkNameTile = "";
            checkDescriptionTile = "";
            checkInteractTile = "";
            if (countPressed == 1) {
                if (keyhandler.upPressed) {
                    direction = "up";
                    if (!topBorder)
                        newMapY -= speed;
                }
                if (keyhandler.downPressed) {
                    direction = "down";
                    if (!bottomBorder)
                        newMapY += speed;
                }
                if (keyhandler.leftPressed) {
                    direction = "left";
                    if (!leftBorder)
                        newMapX -= speed;
                }
                if (keyhandler.rightPressed) {
                    direction = "right";
                    if (!rightBorder)
                        newMapX += speed;
                }
            } else {
                if (!(keyhandler.upPressed && keyhandler.downPressed)
                        && !(keyhandler.leftPressed && keyhandler.rightPressed)) {
                    if (keyhandler.upPressed && keyhandler.leftPressed) {
                        direction = "up-left";
                        if (!topBorder)
                            newMapY -= speedSlant;
                        if (!leftBorder)
                            newMapX -= speedSlant;
                    }
                    if (keyhandler.upPressed && keyhandler.rightPressed) {
                        direction = "up-right";
                        if (!topBorder)
                            newMapY -= speedSlant;
                        if (!rightBorder)
                            newMapX += speedSlant;
                    }
                    if (keyhandler.downPressed && keyhandler.leftPressed) {
                        direction = "down-left";
                        if (!bottomBorder)
                            newMapY += speedSlant;
                        if (!leftBorder)
                            newMapX -= speedSlant;
                    }
                    if (keyhandler.downPressed && keyhandler.rightPressed) {
                        direction = "down-right";
                        if (!bottomBorder)
                            newMapY += speedSlant;
                        if (!rightBorder)
                            newMapX += speedSlant;
                    }
                }
            }
            boolean checkBug = false;
            collision.scanCollision(this, gamepanel.currentMap);
            if (collision.getNumCollision() == 0) {
                hitArea.x = newMapX + boundingBox.width / 4;
                hitArea.y = newMapY + boundingBox.height / 3 * 2;
                collision.scanCollision(this, gamepanel.currentMap);
                if (collision.getNumCollision() > 0) {
                    if (direction.equals("up-left")) {
                        if (!leftBorder)
                            newMapX = mapX - speed;
                        else newMapX = mapX;
                        newMapY = mapY;
                        hitArea.x = newMapX + boundingBox.width / 4;
                        hitArea.y = newMapY + boundingBox.height / 3 * 2;
                        collision.scanCollision(this, gamepanel.currentMap);
                        if (collision.getNumCollision() == 0)
                            direction = "left";
                        else {
                            newMapX = mapX;
                            if (!topBorder)
                                newMapY = mapY - speed;
                            else newMapY = mapY;
                            hitArea.x = newMapX + boundingBox.width / 4;
                            hitArea.y = newMapY + boundingBox.height / 3 * 2;
                            collision.scanCollision(this, gamepanel.currentMap);
                            if (collision.getNumCollision() == 0)
                                direction = "up";
                        }
                    }

                    if (direction.equals("up-right")) {
                        if (!rightBorder)
                            newMapX = mapX + speed;
                        else newMapX = mapX;
                        newMapY = mapY;
                        hitArea.x = newMapX + boundingBox.width / 4;
                        hitArea.y = newMapY + boundingBox.height / 3 * 2;
                        collision.scanCollision(this, gamepanel.currentMap);
                        if (collision.getNumCollision() == 0)
                            direction = "right";
                        else {
                            newMapX = mapX;
                            if (!topBorder)
                                newMapY = mapY - speed;
                            else newMapY = mapY;
                            hitArea.x = newMapX + boundingBox.width / 4;
                            hitArea.y = newMapY + boundingBox.height / 3 * 2;
                            collision.scanCollision(this, gamepanel.currentMap);
                            if (collision.getNumCollision() == 0)
                                direction = "up";
                        }
                    }
                    if (direction.equals("down-left")) {
                        if (!leftBorder)
                            newMapX = mapX - speed;
                        else newMapX = mapX;
                        newMapY = mapY;
                        hitArea.x = newMapX + boundingBox.width / 4;
                        hitArea.y = newMapY + boundingBox.height / 3 * 2;
                        collision.scanCollision(this, gamepanel.currentMap);
                        if (collision.getNumCollision() == 0) {
                            direction = "left";
                        } else {
                            newMapX = mapX;
                            if (!bottomBorder)
                                newMapY += speed;
                            else newMapY = mapY;
                            hitArea.x = newMapX + boundingBox.width / 4;
                            hitArea.y = newMapY + boundingBox.height / 3 * 2;
                            collision.scanCollision(this, gamepanel.currentMap);
                            if (collision.getNumCollision() == 0)
                                direction = "down";
                        }
                    }
                    if (direction.equals("down-right")) {
                        if (!rightBorder)
                            newMapX = mapX + speed;
                        else newMapX = mapX;
                        newMapY = mapY;
                        hitArea.x = newMapX + boundingBox.width / 4;
                        hitArea.y = newMapY + boundingBox.height / 3 * 2;
                        collision.scanCollision(this, gamepanel.currentMap);
                        if (collision.getNumCollision() == 0)
                            direction = "right";
                        else {
                            newMapX = mapX;
                            if (!bottomBorder)
                                newMapY += speed;
                            else newMapY = mapY;
                            hitArea.x = newMapX + boundingBox.width / 4;
                            hitArea.y = newMapY + boundingBox.height / 3 * 2;
                            collision.scanCollision(this, gamepanel.currentMap);
                            if (collision.getNumCollision() == 0)
                                direction = "down";
                        }
                    }
                }
            } else
                checkBug = true;

            if (checkBug || collision.getNumCollision() == 0) {
                mapX = newMapX;
                mapY = newMapY;
                switch (direction) {
                    case "up":
                        curr_animation_player = animation_player_UP;
                        break;
                    case "up-left":
                        curr_animation_player = animation_player_UP;
                        break;
                    case "down":
                        curr_animation_player = animation_player_DOWN;
                        break;
                    case "down-right":
                        curr_animation_player = animation_player_DOWN;
                        break;
                    case "left":
                        curr_animation_player = animation_player_LEFT;
                        break;
                    case "down-left":
                        curr_animation_player = animation_player_LEFT;
                        break;
                    case "up-right":
                        curr_animation_player = animation_player_RIGHT;
                        break;
                    case "right":
                        curr_animation_player = animation_player_RIGHT;
                        break;
                }
            } else {
                Tile[] tile = collision.getCollisionTile();
                ButtonInteract = false;
                for (int i = 0; i < collision.getNumCollision(); ++i) {
                    if (!tile[i].Type.equals("Obstacle")) {
                        ButtonInteract = true;
                        if (tile[i].Type.equals("Collected")) {
                            checkNameTile = tile[i].Name;
                            collision_collect = true;
                        }
                        if (tile[i].Type.equals("Teleport")) {
                            checkDescriptionTile = tile[i].Description;
                            checkTile = checkDescriptionTile;
                        }
                        if (tile[i].Type.equals("Interact")) {
                            checkInteractTile = tile[i].Description;
                            checkTile2 = checkInteractTile;
                        }
                        break;

                    }
                }

            }
        } else {
            switch (direction) {
                case "up":
                    curr_animation_player = animation_player_stand_BACK;
                    break;
                case "up-left":
                    curr_animation_player = animation_player_stand_BACK;
                    break;
                case "down":
                    curr_animation_player = animation_player_stand_FRONT;
                    break;
                case "down-right":
                    curr_animation_player = animation_player_stand_FRONT;
                    break;
                case "left":
                    curr_animation_player = animation_player_stand_LEFT;
                    break;
                case "down-left":
                    curr_animation_player = animation_player_stand_LEFT;
                    break;
                case "up-right":
                    curr_animation_player = animation_player_stand_RIGHT;
                    break;
                case "right":
                    curr_animation_player = animation_player_stand_RIGHT;
                    break;
            }
            direction = "stand";

        }
        posUpdate();

    }

    public void posUpdate() {
        boundingBox.x = min(screenX, mapX);
        boundingBox.y = min(screenY, mapY);
        boundingBox.x += (int) max(0,
                mapX - (gamepanel.currentMap.width - GamePanel.screenWidth / 2) + boundingBox.width / 2);
        boundingBox.y += (int) max(0,
                mapY - (gamepanel.currentMap.height - GamePanel.screenHeight / 2) + boundingBox.height / 2);

        hitArea.x = mapX + boundingBox.width / 4;
        hitArea.y = mapY + boundingBox.height / 3 * 2;
        leftBorder = (boundingBox.x <= 0);
        rightBorder = (boundingBox.x >= GamePanel.screenWidth - boundingBox.width);
        topBorder = (boundingBox.y <= 0);
        bottomBorder = (boundingBox.y >= GamePanel.screenHeight - boundingBox.height);
        // soundDoor();
    }

    public Rectangle getImagebox() {
        int x = (int) (mapX + (107.0 / 450) * boundingBox.width);
        int y = (int) (mapY + (19.0 / 450) * boundingBox.height);
        int width = (int) ((223.0 / 450) * boundingBox.width);
        int height = (int) ((407.0 / 450) * boundingBox.width);

        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics2D g2) {
        curr_animation_player.operation(g2);
    }
    // public void soundDoor() {
    // if(collision_teleport) {
    // if(checkSoundName.equals("Door") && gamepanel.keyH.isInteract) {
    // //System.out.println("hehe");
    // SoundManager.playSound("open_door");
    // }
    // }
    // }
}
