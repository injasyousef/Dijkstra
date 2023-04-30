package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

public class Main extends Application {

	static ArrayList<Country> Countrys;
	static Country sourceCountry = null;
	static Country destinationCountry = null;
	Pane root = new Pane();
	ComboBox<Label> source = new ComboBox<Label>();
	ComboBox<Label> dest = new ComboBox<Label>();
	static float mapHieght=715;
	static float mapWidth=1200;

	@Override
	public void start(Stage stage) throws FileNotFoundException {
		Stage primaryStage = new Stage();

		Scene scene = new Scene(root, 1580, 715);
		primaryStage.setTitle("the main menu");
		root.setStyle("-fx-background-color: 			#90ee90	;\r\n");
		initialize();
		Label names[] = new Label[Countrys.size()];
		Label s = new Label("Sorce:");
		s.setFont(new Font(30));
		s.setTextFill(Color.BLACK);
		Label d = new Label("Destination:");
		d.setFont(new Font(30));
		d.setTextFill(Color.BLACK);
		source.setStyle("        -fx-background-radius:100;\r\n");
		dest.setStyle("        -fx-background-radius:100;\r\n");
		for (int i = 0, j = 0; i < names.length; i++, j++) {
			names[i] = new Label();
			names[i].setFont(new Font(20));
			names[i].setTextFill(Color.BLACK);
			names[i].setText(Countrys.get(i).name);
			source.getItems().add(names[i]);
			names[j] = new Label();
			names[j].setFont(new Font(20));
			names[j].setTextFill(Color.BLACK);
			names[j].setText(Countrys.get(j).name);
			dest.getItems().add(names[j]);
		}
		source.setTranslateX(1380);
		source.setTranslateY(50);
		source.setPrefSize(180, 50);
		dest.setTranslateX(1380);
		dest.setTranslateY(150);
		dest.setPrefSize(180, 50);
		s.setTranslateX(1210);
		s.setTranslateY(50);
		d.setTranslateX(1210);
		d.setTranslateY(150);

		source.setOnAction(e -> {
			sourceCountry=Dijkstra.allNodes.get(source.getValue().getText());
			if(sourceCountry!=null) {
			sourceCountry.getTest().setStyle("-fx-background-color: #FF0000;\r\n" + "        -fx-background-radius:100;\r\n");
			}
		});
		dest.setOnAction(i->{
			destinationCountry=Dijkstra.allNodes.get(dest.getValue().getText());
			if(destinationCountry!=null) {
			destinationCountry.getTest().setStyle("-fx-background-color: #FF0000;\r\n" + "        -fx-background-radius:100;\r\n");
			}
		});

		Button run = new Button("Run");
		run.setFont(new Font(30));
		run.setTranslateX(1290);
		run.setTranslateY(220);
		run.setMinWidth(200);
		run.setMinHeight(80);
		run.setMaxWidth(200);
		run.setMaxHeight(80);
		run.setAlignment(Pos.CENTER);
		run.setStyle("-fx-background-color: #b197c3;\r\n" + "        -fx-background-radius:100;\r\n");

		TextArea path = new TextArea();
		path.setTranslateX(1300);
		path.setTranslateY(320);
		path.setMinSize(270, 220);
		path.setMaxSize(270, 220);
		path.setEditable(false);
		
		Label p=new Label("Path:");
		p.setFont(new Font(30));
		p.setTranslateX(1210);
		p.setTranslateY(320);

		TextField t1 = new TextField();
		t1.setTranslateX(1350);
		t1.setTranslateY(570);
		t1.setPrefSize(220, 50);
		t1.setStyle("        -fx-background-radius:100;\r\n");
		t1.setEditable(false);
		t1.setFont(new Font(20));
		
		Label t=new Label("Total cost:");
		t.setFont(new Font(30));
		t.setTranslateX(1210);
		t.setTranslateY(570);

		run.setOnAction(e -> {
			int v=0,w=0;
			for(int i=0;i<Countrys.size();i++) {
				if(sourceCountry.getFullName().equals(Countrys.get(i).getFullName()))
					v=i;
				if(destinationCountry.getFullName().equals(Countrys.get(i).getFullName()))
					w=i;
			}
			if (sourceCountry != null && destinationCountry != null) {
				Dijkstra graph = new Dijkstra(Countrys, Countrys.get(v), Countrys.get(w));
				graph.generateDijkstra();
				drawPathOnMap(graph.pathTo(Countrys.get(w)));
				root.getChildren().add(group);
				path.setText(graph.getPathString());
				t1.setText(graph.distanceString);
			}
		});
		
		Button reset=new Button("Reset");
		reset.setPrefSize(150, 60);
		reset.setAlignment(Pos.CENTER);
		reset.setTranslateX(1330);
		reset.setTranslateY(640);
		reset.setStyle("-fx-background-color: #b197c3;\r\n" + "        -fx-background-radius:100;\r\n");
		reset.setFont(new Font(30));
		
		reset.setOnAction(action ->{
			sourceCountry.getTest().setStyle("-fx-background-color: #000000;\r\n" + "        -fx-background-radius:100;\r\n");
			destinationCountry.getTest().setStyle("-fx-background-color: #000000;\r\n" + "        -fx-background-radius:100;\r\n");
			sourceCountry=new Country();
			destinationCountry=new Country();
			group.getChildren().clear();
			root.getChildren().remove(group);
			source.setValue(new Label(""));
			dest.setValue(new Label(""));
			path.setText(null);
			t1.setText(null);
		});

		root.getChildren().addAll(source, dest, run, path, t1, s, d,reset,p,t);
		primaryStage.setScene(scene);// set the scene
		primaryStage.show();
	}

	public void initialize() {
		Image image1 = new Image("map.png");
		ImageView imageView1 = new ImageView(image1);
		imageView1.setFitHeight(mapHieght);
		imageView1.setFitWidth(mapWidth);
		imageView1.setVisible(true);
		root.getChildren().add(imageView1);
		for (int i = 0; i < Countrys.size(); i++) {

			Button b = new Button();
			Countrys.get(i).setTest(b);
			b.setUserData(Countrys.get(i));
			b.setTranslateX(getX(Countrys.get(i).x));
			b.setTranslateY(getY(Countrys.get(i).y));

			b.setMinWidth(10);
			b.setMinHeight(10);
			b.setMaxWidth(10);
			b.setMaxHeight(10);
			// set the button color and radius using css
			b.setStyle("-fx-background-color: #000000;\r\n" + "        -fx-background-radius:100;\r\n");
			b.setOnAction(event -> {
				b.setStyle("-fx-background-color: #FF0000;\r\n" + "        -fx-background-radius:100;\r\n");
				if (sourceCountry == null) {
					sourceCountry = (Country) b.getUserData();
					Label l = new Label();
					l.setFont(new Font(20));
					l.setTextFill(Color.BLACK);
					l.setText(sourceCountry.name);
					source.setValue(l);
				} else if (destinationCountry == null && sourceCountry != null) {
					destinationCountry = (Country) b.getUserData();
					Label l = new Label();
					l.setFont(new Font(20));
					l.setTextFill(Color.BLACK);
					l.setText(destinationCountry.name);
					dest.setValue(l);
				}
			});

			Label lb = new Label(Countrys.get(i).name);
			lb.setFont(new Font(10));
			lb.setTextFill(Color.BLACK);
			lb.setTranslateX(getX(Countrys.get(i).x));
			lb.setTranslateY(getY(Countrys.get(i).y) - 10);

			root.getChildren().add(b);
			root.getChildren().add(lb);
		}

	}

	Group group=new Group();
	
	private void drawPathOnMap(Country[] cities) {
		for (int i = 0; i < cities.length - 1; i++) {
			Line line = new Line(getX(cities[i].x) + 5,getY(cities[i].y) + 5,
					getX(cities[i+1].x) + 5,getY(cities[i+1].y) + 5);
			line.setStroke(Color.BLACK);
			line.setStrokeWidth(2);
			group.getChildren().add(line);
		}
		
	}
	
	private float getX(float xCountry) {
		float div=mapWidth/1200;
		return ((3.3334f*xCountry)-45)*div+mapWidth/2;
	}
	private float getY(float yCountry) {
		float div=mapHieght/715;
		return ((-3.97222f*yCountry)-22.5f)*div+mapHieght/2;
	}

	public static void main(String[] args) throws FileNotFoundException {
		Countrys = Dijkstra.readFile();
		launch(args);
	}
}
