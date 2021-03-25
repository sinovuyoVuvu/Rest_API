package utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Window;
import java.time.LocalDateTime;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

// extend base class properties -- inherit base class

public class WindowUtils {

	



	public static void outlineElement(WebElement oCurrentElement, WebDriver Driver) throws InterruptedException {

		// https://stackoverflow.com/questions/21604762/drawing-over-screen-in-java
		// Modified by Craig Adam to draw a highlight around an object

		// AWT Abstract Window Toolkit
//		import java.awt.Color;
//		import java.awt.Graphics;
//		import java.awt.Window;

		Driver.manage().window().maximize();
		int iWaitTimeMs = 250;
		long iDuration = Integer.parseInt(SettingsUtils.getTagNameValue("outlineDuration"));
		int iPadding = 5;
		int iWindowY = Driver.manage().window().getPosition().getY();
		int iBarHeight = 35;
		int iMenuHeight = 70 + 20;

//		System.out.println("maximize");  // OK

//		System.out.println("getX = " + oCurrentElement.getLocation().getX());
//		System.out.println("getY = " + oCurrentElement.getLocation().getY());
//		System.out.println("getWidth = " + oCurrentElement.getSize().getWidth());
//		System.out.println("getHeight = " + oCurrentElement.getSize().getHeight());		
//		System.out.println("offsetTop = " + oCurrentElement.getAttribute("offsetTop"));		
//		System.out.println("Window  getX = " + Driver.manage().window().getPosition().getX());
//		System.out.println("Window  getY = " + Driver.manage().window().getPosition().getY());
//		System.out.println("Window  getWidth = " + Driver.manage().window().getSize().getWidth());
//		System.out.println("Window  getHeight = " + Driver.manage().window().getSize().getHeight());

		int iX = oCurrentElement.getLocation().getX() - iPadding;
		int iY = oCurrentElement.getLocation().getY() + iWindowY + iMenuHeight + iBarHeight - iPadding;
		int iW = oCurrentElement.getSize().getWidth() + 2 * iPadding;
		int iH = oCurrentElement.getSize().getHeight() + 2 * iPadding;
		
		
//		// partial size does not get correct location		
//		System.out.println("getX = " + oCurrentElement.getLocation().getX());
//		System.out.println("getY = " + oCurrentElement.getLocation().getY());
//		System.out.println("getWidth = " + oCurrentElement.getSize().getWidth());
//		System.out.println("getHeight = " + oCurrentElement.getSize().getHeight());		
//		System.out.println("offsetTop = " + oCurrentElement.getAttribute("offsetTop"));
//		System.out.println("");
//		System.out.println("Window  getX = " + Driver.manage().window().getPosition().getX());
//		System.out.println("Window  getY = " + Driver.manage().window().getPosition().getY());
//		System.out.println("Window  getY = " + Driver.manage().window().getSize().getWidth());
//		System.out.println("Window  getHeight = " + Driver.manage().window().getSize().getHeight());
//		System.out.println("");

//		System.out.println("Full screen");
//	Driver.manage().window().fullscreen(); // OK
//		int iBarHeight = 35;
//		System.out.println("getX = " + oCurrentElement.getLocation().getX());
//		System.out.println("getY = " + oCurrentElement.getLocation().getY());
//		System.out.println("getWidth = " + oCurrentElement.getSize().getWidth());
//		System.out.println("getHeight = " + oCurrentElement.getSize().getHeight());		
//		System.out.println("offsetTop = " + oCurrentElement.getAttribute("offsetTop"));		
//		System.out.println("Window  getX = " + Driver.manage().window().getPosition().getX());
//		System.out.println("Window  getY = " + Driver.manage().window().getPosition().getY());
//		System.out.println("Window  getWidth = " + Driver.manage().window().getSize().getWidth());
//		System.out.println("Window  getHeight = " + Driver.manage().window().getSize().getHeight());



//		Actions act= new Actions(Driver);
//		act.moveByOffset(iX+10, iY).click().build().perform();  // moves out of bounds
//	act.moveToElement(oCurrentElement).click().build().perform();
//	act.moveToElement(oCurrentElement, -1000, -2500).build().perform();

//		act.moveByOffset(iX, iY).click().build().perform();

		
		drawRectangle(iX, iY, iW, iH, iDuration, iWaitTimeMs);
		
		
	}

	
	
		
	
	public static void drawRectangle(int iX, int iY, int iW, int iH, long iDuration, int iWaitTimeMs)
			throws InterruptedException {

		if (iDuration > 0) {

			Window w = new Window(null) {

				// window instance
				private static final long serialVersionUID = 1L;

				@Override
				public void paint(Graphics g) {

					g.setColor(Color.BLUE);

					// Example draw string
//			    final Font font = getFont().deriveFont(48f);		    
//			    g.setFont(font);
//			    final String message = "Hello";
//			    FontMetrics metrics = g.getFontMetrics();
//			    g.drawString(message,(getWidth()-metrics.stringWidth(message))/2,(getHeight()-metrics.getHeight())/2);

					// x , y, w , h
					g.drawRect(iX, iY, iW, iH);

					// g.clearRect(arg0, arg1, arg2, arg3);

				}

				@Override
				public void update(Graphics g) {
					paint(g);
				}

			};

			w.setAlwaysOnTop(true);
			w.setBounds(w.getGraphicsConfiguration().getBounds());
			w.setBackground(new Color(0, true));

			LocalDateTime tTimeNow = LocalDateTime.now().minusHours(1);
			LocalDateTime tTimeEnd = LocalDateTime.now().plusSeconds(iDuration);

			// switch visibility on and off
			do {

				Thread.sleep(iWaitTimeMs);
				w.setVisible(true);
				Thread.sleep(iWaitTimeMs);
				w.setVisible(false);

				tTimeNow = LocalDateTime.now();

			} while (tTimeNow.isBefore(tTimeEnd));

			w.removeAll();
			w.dispose();

		}
	}
	
	

}