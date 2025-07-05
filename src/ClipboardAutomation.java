import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * Classe per l'automazione degli appunti di Windows 11
 * Gestisce l'apertura di Notepad e l'automazione dei tasti per Win+V
 */
public class ClipboardAutomation {
    
    private Robot robot;
    
    private boolean firstMultipleClipboardUsage;
    
    public ClipboardAutomation() throws Exception {
        this.robot = new Robot();
        // Imposta un delay tra le pressioni dei tasti per maggiore affidabilità
        robot.setAutoDelay(50);
        firstMultipleClipboardUsage = true;
    }
    
    /**
     * Apre Notepad di Windows
     */
    public void openNotepad() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("notepad.exe");
        pb.start();
        
        // Attende che Notepad si apra completamente
        waitFor(2000);
    }
    
    /**
     * Preme la combinazione Win+V per aprire gli appunti multipli
     */
    public void openClipboardHistory() throws InterruptedException {
    	int waitingTime = 500;
    	
    	robot.keyPress(KeyEvent.VK_WINDOWS);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_WINDOWS);
        
        // Attende 4 secondi per evitare rallentamenti di rendering grafico la prima volta, le altre volte attende mezzo secondo
        if (firstMultipleClipboardUsage) {
        	firstMultipleClipboardUsage = false;
        	waitFor(waitingTime);
        	waitingTime = 100;
        }
        waitFor(waitingTime);
    }
    
    /**
     * Naviga verso il basso nella lista degli appunti
     * @param steps numero di passi verso il basso
     */
    public void navigateDown(int steps) throws InterruptedException {
        for (int i = 0; i < steps; i++) {
            robot.keyPress(KeyEvent.VK_DOWN);
            robot.keyRelease(KeyEvent.VK_DOWN);
            waitFor(100); // Piccola pausa tra le pressioni
        }
    }
    
    /**
     * Preme Invio per confermare la selezione
     */
    public void pressEnter() throws InterruptedException {
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        waitFor(100); // Piccola pausa tra le pressioni
    }

    /**
     * Preme la sequenza per creare il separatore "---"
     */
    public void pressTheSeparatorSequence() throws InterruptedException {
        // -
        robot.keyPress(KeyEvent.VK_MINUS);
        robot.keyRelease(KeyEvent.VK_MINUS);
        // -
        robot.keyPress(KeyEvent.VK_MINUS);
        robot.keyRelease(KeyEvent.VK_MINUS);
        // -
        robot.keyPress(KeyEvent.VK_MINUS);
        robot.keyRelease(KeyEvent.VK_MINUS);
        waitFor(100); // Piccola pausa tra le pressioni
    }
    
    /**
     * Seleziona tutto il testo con Ctrl+A
     */
    public void selectAll() throws InterruptedException {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        waitFor(100); // Piccola pausa
    }
    
    /**
     * Copia il testo selezionato con Ctrl+C
     */
    public void copyToClipboard() throws InterruptedException {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_C);
        robot.keyRelease(KeyEvent.VK_C);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        waitFor(100); // Piccola pausa
    }
    
    /**
     * Attende per il tempo specificato
     * @param milliseconds millisecondi di attesa
     */
    public void waitFor(int milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
    }
    
    /**
     * Incolla un elemento specifico dagli appunti
     * @param position posizione dell'elemento (1 = primo, 4 = quarto)
     */
    public void pasteClipboardItem(int position) throws InterruptedException {
        // Apre gli appunti
        openClipboardHistory();
        
        // Naviga alla posizione desiderata (position-1 perché partiamo da 0)
        if (position > 1) {
            navigateDown(position - 1);
        }
        
        // Incolla l'elemento selezionato
        pressEnter();
        
        // Attende qualch istante per evitare che avvenga un doppio incolla (infatti a volte succede che incolla due volte lo stesso elemento)
        waitFor(200);
        
        // Va a capo
        pressEnter();
    }
    
    /**
     * Esegue l'intera sequenza di automazione con parametri configurabili
     * @param numberOfElements numero di elementi da incollare
     * @param addSeparator se true aggiunge il separatore "---" alla fine
     */
    public void executeAutomation(int numberOfElements, boolean addSeparator) throws Exception {
        // Apre Notepad
        openNotepad();
        
        // Attendi l'apertura del blocco note
        waitFor(1000);
        
        // Incolla gli elementi nell'ordine richiesto: dal più recente al più vecchio
        for (int i = numberOfElements; i >= 1; i--) {
            pasteClipboardItem(i);
            
            // Pausa tra gli elementi (tranne l'ultimo)
            if (i > 1) {
                waitFor(100);
            }
        }
        
        // Aggiunge il separatore solo se richiesto
        if (addSeparator) {
            // Va a capo
            pressEnter();
            // Crea il separatore ---
            pressTheSeparatorSequence();
            // Va a capo
            pressEnter();
            // Va a capo
            pressEnter();
        }
        
        // Attende 100 millisecondi prima di selezionare e copiare tutto
        waitFor(100);
        
        // Seleziona tutto il contenuto
        selectAll();
        
        // Copia tutto negli appunti
        copyToClipboard();
    }
    
    /**
     * Esegue l'automazione con i parametri di default (4 elementi, con separatore)
     * Mantenuto per compatibilità
     */
    public void executeAutomation() throws Exception {
        executeAutomation(4, true);
    }
}