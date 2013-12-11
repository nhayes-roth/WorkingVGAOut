.PHONY:
compile: CharacterMaps.java
	javac CharacterMaps.java

.PHONY:
run: CharacterMaps.class
	java CharacterMaps

.PHONY:
clean:
	rm -f *.class
