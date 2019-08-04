package Chin_Hunter.Executes;

import Chin_Hunter.Executes.Hunting.*;
import Chin_Hunter.Executes.Questing.QuestMain;
import Chin_Hunter.Helpers.Paint;
import Chin_Hunter.Main;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.tab.Combat;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.ui.Log;

public class Starting {

    private Starting(){
        //Private Default Constructor
    }

    public static void execute(){
        //Starting
        Log.fine("Running AIO Chin Hunter by Shteve");
        Log.info("Waiting a few seconds for all the RS stuff to load.");
        Time.sleep(5000);
        Log.info("Ready to go.");
        Main.setPaint(new Paint());

        if (Combat.isAutoRetaliateOn())
            Combat.toggleAutoRetaliate(false);

        if (!Movement.isRunEnabled())
            Movement.toggleRun(true);

        populateItemHashmaps();

        //Sets best target
        Main.onLevelUpEvent();


    }

    private static void populateItemHashmaps(){
        Longtails.populateHashMaps();
        Butterflies.populateHashMaps();
        DeadfallKebbits.populateHashMaps();
        FalconKebbits.populateHashMaps();
        Chinchompas.populateHashMaps();
        QuestMain.populateHashMap();
        PurchaseItems.populateHashMap();
    }


}
