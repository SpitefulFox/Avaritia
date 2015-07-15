uniform sampler2D texture0;
uniform vec3 lightlevel;

void main (void) 
{
    vec4 color;
    vec4 light = gl_Color;
    vec4 mask = texture2D(texture0, gl_TexCoord[0].xy);
    light.rgb *= lightlevel;
    
    color = vec4(1.0,0.0,0.0,1.0);
 
    color.a *= mask.r;
    vec3 shade = light.rgb * 0.1 + vec3(0.9,0.9,0.9);
    color.rgb *= shade;
	color = clamp(color,0.0,1.0);
	
    gl_FragColor = color;
}