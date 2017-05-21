package com.timewarp.games.onedroidcode.objects;

import com.timewarp.engine.entities.GameObject;
import com.timewarp.games.onedroidcode.AssetManager;
import com.timewarp.games.onedroidcode.GameStats;
import com.timewarp.games.onedroidcode.level.TObject;

public class CollectibleItem extends TObject {

    public CollectibleItem() {
        this.solid = false;
    }

    @Override
    public void init() {
        super.init();

        this.texture = AssetManager.collectibleTexture;
        this.animator.playAnimation("collectible");
        this.animator.setLooping(true);
    }

    @Override
    public void onCollision(TObject object) {
        GameObject.destroy(this);
        GameStats.addScore(1);
    }
}
