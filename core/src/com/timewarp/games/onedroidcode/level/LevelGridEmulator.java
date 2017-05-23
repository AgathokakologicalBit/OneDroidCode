package com.timewarp.games.onedroidcode.level;

import com.timewarp.engine.Direction;
import com.timewarp.engine.Vector2D;

import java.util.ArrayList;

public class LevelGridEmulator extends LevelGrid {

    public LevelGridEmulator(int width, int height) {
        super(width, height);
    }

    @Override
    protected void generateField(int width, int height) {
    }

    @Override
    public boolean move(TObject obj, int toX, int toY) {
        if (!objects.contains(obj)) return false;
        if (isObjectSolid(toX, toY)) return false;

        obj.setXY(new Vector2D(toX, toY));

        if (isTileEmpty(toX, toY)) return true;

        TObject[] targetTiles = findObjectsByPos(toX, toY);
        for (TObject tTile : targetTiles) {
            obj.onCollision(tTile);
            tTile.onCollision(obj);
        }

        return true;
    }

    @Override
    public void update() {
        ArrayList<TObject> deleteQueue = new ArrayList<TObject>();
        for (TObject obj : objects) {
            if (obj.isDestroyed) {
                deleteQueue.add(obj);
            }
        }

        for (TObject obj : deleteQueue) {
            objects.remove(obj);
        }
    }

    @Override
    public boolean add(TObject object, int x, int y) {
        if (object.solid && isObjectSolid(x, y))
            return false;

        this.objects.add(object);

        object.grid = this;
        object.init();
        object.transform.moveTo(new Vector2D(x * TILE_SIZE, y * TILE_SIZE));

        return true;
    }

    @Override
    public void rotateBy(TObject obj, Direction direction) {
        obj.direction = obj.direction.rotatedBy(direction);
        obj.transform.setRotation(obj.direction.toRadians());
    }
}
