package application;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class MinesController {
	private int width = 0, height = 0, mines = 0;
	private HBox hbox;
	private Stage primaryStage;
	private Mines board;
	private ButtonMines[][] buttonBoard;

	@FXML
	private TextField TextFieldWidth;

	@FXML
	private TextField TextFieldHeight;

	@FXML
	private TextField TextFieldMines;

	@FXML
	void PressReset(ActionEvent event) {
		boolean flag = true;
		GridPane newGridPane = new GridPane();
		try {
			//getting textFields
			width = Integer.valueOf(TextFieldWidth.getText());
			height = Integer.valueOf(TextFieldHeight.getText());
			mines = Integer.valueOf(TextFieldMines.getText());
		} catch (Exception e) {
			flag = false;
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Sorry...");
			alert.setHeaderText("What abbout Numbers?");
			alert.setContentText("On the TextFields you have to enter numbers.");
			alert.showAndWait();
		}

		if (flag) {
			board = new Mines(height, width, mines);
			buttonBoard = new ButtonMines[height][width];

			List<ColumnConstraints> lstCol = new ArrayList<ColumnConstraints>();
			List<RowConstraints> lstRow = new ArrayList<RowConstraints>();
			
			//Setting the size of the buttons.
			for (int i = 0; i < width; i++)
				lstCol.add(new ColumnConstraints(35));
			for (int i = 0; i < height; i++)
				lstRow.add(new RowConstraints(35));

			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {

					ButtonMines b = new ButtonMines(i, j);
					buttonBoard[i][j] = b;
					b.setText(board.get(i, j));
					//Button Left Click.
					b.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							boolean result = board.open(((ButtonMines) event.getSource()).getX(),
									((ButtonMines) event.getSource()).getY());
							updateButtons();
							if (!result) {
								//openAll();
								board.setShowAll(true);
								updateButtons();
								Alert alert = new Alert(AlertType.ERROR);
								alert.setTitle("Sorry...");
								alert.setHeaderText("What Happend?");
								alert.setContentText("You Lost!");
								alert.showAndWait();
							}
							if (board.isDone()) {
								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setTitle("You are the Winner!!!");
								alert.setHeaderText(null);
								alert.setContentText("Hurray you Woooonnnn!!!!");

								alert.showAndWait();
							}
						}
					});
					b.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {
							if (event.getButton() == MouseButton.SECONDARY) {
								int x = ((ButtonMines) event.getSource()).getX();
								int y = ((ButtonMines) event.getSource()).getY();
								board.toggleFlag(x, y);
								buttonBoard[x][y].setText(board.get(x, y));
							}
						}
					});
					b.setMaxWidth(Double.MAX_VALUE);
					b.setMaxHeight(Double.MAX_VALUE);
					newGridPane.add(b, j, i);
				}
			}
			newGridPane.getColumnConstraints().addAll(lstCol);
			newGridPane.getRowConstraints().addAll(lstRow);

			hbox.getChildren().remove(hbox.getChildren().size() - 1);
			hbox.getChildren().add(newGridPane);

			hbox.autosize();
			primaryStage.sizeToScene();
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setHBox(HBox hbox) {
		this.hbox = hbox;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	/*private void openAll() {
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				board.open(i, j);
	}*/

	private void updateButtons() {
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				buttonBoard[i][j].setText(board.get(i, j));

	}
}
