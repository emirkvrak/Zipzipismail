package effect;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import entity.Player;

/** Lightweight visual effects used to make movement and landing feel responsive. */
public final class ParticleSystem {

    private static final int PARTICLES_PER_BOUNCE = 8;
    private static final int MAX_PARTICLES = 96;
    private static final double PARTICLE_GRAVITY = 220.0;
    private static final Color PARTICLE_COLOR = new Color(238, 210, 150);

    private final List<Particle> particles = new ArrayList<>();
    private final Random random = new Random();

    public void emitBounce(Player player) {
        double originX = player.x() + player.width() / 2.0;
        double originY = player.y() + player.height();
        for (int index = 0; index < PARTICLES_PER_BOUNCE; index++) {
            if (particles.size() >= MAX_PARTICLES) {
                particles.remove(0);
            }
            double velocityX = random.nextDouble(-75.0, 75.0);
            double velocityY = random.nextDouble(-115.0, -45.0);
            double lifetime = random.nextDouble(0.22, 0.38);
            int size = random.nextInt(3, 7);
            particles.add(new Particle(originX + random.nextDouble(-7.0, 7.0),
                    originY - 2.0, velocityX, velocityY, lifetime, size));
        }
    }

    public void update(double deltaSeconds) {
        Iterator<Particle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            Particle particle = iterator.next();
            particle.update(deltaSeconds);
            if (particle.isExpired()) {
                iterator.remove();
            }
        }
    }

    public void clear() {
        particles.clear();
    }

    public void render(Graphics2D graphics, double cameraX, double cameraY) {
        Graphics2D effect = (Graphics2D) graphics.create();
        try {
            for (Particle particle : particles) {
                int alpha = (int) Math.round(255 * particle.opacity());
                effect.setColor(new Color(PARTICLE_COLOR.getRed(), PARTICLE_COLOR.getGreen(),
                        PARTICLE_COLOR.getBlue(), alpha));
                int drawX = (int) Math.round(particle.x - cameraX);
                int drawY = (int) Math.round(particle.y - cameraY);
                effect.fillOval(drawX, drawY, particle.size, particle.size);
            }
        } finally {
            effect.dispose();
        }
    }

    private static final class Particle {

        private double x;
        private double y;
        private double velocityX;
        private double velocityY;
        private final double lifetime;
        private final int size;
        private double age;

        private Particle(double x, double y, double velocityX, double velocityY,
                double lifetime, int size) {
            this.x = x;
            this.y = y;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
            this.lifetime = lifetime;
            this.size = size;
        }

        private void update(double deltaSeconds) {
            x += velocityX * deltaSeconds;
            y += velocityY * deltaSeconds;
            velocityY += PARTICLE_GRAVITY * deltaSeconds;
            age += deltaSeconds;
        }

        private boolean isExpired() {
            return age >= lifetime;
        }

        private double opacity() {
            return Math.max(0.0, 1.0 - age / lifetime);
        }
    }
}
