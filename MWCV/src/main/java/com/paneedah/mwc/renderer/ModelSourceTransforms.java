package com.paneedah.mwc.renderer;

import net.minecraft.client.model.ModelBase;
import org.lwjgl.opengl.GL11;

import java.util.function.Consumer;

/**
 * Represents transformations for different rendering positions of a model.
 * <p>
 * This class contains various positioning callbacks that can be configured to modify the positioning of a model in different scenarios like entity, inventory, third-person view, and more.
 *
 * @author Luna Lage (Desoroxxx)
 * @since 0.1
 */
public final class ModelSourceTransforms {

    // We use empty defaults values to not have null pointers exceptions thrown
    private Runnable entityPositioning = () -> {};
    private Runnable inventoryPositioning = () -> {};
    private Runnable thirdPersonPositioning = () -> {};
    private Runnable firstPersonPositioning = () -> {};
    private Runnable customEquippedPositioning = () -> {};
    private Consumer<ModelBase> firstPersonModelPositioning = model -> {};
    private Consumer<ModelBase> thirdPersonModelPositioning = model -> {};
    private Consumer<ModelBase> inventoryModelPositioning = model -> {};
    private Consumer<ModelBase> entityModelPositioning = model -> {};

    // Todo: Make this empty by making hand rendering a boolean somewhere than weapons and everything can call instead of spending time doing useless OGL calls
    // Unlike the rest, these two aren't empty it's because by default hands shouldn't be rendered
    private Runnable firstPersonLeftHandPositioning = () -> GL11.glScalef(0, 0, 0);
    private Runnable firstPersonRightHandPositioning = () -> GL11.glScalef(0, 0, 0);

    public ModelSourceTransforms() {}

    public Runnable getEntityPositioning() { return entityPositioning; }
    public void setEntityPositioning(Runnable entityPositioning) { this.entityPositioning = entityPositioning; }

    public Runnable getInventoryPositioning() { return inventoryPositioning; }
    public void setInventoryPositioning(Runnable inventoryPositioning) { this.inventoryPositioning = inventoryPositioning; }

    public Runnable getThirdPersonPositioning() { return thirdPersonPositioning; }
    public void setThirdPersonPositioning(Runnable thirdPersonPositioning) { this.thirdPersonPositioning = thirdPersonPositioning; }

    public Runnable getFirstPersonPositioning() { return firstPersonPositioning; }
    public void setFirstPersonPositioning(Runnable firstPersonPositioning) { this.firstPersonPositioning = firstPersonPositioning; }

    public Runnable getCustomEquippedPositioning() { return customEquippedPositioning; }
    public void setCustomEquippedPositioning(Runnable customEquippedPositioning) { this.customEquippedPositioning = customEquippedPositioning; }

    public Consumer<ModelBase> getFirstPersonModelPositioning() { return firstPersonModelPositioning; }
    public void setFirstPersonModelPositioning(Consumer<ModelBase> firstPersonModelPositioning) { this.firstPersonModelPositioning = firstPersonModelPositioning; }

    public Consumer<ModelBase> getThirdPersonModelPositioning() { return thirdPersonModelPositioning; }
    public void setThirdPersonModelPositioning(Consumer<ModelBase> thirdPersonModelPositioning) { this.thirdPersonModelPositioning = thirdPersonModelPositioning; }

    public Consumer<ModelBase> getInventoryModelPositioning() { return inventoryModelPositioning; }
    public void setInventoryModelPositioning(Consumer<ModelBase> inventoryModelPositioning) { this.inventoryModelPositioning = inventoryModelPositioning; }

    public Consumer<ModelBase> getEntityModelPositioning() { return entityModelPositioning; }
    public void setEntityModelPositioning(Consumer<ModelBase> entityModelPositioning) { this.entityModelPositioning = entityModelPositioning; }

    public Runnable getFirstPersonLeftHandPositioning() { return firstPersonLeftHandPositioning; }
    public void setFirstPersonLeftHandPositioning(Runnable firstPersonLeftHandPositioning) { this.firstPersonLeftHandPositioning = firstPersonLeftHandPositioning; }

    public Runnable getFirstPersonRightHandPositioning() { return firstPersonRightHandPositioning; }
    public void setFirstPersonRightHandPositioning(Runnable firstPersonRightHandPositioning) { this.firstPersonRightHandPositioning = firstPersonRightHandPositioning; }

    public static ModelSourceTransformsBuilder builder() {
        return new ModelSourceTransformsBuilder();
    }

    public static class ModelSourceTransformsBuilder {
        private Runnable entityPositioning = () -> {};
        private Runnable inventoryPositioning = () -> {};
        private Runnable thirdPersonPositioning = () -> {};
        private Runnable firstPersonPositioning = () -> {};
        private Runnable customEquippedPositioning = () -> {};
        private Consumer<ModelBase> firstPersonModelPositioning = model -> {};
        private Consumer<ModelBase> thirdPersonModelPositioning = model -> {};
        private Consumer<ModelBase> inventoryModelPositioning = model -> {};
        private Consumer<ModelBase> entityModelPositioning = model -> {};
        private Runnable firstPersonLeftHandPositioning = () -> GL11.glScalef(0, 0, 0);
        private Runnable firstPersonRightHandPositioning = () -> GL11.glScalef(0, 0, 0);

        public ModelSourceTransformsBuilder entityPositioning(Runnable entityPositioning) {
            this.entityPositioning = entityPositioning;
            return this;
        }

        public ModelSourceTransformsBuilder inventoryPositioning(Runnable inventoryPositioning) {
            this.inventoryPositioning = inventoryPositioning;
            return this;
        }

        public ModelSourceTransformsBuilder thirdPersonPositioning(Runnable thirdPersonPositioning) {
            this.thirdPersonPositioning = thirdPersonPositioning;
            return this;
        }

        public ModelSourceTransformsBuilder firstPersonPositioning(Runnable firstPersonPositioning) {
            this.firstPersonPositioning = firstPersonPositioning;
            return this;
        }

        public ModelSourceTransformsBuilder customEquippedPositioning(Runnable customEquippedPositioning) {
            this.customEquippedPositioning = customEquippedPositioning;
            return this;
        }

        public ModelSourceTransformsBuilder firstPersonModelPositioning(Consumer<ModelBase> firstPersonModelPositioning) {
            this.firstPersonModelPositioning = firstPersonModelPositioning;
            return this;
        }

        public ModelSourceTransformsBuilder thirdPersonModelPositioning(Consumer<ModelBase> thirdPersonModelPositioning) {
            this.thirdPersonModelPositioning = thirdPersonModelPositioning;
            return this;
        }

        public ModelSourceTransformsBuilder inventoryModelPositioning(Consumer<ModelBase> inventoryModelPositioning) {
            this.inventoryModelPositioning = inventoryModelPositioning;
            return this;
        }

        public ModelSourceTransformsBuilder entityModelPositioning(Consumer<ModelBase> entityModelPositioning) {
            this.entityModelPositioning = entityModelPositioning;
            return this;
        }

        public ModelSourceTransformsBuilder firstPersonLeftHandPositioning(Runnable firstPersonLeftHandPositioning) {
            this.firstPersonLeftHandPositioning = firstPersonLeftHandPositioning;
            return this;
        }

        public ModelSourceTransformsBuilder firstPersonRightHandPositioning(Runnable firstPersonRightHandPositioning) {
            this.firstPersonRightHandPositioning = firstPersonRightHandPositioning;
            return this;
        }

        public ModelSourceTransforms build() {
            ModelSourceTransforms t = new ModelSourceTransforms();
            t.entityPositioning = this.entityPositioning;
            t.inventoryPositioning = this.inventoryPositioning;
            t.thirdPersonPositioning = this.thirdPersonPositioning;
            t.firstPersonPositioning = this.firstPersonPositioning;
            t.customEquippedPositioning = this.customEquippedPositioning;
            t.firstPersonModelPositioning = this.firstPersonModelPositioning;
            t.thirdPersonModelPositioning = this.thirdPersonModelPositioning;
            t.inventoryModelPositioning = this.inventoryModelPositioning;
            t.entityModelPositioning = this.entityModelPositioning;
            t.firstPersonLeftHandPositioning = this.firstPersonLeftHandPositioning;
            t.firstPersonRightHandPositioning = this.firstPersonRightHandPositioning;
            return t;
        }
    }
}
