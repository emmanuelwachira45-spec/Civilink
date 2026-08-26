# Implementation Plan - Project Cleanup and Error Fix

The project contains numerous duplicate files with the `-Guardian` suffix, causing ambiguity errors and build failures. Many of these `-Guardian` files actually contain the full implementation, while the original files are stubs. This plan aims to consolidate the project by keeping the correct versions and fixing the build configuration.

## User Review Required

> [!IMPORTANT]
> I will be replacing original files with their `-Guardian` counterparts where the latter is more complete, then deleting all `-Guardian` files. This is a destructive operation on the duplicate files.

> [!WARNING]
> I will also adjust the `compileSdk` and `targetSdk` to 35 in `app/build.gradle.kts` to ensure compatibility, as version 37 was causing fundamental library resolution errors.

## Proposed Changes

### Build Configuration

#### [MODIFY] [app/build.gradle.kts](file:///C:/Users/EMMANUEL/OneDrive/المستندات/Civilink/app/build.gradle.kts)
- Update `compileSdk` and `targetSdk` to 35.
- Align dependencies with the more complete version found in the Guardian file.

### Source Code Cleanup

#### [DELETE] All files matching `*-Guardian*`
- Before deletion, I will ensure that the content of the Guardian file is preserved in the corresponding original file if the Guardian version is the complete one.
- This includes Kotlin files, XML resources, and configuration files.

#### [MODIFY] [MainActivity.kt](file:///C:/Users/EMMANUEL/OneDrive/المستندات/Civilink/app/src/main/java/com/example/civilink/MainActivity.kt)
- Fix the `@Composable` invocation error in the preview.
- Ensure imports are correct after ambiguity is resolved.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:assembleDebug` to verify the build.
- Run `analyze_file` on `MainActivity.kt` to ensure no semantic errors remain.

### Manual Verification
- Check the project structure in the IDE to ensure no duplicate files remain.
