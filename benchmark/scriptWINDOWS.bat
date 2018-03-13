@del /Q result.txt
@del /Q err.txt

@echo "Create bin directory"
@md bin

@echo "Create class files"
@javac -d "./../bin" ./../src/Main.java

for %%f in (./instances/*.tsp) do (
	@java -cp "./../bin" tsp.Main %%f -t 60 >> standardOutput.txt 2>> errorOutput.txt
	@timeout /T 1 /NOBREAK
)
