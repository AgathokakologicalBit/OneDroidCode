package com.timewarp.games.onedroidcode.level.levels.basic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.timewarp.engine.Direction;
import com.timewarp.engine.Vector2D;
import com.timewarp.games.onedroidcode.AssetManager;
import com.timewarp.games.onedroidcode.level.CourseInfo;
import com.timewarp.games.onedroidcode.level.Level;
import com.timewarp.games.onedroidcode.level.LevelGrid;
import com.timewarp.games.onedroidcode.objects.CollectibleItem;
import com.timewarp.games.onedroidcode.objects.tiles.TWall;

import java.util.Locale;

public class SpiralBlockLevel implements Level {

    @Override
    public String getSId() {
        return "spiral_block";
    }

    @Override
    public String getName() {
        return "Spiral block";
    }

    @Override
    public TextureRegion getTexture() {
        return AssetManager.levelSpiralBlockTexture;
    }

    @Override
    public Vector2D getSize() {
        return new Vector2D(9, 8);
    }

    @Override
    public void loadTo(LevelGrid grid, int courseId) {
        Direction dir = Direction.RIGHT;
        int distance = courseId + 1;
        final Vector2D pos = new Vector2D();

        for (int i = 0; i <= courseId; ++i) {
            pos.set(pos.add(dir.getVector().mult(distance).add(0.5f)));
            pos.set((int) pos.x, (int) pos.y);

            grid.add(new CollectibleItem(), (int) pos.x, (int) pos.y);
            grid.add(
                    new TWall(),
                    (int) (pos.x + dir.getVector().x + 0.5f),
                    (int) (pos.y + dir.getVector().y + 0.5f)
            );

            dir = dir.rotatedBy(Direction.RIGHT);
            distance -= 1;
        }
    }

    @Override
    public int getCoursesCount() {
        return 7;
    }

    @Override
    public CourseInfo getCourseInfo(final int courseId) {

        return new CourseInfo() {{
            id = courseId;
            name = String.format(Locale.US, "%d line%s spiral", id + 1, id == 0 ? "" : "s");

            minSteps = (id * id + 3 * id + 3) / 2;
            maxSteps = (id * id + 3 * id + 3) / 2 * TICKS_PER_STEP;
            data = id;
        }};
    }
}
