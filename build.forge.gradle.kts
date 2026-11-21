import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.gradle.ext.Gradle
import org.jetbrains.gradle.ext.ProjectSettings
import org.jetbrains.gradle.ext.RunConfigurationContainer

plugins {
    `java-library`
    id("net.neoforged.moddev.legacyforge")
    id("neoforge-mutex")
    id("mod-conventions")
    id("mod-publishing")
    id("slib")
    id("dev-mods")
    id("compat-mods")
}

val modId = property("mod.id") as String
val mixinConfigs = listOf("$modId.mixins.json", "$modId-forge.mixins.json")
val compatMods = listOf("legendary_tooltips", "iceberg", "prism", "emi", "tooltip_overhaul", "obscure_tooltips", "spell_engine")

repositories {
    exclusiveContent {
        forRepository { maven("https://cursemaven.com") { name = "CurseMaven" } }
        filter { includeGroup("curse.maven") }
    }
    exclusiveContent {
        forRepository { maven("https://maven.terraformersmc.com/releases") { name = "TerraformersMC EMI" } }
        filter { includeGroup("dev.emi") }
    }
    exclusiveContent {
        forRepository { maven("https://raw.githubusercontent.com/ObscuriaLithium/modding/main/maven/") { name = "Obscuria" } }
        filter { includeGroup("dev.obscuria") }
    }
    exclusiveContent {
        forRepository { mavenLocal() }
        filter { includeGroup("com.stalemated.cta") }
    }
}

dependencies {
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")

    compatMods.forEach { mod -> findProperty("deps.$mod")?.let { modCompileOnly(it as String) } }
    listOf("fragmentum", "tierify").forEach { lib -> findProperty("deps.$lib")?.let { compileOnly(it as String) } }

    testRuntimeOnly(files(sourceSets.main.get().runtimeClasspath))
    testRuntimeOnly("org.slf4j:slf4j-api:${property("deps.slf4j")}")
    testRuntimeOnly("org.apache.logging.log4j:log4j-core:${property("deps.log4j")}")
}

configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.apache.logging.log4j" && requested.name == "log4j-core") {
            useVersion(property("deps.log4j") as String)
        }
    }
}

legacyForge {
    version = property("deps.forge_loader") as String

    mods {
        register(modId) {
            sourceSet(sourceSets.main.get())
        }
    }

    runs {
        register("client") { client() }
        register("server") { server() }

        configureEach {
            gameDirectory = rootProject.file("run/${sc.current.project}")
            disableIdeRun()
        }
    }
}

if (providers.systemProperty("idea.sync.active").orNull == "true") {
    val ideaSettings = (rootProject.the<IdeaModel>().project as ExtensionAware).the<ProjectSettings>() as ExtensionAware
    val nodePath = path
    legacyForge.runs.configureEach {
        val side = name.replaceFirstChar(Char::uppercase)
        ideaSettings.the<RunConfigurationContainer>().register<Gradle>("Minecraft $side ($nodePath)") {
            taskNames = listOf("$nodePath:run$side")
            projectPath = rootDir.invariantSeparatorsPath
        }
    }
}

mixin {
    add(sourceSets.main.get(), "$modId.refmap.json")
    mixinConfigs.forEach { config(it) }
}

publishMods {
    file = tasks.named<Jar>("reobfJar").flatMap { it.archiveFile }
}

tasks {
    processResources {
        exclude("fabric.mod.json", "META-INF/neoforge.mods.toml", "*-neoforge.mixins.json")

        val mixinConfigFiles = mixinConfigs.map { destinationDir.resolve(it) }
        val refmap = "$modId.refmap.json"
        doLast {
            mixinConfigFiles.forEach { mixinConfig ->
                val config = JsonSlurper().parse(mixinConfig) as Map<*, *>
                mixinConfig.writeText(JsonOutput.toJson(config + ("refmap" to refmap)))
            }
        }
    }

    named<Jar>("jar") {
        manifest.attributes("MixinConfigs" to mixinConfigs.joinToString(","))
    }

    named("createMinecraftArtifacts") {
        dependsOn("stonecutterGenerate")
    }

    named<Copy>("buildAndCollect") {
        from(named<Jar>("reobfJar").flatMap { it.archiveFile }, named<Jar>("sourcesJar").flatMap { it.archiveFile })
    }
}
