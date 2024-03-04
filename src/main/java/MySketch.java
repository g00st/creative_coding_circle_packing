import processing.core.PApplet;
import processing.opengl.PShader;
import controlP5.*;


public class MySketch extends PApplet {
	ControlP5 cp5;
	public void setup() {
		this.getSurface().setResizable(true);
		cp5 = new ControlP5(this);
		cp5.addSlider("maxframe")
				.setPosition(10, 10)
				.setRange(1, 10)
				.setValue(5)
				.setSize(100, 20);
		cp5.addSlider("decay")
				.setPosition(10, 40)
				.setRange(0.001f, 0.005f)
				.setValue(5)
				.setSize(100, 20);
	}

	public void settings() {
		size(500, 500);
	}

	public void frameResized(int w, int h) {

	}

	public void draw(){
		background(64);
		ellipse(mouseX, mouseY, 20, 20);
	}
}
