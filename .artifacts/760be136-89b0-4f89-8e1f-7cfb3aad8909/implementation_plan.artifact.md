# Implementation Plan - Refactor AdminDashboardScreen and Add Preview

This plan outlines the refactoring of `AdminDashboardScreen` to extract UI logic into a stateless Composable and adding a robust `@Preview` with sample data.

## Proposed Changes

### UI Components

#### [MODIFY] [AdminDashboard.kt](file:///C:/Users/hp/StudioProjects/Civilink/app/src/main/java/com/example/civilink/ui/screens/admindashboard/AdminDashboard.kt)
- Extract the UI content of `AdminDashboardScreen` into a new stateless Composable `AdminDashboardContent`.
- Update `AdminDashboardScreen` to collect state from `ReportViewModel` and pass it to `AdminDashboardContent`.
- Update `AdminDashboardScreenPreview` to use `AdminDashboardContent` with sample data and wrap it in `CivilinkTheme`.

## Verification Plan

### Manual Verification
- Render the `AdminDashboardScreenPreview` using the `render_compose_preview` tool to ensure the UI looks correct with sample data.
- Run `analyze_file` to ensure no syntax errors were introduced.
