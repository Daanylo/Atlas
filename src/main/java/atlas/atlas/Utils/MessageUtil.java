package atlas.atlas.Utils;

import atlas.atlas.Atlas;
import atlas.atlas.Managers.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageUtil {

    static MessageManager messageManager = Atlas.getInstance().getMessageManager();
    public static void createMessages() {
        new File(Atlas.getInstance().getDataFolder() + "/data").mkdir();
        File messageFile = new File(Atlas.getInstance().getDataFolder() + "/data/" + "messages.yml");
        FileConfiguration fc = new YamlConfiguration();
        if (!messageFile.exists()) {
            ArrayList<String> answers = new ArrayList<>();
            answers.add("example answer 1");
            answers.add("example answer 2");
            answers.add("example answer 3");
            fc.set("quiz.example question", answers);
            try {
                messageFile.createNewFile();
                fc.save(messageFile);
                Bukkit.getLogger().info("messages.yml created.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadMessages() {
        File messageFile = new File(Atlas.getInstance().getDataFolder() + "/data/" + "messages.yml");
        FileConfiguration fc = new YamlConfiguration();
        try {
            fc.load(messageFile);
            Bukkit.getLogger().info("Loading messages...");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        HashMap<String, ArrayList<String>> questions = new HashMap<>();
        if (fc.isConfigurationSection("quiz")) {
            ConfigurationSection quizSection = fc.getConfigurationSection("quiz");
            for (String key : quizSection.getKeys(false)) {
                String question = key;
                List<String> answersList = quizSection.getStringList(key);
                ArrayList<String> answers = new ArrayList<>(answersList);
                questions.put(question, answers);
            }
        }
        messageManager.setQuestions(questions);
    }
}
