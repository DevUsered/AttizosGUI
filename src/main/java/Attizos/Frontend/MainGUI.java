package Attizos.Frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class MainGUI extends Application {
    @Override
    public void start(Stage ventanaPrincipal) throws Exception {
        Attizos.Backend.Attizos.App.cargarDatosEnRAM();
        Application.setUserAgentStylesheet(new atlantafx.base.theme.PrimerDark().getUserAgentStylesheet());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        Parent design = loader.load();

        ventanaPrincipal.setTitle("Attizos - Login");
        Scene escena = new Scene(design);
        ventanaPrincipal.setScene(escena);
        ventanaPrincipal.setResizable(false);
        ventanaPrincipal.show();

    }
    public static void main(String[] args) {
        launch(args);
    }

}
