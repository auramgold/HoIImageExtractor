HoI Icon Extractor v1.2
	Changes
		Added a config option to ignore certain out-of-place gfx things.
		Added a command line argument system, the only argument currently added being -ignore:x which accomplishes the same thing as the config ignore.
		Changed the names so that spaces in a name are turned to underscores, and if there is no name defined, it defaults to "default"
		Made it so if a dds file specified in the .gfx is corrupt, the program ignores it as if it is a missing file instead of crashing.
		

INSTRUCTIONS

Open a terminal/command prompt/powershell/whatever window here, and run the following command with arguments as specified

	java -jar HoiIconListener.jar [relative path to the gfx file] [name of the output, can be multiple words]

So, for example, if I wanted to extract the focuses, I could do:
	java -jar HoiIconListener.jar interface/goals.gfx focuses

If it works, it will output an HTML file named [name of the output].html which can be opened in your favourite browser.

If it doesn't work out of the box, either A: check your java path variables, or B: check if the HoI directory the config comes with stock is the correct install location.


COMMAND LINE ARGUMENTS
	-ignore:x	Ignores whatever gfx entry x is, works just like the ignore entry in config.json.


CONFIG
	hoiDirectory : The absolute path to the main directory HoI4 is installed in
	ignore: A JSON Array of GFX entries to ignore, because they are out of place or otherwise irrelevant.