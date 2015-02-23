# CrazyPixels
Cellular autotmata screensaver with a colourful rock-paper-scissors implementation.

To configure the way the cellular automata behave there is a CrazyPixelSettings.ini file that contain variables that control many aspects of the calculations.
Additionally, there are a number of preset combinations that yield interesting results. These can be accessed by passing the command line argument "/c" for "configure".

This design was originally inspired by the article and video here: http://www.gamedev.net/blog/844/entry-2249737-another-cellular-automaton-video/


But I found that if the settings were tweaked you could produce even more interesting results and patterns!

## DEMO

10min (with music): https://www.youtube.com/watch?v=TS1BvGaN14g

## TODO:

0) Make the colour interval swap a sinusoidal function?

1) Fix issue with screen resolution / multi monitor setups.

2) Build into a .jar, wrap in a .exe, rename to .scr and it's a screensaver.
(Would be nice to account for /p /s /c command line args)

3) Add the ability to click and drag to paint swathes of colours on the grid.
