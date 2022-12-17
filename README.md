Archived as no further changes will be made to the repository. Other developers are welcome to fork the repository and implement changes

# Ontimer Converter for Makermodule

## Introduction
One of the commands which was provided by the modification called "Makermodule" is called "ontimer". It's immensely useful. Whilst using the "wait" command, you are forced to sit around whilst your scripts execute.
The "ontimer" command allows you to perform other commands whilst the script is performing. This converter allows you to take existing scripts and to transform them into ontimer scripts.

## Automatic Delays

When you run the batch file, it'll ask you for a number. This should be an integer not a string (in short, it should be made up of numbers ranging from 0 to 9 not made up of a-z or anything else.)
Entering an automatic delay of 20 would convert the following script:

```
mmarkfoot;
mplace factory/catw2_b;
mplace factory/catw2_b;
mmove 12 0 0
```

into the following:

```
ontimer 0 "mmarkfoot";
ontimer 20 "mplace factory/catw2_b";
ontimer 40 "mplace factory/catw2_b";
ontimer 60 "mmove 12 0 0"
```

This number will represent how many milliseconds (not centiseconds aka 100ths of a second) you want to wait between each command. It accepts 0 as a number in case you want to rely on your own timings with waits.

## Manual Delays

As well as the per-command delays, you can also use wait commands in the script itself. The ontimer will take this into consideration when it's working out the time to execute the next command.
If you specified 30 to be your automatic delay, the following script would convert from:

```
mmarkfoot;
mplacefx env/fire 250;
wait 100;
mmove 100
```

to the following:

```
ontimer 0 "mmarkfoot";
ontimer 30 "mplacefx env/fire 250";
ontimer 130 "mmove 100"
```

## Subtractive Waits

Subtractive waits are also compatible with the ontimer. Rather than getting you infinitely stuck like you normally would with a negative wait, it will take away the specified amount from the next automatic delay.
As an example, if you have an automatic delay of 20ms and you use the following in a script:

```
mmarkfoot;
wait -2;
mplace factory/catw2_b
```

The converter will output:

```
ontimer 0 "mmarkfoot";
ontimer 0 "mplace factory/catw2_b"
```

## Usage

Before you run the batch file, you'll need to put any files which need converting into the "src" folder.
Once they are all set to go, simply run the batch file and proceed to read step 1 to 3 if you're not sure of how the converting process works.
The converted script will be spit out into the provided "bin" folder (you'll be prompted to create one if you forgot to extract it.)
Once you're happy with the script and don't want to make any more changes to it, or want to pause the script development and want to use a different timing for a different script, move it out of the "bin" folder.
I've provided a folder called "archive" if you can't think of a name for a folder to put them in.
Don't forget to include the folder names if you're going to execute the scripts directly from them.
