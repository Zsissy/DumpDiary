plugins {
    application
    kotlin("jvm")
    kotlin("plugin.serialization")
}

application {
    mainClass.set("com.dumpdiary.backend.ApplicationKt")
}

kotlin {
    jvmToolchain(22)
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:2.3.12")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.12")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.3.12")
    implementation("io.ktor:ktor-server-status-pages-jvm:2.3.12")
    implementation("io.ktor:ktor-server-call-logging-jvm:2.3.12")
    implementation("io.ktor:ktor-server-cors-jvm:2.3.12")
    implementation("io.ktor:ktor-server-auto-head-response-jvm:2.3.12")
    implementation("io.ktor:ktor-server-default-headers-jvm:2.3.12")
    implementation("io.ktor:ktor-server-partial-content-jvm:2.3.12")
    implementation("io.ktor:ktor-server-compression-jvm:2.3.12")
    implementation("io.ktor:ktor-server-auth-jvm:2.3.12")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.3.12")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("at.favre.lib:bcrypt:0.10.2")
    implementation("org.eclipse.angus:jakarta.mail:2.0.3")
    implementation("ch.qos.logback:logback-classic:1.5.6")

    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-server-test-host-jvm:2.3.12")
}
