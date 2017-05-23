package com.timewarp.games.onedroidcode.level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.timewarp.engine.Direction;
import com.timewarp.engine.Vector2D;
import com.timewarp.engine.entities.GameObject;
import com.timewarp.engine.entities.components.ui.ImageRenderer;

public abstract class TObject extends GameObject {

    public LevelGrid grid;

    public TextureRegion texture;
    public Direction direction = Direction.UP;

    public boolean solid = true;

    private ImageRenderer renderer;


    @Override
    public void init() {
        super.init();
        this.renderer = this.addComponent(ImageRenderer.class);
    }


    public void update() {
        super.update();
        this.renderer.image = this.texture;
    }

    public void onCollision(TObject object) {
    }

    public int getX() {
        return (int) (this.transform.position.x / LevelGrid.TILE_SIZE + 0.5f);
    }

    public void setX(int x) {
        this.transform.moveTo(new Vector2D(x * LevelGrid.TILE_SIZE, transform.position.y));
    }

    public int getY() {
        return (int) (this.transform.position.y / LevelGrid.TILE_SIZE + 0.5f);
    }

    public void setY(int y) {
        this.transform.moveTo(new Vector2D(transform.position.x, y * LevelGrid.TILE_SIZE));
    }

    public Vector2D getXY() {
        return new Vector2D(getX(), getY());
    }

    public void setXY(Vector2D xy) {
        this.transform.moveTo(new Vector2D(xy.x * LevelGrid.TILE_SIZE, xy.y * LevelGrid.TILE_SIZE));
    }
}
