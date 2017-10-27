### Vor der Benutzung:

> - php installieren
> - java installieren
> - **das Programm braucht Admin-Rechte**

> - in den [Google APIs](console.developers.google.com) muss ein Projekt angelegt sein
> - hier muss ein Dienstkontoschluessel erstellt werden
> - die .p12-Datei im resources-Ordner speichern

### Bei Aenderung des Codes oder zur Erstellung der jar-Datei:

- Maven installieren
- im cmd:
1. zum Ordner changeIP navigieren
2. mvn install

### Zum haendischen Ausfuehren des Programms:

- im cmd:
1. in den Ordner target vom Projekt changeIP navigieren
2. java -jar changeIP-0.0.1.jar


### Automatische Ausfuerung mithilfe des Windows Task Scheduler (Aufgabenplanung)

0. run.bat anlegen, falls nicht vorhanden
  - in die Datei folgendes eintragen
```
java -jar changeIP-0.0.1.jar
```

1. einfache Aufgabe erstellen
  - Name: beliebiger Name

2. Trigger einstellen
  - taeglich, Uhrzeit einstellen

3. Aktionen
  - Programm starten
  - zur run.bat im target-Ordner navigieren und auswaehlen
  - Starten in: Pfad des target-Ordners


### Credits
Das Programm nutzt die [fb_tools](http://www.mengelke.de/Projekte/FritzBoxTools) von mengelke.de




