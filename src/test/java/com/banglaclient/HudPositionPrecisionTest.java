package com.banglaclient;

import com.banglaclient.hud.HudPosition;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Verifies the critical requirement from Section 17:
 * HUD elements must preserve continuous floating-point coordinates (e.g., X=347.35, Y=128.72)
 * without snapping to corners, edges, or a 9-slot grid.
 */
public class HudPositionPrecisionTest {

    @Test
    void verifyContinuousNonSnappingCoordinates() {
        HudPosition pos = new HudPosition(347.35f, 128.72f);

        assertEquals(347.35f, pos.getRenderX(1920), 0.0001f);
        assertEquals(128.72f, pos.getRenderY(1080), 0.0001f);

        // Translate by fractional mouse delta
        pos.translate(12.40f, -5.22f, 1920, 1080);
        assertEquals(359.75f, pos.getRenderX(1920), 0.0001f);
        assertEquals(123.50f, pos.getRenderY(1080), 0.0001f);

        // Verify JSON serialization round-trip preserves exact floating-point coordinates
        JsonObject json = pos.toJson();
        HudPosition restored = new HudPosition(0.0f, 0.0f);
        restored.fromJson(json);

        assertEquals(359.75f, restored.getExactX(), 0.0001f);
        assertEquals(123.50f, restored.getExactY(), 0.0001f);
    }
}
