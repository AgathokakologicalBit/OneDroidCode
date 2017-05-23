package com.timewarp.games.onedroidcode.editor;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.timewarp.games.onedroidcode.vsl.Node;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NodeController<T extends Node> {

    public final Class<T> nodeType;

    public final ArrayList<NodeIO> inputs;
    public final ArrayList<NodeIO> outputs;

    public String name;
    public String[] path;
    public String pathName;

    public TextureRegion texture;

    public NodeCellComponent parentCell;
    public NodeController next;


    public Node node;


    public NodeController(Class<T> nodeType) {
        this.nodeType = nodeType;

        this.inputs = new ArrayList<NodeIO>();
        this.outputs = new ArrayList<NodeIO>();

        this.identifyNodeType();
        this.extractMethods();
        this.identifyGroup();
    }

    private NodeController(NodeController<T> obj) {
        this.nodeType = obj.nodeType;
        this.inputs = new ArrayList<NodeIO>();
        for (NodeIO io : obj.inputs) {
            this.inputs.add(io.copy());
        }

        this.outputs = new ArrayList<NodeIO>();
        for (NodeIO io : obj.outputs) {
            this.outputs.add(io.copy());
        }

        this.name = obj.name;
        this.path = obj.path;
        this.pathName = obj.pathName;
    }

    private void identifyNodeType() {
        this.name = this.nodeType.getSimpleName()
                .replaceAll("([A-Z])", " $1")
                .toLowerCase()
                .replace("node", "")
                .trim();
        this.path = this.nodeType.getCanonicalName()
                .toLowerCase()
                .replace("com.timewarp.games.onedroidcode.vsl.nodes.", "")
                .split("\\.");
        this.pathName = this.path[this.path.length - 1];
    }

    private void extractMethods() {
        try {
            final Field[] fields = nodeType.getFields();

            for (final Field field : fields) {
                final String propertyName = field.getName();

                if (!propertyName.startsWith("in") && !propertyName.startsWith("out"))
                    continue;

                field.setAccessible(true);
                final NodeIO nodeIOField = new NodeIO(field);
                (propertyName.startsWith("in") ? inputs : outputs).add(nodeIOField);

                Logger.getAnonymousLogger().log(
                        Level.INFO,
                        String.format(
                                Locale.US,
                                "Reading property '%s' of node '%s' as '%s'",
                                propertyName,
                                nodeType.getSimpleName(),
                                nodeIOField.getDisplayName()
                        )
                );
            }
        } catch (final Exception e) {
            e.printStackTrace();

            throw new RuntimeException(
                    String.format(
                            Locale.US,
                            "Can not load node '%s'",
                            nodeType.getSimpleName()
                    )
            );
        }
    }

    private void identifyGroup() {

    }

    public NodeController copy() {
        return new NodeController(this);
    }

    public void reset() {
        parentCell = null;
        next = null;
    }
}
