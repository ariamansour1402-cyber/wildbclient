package com.customclient.module;

import net.minecraft.client.gui.Gui;

/** A HUD module that shows a single line of text on a translucent background. */
public abstract class TextModule extends Module {

    protected TextModule(String name, int x, int y) {
        super(name, true, true, x, y);
    }

    protected abstract String getText();

    @Override
    public int getWidth() {
        return mc.fontRendererObj.getStringWidth(getText()) + 8;
    }

    @Override
    public int getHeight() {
        return 14;
    }

    @Override
    public void render() {
        Gui.drawRect(x, y, x + getWidth(), y + getHeight(), 0x66000000);
        mc.fontRendererObj.drawStringWithShadow(getText(), x + 4, y + 3, 0xFFFFFF);
    }
}
