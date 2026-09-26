import androidx.room.gradle.RoomExtension

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary) // Використовуємо стабільний Android-плагін
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
}

kotlin {
    // Вказуємо єдину версію Java для всього проєкту
    jvmToolchain(11)

    // 1. Стандартна реєстрація Android-таргету
    androidTarget()

    // 2. Налаштування для iOS
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    // 3. Залежності
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // Ktor Client
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            // Voyager (Навігація)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.transitions)
            implementation(libs.voyager.tab.navigator)
            implementation(libs.voyager.screenmodel)
            implementation(libs.voyager.koin)

            // Koin (DI)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)

            // Peekaboo (Доступ та дозвіл до файлів)
            implementation(libs.peekaboo.image.picker)

            // Coil (Завантаження зображень)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

            implementation(libs.compose.reorderable)

            // Room Database
            implementation(libs.androidx.room.runtime)
            implementation(libs.sqlite.bundled)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.android)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

// 4. Окремий блок для конфігурації Android-модуля
android {
    namespace = "com.nazar_protasov.swipehome.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

// 5. Налаштування Room KMP
configure<RoomExtension> {
    schemaDirectory("$projectDir/schemas")
}

// 6. Залежності компілятора (KSP)
dependencies {
    // Вказуємо KSP для всіх таргетів
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
}