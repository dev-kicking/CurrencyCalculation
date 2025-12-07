plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "dev.kick.currencycalculation.core"
    
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
    // Core KTX
    implementation(libs.androidx.core.ktx)
    
    // Testing
    testImplementation(libs.junit)
}

