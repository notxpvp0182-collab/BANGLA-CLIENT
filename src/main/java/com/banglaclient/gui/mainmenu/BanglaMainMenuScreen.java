package com.banglaclient.gui.mainmenu;

import com.banglaclient.animation.SmoothFloat;
import com.banglaclient.core.ClientCore;
import com.banglaclient.gui.hudeditor.HudEditorScreen;
import com.banglaclient.integration.ExternalModDetector;
import com.banglaclient.integration.ExternalModEntry;
import com.banglaclient.keybind.KeybindManager;
import com.banglaclient.theme.ThemeManager;
import com.banglaclient.theme.ThemePalette;
import com.banglaclient.util.ClientResourceManager;
import com.banglaclient.util.Render2DUtil;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.LanguageOptionsScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom BANGLACLIENT Main Menu Screen modeled after Screenshot 2.
 *
 * Features:
 * - Replaceable background resource (assets/banglaclient/textures/gui/main_menu_background_demo.png)
 * - Centered BANGLACLIENT logo & title
 * - Vertical main action buttons: Singleplayer, Multiplayer, Cosmetics, Mods
 * - Store button is REMOVED as explicitly instructed
 * - Highlighted Quit Game button below
 * - Top-right utility toolbar (Player session pill, Click GUI launcher, HUD Editor, Options, Language)
 * - Left-side sequential External Mod navigation buttons that appear/disappear dynamically
 */
public final class BanglaMainMenuScreen extends Screen {

    private static final class MenuButtonItem {
        final String label;
        final float x;
        final float y;
        final float width;
        final float height;
        final boolean primaryAccent;
        final Runnable action;
        final SmoothFloat hoverAnim = new SmoothFloat(0.0f, 16.0f);

        MenuButtonItem(String label, float x, float y, float width, float height, boolean primaryAccent, Runnable action) {
            this.label = label;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.primaryAccent = primaryAccent;
            this.action = action;
        }

        boolean isMouseOver(double mx, double my) {
            return mx >= x && mx <= (x + width) && my >= y && my <= (y + height);
        }
    }

    private final List<MenuButtonItem> centerButtons = new ArrayList<>();
    private final List<MenuButtonItem> topToolbarButtons = new ArrayList<>();
    private final List<MenuButtonItem> externalModButtons = new ArrayList<>();
    private final SmoothFloat entranceAnim = new SmoothFloat(0.0f, 10.0f);
    private boolean showCosmeticsNotice = false;

    public BanglaMainMenuScreen() {
        super(Text.literal(ClientCore.MOD_NAME));
        this.entranceAnim.setTarget(1.0f);
    }

    @Override
    protected void init() {
        super.init();
        centerButtons.clear();
        topToolbarButtons.clear();
        externalModButtons.clear();

        int cx = this.width / 2;
        int cy = this.height / 2;
        float btnW = 180.0f;
        float btnH = 26.0f;
        float gap = 5.0f;
        float startY = cy - 28.0f;

        // 1. Singleplayer
        centerButtons.add(new MenuButtonItem("Singleplayer", cx - btnW / 2.0f, startY, btnW, btnH, false, () -> {
            if (this.client != null) {
                this.client.setScreen(new SelectWorldScreen(this));
            }
        }));

        // 2. Multiplayer
        centerButtons.add(new MenuButtonItem("Multiplayer", cx - btnW / 2.0f, startY + (btnH + gap), btnW, btnH, false, () -> {
            if (this.client != null) {
                this.client.setScreen(new MultiplayerScreen(this));
            }
        }));

        // 3. Cosmetics
        centerButtons.add(new MenuButtonItem("Cosmetics", cx - btnW / 2.0f, startY + (btnH + gap) * 2, btnW, btnH, false, () -> {
            showCosmeticsNotice = !showCosmeticsNotice;
        }));

        // 4. Mods (Opens BANGLACLIENT Click GUI / Module Manager)
        centerButtons.add(new MenuButtonItem("Mods", cx - btnW / 2.0f, startY + (btnH + gap) * 3, btnW, btnH, false, () -> {
            KeybindManager.getInstance().onKeyInput(KeybindManager.getInstance().getOpenGuiKeybind().getKeyCode(), 1);
        }));

        // Note: Store button intentionally omitted per section 7 & 55.

        // 5. Quit Game (Highlighted Accent Button separated below)
        centerButtons.add(new MenuButtonItem("Quit Game", cx - 66.0f, startY + (btnH + gap) * 4 + 18.0f, 132.0f, 26.0f, true, () -> {
            if (this.client != null) {
                this.client.scheduleStop();
            }
        }));

        // Top-right toolbar buttons (Screenshot 2 reference layout)
        float topY = 14.0f;
        float rightCursor = this.width - 16.0f;
        float sqSize = 24.0f;

        // Brand Quick GUI Button (Far right accent square)
        rightCursor -= sqSize;
        topToolbarButtons.add(new MenuButtonItem("BC", rightCursor, topY, sqSize, sqSize, true, () -> {
            KeybindManager.getInstance().onKeyInput(KeybindManager.getInstance().getOpenGuiKeybind().getKeyCode(), 1);
        }));

        // Options Button
        rightCursor -= (sqSize + 5.0f);
        topToolbarButtons.add(new MenuButtonItem("OPT", rightCursor, topY, sqSize, sqSize, false, () -> {
            if (this.client != null) {
                this.client.setScreen(new OptionsScreen(this, this.client.options));
            }
        }));

        // Language Button
        rightCursor -= (sqSize + 5.0f);
        topToolbarButtons.add(new MenuButtonItem("LNG", rightCursor, topY, sqSize, sqSize, false, () -> {
            if (this.client != null) {
                this.client.setScreen(new LanguageOptionsScreen(this, this.client.options, this.client.getLanguageManager()));
            }
        }));

        // HUD Editor Button
        rightCursor -= (sqSize + 5.0f);
        topToolbarButtons.add(new MenuButtonItem("HUD", rightCursor, topY, sqSize, sqSize, false, () -> {
            if (this.client != null) {
                this.client.setScreen(new HudEditorScreen(this));
            }
        }));

        // Dynamic External Mod Buttons in the Left-Side Navigation Area
        List<ExternalModEntry> externalMods = ExternalModDetector.getInstance().getDetectedEntries();
        float extX = 16.0f;
        float extY = 52.0f;
        float extW = 128.0f;
        float extH = 24.0f;

        for (ExternalModEntry entry : externalMods) {
            externalModButtons.add(new MenuButtonItem(entry.getDisplayName(), extX, extY, extW, extH, false, () -> {
                if (this.client != null) {
                    Screen target = entry.createScreen(this);
                    if (target != null) {
                        this.client.setScreen(target);
                    }
                }
            }));
            extY += (extH + 5.0f);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        ClientResourceManager.drawMainMenuBackground(context, this.width, this.height);

        ThemePalette palette = ThemeManager.getInstance().getPalette();
        float anim = entranceAnim.updateAndGet();

        int cx = this.width / 2;
        int cy = this.height / 2;

        // Center Brand Logo + BANGLACLIENT Title (Screenshot 2 composition)
        int logoSize = 32;
        String brandTitle = ClientCore.MOD_NAME;
        int titleW = this.textRenderer.getWidth(brandTitle);
        int totalBrandW = logoSize + 10 + titleW;
        int brandStartX = cx - totalBrandW / 2;
        int brandY = cy - 76;

        ClientResourceManager.drawBrandLogo(context, brandStartX, brandY, logoSize);
        context.drawText(this.textRenderer, brandTitle, brandStartX + logoSize + 10, brandY + 12, palette.getPrimaryText().getArgb(), true);

        // Render Center Action Stack
        for (MenuButtonItem btn : centerButtons) {
            renderStyledButton(context, btn, mouseX, mouseY, palette, anim);
        }

        // Render Top-Right Account Pill & Toolbar
        if (this.client != null && this.client.getSession() != null) {
            String username = this.client.getSession().getUsername();
            float pillW = Math.max(76.0f, this.textRenderer.getWidth(username) + 24.0f);
            float pillX = this.width - 16.0f - (topToolbarButtons.size() * 29.0f) - pillW - 4.0f;
            float pillY = 14.0f;
            Render2DUtil.drawRoundedRect(context, pillX, pillY, pillW, 24.0f, 3.0f, 0xCC121216);
            Render2DUtil.drawRoundedOutline(context, pillX, pillY, pillW, 24.0f, 3.0f, 1.0f, palette.getBorder().getArgb());
            context.drawText(this.textRenderer, username, Math.round(pillX + 12.0f), Math.round(pillY + 8.0f), palette.getPrimaryText().getArgb(), false);
        }

        for (MenuButtonItem btn : topToolbarButtons) {
            renderStyledButton(context, btn, mouseX, mouseY, palette, anim);
        }

        // Render Left-Side External Mod Navigation Sidebar
        if (!externalModButtons.isEmpty()) {
            context.drawText(this.textRenderer, "EXTERNAL MODS", 16, 38, palette.getSecondaryText().getArgb(), false);
            for (MenuButtonItem extBtn : externalModButtons) {
                renderStyledButton(context, extBtn, mouseX, mouseY, palette, anim);
            }
        }

        // Version Footer below Quit Game
        String versionFooter = "BanglaClient " + ClientCore.MOD_VERSION + " (Minecraft 1.21.11 / " + ClientCore.DEVELOPER + ")";
        int footerW = this.textRenderer.getWidth(versionFooter);
        context.drawText(
                this.textRenderer,
                versionFooter,
                cx - footerW / 2,
                Math.round(cy + 136.0f),
                palette.getSecondaryText().getArgb(),
                false
        );

        // Bottom-Right Official Bangla SMP Card
        float cardW = 148.0f;
        float cardH = 42.0f;
        float cardX = this.width - cardW - 16.0f;
        float cardY = this.height - cardH - 16.0f;
        Render2DUtil.drawRoundedRect(context, cardX, cardY, cardW, cardH, 4.0f, 0xD8101014);
        Render2DUtil.drawRoundedOutline(context, cardX, cardY, cardW, cardH, 4.0f, 1.0f, palette.getBorder().getArgb());
        context.drawText(this.textRenderer, "Bangla SMP Official", Math.round(cardX + 10), Math.round(cardY + 9), palette.getAccent().getArgb(), false);
        context.drawText(this.textRenderer, ClientCore.OFFICIAL_SERVER, Math.round(cardX + 10), Math.round(cardY + 23), palette.getPrimaryText().getArgb(), false);

        if (showCosmeticsNotice) {
            String msg = "BC Nametag Icon & Cosmetics active via Scoreboard / Nametag Category";
            int mw = this.textRenderer.getWidth(msg);
            Render2DUtil.drawRoundedRect(context, cx - mw / 2.0f - 10.0f, 52.0f, mw + 20.0f, 22.0f, 4.0f, 0xEE16161C);
            Render2DUtil.drawRoundedOutline(context, cx - mw / 2.0f - 10.0f, 52.0f, mw + 20.0f, 22.0f, 4.0f, 1.0f, palette.getAccent().getArgb());
            context.drawText(this.textRenderer, msg, cx - mw / 2, 59, palette.getPrimaryText().getArgb(), false);
        }
    }

    private void renderStyledButton(DrawContext context, MenuButtonItem btn, int mouseX, int mouseY, ThemePalette palette, float alphaFactor) {
        boolean hovered = btn.isMouseOver(mouseX, mouseY);
        btn.hoverAnim.setTarget(hovered ? 1.0f : 0.0f);
        float h = btn.hoverAnim.updateAndGet();

        int baseBg = btn.primaryAccent
                ? palette.getAccent().withAlphaFactor(0.85f * alphaFactor).getArgb()
                : 0xCC121216;
        int hoverBg = btn.primaryAccent
                ? palette.getHover().withAlphaFactor(alphaFactor).getArgb()
                : palette.getCard().withAlphaFactor(alphaFactor).getArgb();

        int bg = h > 0.01f ? palette.getCard().interpolate(palette.getAccent(), btn.primaryAccent ? 0.85f : h * 0.25f).getArgb() : baseBg;
        if (btn.primaryAccent && h <= 0.01f) {
            bg = baseBg;
        } else if (btn.primaryAccent) {
            bg = hoverBg;
        }

        int border = hovered ? palette.getAccent().getArgb() : palette.getBorder().getArgb();

        Render2DUtil.drawRoundedRect(context, btn.x, btn.y, btn.width, btn.height, 3.0f, bg);
        Render2DUtil.drawRoundedOutline(context, btn.x, btn.y, btn.width, btn.height, 3.0f, 1.0f, border);

        int textW = this.textRenderer.getWidth(btn.label);
        int textX = Math.round(btn.x + (btn.width - textW) / 2.0f);
        int textY = Math.round(btn.y + (btn.height - this.textRenderer.fontHeight) / 2.0f) + 1;
        context.drawText(this.textRenderer, btn.label, textX, textY, palette.getPrimaryText().getArgb(), true);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (MenuButtonItem item : centerButtons) {
                if (item.isMouseOver(mouseX, mouseY)) {
                    item.action.run();
                    return true;
                }
            }
            for (MenuButtonItem item : topToolbarButtons) {
                if (item.isMouseOver(mouseX, mouseY)) {
                    item.action.run();
                    return true;
                }
            }
            for (MenuButtonItem item : externalModButtons) {
                if (item.isMouseOver(mouseX, mouseY)) {
                    item.action.run();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (click != null && this.mouseClicked(click.x(), click.y(), click.button())) {
            return true;
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseDragged(Click click, double deltaX, double deltaY) {
        
        return super.mouseDragged(click, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(Click click) {
        
        return super.mouseReleased(click);
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        
        return super.keyPressed(input);
    }

    @Override
    public boolean charTyped(CharInput input) {
        
        return super.charTyped(input);
    }
}
