package com.timewarp.engine.entities.components.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.timewarp.engine.Vector2D;
import com.timewarp.engine.entities.Component;
import com.timewarp.engine.entities.GameObject;
import com.timewarp.engine.gui.GUI;


public class ImageRenderer extends Component {

    public TextureRegion image;
    public GUI.SizeMode sizeMode = GUI.SizeMode.Stretch;


    public ImageRenderer(GameObject gameObject) {
        super(gameObject);
    }


    @Override
    public void render() {
        if (image == null) return;

        final float imageWidth = image.getRegionWidth();
        final float imageHeight = image.getRegionHeight();
        final Vector2D imageSize = new Vector2D(imageWidth, imageHeight);

        Vector2D imageSizeNew, offset;

        switch (sizeMode) {
            case Normal:
                GUI.drawTextureRegion(
                        image,
                        transform.position.x, transform.position.y,
                        imageSize.x, imageSize.y);
                break;

            case Centered:
                // TODO: render big textures properly
                final Vector2D pos = transform.position.add(imageSize.div(2));
                GUI.drawTextureRegion(
                        image,
                        pos.x, pos.y,
                        imageSize.x, imageSize.y
                );
                break;

            case Fit:
                float controlRation = transform.scale.x / transform.scale.y;
                float imageRatio = imageWidth / imageHeight;

                if (imageRatio > controlRation)
                    imageRatio = controlRation;

                imageSizeNew = imageSize.mult(imageRatio);
                offset = transform.scale.sub(imageSizeNew).div(2);

                GUI.drawTextureRegion(
                        image,
                        offset.x, offset.y,
                        imageSizeNew.x, imageSizeNew.y
                );
                break;

            case Stretch:
                GUI.drawTextureRegion(
                        image,
                        transform.position.x, transform.position.y,
                        transform.scale.x, transform.scale.y,
                        transform.rotation
                );
                break;

            case Zoom:
                // TODO: render big textures properly
                controlRation = transform.scale.x / transform.scale.y;
                imageRatio = imageWidth / imageHeight;

                if (imageRatio < controlRation)
                    imageRatio = controlRation;

                imageSizeNew = imageSize.mult(imageRatio);
                offset = transform.scale.sub(imageSizeNew).div(2);

                GUI.drawTextureRegion(
                        image,
                        offset.x, offset.y,
                        imageSizeNew.x, imageSizeNew.y
                );
                break;
        }
    }
}
