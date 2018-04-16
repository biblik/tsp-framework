@del /Q standardOutput.txt
@del /Q errorOutput.txt
@echo "Create bin directory"
@md ..\bin
@echo "Create class files"

#set Path=%Path%;C:\Program Files\Java\jdk-9\bin

@javac -d "./../bin" -sourcepath "./../src" ./../src/tsp/*.java

java -cp ".\..\bin" tsp.Main .\..\instances\d198.tsp -t 60 >> standardOutput.txt 2>> errorOutput.txt
java -cp ".\..\bin" tsp.Main .\..\instances\d198.tsp -t 60 >> standardOutput.txt 2>> errorOutput.txt
java -cp ".\..\bin" tsp.Main .\..\instances\d657.tsp -t 60 >> standardOutput.txt 2>> errorOutput.txt
java -cp ".\..\bin" tsp.Main .\..\instances\eil10.tsp -t 60 >> standardOutput.txt 2>> errorOutput.txt
java -cp ".\..\bin" tsp.Main .\..\instances\eil101.tsp -t 60 >> standardOutput.txt 2>> errorOutput.txt
java -cp ".\..\bin" tsp.Main .\..\instances\eil51.tsp -t 60 >> standardOutput.txt 2>> errorOutput.txt
java -cp ".\..\bin" tsp.Main .\..\instances\kroA100.tsp -t 60 >> standardOutput.txt 2>> errorOutput.txt
java -cp ".\..\bin" tsp.Main .\..\instances\kroA150.tsp -t 60 >> standardOutput.txt 2>> errorOutput.txt
java -cp ".\..\bin" tsp.Main .\..\instances\KroA200.tsp -t 60 >> standardOutput.txt 2>> errorOutput.txt
java -cp ".\..\bin" tsp.Main .\..\instances\lin318.tsp -t 60 >> standardOutput.txt 2>> errorOutput.txt
java -cp ".\..\bin" tsp.Main .\..\instances\pcb442.tsp -t 60 >> standardOutput.txt 2>> errorOutput.txt
java -cp ".\..\bin" tsp.Main .\..\instances\rat757.tsp -t 60 >> standardOutput.txt 2>> errorOutput.txt 