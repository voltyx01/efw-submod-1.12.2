package com.voltyx.mwccf.geo;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class GeoArmorModel extends ModelBiped {
    
    private final Map<String, ModelRenderer> boneMap = new HashMap<>();
    private final Map<String, float[]> bedrockPivotMap = new HashMap<>();
    
    public ModelRenderer leftBoob;
    public ModelRenderer rightBoob;
    public ModelRenderer bipedRightArmSlim;
    public ModelRenderer bipedLeftArmSlim;
    
    private final Map<String, ModelRenderer> slimBoneMap = new HashMap<>();

    public GeoArmorModel(ResourceLocation geoFile) {
        super();
        this.textureWidth = 128;
        this.textureHeight = 128;
        
        this.bipedHead.cubeList.clear();
        this.bipedHeadwear.cubeList.clear();
        this.bipedBody.cubeList.clear();
        this.bipedRightArm.cubeList.clear();
        this.bipedLeftArm.cubeList.clear();
        this.bipedRightLeg.cubeList.clear();
        this.bipedLeftLeg.cubeList.clear();

        this.leftBoob = new com.voltyx.gender.render.WildfireModelRenderer(128, 128);
        this.rightBoob = new com.voltyx.gender.render.WildfireModelRenderer(128, 128);
        this.bipedRightArmSlim = new ModelRenderer(this);
        this.bipedRightArmSlim.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.bipedLeftArmSlim = new ModelRenderer(this);
        this.bipedLeftArmSlim.setRotationPoint(5.0F, 2.0F, 0.0F);

        boneMap.put("bipedHead", this.bipedHead);
        boneMap.put("bipedBody", this.bipedBody);
        boneMap.put("bipedRightArm", this.bipedRightArm);
        boneMap.put("bipedLeftArm", this.bipedLeftArm);
        boneMap.put("bipedRightLeg", this.bipedRightLeg);
        boneMap.put("bipedLeftLeg", this.bipedLeftLeg);

        slimBoneMap.put("bipedHead", this.bipedHead);
        slimBoneMap.put("bipedBody", this.bipedBody);
        slimBoneMap.put("bipedRightArm", this.bipedRightArmSlim);
        slimBoneMap.put("bipedLeftArm", this.bipedLeftArmSlim);
        slimBoneMap.put("bipedRightLeg", this.bipedRightLeg);
        slimBoneMap.put("bipedLeftLeg", this.bipedLeftLeg);

        bedrockPivotMap.put("bipedHead", new float[]{0, 24, 0});
        bedrockPivotMap.put("bipedBody", new float[]{0, 24, 0});
        bedrockPivotMap.put("bipedRightArm", new float[]{-5, 22, 0});
        bedrockPivotMap.put("bipedLeftArm", new float[]{5, 22, 0});
        bedrockPivotMap.put("bipedRightLeg", new float[]{-2, 12, 0});
        bedrockPivotMap.put("bipedLeftLeg", new float[]{2, 12, 0});

        try {
            java.io.InputStream stream = Minecraft.getMinecraft().getResourceManager().getResource(geoFile).getInputStream();
            String jsonText = org.apache.commons.io.IOUtils.toString(stream, java.nio.charset.StandardCharsets.UTF_8);
            if (jsonText.startsWith("\uFEFF")) {
                jsonText = jsonText.substring(1);
            }
            JsonObject root = new JsonParser().parse(jsonText).getAsJsonObject();
            JsonArray geometries = root.getAsJsonArray("minecraft:geometry");
            if (geometries != null && geometries.size() > 0) {
                JsonObject geometry = geometries.get(0).getAsJsonObject();
                if (geometry.has("description")) {
                    JsonObject desc = geometry.getAsJsonObject("description");
                    if (desc.has("texture_width")) this.textureWidth = desc.get("texture_width").getAsInt();
                    if (desc.has("texture_height")) this.textureHeight = desc.get("texture_height").getAsInt();
                    
                    this.bipedHead.setTextureSize(this.textureWidth, this.textureHeight);
                    this.bipedBody.setTextureSize(this.textureWidth, this.textureHeight);
                    this.bipedRightArm.setTextureSize(this.textureWidth, this.textureHeight);
                    this.bipedLeftArm.setTextureSize(this.textureWidth, this.textureHeight);
                    this.bipedRightLeg.setTextureSize(this.textureWidth, this.textureHeight);
                    this.bipedLeftLeg.setTextureSize(this.textureWidth, this.textureHeight);
                    this.bipedHeadwear.setTextureSize(this.textureWidth, this.textureHeight);
                    this.leftBoob.setTextureSize(this.textureWidth, this.textureHeight);
                    this.rightBoob.setTextureSize(this.textureWidth, this.textureHeight);
                    this.bipedRightArmSlim.setTextureSize(this.textureWidth, this.textureHeight);
                    this.bipedLeftArmSlim.setTextureSize(this.textureWidth, this.textureHeight);
                }

                JsonArray bones = geometry.getAsJsonArray("bones");
                if (bones != null) {
                    Map<String, float[]> vanillaJavaPivots = new HashMap<>();
                    vanillaJavaPivots.put("bipedHead", new float[]{0, 0, 0});
                    vanillaJavaPivots.put("bipedHeadwear", new float[]{0, 0, 0});
                    vanillaJavaPivots.put("bipedBody", new float[]{0, 0, 0});
                    vanillaJavaPivots.put("bipedRightArm", new float[]{-5, 2, 0});
                    vanillaJavaPivots.put("bipedLeftArm", new float[]{5, 2, 0});
                    vanillaJavaPivots.put("bipedRightLeg", new float[]{-1.9f, 12, 0});
                    vanillaJavaPivots.put("bipedLeftLeg", new float[]{1.9f, 12, 0});

                    for (JsonElement boneElem : bones) {
                        JsonObject boneObj = boneElem.getAsJsonObject();
                        final String name = boneObj.get("name").getAsString();
                        
                        float[] pivot = new float[]{0, 0, 0};
                        if (boneObj.has("pivot")) {
                            JsonArray p = boneObj.getAsJsonArray("pivot");
                            pivot[0] = p.get(0).getAsFloat();
                            pivot[1] = p.get(1).getAsFloat();
                            pivot[2] = p.get(2).getAsFloat();
                        }
                        bedrockPivotMap.put(name, pivot);

                        ModelRenderer renderer;
                        ModelRenderer slimRenderer;
                        if (boneMap.containsKey(name)) {
                            renderer = boneMap.get(name);
                            slimRenderer = slimBoneMap.get(name);
                        } else {
                            renderer = new ModelRenderer(this) {
                                @Override
                                public void render(float scale) {
                                    int currentTexture = 0;
                                    float lastLightX = 0;
                                    float lastLightY = 0;
                                    if (name.equals("screen") || name.equals("display")) {
                                        currentTexture = net.minecraft.client.renderer.GlStateManager.glGetInteger(org.lwjgl.opengl.GL11.GL_TEXTURE_BINDING_2D);
                                        if (!com.voltyx.mwccf.geo.BraceletUI.bindScreenTexture()) {
                                            Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("mwccf:textures/models/armor/screen.png"));
                                        }
                                        
                                        lastLightX = net.minecraft.client.renderer.OpenGlHelper.lastBrightnessX;
                                        lastLightY = net.minecraft.client.renderer.OpenGlHelper.lastBrightnessY;
                                        net.minecraft.client.renderer.GlStateManager.disableLighting();
                                        net.minecraft.client.renderer.OpenGlHelper.setLightmapTextureCoords(net.minecraft.client.renderer.OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
                                    }
                                    super.render(scale);
                                    if (name.equals("screen") || name.equals("display")) {
                                        net.minecraft.client.renderer.GlStateManager.bindTexture(currentTexture);
                                        net.minecraft.client.renderer.OpenGlHelper.setLightmapTextureCoords(net.minecraft.client.renderer.OpenGlHelper.lightmapTexUnit, lastLightX, lastLightY);
                                        net.minecraft.client.renderer.GlStateManager.enableLighting();
                                    }
                                }
                            };
                            slimRenderer = new ModelRenderer(this) {
                                @Override
                                public void render(float scale) {
                                    int currentTexture = 0;
                                    float lastLightX = 0;
                                    float lastLightY = 0;
                                    if (name.equals("screen") || name.equals("display")) {
                                        currentTexture = net.minecraft.client.renderer.GlStateManager.glGetInteger(org.lwjgl.opengl.GL11.GL_TEXTURE_BINDING_2D);
                                        if (!com.voltyx.mwccf.geo.BraceletUI.bindScreenTexture()) {
                                            Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("mwccf:textures/models/armor/screen.png"));
                                        }
                                        
                                        lastLightX = net.minecraft.client.renderer.OpenGlHelper.lastBrightnessX;
                                        lastLightY = net.minecraft.client.renderer.OpenGlHelper.lastBrightnessY;
                                        net.minecraft.client.renderer.GlStateManager.disableLighting();
                                        net.minecraft.client.renderer.OpenGlHelper.setLightmapTextureCoords(net.minecraft.client.renderer.OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
                                    }
                                    super.render(scale);
                                    if (name.equals("screen") || name.equals("display")) {
                                        net.minecraft.client.renderer.GlStateManager.bindTexture(currentTexture);
                                        net.minecraft.client.renderer.OpenGlHelper.setLightmapTextureCoords(net.minecraft.client.renderer.OpenGlHelper.lightmapTexUnit, lastLightX, lastLightY);
                                        net.minecraft.client.renderer.GlStateManager.enableLighting();
                                    }
                                }
                            };
                            if (name.equals("screen") || name.equals("display")) {
                                renderer.setTextureSize(8, 8);
                                slimRenderer.setTextureSize(8, 8);
                            } else {
                                renderer.setTextureSize(this.textureWidth, this.textureHeight);
                                slimRenderer.setTextureSize(this.textureWidth, this.textureHeight);
                            }
                            boneMap.put(name, renderer);
                            slimBoneMap.put(name, slimRenderer);
                            
                            // Setup rotation point relative to parent
                            if (boneObj.has("parent")) {
                                String parentName = boneObj.get("parent").getAsString();
                                if (vanillaJavaPivots.containsKey(parentName)) {
                                    float[] parentJavaPivot = vanillaJavaPivots.get(parentName);
                                    float childAbsX = pivot[0];
                                    float childAbsY = 24 - pivot[1];
                                    float childAbsZ = pivot[2];
                                    renderer.setRotationPoint(childAbsX - parentJavaPivot[0], childAbsY - parentJavaPivot[1], childAbsZ - parentJavaPivot[2]);
                                    slimRenderer.setRotationPoint(childAbsX - parentJavaPivot[0], childAbsY - parentJavaPivot[1], childAbsZ - parentJavaPivot[2]);
                                } else {
                                    float[] parentPivot = bedrockPivotMap.get(parentName);
                                    if (parentPivot != null) {
                                        float relX = pivot[0] - parentPivot[0];
                                        float relY = parentPivot[1] - pivot[1];
                                        float relZ = pivot[2] - parentPivot[2];
                                        renderer.setRotationPoint(relX, relY, relZ);
                                        slimRenderer.setRotationPoint(relX, relY, relZ);
                                    }
                                }
                            } else {
                                // Absolute
                                renderer.setRotationPoint(pivot[0], 24 - pivot[1], pivot[2]);
                                slimRenderer.setRotationPoint(pivot[0], 24 - pivot[1], pivot[2]);
                            }
                            
                            if (boneObj.has("rotation")) {
                                JsonArray r = boneObj.getAsJsonArray("rotation");
                                float rx = (float) Math.toRadians(r.get(0).getAsFloat());
                                float ry = (float) Math.toRadians(r.get(1).getAsFloat());
                                float rz = (float) Math.toRadians(r.get(2).getAsFloat());
                                renderer.rotateAngleX = rx; renderer.rotateAngleY = ry; renderer.rotateAngleZ = rz;
                                slimRenderer.rotateAngleX = rx; slimRenderer.rotateAngleY = ry; slimRenderer.rotateAngleZ = rz;
                            }
                        }

                        if (boneObj.has("cubes")) {
                            boolean isFirstCube = true;
                            for (JsonElement cubeElem : boneObj.getAsJsonArray("cubes")) {
                                JsonObject cubeObj = cubeElem.getAsJsonObject();
                                JsonArray origin = cubeObj.getAsJsonArray("origin");
                                JsonArray size = cubeObj.getAsJsonArray("size");
                                
                                float ox = origin.get(0).getAsFloat();
                                float oy = origin.get(1).getAsFloat();
                                float oz = origin.get(2).getAsFloat();
                                
                                float rawSx = size.get(0).getAsFloat();
                                float rawSy = size.get(1).getAsFloat();
                                float rawSz = size.get(2).getAsFloat();
                                
                                int u = 0, v = 0;
                                java.util.Map<String, float[]> faceUvs = null;
                                if (cubeObj.has("uv")) {
                                    JsonElement uvElem = cubeObj.get("uv");
                                    if (uvElem.isJsonArray()) {
                                        JsonArray uvArr = uvElem.getAsJsonArray();
                                        u = uvArr.get(0).getAsInt();
                                        v = uvArr.get(1).getAsInt();
                                    } else if (uvElem.isJsonObject()) {
                                        JsonObject uvObj = uvElem.getAsJsonObject();
                                        faceUvs = new java.util.HashMap<>();
                                        for (java.util.Map.Entry<String, JsonElement> entry : uvObj.entrySet()) {
                                            String fName = entry.getKey();
                                            if (entry.getValue().isJsonObject()) {
                                                JsonObject fObj = entry.getValue().getAsJsonObject();
                                                if (fObj.has("uv") && fObj.has("uv_size")) {
                                                    JsonArray uvA = fObj.getAsJsonArray("uv");
                                                    JsonArray szA = fObj.getAsJsonArray("uv_size");
                                                    faceUvs.put(fName, new float[]{
                                                        uvA.get(0).getAsFloat(),
                                                        uvA.get(1).getAsFloat(),
                                                        szA.get(0).getAsFloat(),
                                                        szA.get(1).getAsFloat()
                                                    });
                                                }
                                            }
                                        }
                                    }
                                }
                                
                                float inflate = cubeObj.has("inflate") ? cubeObj.get("inflate").getAsFloat() : 0.0F;

                                if (name.equals("armorBody")) {
                                    int sz = (int)Math.max(1, Math.round(rawSz));
                                    int sx = (int)Math.max(1, Math.round(rawSx));
                                    int leftU = u + sz + (sx / 2) - 4;
                                    int leftV = v + sz - 2;
                                    ((com.voltyx.gender.render.WildfireModelRenderer)this.leftBoob).addBreastBox(leftU, leftV, -4F, 0.0F, 0F, 4, 5, 3, inflate, false);

                                    int rightU = u + sz - 4;
                                    int rightV = v + sz - 2;
                                    ((com.voltyx.gender.render.WildfireModelRenderer)this.rightBoob).addBreastBox(rightU, rightV, 0F, 0.0F, 0F, 4, 5, 3, inflate, false);
                                }

                                if (cubeObj.has("rotation") || cubeObj.has("pivot")) {
                                    // Individual cube rotation requires a hidden sub-renderer
                                    ModelRenderer cubeRend = new ModelRenderer(this, u, v);
                                    
                                    float[] cubePivot = new float[]{ox, oy, oz}; // fallback
                                    if (cubeObj.has("pivot")) {
                                        JsonArray cp = cubeObj.getAsJsonArray("pivot");
                                        cubePivot[0] = cp.get(0).getAsFloat();
                                        cubePivot[1] = cp.get(1).getAsFloat();
                                        cubePivot[2] = cp.get(2).getAsFloat();
                                    }
                                    
                                    float relX; float relY; float relZ;
                                    if (vanillaJavaPivots.containsKey(name)) {
                                        float[] javaPivot = vanillaJavaPivots.get(name);
                                        relX = cubePivot[0] - javaPivot[0];
                                        relY = (24 - cubePivot[1]) - javaPivot[1];
                                        relZ = cubePivot[2] - javaPivot[2];
                                    } else {
                                        relX = cubePivot[0] - pivot[0];
                                        relY = pivot[1] - cubePivot[1];
                                        relZ = cubePivot[2] - pivot[2];
                                    }
                                    cubeRend.setRotationPoint(relX, relY, relZ);
                                    
                                    if (cubeObj.has("rotation")) {
                                        JsonArray r = cubeObj.getAsJsonArray("rotation");
                                        cubeRend.rotateAngleX = (float) Math.toRadians(r.get(0).getAsFloat());
                                        cubeRend.rotateAngleY = (float) Math.toRadians(r.get(1).getAsFloat());
                                        cubeRend.rotateAngleZ = (float) Math.toRadians(r.get(2).getAsFloat());
                                    }
                                    
                                    float boxX = ox - cubePivot[0];
                                    float boxY = cubePivot[1] - oy - rawSy;
                                    float boxZ = oz - cubePivot[2];
                                    
                                    String nameLower = name.toLowerCase();
                                    boolean isRightArm = nameLower.contains("rightarm");
                                    boolean isLeftArm = nameLower.contains("leftarm");
                                    
                                    float slimBoxX = boxX;
                                    float slimSx = rawSx;
                                    if (isRightArm) {
                                        slimBoxX = boxX + 1.0f;
                                        slimSx = Math.max(0, rawSx - 1.0f);
                                    } else if (isLeftArm) {
                                        slimSx = Math.max(0, rawSx - 1.0f);
                                    }

                                    if (faceUvs != null && !faceUvs.isEmpty()) {
                                        cubeRend.cubeList.add(new FloatModelBox(cubeRend, faceUvs, boxX, boxY, boxZ, rawSx, rawSy, rawSz, inflate, cubeRend.mirror));
                                    } else {
                                        cubeRend.cubeList.add(new FloatModelBox(cubeRend, u, v, boxX, boxY, boxZ, rawSx, rawSy, rawSz, inflate, cubeRend.mirror));
                                    }
                                    renderer.addChild(cubeRend);
                                    
                                    if (slimRenderer != null) {
                                        ModelRenderer slimCubeRend = new ModelRenderer(this, u, v);
                                        slimCubeRend.setRotationPoint(cubeRend.rotationPointX, cubeRend.rotationPointY, cubeRend.rotationPointZ);
                                        slimCubeRend.rotateAngleX = cubeRend.rotateAngleX;
                                        slimCubeRend.rotateAngleY = cubeRend.rotateAngleY;
                                        slimCubeRend.rotateAngleZ = cubeRend.rotateAngleZ;
                                        if (faceUvs != null && !faceUvs.isEmpty()) {
                                            slimCubeRend.cubeList.add(new FloatModelBox(slimCubeRend, faceUvs, slimBoxX, boxY, boxZ, slimSx, rawSy, rawSz, inflate, slimCubeRend.mirror));
                                        } else {
                                            slimCubeRend.cubeList.add(new FloatModelBox(slimCubeRend, u, v, slimBoxX, boxY, boxZ, slimSx, rawSy, rawSz, inflate, slimCubeRend.mirror));
                                        }
                                        slimRenderer.addChild(slimCubeRend);
                                    }
                                } else {
                                    float boxX; float boxY; float boxZ;
                                    if (vanillaJavaPivots.containsKey(name)) {
                                        float[] javaPivot = vanillaJavaPivots.get(name);
                                        boxX = ox - javaPivot[0];
                                        boxY = 24 - oy - rawSy - javaPivot[1];
                                        boxZ = oz - javaPivot[2];
                                    } else {
                                        boxX = ox - pivot[0];
                                        boxY = pivot[1] - oy - rawSy;
                                        boxZ = oz - pivot[2];
                                    }
                                    renderer.setTextureOffset(u, v);
                                    if (faceUvs != null && !faceUvs.isEmpty()) {
                                        renderer.cubeList.add(new FloatModelBox(renderer, faceUvs, boxX, boxY, boxZ, rawSx, rawSy, rawSz, inflate, renderer.mirror));
                                    } else {
                                        renderer.cubeList.add(new FloatModelBox(renderer, u, v, boxX, boxY, boxZ, rawSx, rawSy, rawSz, inflate, renderer.mirror));
                                    }
                                    
                                    if (slimRenderer != null) {
                                        String nameLower = name.toLowerCase();
                                        boolean isRightArm = nameLower.contains("rightarm");
                                        boolean isLeftArm = nameLower.contains("leftarm");
                                        float slimBoxX = boxX;
                                        float slimSx = rawSx;
                                        if (isRightArm) {
                                            slimBoxX = boxX + 1.0f;
                                            slimSx = Math.max(0, rawSx - 1.0f);
                                        } else if (isLeftArm) {
                                            slimSx = Math.max(0, rawSx - 1.0f);
                                        }
                                        slimRenderer.setTextureOffset(u, v);
                                        if (faceUvs != null && !faceUvs.isEmpty()) {
                                            slimRenderer.cubeList.add(new FloatModelBox(slimRenderer, faceUvs, slimBoxX, boxY, boxZ, slimSx, rawSy, rawSz, inflate, slimRenderer.mirror));
                                        } else {
                                            slimRenderer.cubeList.add(new FloatModelBox(slimRenderer, u, v, slimBoxX, boxY, boxZ, slimSx, rawSy, rawSz, inflate, slimRenderer.mirror));
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Link parents
                    for (JsonElement boneElem : bones) {
                        JsonObject boneObj = boneElem.getAsJsonObject();
                        String name = boneObj.get("name").getAsString();
                        if (boneObj.has("parent") && !boneMap.get(name).equals(boneMap.get(boneObj.get("parent").getAsString()))) {
                            String parentName = boneObj.get("parent").getAsString();
                            ModelRenderer parent = boneMap.get(parentName);
                            ModelRenderer child = boneMap.get(name);
                            if (parent != null && child != null) {
                                parent.addChild(child);
                            }
                            
                            ModelRenderer slimParent = slimBoneMap.get(parentName);
                            ModelRenderer slimChild = slimBoneMap.get(name);
                            if (slimParent != null && slimChild != null) {
                                slimParent.addChild(slimChild);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public EntityEquipmentSlot currentSlot = EntityEquipmentSlot.HEAD;
    public ModelBiped syncedModel = null;

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, net.minecraft.entity.Entity entityIn) {
        if (entityIn != null) {
            super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        }
        
        if (this.syncedModel != null) {
            this.bipedRightArm.rotateAngleX = this.syncedModel.bipedRightArm.rotateAngleX;
            this.bipedRightArm.rotateAngleY = this.syncedModel.bipedRightArm.rotateAngleY;
            this.bipedRightArm.rotateAngleZ = this.syncedModel.bipedRightArm.rotateAngleZ;
            this.bipedRightArm.rotationPointX = this.syncedModel.bipedRightArm.rotationPointX;
            this.bipedRightArm.rotationPointY = this.syncedModel.bipedRightArm.rotationPointY;
            this.bipedRightArm.rotationPointZ = this.syncedModel.bipedRightArm.rotationPointZ;
            this.bipedRightArm.offsetX = this.syncedModel.bipedRightArm.offsetX;
            this.bipedRightArm.offsetY = this.syncedModel.bipedRightArm.offsetY;
            this.bipedRightArm.offsetZ = this.syncedModel.bipedRightArm.offsetZ;

            this.bipedLeftArm.rotateAngleX = this.syncedModel.bipedLeftArm.rotateAngleX;
            this.bipedLeftArm.rotateAngleY = this.syncedModel.bipedLeftArm.rotateAngleY;
            this.bipedLeftArm.rotateAngleZ = this.syncedModel.bipedLeftArm.rotateAngleZ;
            this.bipedLeftArm.rotationPointX = this.syncedModel.bipedLeftArm.rotationPointX;
            this.bipedLeftArm.rotationPointY = this.syncedModel.bipedLeftArm.rotationPointY;
            this.bipedLeftArm.rotationPointZ = this.syncedModel.bipedLeftArm.rotationPointZ;
            this.bipedLeftArm.offsetX = this.syncedModel.bipedLeftArm.offsetX;
            this.bipedLeftArm.offsetY = this.syncedModel.bipedLeftArm.offsetY;
            this.bipedLeftArm.offsetZ = this.syncedModel.bipedLeftArm.offsetZ;
        }
    }

    @Override
    public void render(net.minecraft.entity.Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entityIn != null) {
            this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        }
        boolean isSlim = false;
        if (entityIn instanceof net.minecraft.client.entity.AbstractClientPlayer) {
            isSlim = "slim".equals(((net.minecraft.client.entity.AbstractClientPlayer)entityIn).getSkinType());
        }

        this.bipedHead.showModel = this.currentSlot == EntityEquipmentSlot.HEAD;
        this.bipedHeadwear.showModel = this.currentSlot == EntityEquipmentSlot.HEAD;
        this.bipedBody.showModel = this.currentSlot == EntityEquipmentSlot.CHEST;
        this.bipedRightArm.showModel = !isSlim && (this.currentSlot == EntityEquipmentSlot.CHEST);
        this.bipedLeftArm.showModel = !isSlim && (this.currentSlot == EntityEquipmentSlot.CHEST || this.currentSlot == null);
        this.bipedRightLeg.showModel = this.currentSlot == EntityEquipmentSlot.LEGS || this.currentSlot == EntityEquipmentSlot.FEET;
        this.bipedLeftLeg.showModel = this.currentSlot == EntityEquipmentSlot.LEGS || this.currentSlot == EntityEquipmentSlot.FEET;

        // Ensure sub-bones attached to legs (like armorLeftLeg vs armorLeftBoot) are correctly filtered per slot
        for (Map.Entry<String, ModelRenderer> entry : this.boneMap.entrySet()) {
            String bName = entry.getKey().toLowerCase();
            ModelRenderer r = entry.getValue();
            if (r != null && !bName.startsWith("biped")) {
                if (bName.contains("boot") || bName.contains("shoe")) {
                    r.showModel = (this.currentSlot == EntityEquipmentSlot.FEET);
                } else if (bName.contains("leg") || bName.contains("pant")) {
                    r.showModel = (this.currentSlot == EntityEquipmentSlot.LEGS);
                }
            }
        }
        for (Map.Entry<String, ModelRenderer> entry : this.slimBoneMap.entrySet()) {
            String bName = entry.getKey().toLowerCase();
            ModelRenderer r = entry.getValue();
            if (r != null && !bName.startsWith("biped")) {
                if (bName.contains("boot") || bName.contains("shoe")) {
                    r.showModel = (this.currentSlot == EntityEquipmentSlot.FEET);
                } else if (bName.contains("leg") || bName.contains("pant")) {
                    r.showModel = (this.currentSlot == EntityEquipmentSlot.LEGS);
                }
            }
        }

        if (this.syncedModel != null) {
            this.bipedHead.rotateAngleX = this.syncedModel.bipedHead.rotateAngleX;
            this.bipedHead.rotateAngleY = this.syncedModel.bipedHead.rotateAngleY;
            this.bipedHead.rotateAngleZ = this.syncedModel.bipedHead.rotateAngleZ;
            this.bipedHead.rotationPointX = this.syncedModel.bipedHead.rotationPointX;
            this.bipedHead.rotationPointY = this.syncedModel.bipedHead.rotationPointY;
            this.bipedHead.rotationPointZ = this.syncedModel.bipedHead.rotationPointZ;

            this.bipedBody.rotateAngleX = this.syncedModel.bipedBody.rotateAngleX;
            this.bipedBody.rotateAngleY = this.syncedModel.bipedBody.rotateAngleY;
            this.bipedBody.rotateAngleZ = this.syncedModel.bipedBody.rotateAngleZ;
            this.bipedBody.rotationPointX = this.syncedModel.bipedBody.rotationPointX;
            this.bipedBody.rotationPointY = this.syncedModel.bipedBody.rotationPointY;
            this.bipedBody.rotationPointZ = this.syncedModel.bipedBody.rotationPointZ;

            this.bipedRightArm.rotateAngleX = this.syncedModel.bipedRightArm.rotateAngleX;
            this.bipedRightArm.rotateAngleY = this.syncedModel.bipedRightArm.rotateAngleY;
            this.bipedRightArm.rotateAngleZ = this.syncedModel.bipedRightArm.rotateAngleZ;
            this.bipedRightArm.rotationPointX = this.syncedModel.bipedRightArm.rotationPointX;
            this.bipedRightArm.rotationPointY = this.syncedModel.bipedRightArm.rotationPointY;
            this.bipedRightArm.rotationPointZ = this.syncedModel.bipedRightArm.rotationPointZ;
            this.bipedRightArm.offsetX = this.syncedModel.bipedRightArm.offsetX;
            this.bipedRightArm.offsetY = this.syncedModel.bipedRightArm.offsetY;
            this.bipedRightArm.offsetZ = this.syncedModel.bipedRightArm.offsetZ;

            this.bipedLeftArm.rotateAngleX = this.syncedModel.bipedLeftArm.rotateAngleX;
            this.bipedLeftArm.rotateAngleY = this.syncedModel.bipedLeftArm.rotateAngleY;
            this.bipedLeftArm.rotateAngleZ = this.syncedModel.bipedLeftArm.rotateAngleZ;
            this.bipedLeftArm.rotationPointX = this.syncedModel.bipedLeftArm.rotationPointX;
            this.bipedLeftArm.rotationPointY = this.syncedModel.bipedLeftArm.rotationPointY;
            this.bipedLeftArm.rotationPointZ = this.syncedModel.bipedLeftArm.rotationPointZ;
            this.bipedLeftArm.offsetX = this.syncedModel.bipedLeftArm.offsetX;
            this.bipedLeftArm.offsetY = this.syncedModel.bipedLeftArm.offsetY;
            this.bipedLeftArm.offsetZ = this.syncedModel.bipedLeftArm.offsetZ;

            this.bipedRightLeg.rotateAngleX = this.syncedModel.bipedRightLeg.rotateAngleX;
            this.bipedRightLeg.rotateAngleY = this.syncedModel.bipedRightLeg.rotateAngleY;
            this.bipedRightLeg.rotateAngleZ = this.syncedModel.bipedRightLeg.rotateAngleZ;
            this.bipedRightLeg.rotationPointX = this.syncedModel.bipedRightLeg.rotationPointX;
            this.bipedRightLeg.rotationPointY = this.syncedModel.bipedRightLeg.rotationPointY;
            this.bipedRightLeg.rotationPointZ = this.syncedModel.bipedRightLeg.rotationPointZ;

            this.bipedLeftLeg.rotateAngleX = this.syncedModel.bipedLeftLeg.rotateAngleX;
            this.bipedLeftLeg.rotateAngleY = this.syncedModel.bipedLeftLeg.rotateAngleY;
            this.bipedLeftLeg.rotateAngleZ = this.syncedModel.bipedLeftLeg.rotateAngleZ;
            this.bipedLeftLeg.rotationPointX = this.syncedModel.bipedLeftLeg.rotationPointX;
            this.bipedLeftLeg.rotationPointY = this.syncedModel.bipedLeftLeg.rotationPointY;
            this.bipedLeftLeg.rotationPointZ = this.syncedModel.bipedLeftLeg.rotationPointZ;
        }

        GlStateManager.pushMatrix();
        if (this.isChild) {
            float f = 2.0F;
            GlStateManager.scale(1.5F / f, 1.5F / f, 1.5F / f);
            GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
            this.bipedHead.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            this.bipedBody.render(scale);
            if (!isSlim) {
                this.bipedRightArm.render(scale);
                this.bipedLeftArm.render(scale);
            }
            this.bipedRightLeg.render(scale);
            this.bipedLeftLeg.render(scale);
            this.bipedHeadwear.render(scale);
        } else {
            if (entityIn != null && entityIn.isSneaking()) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }

            this.bipedHead.render(scale);
            this.bipedBody.render(scale);
            if (!isSlim) {
                this.bipedRightArm.render(scale);
                this.bipedLeftArm.render(scale);
            }
            this.bipedRightLeg.render(scale);
            this.bipedLeftLeg.render(scale);
            this.bipedHeadwear.render(scale);
        }
        GlStateManager.popMatrix();

        if (isSlim) {
            GlStateManager.pushMatrix();

            if (this.isChild) {
                GlStateManager.scale(0.5F, 0.5F, 0.5F);
                GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            } else {
                if (entityIn != null && entityIn.isSneaking()) {
                    GlStateManager.translate(0.0F, 0.2F, 0.0F);
                }
            }

            // Sync angles right before rendering to capture animation mods' injections
            this.bipedRightArmSlim.rotationPointX = this.bipedRightArm.rotationPointX;
            this.bipedRightArmSlim.rotationPointY = this.bipedRightArm.rotationPointY;
            this.bipedRightArmSlim.rotationPointZ = this.bipedRightArm.rotationPointZ;
            this.bipedRightArmSlim.rotateAngleX = this.bipedRightArm.rotateAngleX;
            this.bipedRightArmSlim.rotateAngleY = this.bipedRightArm.rotateAngleY;
            this.bipedRightArmSlim.rotateAngleZ = this.bipedRightArm.rotateAngleZ;
            this.bipedRightArmSlim.offsetX = this.bipedRightArm.offsetX;
            this.bipedRightArmSlim.offsetY = this.bipedRightArm.offsetY;
            this.bipedRightArmSlim.offsetZ = this.bipedRightArm.offsetZ;

            this.bipedLeftArmSlim.rotationPointX = this.bipedLeftArm.rotationPointX;
            this.bipedLeftArmSlim.rotationPointY = this.bipedLeftArm.rotationPointY;
            this.bipedLeftArmSlim.rotationPointZ = this.bipedLeftArm.rotationPointZ;
            this.bipedLeftArmSlim.rotateAngleX = this.bipedLeftArm.rotateAngleX;
            this.bipedLeftArmSlim.rotateAngleY = this.bipedLeftArm.rotateAngleY;
            this.bipedLeftArmSlim.rotateAngleZ = this.bipedLeftArm.rotateAngleZ;
            this.bipedLeftArmSlim.offsetX = this.bipedLeftArm.offsetX;
            this.bipedLeftArmSlim.offsetY = this.bipedLeftArm.offsetY;
            this.bipedLeftArmSlim.offsetZ = this.bipedLeftArm.offsetZ;

            if (this.currentSlot == null) {
                this.bipedLeftArmSlim.render(scale);
            } else if (this.currentSlot == EntityEquipmentSlot.CHEST) {
                this.bipedRightArmSlim.render(scale);
                this.bipedLeftArmSlim.render(scale);
            }

            GlStateManager.popMatrix();
        }
    }
}
