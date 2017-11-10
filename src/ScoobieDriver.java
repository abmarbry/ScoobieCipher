import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ScoobieDriver {

	public static void main(String[] args) {
		RSA rsa = new RSA();
		try {
		createScoobieCipherGUI(rsa);
		}
		catch(IOException e) { //If path not found for image
			e.printStackTrace();
		}
	}
	
	public static void createScoobieCipherGUI(RSA rsa) throws IOException{
		
		final int FRAME_WIDTH = 500;
		final int FRAME_HEIGHT= 500;
		
		JFrame frame = new JFrame("ScoobieCipher");
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container container = frame.getContentPane();
		frame.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		
		JPanel enterPanel = new JPanel();
		setBigPanelAesthetics(enterPanel);
		
		final int GRID_ROWS = 1;
		final int GRID_COLS = 6;
		final int DIMENSION_SIZE = 90;
		
		JPanel enterButtonPanel = new JPanel();
		enterButtonPanel.setLayout(new GridLayout(GRID_ROWS, GRID_COLS));
		enterPanel.setPreferredSize(new Dimension(DIMENSION_SIZE, DIMENSION_SIZE));
		enterButtonPanel.setBackground(Color.BLACK);
		
		JPanel outputPanel = new JPanel();
		setBigPanelAesthetics(outputPanel);
		
		final int FONT_SIZE = 20;
		
		JLabel messagePrompt = new JLabel("ScoobieCipher");
		messagePrompt.setFont(new Font("Lucida Sans Unicode", Font.BOLD, FONT_SIZE));
		messagePrompt.setForeground(Color.WHITE);
		
		messagePrompt.setHorizontalAlignment(SwingConstants.CENTER);
		enterPanel.add(messagePrompt, BorderLayout.NORTH);
		
		final int TEXT_AREA_ROWS = 20;
		final int TEXT_AREA_COLS = 10;
		
		final int DIMENSION_WIDTH = 20;
		final int DIMENSION_HEIGHT = 30;
		
		final String SCOOBIE_BOY_PATH = "src\\images\\scoobieBoy.jpg";
		
		BufferedImage img = ImageIO.read(new File(SCOOBIE_BOY_PATH));
		JLabel scoobieBoy = new JLabel(new ImageIcon(img));
		outputPanel.add(scoobieBoy, BorderLayout.WEST);
		
		JTextArea inputMessage = new JTextArea(TEXT_AREA_ROWS, TEXT_AREA_COLS);
		inputMessage.setPreferredSize(new Dimension(DIMENSION_WIDTH, DIMENSION_HEIGHT));
		setTextAreaAesthetics(inputMessage, true);
		enterPanel.add(inputMessage, BorderLayout.CENTER);
		
		
		JTextArea outputMessage = new JTextArea(TEXT_AREA_ROWS, TEXT_AREA_COLS);
		outputMessage.setPreferredSize(new Dimension(DIMENSION_WIDTH, DIMENSION_HEIGHT));
		setTextAreaAesthetics(outputMessage, false);
		outputPanel.add(outputMessage, BorderLayout.CENTER);
		//For this ^
		JScrollPane scroll = new JScrollPane(outputMessage);
		scroll.setBackground(Color.WHITE);
		outputPanel.add(scroll);
		
		
		JButton scoobieButton = new JButton("Scoobify");
		scoobieButton.addActionListener(addEncodeListener(inputMessage, outputMessage, rsa));
		setButtonAesthetics(scoobieButton);
		enterButtonPanel.add(scoobieButton);
		
		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				outputMessage.setText("");
				inputMessage.setText("");
		}
		});
		setButtonAesthetics(clearButton);
		enterButtonPanel.add(clearButton);
		
		enterPanel.add(enterButtonPanel, BorderLayout.SOUTH);
	
		
		JButton decodeButton = new JButton("Decode");
		decodeButton.addActionListener(addDecodeListener(outputMessage, rsa));
		setButtonAesthetics(decodeButton);
		outputPanel.add(decodeButton, BorderLayout.SOUTH);
		
		container.add(enterPanel);
		container.add(outputPanel);
		frame.setVisible(true);
		
	}
	
	public static void setBigPanelAesthetics(JPanel p) {
		p.setBorder(new EmptyBorder(10, 10, 10, 10));
		p.setLayout(new BorderLayout(5, 10));
		p.setPreferredSize(new Dimension(200, 30)); //magic num
		p.setBackground(Color.BLACK);
	}
	
	public static void setTextAreaAesthetics(JTextArea t, boolean editable) {
		t.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, false));
		t.setLineWrap(true);
		t.setEditable(editable);
	}
	
	public static void setButtonAesthetics(JButton b) {
		b.setBackground(Color.LIGHT_GRAY);
		b.setForeground(Color.BLACK);
		b.setBorder(BorderFactory.createRaisedBevelBorder());
	}
	
	public static ActionListener addEncodeListener(JTextArea textSource, JTextArea textBox,
			RSA rsa) {
		return new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				textBox.setText(sendEncodedMessage(textSource.getText(), rsa));
		}
		};
	}
	
	public static ActionListener addDecodeListener(JTextArea textBox, RSA rsa) {
		return new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					textBox.setText(sendDecodedMessage(textBox.getText(), rsa));
				}
				catch(IOException i) {
				}
		}
		};
	}
	
	public static String sendEncodedMessage(String message, RSA rsa) {
		final String SAD_SCOOBIE = "What was that? Your message was too long!";
		final int MAX_STR_LEN = 128;
		final int MAX_ARR_SIZE = 10;
		
		byte[][] b = new byte[MAX_ARR_SIZE][];
		StringBuilder returnString;
		int toEncrypt;
		int usedByteArr;
		
		while(true) {
			if(message.length() < MAX_STR_LEN * MAX_ARR_SIZE) {
				toEncrypt = 0;
				usedByteArr = 0;
				returnString = new StringBuilder();
				
				while(toEncrypt + MAX_STR_LEN < message.length()) {
					b[usedByteArr] = rsa.encrypt(message.substring(toEncrypt, 
							toEncrypt + MAX_STR_LEN));
					returnString.append(Scoober.Scoobify(b[usedByteArr]));
					
					toEncrypt += 128;
					usedByteArr++;
				}
				
				if(message.length() != 0) {
					b[usedByteArr] = rsa.encrypt(message.substring(toEncrypt, message.length()));
					
					returnString.append(Scoober.punctuateScoobieString(Scoober.Scoobify(b[usedByteArr])));
					
					usedByteArr++;
					toEncrypt = 0;
				}
				return returnString.toString();
			}
			else {
				return SAD_SCOOBIE;
			}
		}
	}
	
	public static String sendDecodedMessage(String message, RSA rsa) throws IOException{
		return rsa.decrypt(Scoober.deScoobify(message));
	}

}
