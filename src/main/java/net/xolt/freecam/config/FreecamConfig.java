package net.xolt.freecam.config;

import net.minecraft.util.Mth;
import net.minecraft.util.OptionEnum;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.Comparator;

public class FreecamConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // Movement Options

    public static final ForgeConfigSpec.EnumValue<FlightMode> FLIGHT_MODE;
    public static final ForgeConfigSpec.DoubleValue HORIZONTAL_SPEED;
    public static final ForgeConfigSpec.DoubleValue VERTICAL_SPEED;

    // Collision Options

    public static final ForgeConfigSpec.BooleanValue IGNORE_TRANSPARENT_BLOCKS;
    public static final ForgeConfigSpec.BooleanValue IGNORE_OPENABLE_BLOCKS;
    public static final ForgeConfigSpec.BooleanValue IGNORE_ALL_COLLISION;
    public static final ForgeConfigSpec.BooleanValue ALWAYS_CHECK_COLLISION;

    // Visual Options

    public static final ForgeConfigSpec.EnumValue<Perspective> PERSPECTIVE;
    public static final ForgeConfigSpec.BooleanValue SHOW_PLAYER;
    public static final ForgeConfigSpec.BooleanValue SHOW_HAND;
    public static final ForgeConfigSpec.BooleanValue FULL_BRIGHTNESS;
    public static final ForgeConfigSpec.BooleanValue SHOW_SUBMERSION_FOG;

    // Utility Options

    public static final ForgeConfigSpec.BooleanValue DISABLE_ON_DAMAGE;
    public static final ForgeConfigSpec.BooleanValue FREEZE_PLAYER;
    public static final ForgeConfigSpec.BooleanValue ALLOW_INTERACT;
    public static final ForgeConfigSpec.EnumValue<InteractionMode> INTERACTION_MODE;

    // Notification Options

    public static final ForgeConfigSpec.BooleanValue NOTIFY_FREECAM;
    public static final ForgeConfigSpec.BooleanValue NOTIFY_TRIPOD;

    static {
        BUILDER.push("Freecam");

        // Movement Options

        FLIGHT_MODE = BUILDER.comment("The type of flight used by freecam.")
                .defineEnum("Flight Mode", FlightMode.DEFAULT);

        HORIZONTAL_SPEED = BUILDER.comment("The horizontal speed of freecam.")
                .defineInRange("Horizontal Speed", 1.0, 0.0, 10.0);

        VERTICAL_SPEED = BUILDER.comment("The vertical speed of freecam.")
                .defineInRange("Vertical Speed", 1.0, 0.0, 10.0);

        // Collision Options

        IGNORE_TRANSPARENT_BLOCKS = BUILDER.comment("Allows travelling through transparent blocks in freecam.")
                .define("Ignore Transparent Blocks", true);

        IGNORE_OPENABLE_BLOCKS = BUILDER.comment("Allows travelling through doors/trapdoors/gates in freecam.")
                .define("Ignore Openable Blocks", true);

        IGNORE_ALL_COLLISION = BUILDER.comment("Allows travelling through all blocks in freecam.")
                .define("Ignore All Collision", false);

        ALWAYS_CHECK_COLLISION = BUILDER.comment("Whether 'Initial Perspective' should check for collision, even when using 'Ignore All Collision'.")
                .define("Always Check Initial Collision", false);

        // Visual Options

        PERSPECTIVE = BUILDER.comment("The initial perspective of the camera.")
                .defineEnum("Initial Perspective", Perspective.INSIDE);

        SHOW_PLAYER = BUILDER.comment("Shows your player in its original position.")
                .define("Show Player", true);

        SHOW_HAND = BUILDER.comment("Whether you can see your hand in freecam.")
                .define("Show Hand", false);

        FULL_BRIGHTNESS = BUILDER.comment("Increases brightness while in freecam.")
                .define("Full Brightness", false);

        SHOW_SUBMERSION_FOG = BUILDER.comment("Whether you see a fog overlay underwater, in lava, or powdered snow.")
                .define("Show Submersion Fog", false);

        // Utility Options

        DISABLE_ON_DAMAGE = BUILDER.comment("Disables freecam when damage is received.")
                .define("Disable on Damage", true);

        FREEZE_PLAYER = BUILDER.comment("Prevents player movement while freecam is active.\nWARNING: Multiplayer usage not advised.")
                .define("Freeze Player", false);

        ALLOW_INTERACT = BUILDER.comment("Whether you can interact with blocks/entities in freecam.\nWARNING: Multiplayer usage not advised.")
                .define("Allow Interaction", false);

        INTERACTION_MODE = BUILDER.comment("The source of block/entity interactions.")
                .defineEnum("Interaction Mode", InteractionMode.CAMERA);

        // Notification Options

        NOTIFY_FREECAM = BUILDER.comment("Notifies you when entering/exiting freecam.")
                .define("Freecam Notifications", true);

        NOTIFY_TRIPOD = BUILDER.comment("Notifies you when entering/exiting tripod cameras.")
                .define("Tripod Notifications", true);


        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public enum FlightMode implements OptionEnum {
        CREATIVE(0, "text.freecam.configScreen.option.movement.flightMode.creative"),
        DEFAULT(1, "text.freecam.configScreen.option.movement.flightMode.default");

        private static final FlightMode[] BY_ID = Arrays.stream(values()).sorted(Comparator.comparingInt(FlightMode::getId)).toArray((size) -> new FlightMode[size]);
        private final int id;
        private final String key;

        FlightMode(int id, String key) {
            this.id = id;
            this.key = key;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public String getKey() {
            return key;
        }

        public static FlightMode byId(int pId) {
            return BY_ID[Mth.positiveModulo(pId, BY_ID.length)];
        }
    }

    public enum InteractionMode implements OptionEnum {
        CAMERA(0, "text.freecam.configScreen.option.utility.interactionMode.camera"),
        PLAYER(1, "text.freecam.configScreen.option.utility.interactionMode.player");

        private static final InteractionMode[] BY_ID = Arrays.stream(values()).sorted(Comparator.comparingInt(InteractionMode::getId)).toArray((size) -> new InteractionMode[size]);
        private final int id;
        private final String key;

        InteractionMode(int id, String name) {
            this.id = id;
            this.key = name;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public String getKey() {
            return key;
        }

        public static InteractionMode byId(int pId) {
            return BY_ID[Mth.positiveModulo(pId, BY_ID.length)];
        }
    }

    public enum Perspective implements OptionEnum {
        FIRST_PERSON(0, "text.freecam.configScreen.option.visual.perspective.firstPerson"),
        THIRD_PERSON(1, "text.freecam.configScreen.option.visual.perspective.thirdPerson"),
        THIRD_PERSON_MIRROR(2, "text.freecam.configScreen.option.visual.perspective.thirdPersonMirror"),
        INSIDE(3, "text.freecam.configScreen.option.visual.perspective.inside");

        private static final Perspective[] BY_ID = Arrays.stream(values()).sorted(Comparator.comparingInt(Perspective::getId)).toArray(Perspective[]::new);
        private final int id;
        private final String key;

        Perspective(int id, String name) {
            this.id = id;
            this.key = name;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public String getKey() {
            return key;
        }

        public static Perspective byId(int pId) {
            return BY_ID[Mth.positiveModulo(pId, BY_ID.length)];
        }
    }
}
