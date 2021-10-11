package org.rostykamga.imageexifprocessor.model;

/**
 * Model data used to display image property
 */
public class ImageProperty {

    private String name;
    private Object value;

    public ImageProperty(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
