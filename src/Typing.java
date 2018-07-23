import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;

import javax.swing.*;

public class Typing {

	private JFrame frame = new JFrame("Typing");

	private JSplitPane splitPane = new JSplitPane();

	private JButton finishBtn = new JButton("结束");

	private JButton resultsBtn = new JButton("结果");

	private JButton exitBtn = new JButton("退出");

	private JTextArea textArea = new JTextArea(5, 10);

	private JScrollPane scrollPane = new JScrollPane(textArea);

	private Date startTime;

	private Date endTime;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Typing();
			}
		});
	}

	private Typing() {

		splitPaneComponent();
		textAreaAction();
		finishBtnAction();
		resultsBtnAction();
		exitBtnAction();

		frame.setUndecorated(true);//去处边框
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); //最大化
		frame.setResizable(false); //不能改变大小
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(null);
		frame.add(splitPane);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private void splitPaneComponent() {

		Font textFont = new Font("宋体", Font.PLAIN, 20);
		textArea.setFont(textFont);
		textArea.setLineWrap(true);

		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setLeftComponent(scrollPane);
		splitPane.setRightComponent(finishBtn);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();// 获取屏幕的边界
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());// 获取底部任务栏高度
		int monitorFrameHeight = screenSize.height - screenInsets.top - screenInsets.bottom;

		String osName = System.getProperty("os.name");
		splitPane.setDividerSize(2);
		if (osName.contains("Mac")) {
			splitPane.setSize(screenSize.width, monitorFrameHeight);
			splitPane.setDividerLocation(monitorFrameHeight - 50);
		} else if (osName.contains("Windows")) {
			splitPane.setSize(screenSize.width, screenSize.height);
			splitPane.setDividerLocation(screenSize.height - 50);
		}
	}

	private void textAreaAction() {
		textArea.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { //敲击键盘，发生在按键按下后，按键放开前
				if (textArea.getText().equals("")) {
					startTime = new Date();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) { // 按下按键

			}

			@Override
			public void keyReleased(KeyEvent e) {  //松开按键

			}
		});
	}

	private void finishBtnAction() {
		finishBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!textArea.getText().equals("")) {
					finishBtnChange();
				} else {
					JOptionPane.showMessageDialog(null, "请输入文字", "提示", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	private void finishBtnChange() {
		endTime = new Date();

		textArea.setEnabled(false);

		JSplitPane bottomSplitPane = new JSplitPane();
		bottomSplitPane.setLeftComponent(resultsBtn);
		bottomSplitPane.setRightComponent(exitBtn);
		bottomSplitPane.setDividerSize(2);
		bottomSplitPane.setDividerLocation(splitPane.getWidth() / 2);

		splitPane.setRightComponent(bottomSplitPane);
		splitPane.setDividerLocation(splitPane.getHeight() - 50);
		showMsg();
	}

	private void showMsg() {
		long time = endTime.getTime() - startTime.getTime();
		long minutes = (time % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (time % (1000 * 60)) / 1000;
		JOptionPane.showMessageDialog(null,
				"用时: " + minutes + " 分 " + seconds + " 秒 \n" + "相似度: " + result(),
				"结果", JOptionPane.INFORMATION_MESSAGE);
	}

	private float result() {
		String userArticle = textArea.getText();
		String contrastArticle =
				"  Once a circle missed a wedge. The circle wanted to be whole, so it went around looking for its missing piece. But because it was incomplete and therefore could roll only very slowly, it admired the flowers along the way. It chatted with worms. It enjoyed the sunshine. It found lots of different pieces, but none of them fit. So it left them all by the side of the road and kept on searching. Then one day the circle found a piece that fit perfectly. It was so happy. Now it could be whole, with nothing missing. It incorporated the missing piece into itself and began to roll. Now that it was a perfect circle, it could roll very fast, too fast to notice flowers or talk to the worms. When it realized how different the world seemed when it rolled so quickly, it stopped, left its found piece by the side of the road and rolled slowly away.\n" +
						"  The lesson of the story, I suggested, was that in some strange sense we are more whole when we are missing something. The man who has everything is in some ways a poor man. He will never know what it feels like to yearn, to hope, to nourish his soul with the dream of something better. He will never know the experience of having someone who loves him give him something he has always wanted or never had.\n" +
						"  There is a wholeness about the person who has come to terms with his limitations, who has been brave enough to let go of his unrealistic dreams and not feel like a failure for doing so. There is a wholeness about the man or woman who has learned that he or she is strong enough to go through a tragedy and survive, she can lose someone and still feel like a complete person.\n" +
						"  Life is not a trap set for us by God so that he can condemn us for failing. Life is not a spelling bee, where no matter how many words you’ve gotten right, you’re disqualified if you make one mistake. Life is more like a baseball season, where even the best team loses one third of its games and even the worst team has its days of brilliance. Our goal is to win more games than we lose. When we accept that imperfection is part of being human, and when we can continue rolling through life and appreciate it, we will have achieved a wholeness that others can only aspire to. That, I believe, is what God asks of us --- not “Be perfect”, not “Don’t even make a mistake”, but “Be whole”.\n" +
						"  If we are brave enough to love, strong enough to forgive, generous enough to rejoice in another’s happiness, and wise enough to know there is enough love to go around for us all, then we can achieve a fulfillment that no other living creature will ever know.\n";
		EditDistanceCalculator editDistanceCalculator = new EditDistanceCalculator();
		return editDistanceCalculator.correct(contrastArticle, userArticle);
	}

	private void resultsBtnAction() {
		resultsBtn.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showMsg();
			}
		});
	}

	private void exitBtnAction () {
		exitBtn.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,"程序即将退出...");
				System.exit(0);
			}
		});
	}
}
