/*
 * Copyright (C) 2019 auramgold
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package hoiiconlister;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import org.json.JSONObject;

/**
 *
 * @author auramgold
 */
public class HoiIconLister
{

	/**
	 * @param args the command line arguments
	 * @throws java.io.IOException
	 */
	public static void main(String[] args) throws IOException
	{
		Path currentRelativePath = Paths.get("");
		String outputPath = currentRelativePath.toAbsolutePath().toString()+"/";
		
		File configLoc = new File(outputPath + "config.json");
		String configStr = loadTextFile(configLoc);
		JSONObject config = new JSONObject(configStr);
		
		
		String gamePath = config.getString("hoiDirectory");
		
		
		String gfxRelPath = args[0];
		String folderSectionName = "";
		for(int i = 1; i < args.length; ++i)
		{
			folderSectionName += args[i];
		}
		
		Pattern extractNameLoc = Pattern.compile("(?s)[sS]priteType.*?\\=.*?\\{.*?name.*?\\=.*?\"(\\w+?)\".+?texturefile.*?=.*?\"(.+?)\".*?\\}");
		
		File directory = new File(outputPath+"images");
		if (!directory.exists())
		{
			directory.mkdir();
		}
		
		directory = new File(outputPath+"images/"+folderSectionName);
		if (!directory.exists())
		{
			directory.mkdir();
		}
		
		File gfxInputFile = new File(gamePath+gfxRelPath);
		String gfxInputText = loadTextFile(gfxInputFile);
		
		String outBody = "\t<div class=\"supercontainer\">";
		
		Matcher textExtractor = extractNameLoc.matcher(gfxInputText);
		while(textExtractor.find())
		{
			String name = textExtractor.group(1);
			String path = textExtractor.group(2);
			
			
			File ddsImgFile = new File(gamePath + path);
			String relOutPath = "images/" + folderSectionName + "/" + name + ".png";
			File outImgFile = new File(outputPath + relOutPath);
			
			System.out.println(name);
			
			try
			{
				BufferedImage currentImg = importDDS(ddsImgFile);
				ImageIO.write(currentImg, "png", outImgFile);
			}
			catch(FileNotFoundException ex)
			{
				System.out.println("	Image not found, ignoring.");
			}
			
			outBody += generateIconSection(name, relOutPath);
		}
		
		outBody += "</div>";
		
		File templateFile = new File(outputPath + "template.html");
		String templateFileIn = loadTextFile(templateFile);
		String templateFileOut = templateFileIn.replace("|||BODY|||", outBody);
		
		PrintWriter out = new PrintWriter(outputPath + folderSectionName + ".html");
		out.println(templateFileOut);
		out.close();
	}
	
	public static BufferedImage importDDS(File path) throws FileNotFoundException, IOException
	{
		try (FileInputStream fis = new FileInputStream(path))
		{
			byte [] buffer = new byte[fis.available()];
			fis.read(buffer);
			int [] pixels = DDSReader.read(buffer, DDSReader.ARGB, 0);
			int width = DDSReader.getWidth(buffer);
			int height = DDSReader.getHeight(buffer);
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			image.setRGB(0, 0, width, height, pixels, 0, width);
			return image;
		}
	}
	
	public static String loadTextFile(File path) throws IOException
	{
		String content = new String(Files.readAllBytes(path.toPath()));
		return content;
	}
	
	public static String generateIconSection(String name, String imgPath)
	{
		String out = "\r\n\t\t<section class=\"container\">\n" +
"			<div class=\"imagewrap\"><img src=\"" + imgPath + "\" alt=\"" + 
				name.replaceAll("_", "&#8203;_") + "\" /></div>\n" +
"			<div class=\"namewrap\">\n" +
"				<div class=\"name\">" + name.replaceAll("_", "&#8203;_") + "</div>\n" +
"				<button onclick=\"copyStringToClipboard('" + name + "')\">Copy!</button>\n" +
"			</div>\n" +
"		</section>";
		return out;
	}
	
}
