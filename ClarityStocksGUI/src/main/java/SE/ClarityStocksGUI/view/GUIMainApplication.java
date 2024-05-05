package SE.ClarityStocksGUI.view;

import SE.ClarityStocksGUI.controller.FavoriteListController;
import SE.ClarityStocksGUI.controller.GUIMainController;
import io.github.palexdev.materialfx.theming.JavaFXThemes;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GUIMainApplication extends Application {
  
  private Scene mainView;
  private Stage stage;
  private GUIMainController mainController;
  private static GUIMainApplication guiMainApplication;

  @Override
  public void start(Stage stage) throws IOException {

    this.stage = stage;
    //stage.getIcons().add(new Image(
    //    getClass().getResource("/SE/ClarityStocksGUI/view/claritystocksIcon.png")
    //        .toExternalForm()));
    guiMainApplication = this;
    String css = this.getClass().getResource("/se/ClarityStocksGUI/styles.css").toExternalForm();
    //Setting up the Home view

    FXMLLoader mainLoader = new FXMLLoader(GUIMainApplication.class.getResource("Main-view.fxml")); //SLOW AF?
    mainView = new Scene(mainLoader.load(), 1280, 720);
    mainView.getStylesheets().add(css);
    mainController = mainLoader.getController();
    mainController.setApplication(this);


    FXMLLoader favoriteLoader = new FXMLLoader(GUIMainApplication.class.getResource("FavoriteListView.fxml"));
    Parent favoriteRoot = favoriteLoader.load();
    FavoriteListController favoriteListController = favoriteLoader.getController();

    mainController.setFavoriteListController(favoriteListController);

    System.out.println("MainController set up");
    //stockViewController.setCompanyData(companyData);

    //MaterialFX default code to get stylesheets working
    UserAgentBuilder.builder()
        .themes(JavaFXThemes.MODENA)
        .themes(MaterialFXStylesheets.forAssemble(true))
        .setDeploy(true)
        .setResolveAssets(true)
        .build()
        .setGlobal();

    System.out.println("MaterialFX set up");
    stage.setTitle("Clarity Stocks");
    stage.setScene(mainView);
    stage.show();
    System.out.println("Program should be visible");

  }

  public double getHeight() {
    return stage.getHeight();
  }

  public double getWidth() {
    return stage.getWidth();
  }

  public void setHeight(double height) {
    stage.setHeight(height);
  }

  public void setWidth(double width) {
    stage.setWidth(width);
  }
    /*

    public void sceneSwitch(){
        double height = stage.getHeight();
        double width = stage.getWidth();
        stage.setHeight(height);
        stage.setWidth(width);
    }

    public void goToStockView(){
        double height = stage.getHeight();
        double width = stage.getWidth();
        stockViewController.loadStockView("TSLA");

        stage.setHeight(height);
        stage.setWidth(width);
        stage.setScene(stockView);

    }

    public void goToHomeView(){
        double height = stage.getHeight();
        double width = stage.getWidth();

        stage.setHeight(height);
        stage.setWidth(width);
        try {
            stage.setScene(homeView);

        }catch (ClassCastException e){

        }

    }

     */

  public static GUIMainApplication getInstance() {
    return guiMainApplication;
  }

  public static void main(String[] args) {
        /*
            All logging is disabled because of a bug in an external library that gives tons of error messages when
            nothing is wrong.

            Remove the two lines under to enable logging again.

         */

    Logger logger = Logger.getLogger(""); // COMMENT OUT THIS LINE
    logger.setLevel(Level.OFF);     //COMMENT OUT THIS LINE
    System.setProperty("prism.lcdtext", "false");
    launch(args);
  }

}
