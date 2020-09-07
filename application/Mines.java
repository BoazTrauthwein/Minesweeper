package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mines {
	private int height, width, numMines;
	private Field[][] board;
	private List<Point> visited = new ArrayList<Point>();
	private boolean showAll = false;

	public Mines(int height, int width, int numMines) {// We need to insert mines
		Random r = new Random();
		int count = 0;
		this.height = height;
		this.width = width;
		this.numMines = numMines;
		board = new Field[height][width];
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				board[i][j] = new Field();
		int i, j;
		// Inserting Mines.
		while (count < numMines) {
			i = r.nextInt(height);
			j = r.nextInt(width);
			while (board[i][j].isMine() == true) {
				i = r.nextInt(height);
				j = r.nextInt(width);
			}
			board[i][j].setMine(true);
			count++;
			UpdateNumberNeighbourMine(i, j);
			// Update Numbers surrounding mines.
			/*if (i - 1 >= 0 && j - 1 >= 0 && !board[i - 1][j - 1].isMine())
				board[i - 1][j - 1].setNumMines(board[i - 1][j - 1].getNumMines() + 1);
			if (i - 1 >= 0 && !board[i - 1][j].isMine())
				board[i - 1][j].setNumMines(board[i - 1][j].getNumMines() + 1);
			if (i - 1 >= 0 && j + 1 < width && !board[i - 1][j + 1].isMine())
				board[i - 1][j + 1].setNumMines(board[i - 1][j + 1].getNumMines() + 1);

			if (j - 1 >= 0 && !board[i][j - 1].isMine())
				board[i][j - 1].setNumMines(board[i][j - 1].getNumMines() + 1);
			if (j + 1 < width && !board[i][j + 1].isMine())
				board[i][j + 1].setNumMines(board[i][j + 1].getNumMines() + 1);

			if (i + 1 < height && j - 1 >= 0 && !board[i + 1][j - 1].isMine())
				board[i + 1][j - 1].setNumMines(board[i + 1][j - 1].getNumMines() + 1);
			if (i + 1 < height && !board[i + 1][j].isMine())
				board[i + 1][j].setNumMines(board[i + 1][j].getNumMines() + 1);
			if (i + 1 < height && j + 1 < width && !board[i + 1][j + 1].isMine())
				board[i + 1][j + 1].setNumMines(board[i + 1][j + 1].getNumMines() + 1);*/
		}
	}

	public boolean addMine(int i, int j) {
		if (i >= height || j >= width)
			throw new IndexOutOfBoundsException();
		board[i][j].setMine(true);
		numMines++;
		UpdateNumberNeighbourMine(i, j);
		return true;
	}

	public boolean open(int i, int j) {
		if (board[i][j].isOpen() == true)
			return true;
		if (board[i][j].isMine()) {
			board[i][j].setOpen(true);
			return false;
		}
		visited.add(new Point(i, j));
		if (!board[i][j].isMine() && board[i][j].getNumMines() != 0) {
			board[i][j].setOpen(true);
			return true;
		}

		if (board[i][j].getNumMines() == 0)
			board[i][j].setOpen(true);

		// Opens all other neighbours who dont have mines.
		if (i - 1 >= 0 && j - 1 >= 0 && !board[i - 1][j - 1].isMine())
			if (!visited.contains(new Point(i - 1, j - 1)))
				open(i - 1, j - 1);
		if (i - 1 >= 0 && !board[i - 1][j].isMine())
			if (!visited.contains(new Point(i - 1, j)))
				open(i - 1, j);
		if (i - 1 >= 0 && j + 1 < width && !board[i - 1][j + 1].isMine())
			if (!visited.contains(new Point(i - 1, j + 1)))
				open(i - 1, j + 1);

		if (j - 1 >= 0 && !board[i][j - 1].isMine())
			if (!visited.contains(new Point(i, j - 1)))
				open(i, j - 1);
		if (j + 1 < width && !board[i][j + 1].isMine())
			if (!visited.contains(new Point(i, j + 1)))
				open(i, j + 1);

		if (i + 1 < height && j - 1 >= 0 && !board[i + 1][j - 1].isMine())
			if (!visited.contains(new Point(i + 1, j - 1)))
				open(i + 1, j - 1);
		if (i + 1 < height && !board[i + 1][j].isMine())
			if (!visited.contains(new Point(i + 1, j)))
				open(i + 1, j);
		if (i + 1 < height && j + 1 < width && !board[i + 1][j + 1].isMine())
			if (!visited.contains(new Point(i + 1, j + 1)))
				open(i + 1, j + 1);

		return true;
	}

	public void toggleFlag(int x, int y) {
		if (x >= height || y >= width)
			throw new IndexOutOfBoundsException();
		if (board[x][y].isOpen() == false && !board[x][y].isFlag()) {
			board[x][y].setFlag(true);
			return;
		}
		if (board[x][y].isFlag()) {
			board[x][y].setFlag(false);
			return;
		}
	}

	public boolean isDone() {
		int countClosedMines = 0, openFields = 0;
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++) {
				if ((!board[i][j].isOpen()) && board[i][j].isMine())
					countClosedMines++;
				if (board[i][j].isOpen())
					openFields++;
			}
		if (countClosedMines == numMines && openFields == (height * width - numMines))
			return true;
		return false;
	}

	public String get(int i, int j) {
		if (board[i][j].isFlag()&&!showAll)
			return "F";
		if (!board[i][j].isOpen()&&!showAll)
			return ".";
		if(!showAll&&board[i][j].isMine())
			return ".";
		/*if (board[i][j].isOpen()&&showAll&&board[i][j].isMine())
			return "X";*/
		
		if (board[i][j].isOpen()||showAll)
			if (board[i][j].isMine())
				return "X";
		if ((board[i][j].isOpen()||showAll) && board[i][j].getNumMines() != 0)
			return String.valueOf(board[i][j].getNumMines());
		return " ";

	}

	public void setShowAll(boolean showAll) {
		this.showAll=showAll;
	}

	public String toString() {
		String str = "";
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++)
				str += get(i, j);
			str += "\n";
		}
		return str;
	}

	private void UpdateNumberNeighbourMine(int i, int j) {
		// Update Numbers surrounding mines.
		if (i - 1 >= 0 && j - 1 >= 0 && !board[i - 1][j - 1].isMine())
			board[i - 1][j - 1].setNumMines(board[i - 1][j - 1].getNumMines() + 1);
		if (i - 1 >= 0 && !board[i - 1][j].isMine())
			board[i - 1][j].setNumMines(board[i - 1][j].getNumMines() + 1);
		if (i - 1 >= 0 && j + 1 < width && !board[i - 1][j + 1].isMine())
			board[i - 1][j + 1].setNumMines(board[i - 1][j + 1].getNumMines() + 1);

		if (j - 1 >= 0 && !board[i][j - 1].isMine())
			board[i][j - 1].setNumMines(board[i][j - 1].getNumMines() + 1);
		if (j + 1 < width && !board[i][j + 1].isMine())
			board[i][j + 1].setNumMines(board[i][j + 1].getNumMines() + 1);

		if (i + 1 < height && j - 1 >= 0 && !board[i + 1][j - 1].isMine())
			board[i + 1][j - 1].setNumMines(board[i + 1][j - 1].getNumMines() + 1);
		if (i + 1 < height && !board[i + 1][j].isMine())
			board[i + 1][j].setNumMines(board[i + 1][j].getNumMines() + 1);
		if (i + 1 < height && j + 1 < width && !board[i + 1][j + 1].isMine())
			board[i + 1][j + 1].setNumMines(board[i + 1][j + 1].getNumMines() + 1);	
	}
}
