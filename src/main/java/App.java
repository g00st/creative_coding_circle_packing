

import processing.core.PApplet;



public class App {

	public static void main(final String[] args) {
		System.setProperty("gstreamer.library.path", "libs/windows-amd64/gstreamer-1.0");
		String[] appletArgs = new String[] { "MySketch" };
		PApplet.main(appletArgs);
	}
}