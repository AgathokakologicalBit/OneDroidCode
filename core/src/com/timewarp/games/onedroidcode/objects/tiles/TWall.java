package com.timewarp.games.onedroidcode.objects.tiles;

import com.timewarp.games.onedroidcode.AssetManager;
import com.timewarp.games.onedroidcode.level.TObject;

public class TWall extends TObject {

    public TWall() {
        this.texture = AssetManager.wallStoneTexture;
        this.solid = true;
    }

}
