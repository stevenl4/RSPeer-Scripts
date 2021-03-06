package Chin_Hunter.Executes.Eagles_Peak.Puzzle_Rooms;

import Chin_Hunter.Executes.Eagles_Peak.QuestMain;
import Chin_Hunter.Main;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.Pickable;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.Varps;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Pickables;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.ui.Log;

public class SilverFeather {

    public static boolean attemptToSolve(){
        switch (Varps.getBitValue(3099)){
            case 0:{
                SceneObject Pedestal = SceneObjects.getNearest(19974);
                if (Pedestal == null){
                    Log.severe("In Silver feather cave but can't find the pedestal.");
                    return false;
                }
                if (Pedestal.interact("Inspect"))
                    Time.sleepUntil(()->Varps.getBitValue(3099) != 0, Random.nextInt(3500, 5000));
                return false;
            }
            case 1:{
                SceneObject Rock1 = SceneObjects.getNearest(19969);
                if (Rock1 == null){
                    Log.severe("In Silver feather cave but can't find the rock 1.");
                    return false;
                }
                if (Rock1.interact("Inspect"))
                    Time.sleepUntil(()->Varps.getBitValue(3099) != 1, Random.nextInt(3500, 5000));
                return false;
            }
            case 2:{
                SceneObject Rock2 = SceneObjects.getNearest(19970);
                if (Rock2 == null){
                    Log.severe("In Silver feather cave but can't find the rock 2.");
                    return false;
                }
                if (Rock2.interact("Inspect"))
                    Time.sleepUntil(()->Varps.getBitValue(3099) != 2, Random.nextInt(3500, 5000));
                return false;
            }
            case 3:{
                SceneObject Opening = SceneObjects.getNearest(19967);
                if (Opening == null){
                    Log.severe("In Silver feather cave but can't opening.");
                    Position pos = Players.getLocal().getPosition();
                    //Walk north east
                    Main.walkTo(new Position(pos.getX() + 10, pos.getY() + 10, pos.getFloorLevel()));
                    return false;
                }
                if (Opening.interact("Inspect"))
                    Time.sleep(4000, 10000);
                return false;
            }
            case 4:{
                if (Dialog.isOpen()) {
                    QuestMain.selectDialogOption(1);
                    return false;
                }
                Npc Kebbit = Npcs.getNearest("Kebbit");
                if (Kebbit == null){
                    Log.severe("Expected to find a Kebbit but could not find... Lazy cunt prob not awake yet.");
                    Time.sleep(1000, 5000);
                    return false;
                }
                if (Kebbit.interact("Threaten")){
                    Time.sleepUntil(Dialog::isOpen,Random.nextInt(3500, 6000));
                    return false;
                }
            }
            case 5:{
                if (isComplete()){
                    leaveCave();
                    return !isInCave();
                }
                Pickable Feather = Pickables.getNearest("Silver feather");
                if (Feather == null){
                    Log.severe("Could not find silver feather... Should have been dropped here.");
                    return false;
                }
                if (Feather.interact("Take"))
                    Time.sleepUntil(SilverFeather::isComplete, Random.nextInt(3500, 5000));
            }
        }
        return true;
    }

    public static void leaveCave(){
        SceneObject Exit = SceneObjects.getNearest(19900);
        if (Exit == null){
            Log.severe("Could not find exit to Silver feather room.");
            return;
        }
        if (Exit.interact("Enter")) {
            Time.sleepUntil(() -> !isInCave(), Random.nextInt(3500, 5000));
            Time.sleep(500, 1800);
        }
    }

    public static boolean isInCave(){
        //Rocks inside cave
        return SceneObjects.getNearest(19971) != null;
    }

    public static boolean isComplete(){
        return Inventory.contains("Silver feather");
    }

}
