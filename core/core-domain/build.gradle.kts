dependencies {

    implementation(project(":core:core-enum"))
    implementation(project(":support:global"))

    // 트랜잭션 사용을 위한 추가
    implementation("org.springframework:spring-tx:${property("springTxVersion")}")
    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    compileOnly("org.springframework:spring-context")
}
