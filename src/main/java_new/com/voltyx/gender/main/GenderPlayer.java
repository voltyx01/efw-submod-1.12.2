package com.voltyx.gender.main;

import com.google.gson.JsonObject;
import com.voltyx.gender.main.config.ConfigKey;
import com.voltyx.gender.main.config.ClientConfiguration;
import com.voltyx.gender.physics.BreastPhysics;

import java.util.UUID;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class GenderPlayer {
	private boolean blinkEnabled = false;
	private int eyeSize = 1;
	private int eyeDistance = 1;
	private int eyeHeight = 4;
	private int eyelidSize = 1;
	private boolean dualEyelid = false;
	private float blinkFrequency = 1.15f;
	private int eyelidOffsetX = 0;
	private int eyelidOffsetY = -2;

	public int getEyelidOffsetX() {
		return eyelidOffsetX;
	}

	public boolean updateEyelidOffsetX(int value) {
		this.eyelidOffsetX = value;
		return true;
	}

	public int getEyelidOffsetY() {
		return eyelidOffsetY;
	}

	public boolean updateEyelidOffsetY(int value) {
		this.eyelidOffsetY = value;
		return true;
	}

	public int getEyelidSize() {
		return eyelidSize;
	}

	public boolean updateEyelidSize(int value) {
		this.eyelidSize = value;
		return true;
	}

	public boolean isDualEyelid() {
		return dualEyelid;
	}

	public boolean updateDualEyelid(boolean value) {
		this.dualEyelid = value;
		return true;
	}

	public float getBlinkFrequency() {
		return blinkFrequency;
	}

	public boolean updateBlinkFrequency(float value) {
		if (value >= 0.80f && value <= 1.35f) {
			this.blinkFrequency = value;
			return true;
		}
		return false;
	}

	public boolean isBlinkEnabled() {
		return this.blinkEnabled;
	}

	public boolean updateBlinking(boolean value) {
		this.blinkEnabled = value;
		return true;
	}

	public int getEyeSize() {
		return this.eyeSize;
	}

	public boolean updateEyeSize(int value) {
		this.eyeSize = value;
		return true;
	}

	public int getEyeDistance() {
		return this.eyeDistance;
	}

	public boolean updateEyeDistance(int value) {
		this.eyeDistance = value;
		return true;
	}

	public int getEyeHeight() {
		return this.eyeHeight;
	}

	public boolean updateEyeHeight(int value) {
		this.eyeHeight = value;
		return true;
	}

	public boolean needsSync;
	public final UUID uuid;
	private Gender gender;
	private float pBustSize = ClientConfiguration.BUST_SIZE.getDefault();

	private boolean hurtSounds = ClientConfiguration.HURT_SOUNDS.getDefault();

	private boolean breastPhysics = ClientConfiguration.BREAST_PHYSICS.getDefault();
	private boolean armorBreastPhysics = ClientConfiguration.BREAST_PHYSICS_ARMOR.getDefault();
	private float bounceMultiplier = ClientConfiguration.BOUNCE_MULTIPLIER.getDefault();
	private float floppyMultiplier = ClientConfiguration.FLOPPY_MULTIPLIER.getDefault();

	public boolean lockSettings = false;

	public SyncStatus syncStatus = SyncStatus.UNKNOWN;
	private boolean showBreastsInArmor = ClientConfiguration.SHOW_IN_ARMOR.getDefault();

	private final ClientConfiguration cfg;
	private final BreastPhysics lBreastPhysics, rBreastPhysics;
	private final Breasts breasts;

	public GenderPlayer(UUID uuid) {
		this(uuid, ClientConfiguration.GENDER.getDefault());
	}

	public GenderPlayer(UUID uuid, Gender gender) {
		lBreastPhysics = new BreastPhysics(this);
		rBreastPhysics = new BreastPhysics(this);
		breasts = new Breasts();
		this.uuid = uuid;
		this.gender = gender;
		this.cfg = new ClientConfiguration("WildfireGender", this.uuid.toString());
		this.cfg.set(ClientConfiguration.USERNAME, this.uuid);
		this.cfg.setDefault(ClientConfiguration.GENDER);
		this.cfg.setDefault(ClientConfiguration.BUST_SIZE);
		this.cfg.setDefault(ClientConfiguration.HURT_SOUNDS);

		this.cfg.setDefault(ClientConfiguration.BREASTS_OFFSET_X);
		this.cfg.setDefault(ClientConfiguration.BREASTS_OFFSET_Y);
		this.cfg.setDefault(ClientConfiguration.BREASTS_OFFSET_Z);
		this.cfg.setDefault(ClientConfiguration.BREASTS_UNIBOOB);
		this.cfg.setDefault(ClientConfiguration.BREASTS_CLEAVAGE);

		this.cfg.setDefault(ClientConfiguration.BREAST_PHYSICS);
		this.cfg.setDefault(ClientConfiguration.BREAST_PHYSICS_ARMOR);
		this.cfg.setDefault(ClientConfiguration.SHOW_IN_ARMOR);
		this.cfg.setDefault(ClientConfiguration.BOUNCE_MULTIPLIER);
		this.cfg.setDefault(ClientConfiguration.FLOPPY_MULTIPLIER);
		
		this.cfg.setDefault(ClientConfiguration.BLINK_ENABLED);
		this.cfg.setDefault(ClientConfiguration.EYE_SIZE);
		this.cfg.setDefault(ClientConfiguration.EYE_DISTANCE);
		this.cfg.setDefault(ClientConfiguration.EYE_HEIGHT);
		this.cfg.setDefault(ClientConfiguration.EYELID_SIZE);
		this.cfg.setDefault(ClientConfiguration.DUAL_EYELID);
		this.cfg.setDefault(ClientConfiguration.BLINK_FREQUENCY);
		this.cfg.setDefault(ClientConfiguration.EYELID_OFFSET_X);
		this.cfg.setDefault(ClientConfiguration.EYELID_OFFSET_Y);
		this.cfg.finish();
	}

	public ClientConfiguration getConfig() {
		return cfg;
	}

	private <VALUE> boolean updateValue(ConfigKey<VALUE> key, VALUE value, java.util.function.Consumer<VALUE> setter) {
		if (key.validate(value)) {
			setter.accept(value);
			return true;
		}
		return false;
	}

	public Gender getGender() {
		return gender;
	}

	public boolean updateGender(Gender value) {
		return updateValue(ClientConfiguration.GENDER, value, v -> this.gender = v);
	}

	public float getBustSize() {
		return pBustSize;
	}

	public boolean updateBustSize(float value) {
		return updateValue(ClientConfiguration.BUST_SIZE, value, v -> this.pBustSize = v);
	}

	public boolean hasHurtSounds() {
		return hurtSounds;
	}

	public boolean updateHurtSounds(boolean value) {
		return updateValue(ClientConfiguration.HURT_SOUNDS, value, v -> this.hurtSounds = v);
	}

	public boolean hasBreastPhysics() {
		return breastPhysics;
	}

	public boolean updateBreastPhysics(boolean value) {
		return updateValue(ClientConfiguration.BREAST_PHYSICS, value, v -> this.breastPhysics = v);
	}

	public boolean hasArmorBreastPhysics() {
		return armorBreastPhysics;
	}

	public boolean updateArmorBreastPhysics(boolean value) {
		return updateValue(ClientConfiguration.BREAST_PHYSICS_ARMOR, value, v -> this.armorBreastPhysics = v);
	}

	public boolean showBreastsInArmor() {
		return showBreastsInArmor;
	}

	public boolean updateShowBreastsInArmor(boolean value) {
		return updateValue(ClientConfiguration.SHOW_IN_ARMOR, value, v -> this.showBreastsInArmor = v);
	}

	public float getBounceMultiplier() {
		return Math.round((this.getBounceMultiplierRaw() * 3) * 100) / 100f;
	}

	public float getBounceMultiplierRaw() {
		return bounceMultiplier;
	}

	public boolean updateBounceMultiplier(float value) {
		return updateValue(ClientConfiguration.BOUNCE_MULTIPLIER, value, v -> this.bounceMultiplier = v);
	}

	public float getFloppiness() {
		return this.floppyMultiplier;
	}

	public boolean updateFloppiness(float value) {
		return updateValue(ClientConfiguration.FLOPPY_MULTIPLIER, value, v -> this.floppyMultiplier = v);
	}

	public SyncStatus getSyncStatus() {
		return this.syncStatus;
	}

	public static JsonObject toJsonObject(GenderPlayer plr) {
		JsonObject obj = new JsonObject();
		ClientConfiguration.USERNAME.save(obj, plr.uuid);
		ClientConfiguration.GENDER.save(obj, plr.getGender());
		ClientConfiguration.BUST_SIZE.save(obj, plr.getBustSize());
		ClientConfiguration.HURT_SOUNDS.save(obj, plr.hasHurtSounds());
		
		obj.addProperty("blinkEnabled", plr.isBlinkEnabled());
		obj.addProperty("eyeSize", plr.getEyeSize());
		obj.addProperty("eyeDistance", plr.getEyeDistance());
		obj.addProperty("eyeHeight", plr.getEyeHeight());
		obj.addProperty("eyelidSize", plr.getEyelidSize());
		obj.addProperty("dualEyelid", plr.isDualEyelid());
		obj.addProperty("blinkFrequency", plr.getBlinkFrequency());
		obj.addProperty("eyelidOffsetX", plr.getEyelidOffsetX());
		obj.addProperty("eyelidOffsetY", plr.getEyelidOffsetY());

		ClientConfiguration.BREAST_PHYSICS.save(obj, plr.hasBreastPhysics());
		ClientConfiguration.BREAST_PHYSICS_ARMOR.save(obj, plr.hasArmorBreastPhysics());
		ClientConfiguration.SHOW_IN_ARMOR.save(obj, plr.showBreastsInArmor());
		ClientConfiguration.BOUNCE_MULTIPLIER.save(obj, plr.getBounceMultiplierRaw());
		ClientConfiguration.FLOPPY_MULTIPLIER.save(obj, plr.getFloppiness());

		Breasts breasts = plr.getBreasts();
		ClientConfiguration.BREASTS_OFFSET_X.save(obj, breasts.getXOffset());
		ClientConfiguration.BREASTS_OFFSET_Y.save(obj, breasts.getYOffset());
		ClientConfiguration.BREASTS_OFFSET_Z.save(obj, breasts.getZOffset());
		ClientConfiguration.BREASTS_UNIBOOB.save(obj, breasts.isUniboob());
		ClientConfiguration.BREASTS_CLEAVAGE.save(obj, breasts.getCleavage());
		return obj;
	}

	public static GenderPlayer fromJsonObject(JsonObject obj) {
		GenderPlayer plr = new GenderPlayer(ClientConfiguration.USERNAME.read(obj));
		plr.updateGender(ClientConfiguration.GENDER.read(obj));
		plr.updateBustSize(ClientConfiguration.BUST_SIZE.read(obj));
		plr.updateHurtSounds(ClientConfiguration.HURT_SOUNDS.read(obj));
		
		if (obj.has("blinkEnabled")) plr.updateBlinking(obj.get("blinkEnabled").getAsBoolean());
		if (obj.has("eyeSize")) plr.updateEyeSize(obj.get("eyeSize").getAsInt());
		if (obj.has("eyeDistance")) plr.updateEyeDistance(obj.get("eyeDistance").getAsInt());
		if (obj.has("eyeHeight")) plr.updateEyeHeight(obj.get("eyeHeight").getAsInt());
		if (obj.has("eyelidSize")) plr.updateEyelidSize(obj.get("eyelidSize").getAsInt());
		if (obj.has("dualEyelid")) plr.updateDualEyelid(obj.get("dualEyelid").getAsBoolean());
		if (obj.has("blinkFrequency")) plr.updateBlinkFrequency(obj.get("blinkFrequency").getAsFloat());
		if (obj.has("eyelidOffsetX")) plr.updateEyelidOffsetX(obj.get("eyelidOffsetX").getAsInt());
		if (obj.has("eyelidOffsetY")) plr.updateEyelidOffsetY(obj.get("eyelidOffsetY").getAsInt());

		plr.updateBreastPhysics(ClientConfiguration.BREAST_PHYSICS.read(obj));
		plr.updateArmorBreastPhysics(ClientConfiguration.BREAST_PHYSICS_ARMOR.read(obj));
		plr.updateShowBreastsInArmor(ClientConfiguration.SHOW_IN_ARMOR.read(obj));
		plr.updateBounceMultiplier(ClientConfiguration.BOUNCE_MULTIPLIER.read(obj));
		plr.updateFloppiness(ClientConfiguration.FLOPPY_MULTIPLIER.read(obj));

		Breasts breasts = plr.getBreasts();
		breasts.updateXOffset(ClientConfiguration.BREASTS_OFFSET_X.read(obj));
		breasts.updateYOffset(ClientConfiguration.BREASTS_OFFSET_Y.read(obj));
		breasts.updateZOffset(ClientConfiguration.BREASTS_OFFSET_Z.read(obj));
		breasts.updateUniboob(ClientConfiguration.BREASTS_UNIBOOB.read(obj));
		breasts.updateCleavage(ClientConfiguration.BREASTS_CLEAVAGE.read(obj));

		return plr;
	}

	public static GenderPlayer loadCachedPlayer(UUID uuid, boolean markForSync) {
		GenderPlayer plr = WildfireGender.getPlayerById(uuid);
		if (plr != null) {
			plr.lockSettings = false;
			plr.syncStatus = SyncStatus.CACHED;
			ClientConfiguration config = plr.getConfig();
			plr.updateGender(config.get(ClientConfiguration.GENDER));
			plr.updateBustSize(config.get(ClientConfiguration.BUST_SIZE));
			plr.updateHurtSounds(config.get(ClientConfiguration.HURT_SOUNDS));
			
			plr.updateBlinking(config.get(ClientConfiguration.BLINK_ENABLED));
			plr.updateEyeSize(Math.round((Float) config.get(ClientConfiguration.EYE_SIZE)));
			plr.updateEyeDistance(Math.round((Float) config.get(ClientConfiguration.EYE_DISTANCE)));
			plr.updateEyeHeight(Math.round((Float) config.get(ClientConfiguration.EYE_HEIGHT)));
			plr.updateEyelidSize(Math.round((Float) config.get(ClientConfiguration.EYELID_SIZE)));
			plr.updateDualEyelid(config.get(ClientConfiguration.DUAL_EYELID));
			plr.updateBlinkFrequency(config.get(ClientConfiguration.BLINK_FREQUENCY));
			plr.updateEyelidOffsetX(Math.round((Float) config.get(ClientConfiguration.EYELID_OFFSET_X)));
			plr.updateEyelidOffsetY(Math.round((Float) config.get(ClientConfiguration.EYELID_OFFSET_Y)));

			plr.updateBreastPhysics(config.get(ClientConfiguration.BREAST_PHYSICS));
			plr.updateArmorBreastPhysics(config.get(ClientConfiguration.BREAST_PHYSICS_ARMOR));
			plr.updateShowBreastsInArmor(config.get(ClientConfiguration.SHOW_IN_ARMOR));
			plr.updateBounceMultiplier(config.get(ClientConfiguration.BOUNCE_MULTIPLIER));
			plr.updateFloppiness(config.get(ClientConfiguration.FLOPPY_MULTIPLIER));

			Breasts breasts = plr.getBreasts();
			breasts.updateXOffset(config.get(ClientConfiguration.BREASTS_OFFSET_X));
			breasts.updateYOffset(config.get(ClientConfiguration.BREASTS_OFFSET_Y));
			breasts.updateZOffset(config.get(ClientConfiguration.BREASTS_OFFSET_Z));
			breasts.updateUniboob(config.get(ClientConfiguration.BREASTS_UNIBOOB));
			breasts.updateCleavage(config.get(ClientConfiguration.BREASTS_CLEAVAGE));

			if (markForSync) {
				plr.needsSync = true;
			}
			return plr;
		}
		return null;
	}

	public static void saveGenderInfo(GenderPlayer plr) {
		ClientConfiguration config = plr.getConfig();
		config.set(ClientConfiguration.USERNAME, plr.uuid);
		config.set(ClientConfiguration.GENDER, plr.getGender());
		config.set(ClientConfiguration.BUST_SIZE, plr.getBustSize());
		config.set(ClientConfiguration.HURT_SOUNDS, plr.hasHurtSounds());
		
		config.set(ClientConfiguration.BLINK_ENABLED, plr.isBlinkEnabled());
		config.set(ClientConfiguration.EYE_SIZE, (float) plr.getEyeSize());
		config.set(ClientConfiguration.EYE_DISTANCE, (float) plr.getEyeDistance());
		config.set(ClientConfiguration.EYE_HEIGHT, (float) plr.getEyeHeight());
		config.set(ClientConfiguration.EYELID_SIZE, (float) plr.getEyelidSize());
		config.set(ClientConfiguration.DUAL_EYELID, plr.isDualEyelid());
		config.set(ClientConfiguration.BLINK_FREQUENCY, plr.getBlinkFrequency());
		config.set(ClientConfiguration.EYELID_OFFSET_X, (float) plr.getEyelidOffsetX());
		config.set(ClientConfiguration.EYELID_OFFSET_Y, (float) plr.getEyelidOffsetY());

		config.set(ClientConfiguration.BREAST_PHYSICS, plr.hasBreastPhysics());
		config.set(ClientConfiguration.BREAST_PHYSICS_ARMOR, plr.hasArmorBreastPhysics());
		config.set(ClientConfiguration.SHOW_IN_ARMOR, plr.showBreastsInArmor());
		config.set(ClientConfiguration.BOUNCE_MULTIPLIER, plr.getBounceMultiplierRaw());
		config.set(ClientConfiguration.FLOPPY_MULTIPLIER, plr.getFloppiness());

		config.set(ClientConfiguration.BREASTS_OFFSET_X, plr.getBreasts().getXOffset());
		config.set(ClientConfiguration.BREASTS_OFFSET_Y, plr.getBreasts().getYOffset());
		config.set(ClientConfiguration.BREASTS_OFFSET_Z, plr.getBreasts().getZOffset());
		config.set(ClientConfiguration.BREASTS_UNIBOOB, plr.getBreasts().isUniboob());
		config.set(ClientConfiguration.BREASTS_CLEAVAGE, plr.getBreasts().getCleavage());

		config.save();
		plr.needsSync = true;
	}

	public Breasts getBreasts() {
		return breasts;
	}

	public BreastPhysics getLeftBreastPhysics() {
		return lBreastPhysics;
	}

	public BreastPhysics getRightBreastPhysics() {
		return rBreastPhysics;
	}

	public enum SyncStatus {
		CACHED, SYNCED, UNKNOWN
	}

	public enum Gender {
		FEMALE(new TextComponentTranslation("wildfire_gender.label.female")
				.setStyle(new net.minecraft.util.text.Style().setColor(TextFormatting.LIGHT_PURPLE))),
		MALE(new TextComponentTranslation("wildfire_gender.label.male")
				.setStyle(new net.minecraft.util.text.Style().setColor(TextFormatting.BLUE))),
		OTHER(new TextComponentTranslation("wildfire_gender.label.other")
				.setStyle(new net.minecraft.util.text.Style().setColor(TextFormatting.GREEN)));

		private final ITextComponent name;

		Gender(ITextComponent name) {
			this.name = name;
		}

		public ITextComponent getDisplayName() {
			return name;
		}

		public boolean hasFemaleHurtSounds() {
			return this == FEMALE;
		}

		public boolean canHaveBreasts() {
			return this != MALE;
		}
	}
}