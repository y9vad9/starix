package com.y9vad9.brawlex.build.convention.feature

import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("com.y9vad9.brawlex.build.convention.multiplatform-convention")
    id("com.y9vad9.brawlex.build.convention.detekt-convention")
    id("com.y9vad9.brawlex.build.convention.kover-convention")
    id("com.y9vad9.brawlex.build.convention.multiplatform-tests-convention")
}

val libs = the<LibrariesForLibs>()

dependencies {
    "jvmTestImplementation"(libs.mockk)
}

kover.reports.verify.rule {
    /**
     * We want to enforce the application layer to be fully tested.
     * It doesn't have the same problem with value objects constructors as in domain; therefore,
     * we can have a greater requirement here.
     *
     * Temporarily decreased, see https://github.com/y9vad9/brawlex/issues/17.
     */
    minBound(
        minValue = 85,
        coverageUnits = CoverageUnit.LINE,
        aggregationForGroup = AggregationType.COVERED_PERCENTAGE,
    )
}
