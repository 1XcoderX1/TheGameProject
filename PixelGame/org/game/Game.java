package org.game;

import org.graphics.Renderer;
import org.test.TestSprite;
import org.world.World;

public class Game {

	public static void main(String[] args) {

		Renderer.init();
		
		World.currentWorld = new World();
		World.currentWorld.sprites.add(new TestSprite(100, 100));

	}

	public static void quit() {

		System.exit(0);

	}

}
