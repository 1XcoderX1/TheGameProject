package org.object;

import java.awt.Color;
import java.awt.Graphics;

public class Player extends Mob {

	public Player(float posX, float posY) {
		super(posX, posY);

	}

	public void update(float deltaTime) {

	}

	public void render(Graphics g) {

		g.setColor(Color.RED );
		g.draw3DRect((int) (posX - width/2), (int) (posY - height / 2), (int) width, (int) height, false);

	}

}
