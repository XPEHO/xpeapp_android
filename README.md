# XpeApp - Android mobile application

## Description

This is a mobile application for Android devices made with Jetpack Compose.

## Getting Started

### Prerequisites

- Android Studio
- Android SDK

### Android

This is an Android project. You can open it with Android Studio and run it on an emulator or a physical device.

For more information about Android SDK please visit the [Android Developer website](https://developer.android.com/).

### Kotlin

Kotlin is the programming language used in this project. This is a modern programming language that is fully interoperable with Java.

You can learn more about Kotlin on the [Kotlin website](https://kotlinlang.org/).

### Jetpack Compose

Jetpack Compose is a modern toolkit for building native Android UI. It simplifies and accelerates UI development on Android.

You can learn more about Jetpack Compose on the [Android Developer website](https://developer.android.com/jetpack/compose).

## Configuration

### google-services.json

This application works with `Firebase` and require a `google-services.json` file to be placed in the `app` directory.

Please refer to an administrator to get this file.

### Properties

This application require two properties to be set in the `app/config/uat.properties` for `UAT` and `app/config/prod.properties` for `PROD`.

```properties
BACKEND_URL="https://api.example.com"
ENVIRONMENT="uat"
```

The `BACKEND_URL` is the URL of the backend API and the `ENVIRONMENT` is the environment of the application.

## Deployment

First you need to generate an app bundle, for that you just need to create a **pre-release** on the repository. It will trigger a github action that generate the release app bundle. It also generate the release apk to test.

> Warning !
> Make sure to do a **pre-release** before the validation of the app bundle by Google

Next, you need to send the app bundle to the Google Play Console, for that follow the Bookstack documentation : _Mobilité/Android/Comment uploader une release sur Google Play Console_

In order to force XPEHO users to have the latest version of the app, we add to the app a check of the Github repository release latest version. So when a release is done and validated by Google, we need to switch the pre-release to a release on the repository to trigger the force update popup in XpeApp.

## Xpeho UI

This application use the android part of the ui library of XPEHO imported from its [own repository](https://github.com/XPEHO/xpeho_ui_android).

### Import

To import the _xpeho_ui_android_ library you need to setup two environment variables (as mentioned in the [README of the package](https://github.com/XPEHO/xpeho_ui_android/blob/main/README.md)) :

On a mac/linux you can add them to your `.zshrc`/`.bashrc`

```zshrc
#Github
export GITHUB_USER="your_github_username"
export GITHUB_TOKEN="your_github_token"
```

To fill the GITHUB_TOKEN, make sure to generate a PAT (Personal Access Token) on your github account following the [documentation](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens#creating-a-personal-access-token-classic).

You need to allow the following rights :
![repos_right](doc/repos_right.png)
![packages_right](doc/packages_right.png)

## SonarQube

The XPEHO SonarQube perform analysis each time changes are made to this directory and pushed.

[XPEHO SonarQube](https://sonar.infra.xpeho.com)

The configuration of the sonar analysis for this directory is defined in the [gradle file](./app/build.gradle).
