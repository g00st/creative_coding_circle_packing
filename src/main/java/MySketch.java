import controlP5.ControlP5;
import org.opencv.core.CvType;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;
import processing.core.*;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;



import java.util.ArrayList;




public class MySketch extends PApplet {
	ControlP5 cp5;
	private float maxcirclewidth;
	private float mincirclewidth;
	private int activeCircleCount =1;

	private VideoReader videoReader;
	private boolean play = false;
	private ArrayList<PImage> videoFrames = new ArrayList<>();





	PImage img;
	String path = "https://i.kym-cdn.com/entries/icons/facebook/000/003/524/cover5.jpg";

	private float growthRate;
	private ArrayList<PVector> circles;
	public void setup() {


		videoReader = new VideoReader("C:\\Users\\geist\\Desktop\\Creative_coding\\creative_coding_circle_packing\\src\\main\\resources\\data\\BadApple.mp4");
		this.getSurface().setResizable(true);
		cp5 = new ControlP5(this);
		cp5.addSlider("maxcirclewidth")
				.setPosition(10, 10)
				.setRange(10, 100)
				.setValue(5)
				.setSize(100, 20);
		cp5.addSlider("mincirclewidth")
				.setPosition(10, 40)
				.setRange(5, 10f)
				.setValue(1)
				.setSize(100, 20);
		cp5.addSlider("activeCircleCount")
				.setPosition(10, 70)
				.setRange(1, 1000)
				.setValue(1)
				.setSize(100, 20);
		cp5.addSlider("growthRate")
				.setPosition(10, 100)
				.setRange(0.1f, 5)
				.setValue(0.1f)
				.setSize(100, 20);
		cp5.addToggle("play")
				.setPosition(10, 130)
				.setSize(100, 20)
				.setValue(false);
		cp5.addButton("RecordVideo")
				.setPosition(10, 160)
				.setSize(100, 20);

		img = loadImage(path);
		img.loadPixels();

		circles =  new ArrayList<>();

		ellipseMode(CENTER);
		img.resize(width, height);

	}

	public void settings() {
		size(500, 500);
	}

	public void frameResized(int w, int h) {

	}

	private boolean checkCollision(PVector p){
		for (var circle : circles) {
			if (dist(p.x, p.y, circle.x, circle.y) < circle.z+ p.z && p != circle) {
				return true;
			}
		}
		return false;
	}
	public void mouseReleased (){
		if (mouseButton == LEFT) {
			circles.add(new PVector(mouseX, mouseY, mincirclewidth));
		}else if (mouseButton == RIGHT ){
			circles.clear();
			img = 	 videoReader.readFrameAsPImage(this);
			img.resize(width, height);
		}
	}
	private boolean updateCircles(){
 		boolean changed = false;
		for (var circle : circles) {

			circle.z += growthRate;
			if( circle.z  < maxcirclewidth && !checkCollision(circle)) {
				changed = true;
				continue;
			}
			circle.z -= growthRate;
		}
		return changed;
	}
	private void addcircles(){
		if (circles.size() < activeCircleCount) {
			PVector p;

			for(int i = 0; i<1000; i++){
				p = new PVector(random(mincirclewidth, width - mincirclewidth), random(mincirclewidth, height - mincirclewidth), mincirclewidth);
				if(checkCollision(p)){
					continue;

				}
				circles.add(p);
				break;
			}
		}

	}
	private void addimgCircel(){
		if (circles.size() < activeCircleCount) {
			PVector p;

			    p = getBrightestPixel();
				if (p == null){
					return;
				}
				//System.out.println(p);
				circles.add(p);

			}
		}



	private PVector getBrightestPixel() {

		float maxBrightness = 0;
		ArrayList<PVector> brightestPixelCoordinates = new ArrayList<PVector>();
		PVector brightestPixelCoordinate = new PVector(0, 0);

		for (int x = 0; x < img.width; x++) {
			for (int y = 0; y < img.height; y++) {
				int index = x + y * img.width;

				float brightness = brightness(img.pixels[index]);

				if (brightness > maxBrightness ){
					if( checkCollision(new PVector(x, y, mincirclewidth))){

						img.pixels[index] = color(0, 0, 0);
						continue;
					}
					//System.out.println("found");
					maxBrightness = brightness;
					brightestPixelCoordinates.clear();
					brightestPixelCoordinates.add(new PVector(x, y));
				}else if (brightness == maxBrightness){
					if( checkCollision(new PVector(x, y, mincirclewidth))){

						img.pixels[index] = color(0, 0, 0);
						continue;
					}
					brightestPixelCoordinates.add(new PVector(x, y));
				}
			}
		}
		img.updatePixels();

		if (maxBrightness == 0) {
			return null;
		}
		if (!brightestPixelCoordinates.isEmpty()) {
			brightestPixelCoordinate = brightestPixelCoordinates.get((int) random(brightestPixelCoordinates.size()));
		}
		return brightestPixelCoordinate;
	}





	public void RecordVideo(){
		// Create a VideoWriter object
		int count = 0;


		VideoReader videoReader = new VideoReader("C:\\Users\\geist\\Desktop\\Creative_coding\\creative_coding_circle_packing\\src\\main\\resources\\data\\BadApple.mp4");
		PVideoWriter writer = new PVideoWriter("C:\\Users\\geist\\Desktop\\Creative_coding\\creative_coding_circle_packing\\src\\main\\resources\\data\\output.mp4", videoReader.framerate , width, height);
		PImage frame =  videoReader.readFrameAsPImage(this) ;
		while (frame != null){
			circles.clear();
			img = frame;
			img.resize(width, height);
			System.out.println(count);
			addimgCircel();
			while(updateCircles()){
				addimgCircel();
			}
			PGraphics surf = createGraphics(width, height);
			surf.setSize( width,height);
			surf.beginDraw();
			surf.background(0);
			surf.noStroke();
			for (var circle : circles) {

				surf.fill(255, 255, 255);
				surf.ellipse(circle.x, circle.y, circle.z*2, circle.z*2);
			}
			surf.endDraw();
			circles.clear();
			writer.writeFrame(surf.get());
			count++;
			frame = videoReader.readFrameAsPImage(this);
		}
		writer.close();

	}


	public void draw(){
		img.resize(width, height);


		background(0);
		image(img, 0, 0, width, height);

		addimgCircel();
		if(!updateCircles() && play){
			img = 	 videoReader.readFrameAsPImage(this);
			circles.clear();
			if (img == null){
				play = false;
			}
			img.resize(width, height);
		}


		for (var circle : circles) {
			fill(255, 0, 0);
			ellipse(circle.x, circle.y, circle.z*2, circle.z*2);


		}
	}
}
