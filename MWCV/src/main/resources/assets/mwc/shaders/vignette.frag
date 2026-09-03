#version 120

/*
Renders scopes
Author: Homer Riva-Cambrin
Version: October 2nd, 2022

CHANGES:
- Supports fog in scopes
- Optimized, deleted unused variables
- Changed texScale to constant, made it inverse to avoid divison
- Changed getNoise to vec2 as 3rd dimension was never used
- Reduced divison operators
*/

// General constants
const float PI = 3.14;
const vec2 SCREEN_CENTER = vec2(0.5, 0.5);

// Samplers
uniform sampler2D reticle;
uniform sampler2D tex0;
uniform sampler2D dirt;
uniform sampler2D depthTex;

// Tex-coord data
varying vec2 texCoord;

// Parameters for the radial split effect
const int RADIAL_SPLIT_MULTIPLIER = 5;
const vec3 ConvergeX = vec3(-1.0,  0.0, 0.5);
const vec3 ConvergeY = vec3( 0.0, -1.0, 0.5);
const vec3 RadialConvergeX = vec3(1.0, 1.0, 1.0);
const vec3 RadialConvergeY = vec3(1.0, 1.0, 1.0);

// Sets the texture scale of the reticle (1 / 0.04)
const float texScale = 25;

// Tells the shader how far to zoom the reticle in (nice effect)
uniform float reticleZoom = 1.0;
uniform float actualZoom;

// Varying vectors for the reticle parallax effect
varying vec4 posi;
varying vec3 normal;
varying vec3 tangent;

// Night vision uniforms
uniform bool isNightVisionOn;
uniform bool useWhitePhosphor;
uniform float time;

// Forced dark mode for unequip / slot switch / hipfire
uniform bool isForcedDark;
uniform float darkAlpha;

// Fog settings
uniform bool shouldDoFog;
uniform vec3 baseFogColor;
uniform float fogIntensity;

const float SCOPE_RADIUS = 0.46;

// NV constants (Victor Matskiv Sr. [from his original nightvision shader])
const float NIGHTVISION_NOISE_AMP = 4.0;
const float NV_INTENSITY_ADJUST = 20.0;
const float NV_CONTRAST = 0.8;
const vec3 NV_LUMINOSITY_VEC = vec3(0.30, 0.59, 0.11);

const float FISHEYE_INTENSITY = 0.6;

/*
* Linearizes the depth buffer (stored as a logarithmic
* function typically for higher precision close up)
*/
float linearize_depth(float d,float zNear,float zFar)
{
    return zNear * zFar / (zFar + d * (zNear - zFar));
}

/*
* Simple fog from Inigo Quilez's phenomenal blog
* [https://iquilezles.org/articles/fog/]
*/
vec4 applyFog(vec4 color, vec2 tex_coord) {
  float linearizedDepth = linearize_depth(texture2D(depthTex, tex_coord).r, 0.004, 100);
  float fogAmount = 1.0 - exp( -linearizedDepth*fogIntensity );
  vec4  fogColor  = vec4(baseFogColor, 1.0);
  return mix( color, fogColor, fogAmount );
}

/**
* Radial Split Effect - taken from MC's shaders.
**/
vec4 radialSplit(vec2 tc, float deconvergent) {

    // Modification in order to increase the intensity
    // based on the distance from the center.
	  deconvergent *= length(tc-SCREEN_CENTER)*RADIAL_SPLIT_MULTIPLIER;

    vec3 CoordX = tc.x * RadialConvergeX;
    vec3 CoordY = tc.y * RadialConvergeY;

    CoordX += ConvergeX * deconvergent - (RadialConvergeX - 1.0) * 0.5;
    CoordY += ConvergeY * deconvergent - (RadialConvergeY - 1.0) * 0.5;

    float red   = texture2D(tex0, vec2(CoordX.x, CoordY.x)).r;
    float green = texture2D(tex0, vec2(CoordX.y, CoordY.y)).g;
    float blue  = texture2D(tex0, vec2(CoordX.z, CoordY.z)).b;

    return vec4(red, green, blue, 1.0);
}

/**
* Code for creating a parallax effect, from the reflex.frag shader.
*
* Article: https://vazgriz.com/158/reflex-sight-shader-in-unity3d/
*/
vec2 getReticleCoords() {
    vec3 cameraDir = normalize(posi.xyz);
    vec3 offset = cameraDir + normal;

    mat3 mat = mat3(tangent, cross(normal, tangent), normal);
    offset = mat*offset;

    vec2 uv = offset.yx*texScale;
    return uv;
}

/**
* Get single sample of noise.
* (float) timer - continously changing float that acts as a random seed
*
* Source - https://stackoverflow.com/questions/4200224/random-noise-functions-for-glsl
*/
float getNSingle(float timer) {
	return (fract(sin(dot(texCoord, vec2(12.9898,78.233)*(2.0+timer))) * 43758.5453));
}

/**
* Gets a 3D noise vector by taking 3 single samples
*/
vec2 getNoise(float timer) {
	return vec2(getNSingle(timer), getNSingle(timer));
}

/**
*  Applies a night vision effect to the fragment
*
*  Updated code of Victor Matskiv Sr.
*/
vec4 doNightVision(vec4 color) {
      // Apply noise to night-vision
      vec2 uv;
      uv.x = 0.35 * sin(time * 10);
      uv.y = 0.35 * cos(time * 10);
      vec2 noise = getNoise(time) * NIGHTVISION_NOISE_AMP;
      color.xy += noise.xy * 0.005;

      // Find color intensity
      float intensity = dot(NV_LUMINOSITY_VEC, color.rgb);
      intensity = clamp(NV_CONTRAST * (intensity - 0.5) + 0.5, 0.0, 1.0);
      float colorComponent = clamp(intensity / 0.59, 0.0, 1.0) * NV_INTENSITY_ADJUST;

      // Modify the color
      vec4 visionColor;
      if(useWhitePhosphor) {
        visionColor = vec4(colorComponent * 0.7, colorComponent * 0.7, colorComponent, 1.0);
      } else {
        visionColor = vec4(colorComponent * 0.7, colorComponent, colorComponent * 0.7, 1.0);
      }

      // Mix base color and use visionColor as a filter (?)
      float gray = dot(color.rgb, vec3(0.299, 0.587, 0.114));
      vec4 grayColor = vec4(gray, gray, gray, 1.0);
      color = grayColor * visionColor;

      return color;
}

void main(){
      if (isForcedDark || darkAlpha >= 0.999) {
            float dist = distance(texCoord.xy, SCREEN_CENTER);
            float mask = smoothstep(SCOPE_RADIUS, SCOPE_RADIUS - 0.01, dist);
            gl_FragColor = vec4(0.0, 0.0, 0.0, mask);
            return;
      }

      // Our boilerplate color
      vec4 newColor = vec4(0);

      // Get reticle coordinates
      vec2 uv = getReticleCoords();

      // Distort the screen coordinates in order to create a distortion effect
      vec2 centeredTexCoords = texCoord - SCREEN_CENTER;
      vec2 distortedTexCoords = cos(min((length(centeredTexCoords)*FISHEYE_INTENSITY), 1.0)* PI * 0.5) * centeredTexCoords + SCREEN_CENTER;

      // Use a radial split w/ the distorted coordinates
      newColor = radialSplit(distortedTexCoords, 0.001);
      if(shouldDoFog) newColor = applyFog(newColor, distortedTexCoords);

      // Apply night vision
      if(isNightVisionOn) newColor = doNightVision(newColor);

      // Mix in the reticle
      vec2 distortedTexBorder = cos(min((length(centeredTexCoords)), 1.0)*PI*0.5) * uv;
      vec4 reticleFragment = texture2D(reticle, distortedTexBorder*0.05*reticleZoom + SCREEN_CENTER);
      newColor = mix(newColor, reticleFragment, reticleFragment.a);

      // Smoothly dissolve black screen into the zoom picture based on darkAlpha
      newColor.rgb = mix(newColor.rgb, vec3(0.0, 0.0, 0.0), clamp(darkAlpha, 0.0, 1.0));

      // Circular lens crop (clean circular shape without any dirt or darkening vignette)
      float lensDist = distance(texCoord.xy, SCREEN_CENTER);
      float mask = smoothstep(SCOPE_RADIUS, SCOPE_RADIUS - 0.01, lensDist);
      newColor.a = mask;

      gl_FragColor = newColor;
}
