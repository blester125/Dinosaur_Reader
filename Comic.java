import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.imageio.ImageIO;

import java.net.*;
import java.io.*;
import javax.swing.*; 
import java.util.*;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.FlowLayout;
import javax.swing.JMenuBar;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;


public class Comic {

	private static JFrame frame;
	private static JLabel comicLabel;
	private static JTextArea titleText;
	private static String comicURL = "";
	private static Document page;
	private static int max;
	private static int current;

	private static JButton btnFirst;
	private static JButton btnPrevious;
	private static JButton btnRandom;
	private static JButton btnNext;
	private static JButton btnLast;
	private static JButton btnSave;

	private static Random rand;

	public static void main(String args[]) throws Exception {
		page = get("http://www.qwantz.com/index.php");
		max = parseMax(page);
		current = max;
		rand = new Random();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Comic window = new Comic();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Comic() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Dinosaur Comics =)");
		frame.setResizable(false);
		frame.setBounds(100, 100, 737, 628);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel comicPanel = new JPanel();
		comicPanel.setBounds(0,0,735,505);
		frame.getContentPane().add(comicPanel);
		comicPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		comicLabel = new JLabel();
		comicPanel.add(comicLabel);

		JPanel titlePanel = new JPanel();
		titlePanel.setBounds(0, 505, 735, 70);
		frame.getContentPane().add(titlePanel);
		titleText = new JTextArea();
		titleText.setEditable(false);
		titleText.setLineWrap(true);
		titleText.setBackground(UIManager.getColor("Panel.background"));
		titleText.setBounds(2, 2, 733, 68);
		titlePanel.add(titleText);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(0, 570, 735, 30);
		frame.getContentPane().add(buttonPanel);
		
		btnFirst = new JButton("First");
		btnFirst.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					firstComic();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		buttonPanel.setLayout(new GridLayout(0, 6, 0, 0));
		buttonPanel.add(btnFirst);
		
		btnPrevious = new JButton("Previous");
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					prevComic();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		buttonPanel.add(btnPrevious);
		
		btnRandom = new JButton("Random");
		btnRandom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					randomComic();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		buttonPanel.add(btnRandom);
		
		btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					nextComic();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		buttonPanel.add(btnNext);
		
		btnLast = new JButton("Last");
		btnLast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					lastComic();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		buttonPanel.add(btnLast);
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					saveComic();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		buttonPanel.add(btnSave);

		try {
			setComic(current);
			btnLast.setEnabled(false);
			btnNext.setEnabled(false);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	public static Document get(String url) throws Exception {
		Document doc = Jsoup.connect(url).get();
		return doc;
	}

	public static int parseMax(Document html) {
		Element meta = html.select("meta[property=og:url]").first();
		String url = meta.attr("content");
		String number = url.substring(url.indexOf("=")+1, url.length());
		return Integer.parseInt(number);
	}

	public static String parseTitle(Document html) {
		Element ele = html.select("img.comic").first();
		String title = ele.attr("title");
		return title;
	}

	public static String parseImage(Document html) {
		Elements eles = html.select("img.comic");
		Element comic = eles.first();
		String url = comic.attr("src");
		return url;
	}

	public static void saveComic() throws Exception {
		URL url = new URL(comicURL);
		InputStream in = url.openStream();
		OutputStream out = new BufferedOutputStream(new FileOutputStream("comic-" + current + ".png"));
		for (int b; (b = in.read()) != -1;) {
		 	out.write(b);
		}
		out.close();
		in.close();
	}

	public static void displayImage(String src) throws Exception {
		URL url = new URL(src);
		BufferedImage image = ImageIO.read(url);
		comicLabel.setIcon(new ImageIcon(image));
	}

	public static void updateTitle(String title) {
		titleText.setText(title);
	}

	public static void setComic(int num) throws Exception {
		page = get("http://www.qwantz.com/index.php?comic="+num);
		String title = parseTitle(page);
		comicURL = parseImage(page);
		updateTitle(title);
		displayImage(comicURL);
	}

	public static void firstComic() throws Exception {
		current = 1;
		System.out.println(current);
		setComic(current);
		setButtons(current);
	}

	public static void prevComic() throws Exception {
		current = current - 1;
		System.out.println(current);
		setComic(current);
		setButtons(current);
	}

	public static void randomComic() throws Exception {
		current = rand.nextInt(max) + 1;
		System.out.println(current);
		setComic(current);
		setButtons(current);
	}

	public static void nextComic() throws Exception {
		current = current + 1;
		System.out.println(current);
		setComic(current);
		setButtons(current);
	}

	public static void lastComic() throws Exception {
		current = max;
		System.out.println(current);
		setComic(current);
		setButtons(current);
	}

	public static void setButtons(int current) {
		btnFirst.setEnabled(true);
		btnPrevious.setEnabled(true);
		btnNext.setEnabled(true);
		btnLast.setEnabled(true);
		if (current <= 1) {
			btnFirst.setEnabled(false);
			btnPrevious.setEnabled(false);
		}
		if (current >= max) {
			btnNext.setEnabled(false);
			btnLast.setEnabled(false);
		}
	}
}