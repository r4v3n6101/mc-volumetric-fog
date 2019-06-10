#version 120

varying vec2 texcoord;

uniform sampler2D scene;
uniform sampler2D back;
uniform sampler2D front;

const vec3 fog_color = vec3(0, 1, 0);// TODO : not hard-coded
const float fog_density = 500;

void main() {
    float scene_depth = texture2D(scene, texcoord).r;
    float fog_back = texture2D(back, texcoord).r;
    float fog_front = texture2D(front, texcoord).r;

    float k = min(fog_back, scene_depth) - fog_front;
    gl_FragData[0] = vec4(fog_color, fog_density * k);
}
