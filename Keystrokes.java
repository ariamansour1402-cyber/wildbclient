package com.customclient.module;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.KeyBinding;

/** WASD + mouse button display. Boxes light up while the key is held. */
public class Keystrokes extends Module {
    private static final int BOX = 20;
    private static final int GAP = 2;

    public Keystrokes() {
        super("Keystrokes", true, true, 4, 150);
    }

    @Override
    public int getWidth() {
        return BOX * 3 + GAP * 2;
    }

    @Override
    public int getHeight() {
        return BOX * 3 + GAP * 2;
    }

    @Override
    public void render() {
        int col = BOX + GAP;

        // Row 1: W
        key(mc.gameSettings.keyBindForward, "W", x + col, y, BOX, BOX, null);
        // Row 2: A S D
        key(mc.gameSettings.keyBindLeft, "A", x, y + col, BOX, BOX, null);
        key(mc.gameSettings.keyBindBack, "S", x + col, y + col, BOX, BOX, null);
        key(mc.gameSettings.keyBindRight, "D", x + col * 2, y + col, BOX, BOX, null);
        // Row 3: LMB RMB (with CPS)
        int half = (getWidth() - GAP) / 2;
        key(mc.gameSettings.keyBindAttack, "LMB", x, y + col * 2, half, BOX,
                String.valueOf(InfoModules.Cps.left()));
        key(mc.gameSettings.keyBindUseItem, "RMB", x + half + GAP, y + col * 2, half, BOX,
                String.valueOf(InfoModules.Cps.right()));
    }

    private void key(KeyBinding binding, String label, int bx, int by, int w, int h, String sub) {
        boolean down = binding.isKeyDown();
        Gui.drawRect(bx, by, bx + w, by + h, down ? 0x99FFFFFF : 0x66000000);
        int textColor = down ? 0x000000 : 0xFFFFFF;

        int lw = mc.fontRendererObj.getStringWidth(label);
        if (sub == null) {
            mc.fontRendererObj.drawString(label, bx + (w - lw) / 2, by + (h - 8) / 2, textColor);
        } else {
            mc.fontRendererObj.drawString(label, bx + (w - lw) / 2, by + 2, textColor);
            int sw = mc.fontRendererObj.getStringWidth(sub);
            mc.fontRendererObj.drawString(sub, bx + (w - sw) / 2, by + 11, textColor);
        }
    }
}
