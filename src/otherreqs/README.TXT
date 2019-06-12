HoI Icon Extractor v1.1

Open a terminal/command prompt/powershell/whatever window here, and run the following command with arguments as specified

	java -jar HoiIconListener.jar [relative path to the gfx file] [name of the output, can be multiple words]

So, for example, if I wanted to extract the focuses, I could do:
	java -jar HoiIconListener.jar interface/goals.gfx focuses

If it doesn't work, adjust the hoiDirectory attribute in config.json

If it does, output will be in a file named [name of output].html, which you can open in your favourite browser that hopefully isn't Chrome.