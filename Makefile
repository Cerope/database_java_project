all:

generationAleatoire:
	cp ojdbc7.jar bin/
	javac -d bin -classpath bin/ojdbc7.jar -sourcepath src src/MainTest.java
	java -classpath bin::bin/ojdbc7.jar MainTest

userInterface:
	cp ojdbc7.jar bin/
	javac -d bin -classpath bin/ojdbc7.jar -sourcepath src src/Main.java
	java -classpath bin::bin/ojdbc7.jar Main

dropTable:
	cp ojdbc7.jar bin/
	javac -d bin -classpath bin/ojdbc7.jar -sourcepath src src/MainDrop.java
	java -classpath bin::bin/ojdbc7.jar MainDrop
