package com.timewarp.games.onedroidcode.level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.timewarp.engine.Direction;
import com.timewarp.engine.Vector2D;
import com.timewarp.engine.entities.GameObject;

public abstract class TObject extends GameObject {

    public TextureRegion texture;
    public Direction direction = Direction.UP;

    public boolean solid = true;

    public void update() {
        super.update();
    }

    public void onCollision(TObject object) {
    }

    public boolean moveTo(int x, int y) {
        return LevelGrid.instance.move(this, x, y);
    }

    public void rotateBy(Direction direction) {
        this.direction = this.direction.rotatedBy(direction);
        this.animator.playAnimation("rotate_" + direction.toString());
    }

    public int getX() {
        return (int) (this.transform.position.x + 0.5f);
    }

    public void setX(int x) {
        this.transform.moveTo(new Vector2D(x, transform.position.y));
    }

    public int getY() {
        return (int) (this.transform.position.y + 0.5f);
    }

    public void setY(int y) {
        this.transform.moveTo(new Vector2D(transform.position.x, y));
    }
}
