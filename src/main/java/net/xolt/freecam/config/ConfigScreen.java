package net.xolt.freecam.config;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

public class ConfigScreen extends Screen {
    private static final int TITLE_HEIGHT = 8;
    private static final int OPTIONS_LIST_TOP_HEIGHT = 24;
    private static final int OPTIONS_LIST_BOTTOM_OFFSET = 32;
    private static final int OPTIONS_LIST_ITEM_HEIGHT = 25;
    private static final int DONE_BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 20;
    private static final int DONE_BUTTON_TOP_OFFSET = 26;
    private static final int COLLISION_BUTTON_WIDTH = 100;
    private static final int COLLISION_BUTTON_SIDE_OFFSET = 8;
    private static final int COLLISION_BUTTON_TOP_OFFSET = 2;

    protected final Screen previous;

    protected OptionsList optionsList;

    public ConfigScreen(Screen previous) {
        this(previous, Component.translatable("text.freecam.configScreen.title"));
    }

    public ConfigScreen(Screen previous, Component title) {
        super (title);
        this.previous = previous;
    }

    @Override
    protected void init() {
        this.optionsList = new OptionsList(
                this.minecraft, this.width, this.height,
                OPTIONS_LIST_TOP_HEIGHT,
                this.height - OPTIONS_LIST_BOTTOM_OFFSET,
                OPTIONS_LIST_ITEM_HEIGHT
        );

        OptionInstance<FreecamConfig.FlightMode> flightMode = new OptionInstance<FreecamConfig.FlightMode>(
                "text.freecam.configScreen.option.flightMode",
                (value2) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.flightMode.tooltip")),
                (unused, option) -> Component.translatable(option.getKey()),
                new OptionInstance.Enum<>(Arrays.asList(FreecamConfig.FlightMode.values()), Codec.INT.xmap(FreecamConfig.FlightMode::byId, FreecamConfig.FlightMode::getId)),
                (FreecamConfig.FlightMode) FreecamConfig.FLIGHT_MODE.get(),
                (newValue) -> FreecamConfig.FLIGHT_MODE.set(newValue)
        );
        this.optionsList.addBig(flightMode);

        OptionInstance<FreecamConfig.Perspective> perspective = new OptionInstance<FreecamConfig.Perspective>(
                "text.freecam.configScreen.option.perspective",
                (value2) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.perspective.tooltip")),
                (unused, option) -> Component.translatable(option.getKey()),
                new OptionInstance.Enum<>(Arrays.asList(FreecamConfig.Perspective.values()), Codec.INT.xmap(FreecamConfig.Perspective::byId, FreecamConfig.Perspective::getId)),
                (FreecamConfig.Perspective) FreecamConfig.PERSPECTIVE.get(),
                (newValue) -> FreecamConfig.PERSPECTIVE.set(newValue)
        );
        this.optionsList.addBig(perspective);

        OptionInstance<FreecamConfig.InteractionMode> interactionMode = new OptionInstance<FreecamConfig.InteractionMode>(
                "text.freecam.configScreen.option.interactionMode",
                (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.interactionMode.tooltip")),
                (unused, option) -> Component.translatable(option.getKey()),
                new OptionInstance.Enum<>(Arrays.asList(FreecamConfig.InteractionMode.values()), Codec.INT.xmap(FreecamConfig.InteractionMode::byId, FreecamConfig.InteractionMode::getId)),
                (FreecamConfig.InteractionMode) FreecamConfig.INTERACTION_MODE.get(),
                (newValue) -> FreecamConfig.INTERACTION_MODE.set(newValue)
        );
        this.optionsList.addBig(interactionMode);

        OptionInstance<Double> horizontalSpeed = new OptionInstance<>(
                "text.freecam.configScreen.option.horizontalSpeed",
                (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.horizontalSpeed.tooltip")),
                (unused, option) -> Component.translatable("text.freecam.configScreen.option.horizontalSpeed").append(": " + FreecamConfig.HORIZONTAL_SPEED.get()),
                OptionInstance.UnitDouble.INSTANCE,
                FreecamConfig.HORIZONTAL_SPEED.get() / 10,
                (value) -> {
                    if (Math.abs(value - (FreecamConfig.HORIZONTAL_SPEED.get() / 10)) >= 0.01)
                        FreecamConfig.HORIZONTAL_SPEED.set(Math.round(value * 100.0) / 10.0);
                }
        );
        this.optionsList.addBig(horizontalSpeed);

        OptionInstance<Double> verticalSpeed = new OptionInstance<>(
                "text.freecam.configScreen.option.verticalSpeed",
                (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.verticalSpeed.tooltip")),
                (unused, option) -> Component.translatable("text.freecam.configScreen.option.verticalSpeed").append(": " + FreecamConfig.VERTICAL_SPEED.get()),
                OptionInstance.UnitDouble.INSTANCE,
                FreecamConfig.VERTICAL_SPEED.get() / 10,
                (value) -> {
                    if (Math.abs(value - (FreecamConfig.VERTICAL_SPEED.get() / 10)) >= 0.01)
                        FreecamConfig.VERTICAL_SPEED.set(Math.round(value * 100.0) / 10.0);
                }
        );
        this.optionsList.addBig(verticalSpeed);

        OptionInstance<Boolean> disableOnDamage = OptionInstance.createBoolean(
                "text.freecam.configScreen.option.disableOnDamage",
                (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.disableOnDamage.tooltip")),
                FreecamConfig.DISABLE_ON_DAMAGE.get(),
                (value) -> FreecamConfig.DISABLE_ON_DAMAGE.set(value)
        );
        this.optionsList.addBig(disableOnDamage);

        OptionInstance<Boolean> allowInteract = OptionInstance.createBoolean(
                "text.freecam.configScreen.option.allowInteract",
                (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.allowInteract.tooltip")),
                FreecamConfig.ALLOW_INTERACT.get(),
                (value) -> FreecamConfig.ALLOW_INTERACT.set(value)
        );
        this.optionsList.addBig(allowInteract);

        OptionInstance<Boolean> freezePlayer = OptionInstance.createBoolean(
                "text.freecam.configScreen.option.freezePlayer",
                (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.freezePlayer.tooltip")),
                FreecamConfig.FREEZE_PLAYER.get(),
                (value) -> FreecamConfig.FREEZE_PLAYER.set(value)
        );
        this.optionsList.addBig(freezePlayer);

        OptionInstance<Boolean> showPlayer = OptionInstance.createBoolean(
                "text.freecam.configScreen.option.showPlayer",
                (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.showPlayer.tooltip")),
                FreecamConfig.SHOW_PLAYER.get(),
                (value) -> FreecamConfig.SHOW_PLAYER.set(value)
        );
        this.optionsList.addBig(showPlayer);

        OptionInstance<Boolean> showHand = OptionInstance.createBoolean(
                "text.freecam.configScreen.option.showHand",
                (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.showHand.tooltip")),
                FreecamConfig.SHOW_HAND.get(),
                (value) -> FreecamConfig.SHOW_HAND.set(value)
        );
        this.optionsList.addBig(showHand);

        OptionInstance<Boolean> showSubmersion = OptionInstance.createBoolean(
                "text.freecam.configScreen.option.showSubmersion",
                (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.showSubmersion.tooltip")),
                FreecamConfig.SHOW_SUBMERSION.get(),
                (value) -> FreecamConfig.SHOW_SUBMERSION.set(value)
        );
        this.optionsList.addBig(showSubmersion);

        OptionInstance<Boolean> notifyFreecam = OptionInstance.createBoolean(
                "text.freecam.configScreen.option.notifyFreecam",
                (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.notifyFreecam.tooltip")),
                FreecamConfig.NOTIFY_FREECAM.get(),
                (value) -> FreecamConfig.NOTIFY_FREECAM.set(value)
        );
        this.optionsList.addBig(notifyFreecam);

        OptionInstance<Boolean> notifyTripod = OptionInstance.createBoolean(
                "text.freecam.configScreen.option.notifyTripod",
                (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.notifyTripod.tooltip")),
                FreecamConfig.NOTIFY_TRIPOD.get(),
                (value) -> FreecamConfig.NOTIFY_TRIPOD.set(value)
        );
        this.optionsList.addBig(notifyTripod);

        this.addWidget(optionsList);

        this.addRenderableWidget(Button.builder(Component.translatable("text.freecam.collisionOptionsScreen.title"), (button) -> {
            this.minecraft.setScreen(new CollisionOptionsScreen(this));
        }).bounds(this.width - COLLISION_BUTTON_WIDTH - COLLISION_BUTTON_SIDE_OFFSET,
                COLLISION_BUTTON_TOP_OFFSET,
                COLLISION_BUTTON_WIDTH, BUTTON_HEIGHT).build());

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> {
            this.onClose();
        }).bounds((this.width - DONE_BUTTON_WIDTH) / 2,
                this.height - DONE_BUTTON_TOP_OFFSET,
                DONE_BUTTON_WIDTH, BUTTON_HEIGHT).build());
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(previous);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(guiGraphics);
        this.optionsList.render(guiGraphics, pMouseX, pMouseY, pPartialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, TITLE_HEIGHT, 16777215);
        super.render(guiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    private static class CollisionOptionsScreen extends ConfigScreen {

        public CollisionOptionsScreen(Screen previous) {
            super(previous, Component.translatable("text.freecam.collisionOptionsScreen.title"));
        }

        @Override
        protected void init() {
            this.optionsList = new OptionsList(
                    this.minecraft, this.width, this.height,
                    OPTIONS_LIST_TOP_HEIGHT,
                    this.height - OPTIONS_LIST_BOTTOM_OFFSET,
                    OPTIONS_LIST_ITEM_HEIGHT
            );

            OptionInstance<Boolean> ignoreTransparent = OptionInstance.createBoolean(
                    "text.freecam.configScreen.collision.option.ignoreTransparent",
                    (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.collision.option.ignoreTransparent.tooltip")),
                    FreecamConfig.IGNORE_TRANSPARENT_COLLISION.get(),
                    (value) -> FreecamConfig.IGNORE_TRANSPARENT_COLLISION.set(value)
            );
            this.optionsList.addBig(ignoreTransparent);

            OptionInstance<Boolean> ignoreOpenable = OptionInstance.createBoolean(
                    "text.freecam.configScreen.collision.option.ignoreOpenable",
                    (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.collision.option.ignoreOpenable.tooltip")),
                    FreecamConfig.IGNORE_OPENABLE_COLLISION.get(),
                    (value) -> FreecamConfig.IGNORE_OPENABLE_COLLISION.set(value)
            );
            this.optionsList.addBig(ignoreOpenable);

            OptionInstance<Boolean> ignoreAll = OptionInstance.createBoolean(
                    "text.freecam.configScreen.collision.option.ignoreAll",
                    (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.collision.option.ignoreAll.tooltip")),
                    FreecamConfig.IGNORE_ALL_COLLISION.get(),
                    (value) -> FreecamConfig.IGNORE_ALL_COLLISION.set(value)
            );
            this.optionsList.addBig(ignoreAll);

            OptionInstance<Boolean> alwaysCheck = OptionInstance.createBoolean(
                    "text.freecam.configScreen.collision.option.alwaysCheck",
                    (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.collision.option.alwaysCheck.tooltip")),
                    FreecamConfig.ALWAYS_CHECK_COLLISION.get(),
                    (value) -> FreecamConfig.ALWAYS_CHECK_COLLISION.set(value)
            );
            this.optionsList.addBig(alwaysCheck);

            this.addWidget(optionsList);

            this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> {
                this.onClose();
            }).bounds((this.width - DONE_BUTTON_WIDTH) / 2,
                    this.height - DONE_BUTTON_TOP_OFFSET,
                    DONE_BUTTON_WIDTH, BUTTON_HEIGHT).build());
        }
    }
}
