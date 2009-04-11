package net.java.dev.aircarrier.hud;

import java.util.ArrayList;
import java.util.List;

import net.java.dev.aircarrier.util.TextureLoader;

import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Text;
import com.jme.scene.shape.Box;
import com.jme.scene.state.AlphaState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;

/**
 * Node for display in a hud, having a dialog area, and lines of text
 * @author goki
 */
public class MessageDialog extends Node {
	private static final long serialVersionUID = 65734143456905297L;

	private static final String FONT_LOCATION = "/com/jme/app/defaultfont.tga";
	
	DialogBox box;
	List<Text> texts;
	
	int columns;
	int rows;
	
	int tabWidth;
	
	float width;
	float height;

	float fontWidth;
	float fontHeight;
	float interline;
	
	String fontLocation;
	
	DialogBorders borders;

    private AlphaState as;
    private TextureState font;

    private StringBuffer lineBuffer = new StringBuffer();
    private StringBuffer wordBuffer = new StringBuffer();
    
    private static char newline = "\n".charAt(0);
    private static char tab = "\t".charAt(0);
    private static char space = " ".charAt(0);
    private static char hyphen = "-".charAt(0);
    
	public MessageDialog(int columns, int rows) {
		this(createDialogBackdrop(), columns, rows, FONT_LOCATION, new SimpleDialogBorders(12), 0, 4);
	}

	public MessageDialog(DialogBox box, int columns, int rows) {
		this(box, columns, rows, FONT_LOCATION, new SimpleDialogBorders(12), 0, 4);
	}

	public MessageDialog(MessageDialog toCopy) {
		this(
				new DialogBox(toCopy.getBox()), 
				toCopy.getColumns(), toCopy.getRows(), 
				toCopy.getFontLocation(),
				new SimpleDialogBorders(toCopy.getBorders()),
				toCopy.getInterline(),
				toCopy.getTabWidth());
	}
	
	public MessageDialog(DialogBox box, int columns, int rows, String fontLocation, DialogBorders borders, float interline, int tabWidth) {
		this.columns = columns;
		this.rows = rows;
		this.box = box;
		this.borders = borders;
		this.interline = interline;
		this.tabWidth = tabWidth;
		this.fontLocation = fontLocation;
		
		//attach the box
		attachChild(box);
		
		//create lists
		texts = new ArrayList<Text>();
		
		//Create render states
		createRenderStates(fontLocation);
		
		System.out.println("AS: " + as);
		System.out.println("Font: " + font);
		
		//Create blank line and store font size
		Text t = createText("Text0", " ", borders.getLeft(), borders.getTop());
		fontWidth = t.getWidth();
		fontHeight = t.getHeight();
		
		//Add the first line
		t.print("");
		texts.add(t);

		Box b = new Box("box0", new Vector3f(), 2, 2, 2);
		attachChild(b);
		b.getLocalTranslation().set(borders.getLeft(), borders.getTop(), 0);

		
		//Add the rest of the lines
		for (int i = 1; i < rows; i++) {
			texts.add(0, createText("Text" + i, "", borders.getLeft(), borders.getTop() + (fontHeight + interline) * i));
			
			b = new Box("box" + i, new Vector3f(), 2, 2, 2);
			attachChild(b);
			b.getLocalTranslation().set(borders.getLeft(), borders.getTop() + (fontHeight + interline) * i, 0);
		}
		
		//Size the box to fit the text
		width = (columns + 1) * fontWidth + borders.getLeft() + borders.getRight();
		height = rows * fontHeight + (rows - 1) * interline + borders.getTop() + borders.getBottom();
		box.setDimension(width, height);
		
		updateRenderState();
		
	}

	public int getColumns() {
		return columns;
	}

	public int getRows() {
		return rows;
	}

	public DialogBox getBox() {
		return box;
	}

	public int getTabWidth() {
		return tabWidth;
	}

	public void setTabWidth(int tabWidth) {
		this.tabWidth = tabWidth;
	}

	public void printLine(int line, String s) {
		if (line < rows) texts.get(line).print(s);
	}

	public void printLine(int line, StringBuffer s) {
		if (line < rows) texts.get(line).print(s);
	}
	
	public DialogBorders getBorders() {
		return borders;
	}

	public String getFontLocation() {
		return fontLocation;
	}

	public float getInterline() {
		return interline;
	}

	public void printMessage(String s) {
		int line = 0;
		int i = 0;
		
		lineBuffer.setLength(0);
		wordBuffer.setLength(0);
		while ( i <= s.length() ) {
			
			char ch;
			
			//Pretend there is a newline at the end, this makes sure we write out the last line
			if (i < s.length()) {
				ch = s.charAt(i);
			} else {
				ch = newline;
			}
			
			//deal with newlines, spaces, tabs and hyphens roughly the same
			if (ch == space || ch == hyphen || ch == newline || ch == tab) {
				int len = wordBuffer.length();
				int lineLen = lineBuffer.length();
				
				//hyphen should be printed with the word, len is adjusted
				if (ch == hyphen) len++;

				//If there is no room for the word (or word + hypen) on the line, go to a new one
				//(note that we assume that a single word isn't longer than a line, since we will
				//print it on the next line regardless)
				if (lineLen + len > columns) {
					texts.get(line).print(lineBuffer);
					lineBuffer.setLength(0);
					line++;
				}

				//Write the word to the line
				lineBuffer.append(wordBuffer);
				wordBuffer.setLength(0);
				
				//If we have a newline, go to next line
				if (ch == newline) {
					texts.get(line).print(lineBuffer);
					lineBuffer.setLength(0);
					line++;
				
				//If we have a tab, then add required spaces
				} else if (ch == tab) {
					lineBuffer.append(" ");
					while ((lineBuffer.length() % tabWidth) != 0) lineBuffer.append(" ");
				//hyphens and spaces are just appended to the line after the word
				} else {
					lineBuffer.append(ch);					
				}

			//Normal character, just add to word
			} else {
				wordBuffer.append(ch);
			}
			
			//Move to nect source character
			i++;
		}
		
	}
	
    private Text createText(String name, String value, float xPosition, float yPosition) {
        Text text = Text.createDefaultTextLabel(name, value);//new Text(name, value);
        /*text.setTextureCombineMode(TextureState.REPLACE);
        text.setRenderState(as);
        text.setRenderState(font);
        */
        //text.setRenderQueueMode(Renderer.QUEUE_INHERIT);
        //text.updateGeometricState(0.0f, true);
        text.setLocalTranslation(xPosition, yPosition, 0.0f);
        attachChild(text);
        return text;
    }
    
    private void createRenderStates(String fontLocation) {
        as = DisplaySystem.getDisplaySystem().getRenderer().createAlphaState();
        as.setBlendEnabled(true);
        as.setSrcFunction(AlphaState.SB_SRC_ALPHA);
        as.setDstFunction(AlphaState.DB_ONE);
        as.setTestEnabled(true);
        as.setTestFunction(AlphaState.TF_GREATER);
        as.setEnabled(true);
        font = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        font.setTexture(TextureLoader.loadUncompressedTexture(fontLocation));
        font.setEnabled(true);		    	
    }
	
    public static DialogBox createDialogBackdrop() {
        //Make default texture
        Texture dialogTexture = TextureLoader.loadUncompressedTexture("resources/dialogArea.png");
        
        //Make the dialog box
        DialogBox box = new DialogBox("MessageDialogBox", dialogTexture);

        //Create alpha state to blend using alpha
        AlphaState bas = DisplaySystem.getDisplaySystem().getRenderer().createAlphaState();
        bas.setBlendEnabled(true);
        bas.setSrcFunction(AlphaState.SB_SRC_ALPHA);
        bas.setDstFunction(AlphaState.DB_ONE_MINUS_SRC_ALPHA);
        bas.setTestEnabled(false);
        bas.setEnabled(true);
        
        //set box render state
        box.setRenderState(bas);
        box.updateRenderState();
        
        //return the box
        return box;
    }

	public float getHeight() {
		return height;
	}

	public float getWidth() {
		return width;
	}
    
    
}
