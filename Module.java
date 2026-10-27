package com.customclient.module;

import net.minecraft.client.Minecraft;

/**
 * Base class for every feature. HUD modules draw on screen and can be dragged
 * in the HUD editor; non-HUD modules (zoom, no hurt cam, ...) are just toggles.
 */
public abstract class Module {
    protected final Minecraft mc = Minecraft.getMinecraft();

    public final String name;
    public final boolean hud;
    public boolean enabled;
    public int x, y;

    protected Module(String name, boolean hud, boolean enabledByDefault, int x, int y) {
        this.name = name;
        this.hud = hud;
        this.enabled = enabledByDefault;
        this.x = x;
        this.y = y;
    }

    public int getWidth() {
        return 0;
    }

    public int getHeight() {
        return 0;
    }

    /** Called every frame for enabled HUD modules. */
    public void render() {
    }
}
