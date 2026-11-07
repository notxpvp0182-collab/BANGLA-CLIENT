package com.banglaclient.animation;

/**
 * Mathematical easing functions for smooth, non-childish UI transitions.
 */
public enum Easing {
    LINEAR {
        @Override
        public float apply(float t) {
            return clamp(t);
        }
    },
    EASE_OUT_QUAD {
        @Override
        public float apply(float t) {
            float x = clamp(t);
            return 1.0f - (1.0f - x) * (1.0f - x);
        }
    },
    EASE_OUT_CUBIC {
        @Override
        public float apply(float t) {
            float x = clamp(t) - 1.0f;
            return x * x * x + 1.0f;
        }
    },
    EASE_IN_OUT_SMOOTH {
        @Override
        public float apply(float t) {
            float x = clamp(t);
            return x * x * (3.0f - 2.0f * x);
        }
    },
    EASE_OUT_BACK {
        @Override
        public float apply(float t) {
            float x = clamp(t);
            float c1 = 1.70158f;
            float c3 = c1 + 1.0f;
            float p = x - 1.0f;
            return 1.0f + c3 * p * p * p + c1 * p * p;
        }
    };

    public abstract float apply(float progress);

    protected static float clamp(float v) {
        return Math.max(0.0f, Math.min(1.0f, v));
    }
}
