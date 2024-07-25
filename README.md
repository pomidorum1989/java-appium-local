# Mobile Test Automation Framework

This framework is designed to run automated tests on both Android and iOS devices using Appium, Selenium, TestNG, and Gradle. 
It supports parallel execution on multiple devices/emulators and uses a Gradle build configuration with TestNG for test management.

## Installation

### 1. Download XCode and Set Up Simulators

- Start available iOS simulators:
    ```sh
    xcrun simctl list devices
    xcrun simctl list | grep Booted   
    open -a Simulator --args -CurrentDeviceUDID 62EA1EB4-30AD-4180-8799-FA2854364C92
    open -a Simulator --args -CurrentDeviceUDID 321C4A8C-AE5E-43D3-9CF2-B01E4EE3DF0C
    xcrun simctl boot 62EA1EB4-30AD-4180-8799-FA2854364C92   
    xcrun simctl boot 321C4A8C-AE5E-43D3-9CF2-B01E4EE3DF0C

- Start available Android simulators:
  ```sh
   emulator -list-avds
   emulator -avd Pixel_4_API_35
   emulator -avd Pixel_6a_API_35

- Install Appium and Drivers
    ```sh
    npm install -g appium 
    npm install -g appium-doctor
    appium-doctor --android
    brew install ffmpeg
    appium driver install xcuitest  
    appium driver install safari  
    appium driver install uiautomator2
  
- Run tests
    ```sh
    ./gradlew test -Dtestng.suite=src/test/resources/ios-suite.xml
    ./gradlew test -Dtestng.suite=src/test/resources/android-suite.xml
