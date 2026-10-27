package com.customclient.module;

/** Modules without a HUD element: they only change client-side behaviour. */
public final class ToggleModules {
    private ToggleModules() {
    }

    /** Always sprint while holding forward. */
    public static class ToggleSprint extends Module {
        public ToggleSprint() {
            super("Toggle Sprint", false, false, 0, 0);
        }

        /** Call once per client tick. */
        public void tick() {
            if (!enabled || mc.thePlayer == null || mc.currentScreen != null) return;
            if (mc.gameSettings.keyBindForward.isKeyDown()
                    && !mc.thePlayer.isSneaking()
                    && !mc.thePlayer.isUsingItem()
                    && !mc.thePlayer.isCollidedHorizontally
                    && mc.thePlayer.getFoodStats().getFoodLevel() > 6) {
                mc.thePlayer.setSprinting(true);
            }
        }
    }

    /** Hold the zoom key for a smooth FOV zoom (applied only while rendering a frame). */
    public static class Zoom extends Module {
        private static final float ZOOM_FACTOR = 4.0f;
        private float factor = 1.0f;
        private float savedFov;
        private boolean applied;

        public Zoom() {
            super("Zoom", false, true, 0, 0);
        }

        /** Call at the start of a frame. */
        public void apply(boolean zooming) {
            float target = (enabled && zooming) ? ZOOM_FACTOR : 1.0f;
            factor += (target - factor) * 0.25f;
            if (Math.abs(factor - target) < 0.01f) factor = target;

            if (factor != 1.0f) {
                savedFov = mc.gameSettings.fovSetting;
                mc.gameSettings.fovSetting = savedFov / factor;
                applied = true;
            }
        }

        /** Call at the end of a frame so the real FOV setting is never changed permanently. */
        public void restore() {
            if (applied) {
                mc.gameSettings.fovSetting = savedFov;
                applied = false;
            }
        }
    }

    /** Disables the screen shake when you take damage (visual only). */
    public static class NoHurtCam extends Module {
        public NoHurtCam() {
            super("No Hurt Cam", false, true, 0, 0);
        }

        /** Call at the start of a frame, before the camera is set up. */
        public void apply() {
            if (enabled && mc.thePlayer != null) {
                mc.thePlayer.hurtTime = 0;
            }
        }
    }
}
