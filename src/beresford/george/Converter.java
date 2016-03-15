package beresford.george;

import java.io.File;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.Scanner;
import java.io.FileWriter;

/**
 * 
 * @author (George Beresford)
 * @version (02.03.2016)
 */
public class Converter
{
    private ArrayList<Script> scripts = new ArrayList<Script>(10);
    
    /**
     * Creates a new converter class which will be used to format each command
     */
    public Converter ()
	{
        iterateConversions();
    }
    
    private Integer getDelay ()
	{
    	System.out.print("Please enter a delay (ms): ");
        Scanner inputStream = new Scanner(System.in);
        Integer delay = null;
        try
		{
            // Try to convert the input into an integer
            delay = Integer.parseInt(inputStream.nextLine());
        }
		catch (Exception ex)
		{
            System.out.println("Error with parsing input.\nIf you don't know why you received this message\nthen contact the developer, quoting the exact input that you used");
        }
        inputStream.close();
        return delay;
    }
    
    private void iterateConversions ()
	{
        getScripts();
        // Retrieve a value representing the delay between commands
        Integer delayIncrement = getDelay();
        if (delayIncrement != null)
        {
        	// Go through each script
            for (Script script : scripts)
    		{
                String convertedScript = convertScript(script, delayIncrement);
                writeScriptToDisk(script, convertedScript);
            }
        }
    }
    
    private String convertScript (Script script, Integer delayIncrement)
    {
    	System.out.println("Converting " + script.getName());
        String commands = "";
        int index = 0;
        int delay = 0;
        int previousOntimer = 0;
        boolean skipNewLine = false;
        // Add each command to a list of commands
        for (String command : script.getContents())
		{
        	if (command.length() == 6 && command.substring(0, 6).equals("wait 0"))
        	{
        		delay -= delayIncrement;
        		skipNewLine = true;
        	}
        	else if (command.length() > 5 && command.substring(0, 5).equals("wait "))
			{
        		try
				{
        			// Add the wait value to the delay (multiplied by 10 to make centiseconds into milliseconds)
        			delay += (Integer.parseInt(command.substring(5)) * 10);
        		}
				catch (Exception ex)
				{
        			System.out.println("Error in " + script.getName() + " on line " + (index + 1) + ". " + command.substring(4) + " is not a valid wait value.");
        		}
    			skipNewLine = true;
        	}
        	// If the command is actually a comment
			else if (command.length() > 2 && command.substring(0, 2).equals("//"))
			{
				// add the comment to the new script as-is.
        		commands += (command);
			}
        	// If the command is already ontimer-formatted
			else if (command.length() > 8 && command.substring(0, 8).equals("ontimer "))
			{
				String[] splitCommand = command.split(" ");
				Integer parsedDelay = null;
				try
				{
					// find the existing ontimer command's current delay
					parsedDelay = new Integer(splitCommand[1]);
				}
				catch (IllegalFormatException ex)
				{
					System.out.println("Invalid delay amount in script \"" + script.getName() + "\" on line " + index + ". Input given: " + command);
				}
				if (parsedDelay != null)
				{
					/*
					 *  If the delay was successfully found then subtract the difference between
					 *  the new delay and the delay from the previously found ontimer command.
					 */
					int delayDifference = parsedDelay.intValue() - previousOntimer;
					previousOntimer = parsedDelay.intValue();
					// Add our current default-delay to our difference
					delay += delayDifference;
					// Format the command with our new information in a new ontimer.
					commands += "ontimer " + delay + " " + command.substring(12);
					delay += delayIncrement;
				}
				else
				{
            		delay += delayIncrement;
				}
			}
        	/*
        	 *  if the command does not affect delays and is not a comment then add it to
        	 *  the converted script with the new delay taken into account.
        	 */
			else
			{
        		commands += "ontimer " + delay + " " + command;
        		delay += delayIncrement;
        	}
        	index++;
        	// If there is still more of the script to convert and we haven't been told explicitly to not print a new line then do it
        	if (index < script.getContents().size() && skipNewLine == false)
        	{
        		commands += "\r\n";
        	}
        	else
        	{
        		skipNewLine = false;
        	}
        }
        return commands;
    }
    
    private void writeScriptToDisk (Script script, String commands)
    {
    	try
		{
            File directories = new File(script.getBinPath());
            directories.mkdirs();
            File path = new File(directories, script.getName());
            FileWriter outputStream = new FileWriter(path);
            outputStream.write(commands);
            outputStream.close();
        }
		catch (Exception ex)
		{
            System.out.println("Error. Could not print script to \"" + script.getBinPath() + "\\" + script.getName());
        }
    }
    
    private void getScripts ()
	{
    	try
		{
    		// Go through each folder and add the script to the list of scripts
    		for (File scriptFile : new File("src/").listFiles())
			{
    			getScripts(scriptFile);
    		}
    	}
		catch (NullPointerException ex)
		{
    		System.out.println("No src/ folder found. I've created one for you. Put your source scripts inside it.\nThe converted scripts will go to a bin/ folder");
            File srcFolder = new File("src/");
            srcFolder.mkdirs();
    	}
    }
    
    private void getScripts (File scriptFile)
	{
        //If the file is indeed a file then add it to the list of files
        if (scriptFile.isFile())
		{
            System.out.println("Found " + scriptFile.getName());
            scripts.add(new Script(scriptFile));
        }
		else
		{
            // if it is not a file then enter it and try to add all of its children.
            for (File innerScript : scriptFile.listFiles())
			{
                getScripts(innerScript);
            }
        }
    }
    
    public static void main (String[] args)
	{
        new Converter();
    }
}
