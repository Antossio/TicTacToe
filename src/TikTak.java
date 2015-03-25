import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

public class TikTak extends JFrame implements ActionListener, MouseListener {
    Button[] buts;
    Container cnt;
    int n = 3;
    char[][] m = new char [n][n];
    int xPC;
    int yPC;
    int count;
    int rez;
    JButton restart;
    JButton restartPCTurn;
    static int userWin;
    static int pcWin;
    JLabel score;

    public TikTak (int x, int y, boolean isPCturn) {
	setDefaultCloseOperation(EXIT_ON_CLOSE);//при нажатии на х программа закрывается и удаляется из памяти
	setBounds(x, y, 150, 250);//задаются место положение и размер
	setVisible(true);//видимость окна
	setTitle("TicTac");
	cnt = getContentPane();//создание контейнера окна для получения доступа к свойствам
	cnt.setLayout(null);
	restart = new JButton("Start (Your turn)");
	restartPCTurn = new JButton("Start (PC turn)");
	restart.setBounds(5, 155, 125, 20);
	restartPCTurn.setBounds(5, 180, 125, 20);
	cnt.add(restart);
	cnt.add(restartPCTurn);
	restart.addActionListener(this);
	restartPCTurn.addActionListener(this);
	score = new JLabel("You  " + userWin + ":" + pcWin + "  PC");
	score.setBounds(5, 135, 100, 20);
	cnt.add(score);
	init(isPCturn);
    }

    public static void main(String[] args) {
	new TikTak(500, 200, false);
    }

    public void init(boolean isPCTurn) {
	buts = new Button[3 * 3];
	int i = 0;
	for (int j = 7; j <= 87; j = j + 40) { //расстановка кнопок
	    for (int k = 7;k <= 87; k = k + 40) {
		buts[i] = new Button(k, j, 40, 40);
		cnt.add(buts[i].but);
		cnt.add(buts[i].lab);
		repaint();
		buts[i].index = i;
		buts[i].but.addMouseListener(this);
		buts[i].lab.addMouseListener(this);
		i++;
	    }
	}
	if (isPCTurn) {
	    pcTurn();
	}
    }

    @Override
    public void mouseClicked(MouseEvent e) {
	if (count == n * n) {
	    dialogBox("Stand-off.");
	}
	if (e.getButton() == 1) {
	    Object src = e.getSource();
	    for (int i = 0; i < buts.length; i++){
		if (src == buts[i].but && buts[i].open != true) {
		    buts[i].but.setVisible(false);
		    buts[i].lab.setText("X");
		    buts[i].open = true;
		    int z = buts[i].index;
		    m[z / 3][z % 3] = 'X';
		    if (win(m,n)) {
			userWin++;
			score = new JLabel("You  " + userWin + ":" + pcWin + "  PC");
			dialogBox("You win.");
		    }
		    count++;
		    pcTurn();
		}
	    }
	}
    }

    public void pcTurn() {
	if (count == n * n) {
	    dialogBox("Stand-off.");
	}
	int[] xyAttack = attack(m, n);////Вызов проверки на 2 в ряд компьютера
	int[] xyDefend = defender(m, n);//Вызов проверки на 2 в ряд пользователя
	if (count != n * n && !win(m, n)) {
	    do {
		if (xyAttack[0] != 5) {
		    xPC = xyAttack[0];
		    yPC = xyAttack[1];
		}
		else {
		    if (xyDefend[0] != 5) {
			xPC = xyDefend[0];
			yPC = xyDefend[1];
		    }
		    else {
			xPC = (int)(Math.random() * n);
			yPC = (int)(Math.random() * n);
		    }
		}
	    } while (m[xPC][yPC] == 'X' || m[xPC][yPC] == 'O');
	    m[xPC][yPC] = 'O';
	    buts[xPC * 3 + yPC].but.setVisible(false);
	    buts[xPC * 3 + yPC].lab.setText("O");
	    if (win(m,n)) {
		pcWin++;
		score = new JLabel("You  " + userWin + ":" + pcWin + "  PC");
		dialogBox("You loose.");
	    }
	    buts[xPC * 3 + yPC].open = true;
	    count++;
	    if (count == n * n) {
		dialogBox("Stand-off.");
	    }
	}
    }

    static int[] defender (char[][] z, int lngth) {
	char []userWin = {'X', 'X', 0};
	char []userWin1 = {'X', 0, 'X'};
	char []userWin2 = {0, 'X', 'X'};
	return strategy(z, userWin, userWin1, userWin2);
    }

    static int[] attack (char[][] z, int lngth) {
	char []PCWin = {'O', 'O', 0};
	char []PCWin1 = {'O', 0, 'O'};
	char []PCWin2 = {0, 'O', 'O'};
	return strategy(z, PCWin, PCWin1, PCWin2);
    }

    static int[] strategy(char [][]m1, char []cond1, char []cond2, char []cond3) {
	int[] xy = {5,5};
	char []column=new char [m1.length];
	for (int i=0; i<m1.length; i++) { //Проверка строк
	    if (Arrays.equals(m1[i], cond1)) {
		xy[1] = 2;
		xy[0] = i;
	    }
	    else {
		if (Arrays.equals(m1[i], cond2)) {
		    xy[1] = 1;
		    xy[0] = i;
		}
		else {
		    if (Arrays.equals(m1[i], cond3))
		    {
			xy[1] = 0;
			xy[0] = i;
		    }
		}
	    }
	}
	for (int i = 0; i < m1.length; i++) { //Проверка столбцов
	    for (int j = 0;j < m1.length; j++) {
		column[j] = m1[j][i];
	    }
	    if (Arrays.equals(column, cond1)) {
		xy[1] = i;
		xy[0] = 2;
	    }
	    else {
		if (Arrays.equals(column, cond2)) {
		    xy[1] = i;
		    xy[0] = 1;
		}
		else {
		    if (Arrays.equals(column, cond3)) {
			xy[1] = i;
			xy[0] = 0;
		    }
		}
	    }
	}
	for (int j = 0; j < m1.length; j++) { //Проверка главной диагонали
	    column[j] = m1[j][j];
	}
	if (Arrays.equals(column, cond1)) {
	    xy[1] = 2;
	    xy[0] = 2;
	}
	else {
	    if (Arrays.equals(column, cond2)) {
		xy[1] = 1;
		xy[0] = 1;
	    }
	    else {
		if (Arrays.equals(column, cond3)) {
		    xy[1] = 0;
		    xy[0] = 0;
		}
	    }
	}
	for (int j = 0, i = m1.length - 1; j < m1.length; j++, i--) { // Проверка доп. диагонали
	    column[j] = m1[i][j];
	}
	if (Arrays.equals(column, cond1)) {
	    xy[1] = 2;
	    xy[0] = 0;
	}
	else {
	    if (Arrays.equals(column, cond2)) {
		xy[1] = 1;
		xy[0] = 1;
	    }
	    else {
		if (Arrays.equals(column, cond3))
		{
		    xy[1] = 0;
		    xy[0] = 2;
		}
	    }
	}
	return xy;
    }

    public void dialogBox(String s) {  //Диалоговое окно для новой игры 
	int pane = JOptionPane.showConfirmDialog(null, s + " New game?");
	if (pane == 0) {
	    new TikTak(this.getX(), this.getY(), false);
	    this.dispose();
	}
	else if(pane == 1) {
	    this.dispose();
	}
    }

    static boolean win (char[][] z, int lngth) {
	char[] userWin = new char[lngth];
	Arrays.fill(userWin, 'X');
	char []PCWin = new char[lngth];
	Arrays.fill(PCWin, 'O');
	char []column = new char [lngth];
	for (int i = 0; i < z.length; i++) { //Проверка строк
	    if (Arrays.equals(z[i], userWin) || Arrays.equals(z[i], PCWin)) {
		return true;
	    }
	}
	for (int i = 0; i < z.length; i++) { //Проверка столбцов
	    for (int j = 0;j < z.length; j++) {
		column[j] = z[j][i];
	    }
	    if (Arrays.equals(column, userWin) || Arrays.equals(column, PCWin)) {
		return true;
	    }
	}
	for (int j = 0;j < z.length; j++) { //Проверка главной диагонали
	    column[j] = z[j][j];
	}
	if (Arrays.equals(column, userWin) || Arrays.equals(column, PCWin)) {
	    return true;
	}
	for (int j = 0, i = lngth - 1; j < z.length; j++, i--) { // Проверка доп. диагонали
	    column[j] = z[i][j];
	}
	if (Arrays.equals(column, userWin) || Arrays.equals(column, PCWin)) {
	    return true;
	}
	return false;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
	Object src = ae.getSource();
	if (src == restart) {
	    new TikTak(this.getX(), this.getY(), false);
	    this.dispose();
	}
	if (src==restartPCTurn) {
	    new TikTak(this.getX(), this.getY(), true);
	    this.dispose();
	}
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }
}