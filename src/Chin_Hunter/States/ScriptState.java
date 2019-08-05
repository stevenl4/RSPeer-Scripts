package Chin_Hunter.States;


import Chin_Hunter.Executes.*;
import Chin_Hunter.Executes.Herblore.Druidic_Ritual;
import Chin_Hunter.Executes.Herblore.Herblore_Training;
import Chin_Hunter.Executes.Hunting.*;
import Chin_Hunter.Executes.Questing.QuestMain;

public enum ScriptState {

    STARTING{
        @Override
        public void execute() {
            Starting.execute();
        }

        @Override
        public void onStart() {
        }
    },

    BANKING{
        @Override
        public void execute() {
            Banking.execute();
        }

        @Override
        public void onStart() {
            Banking.onStart();
        }
    },

    PURCHASE_ITEMS{
        @Override
        public void execute() {
            PurchaseItems.execute();
        }

        @Override
        public void onStart() {
            PurchaseItems.onStart();
        }
    },

    MUSEUM_QUIZ{
        @Override
        public void execute() {
            MuseumQuiz.execute();
        }

        @Override
        public void onStart() {
            MuseumQuiz.onStart();
        }
    },

    LONGTAILS{
        @Override
        public void execute() {
            Longtails.execute();
        }

        @Override
        public void onStart() {
            Longtails.onStart();
        }
    },

    BUTTERFLIES{
        @Override
        public void execute() {
            Butterflies.execute();
        }

        @Override
        public void onStart() {
            Butterflies.onStart();
        }
    },

    DEADFALL_KEBBITS {
        @Override
        public void execute() { DeadfallKebbits.execute(); }

        @Override
        public void onStart() {
            DeadfallKebbits.onStart();
        }
    },

    FALCON_KEBBITS{
        @Override
        public void execute() { FalconKebbits.execute(); }

        @Override
        public void onStart() {
            FalconKebbits.onStart();
        }
    },

    EAGLES_PEAK_QUEST{
        @Override
        public void execute() { QuestMain.execute(); }

        @Override
        public void onStart() {
            QuestMain.onStart();
        }
    },

    CHINCHOMPAS{
        @Override
        public void execute() { Chinchompas.execute(); }

        @Override
        public void onStart() {
            Chinchompas.onStart();
        }
    },

    DRUIDIC_RITUAL_QUEST{
        @Override
        public void execute() {
            Druidic_Ritual.execute();
        }

        @Override
        public void onStart() {
            Druidic_Ritual.onStart();
        }
    },

    HERBLORE_TRAINING{
        @Override
        public void execute() {
            Herblore_Training.execute();
        }

        @Override
        public void onStart() {
            Herblore_Training.onStart();
        }
    };


    public abstract void execute();

    public abstract void onStart();
}






