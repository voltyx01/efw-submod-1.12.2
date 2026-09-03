import org.jetbrains.gradle.ext.settings

plugins {
    id("com.gtnewhorizons.retrofuturagradle") version "1.4.9"
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.8"
}

group = "efw"
version = "1.0"

val id = "efw"

minecraft {
    mcVersion = "1.12.2"
    username = "Voltyx_"
    extraRunJvmArguments.addAll(listOf(
        "-Dfml.coreMods.load=efw.core.EFWCorePlugin,com.paneedah.mwc.asm.MWCPlugin"
    ))
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spongepowered.org/maven") }
    maven { url = uri("https://maven.blamejared.com") }
    maven { url = uri("https://cursemaven.com") }
    exclusiveContent {
        forRepository {
            maven {
                name = "Curse Maven"
                url = uri("https://cursemaven.com")
            }
        }
        filter {
            includeGroup("curse.maven")
        }
    }
    maven {
        name = "Cleanroom"
        url = uri("https://repo.cleanroommc.com/releases")
    }
}

dependencies {
    implementation(rfg.deobf(files("Modern-Warfare-Cubed-0.1.9.jar")))
    implementation(rfg.deobf("curse.maven:quark-243121:2924091"))
    implementation(rfg.deobf("vazkii.autoreglib:AutoRegLib:1.3-32.33"))
    implementation(rfg.deobf("curse.maven:paragliders-289240:2816556"))
    implementation(rfg.deobf("curse.maven:ydms-weapon-master-608235:5017279"))
    implementation(rfg.deobf("curse.maven:jei-238222:5846804"))

    // Папка lib (если она есть)
    implementation(fileTree(mapOf("dir" to "lib", "include" to listOf("*.jar"))))
    implementation(rfg.deobf(files("libs/aquaacrobatics.jar")))
    // Папка libs: загружаем всё, КРОМЕ паразитов и Simplest (чтобы избежать дублей)
    implementation(fileTree(mapOf(
        "dir" to "libs", 
        "include" to listOf("*.jar"), 
        "exclude" to listOf("Simplest.jar", "Simplest-noshade.jar", "Simplest2.jar", "SRParasites-1.10.7.jar", "aquaacrobatics.jar", "xaerominimap-forge-1.12.2-26.4.2.jar", "xaeroworldmap-forge-1.12.2-1.45.0.jar")
    )))

    // --- ПРАВИЛЬНАЯ ЗАГРУЗКА ЛОКАЛЬНЫХ МОДОВ ЧЕРЕЗ RFG ---
    // Используем rfg.deobf(files(...)), чтобы RFG расшифровал мод!
    // Я ставлю implementation, чтобы паразиты загружались у тебя в Dev-клиенте для тестов. 
    // Если они нужны ТОЛЬКО для компиляции миксина, замени implementation на compileOnly
    implementation(rfg.deobf(files("libs/SRParasites-1.10.7.jar")))
    implementation(rfg.deobf(files("libs/xaerominimap-forge-1.12.2-26.4.2.jar")))
    implementation(rfg.deobf(files("libs/xaeroworldmap-forge-1.12.2-1.45.0.jar")))
    
    // Остальные локальные моды (если для них тоже нужны миксины/деобфускация, оберни в rfg.deobf)
    // compileOnly(files("libs/aquaacrobatics.jar"))
    //compileOnly(files("libs/Simplest-noshade.jar"))

    // --- MIXIN BOOTER ---
    val mixinBooter = modUtils.enableMixins("zone.rong:mixinbooter:8.6", "efw.mixins.refmap.json")
    implementation(mixinBooter)
    
    compileOnly("org.spongepowered:mixin:0.8.5:processor")
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
}

// ДОБАВЬТЕ ЭТОТ БЛОК ДЛЯ АВТОМАТИЗАЦИИ REFMAP:
tasks.withType<JavaCompile> {
    options.compilerArgs.add("-AoutRefMapFile=efw.mixins.refmap.json")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }
}

sourceSets {
    main {
        output.setResourcesDir(java.classesDirectory)
    }
}

val at = project.files("src/main/resources/META-INF/accesstransformer.cfg")

tasks.deobfuscateMergedJarToSrg.configure {
    accessTransformerFiles.from(at)
}

tasks.srgifyBinpatchedJar.configure {
    accessTransformerFiles.from(at)
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    manifest {
        attributes(
            "FMLCorePluginContainsFMLMod" to "true",
            "ForceLoadAsMod" to "true",
            "FMLCorePlugin" to "efw.core.EFWCorePlugin",
            "FMLAT" to "accesstransformer.cfg"
        )
    }
}
tasks.withType<JavaCompile> { options.encoding = "UTF-8" }

tasks.register("deployToParent") {
    doLast {
        val mwcvDir = file("MWCV")
        val rootJar = file("Modern-Warfare-Cubed-0.1.9.jar")
        val classesDir = mwcvDir.resolve("build/classes/java/main")
        
        exec {
            workingDir = mwcvDir
            commandLine(if (System.getProperty("os.name").toLowerCase().contains("win")) listOf("cmd", "/c", "gradlew.bat", "compileJava") else listOf("./gradlew", "compileJava"))
        }
        if (rootJar.exists() && classesDir.exists()) {
            exec {
                commandLine("jar", "uf", rootJar.absolutePath, "-C", classesDir.absolutePath, ".")
            }
            println(">>> Successfully injected MWCV classes into Modern-Warfare-Cubed-0.1.9.jar!")
        }
    }
}
