#version 120

uniform sampler2D tex;
uniform sampler2D lightmap;

varying vec2 texCoord;



varying vec2 lightmapTexCoord;
varying vec3 lightSum;
varying float outAlpha;

void main(){
	vec4 primary = texture2D(tex, texCoord);
	vec4 light = texture2D(lightmap, lightmapTexCoord);
	gl_FragColor = vec4(primary.rgb * lightSum * light.rgb, 1.0);

	//gl_FragColor = texture2D(tex, texCoord);
}
