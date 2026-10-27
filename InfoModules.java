package com.customclient.module;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

import java.util.ArrayDeque;
import java.util.Deque;

/** Simple text-based info modules: FPS, CPS, Coordinates. */
public final class InfoModules {
    private InfoModules() {
    }

    public static class Fps extends TextModule {
        public Fps() {
            super("FPS", 4, 4);
        }

        @Override
        protected String getText() {
            return "FPS: " + Minecraft.getDebugFPS();
        }
    }

    public static class Cps extends TextModule {
        private static final Deque<Long> LEFT = new ArrayDeque<Long>();
        private static final Deque<Long> RIGHT = new ArrayDeque<Long>();

        public Cps() {
            super("CPS", 4, 22);
        }

        /** Call on every mouse press (0 = left, 1 = right). */
        public static void click(int button) {
            long now = System.currentTimeMillis();
            if (button == 0) LEFT.addLast(now);
            else if (button == 1) RIGHT.addLast(now);
        }

        private static int count(Deque<Long> q) {
            long now = System.currentTimeMillis();
            while (!q.isEmpty() && now - q.peekFirst() > 1000L) q.pollFirst();
            return q.size();
        }

        public static int left() {
            return count(LEFT);
        }

        public static int right() {
            return count(RIGHT);
        }

        @Override
        protected String getText() {
            return "CPS: " + left() + " | " + right();
        }
    }

    public static class Coords extends TextModule {
        public Coords() {
            super("Coordinates", 4, 40);
        }

        @Override
        protected String getText() {
            if (mc.thePlayer == null) return "XYZ: -";
            int px = MathHelper.floor_double(mc.thePlayer.posX);
            int py = MathHelper.floor_double(mc.thePlayer.posY);
            int pz = MathHelper.floor_double(mc.thePlayer.posZ);
            String dir = EnumFacing.fromAngle(mc.thePlayer.rotationYaw).getName();
            return "XYZ: " + px + " " + py + " " + pz + " (" + dir + ")";
        }
    }
}
