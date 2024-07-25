plugins {
    java
}

tasks.withType(Wrapper::class) {
    gradleVersion = "8.9"
}

group = "io.dorum"
version = "1.0-SNAPSHOT"

val testNGVersion = "7.10.2"
val seleniumVersion = "4.23.0"
val appiumVersion = "9.2.3"
val lombokVersion = "1.18.34"
val log4jVersion = "2.23.1"
val apacheVersion = "2.16.1"

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/org.testng/testng
    testImplementation("org.testng:testng:$testNGVersion")

    // https://mvnrepository.com/artifact/io.appium/java-client
    implementation("io.appium:java-client:$appiumVersion")

    // https://mvnrepository.com/artifact/org.seleniumhq.selenium/selenium-java
    implementation("org.seleniumhq.selenium:selenium-java:$seleniumVersion")

    // https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-slf4j2-impl
    testImplementation("org.apache.logging.log4j:log4j-slf4j2-impl:$log4jVersion")

    // https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core
    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")

    // https://mvnrepository.com/artifact/org.projectlombok/lombok
    implementation("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    testAnnotationProcessor("org.projectlombok:lombok:$lombokVersion")

    // https://mvnrepository.com/artifact/commons-io/commons-io
    implementation("commons-io:commons-io:$apacheVersion")
}

tasks.test {
    useTestNG {
        val suite = System.getProperty("testng.suite", "src/test/resources/ios-suite.xml")
        suites(suite)
    }
    testLogging {
        events("passed", "skipped", "failed")
    }
}