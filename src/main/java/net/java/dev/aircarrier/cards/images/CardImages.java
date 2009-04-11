package net.java.dev.aircarrier.cards.images;

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.ImageIcon;

public class CardImages {

	String rootURL;
	Map<Integer, Image> images = new HashMap<Integer, Image>(52);
	Image aoShadow;
	Image sharpShadow;
	Image sharpShadowSegmentUp;
	Image sharpShadowSegmentDown;
	Image sharpShadowTop;
	Image sharpShadowBottom;
	Image glowSegmentUp;
	Image glowSegmentDown;
	Image glowTop;
	Image glowBottom;
	Image bg;
	Image back;
	int sharpShadowOffsetX;
	int sharpShadowOffsetY;
	int aoShadowOffsetX;
	int aoShadowOffsetY;
	int cardTitleHeight;
	int cardBackHeight;
	int cardStackWidth;
	int cardStackHeight;
	int cardWidth;
	int cardHeight;
	int originX;
	int originY;
	
	public static CardImages makeIphone() throws IOException {
		return new CardImages("resources/cards/iphone/");
	}

	public static CardImages makeIphoneNonSSS() throws IOException {
		return new CardImages("resources/cards/iphoneNonSSS/");
	}
	
	public CardImages(String rootURL) throws IOException {
		this.rootURL = rootURL;
		Properties props = new Properties();
		props.load(CardImages.class.getClassLoader().getResourceAsStream(rootURL + "cards.props"));
		
		try {
			originX = getIntProp(props, "originX");
			originY = getIntProp(props, "originY");
			sharpShadowOffsetX = getIntProp(props, "sharpShadowOffsetX");
			sharpShadowOffsetY = getIntProp(props, "sharpShadowOffsetY");
			aoShadowOffsetX = getIntProp(props, "aoShadowOffsetX");
			aoShadowOffsetY = getIntProp(props, "aoShadowOffsetY");
			cardTitleHeight = getIntProp(props, "cardTitleHeight");
			cardBackHeight =  getIntProp(props, "cardBackHeight");
			cardStackWidth = getIntProp(props, "cardStackWidth");
			cardStackHeight = getIntProp(props, "cardStackHeight");
			cardWidth = getIntProp(props, "cardWidth");
			cardHeight = getIntProp(props, "cardHeight");
		} catch (ClassCastException cce) {
			throw new IOException("Could not load required properties from cards.props");
		}
		
		this.aoShadow = loadImage("aoShadow.png");
		this.sharpShadow = loadImage("sharpShadow.png");
		this.sharpShadowSegmentUp = loadImage("sharpShadowSegmentUp.png");
		this.sharpShadowSegmentDown = loadImage("sharpShadowSegmentDown.png");
		this.sharpShadowTop = loadImage("sharpShadowTop.png");
		this.sharpShadowBottom = loadImage("sharpShadowBottom.png");
		this.glowSegmentUp = loadImage("glowSegmentUp.png");
		this.glowSegmentDown = loadImage("glowSegmentDown.png");
		this.glowTop = loadImage("glowTop.png");
		this.glowBottom = loadImage("glowBottom.png");
		this.bg = loadImage("bg.png");
		this.back = loadImage("back.png");
	}

	int getIntProp(Properties props, String key) {
		return Integer.parseInt(props.get(key).toString());
	}
	
	public int getSharpShadowOffsetX() {
		return sharpShadowOffsetX;
	}
	public int getSharpShadowOffsetY() {
		return sharpShadowOffsetY;
	}

	public int getAOOffsetX() {
		return aoShadowOffsetX;
	}
	public int getAOOffsetY() {
		return aoShadowOffsetY;
	}
	
	public synchronized Image getImage(int index) {
		Image i = images.get(index);
		if (i == null) {
			i = loadImage("card" + pack(index+1, 2) + ".png");
			images.put(index, i);
		}
		return i;
	}
	
	private Image loadImage(String name) {
		return new ImageIcon(CardImages.class.getClassLoader().getResource(rootURL + name)).getImage();
	}
	
	private String pack(int index, int length) {
		String s = Integer.toString(index);
		while (s.length() < length) {
			s = "0" + s;
		}
		return s;
	}

	public Image getAoShadow() {
		return aoShadow;
	}

	public Image getSharpShadow() {
		return sharpShadow;
	}

	public Image getSharpShadowSegmentUp() {
		return sharpShadowSegmentUp;
	}

	public Image getSharpShadowSegmentDown() {
		return sharpShadowSegmentDown;
	}

	public Image getSharpShadowTop() {
		return sharpShadowTop;
	}

	public Image getSharpShadowBottom() {
		return sharpShadowBottom;
	}

	public Image getBg() {
		return bg;
	}

	public int getCardTitleHeight() {
		return cardTitleHeight;
	}

	public int getCardBackHeight() {
		return cardBackHeight;
	}

	public int getCardStackWidth() {
		return cardStackWidth;
	}

	public int getCardStackHeight() {
		return cardStackHeight;
	}

	public int getCardWidth() {
		return cardWidth;
	}

	public int getCardHeight() {
		return cardHeight;
	}

	public int getOriginX() {
		return originX;
	}

	public int getOriginY() {
		return originY;
	}

	public Image getGlowSegmentUp() {
		return glowSegmentUp;
	}

	public Image getGlowSegmentDown() {
		return glowSegmentDown;
	}

	public Image getGlowTop() {
		return glowTop;
	}

	public Image getGlowBottom() {
		return glowBottom;
	}

	public Image getBack() {
		return back;
	}
	
}
