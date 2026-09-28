# Inventory App

A robust, multi-module Android application for inventory management. Built entirely with Kotlin, Jetpack Compose, and Clean Architecture, this project focuses heavily on a resilient offline-first synchronization engine, dual-path image handling, and local file normalization.

# Key Features
* **Offline-first Sync Engine**: Utilizes WorkManager (`SyncWorker`) and preference datastore (`SyncPreference`) to ensure changes are safely queued locally and pushed to remote server automatically when network connectivity is restored.
* **Dual Path Image Handling**: Manage both local device path and cloud storage URLs in local database entities, ensuring instant UI rendering while syncing large media in the background.
* **Media Normalization**: Includes an `ImageManager` that process EXIF rotation metadata, downsamples high-resolution bitmaps to prevent memory exhaustion, dynamically compresses into JPEG/PNG, and enforces unique cache-busting filenames.
* **Barcode Scanning**: Integrate hardware barcode scanning utilizing a custom `BarcodeAnalyzer` and dedicated `ScannerScreen` for rapid inventory lookup.
* **Inventory Management**: Full CRUD operations for Products and Stock Batches, alongside relational mapping for Categories and Locations. Supported by structured, single responsibility domain use cases
* **Dynamic Filtering & Sorting**: Customizable query builders (`ProductQueryBuilder`) and UI filter bottom sheet to manage local data.

# Project Architecture (Multi Module)
The application is divided into feature and core modules following Clean Architecture principles.

## **Core Modules**
* `:core:database` : The local source of truth containing Room `AppDatabase.kt`, Entities, relational mapping models, and Datastore preferences.
* `:core:network` : The network layer powered by Ktor for multipart image uploads and RESTful JSON payload syncing.
* `:core:domain` : Pure Kotlin module providing foundational models, root error models (`RootError.kt`), standardized result wrappers, and shared utilities used across feature modules.
* `:core:ui` : Shared Jetpack Compose design system containing themes, customized interactive components, and common standard icons.

## **Feature Modules**
* `:features:product` : The primary feature module containing the complete presentation layer (Dashboard, Product List, Detail Screen, Forms) and data/domain implementations specific to product and batch management.
* `:features:scanner` : An isolated feature module responsible solely for barcode scanner screen and optical analyzer logic.

# **Tech Stack**
* **UI**: Jetpack Compose, Material Design components.
* **Architecture**: Clean Architecture, MVVM, Modularization.
* **Local Storage**: Room Database, DataStore.
* **Networking**: Ktor Client
* **Background Processing**: WorkManager
* **Build System**: Gradle (Kotlin DSL `build.gradle.kts` and Version Catalogs `libs.versions.toml`)
* **CI/CD**: GitHub Actions (`android-ci.yml`)

# **Getting Started**

## **Prerequisites**
* Android Studio
* Java Development Kit (JDK): Version 17 or higher
* Android Device or Emulator running API 24 (Android 7.0) or higher

## **Installation**
1. Clone the repository to your local machine.
2. Open the `Inventory-master` root directory in Android Studio.
3. Allow Gradle to sync the dependencies declared in the module `build.gradle.kts` files and the root `libs.versions.toml`
4. Configure your remote API endpoint. Copy the `local.properties.example` file, rename it to `local.properties` in the project root, and replace placeholder URL with active backend domain.
5. Select the app configuration and press Run.