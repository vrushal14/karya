plugins {
	id(Plugins.Kotlin.KAPT)
}

dependencies {

	implementation(project(Modules.CORE))

	implementation(Libs.REDISSON)
	implementation(Libs.SLF4J)

	implementation(Libs.Log4j.API)
	implementation(Libs.Log4j.CORE)
	implementation(Libs.Log4j.KOTLIN_API)

	implementation(Libs.Kotlinx.COROUTINES)
	implementation(Libs.Dagger.DAGGER)

	kapt(Libs.Dagger.COMPILER)
}

tasks.register("copyConfigs") {
	doLast {
		val configPath = File("src/main/resources")
		delete(configPath)
		copy {
			from(project.rootDir.resolve("configs/common"))
			into(configPath.resolve(""))
		}
	}
}
tasks.named("processResources") {
	dependsOn("copyConfigs")
}
