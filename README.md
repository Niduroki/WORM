Programmier-Praktikum Team 101
======
Also see Classes-Sketch.png

Some magical key-combinations:
 - Esc to return to main menu
 - Mousewheel to scroll weapons
 - S to save a game
 - X to sync an online game
 - M to save the current map (useful to create new map files)

JavaDoc
-----
Execute this in team101 directory to create javadoc:
```
javadoc -private de.hhu.propra14.team101.Networking.Exceptions de.hhu.propra14.team101.Savers de.hhu.propra14.team101.Physics de.hhu.propra14.team101.TerrainObjects de.hhu.propra14.team101.GUIElements de.hhu.propra14.team101.Weapons de.hhu.propra14.team101 de.hhu.propra14.team101.Networking -sourcepath src -classpath libraries/easyogg.jar:libraries/jogg-0.0.7.jar:libraries/jorbis-0.0.15.jar:libraries/snakeyaml-1.13.jar -d doc
```

Importing into IntelliJ
----
Also see the images in ReadMe imgs

1. Click on Import Project
2. Search the folder cloned from git and click it once
3. Click next at "Create from existing sources"
4. Click next at project name (you may rename the project, if you like)
5. Click next at sources directory selection
6. Click next at library selection
7. Click next at module structure
8. Select a JDK and click next (if you don't have a JDK yet, click the plus in the top left, and select the folder from your drive, where it is)
9. Click finish at framework detection
10. Set the language level to java 7 (see Step10.png and Step11.png) and reload the project when IntelliJ offers you, to do so
11. Set the resources directory as a resources root