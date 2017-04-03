package org.object;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Sprite {

	public float posX = 0;
	public float posY = 0;

	public BufferedImage image = null;
	
	public float height = 16;
	public float width = 16;
	public boolean isSolid = false;

	public Sprite(float posX, float posY) {

		this.posX = posX;
		this.posY = posY;

	}

	public void update(float deltaTime) {

	}

	public void render(Graphics g) {

		if (image == null) {
			return;

		}

		int realX = (int) (posX - image.getWidth() / 2);
		int realY = (int) (posY - image.getHeight() / 2);

		g.drawImage(image, realX, realY, image.getWidth(), image.getHeight(), null);

	}

}
