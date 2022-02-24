package com.dawnengine.entity;

import com.dawnengine.game.Game;
import com.dawnengine.game.Input;
import com.dawnengine.game.map.Map;
import com.dawnengine.game.map.TileAttribute;
import com.dawnengine.math.Vector2;
import com.dawnengine.network.EntityPacket;
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
            int x = 0;
            int y = 0;
            if (Input.getKey(KeyEvent.VK_W)) {
                y = -32;
            } else if (Input.getKey(KeyEvent.VK_S)) {
                y = 32;
            } else if (Input.getKey(KeyEvent.VK_A)) {
                x = -32;
            } else if (Input.getKey(KeyEvent.VK_D)) {
                x = 32;
            }
            if (x != 0 || y != 0) {
                setGoal(x, y);
            }
        }
        Game.get().getMainCamera().follow(transform().position());
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
        
        if (Input.getKey(KeyEvent.VK_SHIFT)) {
            setSpeed(5f);
        } else {
            setSpeed(1.5f);
        }

        moveTo(desirableGoal);

        var req = new JSONObject();
        req.put("posX", desirableGoal.x);
        req.put("posY", desirableGoal.y);
        req.put("speed", getSpeed());
        invalidate(new EntityPacket(NetworkPackets.CLIENT_PLAYER_MOVE, req));
    }

}
