module SE.ClarityStocksGUI {

  requires org.controlsfx.controls;
  requires org.kordamp.bootstrapfx.core;
  requires javafx.graphics;
  requires javafx.controls;
  requires javafx.swing;
  requires javafx.fxml;
  requires javafx.base;
  requires VirtualizedFX;
  requires MaterialFX;
  requires ClarityStocksAPI;
  requires ClarityStocksData;
  requires java.logging;
  requires ClarityStocksAnalysis;
  requires eu.hansolo.medusa;
    requires ClarityStocksUser;

    opens SE.ClarityStocksGUI.view to javafx.fxml;
  opens SE.ClarityStocksGUI.controller.stockViewTiles;
  opens SE.ClarityStocksGUI.controller to javafx.fxml;
  opens SE.ClarityStocksGUI to javafx.fxml;
  opens SE.ClarityStocksGUI.controller.graphControllers to javafx.fxml;

  exports SE.ClarityStocksGUI.view;
  exports SE.ClarityStocksGUI.controller;
  exports SE.ClarityStocksGUI.controller.stockViewTiles;
  exports SE.ClarityStocksGUI;
  exports SE.ClarityStocksGUI.controller.graphControllers;
    exports SE.ClarityStocksGUI.controller.UserController;
    opens SE.ClarityStocksGUI.controller.UserController to javafx.fxml;
}
