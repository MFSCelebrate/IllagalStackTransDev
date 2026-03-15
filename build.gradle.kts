import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.api.attributes.java.TargetJvmVersion

plugins {
    java
    `java-library`
    idea
    eclipse
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.18"
    id("com.github.johnrengelman.shadow") version "8.1.1" // 用于打包 Mixin 库
}

repositories {
    mavenCentral() // 官方仓库作为后备
    maven {
        name = "OSS Sonatype"
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    maven {
        name = "PaperMC"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "ProtocolLib"
        url = uri("https://repo.dmulloy2.net/repository/public/")
    }
    maven {
        name = "Magic"
        url = uri("https://maven.elmakers.com/repository/")
    }
    maven {
        name = "CodeMC"
        url = uri("https://repo.codemc.org/repository/maven-public/")
    }
    maven {
        name = "JitPack"
        url = uri("https://jitpack.io")
    }
    maven { // Mixin 仓库
        name = "SpongePowered"
        url = uri("https://repo.spongepowered.org/maven/")
    }
}

dependencies {
    compileOnly("dev.folia:folia-api:1.20.4-R0.1-SNAPSHOT")
    paperweight.paperDevBundle("1.21.11-R0.1-SNAPSHOT")
    
    compileOnly("com.comphenix.protocol:ProtocolLib:5.3.0")
    compileOnly("com.elmakers.mine.bukkit:MagicAPI:10.2")
    compileOnly("de.tr7zw:item-nbt-api-plugin:2.8.0")
    compileOnly("com.github.TheBusyBiscuit:Slimefun4:RC-30") { isTransitive = false }
    compileOnly("io.netty:netty-all:4.1.110.Final") {
        because("The version aligns with the version used by Minecraft itself. " +
                "The minecraft server ships netty as well, so we don't need to include it in the jar.")
    }
    compileOnly("com.gmail.nossr50.mcMMO:mcMMO:2.1.217") { isTransitive = false }
    
    compileOnly("fr.minuskube.inv:smart-invs:1.2.7") {
        exclude(group = "org.spigotmc", module = "spigot-api")
    }
    compileOnly("com.github.brcdev-minecraft:shopgui-api:3.0.0") {
        exclude(group = "org.spigotmc", module = "spigot-api")
    }

    // Mixin 相关依赖（编译时使用，运行时由 Ignite 提供）
    compileOnly("org.spongepowered:mixin:0.8.7")
    annotationProcessor("org.spongepowered:mixin:0.8.7")

    // Gson 依赖（同时用于编译和注解处理）
    implementation("com.google.code.gson:gson:2.10.1")
    annotationProcessor("com.google.code.gson:gson:2.10.1")

    // Guava 依赖（用于注解处理）
    annotationProcessor("com.google.guava:guava:33.5.0-jre")

    // 新增：为注解处理器添加 ASM 依赖
    annotationProcessor("org.ow2.asm:asm-util:9.8")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

configurations.all {
    attributes {
        attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 21)
    }
    // 确保依赖解析时优先使用指定版本（如有冲突）
    resolutionStrategy {
        force("com.google.code.gson:gson:2.10.1")
    }
}

tasks.compileJava {
    options.release = 21
    options.encoding = "UTF-8"
    doFirst {
        println("Compile classpath: ${classpath.files.joinToString("\n")}")
    }
}

version = "3.1.0-IllagalStackTrans"

tasks.processResources {
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}

// 配置 Shadow 打包
tasks.shadowJar {
    // 关键修改：移除 dependsOn(tasks.reobfJar)
    // 我们不再显式声明依赖，而是依靠 Paperweight 插件自身的逻辑确保 reobfJar 在 shadowJar 之前运行。
    // 如果 Paperweight 的设计导致 reobfJar 和 assemble/build 有循环依赖，我们主动切断它。
    archiveClassifier.set("")
    
}

// 重要：我们不再让 assemble 或 build 任务依赖 shadowJar。
// 如果您需要生成最终的插件 JAR，请直接运行 `./gradlew shadowJar` 命令。
// 这样，Gradle 就不会在标准构建生命周期中尝试解决循环依赖。
// 如果您仍然希望通过 `./gradlew build` 触发 shadowJar，可以尝试下面的配置，但风险较高：
/*
tasks.build {
    dependsOn(tasks.shadowJar)
}
*/
