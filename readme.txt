How to play the Game of the Amazons (JavaFX version 1.0)

Ensure files are sorted like this
----PACKAGE_NAME
--------src
------------Amazons
----------------AmazonsServer.java
----------------Launcher.java
----------------LocalVersion.java
----------------OnlineVersion.java
------------images
----------------black.png
----------------white.png


To play the online version of the game, please perform the following steps
1. Find your IPv4 address. 
	This can be done by opening a command prompt (Win+R, and type in "cmd") and using the command "ipconfig"
2. In AmazonsServer.java, replace the address of the bindAddress variable with your IPv4 address.
3. In OnlineVersion.java, replace the address of the socket variable in the startButton.setOnAction handler with your IPv4 address.
4. If you have not already opened a command prompt, do so.
5. Copy the location of your Amazons package (For example, C:\Users\YOUR_NAME\eclipse-workspace\Testing\src\Amazons)
6. Type "cd PATH_TO_AMAZONS_FOLDER" into the cmd, replacing the path with the location of the Amazons package.
7. Type "java AmazonsServer.java" into the cmd
8. Run two instances of Launcher.java and select "Online Play" for both.
9. After selecting "Start," both should connect to the server set up in the cmd and be playable!

To play the online version of the game on one computer as opposed to two connected to the same network, please perform the following steps
1. Perform all steps required to play the online version of the game
2. Create another window in Eclipse (Window ==> New Window at the top of the screen).
3. Run Launcher.java in both windows.
4. Select "Online Play" for both launchers.

To play the offline version of the game, perform the following steps
1. Run Launcher.java.
2. Select "Local Play" in the launcher

