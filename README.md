# PartialSShot
## Motivation
Sometimes you see something on your screen what you'd like to share with someone. The usual way to do it is that
you press PRTSCRN, open a graphic editor, paste the image, crop it, save it, upload it, copy a link and finally share it.
Uh... That's a lot of steps!

## Solution
PartialSShot allows you to take a screenshot just by hitting a special shortcut (SHIFT+CTRL+PRTSCRN) and clicking two
points on the monitor. It will automatically take a screenshot, upload it and copy the link to clipboard.

## Download
Download [PartialSShot-1.0.0.jar (1.6 MB)](https://github.com/tzima/PartialSShot/blob/master/PartialSShot/dist/PartialSShot-1.0.0.jar?raw=true).

Download [PartialSShot-1.0.1.jar (1.6 MB)](https://github.com/tzima/PartialSShot/blob/master/PartialSShot/dist/PartialSShot-1.0.1.jar?raw=true).

## Install
You don't need to install anything. Just place the JAR file into some directory (preferably a new one because application
will store configuration and log files there).

## Configuration
Before the GUI is done and ready, you need to supply a configuration file. Creating it from scratch would be too
difficult. Just run the application - it will generate a well-documented template for you.

Configuration is stored in partialsshot.json (inside the same directory where the JAR file is located). Here's how
it looks like:
```
{
	/*
	 * Write all publishers through which you wish to public your content (all at once).
	 * Currently supported options are:
	 *    - "DROPBOX": for Dropbox
	 *    - "LOCAL": for storing into a local folder
	 */
	"publishers": ["LOCAL"],
	
	/*
	 * Choose your camera (the modul for taking screenshots).
	 * Currently supported options are:
	 *   - "RECTANGULAR_CAMERA": take a screenshot of rectangle based on two diagonal points
	 */
	"camera": "RECTANGULAR_CAMERA",
	
	/*
	 * Do you wish to generate a link once a screenshot has been taken? If so, it'll be
	 * automatically copied into the clipboard.
	 * Currently supported options are:
	 *   - "NONE": do not generate a link
	 *   - "DROPBOX": generate a link to the Dropbox
	 */
	"generateLink": "DROPBOX",
	
	/*
	 * ## NOT SUPPORTED IN THIS VERSION ## 
	 * Ask for a name of the generated file?
	 * Currently supported options are:
	 *   - "true": yes, ask for name
	 *   - "false": no, don't ask for name
	 */
	"askForName": "false",
	
	/*
	 * Configuration for a LOCAL publisher. It only takes affect in the case
	 * LOCAL publisher is used.
	 */
	"publisher_Local": {
		/* To which directory do you wish to save taken screenshots? */
		"directory": "/home/user/Pictures/Screenshots/"
	},
	
	/*
	 * Configuration for a DROPBOX publisher. It only takes affect in the case
	 * DROPBOX publisher is used.
	 *
	 * Example configuration (using the default values below):
	 *   I've a directory called "Screenshots" in my public Dropbox directory.
	 *   I'm using a Linux-based operating system and my username is "user". I
	 *   know my Dropbox user ID is "00000000".  
	 */
	"publisher_Dropbox": {
		/* Path to the local Dropbox's public directory. */
		"publicDirectoryRoot": "/home/user/Dropbox/Public/",
		
		/* Name of the directory INSIDE the public directory. */
		"relativeTargetDirPath": "Screenshots/",
		
		/* ID of the user (you see it in your public links). */
		"userID": "00000000",
		
		/* Do NOT change this unless you now what you're doing. */
		"publicDirLink": "https://dl.dropboxusercontent.com/u/%USER_ID%/"
	}
}
```
The current version supports just two content publishers: *DROPBOX* and *LOCAL*. For taking the full advantage of
PartialSShot, you definitely want to use it together with Dropbox. *LOCAL* content publisher just places screenshots
in the local directory.

**Hint for Windows users**: Don't use a single backslash character (`\`) in paths! Always use a normal slash (`/`) or a double backslash (`\\` - which is equivalent to a `\`). Don't mind me, don't mind JSON, mind Microsoft. These are the guys who came up with an idea of using `\` whilst much older UNIX used `/`. In C, which existed way too earlier than Microsoft was established, backslash (`\`) has been used for escaping (writing special characters). I believe Microsoft needed something different than `/` so they came up with `\` and fucked up everything. Backslash (`\`) is still used for escaping in every conventional programming language (Java, C/C++, JavaScript, C# **too**) and because of that fucking idea to use `\` in paths, you've to write double-backslash (`\\`) everywhere.

**Hint for Dropbox users**: Do you have troubles with setting up URL and user ID? It's pretty easy, actually. Open your Dropbox client, go to the directory called Public and choose any file located in there (if there isn't one, just create it). Then open context menu of that file (using right mouse button) and select `Dropbox -> Copy public`. Now copy this link somewhere. Lets say you've got something like this: `https://dl.dropboxusercontent.com/u/12345678/test.txt`. In this case, `12345678` is your user ID and format of the link is `https://dl.dropboxusercontent.com/u/%USER_ID%/`. It wasn't that terrible, right?




## Taking a screenshot
*This assumes you've PartialSShot running and with Dropbox configured. You should see the icon in tray.*

- hit LSHIFT+LCTRL+PRTSCRN (left shift, left control & print screen)
- click on two points (rectangle diagonal)
- press CTRL+V to paste the link (e.g. to chat)

**Hint**: Instead of clicking, you can use LSHIFT+LCTR+PRTSCRN for selecting points too! Just make sure your mouse
pointer is on a proper location.
