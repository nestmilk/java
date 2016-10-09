
import java.util.Random;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
/**
 * Description:
 * <br/>网站: <a href="http://www.crazyit.org">疯狂Java联盟</a>
 * <br/>Copyright (C), 2001-2016, Yeeku.H.Lee
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @author Yeeku.H.Lee kongyeeku@163.com
 * @version 1.0
 */
public class PinBall
{
	Random rand=new Random();
	private static PinBall pb=new PinBall();

	private Frame f = new Frame("弹球游戏");
	private Button b1=new Button("开始");
	private Button b2=new Button("结束");
	private Panel p=new Panel();

	// 桌面的宽度和高度
	private final int TABLE_WIDTH = 300;
	private final int TABLE_HEIGHT = 400;

	// 下面定义球拍的宽度和高度
	private final int RACKET_WIDTH = 60;
	private final int RACKET_HEIGHT = 20;
	// racketX代表球拍的水平和垂直位置
	private int racketX;
	private final int RACKET_Y = 380;
	
	// 小球的大小
	private final int BALL_SIZE = 16;
	// 返回一个-0.5~0.5的比率，用于控制小球的运行方向。
	private final double xyRate = rand.nextDouble() - 0.5;
	// 小球的运行速度
	private int ySpeed;//向下
	private int xSpeed;
	// ballX和ballY代表小球的坐标
	private int ballX;
	private int ballY;

	private MyCanvas tableArea = new MyCanvas();
	private ButtonListener bl=new ButtonListener(); 
	private KeyProcessor kp=new KeyProcessor();
	private TaskPerformer tp=new TaskPerformer();

	private Timer timer;
	
	// 游戏是否结束的旗标
	private boolean isLose;

	public void reSet()
	{
		// racketX代表球拍的水平和垂直位置
		racketX = rand.nextInt(240);
		// ballX和ballY代表小球的坐标
		ballX = rand.nextInt(200) + 20;
		ballY = rand.nextInt(10) + 20;
		//ySpeed和xSpeed代表小球的速度
		ySpeed = 10;//向下
		xSpeed = (int)(ySpeed * xyRate * 2);
		System.out.println(ballX+" "+ballY);
	}

	public void init()
	{
		System.out.println(ballX+" "+ballY);
		// 设置桌面区域的最佳大小
		tableArea.setPreferredSize(new Dimension(TABLE_WIDTH , TABLE_HEIGHT));
		
		f.add(tableArea);
		p.add(b1);
		p.add(b2);
		f.add(p,BorderLayout.SOUTH);

		b1.addActionListener(bl);
		b2.addActionListener(bl);

		b1.addKeyListener(kp);//b1必须被加上KeyListener

		
		f.pack();
		f.setVisible(true);

	}

	public void begin()
	{
		isLose=false;
		timer=new Timer(100, tp);
		timer.start();
	}

	


	public static void main(String[] args)
	{
		pb.init();
	}


	class MyCanvas extends Canvas
	{
		// 重写Canvas的paint方法，实现绘画
		public void paint(Graphics g)
		{
			// 如果游戏已经结束
			if (isLose)
			{
				g.setColor(new Color(255, 0, 0));
				g.setFont(new Font("Times" , Font.BOLD, 30));
				g.drawString("游戏已结束！" , 50 , 200);
			}
			// 如果游戏还未结束
			else
			{
				// 设置颜色，并绘制小球
				g.setColor(new Color(240, 240, 80));
				g.fillOval(ballX , ballY , BALL_SIZE, BALL_SIZE);
				// 设置颜色，并绘制球拍
				g.setColor(new Color(80, 80, 200));
				g.fillRect(racketX , RACKET_Y , RACKET_WIDTH , RACKET_HEIGHT);
			}
		}
	}

	class TaskPerformer implements ActionListener //监控球和拍子的相对位置，每隔0.1秒绘制tableArea
	{
		public void actionPerformed(ActionEvent	e)
		{
			// 如果小球碰到左边边框
			if (ballX  <= 0 || ballX >= TABLE_WIDTH - BALL_SIZE)
			{
				System.out.println("到左右边框");
				xSpeed = -xSpeed;
			}
			// 如果小球高度超出了球拍位置，且横向不在球拍范围之内，游戏结束。
			if (ballY >= RACKET_Y - BALL_SIZE && (ballX < racketX || ballX > racketX + RACKET_WIDTH))
			{
				timer.stop();
				// 设置游戏是否结束的旗标为true。
				isLose = true;
				tableArea.repaint();
			}
			// 如果小球位于球拍之内，且到达球拍位置，小球反弹
			else if (ballY  <= 0 || (ballY >= RACKET_Y - BALL_SIZE && ballX > racketX && ballX <= racketX + RACKET_WIDTH))
			{
				ySpeed = -ySpeed;
			}
			//小球坐标增加
			ballY += ySpeed;
			ballX += xSpeed;
			tableArea.repaint();
		}		
	}

	// 定义键盘监听器
	class KeyProcessor extends KeyAdapter
	{
		public void keyPressed(KeyEvent ke)
		{
		// 按下向左、向右键时，球拍水平坐标分别减少、增加
			if (ke.getKeyCode() == KeyEvent.VK_LEFT)
			{
				if (racketX > 0)
				racketX -= 10;
			}
			if (ke.getKeyCode() == KeyEvent.VK_RIGHT)
			{
				if (racketX < TABLE_WIDTH - RACKET_WIDTH)
				racketX += 10;
			}
		}

	}

	//定义按钮监听器
	class ButtonListener implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			String command=e.getActionCommand();
			if(command=="开始")
			{
				System.out.println("开始");
				pb.reSet();
				pb.begin();
			}
			
			if(command=="结束")
			{	
				System.out.println("结束");
				System.exit(0);
			}
		}
	}

}
