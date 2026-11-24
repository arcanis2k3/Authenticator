# Privacy policy — IDroot Authenticator

Last updated: 24 November 2026 · Governing law: Federal Republic of Germany

The [German version](privacy.de.md) of this policy is authoritative.

This policy covers only the Android app **IDroot Authenticator** (`org.idroot.authenticator`). It does not cover verified handles, the dashboard, or identity checks on [idroot.org](https://idroot.org/en/privacy-policy). That service has its own privacy policy.

## Plain language

The app generates one-time codes on the device. It has no account, no advertising, and no analytics. The vault is not sent to IDroot. The app has no internet permission, so it cannot upload the vault by itself. Data leaves the device only if you export it, copy it, share it, or turn on an Android backup.

## 1. Controller

Sebastian Fleckenstein  
Erthalstr. 6  
97816 Lohr am Main, Germany  
Email: contact@idroot.org

The app is operated by a private individual. No data protection officer is appointed. Questions about this policy go to the address above.

## 2. What the controller does not receive

IDroot Authenticator is a fork of Aegis and runs without a user account. The controller does not receive:

- vault contents, secrets, one-time codes, or the vault password
- biometric samples
- camera images
- an advertising or analytics identifier
- crash reports

There is no IDroot server for this app. No legal basis for a transfer to the controller is needed, because that transfer does not happen.

## 3. Data that stays on the device

This data is created locally and stays there unless you send it somewhere yourself:

| Data | Purpose |
|---|---|
| Vault with account names, issuers, secrets, and settings | Generate one-time codes. Encrypted with a password (AES-256-GCM, scrypt). Without a password it is not encrypted, which the app advises against. |
| Biometric unlock | The system unlocks a key in the Android Keystore. The app does not store a fingerprint or face image. |
| Camera or a picked image | Read a QR code once. No video stream is stored or sent. |
| Clipboard | Only when you copy a code or start an import from the clipboard. |
| Exports and automatic backups | Only to a location you choose through the system. |
| Local audit log | Record actions inside the app. It stays in the app database. |
| Lock notice | A local notification, if you use one. It does not contact a server. |

Android device-to-device transfer is always possible. Android cloud backup is off and includes the vault only if you enable it for an encrypted vault. The device's backup provider then processes the file, not IDroot. An encrypted vault is only as safe as its password.

## 4. Permissions

- **Camera**, not required: scan QR codes.
- **Biometrics**: optional unlock through Android.
- **Vibration**: feedback on the device.
- **Boot completed**: refresh the quick-settings tile. Nothing is sent.

The app does not request an internet permission.

## 5. Other parties

- **Android, the device maker, and any cloud-backup transport you use**, once you allow a system or cloud backup.
- **GitHub**, when you download the source, these texts, or an APK from GitHub. GitHub's own privacy policy applies.
- **idroot.org**, when you open the website in a browser. The website policy applies, not this one.
- **Icon packs** you import remain local files.

The app does not sell data and does not show ads.

## 6. Retention and deletion

The controller does not store the vault. Deleting it means deleting the vault in the app, deleting export files wherever you saved them, and uninstalling the app. System backups also have to be removed with the device provider. The controller cannot reset a forgotten vault password.

## 7. Your rights

Where the GDPR applies to processing by the controller, you have the rights of access, rectification, erasure, restriction, portability, and objection, and the right to withdraw consent. The controller cannot fulfil those rights for vault data it does not hold. You may lodge a complaint with a supervisory authority. For the controller's residence that is the Bavarian state office for data protection supervision (Bayerisches Landesamt für Datenschutzaufsicht).

## 8. Children

The app is not directed at children. It does not ask for an age, because it creates no account.

## 9. Changes

The current text is in this repository. Continuing to use the app after a change means you have noticed it. The website policy for handles can change separately.
