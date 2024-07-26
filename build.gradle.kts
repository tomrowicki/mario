plugins {
    id("java")
}

group = "tomrowicki"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

tasks.test {
    useJUnitPlatform()
}

val lwjglVersion = "3.2.3"
val jomlVersion = "1.10.6"
val lwjglNatives = "natives-windows"
val imguiVersion = "1.77-0.17.1"

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-assimp")
    implementation("org.lwjgl", "lwjgl-glfw")
    implementation("org.lwjgl", "lwjgl-nfd")
    implementation("org.lwjgl", "lwjgl-openal")
    implementation("org.lwjgl", "lwjgl-opengl")
    implementation("org.lwjgl", "lwjgl-stb")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-assimp", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-nfd", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-openal", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = lwjglNatives)
    implementation("org.joml", "joml", jomlVersion)

    // ImGUI stuff
    implementation ("io.imgui.java:binding:$imguiVersion")
    implementation ("io.imgui.java:lwjgl3:$imguiVersion")

    // Include all available natives, but it's likely that you want something specific
    runtimeOnly ("io.imgui.java:natives-windows:$imguiVersion")
    runtimeOnly ("io.imgui.java:natives-windows-x86:$imguiVersion")

    implementation ("com.google.code.gson:gson:2.11.0")
}