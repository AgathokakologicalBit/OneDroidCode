package com.timewarp.games.onedroidcode.level;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.timewarp.engine.Vector2D;

public interface Level {

    int TICKS_PER_STEP = 7;

    String getSId();
    String getName();

    TextureRegion getTexture();

    Vector2D getSize();
    void loadTo(LevelGrid grid, int courseId);

    int getCoursesCount();
    CourseInfo getCourseInfo(int courseId);

}
