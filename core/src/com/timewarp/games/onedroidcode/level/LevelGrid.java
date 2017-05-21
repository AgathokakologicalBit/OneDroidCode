package com.timewarp.games.onedroidcode.level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.timewarp.engine.Direction;
import com.timewarp.engine.SceneManager;
import com.timewarp.engine.Vector2D;
import com.timewarp.engine.entities.GameObject;
import com.timewarp.engine.gui.GUI;
import com.timewarp.engine.gui.controls.PictureBox;
import com.timewarp.games.onedroidcode.AssetManager;
import com.timewarp.games.onedroidcode.objects.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

public class LevelGrid {

    public static final int TILE_SIZE = 128;

    public int width, height;

    private TextureRegion[][] floor;
    private LinkedList<TObject> objects;


    public static LevelGrid instance;

    public Player player;


    public LevelGrid(int width, int height) {
        LevelGrid.instance = this;

        this.width = width;
        this.height = height;

        this.floor = new TextureRegion[height][width];
        this.objects = new LinkedList<TObject>();

        this.add(this.player = new Player(), 0, 0);

        this.generateField(width, height);
    }

    private void generateField(int width, int height) {
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                PictureBox image = GameObject.instantiate(PictureBox.class);
                image.transform.setScale(new Vector2D(TILE_SIZE, TILE_SIZE));
                image.transform.moveTo(new Vector2D(x * TILE_SIZE, y * TILE_SIZE));

                image.setImage(AssetManager.floorGrassTexture);
            }
        }
    }


    public void update() {
        ArrayList<TObject> deleteQueue = new ArrayList<TObject>();

        for (TObject obj : objects) {
            if (!GameObject.exists(obj)) {
                deleteQueue.add(obj);
                continue;
            }

            if (obj.animator.isPlaying()) continue;

            // Object position is FLOAT
            // but getX/getY returns INTEGER value
            // for grid support
            obj.setX(obj.getX());
            obj.setY(obj.getY());
        }

        for (TObject obj : deleteQueue) {
            objects.remove(obj);
        }
    }

    public void draw() {
        GUI.endStaticBlock();
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (floor[y][x] != null) {
                    GUI.drawTextureRegion(
                            floor[y][x],
                            x * TILE_SIZE, y * TILE_SIZE,
                            TILE_SIZE, TILE_SIZE
                    );
                }
            }
        }

        for (TObject obj : objects) {
            GUI.drawTextureRegion(
                    obj.texture,
                    obj.transform.position.x * TILE_SIZE, obj.transform.position.y * TILE_SIZE,
                    TILE_SIZE, TILE_SIZE, obj.transform.rotation
            );
        }
        GUI.beginStaticBlock();
    }

    public void set(int x, int y, int type) {
        floor[y][x] = AssetManager.floorGrassTexture;
    }

    public TObject[] findObjectsByPos(int x, int y) {
        LinkedList<TObject> positionedObjects = new LinkedList<TObject>();

        for (TObject obj : objects)
            if (obj.getX() == x && obj.getY() == y)
                positionedObjects.add(obj);

        return positionedObjects.toArray(new TObject[positionedObjects.size()]);
    }

    public TObject findObjectByPos(int x, int y) {
        for (TObject obj : objects)
            if (obj.getX() == x && obj.getY() == y)
                return obj;

        return null;
    }

    public TObject findSolidObjectByPos(int x, int y) {
        for (TObject obj : objects)
            if (obj.solid && obj.getX() == x && obj.getY() == y)
                return obj;

        return null;
    }


    @SuppressWarnings({"unchecked", "SuspiciousToArrayCall"})
    public <TTO extends TObject> TTO[] findObjectsByType(Class<TTO> type) {
        LinkedList<TTO> typedObjects = new LinkedList<TTO>();

        for (TObject obj : objects)
            if (type.isAssignableFrom(obj.getClass()))
                typedObjects.add((TTO) obj);

        return typedObjects.toArray((TTO[]) Array.newInstance(type, typedObjects.size()));
    }


    public boolean isObjectSolid(int x, int y) {
        return findSolidObjectByPos(x, y) != null
                || x < 0 || y < 0
                || x >= width || y >= height;
    }

    public boolean isTileEmpty(int x, int y) {
        return findObjectByPos(x, y) == null;
    }

    public boolean move(TObject obj, int toX, int toY) {
        if (!objects.contains(obj)) return false;
        if (isObjectSolid(toX, toY)) return false;

        boolean tileIsEmpty = isTileEmpty(toX, toY);

        obj.animator.playAnimation("move_" + Direction.fromVector(toX - obj.getX(), toY - obj.getY()).toString());

        if (tileIsEmpty) return true;

        TObject[] targetTiles = findObjectsByPos(toX, toY);
        for (TObject tTile : targetTiles) {
            obj.onCollision(tTile);
            tTile.onCollision(obj);
        }

        return true;
    }

    public boolean moveBy(TObject obj, int byX, int byY) {
        return move(obj, obj.getX() + byX, obj.getY() + byY);
    }

    public boolean add(TObject object, int x, int y) {
        if (object.solid && isObjectSolid(x, y))
            return false;

        SceneManager.instance.addGameObject(object);
        this.objects.add(object);

        object.setActive(false);
        object.init();
        object.transform.position.set(x, y);

        return true;
    }

    public boolean isAnimated() {
        /*
        for (TObject obj : objects) {
            if (obj.animator.isPlaying()) {
                return true;
            }
        }
        return false;
        */
        return this.player.animator.isPlaying();
    }
}
