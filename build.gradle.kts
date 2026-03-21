import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.api.attributes.java.TargetJvmVersion

plugins {
    java
    `java-library`
    idea
    eclipse
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.18"
    id("com.github.johnrengelman.shadow") version "8.1.1" // 用于打包 ASM 等依赖
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
    compileOnly("org.spongepowered:mixin:0.8.7")
    annotationProcessor("org.spongepowered:mixin:0.8.7")
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
    resolutionStrategy {
        force("com.google.code.gson:gson:2.10.1")
    }
}

tasks.compileJava {
    options.release = 21
    options.encoding = "UTF-8"
    classpath = classpath + configurations.compileClasspath.get()
}

version = "3.1.0-IllagalStackTrans"

tasks.processResources {
    // 改为处理 paper-plugin.yml（如果仍使用 plugin.yml 则保留，建议使用 paper-plugin.yml）
    filesMatching("paper-plugin.yml") {
        expand("version" to project.version)
    }
}

tasks.shadowJar {
    archiveClassifier.set("")
    // 注释掉 relocate，避免解析 Java 21 类文件
    // relocate("org.objectweb.asm", "main.java.me.dniym.libs.asm")
    
    // 可选：排除不必要的依赖，但 ASM 会被保留
    exclude {
        it.name.contains("paper-api") ||
        it.name.contains("folia-api") ||
        it.name.contains("netty-all") ||
        // 可根据需要继续添加其他排除项
        false  // 默认不排除，只排除上面列出的
    }
}
