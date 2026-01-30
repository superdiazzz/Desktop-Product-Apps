## Project Overview

Desktop application berbasis Kotlin Multiplatform (JVM) dengan arsitektur offline-first.
Aplikasi memungkinkan pengguna mengelola product baik offline maupun online, dengan sistem operation queue dan automatic synchronization saat koneksi tersedia.

## Video Result

Hasil project ini akan tampak seperti berikut.

[![Watch the video](https://cdn.loom.com/sessions/thumbnails/514a8fe2f05e4d3e8247b80173c090d1-97ddd324f20b8012-full-play.gif#t=0.1)](https://www.loom.com/share/514a8fe2f05e4d3e8247b80173c090d1)

## Project Structure
```
Desktop-Product-App/
├─ build.gradle.kts
├─ settings.gradle.kts
├─ gradle/
├─ composeApp/
│  ├─ build.gradle.kts
│  └─ src/jvmMain/kotlin/
│     ├─ main.kt
│     └─ ui/
│        ├─ App.kt
│        ├─ screen/
│        │  ├─ HomeScreen.kt
│        │  └─ FormScreen.kt
│        └─ components/
│           └─ StatusBar.kt
└─ shared/
   ├─ build.gradle.kts
   └─ src/commonMain/kotlin/
      ├─ di/
      │  └─ SharedContainer.kt
      ├─ model/
      │  └─ Product.kt
      ├─ network/
      │  ├─ ApiClient.kt
      │  └─ ProductApi.kt
      ├─ repository/
      │  └─ ProductRepository.kt
      ├─ sync/
      │  ├─ QueueEntity.kt
      │  └─ SyncManager.kt
      └─ util/
         └─ NetworkMonitor.kt
```

## Architecture
**UI:** Compose Multiplatform + Material 3

**Navigation:** Voyager

**Network:** Ktor Client

**Local Database:** SQLDelight (SQLite)

**Offline Strategy:** 
  - Local DB sebagai Source of Truth
  - Semua operasi Create/Update/Delete disimpan di queue
  - Queue diproses otomatis saat online

**Sync Strategy:**
  - Push local changes -> Pull remote data

**State Maagement:**
  - Flow + StateFlow
  - Reactive UI (Compose)


## Offline-First Konsep

| Scenario | Behavior |
| :--- | :--- |
| Offline Create | Disimpan ke local DB + queue |
| Offline Update | Update local DB + queue |
| Offline Delete | Delete local DB + queue |
| Online Recovery | Queue diproses otomatis |
| App Restart | Queue tetap tersimpan |

## Network Monitoring
  - Network status dipantau menggunakan NetworkMonitoring
  - Sync otomatis dijalankan saat status berubah online
  - UI menampilkan status:
     - offline
     - Synchronizing
     - All synced

## User Guide

A. Add Product
1. Klik Add Product
2. Isi Title & Description
3. Klik Save
4. Product langsung muncul (offline maupun online)

B. Edit Product
  - Perubahan langsung tersimpan secara lokal
  - Jika offline -> masuk queue

C. Delete Product
  - Product langsung hilang dari list
  - Sync dilakukan saat online

## Build Instructions

  - JDK 17+
  - Gradle (sudah disediakan di project)
  - Internet connection
  - OS:
      - macOS
      - Windows
      - Linux

## Run Apps (Desktop)

Start the server

```bash
  ./gradlew :composeApp:run
```

atau (Windows)

```bash
  gradlew.bat :composeApp:run
```

Sebelum run, kamu bisa build terlebih dahulu

```bash
  ./gradlew :composeApp:build
```

## Build Distribusinya

```bash
  ./gradlew :composeApp:package
```

Output akan tersedia di:

```bash
  composeApp/build/compose/binaries
```

## Database Reset (Bila perlu)

Untuk membersihkan local database

```
bashrm -rf ~/.<app-name>/
```
