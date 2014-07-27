package org.pfaa.chemica.utils;

import java.awt.Color;

public class ColorUtils {
	public static Color blendColors(Color from, Color to, double toFraction) {
		toFraction = Math.min(1.0, toFraction);
		double fromFraction = 1 - toFraction;
		return new Color((int)(from.getRed() * fromFraction + to.getRed() * toFraction),
				         (int)(from.getGreen() * fromFraction + to.getGreen() * toFraction),
				         (int)(from.getBlue() * fromFraction + to.getBlue() * toFraction));
	}
}
