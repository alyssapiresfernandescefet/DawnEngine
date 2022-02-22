package com.dawnengine.game;

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
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;

public class Game extends Canvas implements GameEvents {

    private static Game instance;

    private final GameLoop loop;
    private final Camera mainCamera;
    private final Input input;
    private Map map;

    private static final ArrayList<Entity> entities = new ArrayList<>();
    private static final HashMap<Integer, Entity> entitiesMap = new HashMap<>();

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

    public void start(JSONObject config) {
        var arr = config.getJSONArray("entities");
        for (int i = 0; i < arr.length(); i++) {
            var obj = arr.getJSONObject(i);
            var id = obj.getInt("id");
            var position = new Vector2(obj.getFloat("posX"), obj.getFloat("posY"));
            var scale = new Vector2(obj.getFloat("scaX"), obj.getFloat("scaY"));
            var rotation = obj.getFloat("rot");
            addEntity(new Entity(id, position, scale, rotation));
        }

        var req = new JSONObject().put("mapIndex", config.get("mapIndex"));
        Client.getClient().sendPacket(NetworkPackets.CLIENT_CHECK_MAP_REQUEST,
                req, NetworkPackets.SERVER_CHECK_MAP_RESPONSE, ctx -> {
                    var res = ctx.response();
                    onCheckMapReceived(res.getInt("mapIndex"),
                            res.getLong("lastRevision"));
                });

        lp = new LocalPlayer(config.getInt("playerID"), Vector2.zero());
        addEntity(lp);

        this.createBufferStrategy(3);
        loop.start();
        requestFocus();
    }

    public void stop() {
        adminFrame.dispose();
        loop.stop();
    }

    @Override
    public void onStart() {
        for (Entity entity : entities) {
            entity.start();
        }
    }

    @Override
    public void onUpdate(double dt) {
        for (Entity entity : entities) {
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
        for (Entity entity : entities) {
            entity.networkUpdate();
        }
    }

    @Override
    public void onRender() {
        mainCamera.begin();

        if (map != null) {
            //TODO: Draw only pre-entity tilemap here.
            mainCamera.drawImage(map.getTilemap(), Vector2.zero());
            mainCamera.setColor(Color.WHITE);
            mainCamera.drawString(map.getName(), new Vector2(0, 24));
        }

        for (Entity entity : entities) {
            mainCamera.drawEntity(entity);
        }

        //TODO: Draw post-entity tilemap here.
        adminFrame.renderEditor(mainCamera);

        mainCamera.end();
    }

    public void onCheckMapReceived(int mapIndex, long serverLastRevision) {
        var mapData = MapSerializer.load(mapIndex);
        if (mapData != null && mapData.getLastRevision() == serverLastRevision) {
            this.map = new Map(mapIndex, mapData);
            return;
        }
        var req = new JSONObject().put("mapIndex", mapIndex);
        Client.getClient().sendPacket(NetworkPackets.CLIENT_GET_MAP_REQUEST,
                req, NetworkPackets.SERVER_GET_MAP_RESPONSE, ctx -> {
                    onGetMapEvent(ctx.response());
                });
    }

    public void onGetMapEvent(JSONObject json) {
        var index = json.getInt("mapIndex");
        var mapData = new MapData(json.getString("name"), json.getInt("sizeX"),
                json.getInt("sizeY"), json.getInt("moral"), json.getInt("lUp"),
                json.getInt("lDown"), json.getInt("lRight"), json.getInt("lLeft"),
                json.getLong("lastRevision"), json.getString("tiles"));
        if (this.map == null || index == this.map.getIndex()) {
            this.map = new Map(index, mapData);
        }
        MapSerializer.save(index, mapData);
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public static boolean addEntity(Entity e) {
        boolean added = entities.add(e) && entitiesMap.put(e.id(), e) == null;
        if (added) {
            e.start();
        }
        return added;
    }

    public static boolean removeEntity(int id) {
        var e = entitiesMap.remove(id);
        var notNull = e != null;
        if (notNull) {
            e.onDestroy();
        }
        return notNull && entities.remove(e);
    }

    public static Entity findEntityByID(int id) {
        return entitiesMap.get(id);
    }
    
    public static Game get() {
        if (Game.instance == null) {
            Game.instance = new Game();
        }
        return Game.instance;
    }

}
