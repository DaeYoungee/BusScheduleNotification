plugins {
    id("busSchedule.android.compose.library")
    id("busSchedule.android.hilt")
}

android {
    namespace = "com.busschedule.schedulelist"
}

dependencies {

    implementation(project(":core:designsystem"))
    implementation(project(":core:util"))
    implementation(projects.core.domain)
    implementation(projects.core.navigation)
    implementation(projects.core.common)

    // hiltViewModel 사용
    implementation(libs.androidx.hilt.compose)

    // compose permission
    implementation(libs.accompanist.permission)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(project(":core:datastore"))
    implementation(libs.firebase.messaging.ktx)
    implementation(project(":core:model"))
    implementation(libs.androidx.work.runtime.ktx)
    implementation(project(":core:widget"))
//    implementation(project(":feature:navigate"))

    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}