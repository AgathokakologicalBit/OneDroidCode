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

public class WinkingGateLevel implements Level {

    @Override
    public String getSId() {
        return "winking_gate";
    }

    @Override
    public String getName() {
        return "Winking gate";
    }

    @Override
    public TextureRegion getTexture() {
        return AssetManager.levelWinkingGateTexture;
    }

    @Override
    public Vector2D getSize() {
        return new Vector2D(10, 9);
    }

    @Override
    public void loadTo(LevelGrid grid, int courseId) {
        grid.player.setXY(new Vector2D(3, 1));


        // =- WALLS AROUND MAP -=
        for (int i = 0; i < 9; ++i) {
            grid.add(new TWall(), i + 1, 0);
            grid.add(new TWall(), 0, i);
            grid.add(new TWall(), i, 8);
            grid.add(new TWall(), 9, i + 1);
        }

        // =- COLUMNS AND INNER WALL -=
        for (int i = 2; i < 9; i += 2) {
            grid.add(new TWall(), 2, i);
            grid.add(new TWall(), 4, i);
            grid.add(new TWall(), 6, i);

            grid.add(new TWall(), 7, i); // inner wall
        }

        // =- GATES -=
        for (int i = 0; i < 4; ++i) {
            if (courseId == i) continue;
            grid.add(new TWall(), 7, i * 2 + 1);
        }

        // =- COLLECTIBLES -=
        grid.add(new CollectibleItem(), 8, 1);
        grid.add(new CollectibleItem(), 8, 7);
    }


    @Override
    public int getCoursesCount() {
        return 4;
    }

    @Override
    public CourseInfo getCourseInfo(final int courseId) {
        return new CourseInfo() {{
            id = courseId;
            name = String.format(Locale.US, "door #%d", id);

            minSteps = 3 + (7 * id) + 7;
            maxSteps = (3 + 10 * id) * TICKS_PER_STEP;
            data = id;
        }};
    }
}
