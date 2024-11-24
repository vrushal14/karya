plugins {
	id(Plugins.Kotlin.KAPT)
}

dependencies {
	implementation(project(Modules.CORE))

	implementation(Libs.Ktor.KOTLINX)
	implementation(Libs.Ktor.Client.CORE)
	implementation(Libs.Ktor.Client.CONTENT_NEGOTIATION)
	implementation(Libs.Ktor.Client.CIO)

	implementation(Libs.Dagger.DAGGER)
	kapt(Libs.Dagger.COMPILER)
}
