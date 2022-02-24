package com.dawnengine.entity;

import com.dawnengine.core.GameFrame;
import com.dawnengine.game.Game;
import com.dawnengine.game.Input;
import com.dawnengine.game.map.Map;
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

    private static LocalPlayer instance;

    public static LocalPlayer get() {
        return instance;
    }

    public static LocalPlayer create(int id, Vector2 position) {
        if (instance == null) {
            instance = new LocalPlayer(id, position);
        }
        return instance;
    }

    private LocalPlayer(int id, Vector2 position) {
        super(id, position);
    }

    @Override
    public void update(double dt) {
        super.update(dt);
        if (!isMoving()) {
            if (Input.getKey(KeyEvent.VK_W)) {
                setGoal(0, -32);
            } else if (Input.getKey(KeyEvent.VK_S)) {
                setGoal(0, 32);
            } else if (Input.getKey(KeyEvent.VK_A)) {
                setGoal(-32, 0);
            } else if (Input.getKey(KeyEvent.VK_D)) {
                setGoal(32, 0);
            }
        }

        var map = Game.get().getMap();
        if (map != null) {
            var worldSize = map.getWorldSize();
            var pos = new Vector2(transform().position());
            var gw = GameFrame.GAME_WIDTH * 0.5f;
            var gh = GameFrame.GAME_HEIGHT * 0.5f;
            pos.x = Mathf.clamp(pos.x, gw, worldSize.width - gw);
            pos.y = Mathf.clamp(pos.y, gh, worldSize.height - gh);
            Game.get().getMainCamera().follow(pos);
        }
    }

    private void setGoal(int dx, int dy) {
        Map map = Game.get().getMap();

        Vector2 pos = new Vector2(transform().position());
        pos.x += dx;
        pos.y += dy;
        var desirableGoal = map.getTilePosition(pos);

        if (map.getAttribute(desirableGoal) == TileAttribute.Block) {
            return;
        }

        var speed = 1.5f;
        if (Input.getKey(KeyEvent.VK_SHIFT)) {
            speed = 5f;
        }

        moveTo(desirableGoal, speed);

        var req = new JSONObject();
        req.put("posX", desirableGoal.x);
        req.put("posY", desirableGoal.y);
        req.put("speed", speed);
        invalidate(NetworkPackets.CLIENT_PLAYER_MOVE, req);
    }

}
