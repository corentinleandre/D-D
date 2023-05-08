package filesync;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

public class GUI extends JFrame {
	public static HashMap<Integer,RemoteSyncFolder> folders = new HashMap<Integer,RemoteSyncFolder>();
	public static JTextField addField;
	public static JTextArea listArea;
	public static ArrayList<JTextArea> listAreas = new ArrayList<JTextArea>();
	public static JComboBox<String> nbListesDropdown;
	public static JComboBox<String> listeDropdown;

	public GUI() {
	    // création de la fenêtre
	    setTitle("Synchronisation de dossiers");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setSize(400, 300);

	    // ajout d'un panneau pour la saisie du chemin d'un nouveau dossier synchronisé
	    JPanel addPanel = new JPanel();
	    JLabel addLabel = new JLabel("Nouveau dossier synchronisé:");
	    addField = new JTextField(20);
	    addPanel.add(addLabel);
	    addPanel.add(addField);
	    GererParametres gererParametres = new GererParametres(this);
		addField.addActionListener(gererParametres);

	    // ajout d'un panneau pour l'affichage des dossiers synchronisés
	    JPanel listPanel = new JPanel();
	    JTextArea listArea = new JTextArea(10, 30);
	    listAreas.add(listArea);
	    listArea.setEditable(false);
	    
	    JButton syncButton = new JButton("Synchroniser");
		syncButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: implémenter la synchronisation
			}
		});
		syncButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		JButton pauseButton = new JButton("Pause");
			pauseButton.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent e) {
			        // TODO: implémenter la pause
			    }
			});
		pauseButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel littlePanel = new JPanel();
		littlePanel.setLayout(new BoxLayout(littlePanel, BoxLayout.Y_AXIS));
		JLabel listLabel = new JLabel("Liste 1 :");
		listLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		littlePanel.add(listLabel);
		littlePanel.add(new JScrollPane(listArea));
		littlePanel.add(syncButton);
		littlePanel.add(pauseButton);
		littlePanel.setBackground(Color.PINK);
		listPanel.add(littlePanel);
		listPanel.setBackground(Color.BLUE);

	    // ajout d'un panneau pour les boutons
	    JPanel buttonPanel = new JPanel();
	    JButton quitButton = new JButton("Quitter");
	    quitButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            System.exit(0);
	        }
	    });
	    buttonPanel.add(quitButton);

	    // ajout d'un menu déroulant pour le nombre de listes
	    String[] nbListes = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
	    nbListesDropdown = new JComboBox<>(nbListes);
	    nbListesDropdown.setSelectedIndex(0);
	    nbListesDropdown.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	listLabel.setText("");
	            JComboBox cb = (JComboBox) e.getSource();
	            int nbListes = Integer.parseInt((String) cb.getSelectedItem());

	            // suppression des anciennes listes
	            listPanel.removeAll();

	            // ajout des nouvelles listes
	            for (int i = 0; i < nbListes; i++) {
	                
	                JTextArea listArea = new JTextArea(10, 30);
	                if(listAreas.size()>i){
	                	listArea = listAreas.get(i);
	                }
	                else{
	                	listAreas.add(listArea);
	                }
	                listArea.setEditable(false);
	                listLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	                listPanel.add(listLabel);
	                JButton syncButton = new JButton("Synchroniser");
	                syncButton.setAlignmentX(Component.CENTER_ALIGNMENT);
					syncButton.addActionListener(new ActionListener() {
					    public void actionPerformed(ActionEvent e) {
					        // TODO: implémenter la synchronisation
					    }
					});
					JButton pauseButton = new JButton("Pause");
					pauseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
					pauseButton.addActionListener(new ActionListener() {
					    public void actionPerformed(ActionEvent e) {
					        // TODO: implémenter la pause
					    }
					});

					JPanel littlePanel = new JPanel();
					JLabel listLabel = new JLabel("Liste " + (i + 1) + ":");
					littlePanel.setLayout(new BoxLayout(littlePanel, BoxLayout.Y_AXIS));
					littlePanel.add(listLabel);
					littlePanel.add(new JScrollPane(listArea));
					littlePanel.add(syncButton);
					littlePanel.add(pauseButton);
					littlePanel.setBackground(Color.PINK);
					listPanel.add(littlePanel);
	            }

	            if(listAreas.size()>nbListes){
	            	int length = listAreas.size();
	            	for(int i=(length-1);i>=nbListes;i--){
	            		listAreas.remove(i);
	            	}
	            }

	            // réaffichage des panneaux
	            revalidate();
	            repaint();
	        }
	    });

	    // ajout d'un menu déroulant pour la sélection de la liste
		listeDropdown = new JComboBox<>();
		for (int i = 0; i < nbListesDropdown.getSelectedIndex() + 1; i++) {
		    listeDropdown.addItem("Liste " + (i + 1));
		}
		addPanel.add(listeDropdown);

		// mise à jour du menu déroulant en fonction du nombre de listes
		nbListesDropdown.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        int nbListes = nbListesDropdown.getSelectedIndex() + 1;
		        listeDropdown.removeAllItems();
		        for (int i = 0; i < nbListes; i++) {
		            listeDropdown.addItem("Liste " + (i + 1));
		        }
		    }
		});

	    // ajout des panneaux à la fenêtre
	    setLayout(new BorderLayout());
	    add(addPanel, BorderLayout.NORTH);
	    add(nbListesDropdown, BorderLayout.WEST);
	    add(listPanel, BorderLayout.CENTER);
	    add(buttonPanel, BorderLayout.SOUTH);

	    // affichage de la fenêtre
	    setVisible(true);
	}
}

class GererParametres implements ActionListener {
    private GUI ui;

    public GererParametres(GUI ui) {
        this.ui = ui;
    }

    public void actionPerformed(ActionEvent evt) {
	    JTextField tf = (JTextField) evt.getSource();
	    String s = tf.getText();
	    int nombreDeListes = Integer.parseInt((String) ui.nbListesDropdown.getSelectedItem());
	    int numeroListe = ui.listeDropdown.getSelectedIndex();

	    if (tf.equals(ui.addField)) {
	        // créer un nouvel objet SyncFolder avec le chemin saisi par l'utilisateur
	    	if(s.startsWith("rmi://")) {
	    		try {
	    			System.out.println("looking up : " + s);
					Remote r = Naming.lookup(s);
					if(r instanceof RemoteSyncFolder) {
						ui.folders.put(numeroListe,(RemoteSyncFolder) r);
						(ui.listAreas.get(numeroListe)).append(s + "\n");
				        ui.addField.setText("");
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}else {
				try {
					SyncFolder folder = new SyncFolder(s);
					// ajouter le nouvel objet SyncFolder à la liste folders
			        ui.folders.put(numeroListe,folder);

			        // mettre à jour les listes dans listPanel
					(ui.listAreas.get(numeroListe)).append(s + "\n");

			        // effacer le contenu de la zone de texte addField
			        ui.addField.setText("");
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    	
	    	

	        
	    }
	}
} // fin GererParametres

