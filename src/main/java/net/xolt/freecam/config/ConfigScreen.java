package net.xolt.freecam.config;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

public class ConfigScreen extends Screen {
    private static final int TITLE_TOP_OFFSET = 8;
    private static final int OPTIONS_LIST_TOP_OFFSET = 24;
    private static final int OPTIONS_LIST_BOTTOM_OFFSET = 32;
    private static final int OPTIONS_LIST_ITEM_HEIGHT = 25;
    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_SPACING = 5;
    private static final int DONE_BUTTON_WIDTH = 200;
    private static final int DONE_BUTTON_BOTTOM_OFFSET = 6;

    protected final Screen previous;

    public ConfigScreen(Screen previous) {
        this(previous, Component.translatable("text.freecam.configScreen.title"));
    }

    public ConfigScreen(Screen previous, Component title) {
        super(title);
        this.previous = previous;
    }

    @Override
    protected void init() {
        int verticalOffset = OPTIONS_LIST_TOP_OFFSET + BUTTON_SPACING;

        this.addRenderableWidget(Button.builder(Component.translatable("text.freecam.configScreen.option.movement"), (button) -> {
            this.minecraft.setScreen(new MovementOptionsScreen(this));
        }).bounds((this.width - BUTTON_WIDTH) / 2,
                verticalOffset,
                BUTTON_WIDTH, BUTTON_HEIGHT)
                .tooltip(Tooltip.create(Component.translatable("text.freecam.configScreen.option.movement.@Tooltip"))).build());

        verticalOffset += BUTTON_HEIGHT + BUTTON_SPACING;

        this.addRenderableWidget(Button.builder(Component.translatable("text.freecam.configScreen.option.collision"), (button) -> {
            this.minecraft.setScreen(new CollisionOptionsScreen(this));
        }).bounds((this.width - BUTTON_WIDTH) / 2,
                verticalOffset,
                BUTTON_WIDTH, BUTTON_HEIGHT)
                .tooltip(Tooltip.create(Component.translatable("text.freecam.configScreen.option.collision.@Tooltip"))).build());

        verticalOffset += BUTTON_HEIGHT + BUTTON_SPACING;

        this.addRenderableWidget(Button.builder(Component.translatable("text.freecam.configScreen.option.visual"), (button) -> {
            this.minecraft.setScreen(new VisualOptionsScreen(this));
        }).bounds((this.width - BUTTON_WIDTH) / 2,
                verticalOffset,
                BUTTON_WIDTH, BUTTON_HEIGHT)
                .tooltip(Tooltip.create(Component.translatable("text.freecam.configScreen.option.visual.@Tooltip"))).build());

        verticalOffset += BUTTON_HEIGHT + BUTTON_SPACING;

        this.addRenderableWidget(Button.builder(Component.translatable("text.freecam.configScreen.option.utility"), (button) -> {
            this.minecraft.setScreen(new UtilityOptionsScreen(this));
        }).bounds((this.width - BUTTON_WIDTH) / 2,
                verticalOffset,
                BUTTON_WIDTH, BUTTON_HEIGHT)
                .tooltip(Tooltip.create(Component.translatable("text.freecam.configScreen.option.utility.@Tooltip"))).build());

        verticalOffset += BUTTON_HEIGHT + BUTTON_SPACING;

        this.addRenderableWidget(Button.builder(Component.translatable("text.freecam.configScreen.option.notification"), (button) -> {
            this.minecraft.setScreen(new NotificationOptionsScreen(this));
        }).bounds((this.width - BUTTON_WIDTH) / 2,
                verticalOffset,
                BUTTON_WIDTH, BUTTON_HEIGHT)
                .tooltip(Tooltip.create(Component.translatable("text.freecam.configScreen.option.notification.@Tooltip"))).build());

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> {
            this.onClose();
        }).bounds((this.width - DONE_BUTTON_WIDTH) / 2,
                this.height - BUTTON_HEIGHT - DONE_BUTTON_BOTTOM_OFFSET,
                DONE_BUTTON_WIDTH, BUTTON_HEIGHT).build());
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(previous);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        drawCenteredString(pPoseStack, this.font, this.title, this.width / 2, TITLE_TOP_OFFSET, 16777215);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    private static class OptionsListScreen extends Screen {

        protected final Screen previous;

        protected OptionsList optionsList;

        protected OptionsListScreen(Screen previous, Component title) {
            super(title);
            this.previous = previous;
        }

        @Override
        protected void init() {
            this.optionsList = new OptionsList(
                    this.minecraft, this.width, this.height,
                    OPTIONS_LIST_TOP_OFFSET,
                    this.height - OPTIONS_LIST_BOTTOM_OFFSET,
                    OPTIONS_LIST_ITEM_HEIGHT
            );

            this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> {
                this.onClose();
            }).bounds((this.width - DONE_BUTTON_WIDTH) / 2,
                    this.height - BUTTON_HEIGHT - DONE_BUTTON_BOTTOM_OFFSET,
                    DONE_BUTTON_WIDTH, BUTTON_HEIGHT).build());
        }

        @Override
        public void onClose() {
            this.minecraft.setScreen(previous);
        }

        @Override
        public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
            this.renderBackground(pPoseStack);
            this.optionsList.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
            drawCenteredString(pPoseStack, this.font, this.title, this.width / 2, TITLE_TOP_OFFSET, 16777215);
            super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        }
    }

    private static class MovementOptionsScreen extends OptionsListScreen {

        public MovementOptionsScreen(Screen previous) {
            super(previous, Component.translatable("text.freecam.configScreen.option.movement"));
        }

        @Override
        protected void init() {
            super.init();

            OptionInstance<FreecamConfig.FlightMode> flightMode = new OptionInstance<FreecamConfig.FlightMode>(
                    "text.freecam.configScreen.option.movement.flightMode",
                    (value2) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.movement.flightMode.@Tooltip")),
                    (unused, option) -> Component.translatable(option.getKey()),
                    new OptionInstance.Enum<>(Arrays.asList(FreecamConfig.FlightMode.values()), Codec.INT.xmap(FreecamConfig.FlightMode::byId, FreecamConfig.FlightMode::getId)),
                    (FreecamConfig.FlightMode) FreecamConfig.FLIGHT_MODE.get(),
                    (newValue) -> FreecamConfig.FLIGHT_MODE.set(newValue)
            );
            this.optionsList.addBig(flightMode);

            OptionInstance<Double> horizontalSpeed = new OptionInstance<>(
                    "text.freecam.configScreen.option.movement.horizontalSpeed",
                    (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.movement.horizontalSpeed.@Tooltip")),
                    (unused, option) -> Component.translatable("text.freecam.configScreen.option.movement.horizontalSpeed").append(": " + FreecamConfig.HORIZONTAL_SPEED.get()),
                    OptionInstance.UnitDouble.INSTANCE,
                    FreecamConfig.HORIZONTAL_SPEED.get() / 10,
                    (value) -> {
                        if (Math.abs(value - (FreecamConfig.HORIZONTAL_SPEED.get() / 10)) >= 0.01)
                            FreecamConfig.HORIZONTAL_SPEED.set(Math.round(value * 100.0) / 10.0);
                    }
            );
            this.optionsList.addBig(horizontalSpeed);

            OptionInstance<Double> verticalSpeed = new OptionInstance<>(
                    "text.freecam.configScreen.option.movement.verticalSpeed",
                    (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.movement.verticalSpeed.@Tooltip")),
                    (unused, option) -> Component.translatable("text.freecam.configScreen.option.movement.verticalSpeed").append(": " + FreecamConfig.VERTICAL_SPEED.get()),
                    OptionInstance.UnitDouble.INSTANCE,
                    FreecamConfig.VERTICAL_SPEED.get() / 10,
                    (value) -> {
                        if (Math.abs(value - (FreecamConfig.VERTICAL_SPEED.get() / 10)) >= 0.01)
                            FreecamConfig.VERTICAL_SPEED.set(Math.round(value * 100.0) / 10.0);
                    }
            );
            this.optionsList.addBig(verticalSpeed);

            this.addWidget(optionsList);
        }
    }

    private static class CollisionOptionsScreen extends OptionsListScreen {

        public CollisionOptionsScreen(Screen previous) {
            super(previous, Component.translatable("text.freecam.configScreen.option.collision"));
        }

        @Override
        protected void init() {
            super.init();

            OptionInstance<Boolean> ignoreTransparent = OptionInstance.createBoolean(
                    "text.freecam.configScreen.option.collision.ignoreTransparent",
                    (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.collision.ignoreTransparent.@Tooltip")),
                    FreecamConfig.IGNORE_TRANSPARENT_BLOCKS.get(),
                    (value) -> FreecamConfig.IGNORE_TRANSPARENT_BLOCKS.set(value)
            );
            this.optionsList.addBig(ignoreTransparent);

            OptionInstance<Boolean> ignoreOpenable = OptionInstance.createBoolean(
                    "text.freecam.configScreen.option.collision.ignoreOpenable",
                    (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.collision.ignoreOpenable.@Tooltip")),
                    FreecamConfig.IGNORE_OPENABLE_BLOCKS.get(),
                    (value) -> FreecamConfig.IGNORE_OPENABLE_BLOCKS.set(value)
            );
            this.optionsList.addBig(ignoreOpenable);

            OptionInstance<Boolean> ignoreAll = OptionInstance.createBoolean(
                    "text.freecam.configScreen.option.collision.ignoreAll",
                    (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.collision.ignoreAll.@Tooltip")),
                    FreecamConfig.IGNORE_ALL_COLLISION.get(),
                    (value) -> FreecamConfig.IGNORE_ALL_COLLISION.set(value)
            );
            this.optionsList.addBig(ignoreAll);

            OptionInstance<Boolean> alwaysCheck = OptionInstance.createBoolean(
                    "text.freecam.configScreen.option.collision.alwaysCheck",
                    (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.collision.alwaysCheck.@Tooltip")),
                    FreecamConfig.ALWAYS_CHECK_COLLISION.get(),
                    (value) -> FreecamConfig.ALWAYS_CHECK_COLLISION.set(value)
            );
            this.optionsList.addBig(alwaysCheck);

            this.addWidget(optionsList);
        }
    }

    private static class VisualOptionsScreen extends OptionsListScreen {

        public VisualOptionsScreen(Screen previous) {
            super(previous, Component.translatable("text.freecam.configScreen.option.visual"));
        }

        @Override
        protected void init() {
            super.init();

            OptionInstance<FreecamConfig.Perspective> perspective = new OptionInstance<FreecamConfig.Perspective>(
                    "text.freecam.configScreen.option.visual.perspective",
                    (value2) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.visual.perspective.@Tooltip")),
                    (unused, option) -> Component.translatable(option.getKey()),
                    new OptionInstance.Enum<>(Arrays.asList(FreecamConfig.Perspective.values()), Codec.INT.xmap(FreecamConfig.Perspective::byId, FreecamConfig.Perspective::getId)),
                    (FreecamConfig.Perspective) FreecamConfig.PERSPECTIVE.get(),
                    (newValue) -> FreecamConfig.PERSPECTIVE.set(newValue)
            );
            this.optionsList.addBig(perspective);

            OptionInstance<Boolean> showPlayer = OptionInstance.createBoolean(
                    "text.freecam.configScreen.option.visual.showPlayer",
                    (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.visual.showPlayer.@Tooltip")),
                    FreecamConfig.SHOW_PLAYER.get(),
                    (value) -> FreecamConfig.SHOW_PLAYER.set(value)
            );
            this.optionsList.addBig(showPlayer);

            OptionInstance<Boolean> showHand = OptionInstance.createBoolean(
                    "text.freecam.configScreen.option.visual.showHand",
                    (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.visual.showHand.@Tooltip")),
                    FreecamConfig.SHOW_HAND.get(),
                    (value) -> FreecamConfig.SHOW_HAND.set(value)
            );
            this.optionsList.addBig(showHand);

            OptionInstance<Boolean> fullBright = OptionInstance.createBoolean(
                    "text.freecam.configScreen.option.visual.fullBright",
                    (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.visual.fullBright.@Tooltip")),
                    FreecamConfig.FULL_BRIGHTNESS.get(),
                    (value) -> FreecamConfig.FULL_BRIGHTNESS.set(value)
            );
            this.optionsList.addBig(fullBright);

            OptionInstance<Boolean> showSubmersion = OptionInstance.createBoolean(
                    "text.freecam.configScreen.option.visual.showSubmersion",
                    (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.visual.showSubmersion.@Tooltip")),
                    FreecamConfig.SHOW_SUBMERSION_FOG.get(),
                    (value) -> FreecamConfig.SHOW_SUBMERSION_FOG.set(value)
            );
            this.optionsList.addBig(showSubmersion);

            this.addWidget(optionsList);
        }
    }

    private static class UtilityOptionsScreen extends OptionsListScreen {

        public UtilityOptionsScreen(Screen previous) {
            super(previous, Component.translatable("text.freecam.configScreen.option.utility"));
        }

        @Override
        protected void init() {
            super.init();

            OptionInstance<Boolean> disableOnDamage = OptionInstance.createBoolean(
                    "text.freecam.configScreen.option.utility.disableOnDamage",
                    (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.utility.disableOnDamage.@Tooltip")),
                    FreecamConfig.DISABLE_ON_DAMAGE.get(),
                    (value) -> FreecamConfig.DISABLE_ON_DAMAGE.set(value)
            );
            this.optionsList.addBig(disableOnDamage);

            OptionInstance<Boolean> freezePlayer = OptionInstance.createBoolean(
                    "text.freecam.configScreen.option.utility.freezePlayer",
                    (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.utility.freezePlayer.@Tooltip")),
                    FreecamConfig.FREEZE_PLAYER.get(),
                    (value) -> FreecamConfig.FREEZE_PLAYER.set(value)
            );
            this.optionsList.addBig(freezePlayer);

            OptionInstance<Boolean> allowInteract = OptionInstance.createBoolean(
                    "text.freecam.configScreen.option.utility.allowInteract",
                    (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.utility.allowInteract.@Tooltip")),
                    FreecamConfig.ALLOW_INTERACT.get(),
                    (value) -> FreecamConfig.ALLOW_INTERACT.set(value)
            );
            this.optionsList.addBig(allowInteract);

            OptionInstance<FreecamConfig.InteractionMode> interactionMode = new OptionInstance<FreecamConfig.InteractionMode>(
                    "text.freecam.configScreen.option.utility.interactionMode",
                    (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.utility.interactionMode.@Tooltip")),
                    (unused, option) -> Component.translatable(option.getKey()),
                    new OptionInstance.Enum<>(Arrays.asList(FreecamConfig.InteractionMode.values()), Codec.INT.xmap(FreecamConfig.InteractionMode::byId, FreecamConfig.InteractionMode::getId)),
                    (FreecamConfig.InteractionMode) FreecamConfig.INTERACTION_MODE.get(),
                    (newValue) -> FreecamConfig.INTERACTION_MODE.set(newValue)
            );
            this.optionsList.addBig(interactionMode);

            this.addWidget(optionsList);
        }
    }

    private static class NotificationOptionsScreen extends OptionsListScreen {

        public NotificationOptionsScreen(Screen previous) {
            super(previous, Component.translatable("text.freecam.configScreen.option.notification"));
        }

        @Override
        protected void init() {
            super.init();

            OptionInstance<Boolean> notifyFreecam = OptionInstance.createBoolean(
                    "text.freecam.configScreen.option.notification.notifyFreecam",
                    (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.notification.notifyFreecam.@Tooltip")),
                    FreecamConfig.NOTIFY_FREECAM.get(),
                    (value) -> FreecamConfig.NOTIFY_FREECAM.set(value)
            );
            this.optionsList.addBig(notifyFreecam);

            OptionInstance<Boolean> notifyTripod = OptionInstance.createBoolean(
                    "text.freecam.configScreen.option.notification.notifyTripod",
                    (value) -> Tooltip.create(Component.translatable("text.freecam.configScreen.option.notification.notifyTripod.@Tooltip")),
                    FreecamConfig.NOTIFY_TRIPOD.get(),
                    (value) -> FreecamConfig.NOTIFY_TRIPOD.set(value)
            );
            this.optionsList.addBig(notifyTripod);

            this.addWidget(optionsList);
        }
    }
}
