package com.timewarp.games.onedroidcode.editor;

import com.timewarp.games.onedroidcode.vsl.Node;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NodeController<T extends Node> {

    public Class<T> nodeType;

    public ArrayList<NodeIO> inputs;
    public ArrayList<NodeIO> outputs;


    public NodeController(Class<T> nodeType) {
        this.nodeType = nodeType;

        this.inputs = new ArrayList<NodeIO>();
        this.outputs = new ArrayList<NodeIO>();

        this.extractMethods();
        this.identifyGroup();
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

            // TODO: Crash application with human-readable error
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
}
