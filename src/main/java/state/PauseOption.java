package state;

public enum PauseOption {
    RESUME("DEVAM ET"),
    RESTART("YENİDEN BAŞLAT"),
    CONTROLS("KONTROLLER"),
    MENU("ANA MENÜ");

    private final String label;

    PauseOption(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
