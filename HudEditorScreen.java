package com.customclient.gui;

import com.customclient.module.Module;
import com.customclient.module.ModuleManager;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.List;

/** Drag HUD elements around. Press Esc to save and leave. */
public class HudEditorScreen extends GuiScreen {
    private Module dragging;
    private int offsetX, offsetY;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        for (Module m : ModuleManager.ALL) {
            if (!m.hud || !m.enabled) continue;
            m.render();
            int color = (m == dragging) ? 0x55FFFFFF : 0x33FFFFFF;
            Gui.drawRect(m.x, m.y, m.x + m.getWidth(), m.y + m.getHeight(), color);
        }

        drawCenteredString(fontRendererObj, "Drag the elements. Press Esc to save.",
                width / 2, 8, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton != 0) return;

        List<Module> all = ModuleManager.ALL;
        for (int i = all.size() - 1; i >= 0; i--) {
            Module m = all.get(i);
            if (!m.hud || !m.enabled) continue;
            if (mouseX >= m.x && mouseX <= m.x + m.getWidth()
                    && mouseY >= m.y && mouseY <= m.y + m.getHeight()) {
                dragging = m;
                offsetX = mouseX - m.x;
                offsetY = mouseY - m.y;
                return;
            }
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int lastButton, long timeSinceLastClick) {
        if (dragging == null) return;
        int maxX = Math.max(0, width - dragging.getWidth());
        int maxY = Math.max(0, height - dragging.getHeight());
        dragging.x = Math.max(0, Math.min(maxX, mouseX - offsetX));
        dragging.y = Math.max(0, Math.min(maxY, mouseY - offsetY));
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        dragging = null;
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
