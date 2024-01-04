package atlas.atlas.Managers;

import atlas.atlas.Atlas;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MessageManager {

    public HashMap<String, ArrayList<String>> questions;

    public String currentQuestion;

    public MessageManager() {
        questions = new HashMap<>();
        currentQuestion = null;
        startQuiz();
    }
    public HashMap<String, ArrayList<String>> getQuestions() {
        return questions;
    }
    public void setQuestions(HashMap<String, ArrayList<String>> questions) {
        this.questions = questions;
    }
    public String getCurrentQuestion() {
        return currentQuestion;
    }
    public void setCurrentQuestion(String currentQuestion) {
        this.currentQuestion = currentQuestion;
    }
    public ArrayList<String> getCurrentAnswers() {
        if (currentQuestion != null) {
            return questions.get(currentQuestion);
        }
        return null;
    }

    public boolean isCorrectAnswer(String answer) {
        if (getCurrentAnswers() == null) {
            return false;
        }
        for (String possibleAnswer : getCurrentAnswers()) {
            if (possibleAnswer.equalsIgnoreCase(answer)) {
                return true;
            }
        }
        return false;
    }

    public void startQuiz() {
        new BukkitRunnable() {
            int time = 0;
            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().isEmpty()) {
                    return;
                }
                if (time % 10 == 0) {
                    String question = getRandomKey(questions);
                    setCurrentQuestion(question);
                    Bukkit.broadcastMessage("§b" + question);
                }
                if (time % 5 == 0 && time % 10 != 0 && time != 0 && getCurrentQuestion() != null) {
                    Bukkit.broadcastMessage("§8§lSadly nobody got the answer.\n§eThe correct answer was \"" + getCurrentAnswers().get(0) + "\"");
                    setCurrentQuestion(null);
                }
                time++;
            }
        }.runTaskTimer(Atlas.getPlugin(Atlas.class), 0, 20);
    }
    private String getRandomKey(HashMap<String, ArrayList<String>> hashMap) {
        if (hashMap != null && !hashMap.isEmpty()) {
            ArrayList<String> keysList = new ArrayList<>(hashMap.keySet());
            Random random = new Random();
            int randomIndex = random.nextInt(keysList.size());
            return keysList.get(randomIndex);
        } else {
            return null;
        }
    }
}
