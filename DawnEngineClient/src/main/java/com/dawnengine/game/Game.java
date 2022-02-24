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

    private static final HashMap<Integer, Entity> entities = new HashMap<>();

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
            addEntity(new Entity(id, position));
        }

        var req = new JSONObject().put("mapIndex", config.get("mapIndex"));
        Client.getClient().sendPacket(NetworkPackets.CLIENT_CHECK_MAP_REQUEST,
                req, NetworkPackets.SERVER_CHECK_MAP_RESPONSE, ctx -> {
                    var res = ctx.response();
                    var mapIndex = res.getInt("mapIndex");
                    var serverLastRevision = res.getLong("lastRevision");

                    var mapData = MapSerializer.load(mapIndex);

                    if (mapData != null && mapData.getLastRevision() == serverLastRevision) {
                        this.map = new Map(mapIndex, mapData);
                        return;
                    }

                    var req2 = new JSONObject().put("mapIndex", mapIndex);
                    Client.getClient().sendPacket(NetworkPackets.CLIENT_GET_MAP_REQUEST,
                            req2, NetworkPackets.SERVER_GET_MAP_RESPONSE, ctx2 -> {
                                onGetMapEvent(ctx2.response());
                            });
                });

        lp = LocalPlayer.create(config.getInt("playerID"),
                new Vector2(config.getFloat("posX"), config.getFloat("posY")));
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
            mainCamera.drawString(map.getName(), new Vector2(590, 10));
        }

        adminFrame.renderEditor(mainCamera);

        mainCamera.end();
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

    public Camera getMainCamera() {
        return mainCamera;
    }

    public static boolean addEntity(Entity e) {
        if (e == null || entities.containsKey(e.id())) {
            return false;
        }
        entities.put(e.id(), e);
        e.start();
        return true;
    }

    public static boolean removeEntity(int id) {
        var e = entities.remove(id);
        var notNull = e != null;
        if (notNull) {
            e.onDestroy();
        }
        return notNull;
    }

    public static Entity findEntityByID(int id) {
        return entities.get(id);
    }

    public static Game get() {
        if (Game.instance == null) {
            Game.instance = new Game();
        }
        return Game.instance;
    }

}
