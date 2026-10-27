package com.customclient.gui;

import com.customclient.module.Module;
import com.customclient.module.ModuleManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

/** Main settings menu (opens with Right Shift by default). */
public class MenuScreen extends GuiScreen {
    private static final int BTN_W = 150;
    private static final int BTN_H = 20;
    private static final int GAP = 4;
    private static final int ID_EDIT_HUD = 100;
    private static final int ID_DONE = 101;

    private int top;

    private static String label(Module m) {
        return m.name + ": " + (m.enabled ? "\u00a7aON" : "\u00a7cOFF");
    }

    @Override
    public void initGui() {
        buttonList.clear();
        top = height / 6;
        int left = width / 2 - BTN_W - GAP / 2;

        int count = ModuleManager.ALL.size();
        for (int i = 0; i < count; i++) {
            Module m = ModuleManager.ALL.get(i);
            int col = i % 2;
            int row = i / 2;
            buttonList.add(new GuiButton(i, left + col * (BTN_W + GAP), top + row * (BTN_H + GAP),
                    BTN_W, BTN_H, label(m)));
        }

        int rows = (count + 1) / 2;
        int by = top + rows * (BTN_H + GAP) + 10;
        buttonList.add(new GuiButton(ID_EDIT_HUD, left, by, BTN_W, BTN_H, "Edit HUD Layout"));
        buttonList.add(new GuiButton(ID_DONE, left + BTN_W + GAP, by, BTN_W, BTN_H, "Done"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == ID_EDIT_HUD) {
            mc.displayGuiScreen(new HudEditorScreen());
        } else if (button.id == ID_DONE) {
            mc.displayGuiScreen(null);
        } else if (button.id >= 0 && button.id < ModuleManager.ALL.size()) {
            Module m = ModuleManager.ALL.get(button.id);
            m.enabled = !m.enabled;
            button.displayString = label(m);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, "Custom PvP Client", width / 2, top - 20, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed() {
        ModuleManager.save();
    }
}
