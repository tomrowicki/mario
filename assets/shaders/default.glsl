#type vertex
#version 330 core
layout (location=0) in vec3 aPos; // "a" stands for "attribute"
layout (location=1) in vec4 aColor; // in means it's an input variable - passed from somewhere (java code)

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor; // f for fragment; out because it's passed further to fragment shader

void main() {
    fColor = aColor;
    gl_Position = uProjection * uView * vec4(aPos, 1.0); // establishes the position of the geometrical shape
}

#type fragment
#version 330 core

uniform float uTime;

in vec4 fColor;

out vec4 color;

void main () {
    // color = sin(uTime) * fColor; // establishes the colours of pixels (4) within the geometrical shape; pixels between get shaded based on proximity to the 4
    float avg = (fColor.r + fColor.g + fColor.b) / 3;
    color = vec4(avg, avg, avg, 1);
}