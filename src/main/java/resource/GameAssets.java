package resource;

import java.awt.Image;
import java.awt.image.BufferedImage;

public final class GameAssets {

    private final BufferedImage solidTile;
    private final BufferedImage hazardTile;
    private final BufferedImage goalTile;
    private final Image player;
    private final Image menuBackground;
    private final Image levelOneBackground;
    private final Image levelTwoBackground;
    private final Image levelThreeBackground;

    public GameAssets() {
        solidTile = ResourceLoader.bufferedImage("/Blocks/zigblockbir.jpeg");
        hazardTile = ResourceLoader.bufferedImage("/Blocks/zigtestere.png");
        goalTile = ResourceLoader.bufferedImage("/Blocks/zigpota.png");
        player = ResourceLoader.image("/Resim/zigtop.png");
        menuBackground = ResourceLoader.image("/Resim/zigresim.png");
        levelOneBackground = ResourceLoader.image("/Resim/colbir.jpeg");
        levelTwoBackground = ResourceLoader.image("/Resim/coliki.jpeg");
        levelThreeBackground = ResourceLoader.image("/Resim/colAnaiki.jpeg");
    }

    public BufferedImage solidTile() {
        return solidTile;
    }

    public BufferedImage hazardTile() {
        return hazardTile;
    }

    public BufferedImage goalTile() {
        return goalTile;
    }

    public Image player() {
        return player;
    }

    public Image menuBackground() {
        return menuBackground;
    }

    public Image levelBackground(int levelNumber) {
        return switch (levelNumber) {
            case 1 -> levelOneBackground;
            case 2 -> levelTwoBackground;
            case 3 -> levelThreeBackground;
            default -> throw new IllegalArgumentException("Geçersiz bölüm: " + levelNumber);
        };
    }
}
