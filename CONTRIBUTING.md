# Contributing

This repository is IDroot Authenticator, maintained by Sebastian Fleckenstein.
It is a fork of Aegis with its own functions. Issues and pull requests belong here:
[arcanis2k3/Authenticator](https://github.com/arcanis2k3/Authenticator/issues).

Please read [the FAQ](FAQ.md) before reporting a bug or requesting a feature.
Security reports go through [private vulnerability reporting](SECURITY.md), not a public issue.

## Translations

Pull requests that update the strings in this repository are welcome. The
Crowdin project of upstream Aegis is not the source for this fork.

## Pull requests

For a large change, open a feature request in this repository first.

## Bug reports

Use the bug form and include the app version from the About screen. Reports
without enough detail will be closed.

The package id of a release build is `org.idroot.authenticator`. A debug build
uses `org.idroot.authenticator.debug`.

### Capturing a log with ADB

In some cases, we ask our users to obtain a debug log from their device. This is
typically only necessary if IDroot Authenticator:
- Is unable to recover from an error and crashes.
- Only shows a generic error to the user, but writes a more detailed one to the
  log.

Capturing a log with the Android Debug Bridge (ADB) allows us to see the stack
trace and the exception that occurred.

#### Preparation

Before you can capture a log, you first need to go through a one-time setup
process on your Android device and computer.

##### Prerequisites

- Your Android device.
- A computer with Windows, Mac or Linux.
- A USB cable to connect your Android device to your computer.

##### Setup

__On your Android device__:

1. Navigate to ``Settings -> About``, scroll down and start tapping on the build
   number until developer options are enabled.
2. Navigate to ``Settings -> System -> Developer options`` and enable ``USB
   debugging``.

These navigation steps may differ slightly across Android versions and ROMs.

__On your computer__:

3. Download and extract the SDK platform tools for Android:
   https://developer.android.com/studio/releases/platform-tools.
4. Start your terminal emulator (If you're on Windows, start PowerShell) and
   navigate to the folder where platform-tools was extracted.
5. Execute ``adb devices``.

__On your Android device__:

6. A prompt will appear. Select "Always allow from this computer" and accept the
   connection.

#### Capturing a log

__On your Android device__:

1. Start IDroot Authenticator.

__On your PC__:

2. Start your terminal emulator (If you're on Windows, start PowerShell) and
   navigate to the folder where platform-tools was extracted.
3. Start a log capture by executing the following commands.

    ```
    adb logcat -c
    adb logcat > debug.log
    ```

    The logcat command captures the full system log by default, which may expose
    some sensitive information. While this information can sometimes help with
    finding the root cause of the issue, it is not always necessary. To only
    capture the log output of IDroot Authenticator, replace the last logcat command with the
    one below:

    ```sh
    adb logcat --pid=$(adb shell pidof -s org.idroot.authenticator) > debug.log
    ```

    _If you are using a debug APK, replace ``org.idroot.authenticator`` with
   ``org.idroot.authenticator.debug``._

__On your Android device__:

4. Reproduce the issue.

__On your PC__:

5. Stop the log capture with Ctrl+C.
6. Attach the ``debug.log`` file to your issue on GitHub.
