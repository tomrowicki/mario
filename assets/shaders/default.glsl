#type vertex
#version 330 core
layout (location=0) in vec3 aPos;// "a" stands for "attribute"
layout (location=1) in vec4 aColor;// in means it's an input variable - passed from somewhere (java code)
layout (location=2) in vec2 aTexCoords;
layout (location=3) in float aTexId;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;// f for fragment; out because it's passed further to fragment shader
out vec2 fTexCoords;
out float fTexId;

void main() {
    fColor = aColor;
    fTexCoords = aTexCoords;
    fTexId = aTexId;
    gl_Position = uProjection * uView * vec4(aPos, 1.0);// establishes the position of the geometrical shape
}

#type fragment
#version 330 core

in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;

uniform sampler2D uTextures[8];

out vec4 color;

void main () {
    // color = sin(uTime) * fColor; // establishes the colours of pixels (4) within the geometrical shape; pixels between get shaded based on proximity to the 4
    //    float avg = (fColor.r + fColor.g + fColor.b) / 3;
    //    color = vec4(avg, avg, avg, 1);
    if (fTexId > 0) {
        int id = int(fTexId);
        color = fColor * texture(uTextures[id], fTexCoords);
//        color = vec4(fTexCoords, 0, 1); // (x, y, 0, 1)
    } else {
        color = fColor;
    }
}