package com.timewarp.engine.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.timewarp.engine.Vector2D;

public class GUI {

    public static Texture emptyTexture;

    public static SpriteBatch batch;

    public static boolean isLastTouched = false;
    public static boolean isTouched = false;
    public static Vector2D touchPosition = new Vector2D();

    public static int Width;
    public static int Height;

    public static BitmapFont font;

    private static GlyphLayout layout;

    private static float baseTextSize;


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
        emptyTexture = new Texture(pix);

        baseTextSize = getTextSize("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()").y;
    }


    /**
     * Returns size in pixels of given string
     *
     * @param text Target string
     * @return Text's size in pixels
     */
    public static Vector2D getTextSize(String text) {
        font.getData().setScale(1);
        layout.setText(font, text);
        return new Vector2D(layout.width, layout.height);
    }

    /**
     * Returns size in pixels of given string
     *
     * @param text  Target string
     * @param scale Text scale multiplier
     * @return Text's size in pixels
     */
    public static Vector2D getTextSize(String text, float scale) {
        font.getData().setScale(baseTextSize / scale);
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
    public static void DrawRectangle(Vector2D position, Vector2D size, float thickness, Color color) {
        DrawRectangle(position.x, position.y, size.x, size.y, thickness, color);
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
    public static void DrawRectangle(float x, float y, float width, float height, float thickness) {
        DrawRectangle(x, y, width, height, thickness, Color.BLACK);
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
    public static void DrawRectangle(float x, float y, float width, float height, float thickness, Color color) {
        DrawPanel(x - thickness, y - thickness, width + thickness * 2, thickness, color);
        DrawPanel(x - thickness, y + height, width + thickness * 2, thickness, color);
        DrawPanel(x - thickness, y + thickness, thickness, height - thickness * 2, color);
        DrawPanel(x + width, y + thickness, thickness, height - thickness * 2, color);
    }


    /**
     * Draws panel at given position with specified size and default color
     *
     * @param position Position in pixels
     * @param size     Size in pixels
     */
    public static void DrawPanel(Vector2D position, Vector2D size) {
        DrawPanel(position.x, position.y, size.x, size.y, Color.LIGHT_GRAY);
    }

    /**
     * Draws panel at given position with specified size and color
     *
     * @param position Position in pixels
     * @param size     Size in pixels
     * @param color    Background color
     */
    public static void DrawPanel(Vector2D position, Vector2D size, Color color) {
        DrawPanel(position.x, position.y, size.x, size.y, color);
    }

    /**
     * Draws panel at given position with specified size and default color
     *
     * @param x      Horizontal position in pixels
     * @param y      Vertical position in pixels
     * @param width  Width in pixels
     * @param height Height in pixels
     */
    public static void DrawPanel(float x, float y, float width, float height) {
        DrawPanel(x, y, width, height, Color.LIGHT_GRAY);
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
    public static void DrawPanel(float x, float y, float width, float height, Color color) {
        batch.setColor(color);
        batch.draw(emptyTexture, x, Height - y, width, -height);
        batch.setColor(Color.WHITE);
    }


    /**
     * Draws text at given position with default size and color
     * @param text Text to draw
     * @param position Position in pixels
     */
    public static void DrawText(String text, Vector2D position) {
        DrawText(text, position.x, position.y, Color.WHITE, 16);
    }

    /**
     * Draws text at given position with specified size and default color
     * @param text Text to draw
     * @param position Position in pixels
     * @param size Font size multiplier
     */
    public static void DrawText(String text, Vector2D position, float size) {
        DrawText(text, position.x, position.y, Color.WHITE, size);
    }

    /**
     * Draws text at given position with specified color and default size
     * @param text Text to draw
     * @param position Position in pixels
     * @param color Text color
     */
    public static void DrawText(String text, Vector2D position, Color color) {
        DrawText(text, position.x, position.y, color, 16);
    }

    /**
     * Draws text at given position with specified size and color
     * @param text Text to draw
     * @param position Position in pixels
     * @param size Font size multiplier
     * @param color Text color
     */
    public static void DrawText(String text, Vector2D position, float size, Color color) {
        DrawText(text, position.x, position.y, color, size);
    }

    /**
     * Draws text at given position with default size and color
     * @param text Text to draw
     * @param x Horizontal position in pixels
     * @param y Vertical position in pixels
     */
    public static void DrawText(String text, float x, float y) {
        DrawText(text, x, y, Color.WHITE, 16);
    }

    /**
     * Draws text at given position with specified size and default color
     * @param text Text to draw
     * @param x Horizontal position in pixels
     * @param y Vertical position in pixels
     * @param size Font size multiplier
     */
    public static void DrawText(String text, float x, float y, float size) {
        DrawText(text, x, y, Color.WHITE, size);
    }

    /**
     * Draws text at given position with specified color and default size
     * @param text Text to draw
     * @param x Horizontal position in pixels
     * @param y Vertical position in pixels
     * @param color Text color
     */
    public static void DrawText(String text, float x, float y, Color color) {
        DrawText(text, x, y, color, 16);
    }

    /**
     * Draws text at given position with default size and color
     * @param text Text to draw
     * @param x Horizontal position in pixels
     * @param y Vertical position in pixels
     * @param width Text container width in pixels
     * @param height Text container height in pixels
     */
    public static void DrawText(String text, float x, float y, float width, float height) {
        DrawText(text, x, y, width, height, 16, Color.WHITE);
    }

    /**
     * Draws text at given position with given container size, text scale and color
     * @param text Text to draw
     * @param x Horizontal position in pixels
     * @param y Vertical position in pixels
     * @param width Text container width in pixels
     * @param height Text container height in pixels
     * @param scale Text scale multiplier
     * @param color Text color
     */
    public static void DrawText(String text, float x, float y, float width, float height, float scale, Color color) {
        final Vector2D textSize = getTextSize(text, scale);
        final float offset_left = (width - textSize.x) / 2;
        final float offset_top = (height - textSize.y / 2) / 2;

        DrawText(text, x + offset_left, y + offset_top, color, scale);
    }

    /**
     * Draws text at given position with specified container size, text scale and color
     * @param text Text to draw
     * @param position Position in pixels
     * @param size Container size in pixels
     * @param scale Text scale multiplier
     * @param color Text color
     */
    public static void DrawText(String text, Vector2D position, Vector2D size, float scale, Color color) {
        DrawText(text, position.x, position.y, size.x, size.y, scale, color);
    }

    /**
     * Draws text at given position with specified color and size
     * @param text Text to draw
     * @param x Horizontal position in pixels
     * @param y Vertical position in pixels
     * @param color Text color
     * @param scale Text scale modifier
     */
    public static void DrawText(String text, float x, float y, Color color, float scale) {
        font.setColor(color);
        font.getRegion().getTexture().setFilter(
                Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear
        );

        final float iscale = scale / baseTextSize;
        font.getData().setScale(iscale);
        font.draw(batch, text, x, Height - y);
    }


    /**
     * Draws texture at given position with specified size
     * @param texture Texture to draw
     * @param x  Horizontal position in pixels
     * @param y Vertical position in pixels
     * @param width Texture width in pixels
     * @param height Texture height in pixels
     */
    public static void DrawTexture(Texture texture, float x, float y, float width, float height) {
        batch.draw(texture, x, Height - y - height, width, height);
    }

    /**
     * Draws texture at given position with specified size
     * @param texture Texture to draw
     * @param position  Position in pixels
     * @param size Texture size in pixels
     */
    public static void DrawTexture(Texture texture, Vector2D position, Vector2D size) {
        DrawTexture(texture, position.x, position.y, size.x, size.y);
    }

    // TODO: Create other drawing methods
}