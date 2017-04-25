package com.timewarp.games.onedroidcode.level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.timewarp.engine.Direction;
import com.timewarp.engine.gui.GUI;
import com.timewarp.games.onedroidcode.AssetManager;
import com.timewarp.games.onedroidcode.objects.Player;

import java.util.LinkedList;

public class LevelGrid {

    private int width, height;

    private TextureRegion[][] floor;
    private LinkedList<TObject> objects;


    public static LevelGrid instance;

    public TObject player;


    public LevelGrid(int width, int height) {
        this.width = width;
        this.height = height;

        this.floor = new TextureRegion[height][width];
        this.objects = new LinkedList<TObject>();

        this.objects.add(this.player = new Player());

        LevelGrid.instance = this;
    }

    public void update() {
        for (TObject obj : objects) {
            obj.update();
        }
    }

    public void draw() {
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (floor[y][x] != null) {
                    GUI.DrawTextureRegion(floor[y][x], x * 128, y * 128, 128, 128);
                }
            }
        }

        for (TObject obj : objects) {
            GUI.DrawTextureRegion(obj.texture, obj.x * 128, obj.y * 128, 128, 128, getRotation(obj));
        }
    }

    private float getRotation(TObject obj) {
        switch (obj.direction) {
            case DOWN:
                return 180;
            case UP:
                return 0;
            case RIGHT:
                return -90;
            case LEFT:
                return 90;
        }

        return 0;
    }

    public void set(int x, int y, int type) {
        floor[y][x] = AssetManager.floorTiledStoneTexture;
    }

    public TObject[] findObjectsByPos(int x, int y) {
        LinkedList<TObject> positionedObjects = new LinkedList<TObject>();
        for (TObject obj : objects)
            if (obj.x == x && obj.y == y)
                positionedObjects.add(obj);

        return positionedObjects.toArray(new TObject[positionedObjects.size()]);
    }

    public TObject findObjectByPos(int x, int y) {
        for (TObject obj : objects)
            if (obj.x == x && obj.y == y)
                return obj;

        return null;
    }

    public TObject findSolidObjectByPos(int x, int y) {
        for (TObject obj : objects)
            if (obj.solid && obj.x == x && obj.y == y)
                return obj;

        return null;
    }


    public boolean isObjectSolid(int x, int y) {
        return findSolidObjectByPos(x, y) != null;
    }

    public boolean isTileEmpty(int x, int y) {
        return findObjectByPos(x, y) == null;
    }

    public boolean move(TObject obj, int toX, int toY) {
        if (!objects.contains(obj)) return false;
        if (isObjectSolid(toX, toY)) return false;

        boolean tileIsEmpty = isTileEmpty(toX, toY);

        int diffX = Math.min(Math.max(toX - obj.x, -1), 1);
        int diffY = Math.min(Math.max(toY - obj.y, -1), 1);
        int diff = diffX + diffY * 10;

        switch (diff) {
            case 0:
                obj.direction = Direction.EMPTY;
                break;

            case 1:
                obj.direction = Direction.RIGHT;
                break;
            case -1:
                obj.direction = Direction.LEFT;
                break;
            case 10:
                obj.direction = Direction.DOWN;
                break;
            case -10:
                obj.direction = Direction.UP;
                break;
        }

        obj.x = toX;
        obj.y = toY;
        if (tileIsEmpty) return true;

        TObject[] targetTiles = findObjectsByPos(toX, toY);
        for (TObject tTile : targetTiles) {
            obj.onCollision(tTile);
            tTile.onCollision(obj);
        }

        return true;
    }

    public boolean moveBy(TObject obj, int byX, int byY) {
        return move(obj, obj.x + byX, obj.y + byY);
    }
}
