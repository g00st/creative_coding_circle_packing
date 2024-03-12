import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import processing.core.PApplet;
import processing.core.PImage;

import static processing.core.PConstants.RGB;

public class VideoReader {
    public String path;
    public VideoCapture cap;
    public int width;
    public int height;
    public boolean looping = false;
    public int framerate ;

    VideoReader(String path) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        this.path = path;
        cap = new VideoCapture();
        cap.open(path);
        cap.set(Videoio.CAP_PROP_CONVERT_RGB, 1); // Add this line
        framerate = (int) cap.get(Videoio.CAP_PROP_FPS);

        if (!cap.isOpened()) {
            System.out.println("Error: Unable to open video file.");
        }
    }

    public Mat readFrame() {
        System.out.println("Reading frame");
        Mat frame = new Mat();
        if (!cap.read(frame) ) {
            if (looping) {
            cap.set(Videoio.CAP_PROP_POS_FRAMES, 0);
            cap.read(frame);}
            else {
                System.out.println("Error: Unable to read frame.");
                return null;
            }
        }


        return frame;
    }

    public PImage readFrameAsPImage(PApplet applet) {
        Mat frame = readFrame();
        if (frame == null) {
            return null;
        }
        PImage img = applet.createImage(frame.width(), frame.height(), RGB);


        byte[] data = new byte[frame.rows() * frame.cols() * frame.channels()];
        frame.get(0, 0, data);

        img.loadPixels();

        for (int i = 0; i < img.pixels.length; i++) {
            int b = data[i*3] & 0xFF;
            int g = data[i*3+1] & 0xFF;
            int r = data[i*3+2] & 0xFF;
            img.pixels[i] = applet.color(r, g, b);
        }
        img.updatePixels();

        return img;
    }
}
