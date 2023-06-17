package net.xolt.freecam.config;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.CycleOption;
import net.minecraft.client.ProgressOption;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;
import java.util.function.Consumer;

import static net.minecraft.client.gui.screens.OptionsSubScreen.tooltipAt;
import static net.xolt.freecam.Freecam.MC;

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
        this(previous, new TranslatableComponent("text.freecam.configScreen.title"));
    }

    public ConfigScreen(Screen previous, Component title) {
        super(title);
        this.previous = previous;
    }

    @Override
    protected void init() {
        int verticalOffset = OPTIONS_LIST_TOP_OFFSET + BUTTON_SPACING;

        this.addRenderableWidget(new Button(
                (this.width - BUTTON_WIDTH) / 2,
                verticalOffset,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                new TranslatableComponent("text.freecam.configScreen.option.movement"),
                button -> minecraft.setScreen(new MovementOptionsScreen(this)),
                new ButtonTooltip(this, new TranslatableComponent("text.freecam.configScreen.option.movement.@Tooltip"))
        ));

        verticalOffset += BUTTON_HEIGHT + BUTTON_SPACING;

        this.addRenderableWidget(new Button(
                (this.width - BUTTON_WIDTH) / 2,
                verticalOffset,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                new TranslatableComponent("text.freecam.configScreen.option.collision"),
                button -> minecraft.setScreen(new CollisionOptionsScreen(this)),
                new ButtonTooltip(this, new TranslatableComponent("text.freecam.configScreen.option.collision.@Tooltip"))
        ));

        verticalOffset += BUTTON_HEIGHT + BUTTON_SPACING;

        this.addRenderableWidget(new Button(
                (this.width - BUTTON_WIDTH) / 2,
                verticalOffset,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                new TranslatableComponent("text.freecam.configScreen.option.visual"),
                button -> minecraft.setScreen(new VisualOptionsScreen(this)),
                new ButtonTooltip(this, new TranslatableComponent("text.freecam.configScreen.option.visual.@Tooltip"))
        ));

        verticalOffset += BUTTON_HEIGHT + BUTTON_SPACING;

        this.addRenderableWidget(new Button(
                (this.width - BUTTON_WIDTH) / 2,
                verticalOffset,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                new TranslatableComponent("text.freecam.configScreen.option.utility"),
                button -> minecraft.setScreen(new UtilityOptionsScreen(this)),
                new ButtonTooltip(this, new TranslatableComponent("text.freecam.configScreen.option.utility.@Tooltip"))
        ));

        verticalOffset += BUTTON_HEIGHT + BUTTON_SPACING;

        this.addRenderableWidget(new Button(
                (this.width - BUTTON_WIDTH) / 2,
                verticalOffset,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                new TranslatableComponent("text.freecam.configScreen.option.notification"),
                button -> minecraft.setScreen(new NotificationOptionsScreen(this)),
                new ButtonTooltip(this, new TranslatableComponent("text.freecam.configScreen.option.notification.@Tooltip"))
        ));

        this.addRenderableWidget(new Button(
                (this.width - DONE_BUTTON_WIDTH) / 2,
                this.height - BUTTON_HEIGHT - DONE_BUTTON_BOTTOM_OFFSET,
                DONE_BUTTON_WIDTH, BUTTON_HEIGHT,
                CommonComponents.GUI_DONE,
                button -> this.onClose()
        ));
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

            this.addRenderableWidget(new Button(
                    (this.width - DONE_BUTTON_WIDTH) / 2,
                    this.height - BUTTON_HEIGHT - DONE_BUTTON_BOTTOM_OFFSET,
                    DONE_BUTTON_WIDTH, BUTTON_HEIGHT,
                    CommonComponents.GUI_DONE,
                    button -> this.onClose()
            ));
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
            List<FormattedCharSequence> list = tooltipAt(this.optionsList, pMouseX, pMouseY);
            this.renderTooltip(pPoseStack, list, pMouseX, pMouseY);
        }
    }

    private static class MovementOptionsScreen extends OptionsListScreen {

        public MovementOptionsScreen(Screen previous) {
            super(previous, new TranslatableComponent("text.freecam.configScreen.option.movement"));
        }

        @Override
        protected void init() {
            super.init();

            CycleOption<FreecamConfig.FlightMode> flightMode = CycleOption.create(
                    "text.freecam.configScreen.option.movement.flightMode",
                    FreecamConfig.FlightMode.values(),
                    (option) -> new TranslatableComponent(option.getKey()),
                    (option) -> (FreecamConfig.FlightMode) FreecamConfig.FLIGHT_MODE.get(),
                    (pOptions, pOption, pValue) -> FreecamConfig.FLIGHT_MODE.set(pValue)
            );
            flightMode.setTooltip((mc) -> (value) -> MC.font.split(new TranslatableComponent("text.freecam.configScreen.option.movement.flightMode.@Tooltip"), 200));
            this.optionsList.addBig(flightMode);

            ProgressOption horizontalSpeed = new ProgressOption(
                    "text.freecam.configScreen.option.movement.horizontalSpeed",
                    0.0,
                    10.0,
                    0.1F,
                    (option) -> FreecamConfig.HORIZONTAL_SPEED.get(),
                    (option, value) -> {
                        if (Math.abs((Math.round(value * 1000.0) / 1000.0) - FreecamConfig.HORIZONTAL_SPEED.get()) >= 0.095) {
                            FreecamConfig.HORIZONTAL_SPEED.set(Math.round(value * 10.0) / 10.0);
                        }
                    },
                    (option, pOption) -> new TranslatableComponent("text.freecam.configScreen.option.movement.horizontalSpeed").append(": " + FreecamConfig.HORIZONTAL_SPEED.get()),
                    (mc) -> MC.font.split(new TranslatableComponent("text.freecam.configScreen.option.movement.horizontalSpeed.@Tooltip"), 200)
            );
            this.optionsList.addBig(horizontalSpeed);

            ProgressOption verticalSpeed = new ProgressOption(
                    "text.freecam.configScreen.option.movement.verticalSpeed",
                    0.0,
                    10.0,
                    0.1F,
                    (option) -> FreecamConfig.VERTICAL_SPEED.get(),
                    (option, value) -> {
                        if (Math.abs((Math.round(value * 1000.0) / 1000.0) - FreecamConfig.VERTICAL_SPEED.get()) >= 0.095) {
                            FreecamConfig.VERTICAL_SPEED.set(Math.round(value * 10.0) / 10.0);
                        }
                    },
                    (option, pOption) -> new TranslatableComponent("text.freecam.configScreen.option.movement.verticalSpeed").append(": " + FreecamConfig.VERTICAL_SPEED.get()),
                    (mc) -> MC.font.split(new TranslatableComponent("text.freecam.configScreen.option.movement.verticalSpeed.@Tooltip"), 200)
            );
            this.optionsList.addBig(verticalSpeed);

            this.addWidget(optionsList);
        }
    }

    private static class CollisionOptionsScreen extends OptionsListScreen {

        public CollisionOptionsScreen(Screen previous) {
            super(previous, new TranslatableComponent("text.freecam.configScreen.option.collision"));
        }

        @Override
        protected void init() {
            super.init();

            CycleOption<Boolean> ignoreTransparent = CycleOption.createOnOff(
                    "text.freecam.configScreen.option.collision.ignoreTransparent",
                    (option) -> FreecamConfig.IGNORE_TRANSPARENT_BLOCKS.get(),
                    (pOptions, pOption, pValue) -> FreecamConfig.IGNORE_TRANSPARENT_BLOCKS.set(pValue)
            );
            ignoreTransparent.setTooltip((mc) -> (value) -> MC.font.split(new TranslatableComponent("text.freecam.configScreen.option.collision.ignoreTransparent.@Tooltip"), 200));
            this.optionsList.addBig(ignoreTransparent);

            CycleOption<Boolean> ignoreOpenable = CycleOption.createOnOff(
                    "text.freecam.configScreen.option.collision.ignoreOpenable",
                    (option) -> FreecamConfig.IGNORE_OPENABLE_BLOCKS.get(),
                    (pOptions, pOption, pValue) -> FreecamConfig.IGNORE_OPENABLE_BLOCKS.set(pValue)
            );
            ignoreOpenable.setTooltip((mc) -> (value) -> MC.font.split(new TranslatableComponent("text.freecam.configScreen.option.collision.ignoreOpenable.@Tooltip"), 200));
            this.optionsList.addBig(ignoreOpenable);

            CycleOption<Boolean> ignoreAll = CycleOption.createOnOff(
                    "text.freecam.configScreen.option.collision.ignoreAll",
                    (option) -> FreecamConfig.IGNORE_ALL_COLLISION.get(),
                    (pOptions, pOption, pValue) -> FreecamConfig.IGNORE_ALL_COLLISION.set(pValue)
            );
            ignoreAll.setTooltip((mc) -> (value) -> MC.font.split(new TranslatableComponent("text.freecam.configScreen.option.collision.ignoreAll.@Tooltip"), 200));
            this.optionsList.addBig(ignoreAll);

            CycleOption<Boolean> alwaysCheck = CycleOption.createOnOff(
                    "text.freecam.configScreen.option.collision.alwaysCheck",
                    (option) -> FreecamConfig.ALWAYS_CHECK_COLLISION.get(),
                    (pOptions, pOption, pValue) -> FreecamConfig.ALWAYS_CHECK_COLLISION.set(pValue)
            );
            alwaysCheck.setTooltip((mc) -> (value) -> MC.font.split(new TranslatableComponent("text.freecam.configScreen.option.collision.alwaysCheck.@Tooltip"), 200));
            this.optionsList.addBig(alwaysCheck);

            this.addWidget(optionsList);
        }
    }

    private static class VisualOptionsScreen extends OptionsListScreen {

        public VisualOptionsScreen(Screen previous) {
            super(previous, new TranslatableComponent("text.freecam.configScreen.option.visual"));
        }

        @Override
        protected void init() {
            super.init();

            CycleOption<FreecamConfig.Perspective> perspective = CycleOption.create(
                    "text.freecam.configScreen.option.visual.perspective",
                    FreecamConfig.Perspective.values(),
                    (option) -> new TranslatableComponent(option.getKey()),
                    (option) -> (FreecamConfig.Perspective) FreecamConfig.PERSPECTIVE.get(),
                    (pOptions, pOption, pValue) -> FreecamConfig.PERSPECTIVE.set(pValue)
            );
            perspective.setTooltip((mc) -> (value) -> MC.font.split(new TranslatableComponent("text.freecam.configScreen.option.visual.perspective.@Tooltip"), 200));
            this.optionsList.addBig(perspective);

            CycleOption<Boolean> showPlayer = CycleOption.createOnOff(
                    "text.freecam.configScreen.option.visual.showPlayer",
                    (option) -> FreecamConfig.SHOW_PLAYER.get(),
                    (pOptions, pOption, pValue) -> FreecamConfig.SHOW_PLAYER.set(pValue)
            );
            showPlayer.setTooltip((mc) -> (value) -> MC.font.split(new TranslatableComponent("text.freecam.configScreen.option.visual.showPlayer.@Tooltip"), 200));
            this.optionsList.addBig(showPlayer);

            CycleOption<Boolean> showHand = CycleOption.createOnOff(
                    "text.freecam.configScreen.option.visual.showHand",
                    (option) -> FreecamConfig.SHOW_HAND.get(),
                    (pOptions, pOption, pValue) -> FreecamConfig.SHOW_HAND.set(pValue)
            );
            showHand.setTooltip((mc) -> (value) -> MC.font.split(new TranslatableComponent("text.freecam.configScreen.option.visual.showHand.@Tooltip"), 200));
            this.optionsList.addBig(showHand);

            CycleOption<Boolean> showSubmersion = CycleOption.createOnOff(
                    "text.freecam.configScreen.option.visual.showSubmersion",
                    (option) -> FreecamConfig.SHOW_SUBMERSION_FOG.get(),
                    (pOptions, pOption, pValue) -> FreecamConfig.SHOW_SUBMERSION_FOG.set(pValue)
            );
            showSubmersion.setTooltip((mc) -> (value) -> MC.font.split(new TranslatableComponent("text.freecam.configScreen.option.visual.showSubmersion.@Tooltip"), 200));
            this.optionsList.addBig(showSubmersion);

            this.addWidget(optionsList);
        }
    }

    private static class UtilityOptionsScreen extends OptionsListScreen {

        public UtilityOptionsScreen(Screen previous) {
            super(previous, new TranslatableComponent("text.freecam.configScreen.option.utility"));
        }

        @Override
        protected void init() {
            super.init();

            CycleOption<Boolean> disableOnDamage = CycleOption.createOnOff(
                    "text.freecam.configScreen.option.utility.disableOnDamage",
                    (option) -> FreecamConfig.DISABLE_ON_DAMAGE.get(),
                    (pOptions, pOption, pValue) -> FreecamConfig.DISABLE_ON_DAMAGE.set(pValue)
            );
            disableOnDamage.setTooltip((mc) -> (value) -> MC.font.split(new TranslatableComponent("text.freecam.configScreen.option.utility.disableOnDamage.@Tooltip"), 200));
            this.optionsList.addBig(disableOnDamage);

            CycleOption<Boolean> freezePlayer = CycleOption.createOnOff(
                    "text.freecam.configScreen.option.utility.freezePlayer",
                    (option) -> FreecamConfig.FREEZE_PLAYER.get(),
                    (pOptions, pOption, pValue) -> FreecamConfig.FREEZE_PLAYER.set(pValue)
            );
            freezePlayer.setTooltip((mc) -> (value) -> MC.font.split(new TranslatableComponent("text.freecam.configScreen.option.utility.freezePlayer.@Tooltip"), 200));
            this.optionsList.addBig(freezePlayer);

            CycleOption<Boolean> allowInteract = CycleOption.createOnOff(
                    "text.freecam.configScreen.option.utility.allowInteract",
                    (option) -> FreecamConfig.ALLOW_INTERACT.get(),
                    (pOptions, pOption, pValue) -> FreecamConfig.ALLOW_INTERACT.set(pValue)
            );
            allowInteract.setTooltip((mc) -> (value) -> MC.font.split(new TranslatableComponent("text.freecam.configScreen.option.utility.allowInteract.@Tooltip"), 200));
            this.optionsList.addBig(allowInteract);

            CycleOption<FreecamConfig.InteractionMode> interactionMode = CycleOption.create(
                    "text.freecam.configScreen.option.utility.interactionMode",
                    FreecamConfig.InteractionMode.values(),
                    (option) -> new TranslatableComponent(option.getKey()),
                    (option) -> (FreecamConfig.InteractionMode) FreecamConfig.INTERACTION_MODE.get(),
                    (pOptions, pOption, pValue) -> FreecamConfig.INTERACTION_MODE.set(pValue)
            );
            interactionMode.setTooltip((mc) -> (value) -> MC.font.split(new TranslatableComponent("text.freecam.configScreen.option.utility.interactionMode.@Tooltip"), 200));
            this.optionsList.addBig(interactionMode);

            this.addWidget(optionsList);
        }
    }

    private static class NotificationOptionsScreen extends OptionsListScreen {

        public NotificationOptionsScreen(Screen previous) {
            super(previous, new TranslatableComponent("text.freecam.configScreen.option.notification"));
        }

        @Override
        protected void init() {
            super.init();

            CycleOption<Boolean> notifyFreecam = CycleOption.createOnOff(
                    "text.freecam.configScreen.option.notification.notifyFreecam",
                    (option) -> FreecamConfig.NOTIFY_FREECAM.get(),
                    (pOptions, pOption, pValue) -> FreecamConfig.NOTIFY_FREECAM.set(pValue)
            );
            notifyFreecam.setTooltip((mc) -> (value) -> MC.font.split(new TranslatableComponent("text.freecam.configScreen.option.notification.notifyFreecam.@Tooltip"), 200));
            this.optionsList.addBig(notifyFreecam);

            CycleOption<Boolean> notifyTripod = CycleOption.createOnOff(
                    "text.freecam.configScreen.option.notification.notifyTripod",
                    (option) -> FreecamConfig.NOTIFY_TRIPOD.get(),
                    (pOptions, pOption, pValue) -> FreecamConfig.NOTIFY_TRIPOD.set(pValue)
            );
            notifyTripod.setTooltip((mc) -> (value) -> MC.font.split(new TranslatableComponent("text.freecam.configScreen.option.notification.notifyTripod.@Tooltip"), 200));
            this.optionsList.addBig(notifyTripod);

            this.addWidget(optionsList);
        }
    }

    private static class ButtonTooltip implements Button.OnTooltip {

        private Screen screen;
        private Component tooltip;

        public ButtonTooltip(Screen screen, Component tooltip) {
            this.screen = screen;
            this.tooltip = tooltip;
        }

        @Override public void onTooltip(Button pButton, PoseStack pPoseStack, int pMouseX, int pMouseY) {
           screen.renderTooltip(pPoseStack, tooltip, pMouseX, pMouseY);
        }

        @Override public void narrateTooltip(Consumer<Component> pContents) {
            pContents.accept(tooltip);
        }
    }
}
