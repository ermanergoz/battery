# Battery

An Android app that shows live battery statistics and tells you when to unplug the charger, so the battery wears out more slowly.

<p align="center">
	<img src="resources/Screenshot_20210316-010430.png" height="500" alt="Main screen of the app">
</p>

## Why I built it

I built Battery in 2020 for my own phone, a General Mobile GM9. I had two goals: see what the battery was doing (how much current it drew, how warm it got, what voltage it ran at) and make the battery last longer.

Lithium-ion batteries wear out faster when they stay at full charge and when they run hot. Along with the live stats, the app has a charge alarm. I set a limit, such as 80%, and the phone tells me to unplug the charger when the battery reaches it.

## Three versions of the same app

After the first version, I kept using this app as a practice project and rebuilt it twice with new tools. Each version has its own branch:

| Year | Branch | What it is |
| --- | --- | --- |
| 2020 | [`legacy`](https://github.com/ermanergoz/battery/tree/legacy) | **This branch.** The original app: Kotlin, XML layouts, MVVM with ViewModel and LiveData |
| 2021 | [`rxjava`](https://github.com/ermanergoz/battery/tree/rxjava) | The same app with its asynchronous code rewritten in RxJava 3, to practise reactive programming |
| 2022 | [`main`](https://github.com/ermanergoz/battery/tree/main) | The latest version: the UI rebuilt in Jetpack Compose, which I did to learn and practise Compose |

The features are mostly the same in all three versions, so you can compare the branches to see how each stack handles the same problems.

## Features

- Battery percentage, current, temperature and voltage, updated live
- The charging source: AC, USB, wireless or discharging
- Battery health, status and technology, in the info dialog
- An ongoing notification with live battery stats
- A charge alarm that notifies you when the battery reaches the limit you set
- After a reboot or an app update, the notification and the alarm start again if they were on
- A shortcut to the system's battery usage screen
- Light and dark themes, following the system or chosen in the settings

## What this project shows

- I built a complete app for a real need, from the idea to a working app on my own phone
- It uses Android platform APIs: battery broadcasts and `BatteryManager`, a foreground service, notification channels, a boot receiver and settings
- The data layer, the alarm rules, the background service and the UI are kept separate, and unit tests cover the logic
- I refactored the same codebase onto new technologies and kept its behaviour the same (see the other branches)

## How this version works

- Kotlin with XML layouts and View Binding
- MVVM: `MainViewModel` gives the screen its battery state as `LiveData`
- `BatteryStatusLiveData` listens to the `ACTION_BATTERY_CHANGED` broadcast only while something observes it
- The battery broadcast does not include the current, so a coroutine `liveData {}` builder polls it every second while the screen is visible
- `BatteryService` is a foreground service that keeps the ongoing notification and the charge alarm running in the background. `BootReceiver` starts it again after a reboot or an app update
- `ChargeAlarm` holds the alarm rule. It alerts once when the battery reaches the limit while charging, and it does not repeat while the level moves up and down around the limit
- The settings screen uses AndroidX Preference

## Build & Run

- Clone this branch:

	```sh
	git clone -b legacy https://github.com/ermanergoz/battery.git
	```

- Open the project in Android Studio with **File > Open** and select the cloned folder. When Gradle sync finishes, the app is ready to run.

This branch still uses the build tools it was written with: Kotlin 1.3.70, Android Gradle Plugin 4.0.0, Gradle 6.1.1, compile SDK 29, min SDK 23 (Android 6.0). The app version is 1.0.1.

## Tests

The project has unit tests for the battery status mapping, the charge alarm rules and the theme setting. They run on the JVM, so you do not need a device:

```sh
./gradlew test
```

## Status

Archived. This branch keeps the app as it was in 2020. The latest version is on the [`main`](https://github.com/ermanergoz/battery/tree/main) branch.

## Meta

Yusuf Erman ERGÖZ – erman.ergoz@gmail.com

Distributed under the MIT license. See ``LICENSE`` for more information.

[https://github.com/ermanergoz](https://github.com/ermanergoz)

## Acknowledgements

Icons from [Flaticon](https://www.flaticon.com/): the app logo by Smashicons, the discharging icon by Icongeek26, the USB and wireless charging icons by Freepik, and the AC charging icon by Pixel perfect.
