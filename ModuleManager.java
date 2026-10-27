package com.customclient.module;

import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/** Holds every module and persists their on/off state and HUD positions. */
public final class ModuleManager {
    public static final List<Module> ALL = new ArrayList<Module>();

    public static final InfoModules.Fps FPS = add(new InfoModules.Fps());
    public static final InfoModules.Cps CPS = add(new InfoModules.Cps());
    public static final InfoModules.Coords COORDS = add(new InfoModules.Coords());
    public static final ArmorStatus ARMOR = add(new ArmorStatus());
    public static final Keystrokes KEYSTROKES = add(new Keystrokes());
    public static final ToggleModules.ToggleSprint TOGGLE_SPRINT = add(new ToggleModules.ToggleSprint());
    public static final ToggleModules.Zoom ZOOM = add(new ToggleModules.Zoom());
    public static final ToggleModules.NoHurtCam NO_HURT_CAM = add(new ToggleModules.NoHurtCam());

    private ModuleManager() {
    }

    private static <T extends Module> T add(T module) {
        ALL.add(module);
        return module;
    }

    private static File configFile() {
        return new File(Minecraft.getMinecraft().mcDataDir, "config/customclient.properties");
    }

    private static String key(Module m, String field) {
        return m.name.replace(' ', '_') + "." + field;
    }

    public static void load() {
        Properties p = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream(configFile());
            p.load(in);
        } catch (IOException e) {
            return; // first launch: keep defaults
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
        }

        for (Module m : ALL) {
            m.enabled = Boolean.parseBoolean(p.getProperty(key(m, "enabled"), String.valueOf(m.enabled)));
            try {
                m.x = Integer.parseInt(p.getProperty(key(m, "x"), String.valueOf(m.x)));
                m.y = Integer.parseInt(p.getProperty(key(m, "y"), String.valueOf(m.y)));
            } catch (NumberFormatException ignored) {
            }
        }
    }

    public static void save() {
        Properties p = new Properties();
        for (Module m : ALL) {
            p.setProperty(key(m, "enabled"), String.valueOf(m.enabled));
            p.setProperty(key(m, "x"), String.valueOf(m.x));
            p.setProperty(key(m, "y"), String.valueOf(m.y));
        }

        File file = configFile();
        file.getParentFile().mkdirs();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            p.store(out, "Custom PvP Client settings");
        } catch (IOException ignored) {
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
