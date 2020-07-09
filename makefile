build:
	javac src/pokemon/**/*.java -cp src/ -d out/ 

play:
	java -cp out/ pokemon/GameDriver
