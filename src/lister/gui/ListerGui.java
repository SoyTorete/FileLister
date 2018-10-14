package lister.gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.Window.Type;

public class ListerGui {

	private JFrame frmFiledirectoryListTool;
	private JTextField dirTextfield;
	private JTextField dir_FileTextfield;
	private DefaultListModel<String> listModel;
	
	private boolean getFiles = false;
	private boolean getDirs = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ListerGui window = new ListerGui();
					window.frmFiledirectoryListTool.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ListerGui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmFiledirectoryListTool = new JFrame();
		frmFiledirectoryListTool.setType(Type.UTILITY);
		frmFiledirectoryListTool.setResizable(false);
		frmFiledirectoryListTool.setTitle("File/Directory List Tool");
		frmFiledirectoryListTool.setBounds(100, 100, 512, 255);
		frmFiledirectoryListTool.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmFiledirectoryListTool.getContentPane().setLayout(null);
		
		JLabel lblEnterADirectory = new JLabel("Enter a directory:");
		lblEnterADirectory.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblEnterADirectory.setBounds(10, 11, 92, 20);
		frmFiledirectoryListTool.getContentPane().add(lblEnterADirectory);
		
		dirTextfield = new JTextField();
		dirTextfield.setBounds(10, 30, 208, 20);
		frmFiledirectoryListTool.getContentPane().add(dirTextfield);
		dirTextfield.setColumns(10);
		
		dir_FileTextfield = new JTextField();
		dir_FileTextfield.setEnabled(false);
		dir_FileTextfield.setBounds(10, 104, 208, 20);
		frmFiledirectoryListTool.getContentPane().add(dir_FileTextfield);
		dir_FileTextfield.setToolTipText("Separate types with a comma; Leave blank to search for all types. Ex: txt,bin,jar");
		dir_FileTextfield.setColumns(10);
		
		JLabel lblFileTypes = new JLabel("File Types:");
		lblFileTypes.setBounds(16, 91, 58, 14);
		frmFiledirectoryListTool.getContentPane().add(lblFileTypes);
		lblFileTypes.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		JRadioButton rdbtnListFiles = new JRadioButton("List Files");
		rdbtnListFiles.setToolTipText("List the files with optional types in the given directory.");
		rdbtnListFiles.setBounds(10, 67, 109, 23);
		frmFiledirectoryListTool.getContentPane().add(rdbtnListFiles);
		rdbtnListFiles.setBackground(SystemColor.control);
		
		JRadioButton rdbtnListDirectories = new JRadioButton("List Directories");
		rdbtnListDirectories.setToolTipText("List all sub-directories inside the given directory.");
		rdbtnListDirectories.setBackground(SystemColor.menu);
		rdbtnListDirectories.setBounds(10, 134, 116, 23);
		frmFiledirectoryListTool.getContentPane().add(rdbtnListDirectories);
		
		JButton btnGenerateList = new JButton("Generate List");
		btnGenerateList.setBounds(51, 182, 135, 23);
		frmFiledirectoryListTool.getContentPane().add(btnGenerateList);
		
		listModel = new DefaultListModel<String>();
		
		JLabel lblFiledirectoryList = new JLabel("File/Directory List");
		lblFiledirectoryList.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblFiledirectoryList.setBounds(325, 11, 86, 20);
		frmFiledirectoryListTool.getContentPane().add(lblFiledirectoryList);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(247, 31, 239, 174);
		frmFiledirectoryListTool.getContentPane().add(scrollPane);
		JList<String> list = new JList<String>(listModel);
		scrollPane.setViewportView(list);
		
		rdbtnListFiles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (rdbtnListDirectories.isSelected())
					rdbtnListDirectories.setSelected(false);
				
				boolean shouldEnableFileArgs = rdbtnListFiles.isSelected() ? true : false;
				dir_FileTextfield.setEnabled(shouldEnableFileArgs);
				getFiles = shouldEnableFileArgs;
				if (getDirs)
					getDirs = false;
			}
		});
		
		rdbtnListDirectories.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (rdbtnListFiles.isSelected())
					rdbtnListFiles.setSelected(false);
				
				dir_FileTextfield.setEnabled(false);
				if (getFiles)
					getFiles = false;
				getDirs = true;
			}
		});

		btnGenerateList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				generateList();
			}
		});
	}
	
	private void generateList() {
		listModel.removeAllElements();
		String givenDir = dirTextfield.getText().trim();
		File directory = new File(givenDir);
		if (!directory.isDirectory()) {
			System.err.println("Given directory is not a directory. Please give a directory.");
			return;
		}
		
		if (getFiles) {
			String typeText = dir_FileTextfield.getText().trim();
			String[] types = null;
			if (!typeText.isEmpty()) {
				if (typeText.contains(",")) 
					types = typeText.split(",");
				else
					types = new String[] { typeText };
			}
			File[] files = directory.listFiles();
			for (File f : files) {
				if (!f.isDirectory()) {
					if (types != null && types.length > 0) {
						for (String type : types) {
							if (f.getName().endsWith(type)) {
								listModel.addElement(f.getName());
							}
						}
					} else {
						listModel.addElement(f.getName());
					}
				}
			}
		} else if (getDirs) {
			File[] files = directory.listFiles();
			for (File f : files) {
				if (f.isDirectory()) {
					listModel.addElement(f.getName());
				}
			}
		}
	}
}
