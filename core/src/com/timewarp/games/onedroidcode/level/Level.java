package com.timewarp.games.onedroidcode.level;


import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface Level {

    String getSId();

    String getName();

    TextureRegion getTexture();

    void loadTo(LevelGrid grid, int courseId);

    int getCoursesCount();

    CourseInfo getCourseInfo(int courseId);

}
