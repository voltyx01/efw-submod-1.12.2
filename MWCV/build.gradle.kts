import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.jetbrains.gradle.ext.Gradle
import org.jetbrains.gradle.ext.runConfigurations
import org.jetbrains.gradle.ext.settings

plugins {
    id("com.gtnewhorizons.retrofuturagradle") version "1.4.9"
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.8"
    id("com.github.gmazzo.buildconfig") version "5.3.5"
}

group = "com.paneedah"
version = "0.1.9"

val id = "mwc"
val plugin = "${project.group}.${id}.asm.MWCPlugin"

val groovyScriptVersion = "1.0.0"
val mixinBooterVersion = "8.6"

minecraft {
    mcVersion = "1.12.2"
    username = "Desoroxxx"
    extraRunJvmArguments = listOf("-Dforge.logging.console.level=debug", "-Dfml.coreMods.load=${plugin}", "-Dmixin.hotSwap=true", "-Dmixin.checks.mixininterfaces=true", "-Dmixin.debug.export=true")

    injectedTags.put("ID", id)
    injectedTags.put("VERSION", project.version)
}

tasks.injectTags.configure {
    outputClassName.set("com.paneedah.${id}.Tags")
}

repositories {
    mavenCentral()
    maven {
        name = "Cleanroom"
        url = uri("https://repo.cleanroommc.com/releases")
    }

    listOf("release", "beta", "dev").forEach { repoType ->
        maven {
            name = "Red Studio - ${repoType.capitalize()}"
            url = uri("https://repo.redstudio.dev/$repoType")
        }
    }

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
}

dependencies {
    implementation(rfg.deobf("curse.maven:red-core-873867:7455761"))

    compileOnly(rfg.deobf("curse.maven:techguns-244201:2958103"))
    compileOnly("com.cleanroommc:groovyscript:1.1.0") {
        isTransitive = false
    }

    annotationProcessor("org.ow2.asm", "asm-debug-all", "5.2")
    annotationProcessor("com.google.guava", "guava", "32.1.2-jre")
    annotationProcessor("com.google.code.gson", "gson", "2.8.9")

    val mixinBooter: String = modUtils.enableMixins("zone.rong:mixinbooter:8.6", "mixins.${id}.refmap.json") as String
    api(mixinBooter) {
        isTransitive = false
    }
    annotationProcessor(mixinBooter) {
        isTransitive = false
    }
}

idea {
    module {
        inheritOutputDirs = true

        excludeDirs = setOf(
            file(".github"), file(".gradle"), file(".idea"), file("build"), file("gradle"), file("run")
        )
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }
    withSourcesJar()
}

val at = project.files("src/main/resources/META-INF/${id}_at.cfg")

tasks.deobfuscateMergedJarToSrg.configure {
    accessTransformerFiles.from(at)
}

tasks.srgifyBinpatchedJar.configure {
    accessTransformerFiles.from(at)
}

tasks.processResources.configure {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    inputs.property("id", id)
    inputs.property("name", project.name)
    inputs.property("version", project.version)

    filesMatching(listOf("mcmod.info", "pack.mcmeta")) {
        expand(
            "id" to id,
            "name" to project.name,
            "version" to project.version
        )
    }

    filesMatching("**/lang/*.lang") {
        filter {
            it.replace(Regex("(?<!\\\\)\\\\u([0-9a-fA-F]{4})")) { matchResult ->
                matchResult.groupValues[1].toInt(16).toChar().toString()
            }
        }
    }
}

tasks.withType<ProcessResources>().configureEach {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    filesMatching(listOf("META-INF/mods.toml", "pack.mcmeta", "mcmod.info")) {
        filter {
            it.replace(Regex("(?<!\\\\)\\\\u([0-9a-fA-F]{4})")) { matchResult ->
                matchResult.groupValues[1].toInt(16).toChar().toString()
            }
        }
    }

    from(sourceSets.main.get().resources.srcDirs) {
        include("**/*.png")
        include("**/*.obj")
        include("**/*.mtl")
        include("**/*.ogg")
        include("**/*.vsh")
        include("**/*.fsh")
    }
}

tasks.named<Jar>("jar") {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    manifest {
        attributes(
            "ModSide" to "BOTH",
            "FMLAT" to "${id}_at.cfg",
            "FMLCorePlugin" to "${plugin}",
            "FMLCorePluginContainsFMLMod" to "true",
            "ForceLoadAsMod" to "true"
        )
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.isFork = true
    options.forkOptions.jvmArgs = listOf("-Xmx4G", "-XX:+UseG1GC")
}

tasks.register("deployToParent") {
    dependsOn("compileJava")
    doLast {
        val rootJar = rootProject.projectDir.parentFile.resolve("Modern-Warfare-Cubed-0.1.9.jar")
        val classesDir = file("build/classes/java/main")
        if (rootJar.exists() && classesDir.exists()) {
            exec {
                commandLine("jar", "uf", rootJar.absolutePath, "-C", classesDir.absolutePath, ".")
            }
            println(">>> Successfully injected updated classes from MWCV into parent Modern-Warfare-Cubed-0.1.9.jar!")
        }
    }
}

tasks.named("build") {
    finalizedBy("copyJarToDir")
    
    doLast {
        val libsDir = file("build/libs")
        
        // Ищем в папке build/libs финальный jar (игнорируем dev и sources)
        val finalJar = libsDir.listFiles()?.find { 
            it.name.endsWith(".jar") && 
            !it.name.endsWith("-dev.jar") && 
            !it.name.endsWith("-sources.jar")
        }
        
        if (finalJar != null && finalJar.exists()) {
            val targetDir = file("C:/Users/reizv/Documents/mwccf")
            if (!targetDir.exists()) targetDir.mkdirs()
            
            val targetFile = File(targetDir, finalJar.name)
            finalJar.copyTo(targetFile, overwrite = true)
            
            println(">>> Final JAR (${finalJar.name}) successfully copied to ${targetDir.absolutePath}")
        } else {
            println(">>> [WARNING] Final JAR not found in build/libs!")
        }
    }
}
tasks.register<Copy>("copyJarToDir") {
    // Отключаем проверку состояния целевой папки
    doNotTrackState("Target directory contains unreadable or locked files")
    
    from(tasks.named<Jar>("jar").get().archiveFile)
    into("C:/Users/reizv/Documents/mwccf")
}