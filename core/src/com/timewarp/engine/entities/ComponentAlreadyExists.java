package com.timewarp.engine.entities;


public class ComponentAlreadyExists extends Exception {
    public ComponentAlreadyExists(String name) {
        super("Component with name '" + name + "' already exists on target GameObject");
    }
}
