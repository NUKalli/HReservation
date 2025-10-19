package org.example.core;

public class DigitalKey {

    private int id;
    private boolean status;

    public void activate() {
        this.status = true;
    }
    public void deactivate() {
        this.status = false;
    }
    public boolean isValid() {
        return this.status;
    }
}
