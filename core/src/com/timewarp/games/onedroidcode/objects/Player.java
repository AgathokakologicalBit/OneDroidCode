package com.timewarp.games.onedroidcode.objects;

import com.timewarp.games.onedroidcode.AssetManager;
import com.timewarp.games.onedroidcode.level.TObject;

public class Player extends TObject {

    public Player() {
        this.texture = AssetManager.playerTexture;
    }
}
