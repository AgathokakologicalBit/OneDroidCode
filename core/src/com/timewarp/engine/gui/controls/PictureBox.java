package com.timewarp.engine.gui.controls;

import com.badlogic.gdx.graphics.Texture;
import com.timewarp.engine.Vector2D;
import com.timewarp.engine.gui.GUI;
import com.timewarp.engine.gui.GUIControl;

public class PictureBox extends GUIControl {

    public Texture image;
    public GUI.SizeMode sizeMode = GUI.SizeMode.Normal;

    public PictureBox() {
        super();
    }

    public PictureBox(float x, float y) {
        super(x, y);
    }

    public PictureBox(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    public void render() {
        super.render();

        if (image == null) return;

        final float imageWidth = image.getWidth();
        final float imageHeight = image.getHeight();
        final Vector2D imageSize = new Vector2D(imageWidth, imageHeight);

        Vector2D imageSizeNew, offset;

        switch (sizeMode)
        {
            case Normal:
                GUI.DrawTexture(image, position, imageSize);
                break;

            case Centered:
                // TODO: render big textures properly
                GUI.DrawTexture(image, position.add(imageSize.div(2)), imageSize);
                break;

            case Fit:
                float controlRation = size.x / size.y;
                float imageRatio = imageWidth / imageHeight;

                if (imageRatio > controlRation)
                    imageRatio = controlRation;

                imageSizeNew = imageSize.mult(imageRatio);
                offset = size.sub(imageSizeNew).div(2);

                GUI.DrawTexture(image, offset, imageSizeNew);
                break;

            case Stretch:
                GUI.DrawTexture(image, position, size);
                break;

            case Zoom:
                // TODO: render big textures properly
                controlRation = size.x / size.y;
                imageRatio = imageWidth / imageHeight;

                if (imageRatio < controlRation)
                    imageRatio = controlRation;

                imageSizeNew = imageSize.mult(imageRatio);
                offset = size.sub(imageSizeNew).div(2);

                GUI.DrawTexture(image, offset, imageSizeNew);
                break;
        }
    }
}