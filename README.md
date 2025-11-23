# Appll - Time Capsule & Digital Legacy App

Appll is an Android application designed to help users preserve their memories and plan for their digital legacy. It allows users to create "time capsules" — messages with images and titles — scheduled to be viewed at a future date. Additionally, it provides tools for digital legacy planning.

## Features

*   **User Authentication:** Secure login via Google Sign-In and Firebase Authentication.
*   **Create Time Capsules:** Compose personal messages with a title and attach an image.
*   **Schedule Delivery:** Set a specific future date and time for the time capsule to be "opened" or viewed.
*   **View Time Capsules:** Access your collection of created time capsules.
*   **Digital Legacy Planning:** A dedicated section for planning your digital afterlife and legacy.
*   **Surprise Me:** A feature designed to offer unexpected content or interactions.

## Tech Stack

*   **Language:** Java
*   **Platform:** Android (Min SDK: 24, Target SDK: 34)
*   **Build System:** Gradle (Kotlin DSL)
*   **Backend Services (Firebase):**
    *   **Authentication:** For user management and Google Sign-In.
    *   **Realtime Database:** Stores user data, time capsule metadata, and messages.
    *   **Storage:** Stores images attached to time capsules.
*   **Key Libraries:**
    *   **Google Play Services Auth:** For handling Google Sign-In flows.
    *   **Glide:** For efficient image loading and caching.
    *   **AndroidX:** Utilizing AppCompat, Material Design components, and ConstraintLayout for UI.

## Setup Instructions

To run this project locally, follow these steps:

1.  **Clone the Repository:**
    ```bash
    git clone <repository-url>
    cd appll
    ```

2.  **Open in Android Studio:**
    *   Open Android Studio and select "Open an existing Android Studio project".
    *   Navigate to the cloned directory and select it.

3.  **Firebase Configuration:**
    *   This project relies on Firebase services. You must provide your own `google-services.json` file.
    *   Go to the [Firebase Console](https://console.firebase.google.com/) and create a new project.
    *   Enable **Authentication** and set up the **Google Sign-In** provider.
    *   Enable **Realtime Database** and **Storage**.
    *   Add an Android app to your Firebase project with the package name `com.example.appll`.
    *   Download the `google-services.json` file and place it in the `app/` directory of the project.

4.  **Build and Run:**
    *   Sync the project with Gradle files.
    *   Connect an Android device or start an emulator.
    *   Run the application (`Shift + F10` on Windows/Linux, `Control + R` on macOS).

## Permissions

The app requires the following permissions:
*   `INTERNET`: To access Firebase services.
*   `CAMERA`: To capture images for time capsules.
*   `READ_EXTERNAL_STORAGE` / `WRITE_EXTERNAL_STORAGE`: To pick images from the gallery.

## License

[Add License Information Here]
