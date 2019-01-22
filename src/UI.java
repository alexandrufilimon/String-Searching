
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

public class UI {
	
	private JTextField patternSearch;
	private JTextArea textArea = new JTextArea();
	private JLabel lblMatchings = new JLabel("Matchings found: ");
	private JLabel counts = new JLabel();
	private KMP object = new KMP();
	private ArrayList<Integer> matchingIx;
	private Highlighter highlighter = textArea.getHighlighter();
	


	// +++++++++++++++++++++++++++ CONSTRUCTOR +++++++++++++++++++++++++++ 
	public UI() {
		initialize();
	}


	// +++++++++++++++++++++++++++ MEMBER FUNCTIONS +++++++++++++++++++++++++++ 
	private void initialize() {
		// ++++++++++++++++ STYLE FRAME ++++++++++++++++
		JFrame frame = new JFrame();
		ImageIcon img = new ImageIcon("icon.png");
		frame.setIconImage(img.getImage());
		frame.setTitle("KMP - String search");
		frame.setBounds(100, 100, 572, 332);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		// ++++++++++++++++ STYLE PATTERN TEXTFIELD ++++++++++++++++
		patternSearch = new JTextField();
		patternSearch.setBounds(10, 11, 318, 20);
		frame.getContentPane().add(patternSearch);
		patternSearch.setColumns(10);
		
		// ++++++++++++++++ STYLE SEARCH BUTTON ++++++++++++++++
		JButton butonSearch = new JButton("Search");
		butonSearch.addActionListener(new ActionListener()
		{	  
			public void actionPerformed(final ActionEvent e){
					SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
						   @Override
						   protected Boolean doInBackground() throws Exception {
								String pattern = patternSearch.getText();
								String txt = textArea.getText();
								object.searchKMP(pattern, txt);
						    return true;
						   }

						   protected void done() {
								counts.setText(KMP.matchCount + "");
								KMP.matchCount = 0;
								resetHighlight();
								matchingIx = new ArrayList<Integer>(KMP.matchIndex);
								KMP.matchIndex.clear();
						   }

						  };

						  worker.execute();
			}
		});
		butonSearch.setBounds(335, 10, 89, 23);
		frame.getContentPane().add(butonSearch);
		
		// ++++++++++++++++ SCROLLPANE, TEXTAREA AND LABELS ++++++++++++++++
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(textArea);
		scrollPane.setBounds(10, 42, 536, 209);
		frame.getContentPane().add(scrollPane);
		
		textArea.setLineWrap(true);
		
		scrollPane.setViewportView(textArea);
		
		lblMatchings.setBounds(10, 262, 114, 21);
		frame.getContentPane().add(lblMatchings);
		
		counts.setBounds(111, 262, 46, 21);
		frame.getContentPane().add(counts);
		
		
		// ++++++++++++++++ STYLE HIGHLIGHT BUTTON ++++++++++++++++
		JButton butonHighlight = new JButton("Highlight matchings");
		butonHighlight.addActionListener(new ActionListener()
		{	  
			public void actionPerformed(final ActionEvent e){
			    HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
				for(int i = 0; i<matchingIx.size(); i++){
				try {
					highlighter.addHighlight(matchingIx.get(i), matchingIx.get(i)+patternSearch.getText().length(), painter);
				} catch (BadLocationException g) {
					g.printStackTrace();
				}
				}
			}
		});
		butonHighlight.setBounds(394, 261, 152, 23);
		frame.getContentPane().add(butonHighlight);
		
		JButton butonOpenFile = new JButton("Open file...");
		butonOpenFile.addActionListener(new ActionListener()
		{	  
			public void actionPerformed(final ActionEvent e){
				try {
					loadFile();
					resetHighlight();
				    counts.setText("0");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		butonOpenFile.setBounds(442, 10, 104, 23);
		frame.getContentPane().add(butonOpenFile);
		
		//
		frame.setVisible(true);
		//
	}
	
	// ++++++++++++++++ JFileChooser + Read to JTextArea ++++++++++++++++
	private void loadFile() throws IOException{
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Select a TXT File");
		jfc.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT Files", "txt");
		jfc.addChoosableFileFilter(filter);

		int returnValue = jfc.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			jfc.getSelectedFile().getPath();
		}
		
		// +++++++++++++++++++++++++ Reading .TXT File ++++++++++++++++++++++++
		try(final Reader in = new FileReader(jfc.getSelectedFile().getPath()))
		{
			textArea.read(in, in);
		}
	}
	
	private void resetHighlight(){
		// ++++++++++++++++++++++ RESET HIGHLIGHTED WORDS +++++++++++++++++++
		Highlighter.Highlight[] hilites = highlighter.getHighlights();
	    for (int i = 0; i < hilites.length; i++) {
	      if (hilites[i].getPainter() instanceof HighlightPainter) {
	        highlighter.removeHighlight(hilites[i]);
	      }
	    }
	}
}
