package com.timewarp.games.onedroidcode.level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.timewarp.engine.Direction;

public abstract class TObject {

    public TextureRegion texture;
    public int x, y;
    public Direction direction = Direction.UP;

    public boolean solid = true;

    public abstract void update();

    public void onCollision(TObject object) {
    }

    public boolean moveTo(int x, int y) {
        return LevelGrid.instance.move(this, x, y);
    }

    public void rotateBy(Direction direction) {
        this.direction = this.direction.rotatedBy(direction);
    }
}
