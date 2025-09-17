import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Interfaccia grafica JavaFX per l'automazione degli appunti
 */
public class ClipboardGUI extends Application {
    
    private ClipboardAutomation automation;
    private Button startButton;
    private Label statusLabel;
    private Label titleLabel;
    private Label instructionLabel;
    private Label elementsLabel;
    private Spinner<Integer> elementsSpinner;
    private CheckBox separatorCheckBox;
    private CheckBox specialEffectsCheckBox;
    private Stage primaryStage;
    private Pane bubblePane;
    private List<Circle> bubbles;
    private Timeline bubbleAnimation;
    private Random random;
    private I18nManager i18n = I18nManager.getInstance();
    private MenuBar menuBar;
    private Menu languageMenu;
    private MenuItem italianMenuItem;
    private MenuItem englishMenuItem;
    
    @Override
    public void start(Stage primaryStage) {
        try {
            this.primaryStage = primaryStage;
            this.bubbles = new ArrayList<>();
            this.random = new Random();
            
            // Inizializza l'automazione
            automation = new ClipboardAutomation();
            
            // Crea la menu bar
            createMenuBar();
            
            // Crea i componenti dell'interfaccia
            createComponents();
            
            // Configura il layout principale
            BorderPane mainLayout = new BorderPane();
            mainLayout.setTop(menuBar);
            
            // Configura il layout centrale
            Pane mainPane = new Pane();
            
            VBox root = new VBox(20);
            root.setAlignment(Pos.CENTER);
            root.setPadding(new Insets(30));
            root.setStyle("-fx-background-color: #f8f9fa;");
            
            titleLabel = new Label(i18n.getText("title.label"));
            titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
            
            instructionLabel = new Label(i18n.getText("instruction.label"));
            instructionLabel.setStyle("-fx-text-alignment: center; -fx-text-fill: #34495e;");
            
            // Crea il pannello di configurazione
            VBox configPanel = createConfigurationPanel();
            
            root.getChildren().addAll(titleLabel, instructionLabel, configPanel, startButton, statusLabel);
            
            // Centra il VBox root nel mainPane
            root.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
                root.setLayoutX((500 - newBounds.getWidth()) / 2);
                root.setLayoutY((400 - newBounds.getHeight()) / 2);
            });
            
            // Crea il pannello delle bolle sopra tutto
            bubblePane = new Pane();
            bubblePane.setPrefSize(500, 400);
            bubblePane.setMouseTransparent(true);
            // Posiziona le bolle per coprire tutta la finestra
            bubblePane.setLayoutX(0);
            bubblePane.setLayoutY(0);
            
            mainPane.getChildren().addAll(root, bubblePane);
            mainLayout.setCenter(mainPane);
            
            // Configura la scena
            Scene scene = new Scene(mainLayout, 500, 430); // Aumenta altezza per il menu
            primaryStage.setTitle(i18n.getText("window.title"));
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            
            // Configura la chiusura dell'applicazione
            primaryStage.setOnCloseRequest(e -> {
                e.consume();
                closeWithAnimation();
            });
            
            primaryStage.show();
            
            // Inizializza le animazioni delle bolle
            initializeBubbleAnimation();
            
            // Applica gli effetti ai componenti
            applyComponentEffects();
            
        } catch (Exception e) {
            showErrorDialog(i18n.getText("error.init.title"), i18n.getText("error.init.message") + e.getMessage());
        }
    }
    
    /**
     * Crea i componenti dell'interfaccia
     */
    private void createComponents() {
        startButton = new Button(i18n.getText("start.button"));
        startButton.setStyle("-fx-font-size: 14px; -fx-padding: 15px 30px; " +
                            "-fx-background-color: #27ae60; -fx-text-fill: white; " +
                            "-fx-background-radius: 25px; -fx-border-radius: 25px; " +
                            "-fx-font-weight: bold; -fx-cursor: hand;");
        startButton.setOnAction(e -> startAutomation());
        
        // Aggiungi effetto ombra al pulsante
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.3));
        shadow.setOffsetX(2);
        shadow.setOffsetY(2);
        shadow.setRadius(5);
        startButton.setEffect(shadow);
        
        statusLabel = new Label(i18n.getText("status.ready"));
        statusLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold; -fx-font-size: 12px;");
        
        // Spinner per il numero di elementi
        elementsSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 4);
        elementsSpinner.setValueFactory(valueFactory);
        elementsSpinner.setEditable(true);
        elementsSpinner.setPrefWidth(80);
        elementsSpinner.setStyle("-fx-background-radius: 5px; -fx-border-radius: 5px;");
        
        // Aggiunge listener per aggiornare il valore quando viene digitato manualmente
        elementsSpinner.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                try {
                    int value = Integer.parseInt(newValue);
                    if (value >= 1 && value <= 20) {
                        elementsSpinner.getValueFactory().setValue(value);
                    }
                } catch (NumberFormatException e) {
                    // Ignora valori non numerici
                }
            }
        });
        
        // CheckBox per il separatore
        separatorCheckBox = new CheckBox(i18n.getText("separator.checkbox"));
        separatorCheckBox.setSelected(true);
        separatorCheckBox.setStyle("-fx-text-fill: #2c3e50;");
        
        // CheckBox per gli effetti speciali
        specialEffectsCheckBox = new CheckBox(i18n.getText("effects.checkbox"));
        specialEffectsCheckBox.setSelected(true);
        specialEffectsCheckBox.setStyle("-fx-text-fill: #8e44ad; -fx-font-weight: bold;");
        specialEffectsCheckBox.setOnAction(e -> toggleSpecialEffects());
    }
    
    /**
     * Crea il pannello di configurazione
     */
    private VBox createConfigurationPanel() {
        VBox configPanel = new VBox(15);
        configPanel.setAlignment(Pos.CENTER);
        configPanel.setStyle("-fx-background-color: #ecf0f1; -fx-padding: 20px; " +
                            "-fx-background-radius: 10px; -fx-border-color: #bdc3c7; " +
                            "-fx-border-radius: 10px; -fx-border-width: 1px;");
        
        // Aggiungi effetto ombra al pannello
        DropShadow panelShadow = new DropShadow();
        panelShadow.setColor(Color.rgb(0, 0, 0, 0.1));
        panelShadow.setOffsetX(1);
        panelShadow.setOffsetY(1);
        panelShadow.setRadius(3);
        configPanel.setEffect(panelShadow);
        
        // Riga per il numero di elementi
        HBox elementsRow = new HBox(10);
        elementsRow.setAlignment(Pos.CENTER);
        elementsLabel = new Label(i18n.getText("elements.label"));
        elementsLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        elementsRow.getChildren().addAll(elementsLabel, elementsSpinner);
        
        // Riga per il separatore
        HBox separatorRow = new HBox(10);
        separatorRow.setAlignment(Pos.CENTER);
        separatorRow.getChildren().add(separatorCheckBox);
        
        // Riga per gli effetti speciali
        HBox effectsRow = new HBox(10);
        effectsRow.setAlignment(Pos.CENTER);
        effectsRow.getChildren().add(specialEffectsCheckBox);
        
        configPanel.getChildren().addAll(elementsRow, separatorRow, effectsRow);
        return configPanel;
    }
    
    /**
     * Avvia il processo di automazione
     */
    private void startAutomation() {
        // Disabilita il pulsante durante l'esecuzione
        startButton.setDisable(true);
        statusLabel.setText(i18n.getText("status.running"));
        statusLabel.setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
        
        // Esegue l'automazione in un thread separato per non bloccare l'interfaccia
        Thread automationThread = new Thread(() -> {
            try {
                // Piccola pausa prima di iniziare per permettere all'utente di prepararsi
                Thread.sleep(2000);
                
                // Ottiene i valori configurati dall'utente
                int numberOfElements = elementsSpinner.getValue();
                boolean addSeparator = separatorCheckBox.isSelected();
                
                // Esegue l'automazione con i parametri configurati
                automation.executeAutomation(numberOfElements, addSeparator);
                
                // Aggiorna l'interfaccia nel thread JavaFX
                javafx.application.Platform.runLater(() -> {
                    statusLabel.setText(i18n.getText("status.completed"));
                    statusLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    startButton.setDisable(false);
                });
                
            } catch (Exception ex) {
                // Gestisce gli errori
                javafx.application.Platform.runLater(() -> {
                    statusLabel.setText(i18n.getText("status.error"));
                    statusLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    startButton.setDisable(false);
                    showErrorDialog(i18n.getText("error.automation.title"), i18n.getText("error.automation.message") + ex.getMessage());
                });
            }
        });
        
        automationThread.setDaemon(true);
        automationThread.start();
    }
    
    /**
     * Crea la menu bar con il menu lingua
     */
    private void createMenuBar() {
        menuBar = new MenuBar();
        languageMenu = new Menu(i18n.getText("menu.language"));
        
        italianMenuItem = new MenuItem(i18n.getText("menu.italian"));
        englishMenuItem = new MenuItem(i18n.getText("menu.english"));
        
        italianMenuItem.setOnAction(e -> {
            i18n.setLanguage("it");
            updateAllTexts();
        });
        
        englishMenuItem.setOnAction(e -> {
            i18n.setLanguage("en");
            updateAllTexts();
        });
        
        languageMenu.getItems().addAll(italianMenuItem, englishMenuItem);
        menuBar.getMenus().add(languageMenu);
    }
    
    /**
     * Aggiorna tutti i testi dell'interfaccia con la lingua corrente
     */
    private void updateAllTexts() {
        primaryStage.setTitle(i18n.getText("window.title"));
        titleLabel.setText(i18n.getText("title.label"));
        instructionLabel.setText(i18n.getText("instruction.label"));
        elementsLabel.setText(i18n.getText("elements.label"));
        separatorCheckBox.setText(i18n.getText("separator.checkbox"));
        specialEffectsCheckBox.setText(i18n.getText("effects.checkbox"));
        startButton.setText(i18n.getText("start.button"));
        
        // Aggiorna il messaggio di stato solo se è quello di default
        String currentStatus = statusLabel.getText();
        if (currentStatus.equals("Pronto per l'automazione") || currentStatus.equals("Ready for automation")) {
            statusLabel.setText(i18n.getText("status.ready"));
        }
        
        languageMenu.setText(i18n.getText("menu.language"));
        italianMenuItem.setText(i18n.getText("menu.italian"));
        englishMenuItem.setText(i18n.getText("menu.english"));
    }
    
    /**
     * Mostra un dialog di errore
     */
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Inizializza l'animazione delle bolle
     */
    private void initializeBubbleAnimation() {
        bubbleAnimation = new Timeline();
        bubbleAnimation.setCycleCount(Timeline.INDEFINITE);
        
        // Crea bolle periodicamente
        bubbleAnimation.getKeyFrames().add(
            new javafx.animation.KeyFrame(Duration.millis(800), e -> createBubble())
        );
        
        bubbleAnimation.play();
    }
    
    /**
     * Crea una nuova bolla
     */
    private void createBubble() {
        if (!specialEffectsCheckBox.isSelected()) return;
        
        Circle bubble = new Circle();
        bubble.setRadius(random.nextDouble() * 8 + 3); // Raggio tra 3 e 11
        bubble.setFill(Color.rgb(52, 152, 219, 0.3 + random.nextDouble() * 0.4));
        bubble.setCenterX(random.nextDouble() * 500);
        bubble.setCenterY(400 + bubble.getRadius());
        
        bubblePane.getChildren().add(bubble);
        bubbles.add(bubble);
        
        // Animazione di movimento verso l'alto
        TranslateTransition moveUp = new TranslateTransition(Duration.seconds(3 + random.nextDouble() * 2), bubble);
        moveUp.setToY(-450 - bubble.getRadius());
        
        // Animazione di oscillazione orizzontale
        TranslateTransition sway = new TranslateTransition(Duration.seconds(1 + random.nextDouble()), bubble);
        sway.setToX((random.nextDouble() - 0.5) * 50);
        sway.setCycleCount(Timeline.INDEFINITE);
        sway.setAutoReverse(true);
        
        // Animazione di scala
        ScaleTransition scale = new ScaleTransition(Duration.seconds(0.5 + random.nextDouble()), bubble);
        scale.setToX(0.8 + random.nextDouble() * 0.4);
        scale.setToY(0.8 + random.nextDouble() * 0.4);
        scale.setCycleCount(Timeline.INDEFINITE);
        scale.setAutoReverse(true);
        
        ParallelTransition bubbleAnim = new ParallelTransition(moveUp, sway, scale);
        bubbleAnim.setOnFinished(e -> {
            bubblePane.getChildren().remove(bubble);
            bubbles.remove(bubble);
        });
        
        bubbleAnim.play();
    }
    
    /**
     * Applica effetti ai componenti
     */
    private void applyComponentEffects() {
        // Effetti sul pulsante principale
        applyButtonEffects();
        
        // Effetti sui componenti editabili
        applyHoverEffects(elementsSpinner);
        applyHoverEffects(separatorCheckBox);
        applyHoverEffects(specialEffectsCheckBox);
    }
    
    /**
     * Applica effetti speciali al pulsante principale
     */
    private void applyButtonEffects() {
        startButton.setOnMouseEntered(e -> {
            if (specialEffectsCheckBox.isSelected()) {
                // Reset forzato a 0 gradi prima dell'animazione
                startButton.setRotate(0);
                
                // Animazione di rotazione estremamente sottile (5 gradi)
                RotateTransition rotate = new RotateTransition(Duration.millis(300), startButton);
                rotate.setToAngle(5); // Rotazione assoluta a 5 gradi
                rotate.setInterpolator(Interpolator.EASE_BOTH);
                
                // Animazione di scala
                ScaleTransition scale = new ScaleTransition(Duration.millis(300), startButton);
                scale.setToX(1.2);
                scale.setToY(1.2);
                scale.setInterpolator(Interpolator.EASE_BOTH);
                
                // Effetto glow
                DropShadow glow = new DropShadow();
                glow.setColor(Color.rgb(39, 174, 96, 0.8));
                glow.setRadius(20);
                glow.setSpread(0.3);
                startButton.setEffect(glow);
                
                ParallelTransition enterAnim = new ParallelTransition(rotate, scale);
                enterAnim.play();
            } else {
                // Animazione semplice di ingrandimento
                ScaleTransition scale = new ScaleTransition(Duration.millis(200), startButton);
                scale.setToX(1.1);
                scale.setToY(1.1);
                scale.play();
                
                // Effetto ombra più pronunciato
                DropShadow shadow = new DropShadow();
                shadow.setColor(Color.rgb(0, 0, 0, 0.5));
                shadow.setOffsetX(3);
                shadow.setOffsetY(3);
                shadow.setRadius(8);
                startButton.setEffect(shadow);
            }
        });
        
        startButton.setOnMouseExited(e -> {
            if (specialEffectsCheckBox.isSelected()) {
                // Rotazione di ritorno a 0 gradi
                RotateTransition rotate = new RotateTransition(Duration.millis(200), startButton);
                rotate.setToAngle(0); // Rotazione assoluta a 0 gradi
                rotate.setInterpolator(Interpolator.EASE_IN);
                
                // Ritorno alla scala normale
                ScaleTransition scale = new ScaleTransition(Duration.millis(200), startButton);
                scale.setToX(1.0);
                scale.setToY(1.0);
                scale.setInterpolator(Interpolator.EASE_IN);
                
                // Ripristina ombra normale
                DropShadow shadow = new DropShadow();
                shadow.setColor(Color.rgb(0, 0, 0, 0.3));
                shadow.setOffsetX(2);
                shadow.setOffsetY(2);
                shadow.setRadius(5);
                startButton.setEffect(shadow);
                
                ParallelTransition exitAnim = new ParallelTransition(rotate, scale);
                exitAnim.play();
            } else {
                // Ritorno semplice alla scala normale
                ScaleTransition scale = new ScaleTransition(Duration.millis(200), startButton);
                scale.setToX(1.0);
                scale.setToY(1.0);
                scale.play();
                
                // Ripristina ombra normale
                DropShadow shadow = new DropShadow();
                shadow.setColor(Color.rgb(0, 0, 0, 0.3));
                shadow.setOffsetX(2);
                shadow.setOffsetY(2);
                shadow.setRadius(5);
                startButton.setEffect(shadow);
            }
        });
    }
    
    /**
     * Applica effetti hover ai componenti
     */
    private void applyHoverEffects(javafx.scene.Node component) {
        component.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(150), component);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();
        });
        
        component.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(150), component);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });
    }
    
    /**
     * Gestisce il toggle degli effetti speciali
     */
    private void toggleSpecialEffects() {
        if (!specialEffectsCheckBox.isSelected()) {
            // Rimuovi tutte le bolle esistenti
            bubblePane.getChildren().clear();
            bubbles.clear();
        }
    }
    
    /**
     * Chiude l'applicazione con animazione
     */
    private void closeWithAnimation() {
        // Ferma l'animazione delle bolle
        if (bubbleAnimation != null) {
            bubbleAnimation.stop();
        }
        
        // Animazione di zoom out
        ScaleTransition zoomOut = new ScaleTransition(Duration.seconds(1), primaryStage.getScene().getRoot());
        zoomOut.setToX(0.1);
        zoomOut.setToY(0.1);
        
        // Animazione di rotazione
        RotateTransition rotate = new RotateTransition(Duration.seconds(1), primaryStage.getScene().getRoot());
        rotate.setByAngle(360);
        
        // Animazione di fade out verso bianco
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), primaryStage.getScene().getRoot());
        fadeOut.setToValue(0.0);
        
        // Effetto blur
        GaussianBlur blur = new GaussianBlur();
        Timeline blurTimeline = new Timeline(
            new javafx.animation.KeyFrame(Duration.ZERO, 
                new javafx.animation.KeyValue(blur.radiusProperty(), 0)),
            new javafx.animation.KeyFrame(Duration.seconds(1), 
                new javafx.animation.KeyValue(blur.radiusProperty(), 20))
        );
        primaryStage.getScene().getRoot().setEffect(blur);
        
        ParallelTransition closeAnimation = new ParallelTransition(zoomOut, rotate, fadeOut);
        closeAnimation.setOnFinished(e -> {
            primaryStage.close();
            System.exit(0);
        });
        
        blurTimeline.play();
        closeAnimation.play();
    }
    
    /**
     * Metodo main per avviare l'applicazione JavaFX
     */
    public static void main(String[] args) {
        launch(args);
    }
}