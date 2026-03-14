import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainSpec
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
    mavenCentral()
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

    // Mixin 相关依赖
    compileOnly("org.spongepowered:mixin:0.8.7")
    annotationProcessor("org.spongepowered:mixin:0.8.7")
    implementation("org.spongepowered:mixin:0.8.7")

    // 关键：Gson 依赖（必须添加）
    implementation("com.google.code.gson:gson:2.10.1")
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
}

tasks.compileJava {
    options.release = 21
    options.encoding = "UTF-8"
}

version = "3.0.00-Preview ExtraVer Jre21"

tasks.processResources {
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}

// 配置 Shadow 打包
tasks.shadowJar {
    archiveClassifier.set("") // 替换默认的 jar（无 classifier）
    // 将 Mixin 库重新打包到你的包名下，避免与其他插件冲突
    relocate("org.spongepowered.asm", "main.java.me.dniym.mixin.asm")
}

tasks.assemble {
    dependsOn(tasks.reobfJar, tasks.shadowJar) // 同时生成 reobf 和 shadow JAR
}
