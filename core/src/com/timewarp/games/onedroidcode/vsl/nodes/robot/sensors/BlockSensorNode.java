package com.timewarp.games.onedroidcode.vsl.nodes.robot.sensors;

import com.timewarp.engine.Direction;
import com.timewarp.engine.Vector2D;
import com.timewarp.games.onedroidcode.level.LevelGrid;
import com.timewarp.games.onedroidcode.objects.Player;
import com.timewarp.games.onedroidcode.vsl.CodeRunner;
import com.timewarp.games.onedroidcode.vsl.Node;
import com.timewarp.games.onedroidcode.vsl.Value;

public class BlockSensorNode extends Node {

    @Override
    public Node execute(CodeRunner runner) {
        final Player player = LevelGrid.instance.player;
        final Direction dir = player.direction;
        final Vector2D position = player.transform.position;

        final Vector2D target = position.add(dir.getVector()).add(0.5f);

        final boolean isSolid = LevelGrid.instance.isObjectSolid((int) target.x, (int) target.y);
        runner.setFlag("boolean", new Value(Value.TYPE_BOOLEAN, isSolid));

        return next;
    }

    @Override
    public void reset() {

    }
}
