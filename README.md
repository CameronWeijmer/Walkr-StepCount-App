# Walkr-StepCount-App

## 1. Änderungen an der Aufgabenstellung

Ich musste an der Aufgabenstellung ändern, dass man einen Start und Stopp-Button hat. Stattdessen habe ich implementiert, dass die verbrauchten Kalorien und die KM Anzeige automatisch aktualisiert werden. Während der Implementierung habe ich gemerkt, dass diese Buttons nicht notwendig sind, da man die Schritte nicht stoppen oder pausieren möchte. Das Klassendiagramm musste ich ebenfalls angepasst werden, da die Buttons nicht mehr vorhanden waren. Zusätzlich habe ich neue Methoden implementiert.

## 2. Linter Command

Ich habe den Linter installiert und ein `checkStyle.XML` erstellt, welches überprüft, dass die Methoden nicht länger als 30 Zeilen sind. Hier ist der Code dazu:

```
<?xml version="1.0" ?>

<!DOCTYPE module PUBLIC
    "-//Checkstyle//DTD Checkstyle Configuration 1.2//EN"
    "https://checkstyle.org/dtds/configuration_1_2.dtd">

<module name="Checker">
    <module name="TreeWalker">
        <module name="MethodLength">
            <property
                name="max"
                value="30" />
        </module>
    </module>
</module>
```
Ausserdem musste ich ein Plugin in Android Studio installieren, das heisst: # CheckStyle-IDEA

## 3. Link zum Repo ist: https://github.com/CameronWeijmer/Walkr-StepCount-App.git
