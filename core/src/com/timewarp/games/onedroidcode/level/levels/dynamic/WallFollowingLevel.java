package com.timewarp.games.onedroidcode.level.levels.dynamic;

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
    public CourseInfo getCourseInfo(int courseId) {
        CourseInfo info = new CourseInfo();

        final int w = courseId % 4 + 1;
        final int h = courseId / 4 + 1;

        info.name = String.format(Locale.US, "%dx%d", h, w);
        info.id = courseId;

        info.maxSteps = (w + h + 2) * 2 * 10;
        info.data = new Vector2D(w, h);

        return info;
    }
}
