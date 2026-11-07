package com.banglaclient.module;

import com.banglaclient.core.BanglaLogger;
import com.banglaclient.module.impl.advanced.*;
import com.banglaclient.module.impl.camera.*;
import com.banglaclient.module.impl.chat.*;
import com.banglaclient.module.impl.client.*;
import com.banglaclient.module.impl.coremod.*;
import com.banglaclient.module.impl.hardware.*;
import com.banglaclient.module.impl.hud.*;
import com.banglaclient.module.impl.input.*;
import com.banglaclient.module.impl.inventory.*;
import com.banglaclient.module.impl.network.*;
import com.banglaclient.module.impl.optimizer.*;
import com.banglaclient.module.impl.overlay.*;
import com.banglaclient.module.impl.performance.*;
import com.banglaclient.module.impl.pvp.*;
import com.banglaclient.module.impl.quality.*;
import com.banglaclient.module.impl.smallitems.*;
import com.banglaclient.module.impl.visual.*;
import com.banglaclient.module.impl.worldmap.*;
import com.banglaclient.module.impl.worldvisual.*;

/**
 * Registers all 315 independent BANGLACLIENT module classes into the central ModuleRegistry.
 */
public final class AllModulesBootstrap {

    private static boolean registered = false;

    private AllModulesBootstrap() {
    }

    public static synchronized void registerAllModules() {
        if (registered) {
            return;
        }
        registered = true;
        ModuleRegistry reg = ModuleRegistry.getInstance();

        // 01 — OPTIMIZER (001–048)
        reg.register(new InputOptimizer());
        reg.register(new RenderOptimizer());
        reg.register(new EntityCulling());
        reg.register(new CaveCulling());
        reg.register(new BlockRenderingOptimizer());
        reg.register(new ItemRenderingOptimizer());
        reg.register(new MobRenderingOptimizer());
        reg.register(new ParticleOptimizer());
        reg.register(new AnimationOptimizer());
        reg.register(new ChunkLoadingOptimizer());
        reg.register(new DynamicRenderDistance());
        reg.register(new SmartRenderDistance());
        reg.register(new ChunkUpdateBatching());
        reg.register(new ChunkMeshCache());
        reg.register(new BlockFaceCulling());
        reg.register(new OcclusionCulling());
        reg.register(new TransparentRenderOptimizer());
        reg.register(new RenderLayerOptimizer());
        reg.register(new SignRenderingOptimizer());
        reg.register(new NametagRenderingOptimizer());
        reg.register(new WeatherRenderingOptimizer());
        reg.register(new CloudRenderingOptimizer());
        reg.register(new SkyRenderingOptimizer());
        reg.register(new WaterRenderingOptimizer());
        reg.register(new LavaRenderingOptimizer());
        reg.register(new FireRenderingOptimizer());
        reg.register(new BeaconBeamOptimizer());
        reg.register(new XpOptimizer());
        reg.register(new TntOptimizer());
        reg.register(new ItemEntityOptimizer());
        reg.register(new FastHotbarOptimizer());
        reg.register(new PotionOptimizer());
        reg.register(new ElytraOptimizer());
        reg.register(new MaceOptimizer());
        reg.register(new TotemOptimizer());
        reg.register(new CrossbowOptimizer());
        reg.register(new BowOptimizer());
        reg.register(new ProjectileOptimizer());
        reg.register(new CrystalOptimizer());
        reg.register(new PearlOptimizer());
        reg.register(new ShieldOptimizer());
        reg.register(new KnockbackOptimizer());
        reg.register(new WindChargeOptimizer());
        reg.register(new StringOptimizer());
        reg.register(new SweetBerryOptimizer());
        reg.register(new ConsumableOptimizer());
        reg.register(new CartOptimizer());
        reg.register(new NoShadowBlocks());

        // 02 — PERFORMANCE & FPS (049–073)
        reg.register(new FpsDisplayModule());
        reg.register(new OnePercentLowFps());
        reg.register(new FrameTimeDisplay());
        reg.register(new FpsGraph());
        reg.register(new FrameTimeGraph());
        reg.register(new GpuUsageHud());
        reg.register(new CpuUsageHud());
        reg.register(new RamUsageHud());
        reg.register(new EntityCounter());
        reg.register(new ParticleCounter());
        reg.register(new ChunkCounter());
        reg.register(new PerformanceProfiler());
        reg.register(new FpsBottleneckDetector());
        reg.register(new FramePacingOptimizer());
        reg.register(new AdaptiveFpsLimiter());
        reg.register(new UnfocusedFpsLimiter());
        reg.register(new MenuFpsLimiter());
        reg.register(new WorldFpsLimiter());
        reg.register(new SmartVsync());
        reg.register(new DynamicSimulationDistance());
        reg.register(new LoadingScreenOptimizer());
        reg.register(new StartupOptimizer());
        reg.register(new FastWorldLoading());
        reg.register(new FastServerSwitchingPerf());
        reg.register(new FastQuit());

        // 03 — RAM / CPU / GPU (074–087)
        reg.register(new RamUsageOptimizer());
        reg.register(new MemoryCleanup());
        reg.register(new ManualMemoryCleanupKeybind());
        reg.register(new SmartMemoryCleanup());
        reg.register(new TextureMemoryOptimizer());
        reg.register(new ModelCacheOptimizer());
        reg.register(new ResourceCacheOptimizer());
        reg.register(new CpuThreadOptimizer());
        reg.register(new RenderThreadOptimizer());
        reg.register(new BackgroundTaskOptimizer());
        reg.register(new IdleTaskSuspension());
        reg.register(new AsyncResourceLoading());
        reg.register(new AsyncChunkPreparation());
        reg.register(new CrashSafeCleanup());

        // 04 — KEYBINDS / INPUT / MOVEMENT (088–103)
        reg.register(new CustomKeybindSystem());
        reg.register(new KeybindConflictDetectorModule());
        reg.register(new MidClickBind());
        reg.register(new MidClickElytra());
        reg.register(new MidClickRocket());
        reg.register(new MidClickXp());
        reg.register(new FastXpBottle());
        reg.register(new ToggleSprint());
        reg.register(new AutoSprint());
        reg.register(new ToggleSneak());
        reg.register(new MouseTweaks());
        reg.register(new BreakDelayControl());
        reg.register(new LeftClickDelayControl());
        reg.register(new FastBlockPlacement());
        reg.register(new AccurateBlockPlacement());
        reg.register(new FlySpeedControl());

        // 05 — PVP / COMBAT (104–122)
        reg.register(new CpsCounter());
        reg.register(new LeftRightCps());
        reg.register(new CpsGraph());
        reg.register(new AttackCooldownHud());
        reg.register(new WeaponCooldownHud());
        reg.register(new AttackIndicatorCustomizer());
        reg.register(new ReachDisplay());
        reg.register(new HitCounter());
        reg.register(new ComboCounter());
        reg.register(new HitEffect());
        reg.register(new DamageFlashControl());
        reg.register(new KnockbackCameraControl());
        reg.register(new HurtCameraControl());
        reg.register(new ShieldIndicator());
        reg.register(new ProjectileIndicator());
        reg.register(new TntFuseHud());
        reg.register(new CrystalStatusHud());
        reg.register(new AttackRangeVisualizer());
        reg.register(new ProjectileTrajectoryPreview());

        // 06 — HUD / INFORMATION (123–150)
        reg.register(new ModularHudEditorModule());
        reg.register(new HudScaleControl());
        reg.register(new HudOpacityControl());
        reg.register(new HudPresets());
        reg.register(new CoordinatesHud());
        reg.register(new DirectionHud());
        reg.register(new BiomeDisplay());
        reg.register(new LightLevelDisplay());
        reg.register(new TargetBlockHud());
        reg.register(new TargetEntityHud());
        reg.register(new ChunkCoordinates());
        reg.register(new DistanceDisplay());
        reg.register(new ServerTpsDisplay());
        reg.register(new PingDisplay());
        reg.register(new PingGraph());
        reg.register(new NetworkStatusHud());
        reg.register(new PacketRateDisplay());
        reg.register(new FpsHudDisplay());
        reg.register(new HealthBarHud());
        reg.register(new ArmorHud());
        reg.register(new PotionHud());
        reg.register(new EffectTimer());
        reg.register(new TotemCounter());
        reg.register(new InventoryHud());
        reg.register(new PlayTimeCounter());
        reg.register(new DayCounter());
        reg.register(new MoonPhase());
        reg.register(new RealTimeClock());

        // 07 — CAMERA / VIEWMODEL (151–167)
        reg.register(new Freelook());
        reg.register(new CameraSmoothing());
        reg.register(new CameraDistance());
        reg.register(new CameraCollisionFix());
        reg.register(new CustomFov());
        reg.register(new SprintFovControl());
        reg.register(new SpeedFovControl());
        reg.register(new ItemFovControl());
        reg.register(new DamageFovControl());
        reg.register(new NoViewBobbing());
        reg.register(new NoCameraShake());
        reg.register(new DamageTiltControl());
        reg.register(new MotionBlurControl());
        reg.register(new PotionCameraEffectOff());
        reg.register(new DeathCameraEffectOff());
        reg.register(new ViewmodelEditor());
        reg.register(new ThreeDF5View());

        // 08 — VISUAL / GRAPHICS (168–185)
        reg.register(new RenderScale());
        reg.register(new ResolutionControl());
        reg.register(new FxaaAntiAliasing());
        reg.register(new HdrToggle());
        reg.register(new SharpeningFilter());
        reg.register(new BrightnessControl());
        reg.register(new Fullbright());
        reg.register(new ColorSaturation());
        reg.register(new ColorControls());
        reg.register(new NoFog());
        reg.register(new CustomFog());
        reg.register(new ClearWater());
        reg.register(new ClearLava());
        reg.register(new LowFire());
        reg.register(new CustomSky());
        reg.register(new SkyColorWheel());
        reg.register(new DarkGuiMode());
        reg.register(new CustomCrosshair());

        // 09 — WORLD VISUAL (186–195)
        reg.register(new WeatherControl());
        reg.register(new WeatherIntensity());
        reg.register(new CloudControl());
        reg.register(new CloudDistance());
        reg.register(new WaterQualityControl());
        reg.register(new LavaQualityControl());
        reg.register(new FireRenderingControl());
        reg.register(new ParticleVisibilityControl());
        reg.register(new PotionParticleOff());
        reg.register(new PotionFovParticleOff());

        // 10 — INVENTORY / ITEMS (196–210)
        reg.register(new ShulkerBoxPreview());
        reg.register(new ContainerPreview());
        reg.register(new ItemHighlighter());
        reg.register(new RarityHighlight());
        reg.register(new StackCounter());
        reg.register(new DurabilityPercentage());
        reg.register(new DurabilityBarCustomizer());
        reg.register(new LowDurabilityAlert());
        reg.register(new ItemTooltipCustomizer());
        reg.register(new CompactTooltip());
        reg.register(new EnchantmentTooltip());
        reg.register(new PotionTooltip());
        reg.register(new OffhandHud());
        reg.register(new HeldItemScale());
        reg.register(new SmallItems());

        // 11 — SMALL ITEM SYSTEM (211–219)
        reg.register(new SmallSword());
        reg.register(new SmallAxe());
        reg.register(new SmallPickaxe());
        reg.register(new SmallShovel());
        reg.register(new SmallFood());
        reg.register(new SmallBlock());
        reg.register(new SmallShield());
        reg.register(new SmallTotem());
        reg.register(new TotemResizer());

        // 12 — CHAT (220–232)
        reg.register(new ChatHead());
        reg.register(new TablistHead());
        reg.register(new ChatFilter());
        reg.register(new ChatSearch());
        reg.register(new ChatCopy());
        reg.register(new ChatClear());
        reg.register(new ChatTimestamp());
        reg.register(new KeywordHighlight());
        reg.register(new MentionAlert());
        reg.register(new ChatScale());
        reg.register(new ChatWidthHeight());
        reg.register(new ChatBackgroundOpacity());
        reg.register(new AutoGg());

        // 13 — NETWORK / SERVER (233–242)
        reg.register(new NetworkOptimizer());
        reg.register(new LatencyOptimizer());
        reg.register(new FastIpPing());
        reg.register(new ConnectionStatus());
        reg.register(new AutoReconnect());
        reg.register(new ReconnectDelay());
        reg.register(new ServerAddressDisplay());
        reg.register(new ServerBrandDisplay());
        reg.register(new FastServerSwitchingNet());
        reg.register(new VoiceChatIntegration());

        // 14 — WORLD / MAP (243–254)
        reg.register(new Waypoints());
        reg.register(new WaypointCoordinates());
        reg.register(new WaypointDistance());
        reg.register(new WaypointColors());
        reg.register(new WaypointGroups());
        reg.register(new DimensionSpecificWaypoints());
        reg.register(new DeathLocation());
        reg.register(new DeathWaypoint());
        reg.register(new ChunkBorderDisplay());
        reg.register(new SlimeChunkInfo());
        reg.register(new SpawnLocation());
        reg.register(new StructureLocationHelper());

        // 15 — SCOREBOARD / TABLIST / NAMETAG (255–259)
        reg.register(new CustomTablist());
        reg.register(new ScoreboardCustomizer());
        reg.register(new CustomNametag());
        reg.register(new NametagDistanceControl());
        reg.register(new BcNametagIcon());

        // 16 — CLIENT / SETTINGS (260–280)
        reg.register(new ProfileManagerModule());
        reg.register(new ServerSpecificProfile());
        reg.register(new WorldSpecificProfile());
        reg.register(new PvpProfile());
        reg.register(new SurvivalProfile());
        reg.register(new LowEndProfile());
        reg.register(new BalancedProfile());
        reg.register(new QualityProfile());
        reg.register(new ExtremeFpsProfile());
        reg.register(new OneClickOptimization());
        reg.register(new HardwareDetection());
        reg.register(new RecommendedSettings());
        reg.register(new SettingsSearch());
        reg.register(new FavoriteSettings());
        reg.register(new ConfigBackup());
        reg.register(new ConfigImportExportModule());
        reg.register(new SafeMode());
        reg.register(new CompatibilityMode());
        reg.register(new CrashProtection());
        reg.register(new VanillaMode());
        reg.register(new LoadingIndicator());

        // 17 — ADVANCED PERFORMANCE (281–293)
        reg.register(new PerformanceDashboard());
        reg.register(new AutomaticPerformanceTuning());
        reg.register(new TargetFpsMode());
        reg.register(new BatterySaver());
        reg.register(new ThermalMode());
        reg.register(new BackgroundMode());
        reg.register(new LowPowerMode());
        reg.register(new AfkOptimization());
        reg.register(new SessionStatistics());
        reg.register(new PerformanceHistory());
        reg.register(new OptimizationLog());
        reg.register(new ResetOptimization());
        reg.register(new MasterPerformanceSwitch());

        // 18 — CLIENT QUALITY FEATURES (294–303)
        reg.register(new CustomMainMenuModule());
        reg.register(new CustomLoadingScreenModule());
        reg.register(new DarkModeGui());
        reg.register(new GuiAnimationControl());
        reg.register(new GuiRenderingOptimizer());
        reg.register(new ResourceReloadOptimizer());
        reg.register(new StartupCache());
        reg.register(new FastResourceReload());
        reg.register(new ConfigAutoSave());
        reg.register(new ConfigRecovery());

        // 19 — BANGLACLIENT CORE (304–315)
        reg.register(new BanglaClientHud());
        reg.register(new BanglaClientThemeSystem());
        reg.register(new BanglaClientIconSystem());
        reg.register(new FeatureToggleSystem());
        reg.register(new CategoryBasedSettings());
        reg.register(new SearchableSettingsMenu());
        reg.register(new ProfileSwitching());
        reg.register(new GlobalKeybindManagerModule());
        reg.register(new PerformancePresets());
        reg.register(new VisualPresets());
        reg.register(new PvpPresets());
        reg.register(new BackupAndRestore());

        BanglaLogger.info("Successfully registered {} independent BANGLACLIENT modules across 19 categories.",
                reg.getAllModules().size());
    }
}
