package net.xolt.freecam.config;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.OptionSlider;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.OptionButton;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.settings.BooleanOption;
import net.minecraft.client.settings.IteratableOption;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Optional;

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
        this(previous, new TranslationTextComponent("text.freecam.configScreen.title"));
    }

    public ConfigScreen(Screen previous, ITextComponent title) {
        super (title);
        this.previous = previous;
    }

    @Override
    protected void init() {
        int verticalOffset = OPTIONS_LIST_TOP_OFFSET + BUTTON_SPACING;

        this.addButton(new Button(
                (this.width - BUTTON_WIDTH) / 2,
                verticalOffset,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                new TranslationTextComponent("text.freecam.configScreen.option.movement"),
                button -> minecraft.setScreen(new MovementOptionsScreen(this)),
                new ButtonTooltip(this,  new TranslationTextComponent("text.freecam.configScreen.option.movement.@Tooltip"))
        ));

        verticalOffset += BUTTON_HEIGHT + BUTTON_SPACING;

        this.addButton(new Button(
                (this.width - BUTTON_WIDTH) / 2,
                verticalOffset,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                new TranslationTextComponent("text.freecam.configScreen.option.collision"),
                button -> minecraft.setScreen(new CollisionOptionsScreen(this)),
                new ButtonTooltip(this, new TranslationTextComponent("text.freecam.configScreen.option.collision.@Tooltip"))
        ));

        verticalOffset += BUTTON_HEIGHT + BUTTON_SPACING;

        this.addButton(new Button(
                (this.width - BUTTON_WIDTH) / 2,
                verticalOffset,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                new TranslationTextComponent("text.freecam.configScreen.option.visual"),
                button -> minecraft.setScreen(new VisualOptionsScreen(this)),
                new ButtonTooltip(this, new TranslationTextComponent("text.freecam.configScreen.option.visual.@Tooltip"))
        ));

        verticalOffset += BUTTON_HEIGHT + BUTTON_SPACING;

        this.addButton(new Button(
                (this.width - BUTTON_WIDTH) / 2,
                verticalOffset,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                new TranslationTextComponent("text.freecam.configScreen.option.utility"),
                button -> minecraft.setScreen(new UtilityOptionsScreen(this)),
                new ButtonTooltip(this, new TranslationTextComponent("text.freecam.configScreen.option.utility.@Tooltip"))
        ));

        verticalOffset += BUTTON_HEIGHT + BUTTON_SPACING;

        this.addButton(new Button(
                (this.width - BUTTON_WIDTH) / 2,
                verticalOffset,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                new TranslationTextComponent("text.freecam.configScreen.option.notification"),
                button -> minecraft.setScreen(new NotificationOptionsScreen(this)),
                new ButtonTooltip(this, new TranslationTextComponent("text.freecam.configScreen.option.notification.@Tooltip"))
        ));

        this.addButton(new Button(
                (this.width - DONE_BUTTON_WIDTH) / 2,
                this.height - BUTTON_HEIGHT - DONE_BUTTON_BOTTOM_OFFSET,
                DONE_BUTTON_WIDTH, BUTTON_HEIGHT,
                DialogTexts.GUI_DONE,
                button -> this.onClose()
        ));
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(previous);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        drawCenteredString(matrixStack, this.font, this.title.getString(),
                this.width / 2, TITLE_TOP_OFFSET, 0xFFFFFF);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    private static class OptionsListScreen extends Screen {

        protected final Screen previous;

        protected OptionsRowList optionsList;

        protected OptionsListScreen(Screen previous, ITextComponent title) {
            super(title);
            this.previous = previous;
        }

        @Override
        protected void init() {
            this.optionsList = new OptionsRowList(
                    this.minecraft, this.width, this.height,
                    OPTIONS_LIST_TOP_OFFSET,
                    this.height - OPTIONS_LIST_BOTTOM_OFFSET,
                    OPTIONS_LIST_ITEM_HEIGHT
            );

            this.addWidget(optionsList);

            this.addButton(new Button(
                    (this.width - DONE_BUTTON_WIDTH) / 2,
                    this.height - BUTTON_HEIGHT - DONE_BUTTON_BOTTOM_OFFSET,
                    DONE_BUTTON_WIDTH, BUTTON_HEIGHT,
                    DialogTexts.GUI_DONE,
                    button -> this.onClose()
            ));

            this.children.add(optionsList);
        }

        @Override
        public void onClose() {
            this.minecraft.setScreen(previous);
        }

        public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
            this.renderBackground(matrixStack);
            this.optionsList.render(matrixStack, mouseX, mouseY, partialTicks);
            Optional<Widget> hoveredButton = optionsList.getMouseOver(mouseX, mouseY);
            if (hoveredButton.isPresent()) {
                if (hoveredButton.get() instanceof OptionButton) {
                    renderToolTip(matrixStack, ((OptionButton) hoveredButton.get()).getTooltip().get(), mouseX, mouseY, null);
                } else if (hoveredButton.get() instanceof OptionSlider) {
                    renderToolTip(matrixStack, ((OptionSlider) hoveredButton.get()).getTooltip().get(), mouseX, mouseY, null);
                }
            }
            drawCenteredString(matrixStack, this.font, this.title.getString(),
                    this.width / 2, TITLE_TOP_OFFSET, 0xFFFFFF);
            super.render(matrixStack, mouseX, mouseY, partialTicks);
        }
    }

    private static class MovementOptionsScreen extends OptionsListScreen {

        public MovementOptionsScreen(Screen previous) {
            super(previous, new TranslationTextComponent("text.freecam.configScreen.option.movement"));
        }

        @Override
        protected void init() {
            super.init();

            IteratableOption flightMode = new IteratableOption(
                    "text.freecam.configScreen.option.movement.flightMode",
                    (unused, newValue) -> FreecamConfig.FLIGHT_MODE.set(FreecamConfig.FlightMode.values()[(((FreecamConfig.FlightMode) FreecamConfig.FLIGHT_MODE.get()).ordinal() + newValue) % FreecamConfig.FlightMode.values().length]),
                    (unused, option) -> new TranslationTextComponent("text.freecam.configScreen.option.movement.flightMode").append(": ").append(new TranslationTextComponent(((FreecamConfig.FlightMode) FreecamConfig.FLIGHT_MODE.get()).getKey()))
            );
            flightMode.setTooltip(MC.font.split(new TranslationTextComponent("text.freecam.configScreen.option.movement.flightMode.@Tooltip"), 200));
            this.optionsList.addBig(flightMode);

            SliderPercentageOption horizontalSpeed = new SliderPercentageOption(
                    "text.freecam.configScreen.option.movement.horizontalSpeed",
                    0.0, 10.0, 0.1F,
                    unused -> FreecamConfig.HORIZONTAL_SPEED.get(),
                    (unused, newValue) -> {
                        if (Math.abs((Math.round(newValue * 1000.0) / 1000.0) - FreecamConfig.HORIZONTAL_SPEED.get()) >= 0.095) {
                            FreecamConfig.HORIZONTAL_SPEED.set(Math.round(newValue * 10.0) / 10.0);
                        }
                    },
                    (gs, option) -> new TranslationTextComponent("text.freecam.configScreen.option.movement.horizontalSpeed").append(": " + option.get(gs))
            );
            horizontalSpeed.setTooltip(MC.font.split(new TranslationTextComponent("text.freecam.configScreen.option.movement.horizontalSpeed.@Tooltip"), 200));
            this.optionsList.addBig(horizontalSpeed);

            SliderPercentageOption verticalSpeed = new SliderPercentageOption(
                    "text.freecam.configScreen.option.movement.verticalSpeed",
                    0.0, 10.0, 0.1F,
                    unused -> FreecamConfig.VERTICAL_SPEED.get(),
                    (unused, newValue) -> {
                        if (Math.abs((Math.round(newValue * 1000.0) / 1000.0) - FreecamConfig.VERTICAL_SPEED.get()) >= 0.095) {
                            FreecamConfig.VERTICAL_SPEED.set(Math.round(newValue * 10.0) / 10.0);
                        }
                    },
                    (gs, option) -> new TranslationTextComponent("text.freecam.configScreen.option.movement.verticalSpeed").append(": " + option.get(gs))
            );
            verticalSpeed.setTooltip(MC.font.split(new TranslationTextComponent("text.freecam.configScreen.option.movement.verticalSpeed.@Tooltip"), 200));
            this.optionsList.addBig(verticalSpeed);

            this.addWidget(optionsList);
        }
    }

    private static class CollisionOptionsScreen extends OptionsListScreen {

        public CollisionOptionsScreen(Screen previous) {
            super(previous, new TranslationTextComponent("text.freecam.configScreen.option.collision"));
        }

        @Override
        protected void init() {
            super.init();

            BooleanOption ignoreTransparent = new BooleanOption(
                    "text.freecam.configScreen.option.collision.ignoreTransparent",
                    unused -> FreecamConfig.IGNORE_TRANSPARENT_BLOCKS.get(),
                    (unused, newValue) -> FreecamConfig.IGNORE_TRANSPARENT_BLOCKS.set(newValue)
            );
            ignoreTransparent.setTooltip(MC.font.split(new TranslationTextComponent("text.freecam.configScreen.option.collision.ignoreTransparent.@Tooltip"), 200));
            this.optionsList.addBig(ignoreTransparent);

            BooleanOption ignoreOpenable = new BooleanOption(
                    "text.freecam.configScreen.option.collision.ignoreOpenable",
                    unused -> FreecamConfig.IGNORE_OPENABLE_BLOCKS.get(),
                    (unused, newValue) -> FreecamConfig.IGNORE_OPENABLE_BLOCKS.set(newValue)
            );
            ignoreOpenable.setTooltip(MC.font.split(new TranslationTextComponent("text.freecam.configScreen.option.collision.ignoreOpenable.@Tooltip"), 200));
            this.optionsList.addBig(ignoreOpenable);

            BooleanOption ignoreAll = new BooleanOption(
                    "text.freecam.configScreen.option.collision.ignoreAll",
                    unused -> FreecamConfig.IGNORE_ALL_COLLISION.get(),
                    (unused, newValue) -> FreecamConfig.IGNORE_ALL_COLLISION.set(newValue)
            );
            ignoreAll.setTooltip(MC.font.split(new TranslationTextComponent("text.freecam.configScreen.option.collision.ignoreAll.@Tooltip"), 200));
            this.optionsList.addBig(ignoreAll);

            BooleanOption alwaysCheck = new BooleanOption(
                    "text.freecam.configScreen.option.collision.alwaysCheck",
                    unused -> FreecamConfig.ALWAYS_CHECK_COLLISION.get(),
                    (unused, newValue) -> FreecamConfig.ALWAYS_CHECK_COLLISION.set(newValue)
            );
            alwaysCheck.setTooltip(MC.font.split(new TranslationTextComponent("text.freecam.configScreen.option.collision.alwaysCheck.@Tooltip"), 200));
            this.optionsList.addBig(alwaysCheck);

            this.addWidget(optionsList);
        }
    }

    private static class VisualOptionsScreen extends OptionsListScreen {

        public VisualOptionsScreen(Screen previous) {
            super(previous, new TranslationTextComponent("text.freecam.configScreen.option.visual"));
        }

        @Override
        protected void init() {
            super.init();

            IteratableOption perspective = new IteratableOption(
                    "text.freecam.configScreen.option.visual.perspective",
                    (unused, newValue) -> FreecamConfig.PERSPECTIVE.set(FreecamConfig.Perspective.values()[(((FreecamConfig.Perspective) FreecamConfig.PERSPECTIVE.get()).ordinal() + newValue) % FreecamConfig.Perspective.values().length]),
                    (unused, option) -> new TranslationTextComponent("text.freecam.configScreen.option.visual.perspective").append(": ").append(new TranslationTextComponent(((FreecamConfig.Perspective) FreecamConfig.PERSPECTIVE.get()).getKey()))
            );
            perspective.setTooltip(MC.font.split(new TranslationTextComponent("text.freecam.configScreen.option.visual.perspective.@Tooltip"), 200));
            this.optionsList.addBig(perspective);

            BooleanOption showPlayer = new BooleanOption(
                    "text.freecam.configScreen.option.visual.showPlayer",
                    unused -> FreecamConfig.SHOW_PLAYER.get(),
                    (unused, newValue) -> FreecamConfig.SHOW_PLAYER.set(newValue)
            );
            showPlayer.setTooltip(MC.font.split(new TranslationTextComponent("text.freecam.configScreen.option.visual.showPlayer.@Tooltip"), 200));
            this.optionsList.addBig(showPlayer);

            BooleanOption showHand = new BooleanOption(
                    "text.freecam.configScreen.option.visual.showHand",
                    unused -> FreecamConfig.SHOW_HAND.get(),
                    (unused, newValue) -> FreecamConfig.SHOW_HAND.set(newValue)
            );
            showHand.setTooltip(MC.font.split(new TranslationTextComponent("text.freecam.configScreen.option.visual.showHand.@Tooltip"), 200));
            this.optionsList.addBig(showHand);

            BooleanOption fullBright = new BooleanOption(
                    "text.freecam.configScreen.option.visual.fullBright",
                    unused -> FreecamConfig.FULL_BRIGHTNESS.get(),
                    (unused, newValue) -> FreecamConfig.FULL_BRIGHTNESS.set(newValue)
            );
            fullBright.setTooltip(MC.font.split(new TranslationTextComponent("text.freecam.configScreen.option.visual.fullBright.@Tooltip"), 200));
            this.optionsList.addBig(fullBright);

            BooleanOption showSubmersion = new BooleanOption(
                    "text.freecam.configScreen.option.visual.showSubmersion",
                    unused -> FreecamConfig.SHOW_SUBMERSION_FOG.get(),
                    (unused, newValue) -> FreecamConfig.SHOW_SUBMERSION_FOG.set(newValue)
            );
            showSubmersion.setTooltip(MC.font.split(new TranslationTextComponent("text.freecam.configScreen.option.visual.showSubmersion.@Tooltip"), 200));
            this.optionsList.addBig(showSubmersion);

            this.addWidget(optionsList);
        }
    }

    private static class UtilityOptionsScreen extends OptionsListScreen {

        public UtilityOptionsScreen(Screen previous) {
            super(previous, new TranslationTextComponent("text.freecam.configScreen.option.utility"));
        }

        @Override
        protected void init() {
            super.init();

            BooleanOption disableOnDamage = new BooleanOption(
                    "text.freecam.configScreen.option.utility.disableOnDamage",
                    unused -> FreecamConfig.DISABLE_ON_DAMAGE.get(),
                    (unused, newValue) -> FreecamConfig.DISABLE_ON_DAMAGE.set(newValue)
            );
            disableOnDamage.setTooltip(MC.font.split(new TranslationTextComponent("text.freecam.configScreen.option.utility.disableOnDamage.@Tooltip"), 200));
            this.optionsList.addBig(disableOnDamage);

            BooleanOption freezePlayer = new BooleanOption(
                    "text.freecam.configScreen.option.utility.freezePlayer",
                    unused -> FreecamConfig.FREEZE_PLAYER.get(),
                    (unused, newValue) -> FreecamConfig.FREEZE_PLAYER.set(newValue)
            );
            freezePlayer.setTooltip(MC.font.split(new TranslationTextComponent("text.freecam.configScreen.option.utility.freezePlayer.@Tooltip"), 200));
            this.optionsList.addBig(freezePlayer);

            BooleanOption allowInteract = new BooleanOption(
                    "text.freecam.configScreen.option.utility.allowInteract",
                    unused -> FreecamConfig.ALLOW_INTERACT.get(),
                    (unused, newValue) -> FreecamConfig.ALLOW_INTERACT.set(newValue)
            );
            allowInteract.setTooltip(MC.font.split(new TranslationTextComponent("text.freecam.configScreen.option.utility.allowInteract.@Tooltip"), 200));
            this.optionsList.addBig(allowInteract);

            IteratableOption interactionMode = new IteratableOption(
                    "text.freecam.configScreen.option.utility.interactionMode",
                    (unused, newValue) -> FreecamConfig.INTERACTION_MODE.set(FreecamConfig.InteractionMode.values()[(((FreecamConfig.InteractionMode) FreecamConfig.INTERACTION_MODE.get()).ordinal() + newValue) % FreecamConfig.InteractionMode.values().length]),
                    (unused, option) -> new TranslationTextComponent("text.freecam.configScreen.option.utility.interactionMode").append(": ").append(new TranslationTextComponent(((FreecamConfig.InteractionMode) FreecamConfig.INTERACTION_MODE.get()).getKey()))
            );
            interactionMode.setTooltip(MC.font.split(new TranslationTextComponent("text.freecam.configScreen.option.utility.interactionMode.@Tooltip"), 200));
            this.optionsList.addBig(interactionMode);

            this.addWidget(optionsList);
        }
    }

    private static class NotificationOptionsScreen extends OptionsListScreen {

        public NotificationOptionsScreen(Screen previous) {
            super(previous, new TranslationTextComponent("text.freecam.configScreen.option.notification"));
        }

        @Override
        protected void init() {
            super.init();

            BooleanOption notifyFreecam = new BooleanOption(
                    "text.freecam.configScreen.option.notification.notifyFreecam",
                    unused -> FreecamConfig.NOTIFY_FREECAM.get(),
                    (unused, newValue) -> FreecamConfig.NOTIFY_FREECAM.set(newValue)
            );
            notifyFreecam.setTooltip(MC.font.split(new TranslationTextComponent("text.freecam.configScreen.option.notification.notifyFreecam.@Tooltip"), 200));
            this.optionsList.addBig(notifyFreecam);

            BooleanOption notifyTripod = new BooleanOption(
                    "text.freecam.configScreen.option.notification.notifyTripod",
                    unused -> FreecamConfig.NOTIFY_TRIPOD.get(),
                    (unused, newValue) -> FreecamConfig.NOTIFY_TRIPOD.set(newValue)
            );
            notifyTripod.setTooltip(MC.font.split(new TranslationTextComponent("text.freecam.configScreen.option.notification.notifyTripod.@Tooltip"), 200));
            this.optionsList.addBig(notifyTripod);

            this.addWidget(optionsList);
        }
    }

    private static class ButtonTooltip implements Button.ITooltip {

        private Screen screen;
        private ITextComponent tooltip;

        public ButtonTooltip(Screen screen, ITextComponent tooltip) {
            this.screen = screen;
            this.tooltip = tooltip;
        }

        @Override public void onTooltip(Button pButton, MatrixStack pPoseStack, int pMouseX, int pMouseY) {
            screen.renderTooltip(pPoseStack, tooltip, pMouseX, pMouseY);
        }
    }
}
