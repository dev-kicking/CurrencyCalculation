plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "dev.kick.currencycalculation.domain"
    
    compileSdk = 36
    
    defaultConfig {
        minSdk = 28
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Core 모듈 의존성
    implementation(project(":core"))
    
    // Testing
    testImplementation(libs.junit)
}
