# Datenschutzerklärung — IDroot Authenticator

Zuletzt aktualisiert: 24. November 2026 · Anwendbares Recht: Bundesrepublik Deutschland

Diese deutsche Fassung ist die maßgebliche Version. Bei Abweichungen gilt sie vor der [englischen Fassung](privacy.en.md).

Diese Erklärung gilt nur für die Android-App **IDroot Authenticator** (`org.idroot.authenticator`). Sie gilt nicht für verifizierte Handles, das Dashboard oder die Identitätsprüfung auf [idroot.org](https://idroot.org/de/privacy-policy). Dafür gibt es eine eigene Datenschutzerklärung.

## Kurzfassung

Die App erzeugt Einmalcodes auf dem Gerät. Sie hat kein Konto, keine Werbung und keine Analyse. Der Tresor wird nicht an IDroot geschickt. Die App hat keine Internet-Berechtigung und kann ihn deshalb selbst nicht hochladen. Daten verlassen das Gerät nur, wenn du sie exportierst, kopierst, weitergibst oder eine Android-Sicherung einschaltest.

## 1. Verantwortlicher

Sebastian Fleckenstein  
Erthalstr. 6  
97816 Lohr am Main, Deutschland  
E-Mail: contact@idroot.org

Die App wird von einer Privatperson betrieben. Ein Datenschutzbeauftragter ist nicht bestellt. Fragen zu dieser Erklärung gehen an die Adresse oben.

## 2. Was der Verantwortliche nicht erhält

IDroot Authenticator ist ein Fork von Aegis und läuft ohne Benutzerkonto. Der Verantwortliche erhält nicht:

- den Inhalt des Tresors, Geheimnisse, Einmalcodes oder das Tresor-Passwort
- biometrische Merkmale
- Kamerabilder
- eine Gerätekennung zu Werbe- oder Analysezwecken
- Absturzberichte

Es gibt keinen Server von IDroot für diese App. Rechtsgrundlage für eine Übermittlung an den Verantwortlichen entfällt, weil diese Übermittlung nicht stattfindet.

## 3. Daten auf dem Gerät

Die folgenden Daten entstehen nur lokal und bleiben dort, solange du sie nicht selbst weitergibst:

| Daten | Zweck |
|---|---|
| Tresor mit Kontonamen, Ausstellern, Geheimnissen und Einstellungen | Einmalcodes erzeugen. Mit Passwort verschlüsselt (AES-256-GCM, scrypt). Ohne Passwort unverschlüsselt, was die App abrät. |
| Biometrisches Entsperren | Das System entsperrt einen Schlüssel im Android-Keystore. Die App speichert kein Fingerabdruck- oder Gesichtsbild. |
| Kamera oder ausgewähltes Bild | QR-Code einmalig lesen. Es wird kein Videostream gespeichert oder gesendet. |
| Zwischenablage | Nur wenn du einen Code kopierst oder einen Import aus der Zwischenablage startest. |
| Exporte und automatische Sicherungen | Nur an den Speicherort, den du über das System auswählst. |
| Lokales Prüfprotokoll | Aktionen in der App nachvollziehen. Es bleibt in der App-Datenbank. |
| Sperrhinweis | Eine lokale Benachrichtigung, falls du sie nutzt. Sie kontaktiert keinen Server. |

Gerät-zu-Gerät-Übertragungen von Android sind immer möglich. Cloud-Sicherungen von Android sind aus und erfassen den Tresor nur, wenn du sie für einen verschlüsselten Tresor einschaltest. Dann verarbeitet der Sicherungsanbieter des Geräts die Datei, nicht IDroot. Auch ein verschlüsselter Tresor ist nur so sicher wie das Passwort.

## 4. Berechtigungen

- **Kamera**, nicht zwingend: QR-Codes scannen.
- **Biometrie**: optionales Entsperren über Android.
- **Vibration**: Rückmeldung auf dem Gerät.
- **Startabschluss**: die Kachel in den Schnelleinstellungen aktualisieren. Dabei wird nichts gesendet.

Die App fordert keine Internet-Berechtigung an.

## 5. Dritte

- **Android, Gerätehersteller und ein von dir genutzter Cloud-Sicherungstransport**, sobald du eine System- oder Cloud-Sicherung zulässt.
- **GitHub**, wenn du den Quellcode, diese Texte oder ein APK von GitHub lädst. Dafür gilt die Datenschutzerklärung von GitHub.
- **idroot.org**, wenn du die Website im Browser öffnest. Dafür gilt die Datenschutzerklärung der Website, nicht diese.
- **Icon-Pakete**, die du selbst importierst, bleiben lokale Dateien.

Die App verkauft keine Daten und schaltet keine Werbung.

## 6. Speicherung und Löschung

Der Verantwortliche speichert den Tresor nicht. Löschen heißt, den Tresor in der App zu löschen, Exportdateien an den von dir gewählten Orten zu löschen und die App zu deinstallieren. System-Sicherungen musst du zusätzlich beim Anbieter des Geräts entfernen. Der Verantwortliche kann ein vergessenes Tresor-Passwort nicht zurücksetzen.

## 7. Deine Rechte

Soweit die DSGVO auf eine Verarbeitung durch den Verantwortlichen anwendbar ist, hast du die Rechte auf Auskunft, Berichtigung, Löschung, Einschränkung, Datenübertragbarkeit und Widerspruch sowie das Recht, eine Einwilligung zu widerrufen. Diese Rechte kann der Verantwortliche nicht für Tresor-Daten erfüllen, die er nicht hat. Beschwerden kannst du an eine Aufsichtsbehörde richten. Für den Sitz des Verantwortlichen ist das das Bayerische Landesamt für Datenschutzaufsicht.

## 8. Kinder

Die App richtet sich nicht an Kinder. Sie erhebt von sich aus keine Altersangabe, weil sie kein Konto anlegt.

## 9. Änderungen

Die aktuelle Fassung liegt in diesem Repository. Weiteres Verwenden der App nach einer Änderung gilt als Kenntnisnahme. Die Website-Fassung für Handles kann sich unabhängig davon ändern.
