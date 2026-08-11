package resource;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public final class ResourceLoader {

    private ResourceLoader() {
    }

    public static URL url(String path) {
        URL resource = ResourceLoader.class.getResource(path);
        if (resource == null) {
            throw new IllegalStateException("Resource bulunamadı: " + path);
        }
        return resource;
    }

    public static InputStream stream(String path) {
        try {
            return url(path).openStream();
        } catch (IOException e) {
            throw new IllegalStateException("Resource açılamadı: " + path, e);
        }
    }

    public static BufferedImage bufferedImage(String path) {
        try {
            BufferedImage image = ImageIO.read(url(path));
            if (image == null) {
                throw new IllegalStateException("Görsel okunamadı: " + path);
            }
            return image;
        } catch (IOException e) {
            throw new IllegalStateException("Görsel okunamadı: " + path, e);
        }
    }

    public static Image image(String path) {
        return new ImageIcon(url(path)).getImage();
    }
}
