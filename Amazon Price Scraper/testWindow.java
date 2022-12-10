package testing;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class testWindow {
	


	public testWindow(String currPrice) {
		JFrame frame = new JFrame();
		
		String asinID;
		String prevPrice;
		
		//JButton button = new JButton("Current Price");
		
		JTextArea textArea = new JTextArea("Enter ASIN ID Here");
		
		JTextArea priceText = new JTextArea("Enter your desired price");
		
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(50, 150, 50, 150));
		panel.setLayout(new GridLayout(0, 1));
		//panel.add(button);
		panel.add(textArea);
		textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    textArea.setBounds(50, 50, 150, 150);
	    
	    panel.add(priceText);
	    priceText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    priceText.setBounds(50, 50, 150, 150);
		
		
		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("web scraper");
		frame.pack();
		frame.setVisible(true);
		
		
		asinID = textArea.getText();
		prevPrice = priceText.getText();
		
	}


	
		
	}

		

		

