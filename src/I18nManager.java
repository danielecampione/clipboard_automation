import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.List;

public class I18nManager {
    private static I18nManager instance;
    private String currentLanguage = "en"; // Default inglese (English)
    private Map<String, Map<String, String>> translations;
    private List<Consumer<String>> languageChangeListeners;
    
    private I18nManager() {
        translations = new HashMap<>();
        languageChangeListeners = new ArrayList<>();
        initializeTranslations();
    }
    
    public static I18nManager getInstance() {
        if (instance == null) {
            instance = new I18nManager();
        }
        return instance;
    }
    
    private void initializeTranslations() {
        // Traduzioni italiane
        Map<String, String> italian = new HashMap<>();
        italian.put("app.title", "Automazione Appunti");
        italian.put("window.title", "Clipboard Automation");
        italian.put("title.label", "Automazione Appunti");
        italian.put("instruction.label", "Configura i parametri e assicurati di avere abbastanza elementi\nnegli appunti di sistema prima di avviare l'automazione.");
        italian.put("elements.label", "Numero di elementi da incollare:");
        italian.put("separator.checkbox", "Aggiungi separatore \"---\" alla fine");
        italian.put("effects.checkbox", "Effetti speciali");
        italian.put("start.button", "Avvia Automazione");
        italian.put("status.ready", "Pronto per l'automazione");
        italian.put("status.running", "Automazione in corso...");
        italian.put("status.completed", "Automazione completata con successo!");
        italian.put("status.error", "Errore durante l'automazione");
        italian.put("error.init.title", "Errore di inizializzazione");
        italian.put("error.init.message", "Impossibile inizializzare l'automazione: ");
        italian.put("error.automation.title", "Errore di automazione");
        italian.put("error.automation.message", "Si è verificato un errore: ");
        italian.put("menu.language", "Lingua");
        italian.put("menu.italian", "Italiano");
        italian.put("menu.english", "English");
        
        // Traduzioni inglesi
        Map<String, String> english = new HashMap<>();
        english.put("app.title", "Clipboard Automation");
        english.put("window.title", "Clipboard Automation");
        english.put("title.label", "Clipboard Automation");
        english.put("instruction.label", "Configure the parameters and make sure you have enough elements\nin the system clipboard before starting the automation.");
        english.put("elements.label", "Number of elements to paste:");
        english.put("separator.checkbox", "Add separator \"---\" at the end");
        english.put("effects.checkbox", "Special effects");
        english.put("start.button", "Start Automation");
        english.put("status.ready", "Ready for automation");
        english.put("status.running", "Automation in progress...");
        english.put("status.completed", "Automation completed successfully!");
        english.put("status.error", "Error during automation");
        english.put("error.init.title", "Initialization Error");
        english.put("error.init.message", "Unable to initialize automation: ");
        english.put("error.automation.title", "Automation Error");
        english.put("error.automation.message", "An error occurred: ");
        english.put("menu.language", "Language");
        english.put("menu.italian", "Italiano");
        english.put("menu.english", "English");
        
        translations.put("it", italian);
        translations.put("en", english);
    }
    
    public String getText(String key) {
        Map<String, String> currentTranslations = translations.get(currentLanguage);
        return currentTranslations.getOrDefault(key, key);
    }
    
    public String getText(String key, String... params) {
        String text = getText(key);
        for (int i = 0; i < params.length; i++) {
            text = text.replace("{" + i + "}", params[i]);
        }
        return text;
    }
    
    public void setLanguage(String language) {
        if (translations.containsKey(language) && !currentLanguage.equals(language)) {
            currentLanguage = language;
            notifyLanguageChange();
        }
    }
    
    public String getCurrentLanguage() {
        return currentLanguage;
    }
    
    public void addLanguageChangeListener(Consumer<String> listener) {
        languageChangeListeners.add(listener);
    }
    
    private void notifyLanguageChange() {
        for (Consumer<String> listener : languageChangeListeners) {
            listener.accept(currentLanguage);
        }
    }
}