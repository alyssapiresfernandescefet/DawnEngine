package com.dawnengine.game.map;

import com.dawnengine.math.Vector2;
import com.dawnengine.serializers.TilesetLoader;
import com.dawnengine.serializers.objects.MapData;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author alyss
 */
public class Map {

    public static final int BACKGROUND_LAYERS = 3, FOREGROUND_LAYERS = 2;

    public static final int IGNORED_COLOR = 0xFFFF00FF;

    private final int index;
    private String name;
    private long lastRevision;
    private int tileCountX, tileCountY;
    private MapMoral moral;
    private MapLink[] links;
    private Tile[][] tiles;
    private TileAttribute[] attributes;

    private BufferedImage backgroundTilemap, foregroundTilemap;

    public Map(int index, MapData map) {
        this.index = index;
        this.name = map.getName();
        this.lastRevision = map.getLastRevision();
        this.tileCountX = map.getTileCountX();
        this.tileCountY = map.getTileCountY();
        this.moral = MapMoral.values()[map.getMoral()];
        this.links = new MapLink[]{
            new MapLink(map.getLinkUp(), MapLink.UP),
            new MapLink(map.getLinkDown(), MapLink.DOWN),
            new MapLink(map.getLinkRight(), MapLink.RIGHT),
            new MapLink(map.getLinkLeft(), MapLink.LEFT),};

        var size = this.tileCountX * this.tileCountY;
        this.tiles = new Tile[Tile.LAYERS_NUM][size];
        this.attributes = new TileAttribute[size];

        if (map.getTiles().isEmpty()) {
            var width = tileCountX * Tile.SIZE_X;
            var height = tileCountY * Tile.SIZE_Y;
            backgroundTilemap = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            foregroundTilemap = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            return;
        }

        String[] serializedTiles = map.getTiles().split("_");
        var tilesetsMap = new HashMap<Integer, Tileset>(1);

        for (int i = 0; i < serializedTiles.length; i++) {
            var split = serializedTiles[i].split("x");

            var layerIndex = Integer.parseInt(split[0]);
            var mapIndex = Integer.parseInt(split[1]);

            var tilesetNum = Integer.parseInt(split[2]);
            var tileIndex = Integer.parseInt(split[3]);

            if (tilesetNum < 1 || tileIndex < 0) {
                continue;
            }

            if (split.length > 4) {
                var attrIndex = Integer.parseInt(split[4]);
                this.attributes[mapIndex] = TileAttribute.values()[attrIndex];
            }

            Tileset tileset = tilesetsMap.get(tilesetNum);
            if (tileset == null) {
                tileset = TilesetLoader.load(tilesetNum);
                tilesetsMap.put(tilesetNum, tileset);
            }

            this.tiles[layerIndex][mapIndex] = new Tile(tileset, tileIndex);
        }

        bakeTilemap();
    }

    public Map(Map other) {
        this.index = other.index;
        this.name = other.name;
        this.lastRevision = other.lastRevision;
        this.tileCountX = other.tileCountX;
        this.tileCountY = other.tileCountY;
        this.moral = other.moral;
        this.links = other.links;
        this.tiles = new Tile[other.tiles.length][this.tileCountX * this.tileCountY];
        this.attributes = Arrays.copyOf(other.attributes, other.attributes.length);
        for (int x = 0; x < this.tiles.length; x++) {
            for (int y = 0; y < this.tiles[x].length; y++) {
                var tile = other.tiles[x][y];
                if (tile != null) {
                    this.tiles[x][y] = new Tile(tile);
                }
            }
        }
        bakeTilemap();
    }

    private void bakeTilemap() {
        var width = tileCountX * Tile.SIZE_X;
        var height = tileCountY * Tile.SIZE_Y;

        backgroundTilemap = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        foregroundTilemap = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < tileCountX * tileCountY; i++) {
            bakeTilemap(i);
        }
    }

    /**
     * Bakes a section of the tilemap.
     *
     * @param index the index of the tile to be baked into the tilemap.
     */
    private void bakeTilemap(int index) {
        var posX = index % tileCountX * Tile.SIZE_X;
        var posY = index / tileCountX * Tile.SIZE_Y;
        var data = new int[Tile.SIZE_X * Tile.SIZE_Y];

        backgroundTilemap.setRGB(posX, posY, Tile.SIZE_X, Tile.SIZE_Y,
                data, 0, Tile.SIZE_X);
        foregroundTilemap.setRGB(posX, posY, Tile.SIZE_X, Tile.SIZE_Y,
                data, 0, Tile.SIZE_X);

        for (int layerNum = 0; layerNum < Tile.LAYERS_NUM; layerNum++) {
            var tile = tiles[layerNum][index];

            if (tile == null) {
                continue;
            }

            for (int i = 0; i < Tile.SIZE_X * Tile.SIZE_Y; i++) {
                var x = i % Tile.SIZE_X;
                var y = i / Tile.SIZE_X;
                var rgb = tile.getSprite().getRGB(x, y);
                if (rgb != 0 && rgb != IGNORED_COLOR) {
                    if (layerNum < BACKGROUND_LAYERS) {
                        backgroundTilemap.setRGB(x + posX, y + posY, rgb);
                    } else {
                        foregroundTilemap.setRGB(x + posX, y + posY, rgb);
                    }
                }
            }
        }
    }

    public MapLink getLink(Vector2 worldPosition) {
        int x = (int) worldPosition.x;
        int y = (int) worldPosition.y;

        if (x < 0) {
            return links[MapLink.LEFT];
        } else if (y < 0) {
            return links[MapLink.UP];
        }

        x /= Tile.SIZE_X;
        y /= Tile.SIZE_Y;

        if (x >= tileCountX) {
            return links[MapLink.RIGHT];
        } else if (y >= tileCountY) {
            return links[MapLink.DOWN];
        }

        return null;
    }

    public MapLink getLink(int direction) {
        return this.links[direction];
    }

    public void setLink(int direction, int index) {
        this.links[direction].setMapIndex(index);
    }

    public void setLinks(MapLink[] links) {
        this.links = links;
    }

    public String getSerializedTiles() {
        String str = "";
        for (int layerNum = 0; layerNum < tiles.length; layerNum++) {
            var layer = tiles[layerNum];
            for (int mapPos = 0; mapPos < layer.length; mapPos++) {
                var tile = layer[mapPos];
                if (tile == null) {
                    continue;
                }
                str += layerNum + "x"
                        + mapPos + "x"
                        + tile.getTilesetNum() + "x";
                var attr = attributes[mapPos];
                if (attr == null) {
                    str += tile.getTileIndex() + "_";
                } else {
                    str += tile.getTileIndex() + "x";
                    str += attr.ordinal() + "_";
                }
            }
        }
        var newLength = str.length() - 1;
        if (newLength < 0) {
            return str;
        }
        return str.substring(0, newLength);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLastRevision() {
        return lastRevision;
    }

    public void setLastRevision(long lastRevision) {
        this.lastRevision = lastRevision;
    }

    public Vector2[] getTilesPositions() {
        var size = tileCountX * tileCountY;
        var posisitons = new Vector2[size];
        for (int i = 0; i < size; i++) {
            posisitons[i] = new Vector2(
                    (i % tileCountX) * Tile.SIZE_X + Tile.SIZE_X / 2,
                    (i / tileCountX) * Tile.SIZE_X + Tile.SIZE_Y / 2);
        }
        return posisitons;
    }

    public Vector2 getTilePosition(Vector2 worldPosition) {
        int x = (int) worldPosition.x / Tile.SIZE_X;
        int y = (int) worldPosition.y / Tile.SIZE_Y;

        if (x < 0) {
            x = 0;
        } else if (x >= tileCountX) {
            x = tileCountX - 1;
        }

        if (y < 0) {
            y = 0;
        } else if (y >= tileCountY) {
            y = tileCountY - 1;
        }

        return new Vector2(x * Tile.SIZE_X + Tile.SIZE_X * 0.5f,
                y * Tile.SIZE_Y + Tile.SIZE_Y * 0.5f);
    }

    public void setTile(int layer, int index, Tile tile) {
        tiles[layer][index] = tile;
        bakeTilemap(index);
    }

    public void setTiles(int layer, Tile tile) {
        var layerTiles = tiles[layer];
        for (int i = 0; i < layerTiles.length; i++) {
            layerTiles[i] = tile;
            bakeTilemap(i);
        }
    }

    public TileAttribute getAttribute(int index) {
        var attr = attributes[index];
        if (attr == null) {
            attr = TileAttribute.None;
        }
        return attr;
    }

    public TileAttribute getAttribute(Vector2 worldPosition) {
        int x = (int) worldPosition.x / Tile.SIZE_X;
        int y = (int) worldPosition.y / Tile.SIZE_Y;

        if (x < 0) {
            x = 0;
        } else if (x >= tileCountX) {
            x = tileCountX - 1;
        }

        if (y < 0) {
            y = 0;
        } else if (y >= tileCountY) {
            y = tileCountY - 1;
        }

        var attr = attributes[x + y * tileCountX];
        if (attr == null) {
            attr = TileAttribute.None;
        }
        return attr;
    }

    public void setAttribute(int index, TileAttribute attribute) {
        attributes[index] = attribute;
    }

    public void setAttribute(int x, int y, TileAttribute attribute) {
        attributes[x + y * tileCountX] = attribute;
    }

    public Dimension getWorldSize() {
        return new Dimension(tileCountX * Tile.SIZE_X, tileCountY * Tile.SIZE_Y);
    }

    public void setSize(Dimension size) {
        var newTileCountX = size.width;
        var newTileCountY = size.height;
        var newSize = newTileCountX * newTileCountY;

        var attrAux = this.attributes;
        this.attributes = new TileAttribute[newSize];

        for (int layerNum = 0; layerNum < Tile.LAYERS_NUM; layerNum++) {
            var tilesAux = this.tiles[layerNum];
            this.tiles[layerNum] = new Tile[newSize];
            var layerTiles = this.tiles[layerNum];

            for (int tileNum = 0; tileNum < tileCountX * tileCountY; tileNum++) {
                var x = tileNum % tileCountX;
                var y = tileNum / tileCountX;
                var newPos = x + y * newTileCountX;
                var oldPos = x + y * tileCountX;

                if (newPos < newSize) {
                    layerTiles[newPos] = tilesAux[oldPos];
                    this.attributes[newPos] = attrAux[oldPos];
                }
            }
        }

        tileCountX = newTileCountX;
        tileCountY = newTileCountY;
        bakeTilemap();
    }

    public int getTileCountX() {
        return tileCountX;
    }

    public int getTileCountY() {
        return tileCountY;
    }

    public BufferedImage getBackgroundTilemap() {
        return backgroundTilemap;
    }

    public BufferedImage getForegroundTilemap() {
        return foregroundTilemap;
    }

    public int getIndex() {
        return index;
    }

    public MapMoral getMoral() {
        return moral;
    }

    public void setMoral(MapMoral moral) {
        this.moral = moral;
    }

}
