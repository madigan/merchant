package tech.otter.merchant.model.factories;

import tech.otter.merchant.controller.Controller;
import tech.otter.merchant.model.Model;
import tech.otter.merchant.model.Quest;
import tech.otter.merchant.model.quests.IntroQuest;

public class QuestFactory {
    public Quest make(String name, Controller controller, Model world) {
        switch(name) {
            case "INTRO_QUEST":
                Quest quest = new IntroQuest("Main Story", controller, world);
                return quest;
        }
        return null;
    }
}
