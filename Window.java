import java.awt.*;
import javax.swing.*;

public class Window {
	JButton [] buttons = new JButton[18];
	JTextArea txtArea = new JTextArea(2, 25);
	private JTextArea textArea;
	public Window() {
		String [][] btn_Txt = //All calculator numbers/operations
			{{"7","8","9"},
			{"4","5","6"},
			{"1","2","3"},
			{"0","+","-"},
			{"=","x","/"},
			{"(",")","C"}};
		
		
		JPanel panelButtons = new JPanel();
		JPanel panelMain = new JPanel(new BorderLayout(5,5)); 
		
		panelButtons.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		panelButtons.setLayout(new GridLayout(6,3,5,5));
		
		Font font = new Font(Font.SANS_SERIF, Font.BOLD, 100);
		Font txtFont = new Font(Font.SANS_SERIF, Font.BOLD, 55);
		
		//Text Field Area
		txtArea.setEditable(false);
		txtArea.setFont(txtFont);
		panelMain.add(txtArea, BorderLayout.NORTH);
		
		int count = 0;
		//Add buttons to panel
		for (int i = 0; i < 6; i ++) {
			for(int k = 0; k < 3; k++) {
				JButton button = new JButton(btn_Txt[i][k]);
				buttons[count] = button;
				count++;
				button.setFont(font);
				panelButtons.add(button);
			}
		}
		
		panelMain.add(panelButtons, BorderLayout.CENTER);
		
		JFrame frame = new JFrame("Calculator");
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panelMain);
		frame.pack();
		frame.setVisible(true);
		
	}
	
	public JButton[] getButtons() {
		return buttons;
	}
}
