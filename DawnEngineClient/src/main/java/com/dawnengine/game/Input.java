package com.dawnengine.game;

import com.dawnengine.game.graphics.Camera;
import com.dawnengine.math.Vector2;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

/**
 *
 * @author alyss
 */
public class Input implements KeyListener, MouseListener, FocusListener,
        MouseMotionListener {

    private static Camera mainCamera = null;
    private static Vector2 camPos = Vector2.zero();

    private static final ArrayList<Integer> pressedKeys = new ArrayList<>();
    private static final ArrayList<Integer> releasedKeys = new ArrayList<>();
    private static final ArrayList<Integer> heldKeys = new ArrayList<>();

    private static final ArrayList<Integer> pressedButtons = new ArrayList<>();
    private static final ArrayList<Integer> releasedButtons = new ArrayList<>();
    private static final ArrayList<Integer> heldButtons = new ArrayList<>();
    private static int mouseX, mouseY;
    private static boolean isDragging;

    private Input() {
    }

    public static Input getInstance(Camera gameCamera) {
        if (Input.mainCamera != null) {
            throw new ExceptionInInitializerError("There is already an active input.");
        }
        Input.mainCamera = gameCamera;
        return new Input();
    }

    public static void update() {
        pressedKeys.clear();
        releasedKeys.clear();
        pressedButtons.clear();
        releasedButtons.clear();
        isDragging = false;
        camPos = mainCamera.position();
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

    public static boolean getMouseButton(int button) {
        return heldButtons.contains(button);
    }

    public static boolean getMouseButtonDown(int button) {
        return pressedButtons.contains(button);
    }

    public static boolean getMouseButtonUp(int button) {
        return releasedButtons.contains(button);
    }

    public static boolean isMouseDragging() {
        return isDragging;
    }

    public static Vector2 getMousePosition() {
        return new Vector2(mouseX, mouseY);
    }

    @Override
    public void focusGained(FocusEvent e) {
        
    }

    @Override
    public void focusLost(FocusEvent e) {
        pressedKeys.clear();
        releasedKeys.clear();
        heldKeys.clear();
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
    public void mousePressed(MouseEvent e) {
        int code = e.getButton();
        if (!heldButtons.contains(code)) {
            heldButtons.add(code);
            pressedButtons.add(code);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Integer code = Integer.valueOf(e.getButton());
        pressedButtons.remove(code);
        heldButtons.remove(code);
        releasedButtons.add(code);
        isDragging = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        isDragging = true;
        mouseX = (int) (e.getX() - camPos.x);
        mouseY = (int) (e.getY() - camPos.y);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = (int) (e.getX() - camPos.x);
        mouseY = (int) (e.getY() - camPos.y);
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

    @Override
    public void mouseClicked(MouseEvent e) {

    }

}
