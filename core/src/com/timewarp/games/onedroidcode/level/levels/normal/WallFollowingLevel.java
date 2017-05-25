package com.timewarp.games.onedroidcode.level.levels.normal;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.timewarp.engine.Vector2D;
import com.timewarp.games.onedroidcode.AssetManager;
import com.timewarp.games.onedroidcode.level.CourseInfo;
import com.timewarp.games.onedroidcode.level.Level;
import com.timewarp.games.onedroidcode.level.LevelGrid;
import com.timewarp.games.onedroidcode.objects.CollectibleItem;
import com.timewarp.games.onedroidcode.objects.tiles.TWall;

import java.util.Locale;

public class WallFollowingLevel implements Level {


    @Override
    public Vector2D getSize() {
        return new Vector2D(10, 10);
    }

    @Override
    public String getSId() {
        return "wall_follower";
    }

    @Override
    public String getName() {
        return "Wall follower";
    }

    @Override
    public TextureRegion getTexture() {
        return AssetManager.levelWallFollowerTexture;
    }

    @Override
    public void loadTo(LevelGrid grid, int courseId) {
        grid.player.setX(2);
        grid.player.setY(1);

        final CourseInfo course = getCourseInfo(courseId);
        final Vector2D size = (Vector2D) course.data;

        final int width = (int) size.x;
        final int height = (int) size.y;

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                grid.add(new TWall(), x + 3, y + 2);
            }
        }

        grid.add(new CollectibleItem(), 3 + width, 1);
        grid.add(new CollectibleItem(), 3 + width, 2 + height);
        grid.add(new CollectibleItem(), 2, 2 + height);
    }

    @Override
    public int getCoursesCount() {
        return 16;
    }

    @Override
    public CourseInfo getCourseInfo(final int courseId) {
        final int w = courseId % 4 + 1;
        final int h = courseId / 4 + 1;

        return new CourseInfo() {{
            id = courseId;
            name = String.format(Locale.US, "%dx%d", h, w);

            minSteps = w * 3 + h + 5;
            maxSteps = (w * 2 + h + 2) * 4 * TICKS_PER_STEP;
            data = new Vector2D(w, h);
        }};
    }
}
