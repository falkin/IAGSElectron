package ch.eif.ihm2.ihm;

import java.awt.*;
import java.awt.event.*;
import java.util.Locale;
import javax.swing.*;
import javax.swing.event.*;
import net.miginfocom.swing.MigLayout;
import ch.eif.ihm2.cst.Constants;
import ch.eif.ihm2.model.*;

/**
 * Menu Panel User Interface
 * 
 * @author Bernhard Leutwiler, Stefan Aebischer
 * 
 */
@SuppressWarnings("serial")
public class MenuPanel extends JDialog  {
	private IModelOperations iModOp;
	private GameFrame parent;
	private ISettings settings; // Settings reference for asynchronous modifications
	// Menu GUI Components
	private JTabbedPane menuTabbedPane = new JTabbedPane();
	private JLabel title = new JLabel();
	private JLabel player1Lab = new JLabel();
	private JLabel desactivatePlayer2 = new JLabel();
	private JTextField inputPlayer1Name = new JTextField(20);
	private JButton colorBtP1 = new JButton();
	private JLabel player2Lab = new JLabel();
	private JTextField inputPlayer2Name = new JTextField(20);
	private JButton colorBtP2 = new JButton();
	private JLabel labSpeed = new JLabel();
	private JRadioButton radioBtSlow = new JRadioButton();
	private JRadioButton radioBtNormal = new JRadioButton();
	private JRadioButton radioBtHigh = new JRadioButton();
	private JRadioButton radioBtGodlike = new JRadioButton();
	private JButton btPlay = new JButton("Hallo");
	private JLabel copyrightLabel = new JLabel();
	private ButtonGroup speedGrp = new ButtonGroup();
	private JLabel labMovement = new JLabel();
	private JLabel labShoot = new JLabel();
	private JLabel labReammo = new JLabel();
	private JLabel labMaxLen = new JLabel();
	private JSpinner rec = new JSpinner();
	private JSpinner maxlen = new JSpinner();
	private JLabel labData = new JLabel();
	private JComboBox player1List = new JComboBox(Constants.NAME_P1_LIST);
	private JComboBox player2List = new JComboBox(Constants.NAME_P2_LIST);
	private JCheckBox chkPlayer2 = new JCheckBox();
	private JButton load = new JButton();
	private JButton save = new JButton();
	private JButton reset = new JButton();
	private JLabel helpTitle = new JLabel();
	private JTextArea helpTextArea = new JTextArea();
	private JComboBox langCombo = new JComboBox();
	private JLabel msgLab = new JLabel("  ");
	private MovementKeyPanel k1 = new MovementKeyPanel(1);
	private MovementKeyPanel k2 = new MovementKeyPanel(2);
	private MovementKeyPanel k3 = new MovementKeyPanel(3);
	/**
	 * Constructor
	 * 
	 * @param parent - Parent element for a correct placement
	 * @param m
	 */
	public MenuPanel(GameFrame parent, IModelOperations iModOp) {
		super(parent);
		this.parent = parent;
		this.iModOp = iModOp;
		settings = Settings.getInstance();
		setUndecorated(true);
		setModalityType(Dialog.ModalityType.MODELESS);
		setMinimumSize(new Dimension(Constants.MENUWIDTH, Constants.MENUHEIGHT));

		menuTabbedPane.addTab("", getMenuTab());
		menuTabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		menuTabbedPane.addTab("", getSettingsTab());
		menuTabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
		menuTabbedPane.addTab("", getHelpTab());
		menuTabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

		menuTabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				switch (menuTabbedPane.getSelectedIndex()){
					case 0 : btPlay.requestFocusInWindow(); break;
					case 1 : labMovement.requestFocusInWindow(); break;
					case 2 : helpTitle.requestFocusInWindow(); break;
					default: break;
				}
			}
		});
		retranslate();
		reLoadContent();
		this.add(menuTabbedPane);
		pack();
		setLocationRelativeTo(parent);
		
		this.getRootPane().setDefaultButton(btPlay);
	}

	/**
	 * Method called when language is changed, to change component texts
	 */
	public void retranslate() {
		menuTabbedPane.setTitleAt(0, Translate.fromKey("menu.menutab"));
		menuTabbedPane.setTitleAt(1, Translate.fromKey("menu.settingstab"));
		menuTabbedPane.setTitleAt(2, Translate.fromKey("menu.helptab"));
		menuTabbedPane.setBackground(Color.WHITE);
		title.setText(Translate.fromKey("title"));
		player1Lab.setText(Translate.fromKeyWithParam("playerNb",
				new Integer(1)));
		desactivatePlayer2.setText(Translate.fromKey("menu.activate"));
		colorBtP1.setText(Translate.fromKey("menu.menutab.color"));
		player2Lab.setText(Translate.fromKeyWithParam("playerNb",
				new Integer(2)));
		colorBtP2.setText(Translate.fromKey("menu.menutab.color"));
		labSpeed.setText(Translate.fromKey("menu.menutab.speed"));
		radioBtSlow.setText(Translate.fromKey("menu.menutab.slow"));
		radioBtNormal.setText(Translate.fromKey("menu.menutab.normal"));
		radioBtHigh.setText(Translate.fromKey("menu.menutab.fast"));
		radioBtGodlike.setText(Translate.fromKey("menu.menutab.godlike"));
		btPlay.setText(Translate.fromKey("play"));
		btPlay.setMnemonic(btPlay.getText().charAt(0));
		labMovement.setText(Translate.fromKey("menu.settingstab.movement"));
		labShoot.setText(Translate.fromKey("menu.settingstab.shoot"));
		labReammo.setText(Translate.fromKey("menu.settingstab.weaponreammo"));
		labMaxLen.setText(Translate.fromKey("menu.settingstab.maxpllen"));
		labData.setText(Translate.fromKey("menu.settingstab.gamedata"));
		load.setText(Translate.fromKey("load"));
		save.setText(Translate.fromKey("save"));
		reset.setText(Translate.fromKey("reset"));
		helpTextArea.setText(Translate.fromKey("menu.help.text"));
		helpTitle.setText(Translate.fromKey("menu.help.title"));
		copyrightLabel.setText(Translate.fromKey("copyright"));
		pack();
		setLocationRelativeTo(parent);
	}

	/**
	 * Method to get or generate contents of settings fields
	 */
	public void reLoadContent() {
		// Main
		player1List.setSelectedIndex(1);
		//player1List.addActionListener(this);
	 
		player2List.setSelectedIndex(0);
		//player2List.addActionListener(this);

		//inputPlayer1Name.setText(settings.getPlayer1().getName());
		iModOp.getInfoPropertyChangeSupport().firePropertyChange("name1", null,
				Constants.DEFAULT_AI);
		//inputPlayer2Name.setText(settings.getPlayer2().getName());
		iModOp.getInfoPropertyChangeSupport().firePropertyChange("name2", null,
				Constants.DEFAULT_P2_NAME);
		colorBtP1.setForeground(settings.getPlayer1().getColor());
		colorBtP2.setForeground(settings.getPlayer2().getColor());
		switch (settings.getDifficulty()) {
		case SLOW:
			radioBtSlow.setSelected(true);
			break;
		case NORMAL:
			radioBtNormal.setSelected(true);
			break;
		case FAST:
			radioBtHigh.setSelected(true);
			break;
		case GODLIKE:
			radioBtGodlike.setSelected(true);
			break;
		default:
			radioBtNormal.setSelected(true);
			break;
		}

		langCombo.removeActionListener(langAct);
		for (int i = 0; i < Constants.LANGUAGES.length; i++) {
			String lang = Constants.LANGUAGES[i];
			langCombo.addItem(new Locale(lang).getDisplayLanguage(new Locale(
					lang)));
			if (Translate.getLocale().getLanguage().equals(lang)) {
				langCombo.setSelectedIndex(i);
			}
		}
		langCombo.addActionListener(langAct);

		// Settings
		k1.reloadContent();
		k2.reloadContent();
		k3.reloadContent();
		rec.setModel(new SpinnerNumberModel(iModOp.getSettings()
				.getWeaponRechargeTime(), 0, Constants.MAX_RECTIME, 1));
		maxlen.setModel(new SpinnerNumberModel(iModOp.getSettings()
				.getMaxPlayerLength(), 0, Constants.MAX_QUEUELEN, 1));

	}

	/**
	 * Generates a Panel with the content of Main Menu
	 * 
	 * @return - JPanel
	 */
	public JPanel getMenuTab() {
		MigLayout menuLayout = new MigLayout("", "[]20[grow]20[]",
				"20[]20[][][]30[][][]");

		JPanel menupan = new JPanel(menuLayout);
		
		title.setFont(new Font("ARIAL", Font.BOLD, 25));
		title.setForeground(Color.DARK_GRAY);
		title.setHorizontalAlignment(JLabel.CENTER);
		menupan.add(title, "growx, span, center,gaptop 20,gapbottom 20");

		
	/*	inputPlayer1Name.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent arg0) {
				if (inputPlayer1Name.getText().equals("")) {
					inputPlayer1Name.setText(Constants.DEFAULT_P1_NAME);
					iModOp.getInfoPropertyChangeSupport().firePropertyChange(
							"name1", null, Constants.DEFAULT_P1_NAME);
				} else if (inputPlayer1Name.getText().equals(
						settings.getPlayer2().getName())) {
					JOptionPane.showMessageDialog(null, "Name invalid", "",
							JOptionPane.ERROR_MESSAGE);
					inputPlayer1Name.setText(Constants.DEFAULT_P1_NAME);
					iModOp.getInfoPropertyChangeSupport().firePropertyChange(
							"name1", null, Constants.DEFAULT_P1_NAME);
				} else if (inputPlayer1Name.getText().length() > 20) {
					JOptionPane.showMessageDialog(null, "Name too long", "",
							JOptionPane.ERROR_MESSAGE);
					inputPlayer1Name.setText(Constants.DEFAULT_P1_NAME);
					iModOp.getInfoPropertyChangeSupport().firePropertyChange(
							"name1", null, Constants.DEFAULT_P1_NAME);
				}
			}

			public void focusGained(FocusEvent arg0) {
				if (inputPlayer1Name.getText()
						.equals(Constants.DEFAULT_P1_NAME)) {
					inputPlayer1Name.setText("");
					iModOp.getInfoPropertyChangeSupport().firePropertyChange(
							"name1", null, Constants.DEFAULT_P1_NAME);
				}
			}
		});
		inputPlayer1Name.getDocument().addDocumentListener(
				new DocumentListener() {
					public void removeUpdate(DocumentEvent arg0) {
						update();
					}

					public void insertUpdate(DocumentEvent arg0) {
						update();
					}

					public void changedUpdate(DocumentEvent arg0) {}

					private void update() {
						String newText = inputPlayer1Name.getText();
						if (newText.length() > 20) {
							newText = newText.substring(0, 20);
						}
						settings.getPlayer1().setName(newText);
						k1.reloadContent();
						iModOp.getInfoPropertyChangeSupport()
								.firePropertyChange("name1", null, newText);
					}
				});
		
		inputPlayer2Name.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent arg0) {
				if (inputPlayer2Name.getText().equals("")) {
					inputPlayer2Name.setText(Constants.DEFAULT_P2_NAME);
					iModOp.getInfoPropertyChangeSupport().firePropertyChange(
							"name2", null, Constants.DEFAULT_P2_NAME);
				} else if (inputPlayer2Name.getText().equals(
						settings.getPlayer1().getName())) {
					JOptionPane.showMessageDialog(null, "Name invalid", "",
							JOptionPane.ERROR_MESSAGE);
					inputPlayer2Name.setText(Constants.DEFAULT_P2_NAME);
					iModOp.getInfoPropertyChangeSupport().firePropertyChange(
							"name2", null, Constants.DEFAULT_P2_NAME);
				} else if (inputPlayer2Name.getText().length() > 20) {
					JOptionPane.showMessageDialog(null, "Name too long", "",
							JOptionPane.ERROR_MESSAGE);
					inputPlayer2Name.setText(Constants.DEFAULT_P2_NAME);
					iModOp.getInfoPropertyChangeSupport().firePropertyChange(
							"name2", null, Constants.DEFAULT_P2_NAME);
				}
			}

			public void focusGained(FocusEvent arg0) {
				if (inputPlayer2Name.getText()
						.equals(Constants.DEFAULT_P2_NAME)) {
					inputPlayer2Name.setText("");
					iModOp.getInfoPropertyChangeSupport().firePropertyChange(
							"name2", null, Constants.DEFAULT_P2_NAME);
				}
			}
		});
		inputPlayer2Name.getDocument().addDocumentListener(
				new DocumentListener() {
					public void removeUpdate(DocumentEvent arg0) {
						update();
					}

					public void insertUpdate(DocumentEvent arg0) {
						update();
					}

					public void changedUpdate(DocumentEvent arg0) {
					}

					private void update() {
						String newText = inputPlayer2Name.getText();
						if (newText.length() > 20) {
							newText = newText.substring(0, 20);
						}
						settings.getPlayer2().setName(newText);
						k2.reloadContent();
						iModOp.getInfoPropertyChangeSupport()
								.firePropertyChange("name2", null, newText);
					}
				});*/

		menupan.add(player1Lab, "shrink, gapleft 20");
		menupan.add(player1List, "gapleft 20, growx");
		colorBtP1.addActionListener(colorswitch);
		menupan.add(colorBtP1, "right, gapright 20, wrap");

		menupan.add(player2Lab, "shrink, gapleft 20");
		menupan.add(player2List, "gapleft 20, growx");
		colorBtP2.addActionListener(colorswitch);
		menupan.add(colorBtP2, "right, gapright 20, wrap");
		menupan.add(desactivatePlayer2,  "shrink, gapleft 20");
		menupan.add(chkPlayer2,  " gapleft 20,  wrap");
		menupan.setBackground(Color.WHITE);
		player1List.addItemListener(selectPlayer1);
		player2List.addItemListener(selectPlayer2);
		chkPlayer2.setBackground(Color.WHITE);
		chkPlayer2.setSelected(true);
		chkPlayer2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chkPlayer2.isSelected()) {
					player2List.setEnabled(true);
					colorBtP2.setEnabled(true);	
					iModOp.getInfoPropertyChangeSupport().firePropertyChange("name2", null,
							player2List.getSelectedItem());
					iModOp.getInfoPropertyChangeSupport().firePropertyChange("coverPlayer2", null,
							true);	
				}
				else{
					player2List.setEnabled(false);
					colorBtP2.setEnabled(false);
					iModOp.getInfoPropertyChangeSupport().firePropertyChange("name2", null,
							"");
					iModOp.getInfoPropertyChangeSupport().firePropertyChange("coverPlayer2", null,
							false);					
				}
			}
		});
		iModOp.getInfoPropertyChangeSupport().firePropertyChange(
				"name1", null, player1List.getSelectedItem().toString());	
		 iModOp.getInfoPropertyChangeSupport().firePropertyChange(
					"name2", null, player2List.getSelectedItem().toString());
		radioBtSlow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (radioBtSlow.isSelected()) {
					settings.setDifficulty(Constants.DIFFICULTY.SLOW);
				}
			}
		});

		radioBtNormal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (radioBtNormal.isSelected()) {
					settings.setDifficulty(Constants.DIFFICULTY.NORMAL);
				}
			}
		});

		radioBtHigh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (radioBtHigh.isSelected()) {
					settings.setDifficulty(Constants.DIFFICULTY.FAST);
				}
			}
		});

		radioBtGodlike.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (radioBtGodlike.isSelected()) {
					settings.setDifficulty(Constants.DIFFICULTY.GODLIKE);
				}
			}
		});
		
		menupan.add(labSpeed,"span, gapleft 20");
		langCombo.addActionListener(langAct);

		menupan.add(radioBtSlow, "span 2, gapleft 20, wrap");
		menupan.add(radioBtNormal, "span 2, gapleft 20");
		menupan.add(langCombo, "span 2, gapleft 20, gapright 20, right, wrap");
		menupan.add(radioBtHigh, "span 2, gapleft 20, wrap");
		menupan.add(radioBtGodlike, "span 2, gapleft 20, wrap");
		speedGrp.add(radioBtSlow);
		speedGrp.add(radioBtNormal);
		speedGrp.add(radioBtHigh);
		speedGrp.add(radioBtGodlike);
		radioBtSlow.setBackground(Color.WHITE);
		radioBtNormal.setBackground(Color.WHITE);
		radioBtGodlike.setBackground(Color.WHITE);
		radioBtHigh.setBackground(Color.WHITE);
		btPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.startGame(player1List.getSelectedItem().toString(),player2List.getSelectedItem().toString(),chkPlayer2.isSelected());
			}
		});
		menupan.add(btPlay, "span, w 200!, h 40!, center, wrap");
		return menupan;
	}

	/**Method to generate Settings Panel
	 * 
	 * @return - JPanel
	 */
	public JPanel getSettingsTab() {
		MigLayout settingsLayout = new MigLayout("", "[][grow][]",
				"20[]20[]20[][]20[]");

		JPanel settingsPan = new JPanel(settingsLayout);

		labMovement.setHorizontalAlignment(JLabel.CENTER);
		labMovement.setFont(new Font("ARIAL", Font.BOLD, 25));
		labMovement.setForeground(Color.DARK_GRAY);
		
		settingsPan.add(labMovement, "cell 0 0 3 1, grow");

		settingsPan.add(k1, "cell 0 1 1 1, gapleft 20");
		settingsPan.add(labShoot, "cell 1 1 1 1, growx, bottom, gapbottom 5");
		settingsPan.add(k2, "cell 2 1 1 1, right, gapright 20");
		settingsPan.setBackground(Color.WHITE);
		k1.setBackground(Color.WHITE);
		k2.setBackground(Color.WHITE);
		settingsPan.add(labReammo, "cell 0 2 1 1, gapleft 20");
		rec.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				iModOp.getSettings().setWeaponRechargeTime(
						((Integer) rec.getValue()).intValue());
			}
		});
		settingsPan.add(rec, "cell 2 2 1 1, right, gapright 20");

		settingsPan.add(labMaxLen, "cell 0 3 1 1, gapleft 20");
		maxlen.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				iModOp.getSettings().setMaxPlayerLength(
						((Integer) maxlen.getValue()).intValue());
			}
		});
		
		settingsPan.add(maxlen, "cell 2 3 1 1, right, gapright 20");
		
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable() {
					public void run() {
						settings.loadSettingsFromDisk();
						msgLab.setForeground(Color.green);
						msgLab.setText("ok");
						reLoadContent();
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						msgLab.setText("  ");
					}
				}).start();
			}
		});

		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					public void run() {
						settings.saveSettingsToDisk();
						msgLab.setForeground(Color.green);
						msgLab.setText("ok");
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						msgLab.setText("  ");
					}
				}).start();
			}
		});

		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					public void run() {
						settings.resetSettings();
						msgLab.setForeground(Color.green);
						msgLab.setText("ok");
						reLoadContent();
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						msgLab.setText("  ");
					}
				}).start();
			}
		});

		settingsPan.add(labData, "cell 0 4 1 1, gapleft 20");
		settingsPan.add(msgLab, "cell 1 4 2 1, span 2, split 4, right");
		settingsPan.add(load);
		settingsPan.add(save);
		settingsPan.add(reset, "gapright 20");

		return settingsPan;
	}

	/**
	 * Method to generate Help Panel
	 * 
	 * @return - JPanel
	 */
	public JPanel getHelpTab() {
		MigLayout helpLayout = new MigLayout("insets 40, wrap 1, center");
		JPanel helpPanel = new JPanel(helpLayout);
		helpTitle.setFont(new Font("ARIAL", Font.BOLD, 25));
		helpTitle.setForeground(Color.DARK_GRAY);
		helpTextArea.setEditable(false);
		helpTextArea.setBackground(Color.BLACK);
		helpTextArea.setForeground(Color.GREEN);
		JScrollPane scroll = new JScrollPane(helpTextArea);
		helpPanel.add(helpTitle, "center, gaptop 10, gapbottom 20, wrap");
		helpPanel.add(scroll, "w 450px, h 200px, center");
		copyrightLabel.setForeground(Color.GRAY);
		copyrightLabel.setHorizontalAlignment(JLabel.CENTER);
		helpPanel.add(copyrightLabel, "span,gaptop 20,center,growx");
		helpPanel.setBackground(Color.WHITE);
		return helpPanel;
	}

	/**
	 * ActionListener to handle language switch
	 */
	ActionListener langAct = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			Translate.setLocale(new Locale(Constants.LANGUAGES[langCombo
					.getSelectedIndex()]));
			settings.setLocale(Translate.getLocale());
			parent.retranslate();
		}
	};
	/*
	 * ActionListener to handle color choosers
	 */
	ItemListener  selectPlayer1 = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			 if (e.getStateChange() == ItemEvent.SELECTED) {
		          Object item = e.getItem();
		          if(item == "AI"){
		        	  iModOp.getInfoPropertyChangeSupport().firePropertyChange(
								"name1", null, item);	  
		          }
		          else{
		        	  iModOp.getInfoPropertyChangeSupport().firePropertyChange(
								"name1", null, item);	
		          }// do something with object
		       }
		}
	};
	ItemListener  selectPlayer2 = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			 if (e.getStateChange() == ItemEvent.SELECTED) {
		          Object item = e.getItem();
		          if(item == "AI"){
		        	  iModOp.getInfoPropertyChangeSupport().firePropertyChange(
								"name2", null, item);	        	  
		          }
		          else{
		        	  iModOp.getInfoPropertyChangeSupport().firePropertyChange(
								"name2", null, item);	
		          }// do something with object
		       }
		}
	};
	/*
	 * ActionListener to handle color choosers
	 */
	ActionListener colorswitch = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			Color c = Color.BLACK;
			Color colorP1 = settings.getPlayer1().getColor();
			Color colorP2 = settings.getPlayer2().getColor();
			int diff = 0, diff2 = 0;
			int player = 2;
			int green = 0, blue = 0, red = 0;

			if (evt.getSource().equals(colorBtP1)) {
				player = 1;
			}

			switch (player) {
			case 1:
				c = JColorChooser.showDialog(null, Translate.fromKeyWithParam(
						"menu.menutab.colorchooser.banner", 1), settings
						.getPlayer1().getColor());
				if (c == null) {
					c = settings.getPlayer1().getColor();
				}
				green = c.getGreen() - colorP2.getGreen();
				blue = c.getBlue() - colorP2.getBlue();
				red = c.getRed() - colorP2.getRed();
				break;
			case 2:
				c = JColorChooser.showDialog(null, Translate.fromKeyWithParam(
						"menu.menutab.colorchooser.banner", 1), settings
						.getPlayer2().getColor());
				if (c == null) {
					c = settings.getPlayer2().getColor();
				}
				green = c.getGreen() - colorP1.getGreen();
				blue = c.getBlue() - colorP1.getBlue();
				red = c.getRed() - colorP1.getRed();
				break;
			}
			diff = (int) Math.round(Math
					.sqrt((green * green + blue * blue + red * red)));
			diff2 = (int) Math.round(Math.sqrt((c.getGreen() * c.getGreen()
					+ c.getBlue() * c.getBlue() + c.getRed() * c.getRed())));
			if (diff2 < Constants.COLOR_BRIGHTNESS_LEVEL) {
				JOptionPane.showMessageDialog(null,
						Translate.fromKey("menu.settingstab.colortooblack"),
						"", JOptionPane.ERROR_MESSAGE);
			} else if (diff < Constants.MINIMAL_COLOR_DIFFERENCE) {
				JOptionPane.showMessageDialog(null,
						Translate.fromKey("menu.settingstab.colortooclose"),
						"", JOptionPane.ERROR_MESSAGE);
			} else {
				switch (player) {
				case 1:
					colorBtP1.setForeground(c);
					settings.getPlayer1().setColor(c);
					iModOp.getInfoPropertyChangeSupport().firePropertyChange(
							"color1", null, c);
					break;
				case 2:
					colorBtP2.setForeground(c);
					settings.getPlayer2().setColor(c);
					iModOp.getInfoPropertyChangeSupport().firePropertyChange(
							"color2", null, c);
					break;
				}
			}
		}
	};


}
