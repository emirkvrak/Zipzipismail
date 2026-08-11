package world;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import core.GameConfig;
import resource.ResourceLoader;

public final class MapLoader {

    private MapLoader() {
    }

    public static GameMap load(String resourcePath) {
        try (InputStream input = ResourceLoader.stream(resourcePath);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(input, StandardCharsets.UTF_8))) {
            int width = readRequiredInt(reader, "harita genişliği");
            int height = readRequiredInt(reader, "harita yüksekliği");
            List<Tile> tiles = new ArrayList<>();

            for (int row = 0; row < height; row++) {
                int[] values = parseRow(reader.readLine(), width);
                for (int column = 0; column < width; column++) {
                    if (values[column] != 0) {
                        tiles.add(new Tile(column * GameConfig.TILE_SIZE,
                                row * GameConfig.TILE_SIZE,
                                TileType.fromMapId(values[column])));
                    }
                }
            }

            String line = readNextNonEmptyLine(reader);
            if (line == null) {
                throw new IOException("Hareketli platform sayısı eksik.");
            }

            int platformCount = parseCount(line, "hareketli platform");
            List<MovingPlatform> movingPlatforms = new ArrayList<>();
            for (int i = 0; i < platformCount; i++) {
                int[] values = parseRow(reader.readLine(), 5);
                movingPlatforms.add(new MovingPlatform(
                        values[0] * GameConfig.TILE_SIZE,
                        values[1] * GameConfig.TILE_SIZE,
                        TileType.fromMapId(values[2]),
                        values[3] * GameConfig.TILE_SIZE,
                        values[4] * GameConfig.TILE_SIZE));
            }

            List<Checkpoint> checkpoints = new ArrayList<>();
            String checkpointCountLine = readNextNonEmptyLine(reader);
            if (checkpointCountLine != null) {
                int checkpointCount = parseCount(checkpointCountLine, "checkpoint");
                for (int i = 0; i < checkpointCount; i++) {
                    int[] values = parseRow(reader.readLine(), 2);
                    checkpoints.add(new Checkpoint(
                            values[0] * GameConfig.TILE_SIZE,
                            values[1] * GameConfig.TILE_SIZE));
                }
            }

            List<Collectible> collectibles = new ArrayList<>();
            String collectibleCountLine = readNextNonEmptyLine(reader);
            if (collectibleCountLine != null) {
                int collectibleCount = parseCount(collectibleCountLine, "toplanabilir");
                for (int i = 0; i < collectibleCount; i++) {
                    int[] values = parseRow(reader.readLine(), 2);
                    collectibles.add(new Collectible(
                            values[0] * GameConfig.TILE_SIZE,
                            values[1] * GameConfig.TILE_SIZE));
                }
            }
            return new GameMap(width, height, tiles, movingPlatforms, checkpoints, collectibles);
        } catch (IOException | IllegalArgumentException e) {
            throw new IllegalStateException("Harita yüklenemedi: " + resourcePath, e);
        }
    }

    private static int readRequiredInt(BufferedReader reader, String field) throws IOException {
        String line = reader.readLine();
        if (line == null || line.isBlank()) {
            throw new IOException("Eksik harita alanı: " + field);
        }
        int value = Integer.parseInt(line.trim());
        if (value <= 0) {
            throw new IOException(field + " sıfırdan büyük olmalı.");
        }
        return value;
    }

    private static int parseCount(String line, String field) {
        int value = Integer.parseInt(line.trim());
        if (value < 0) {
            throw new IllegalArgumentException(field + " sayısı negatif olamaz.");
        }
        return value;
    }

    private static String readNextNonEmptyLine(BufferedReader reader) throws IOException {
        String line;
        do {
            line = reader.readLine();
        } while (line != null && line.isBlank());
        return line == null ? null : line.trim();
    }

    private static int[] parseRow(String row, int expectedWidth) {
        if (row == null || row.isBlank()) {
            throw new IllegalArgumentException("Harita satırı boş.");
        }
        String[] tokens = row.trim().split("\\s+");
        if (tokens.length != expectedWidth) {
            throw new IllegalArgumentException("Harita satırında " + expectedWidth
                    + " değer bekleniyor, " + tokens.length + " bulundu.");
        }
        int[] values = new int[expectedWidth];
        for (int i = 0; i < expectedWidth; i++) {
            values[i] = Integer.parseInt(tokens[i]);
        }
        return values;
    }
}
