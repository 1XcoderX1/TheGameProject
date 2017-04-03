package org.graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.game.Game;
import org.input.Input;
import org.world.World;

public class Renderer {

	private static Frame frame;
	private static Canvas canvas;

	private static int canvasWidth = 0;
	private static int canvasHeight = 00;

	private static final int GAME_WIDTH = 400;
	private static final int GAME_HEIGHT = 250;

	private static int gameWidth = 0;
	private static int gameHeight = 0;

	private static long lastFPScheck = 0;
	private static int currentFPS = 0;
	private static int totalFrames = 0;

	private static void getBestSize() {

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();

		boolean done = false;

		while (!done) {

			canvasWidth += GAME_WIDTH;
			canvasHeight += GAME_HEIGHT;

			if (canvasWidth > screenSize.width || canvasHeight > screenSize.height) {

				canvasWidth -= GAME_WIDTH;
				canvasHeight -= GAME_HEIGHT;
				done = true;

			}

		}

		int xDiff = screenSize.width - canvasWidth;

		int yDiff = screenSize.height - canvasHeight;
		int factor = canvasWidth / GAME_WIDTH;

		gameWidth = canvasWidth / factor + xDiff / factor;
		gameHeight = canvasHeight / factor + yDiff / factor;

		canvasHeight = gameHeight * factor;
		canvasWidth = gameWidth * factor;

	}

	public static void makeFullscreen() {

		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gDev = env.getDefaultScreenDevice();

		if (gDev.isFullScreenSupported()) {

			frame.setUndecorated(true);
			gDev.setFullScreenWindow(frame);

		}

	}

	public static void init() {

		getBestSize();

		frame = new Frame();
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(canvasWidth, canvasHeight));
		frame.add(canvas);
		makeFullscreen();

		frame.pack();
		frame.setLocationRelativeTo(canvas);
		frame.setResizable(true);

		frame.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {

				Game.quit();

			}

		});

		frame.setVisible(true);

		canvas.addKeyListener(new Input());
		canvas.requestFocus();

		startRendering();

	}

	public static void startRendering() {

		Thread renderThread = new Thread() {

			public void run() {

				GraphicsConfiguration gConfig = canvas.getGraphicsConfiguration();
				VolatileImage vImg = gConfig.createCompatibleVolatileImage(gameWidth, gameHeight);

				while (true) {

					// FPS Counter
					totalFrames++;
					if (System.nanoTime() > lastFPScheck + 1000000000) {

						lastFPScheck = System.nanoTime();
						currentFPS = totalFrames;
						totalFrames = 0;

					}

					if (vImg.validate(gConfig) == VolatileImage.IMAGE_INCOMPATIBLE) {

						vImg = gConfig.createCompatibleVolatileImage(gameWidth, gameHeight);

					}

					Graphics imgGraphs = vImg.getGraphics();

					imgGraphs.setColor(Color.BLACK);
					imgGraphs.fillRect(0, 0, gameWidth, gameHeight);

					// TODO: Put some render stuff in

					World.update();
					World.render(imgGraphs);

					// Display FPS Counter
					imgGraphs.setColor(Color.DARK_GRAY);
					imgGraphs.drawString(String.valueOf(currentFPS), 4, gameHeight - 5);

					imgGraphs.dispose();

					imgGraphs = canvas.getGraphics();
					imgGraphs.drawImage(vImg, 0, 0, canvasWidth, canvasHeight, null);

					imgGraphs.dispose();

				}

			}

		};

		renderThread.setName("Render Thread");
		renderThread.start();

	}

	public static BufferedImage loadImage(String path) throws IOException {

		BufferedImage rawImage = ImageIO.read(Renderer.class.getResource(path));
		BufferedImage finalImage = canvas.getGraphicsConfiguration().createCompatibleImage(rawImage.getWidth(),
				rawImage.getHeight(), rawImage.getTransparency());

		finalImage.getGraphics().drawImage(rawImage, 0, 0, rawImage.getWidth(), rawImage.getHeight(), null);

		return finalImage;

	}

}
