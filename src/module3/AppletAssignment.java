package module3;
import processing.core.*;

public class AppletAssignment extends PApplet {

	private String URL = "http://freetobescott.com/courses/ucsdjava/palmTrees.jpg";
	//private String URL = "http://photographyartimages.com/Seascape-Ocean-Photography/seascape-ocean%20images/Palm-Trees-Line-Beach-On-Rangiroa.jpg";
	private PImage backgroundImg;
	// my addition for time
	private short hour = 7;
	private short min = 0;
	
	public void setup() {
		size(200,200);
		backgroundImg = loadImage(URL,"jpg");
	}
	
	public void draw() {
		backgroundImg.resize(0, height);
		image(backgroundImg,0,0);
		//Thread.sleep(50);
	/*	if (++min == 60) {
			min=0;
			++hour;
		}
		if (hour == 10) {
			hour = 7;
		}
		if (hour == 9) {
			fill(95,95,95);
		}
		else {
			fill(255,209,0);
		} */
		int[] color = sunColorSec(second());
		fill(color[0],color[1],color[2]);
		ellipse(width/4,height/5,height/5,height/5);
	}
	
	public int[] sunColorSec(float seconds) {
		int[] rgb = new int[3];
		float diffFrom30 = Math.abs(30-seconds);
		float ratio = diffFrom30/30;
		rgb[0] = (int) (255*ratio);
		rgb[1] = (int) (255*ratio);
		rgb[2] = 0;
		
		return rgb;
	}	
	
}
