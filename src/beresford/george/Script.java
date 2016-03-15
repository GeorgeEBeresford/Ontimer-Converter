package beresford.george;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Script
{
    private ArrayList<String> contents = new ArrayList<String>(20);
    private String fileName;
    private String binPath;
    
    public Script (File script)
	{
        setContents(script);
        setBinPath(script);
    }
    
    // Hide this constructor as it should never be used
    @SuppressWarnings("unused")
	private Script ()
	{
    }
    
    private void setContents (File script)
	{
        try
		{
            Scanner fileScanner = new Scanner(script);
            // Whilst the scanner has another line, add it to the contents.
            while (fileScanner.hasNextLine())
			{
                String[] commandsSplit = fileScanner.nextLine().split(";");
                for (String command : commandsSplit)
				{
                    if (!(command.equals("")))
					{
                        contents.add(command);
                    }
					else if ((command.length() > 8) && (command.substring(0, 8).equals("ontimer ")))
					{
                        contents.add(command.substring(7));
                    }
                }
            }
            fileName = script.getName();
            fileScanner.close();
        }
		catch (FileNotFoundException ex)
		{
            // Pass contents to the garbage collector
            contents = null;
        }
    }
    
    /**
     * Returns the contents list
     * @return a list of commands
     */
    public ArrayList<String> getContents ()
	{
        return contents;
    }

    /**
     * Returns the name of the script
     * @return Returns a string w   hich represents the filename of the script.
     */
    public String getName ()
	{
        return fileName;
    }
    
    private void setBinPath (File file)
	{
        try
		{
            binPath = "bin\\" + file.getParent().substring(4);
        }
		catch (StringIndexOutOfBoundsException ex)
		{
            binPath = "bin\\";
        }
    }
    
    public String getBinPath ()
	{
        return binPath;
    }
}
