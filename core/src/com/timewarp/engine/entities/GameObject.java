package com.timewarp.engine.entities;

import com.timewarp.engine.Time;
import com.timewarp.engine.Vector2D;
import com.timewarp.engine.gui.GUIControl;

import java.util.ArrayList;


public abstract class GameObject extends GUIControl {


    public final GameObject gameObject;


    private ArrayList<Component> components;
    private ArrayList<Component> componentsInitList;

    public GameObject () {
        super.init(new Vector2D(), new Vector2D());

        this.components = new ArrayList<Component>(2);
        this.componentsInitList = new ArrayList<Component>(2);

        this.gameObject = this;

        start();
    }

    @Override
    public final void update() {
        super.update();

        for (Component component: this.componentsInitList)
            component.start();


        for (Component component: this.components) {
            if (component.isActive())
                component.update();
        }

        for (Component component: this.components) {
            if (component.isActive())
                component.postUpdate();
        }

        this.tick(Time.getDeltaTime());
    }

    public abstract void start();
    public abstract void tick(double deltaTime);
    public abstract void postTick(double deltaTime);

    /**
     * Returns gameobject's component by specified class
     * Returns NULL if component wasn't found on object
     * @param component_class Target component class
     * @return Object's component
     */
    public final Component getComponent(Class<Component> component_class) {
        final String target_name = component_class.getName();

        for (Component c : this.components) {
            if (c.getClass().getName().equals(target_name))
                return c;
        }

        return null;
    }

    public final void addComponent(Class<Component> component_class) throws ComponentAlreadyExists {
        if (null != gameObject.getComponent(component_class))
            throw new ComponentAlreadyExists(component_class.getSimpleName());

        try {
            Component component = component_class.newInstance();
            component.awake();
            this.components.add(component);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
