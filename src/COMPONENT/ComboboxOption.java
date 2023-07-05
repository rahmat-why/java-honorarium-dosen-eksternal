package COMPONENT;

public class ComboboxOption {
    private String value;
    private String display;
    private String helper;

    public ComboboxOption(String value, String display) {
        this.value = value;
        this.display = display;
    }

    public ComboboxOption(String value, String display, String helper) {
        this.value = value;
        this.display = display;
        this.helper = helper;
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
        return helper;
    }
}
