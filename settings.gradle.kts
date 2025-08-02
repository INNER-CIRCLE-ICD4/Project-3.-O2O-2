pluginManagement {
    plugins {
        kotlin("jvm") version "2.1.20"
    }
}
rootProject.name = "taxi-matching-service"

include(
    "admin-service",
    "auth-service",
    "location-service",
    "matching-service",
    "rating-service",
    "notification-service"
)
