package com.paneedah.weaponlib.stats;

import com.google.gson.annotations.SerializedName;

/**
 * Data model representing stat modifiers provided by an individual attachment.
 * All multiplier values are baseline 1.0 (no change), e.g. 0.85 means -15% (better recoil, worse speed, etc.).
 * Weight is in kilograms.
 */
public class AttachmentStatData {

    @SerializedName("recoilMultiplier")
    public double recoilMultiplier = 1.0;

    @SerializedName("visualRecoilMultiplier")
    public double visualRecoilMultiplier = 1.0;

    @SerializedName("recoilRecoveryMultiplier")
    public double recoilRecoveryMultiplier = 1.0;

    @SerializedName("hipSpreadMultiplier")
    public double hipSpreadMultiplier = 1.0;

    @SerializedName("aimSpreadMultiplier")
    public double aimSpreadMultiplier = 1.0;

    @SerializedName("adsSpeedMultiplier")
    public double adsSpeedMultiplier = 1.0;

    @SerializedName("drawSpeedMultiplier")
    public double drawSpeedMultiplier = 1.0;

    @SerializedName("reloadSpeedMultiplier")
    public double reloadSpeedMultiplier = 1.0;

    @SerializedName("weight")
    public double weight = 0.0; // In kg (e.g. 0.35 kg)

    public AttachmentStatData() {
    }

    public AttachmentStatData(double recoilMultiplier, double visualRecoilMultiplier, double recoilRecoveryMultiplier,
                              double hipSpreadMultiplier, double aimSpreadMultiplier,
                              double adsSpeedMultiplier, double drawSpeedMultiplier, double reloadSpeedMultiplier,
                              double weight) {
        this.recoilMultiplier = recoilMultiplier;
        this.visualRecoilMultiplier = visualRecoilMultiplier;
        this.recoilRecoveryMultiplier = recoilRecoveryMultiplier;
        this.hipSpreadMultiplier = hipSpreadMultiplier;
        this.aimSpreadMultiplier = aimSpreadMultiplier;
        this.adsSpeedMultiplier = adsSpeedMultiplier;
        this.drawSpeedMultiplier = drawSpeedMultiplier;
        this.reloadSpeedMultiplier = reloadSpeedMultiplier;
        this.weight = weight;
    }

    public static AttachmentStatData createDefault() {
        return new AttachmentStatData();
    }
}
