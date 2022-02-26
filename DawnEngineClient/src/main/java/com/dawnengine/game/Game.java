package com.dawnengine.game;

import com.dawnengine.game.graphics.StringRenderPosition;
import com.dawnengine.game.graphics.Camera;
import com.dawnengine.editor.AdministratorFrame;
import com.dawnengine.entity.Entity;
import com.dawnengine.entity.LocalPlayer;
import com.dawnengine.game.map.Map;
import com.dawnengine.serializers.objects.MapData;
import com.dawnengine.serializers.MapSerializer;
import com.dawnengine.math.Vector2;
import com.dawnengine.network.Client;
import com.dawnengine.network.NetworkPackets;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import org.json.JSONObject;

public class Game extends Canvas implements GameEvents {

    private static Game instance;

    private final GameLoop loop;
    private final Camera mainCamera;
    private final Input input;
    private Map map;

    private final HashMap<Integer, Entity> entities = new HashMap<>();

    private LocalPlayer lp;
    private final AdministratorFrame adminFrame;

    private Game() {
        loop = new GameLoop(this);
        mainCamera = new Camera(this);
        adminFrame = new AdministratorFrame(this);

        input = Input.getInstance(mainCamera);
        this.addKeyListener(input);
        this.addMouseMotionListener(input);
        this.addMouseListener(input);
        this.addFocusListener(input);
    }

    public void start(JSONObject loginConfig) {
        var mapIndex = loginConfig.getInt("mapIndex");

        lp = LocalPlayer.create(loginConfig.getInt("playerID"),
                new Vector2(loginConfig.getFloat("posX"), loginConfig.getFloat("posY")));

        this.createBufferStrategy(3);
        loop.start();
        
        var mapData = MapSerializer.load(mapIndex);

        var req = new JSONObject()
                .put("mapIndex", mapIndex);
        
        if (mapData != null) {
            req.put("lastRevision", mapData.getLastRevision());
        }
        Client.getClient().sendPacket(NetworkPackets.CLIENT_GET_MAP_REQUEST, req);

        requestFocus();
    }

    public void stop() {
        adminFrame.dispose();
        loop.stop();
    }

    @Override
    public void onStart() {
        for (Entity entity : entities.values()) {
            entity.start();
        }
    }

    @Override
    public void onUpdate(double dt) {
        for (Entity entity : entities.values()) {
            entity.update(dt);
        }

        if (Input.getKeyDown(KeyEvent.VK_INSERT)) {
            adminFrame.setVisible(!adminFrame.isVisible());
        }

        adminFrame.updateEditor();

        Input.update();
    }

    @Override
    public void onNetworkUpdate() {
        for (Entity entity : entities.values()) {
            entity.networkUpdate();
        }
    }

    @Override
    public void onRender() {
        mainCamera.begin();

        if (map != null) {
            mainCamera.drawImage(map.getBackgroundTilemap(), Vector2.zero());
        }

        for (Entity entity : entities.values()) {
            mainCamera.drawEntity(entity);
        }

        if (map != null) {
            mainCamera.drawImage(map.getForegroundTilemap(), Vector2.zero());
            mainCamera.setColor(Color.WHITE);
            mainCamera.setStringRenderPosition(StringRenderPosition.Center_Fixed);
            mainCamera.drawString(map.getName(), new Vector2(mainCamera.center().x, 10));
        }

        adminFrame.renderEditor(mainCamera);

        mainCamera.end();
    }

    public void onGetMapEvent(JSONObject res) {
        var index = res.getInt("mapIndex");
        if (res.optBoolean("update")) {
            var mapData = new MapData(res.getString("name"), res.getInt("sizeX"),
                    res.getInt("sizeY"), res.getInt("moral"), res.getInt("lUp"),
                    res.getInt("lDown"), res.getInt("lRight"), res.getInt("lLeft"),
                    res.getLong("lastRevision"), res.getString("tiles"));
            this.map = new Map(index, mapData);
            MapSerializer.save(index, mapData);
        } else {
            this.map = new Map(index, MapSerializer.load(index));
        }
        
        var posX = res.optFloat("newPosX");
        var posY = res.optFloat("newPosY");
        if (!Float.isNaN(posX) && !Float.isNaN(posY)) {
            lp.transform().position(posX, posY);
        }

        var arr = res.optJSONArray("entities");
        if (arr != null) {
            entities.clear();
            addEntity(lp);
            for (int i = 0; i < arr.length(); i++) {
                var obj = arr.getJSONObject(i);
                var id = obj.getInt("id");
                var position = new Vector2(obj.getFloat("posX"), obj.getFloat("posY"));
                addEntity(new Entity(id, position));
            }
        }
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Camera getMainCamera() {
        return mainCamera;
    }

    public boolean addEntity(Entity e) {
        if (e == null || entities.containsKey(e.id())) {
            return false;
        }
        entities.put(e.id(), e);
        e.start();
        return true;
    }

    public boolean removeEntity(int id) {
        var e = entities.remove(id);
        var notNull = e != null;
        if (notNull) {
            e.onDestroy();
        }
        return notNull;
    }

    public Entity findEntityByID(int id) {
        return entities.get(id);
    }

    public static Game get() {
        if (Game.instance == null) {
            Game.instance = new Game();
        }
        return Game.instance;
    }

}
