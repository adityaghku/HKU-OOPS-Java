import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
	
	/**
	 * Our main method which creates the GUI and calls all the methods and classes
	 * for the game to be played. 
	 * @param args The command line arguments which are not used in this program.  
	 **/
	
	public static void main(String[] args) {
		
		Gameplay game = new Gameplay();

		// Set up
		JLabel[] label_dealer = { new JLabel(), new JLabel(), new JLabel() };

		JLabel[] label_player = { new JLabel(), new JLabel(), new JLabel() };

		for (int i = 0; i < 3; i++) {
			label_dealer[i].setIcon(new ImageIcon("Images/card_back.gif"));
			label_player[i].setIcon(new ImageIcon("Images/card_back.gif"));
		}
		
		JButton[] rpcards = { new JButton("Replace Card 1"), new JButton("Replace Card 2"),
				new JButton("Replace Card 3") };

		JButton btn_start = new JButton("Start");
		JButton btn_result = new JButton("Result");

		JLabel label_bet = new JLabel("Bet: $");
		JLabel label_info = new JLabel("Please place your bet!");
		JLabel label_money = new JLabel(String.format("The amount of money you have: $%d", game.getUser().getWallet()));

		JTextField input_bet = new JTextField(10);

		for (int i = 0; i < 3; i++) {
			rpcards[i].setEnabled(false);
		}


		btn_result.setEnabled(false);

		JPanel MainPanel = new JPanel();
		JPanel DealerPanel = new JPanel();
		JPanel PlayerPanel = new JPanel();
		JPanel RpCardBtnPanel = new JPanel();
		JPanel ButtonPanel = new JPanel();
		JPanel InfoPanel = new JPanel();

		MainPanel.setLayout(new GridLayout(5, 1));
		MainPanel.add(DealerPanel);
		MainPanel.add(PlayerPanel);
		MainPanel.add(RpCardBtnPanel);
		MainPanel.add(ButtonPanel);
		MainPanel.add(InfoPanel);

		for (int i = 0; i < 3; i++) {
			DealerPanel.add(label_dealer[i]);
			PlayerPanel.add(label_player[i]);
			RpCardBtnPanel.add(rpcards[i]);
		}

		DealerPanel.setBackground(Color.green);
		PlayerPanel.setBackground(Color.green);
		RpCardBtnPanel.setBackground(Color.green);

		ButtonPanel.add(label_bet);
		ButtonPanel.add(input_bet);
		ButtonPanel.add(btn_start);
		ButtonPanel.add(btn_result);

		InfoPanel.add(label_info);
		InfoPanel.add(label_money);

		JMenu control = new JMenu("Control");
		JMenuItem exit = new JMenuItem("Exit");
		control.add(exit);

		JMenu help = new JMenu("Help");
		JMenuItem instructions = new JMenuItem("Instructions");
		help.add(instructions);

		JMenuBar menu = new JMenuBar();
		menu.add(control);
		menu.add(help);

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(MainPanel);
		frame.setJMenuBar(menu);
		frame.setTitle("A Simple Card Game");
		frame.setSize(500, 700);

		exit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}

		});

		instructions.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				JOptionPane.showMessageDialog(null, "J, Q, K are regarded as special cards.\n"
						+ "Rule 1: The one with more special cards wins.\n"
						+ "Rule 2: If both have the same number of special cards, add the face values of the other card(s) and take the remainder after dividing the sum by 10. The one with a bigger remainder wins. (Note: Ace = 1).\n"
						+ "Rule 3: The dealer wins if both rule 1 and rule 2 cannot distinguish the winner.");
			}

		});
		
		
		btn_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
				String betString = input_bet.getText();
				
				if (!game.validBet(betString)) {
					JOptionPane.showMessageDialog(null,"WARNING: The bet you place must be a positive integer");
					return;
				}
				
				int bet = Integer.parseInt(betString);
				
				if (game.getUser().getWallet() < bet) {
					JOptionPane.showMessageDialog(null,"You don't have enough money for this bet");
					return;
				}
				
				label_info.setText(String.format("Your current bet is $%d.", bet));
				
				game.setBet(bet);
				game.dealCards();
				
				for (int i = 0; i < 3; i++) {
					rpcards[i].setEnabled(true);
					label_player[i].setIcon(new ImageIcon(game.getUser().getImagePath(i)));
				}
				
				btn_result.setEnabled(true);
				btn_start.setEnabled(false);				
				
			}

		});

		btn_result.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
				game.setMaxMoves(0);
				
				for (int i = 0; i < 3; i++) {
					label_dealer[i].setIcon(new ImageIcon(game.getDealer().getImagePath(i)));
				}
				
				
				if (game.userWins()) {
					JOptionPane.showMessageDialog(null, "Congratulations! You win this round!");
					game.getUser().updateWallet(game.getBet(), true); 
				}
				
				else {
					JOptionPane.showMessageDialog(null, "Sorry! The dealer wins this round!");
					game.getUser().updateWallet(game.getBet(), false); 
				}
				
				if (game.getUser().getWallet() <=0) {
					JOptionPane.showMessageDialog(null, "Game over!\n" + "You have no more money!\n" + "Please start a new game!");
					label_info.setText("Game over! You have no more money! Please start a new game!");
					label_money.setText("");
				}
				
				else {
					label_info.setText("Please place your bet!");
					label_money.setText(String.format("The amount of money you have: $%d", game.getUser().getWallet()));
					btn_start.setEnabled(true);
				}
				
				btn_result.setEnabled(false);
				
				for (int i = 0; i < 3; i++) {
					rpcards[i].setEnabled(false);
					label_dealer[i].setIcon(new ImageIcon("FinalSubmission/Images/card_back.gif"));
					label_player[i].setIcon(new ImageIcon("FinalSubmission/Images/card_back.gif"));
				}
				
			}

		});

		for (int i = 0; i < 3; i++) {
			
			final int i2 = i;
			
			rpcards[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					
					game.replaceUserCards(i2);
					label_player[i2].setIcon(new ImageIcon (game.getUser().getImagePath(i2)));
					rpcards[i2].setEnabled(false);
					game.setMaxMoves(game.getMaxMoves() + 1);
					
					if (game.getMaxMoves() >=2) {
						for (int j = 0; j < 3; j++) {
							rpcards[j].setEnabled(false);
						}
					}
					// check if two cards have been replaced
				}
			});
		}

		frame.setVisible(true);

	}

}
