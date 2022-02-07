package com.dawnengine.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 *
 * @author alyss
 */
public class Input implements KeyListener, MouseListener {

    private static final ArrayList<Integer> pressedKeys = new ArrayList<>();
    private static final ArrayList<Integer> releasedKeys = new ArrayList<>();
    private static final ArrayList<Integer> heldKeys = new ArrayList<>();

    public static void update() {
        pressedKeys.clear();
        releasedKeys.clear();
    }

    public static boolean getKey(int keyCode) {
        return heldKeys.contains(keyCode);
    }

    public static boolean getKeyDown(int keyCode) {
        return pressedKeys.contains(keyCode);
    }
    
    public static boolean getKeyUp(int keyCode) {
        return releasedKeys.contains(keyCode);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (!heldKeys.contains(keyCode)) {
            heldKeys.add(keyCode);
            pressedKeys.add(keyCode);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Integer val = Integer.valueOf(e.getKeyCode());
        pressedKeys.remove(val);
        heldKeys.remove(val);
        releasedKeys.add(val);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    //**************************
    //*                        *
    //*      UNSUPPORTED       *
    //*                        *
    //**************************
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
