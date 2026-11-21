import dev.kikugie.stonecutter.build.StonecutterBuildExtension
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

plugins {
    `java-library`
}

val sc = extensions.getByType<StonecutterBuildExtension>()
val modId = sc.properties.get<String>("mod.id")
val modPackage = sc.properties.get<String>("mod.group").replace('.', '/')
val absentPackages = COMPAT_PACKAGES.filterKeys { sc.properties.getOrNull<String>("deps.$it") == null }.values.toList()

if (absentPackages.isNotEmpty()) {
    sourceSets.main {
        java {
            absentPackages.forEach { exclude("$modPackage/compat/$it/**", "$modPackage/mixin/client/compat/$it/**") }
        }
    }

    tasks.processResources {
        val mixinConfig = destinationDir.resolve("$modId.mixins.json")
        val absentMixinPrefixes = absentPackages.map { "compat.$it." }
        inputs.property("absentCompatPackages", absentPackages)
        doLast {
            val config = JsonSlurper().parse(mixinConfig) as Map<*, *>
            val presentMixins = config.mapValues { (_, value) ->
                if (value is List<*>) value.filterNot { mixin -> absentMixinPrefixes.any { (mixin as String).startsWith(it) } } else value
            }
            mixinConfig.writeText(JsonOutput.prettyPrint(JsonOutput.toJson(presentMixins)))
        }
    }
}
