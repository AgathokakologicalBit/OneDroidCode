package com.timewarp.games.onedroidcode.level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.timewarp.engine.gui.GUI;
import com.timewarp.games.onedroidcode.AssetManager;
import com.timewarp.games.onedroidcode.objects.Player;

import java.util.LinkedList;

public class LevelGrid {

    public static final int TILE_SIZE = 128;

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
                    GUI.DrawTextureRegion(
                            floor[y][x],
                            x * TILE_SIZE, y * TILE_SIZE,
                            TILE_SIZE, TILE_SIZE
                    );
                }
            }
        }

        for (TObject obj : objects) {
            GUI.DrawTextureRegion(
                    obj.texture,
                    obj.x * TILE_SIZE, obj.y * TILE_SIZE,
                    TILE_SIZE, TILE_SIZE, getRotation(obj)
            );
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

            case UP_RIGHT:
                return -45;
            case UP_LEFT:
                return 45;
            case DOWN_LEFT:
                return 135;
            case DOWN_RIGHT:
                return -135;
        }

        return 0;
    }

    public void set(int x, int y, int type) {
        floor[y][x] = AssetManager.floorGrassTexture;
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


    @SuppressWarnings({"unchecked", "SuspiciousToArrayCall"})
    public <TTO extends TObject> TTO[] findObjectsByType(Class<TTO> type) {
        LinkedList<TTO> typedObjects = new LinkedList<TTO>();

        for (TObject obj : objects)
            if (type.isAssignableFrom(obj.getClass()))
                typedObjects.add((TTO) obj);

        return (TTO[]) typedObjects.toArray(new Object[typedObjects.size()]);
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

    public boolean add(TObject object, int x, int y) {
        if (object.solid && isObjectSolid(x, y))
            return false;

        this.objects.add(object);
        object.x = x;
        object.y = y;

        return true;
    }
}
