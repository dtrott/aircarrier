package net.java.dev.aircarrier.cards.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.java.dev.aircarrier.cards.Card;
import net.java.dev.aircarrier.cards.Deck;
import net.java.dev.aircarrier.cards.field.Field;
import net.java.dev.aircarrier.cards.field.StackPlacement;
import net.java.dev.aircarrier.cards.images.CardImages;
import net.java.dev.aircarrier.cards.stack.CardPlacement;
import net.java.dev.aircarrier.cards.stack.Stack;
import net.java.dev.aircarrier.cards.stack.StackIndex;
import net.java.dev.aircarrier.cards.stack.CardPlacement.Facing;
import net.java.dev.aircarrier.cards.util.ReverseComparator;


public class FieldPanel extends JPanel {

	CardImages images;
	Field field;
	SortedSet<StackPlacement> sortedField = new TreeSet<StackPlacement>();
	SortedSet<StackPlacement> reverseSortedField = new TreeSet<StackPlacement>(new ReverseComparator<StackPlacement>());
	
	public FieldPanel(Field field) {
		super();
		this.field = field;
		setPreferredSize(new Dimension(480,320));
		setMinimumSize(getPreferredSize());
		setPreferredSize(new Dimension(480,320));
		try {
			images = CardImages.makeIphoneNonSSS();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				int x = e.getX();
				int y = e.getY();
				
				System.out.println(FieldPanel.this.pick(x, y));
			}
		});
		
	}

	
	public StackIndex pick(int x, int y) {
		//Search stacks, front first
		reverseSortedField.clear();
		reverseSortedField.addAll(field.getStackPlacements());
		
		for (StackPlacement sp : reverseSortedField) {
			StackIndex p = pick(sp, x, y);
			if (p != null) return p;
		}
		
		
		return null;
	}
	
	public StackIndex pick(StackPlacement sp, int x, int y) {
		
		//Check along x
		if (x < sp.getX()) return null;
		if (x > sp.getX() + images.getCardStackWidth()) return null;
		
		//Check along y
		if (y < sp.getY()) return null;

		
		//Iterate through all but last card (we are doing the
		//cards that are overlapped by others, which are only selectable
		//in the exposed area. This code is the same as for drawing
		//the cards, since it uses the amount we move down between
		//each drawn card)
		Stack stack = sp.getStack();
		int yOffset = sp.getY();
		
		//If stack is spread, check everything but the top card first
		if (stack.isSpread()) {
			for (int i = 0; i < stack.getContents().size()-1;i++) {
				CardPlacement cp = stack.getContents().get(i);
				int yMove = 0;
				if (cp.getFacing()==Facing.UP) {
					yMove = images.getCardTitleHeight();
				} else {
					yMove = images.getCardBackHeight();
				}
				yOffset+=yMove;
				
				//Check if we are less than current y - in this case we are selected
				if (y < yOffset) {
					return new StackIndex(stack, i);
				}
			}
		}
		
		//For the last card, jump full height
		yOffset += images.getCardHeight();
		if (y < yOffset) {
			return new StackIndex(stack, stack.getContents().size()-1);
		}
		
		return null;
	}
	
	void paintField(Graphics2D g, Field field) {
		sortedField.clear();
		sortedField.addAll(field.getStackPlacements());
		for (StackPlacement sp : sortedField) {
			paintStackPlacement(g, sp);			
		}
	}

	void paintStackPlacement(Graphics2D g, StackPlacement sp) {
		paintStack(g, sp.getStack(), sp.getX(), sp.getY());
	}
	
	@Override
	protected void paintComponent(Graphics graphics) {
		// TODO Auto-generated method stub
		super.paintComponent(graphics);
		
		Graphics2D g = (Graphics2D)graphics;

		g.drawImage(images.getBg(), 0, 0, null);

		paintField(g, field);
		
	}

	void paintStack(Graphics2D g, Stack stack, int x, int y) {
		if (stack.getContents().isEmpty()) return;
		boolean first = true;
		List<CardPlacement> toDraw;
		if (stack.isSpread()) {
			toDraw = stack.getContents();
		} else {
			toDraw = stack.getContents().subList(stack.getContents().size()-1, stack.getContents().size());
		}
		paintShadow(g, x, y, toDraw);
		for (CardPlacement cp : toDraw) {
			Card c = cp.getCard();
			Image image = null;
			int yMove = 0;
			boolean ao;
			if (cp.getFacing()==Facing.UP) {
				image = images.getImage(c.getIndex());
				yMove = images.getCardTitleHeight();
				ao = true;
			} else {
				image = images.getBack();
				yMove = images.getCardBackHeight();
				ao = first;
			}
			paintCard(g, image, x, y, ao);
			y+=yMove;
			first = false;
		}
	}
	
	void paintCard(Graphics2D g, Image card, int x, int y, boolean ao) {
		if (ao) g.drawImage(images.getAoShadow(), x + images.getAOOffsetX(), y + images.getAOOffsetY(), null);
		g.drawImage(card, x, y, null);		
	}

	void paintShadow(Graphics2D g, int x, int y) {
		//g.drawImage(images.getSharpShadow(), x + images.getSharpShadowOffsetX(), y + images.getSharpShadowOffsetY(), null);
		paintShadow(g, x, y, 1, 0);
	}

	void paintShadow(Graphics2D g, int x, int y, List<CardPlacement> cards) {
		int upCount = 0;
		int downCount = 0;
		for (CardPlacement cp : cards) {
			if (cp.getFacing()==Facing.UP) {
				upCount++;
			} else {
				downCount++;
			}
		}

		//The last card (topmost) doesn't count, since it is included in the
		//top and bottom shadow segments
		if (cards.get(cards.size()-1).getFacing()==Facing.UP) {
			upCount--;
		} else {
			downCount--;
		}
		
		paintShadow(g, x, y, upCount, downCount);
	}

	void paintShadow(Graphics2D g, int x, int y, int segmentsUp, int segmentsDown) {
		paintStackStuff(g, x + images.getSharpShadowOffsetX(), y + images.getSharpShadowOffsetY(), segmentsUp, segmentsDown, images.getSharpShadowTop(), images.getSharpShadowSegmentUp(), images.getSharpShadowSegmentDown(), images.getSharpShadowBottom());
	}

	void paintGlow(Graphics2D g, int x, int y, int segmentsUp, int segmentsDown) {
		paintStackStuff(g, x, y, segmentsUp, segmentsDown, images.getGlowTop(), images.getGlowSegmentUp(), images.getGlowSegmentDown(), images.getGlowBottom());
	}

	void paintStackStuff(Graphics2D g, int x, int y, int segmentsUp, int segmentsDown, Image top, Image segmentUp, Image segmentDown, Image bottom) {
		g.drawImage(top, x, y, null);
		y+=images.getCardTitleHeight();

		for (int i = 0; i < segmentsUp; i++) {
			g.drawImage(segmentUp, x, y, null);
			y+=images.getCardTitleHeight();
		}

		for (int i = 0; i < segmentsDown; i++) {
			g.drawImage(segmentDown, x, y, null);
			y+=images.getCardBackHeight();
		}
		g.drawImage(bottom, x, y, null);
	}

	private static Stack makeStack(Deck deck, String name) {
		List<Card> deckList = new ArrayList<Card>(deck.getCards());
		List<CardPlacement> cards = new ArrayList<CardPlacement>();
		cards.add(new CardPlacement(deckList.get(0), Facing.DOWN));
		cards.add(new CardPlacement(deckList.get(0), Facing.DOWN));
		cards.add(new CardPlacement(deckList.get(12), Facing.UP));
		cards.add(new CardPlacement(deckList.get(0), Facing.DOWN));
		cards.add(new CardPlacement(deckList.get(0), Facing.DOWN));
		cards.add(new CardPlacement(deckList.get(12), Facing.UP));
		cards.add(new CardPlacement(deckList.get(1), Facing.DOWN));
		cards.add(new CardPlacement(deckList.get(2), Facing.DOWN));
		cards.add(new CardPlacement(deckList.get(3), Facing.DOWN));
		cards.add(new CardPlacement(deckList.get(4), Facing.UP));
		cards.add(new CardPlacement(deckList.get(27), Facing.UP));
		Stack stack = new Stack(name, cards);
		stack.setSpread(true);
		return stack;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				
				Deck deck = Deck.getInstance();

				Field field = new Field();
				StackPlacement sp = new StackPlacement(makeStack(deck, "TestStack1"));
				field.addStackPlacement(sp);
				sp.setX(20);
				sp.setY(20);
				sp.setZ(-1);

				sp = new StackPlacement(makeStack(deck, "TestStack2"));
				field.addStackPlacement(sp);
				sp.setX(30);
				sp.setY(30);

				sp = new StackPlacement(makeStack(deck, "TestStack3"));
				field.addStackPlacement(sp);
				sp.setX(45);
				sp.setY(30);

				FieldPanel panel = new FieldPanel(field);
				
				panel.pick(0, 0);
				
				JFrame frame = new JFrame("CardPanel Test App");
				frame.add(panel);
				frame.pack();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
		
	}

}
