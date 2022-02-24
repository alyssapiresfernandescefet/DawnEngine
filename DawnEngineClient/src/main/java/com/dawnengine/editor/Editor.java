package com.dawnengine.editor;

import com.dawnengine.game.graphics.Camera;
import javax.swing.JFrame;

/**
 *
 * @author alyss
 */
public abstract class Editor extends JFrame {

    public abstract void update();
    public abstract void render(Camera cam);
}
