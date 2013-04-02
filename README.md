BAZAM - A Shazam-like Application in Java
==========================
This is a standalone JAR application that can index audio files into a library, from which you can diff
short audio snippets (less than a minute) against the library. It will return the closest match in a popup. 

However, this is really badly written code back when I was a bad programmer. So do not expect it to be able to index *anything* longer than a few minutes (let alone a folder of long tracks). There is no good organization of code; it is quite sloppy.

Build
===========
Install ant: 
`sudo apt-get install ant1.7`

Run: 
`ant clean build-jar`

The jar file will be in `./bin/jar`. For some reason I created the manifest file incorrectly, so you have to run the jar with 

`java -cp bazam.jar bazam.StartFrame`

TODO
==========
Basically, fix everything. I can't believe how badly written (and slow) this is. I might get around to this once someday.

