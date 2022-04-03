package com.dawnengine.game;

import com.dawnengine.game.graphics.StringRenderPosition;
import com.dawnengine.game.graphics.Camera;
import com.dawnengine.editor.AdministratorFrame;
import com.dawnengine.entity.Entity;
import com.dawnengine.entity.NetworkEntity;
import com.dawnengine.entity.Player;
import com.dawnengine.game.map.Map;
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

    private static final Game instance = new Game();

    private final GameLoop loop;
    private final Camera mainCamera;
    private final CameraManager cameraManager;
    private final Input input;
    private Map map;

    private final HashMap<Integer, Entity> entities = new HashMap<>();
    private final HashMap<Integer, NetworkEntity> networkEntities = new HashMap<>(1);

    private Player player;

    private final AdministratorFrame adminFrame;

    public Game() {
        loop = new GameLoop(this);
        mainCamera = new Camera(this);
        cameraManager = new CameraManager(mainCamera);
        adminFrame = new AdministratorFrame(this);

        input = Input.createInstance(mainCamera);
        this.addKeyListener(input);
        this.addMouseMotionListener(input);
        this.addMouseListener(input);
        this.addFocusListener(input);
    }

    public void start(JSONObject loginConfig) {
        var mapIndex = loginConfig.getInt("mapIndex");

        player = new Player(loginConfig.getInt("playerID"),
                new Vector2(loginConfig.getFloat("posX"),
                        loginConfig.getFloat("posY")));

        this.createBufferStrategy(3);
        loop.start();

        var req = new JSONObject().put("mapIndex", mapIndex);
        var mapData = MapSerializer.load(mapIndex);
        if (mapData != null) {
            req.put("lastRevision", mapData.getLastRevision());
        }
        Client.get().sendPacket(NetworkPackets.CL_GET_MAP_EV_TR, req);

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
        for (NetworkEntity entity : networkEntities.values()) {
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

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public CameraManager getCamera() {
        return cameraManager;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean addEntity(Entity e) {
        if (e == null || entities.containsKey(e.id())) {
            return false;
        }
        entities.put(e.id(), e);
        e.start();
        if (e instanceof NetworkEntity ne) {
            networkEntities.put(ne.id(), ne);
        }
        return true;
    }

    public boolean removeEntity(int id) {
        var e = entities.remove(id);
        var notNull = e != null;
        if (notNull) {
            e.onDestroy();
        }
        if (e instanceof NetworkEntity) {
            networkEntities.remove(id);
        }
        return notNull;
    }

    public void clearEntities() {
        entities.clear();
        networkEntities.clear();
    }

    public Entity findEntityByID(int id) {
        return entities.get(id);
    }

    public static Game get() {
        return Game.instance;
    }
}
