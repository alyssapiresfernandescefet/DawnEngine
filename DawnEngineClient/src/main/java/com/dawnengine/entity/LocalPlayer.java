package com.dawnengine.entity;

import com.dawnengine.core.GameFrame;
import com.dawnengine.game.Game;
import com.dawnengine.game.Input;
import com.dawnengine.game.map.Map;
import com.dawnengine.game.map.MapLink;
import com.dawnengine.game.map.Tile;
import com.dawnengine.game.map.TileAttribute;
import com.dawnengine.math.Mathf;
import com.dawnengine.math.Vector2;
import com.dawnengine.network.NetworkPackets;
import java.awt.event.KeyEvent;
import org.json.JSONObject;

/**
 *
 * @author alyss
 */
public class LocalPlayer extends NetworkEntity {

    private static final float WALKING_SPEED = 1.5f, RUNNING_SPEED = 5f;

    private static LocalPlayer instance;

    public static LocalPlayer create(int id, Vector2 position) {
        if (instance != null) {
            return null;
        }
        instance = new LocalPlayer(id, position);
        return instance;
    }

    private LocalPlayer(int id, Vector2 position) {
        super(id, position);
    }

    @Override
    public void start() {
        Map map = Game.get().getMap();
        move(map.getTilePosition(transform().position()));
    }

    @Override
    public void update(double dt) {
        super.update(dt);
        if (!isMoving()) {
            if (Input.getKey(KeyEvent.VK_W)) {
                setGoal(0, -Tile.SIZE_Y);
            } else if (Input.getKey(KeyEvent.VK_S)) {
                setGoal(0, Tile.SIZE_Y);
            } else if (Input.getKey(KeyEvent.VK_A)) {
                setGoal(-Tile.SIZE_X, 0);
            } else if (Input.getKey(KeyEvent.VK_D)) {
                setGoal(Tile.SIZE_X, 0);
            }
        }
        if (Input.getKeyDown(KeyEvent.VK_C)) {
            transform().position(new Vector2(0, 0));
        }

        cameraFollow();
    }

    private void setGoal(int dx, int dy) {
        Map map = Game.get().getMap();

        Vector2 goal = new Vector2(transform().position());
        goal.x += dx;
        goal.y += dy;
        var actualGoal = map.getTilePosition(goal);

        if (map.getAttribute(actualGoal) == TileAttribute.Block) {
            return;
        }

        var link = map.getLink(goal);
        if (link == null) {
            move(actualGoal);
        } else {
            teleport(link);
        }

    }

    private void move(Vector2 desirableGoal) {
        var speed = WALKING_SPEED;
        if (Input.getKey(KeyEvent.VK_SHIFT)) {
            speed = RUNNING_SPEED;
        }

        var req = new JSONObject()
                .put("posX", desirableGoal.x)
                .put("posY", desirableGoal.y)
                .put("speed", speed);
        invalidate(NetworkPackets.CL_PLAYER_MOVE_EV, req);

        moveTo(desirableGoal, speed);
    }

    private void teleport(MapLink link) {
        var req = new JSONObject()
                .put("mapIndex", link.getMapIndex())
                .put("dir", link.getDirection());
        invalidate(NetworkPackets.CL_PLAYER_MOVE_MAP_REQ, req);
    }

    private void cameraFollow() {
        var map = Game.get().getMap();
        if (map != null) {
            var worldSize = map.getWorldSize();
            var pos = new Vector2(transform().position());
            float gw = GameFrame.GAME_WIDTH * 0.5f;
            float gh = GameFrame.GAME_HEIGHT * 0.5f;
            pos.x = Mathf.clamp(pos.x, gw, worldSize.width - gw);
            pos.y = Mathf.clamp(pos.y, gh, worldSize.height - gh);
            Game.get().getMainCamera().follow(pos);
        }
    }

}
