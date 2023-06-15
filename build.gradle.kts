@file:Suppress("DSL_SCOPE_VIOLATION", "MISSING_DEPENDENCY_CLASS", "FUNCTION_CALL_EXPECTED", "PropertyName")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	java
	`maven-publish`

	alias(libs.plugins.kotlin)
	alias(libs.plugins.quilt.loom)
}

val archives_base_name: String by project
base.archivesName.set(archives_base_name)

val javaVersion = 17

repositories {
	maven("https://maven.isxander.dev/releases")
	maven("https://maven.terraformersmc.com/releases/")
	maven("https://jitpack.io")

	exclusiveContent {
		forRepository {
			maven {
				name = "Modrinth"
				url = uri("https://api.modrinth.com/maven")
			}
		}
		filter {
			includeGroup("maven.modrinth")
		}
	}
}

dependencies {
	minecraft(libs.minecraft)
	mappings(
		variantOf(libs.quilt.mappings) {
			classifier("intermediary-v2")
		}
	)

	modImplementation(libs.quilt.loader)
	modImplementation(libs.qfapi)
	modImplementation(libs.qkl)

	modImplementation(libs.yacl)
	modImplementation(libs.discord)
	include(libs.discord)?.let { modImplementation(it) }

	modImplementation(libs.modmenu)

	modLocalRuntime(libs.sodium)
	modLocalRuntime(libs.indium)
}

tasks {
	withType<KotlinCompile> {
		kotlinOptions {
			jvmTarget = javaVersion.toString()
			languageVersion = libs.plugins.kotlin.get().version.requiredVersion.substringBeforeLast('.')
		}
	}

	withType<JavaCompile>.configureEach {
		options.encoding = "UTF-8"
		options.isDeprecation = true
		options.release.set(javaVersion)
	}

	processResources {
		filteringCharset = "UTF-8"
		inputs.property("version", project.version)

		filesMatching("quilt.mod.json") {
			expand(
				mapOf(
					"version" to project.version
				)
			)
		}
	}

	javadoc {
		options.encoding = "UTF-8"
	}

	wrapper {
		distributionType = Wrapper.DistributionType.BIN
	}

	jar {
		from("LICENSE") {
			rename { "LICENSE_${archives_base_name}" }
		}
	}
}

val targetJavaVersion = JavaVersion.toVersion(javaVersion)
if (JavaVersion.current() < targetJavaVersion) {
	kotlin.jvmToolchain(javaVersion)

	java.toolchain {
		languageVersion.set(JavaLanguageVersion.of(javaVersion))
	}
}

java {
	withSourcesJar()

	sourceCompatibility = targetJavaVersion
	targetCompatibility = targetJavaVersion
}

publishing {
	publications {
		register<MavenPublication>("Maven") {
			from(components.getByName("java"))
		}
	}

	repositories {

	}
}
