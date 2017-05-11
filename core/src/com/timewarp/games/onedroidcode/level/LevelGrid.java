package com.timewarp.games.onedroidcode.level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.timewarp.engine.Direction;
import com.timewarp.engine.Vector2D;
import com.timewarp.engine.entities.GameObject;
import com.timewarp.engine.gui.GUI;
import com.timewarp.engine.gui.controls.PictureBox;
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
        LevelGrid.instance = this;

        this.width = width;
        this.height = height;

        this.floor = new TextureRegion[height][width];
        this.objects = new LinkedList<TObject>();

        this.add(this.player = new Player(), 0, 0);

        this.generateField(width, height);
    }

    private void generateField(int width, int height) {
        final int BLOCK_SIZE = 128;

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                PictureBox image = GameObject.instantiate(PictureBox.class);
                image.transform.setScale(new Vector2D(BLOCK_SIZE, BLOCK_SIZE));
                image.transform.moveTo(new Vector2D(x * BLOCK_SIZE, y * BLOCK_SIZE));

                image.setImage(AssetManager.floorGrassTexture);
            }
        }
    }


    public void update() {
        for (TObject obj : objects) {
            obj.update();
            if (obj.animator.isPlaying()) continue;

            // Object position is FLOAT
            // but getX/getY returns INTEGER value
            // for grid support
            obj.setX(obj.getX());
            obj.setY(obj.getY());
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
                    obj.transform.position.x * TILE_SIZE, obj.transform.position.y * TILE_SIZE,
                    TILE_SIZE, TILE_SIZE, obj.transform.rotation
            );
        }
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

        this.objects.add(object);
        object.transform.position.set(x, y);
        object.init();

        return true;
    }

    public boolean isAnimated() {
        for (TObject obj : objects) {
            if (obj.animator.isPlaying()) {
                return true;
            }
        }
        return false;
    }
}
