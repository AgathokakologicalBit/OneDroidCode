package com.timewarp.engine.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.timewarp.engine.Math.Mathf;
import com.timewarp.engine.Vector2D;

public class GUI {

    public static TextureRegion emptyTexture;

    public static SpriteBatch batch;

    public static boolean isLastTouched = false;
    public static boolean isTouched = false;
    public static Vector2D touchPosition = new Vector2D();
    public static Vector2D touchStartPosition = new Vector2D();
    public static Vector2D lastTouchPosition = new Vector2D();

    public static int Width;
    public static int Height;

    public static BitmapFont font;

    private static GlyphLayout layout;

    private static float baseTextSize;


    public static Vector2D cameraPosition = new Vector2D();
    private static Vector2D cameraPositionLast = new Vector2D();

    public static float timeSinceTouchStart;
    public static boolean isClicked;
    public static boolean isLongClicked;
    public static boolean isLastLongClicked;


    public static void resetCameraPosition() {
        cameraPosition.set(0, 0);
    }

    public static void translateBy(Vector2D direction) {
        cameraPosition.set(cameraPosition.add(direction));
    }

    public static void moveCamera(float x, float y) {
        cameraPosition.set(x, y);
    }

    public static void beginStaticBlock() {
        cameraPositionLast = cameraPosition.copy();
        cameraPosition.set(0, 0);
    }

    public static void endStaticBlock() {
        cameraPosition = cameraPositionLast;
    }


    /**
     * Size mode
     * Used for drawing images
     */
    public enum SizeMode {
        Normal,
        Centered,
        Fit,
        Stretch,
        Zoom
    }

    /**
     * Specifies control orientation if it is supported
     */
    public enum Orientation {
        Horizontal,
        Vertical
    }


    /**
     * Initializes GUI
     * Loads font and preparing UI for drawing
     */
    public static void init() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/defaultFont.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 48;
        font = generator.generateFont(parameter);
        generator.dispose();

        layout = new GlyphLayout();

        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(1, 1, 1, 1);
        pix.fill();
        emptyTexture = new TextureRegion(new Texture(pix));


        // CALCULATING BASE TEXT SIZE
        final String exampleText = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()";
        font.getData().setScale(1);
        layout.setText(font, exampleText);
        baseTextSize = layout.height;
    }


    /**
     * Returns size in pixels of given string
     *
     * @param text  Target string
     * @param scale Text scale multiplier
     * @return Text's size in pixels
     */
    public static Vector2D getTextSize(String text, float scale) {
        font.getData().setScale(scale / baseTextSize);
        layout.setText(font, text);
        return new Vector2D(layout.width, layout.height);
    }


    /**
     * Draws rectangle shape at given position with specified size, thickness and color
     *
     * @param position  Position in pixels
     * @param size      Size in pixels
     * @param thickness Width of border
     * @param color     Shape color
     */
    public static void drawRectangle(Vector2D position, Vector2D size, float thickness, Color color) {
        drawRectangle(position.x, position.y, size.x, size.y, thickness, color);
    }

    /**
     * Draws rectangle shape at given position with specified size and thickness
     *
     * @param x         Horizontal position in pixels
     * @param y         Vertical position in pixels
     * @param width     Width in pixels
     * @param height    Height in pixels
     * @param thickness Width of border
     */
    public static void drawRectangle(float x, float y, float width, float height, float thickness) {
        drawRectangle(x, y, width, height, thickness, Color.BLACK);
    }

    /**
     * Draws rectangle shape at given position with specified size, thickness and color
     *
     * @param x         Horizontal position in pixels
     * @param y         Vertical position in pixels
     * @param width     Width in pixels
     * @param height    Height in pixels
     * @param thickness Width of border
     * @param color     Shape color
     */
    public static void drawRectangle(float x, float y, float width, float height, float thickness, Color color) {
        drawPanel(x - thickness, y - thickness, width + thickness * 2, thickness, color);
        drawPanel(x - thickness, y + height, width + thickness * 2, thickness, color);
        drawPanel(x - thickness, y, thickness, height, color);
        drawPanel(x + width, y, thickness, height, color);
    }


    /**
     * Draws panel at given position with specified size and default color
     *
     * @param position Position in pixels
     * @param size     Size in pixels
     */
    public static void drawPanel(Vector2D position, Vector2D size) {
        drawPanel(position.x, position.y, size.x, size.y, Color.LIGHT_GRAY);
    }

    /**
     * Draws panel at given position with specified size and color
     *
     * @param position Position in pixels
     * @param size     Size in pixels
     * @param color    Background color
     */
    public static void drawPanel(Vector2D position, Vector2D size, Color color) {
        drawPanel(position.x, position.y, size.x, size.y, color);
    }

    /**
     * Draws panel at given position with specified size and default color
     *
     * @param x      Horizontal position in pixels
     * @param y      Vertical position in pixels
     * @param width  Width in pixels
     * @param height Height in pixels
     */
    public static void drawPanel(float x, float y, float width, float height) {
        drawPanel(x, y, width, height, Color.LIGHT_GRAY);
    }

    /**
     * Draws panel at given position with specified size and color
     *
     * @param x      Horizontal position in pixels
     * @param y      Vertical position in pixels
     * @param width  Width in pixels
     * @param height Height in pixels
     * @param color  Background color
     */
    public static void drawPanel(float x, float y, float width, float height, Color color) {
        batch.setColor(color);
        batch.draw(emptyTexture, x + cameraPosition.x, Height - (y + cameraPosition.y), width, -height);
        batch.setColor(Color.WHITE);
    }


    /**
     * Draws text at given position with default size and color
     * @param text Text to render
     * @param position Position in pixels
     */
    public static void drawText(String text, Vector2D position) {
        drawText(text, position.x, position.y, Color.WHITE, 16);
    }

    /**
     * Draws text at given position with specified size and default color
     * @param text Text to render
     * @param position Position in pixels
     * @param size Font size multiplier
     */
    public static void drawText(String text, Vector2D position, float size) {
        drawText(text, position.x, position.y, Color.WHITE, size);
    }

    /**
     * Draws text at given position with specified color and default size
     * @param text Text to render
     * @param position Position in pixels
     * @param color Text color
     */
    public static void drawText(String text, Vector2D position, Color color) {
        drawText(text, position.x, position.y, color, 16);
    }

    /**
     * Draws text at given position with specified size and color
     * @param text Text to render
     * @param position Position in pixels
     * @param size Font size multiplier
     * @param color Text color
     */
    public static void drawText(String text, Vector2D position, float size, Color color) {
        drawText(text, position.x, position.y, color, size);
    }

    /**
     * Draws text at given position with default size and color
     * @param text Text to render
     * @param x Horizontal position in pixels
     * @param y Vertical position in pixels
     */
    public static void drawText(String text, float x, float y) {
        drawText(text, x, y, Color.WHITE, 16);
    }

    /**
     * Draws text at given position with specified size and default color
     * @param text Text to render
     * @param x Horizontal position in pixels
     * @param y Vertical position in pixels
     * @param size Font size multiplier
     */
    public static void drawText(String text, float x, float y, float size) {
        drawText(text, x, y, Color.WHITE, size);
    }

    /**
     * Draws text at given position with specified color and default size
     * @param text Text to render
     * @param x Horizontal position in pixels
     * @param y Vertical position in pixels
     * @param color Text color
     */
    public static void drawText(String text, float x, float y, Color color) {
        drawText(text, x, y, color, 16);
    }

    /**
     * Draws text at given position with default size and color
     * @param text Text to render
     * @param x Horizontal position in pixels
     * @param y Vertical position in pixels
     * @param width Text container width in pixels
     * @param height Text container height in pixels
     */
    public static void drawText(String text, float x, float y, float width, float height) {
        drawText(text, x, y, width, height, Color.WHITE);
    }

    /**
     * Draws text at given position with given container size, text scale and color
     * @param text Text to render
     * @param x Horizontal position in pixels
     * @param y Vertical position in pixels
     * @param width Text container width in pixels
     * @param height Text container height in pixels
     * @param scale Text scale multiplier
     * @param color Text color
     */
    public static void drawText(String text, float x, float y, float width, float height, float scale, Color color) {
        if (text == null || text.isEmpty()) return;
        final float offset_top = (height - baseTextSize) / 2;

        drawText(text, x, y + offset_top, color, scale);
    }

    /**
     * Draws text at given position with specified container size, text scale and color
     * @param text Text to render
     * @param position Position in pixels
     * @param size Container size in pixels
     * @param scale Text scale multiplier
     * @param color Text color
     */
    public static void drawText(String text, Vector2D position, Vector2D size, float scale, Color color) {
        drawText(text, position.x, position.y, size.x, size.y, scale, color);
    }

    /**
     * Draws text at given position with specified color and size
     * @param text Text to render
     * @param x Horizontal position in pixels
     * @param y Vertical position in pixels
     * @param color Text color
     * @param scale Text scale modifier
     */
    public static void drawText(String text, float x, float y, Color color, float scale) {
        if (text == null || text.isEmpty()) return;
        if (scale <= 0) return;

        font.setColor(color);
        font.getRegion().getTexture().setFilter(
                Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear
        );

        final float iscale = scale / baseTextSize;
        font.getData().setScale(iscale);
        font.draw(batch, text, x + cameraPosition.x, Height - (y + cameraPosition.y));
    }

    public static void drawText(String text, float x, float y, float width, float height, Color color) {
        drawText(text, x, y, width, height, color, true);
    }

    public static void drawText(String text, float x, float y, float width, float height, Color color, boolean center) {
        final float size = Math.min(width / getTextSize(text, 1f).x, height / 2);
        if (size == 0) return;
        drawText(text, x, y, width, height, size, color, center);
    }

    public static void drawText(String t, float x, float y, float w, float h, float s, Color c, boolean ch) {
        if (ch) {
            final float tw = getTextSize(t, s).x;
            drawText(t, x + (w - tw) / 2, y, tw, h, s, c);
        } else {
            drawText(t, x, y, w, h, s, c);
        }
    }


    /**
     * Draws texture at given position with specified size
     * @param texture Texture to render
     * @param x  Horizontal position in pixels
     * @param y Vertical position in pixels
     * @param width Texture width in pixels
     * @param height Texture height in pixels
     */
    public static void drawTexture(Texture texture, float x, float y, float width, float height) {
        if (texture == null) return;

        batch.draw(texture, x + cameraPosition.x, Height - (y + cameraPosition.y) - height, width, height);
    }

    /**
     * Draws texture at given position with specified size
     * @param texture Texture to render
     * @param position  Position in pixels
     * @param size Texture size in pixels
     */
    public static void drawTexture(Texture texture, Vector2D position, Vector2D size) {
        drawTexture(texture, position.x, position.y, size.x, size.y);
    }

    public static void drawTextureRegion(TextureRegion texture, float x, float y, float width, float height) {
        if (texture == null) return;

        batch.draw(texture, x + cameraPosition.x, Height - (y + cameraPosition.y) - height, width, height);
    }

    public static void drawTextureRegion(TextureRegion texture, float x, float y, float width, float height, float rotation) {
        if (texture == null) return;

        batch.draw(
                texture, x + cameraPosition.x, Height - (y + cameraPosition.y) - height,
                width / 2, height / 2,
                width, height,
                1f, 1f,
                rotation * 180 / Mathf.PI
        );
    }

    public static void drawLine(float x1, float y1, float x2, float y2, float thickness, Color c) {
        float dx = x2 - x1;
        float dy = y1 - y2;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        float rad = (float) Math.atan2(dy, dx);

        batch.setColor(c);
        batch.draw(
                emptyTexture, x1 + cameraPosition.x, Height - (y1 + cameraPosition.y),
                0, 0,
                dist, thickness,
                1f, 1f,
                rad * 180 / Mathf.PI
        );
        batch.setColor(Color.WHITE);
    }
}