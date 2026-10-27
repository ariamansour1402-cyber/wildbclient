package com.customclient.module;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

/** Shows worn armor (helmet to boots) with remaining durability. */
public class ArmorStatus extends Module {

    public ArmorStatus() {
        super("Armor Status", true, true, 4, 80);
    }

    @Override
    public int getWidth() {
        return 58;
    }

    @Override
    public int getHeight() {
        return 64;
    }

    @Override
    public void render() {
        if (mc.thePlayer == null) return;

        // armorInventory[3] = helmet ... [0] = boots
        for (int row = 0; row < 4; row++) {
            ItemStack stack = mc.thePlayer.inventory.armorInventory[3 - row];
            if (stack == null) continue;

            int px = x;
            int py = y + row * 16;

            GlStateManager.enableRescaleNormal();
            RenderHelper.enableGUIStandardItemLighting();
            mc.getRenderItem().renderItemAndEffectIntoGUI(stack, px, py);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableLighting();

            if (stack.getMaxDamage() > 0) {
                int left = stack.getMaxDamage() - stack.getItemDamage();
                mc.fontRendererObj.drawStringWithShadow(String.valueOf(left), px + 20, py + 4, 0xFFFFFF);
            }
        }
    }
}
