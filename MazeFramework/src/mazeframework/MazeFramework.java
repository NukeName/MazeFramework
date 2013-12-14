/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mazeframework;

import java.awt.Point;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import mazeframework.interfaces.MazeGlobals;

/**
 *
 * @author Heisenberg
 */
public class MazeFramework extends Application {

    private Slider slider;
    private CheckMenuItem noDiagonal;
    private FlowPane mazeCentered;
    private MazeGrid mazeGrid;
    private StackPane root;
    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        root = new StackPane();
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });


        //root.getChildren().add(btn);
        Group mazeGroup = new Group();
        //root.getChildren().add(mazeGroup);
        VBox guiBox = new VBox();
        guiBox.setAlignment(Pos.TOP_CENTER);

        MenuBar menu = new MenuBar();
        Menu file = new Menu("File");
        MenuItem openMazeFile = new MenuItem("Open Maze File");
        openMazeFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Maze File");
                //   System.out.println(pic.getId());
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze File", "*.maze"));
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    try (
                            InputStream fStream = new FileInputStream(file);
                            InputStream buffer = new BufferedInputStream(fStream);
                            ObjectInput input = new ObjectInputStream(buffer);) {
                        MazeGridDataContainer mgd = (MazeGridDataContainer) input.readObject();
                        MazeGrid mgrid = mgd.reconstructMazeGrid();
                        mazeCentered.getChildren().clear();
                        mazeGrid = mgrid;
                        mazeCentered.getChildren().add(mgrid.getGridGroup());
                        slider.adjustValue(mgrid.getSideCount());
                    } catch (IOException | ClassNotFoundException ex) {
                        Logger.getLogger(MazeFramework.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null, ex.getMessage(), ex.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        });
        MenuItem saveMazeFile = new MenuItem("Save Maze File");
        saveMazeFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Maze");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze File", "*.maze"));
                //     System.out.println(pic.getId());
                File file = fileChooser.showSaveDialog(stage);
                if (file != null) {
                    if (mazeGrid != null) {
                        try (OutputStream outputFile = new FileOutputStream(file.getAbsolutePath() + ".maze");
                                OutputStream buffer = new BufferedOutputStream(outputFile);
                                ObjectOutput output = new ObjectOutputStream(buffer);) {
                            output.writeObject(mazeGrid.toDataContainer());
                        } catch (IOException ex) {
                            Logger.getLogger(MazeFramework.class.getName()).log(Level.SEVERE, null, ex);
                            JOptionPane.showMessageDialog(null, ex.getMessage(), ex.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                        }

                    }
                }




            }
        });
        file.getItems().addAll(openMazeFile, saveMazeFile);

        Menu options = new Menu("Cells per row");
        CustomMenuItem cells = new CustomMenuItem();
        slider = new Slider(5, 50, 10);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                mazeCentered.getChildren().clear();
                MazeGrid mg = new MazeGrid(null, (int) (slider.getValue()), 500);
                mazeGrid = mg;
                mazeCentered.getChildren().add(mg.getGridGroup());
            }
        });
        cells.setContent(slider);


        options.getItems().addAll(cells);

        Menu execution = new Menu("Execution");
        noDiagonal = new CheckMenuItem("Diagonal Movement");
        MenuItem execute = new MenuItem("Execute");
        execute.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (mazeGrid.hasStartAndEnd()) {

                    Task<List<Point>> tsk = new Task<List<Point>>() {
                        @Override
                        protected List<Point> call() {
                            MazeGlobals.algorithm.initPathfinder(mazeGrid.getConvertedGrid(), mazeGrid.getStart(), mazeGrid.getGoal());
                            List<Point> res = MazeGlobals.algorithm.startPathfinder(noDiagonal.isSelected());
                            return res;
                        }
                    };

                    tsk.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent t) {
                            mazeGrid.overlaySolution((List<Point>) t.getSource().getValue());
                        }
                    });
                    
                    tsk.setOnFailed(new EventHandler<WorkerStateEvent>() {

                        @Override
                        public void handle(WorkerStateEvent t) {
                            try {
                                throw t.getSource().getException();
                            } catch (Throwable ex) {
                                Logger.getLogger(MazeFramework.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    

                    Thread thrd = new Thread(tsk);
                    thrd.start();

                } else {
                    JOptionPane.showMessageDialog(null, "Goal and Start positions are not set!", "Can't run!", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        execution.getItems().addAll(noDiagonal, execute);



        menu.getMenus().addAll(file, options, execution);

        mazeCentered = new FlowPane();
        mazeCentered.setAlignment(Pos.CENTER);


        MazeGrid mg = new MazeGrid(null, 10, 500);
        mazeGrid = mg;
        mazeCentered.getChildren().add(mg.getGridGroup());



        guiBox.getChildren().add(menu);
        guiBox.getChildren().add(mazeCentered);

        root.getChildren().add(guiBox);
        Scene scene = new Scene(root, 500, 520);

        primaryStage.setTitle("Maze Framework");
        //  primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
