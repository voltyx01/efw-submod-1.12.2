package net.bettercombat.client.collision;

import net.bettercombat.api.WeaponAttributes;
import net.minecraft.util.math.Vec3d;

public class WeaponHitBoxes {
    public static Vec3d createHitbox(WeaponAttributes.HitBoxShape shape, double attackRange, boolean isSpinAttack) {
        if (shape == null) {
            shape = WeaponAttributes.HitBoxShape.HORIZONTAL_PLANE;
        }
        switch (shape) {
            case FORWARD_BOX:
                return new Vec3d(attackRange * 0.5, attackRange * 0.5, attackRange);
            case VERTICAL_PLANE: {
                float zMultiplier = isSpinAttack ? 2.0f : 1.0f;
                return new Vec3d(attackRange / 3.0, attackRange * 2.0, attackRange * zMultiplier);
            }
            case HORIZONTAL_PLANE:
            default: {
                float zMultiplier = isSpinAttack ? 2.0f : 1.0f;
                return new Vec3d(attackRange * 2.0, attackRange / 3.0, attackRange * zMultiplier);
            }
        }
    }
}
