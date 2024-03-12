import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.videoio.VideoWriter;
import processing.core.PImage;

public class PVideoWriter {
    public VideoWriter writer;

    PVideoWriter(String outputFilename, double fps, int width, int height) {
        // Create a VideoWriter ob
         writer = new VideoWriter(outputFilename, VideoWriter.fourcc('m', 'p', '4', 'v'), fps, new Size(width,height));
        if (!writer.isOpened()) {
            System.out.println("Error: Unable to open VideoWriter.");
            return;
        }

    }

    public void writeFrame(PImage img) {
        System.out.println("writing");
        // Convert the PImage to a Mat object
        Mat mat = new Mat(img.height, img.width, CvType.CV_8UC3);
        byte[] pixels = new byte[img.width * img.height *3];
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            pixels[i*3+2] = (byte) (img.pixels[i] >> 16 & 0xFF);
            pixels[i*3+1] = (byte) (img.pixels[i] >> 8 & 0xFF);
            pixels[i*3] = (byte) (img.pixels[i] & 0xFF);
        }
        mat.put(0, 0, pixels);
        writer.write(mat);
    }

    public void close() {
        writer.release();
    }
}
