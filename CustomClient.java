package com.customclient;

import com.customclient.gui.MenuScreen;
import com.customclient.module.InfoModules;
import com.customclient.module.Module;
import com.customclient.module.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

/**
 * Custom PvP Client for Minecraft 1.8.9 (Forge).
 * Fair-play only: visual and quality-of-life features, no combat advantages.
 */
@Mod(modid = CustomClient.MODID, name = "Custom PvP Client", version = "1.0",
        acceptedMinecraftVersions = "[1.8.9]", acceptableRemoteVersions = "*")
public class CustomClient {
    public static final String MODID = "customclient";

    private final Minecraft mc = Minecraft.getMinecraft();
    private KeyBinding menuKey;
    private KeyBinding zoomKey;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        menuKey = new KeyBinding("Open Menu", Keyboard.KEY_RSHIFT, "Custom PvP Client");
        zoomKey = new KeyBinding("Zoom", Keyboard.KEY_C, "Custom PvP Client");
        ClientRegistry.registerKeyBinding(menuKey);
        ClientRegistry.registerKeyBinding(zoomKey);

        ModuleManager.load();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        if (menuKey.isPressed() && mc.currentScreen == null && mc.thePlayer != null) {
            mc.displayGuiScreen(new MenuScreen());
        }
        ModuleManager.TOGGLE_SPRINT.tick();
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            ModuleManager.NO_HURT_CAM.apply();
            ModuleManager.ZOOM.apply(zoomKey.isKeyDown() && mc.currentScreen == null);
        } else {
            ModuleManager.ZOOM.restore();
        }
    }

    @SubscribeEvent
    public void onOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;
        if (mc.thePlayer == null || mc.gameSettings.showDebugInfo || mc.gameSettings.hideGUI) return;

        for (Module m : ModuleManager.ALL) {
            if (m.hud && m.enabled) m.render();
        }
    }

    @SubscribeEvent
    public void onMouse(MouseEvent event) {
        if (event.buttonstate) {
            InfoModules.Cps.click(event.button);
        }
    }
}
