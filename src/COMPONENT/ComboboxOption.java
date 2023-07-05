package COMPONENT;

public class ComboboxOption {
    private String value;
    private String display;

    public ComboboxOption(String value, String display) {
        this.value = value;
        this.display = display;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return display;
    }

    public String getDisplay() {
        return display;
    }

    public String getHelper() {
        return getHelper();
    }
}
