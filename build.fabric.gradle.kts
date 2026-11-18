plugins {
    id("dev.kikugie.loom-back-compat")
    id("mod-conventions")
    id("mod-publishing")
    id("slib")
}

val compatMods = listOf(
    "legendary_tooltips", "iceberg", "prism", "forge_config_api_port", "emi", "tooltip_overhaul",
    "puffish_skills", "tierify", "necronomicon", "obscure_tooltips", "spell_engine",
)
val devRuntimeMods = listOf("emi", "cta")

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
    minecraft("com.mojang:minecraft:${sc.current.version}")
    loomx.applyMojangMappings()

    modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric_api")}")

    compatMods.forEach { mod -> findProperty("deps.$mod")?.let { modCompileOnly(it as String) } }
    findProperty("deps.fragmentum")?.let { compileOnly(it as String) }
    devRuntimeMods.forEach { mod -> findProperty("deps.$mod")?.let { modLocalRuntime(it as String) { isTransitive = false } } }

    testRuntimeOnly(files(sourceSets.main.get().compileClasspath))
}

loom {
    fabricModJsonPath = rootProject.file("src/main/resources/fabric.mod.json")

    decompilerOptions.named("vineflower") {
        options.put("mark-corresponding-synthetics", "1")
    }

    runConfigs.all {
        preferGradleTask = true
        generateRunConfig = true
        runDirectory = rootProject.file("run/${sc.current.project}")
        jvmArguments.add("-Dmixin.debug.export=true")
    }
}

publishMods {
    file = loomx.modJar.flatMap { it.archiveFile }
}

tasks {
    processResources {
        exclude("META-INF/neoforge.mods.toml", "META-INF/mods.toml", "pack.mcmeta", "*-forge.mixins.json")
    }

    named<Copy>("buildAndCollect") {
        from(loomx.modJar.flatMap { it.archiveFile }, loomx.modSourcesJar.flatMap { it.archiveFile })
    }
}
