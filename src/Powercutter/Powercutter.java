package Powercutter;

import Chin_Hunter.Executes.Questing.Puzzle_Rooms.BronzeFeather;
import Chin_Hunter.Executes.Questing.Puzzle_Rooms.GoldenFeather;
import Chin_Hunter.Executes.Questing.Puzzle_Rooms.SilverFeather;
import Chin_Hunter.Executes.Questing.QuestAreas;
import Chin_Hunter.Executes.Questing.QuestTraveling;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Pickable;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.Worlds;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.GrandExchange;
import org.rspeer.runetek.api.component.GrandExchangeSetup;
import org.rspeer.runetek.api.component.chatter.ClanChat;
import org.rspeer.runetek.api.component.tab.*;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Pickables;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.runetek.event.listeners.ChatMessageListener;
import org.rspeer.runetek.event.types.ChatMessageEvent;
import org.rspeer.runetek.event.types.ChatMessageType;
import org.rspeer.runetek.providers.RSClanMember;
import org.rspeer.runetek.providers.RSGrandExchangeOffer;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.text.NumberFormat;
import java.util.Map;

@ScriptMeta(desc = "Chops any tree, anywhere and drops the logs.", developer = "Shteve", name = "Powercutter", category = ScriptCategory.WOODCUTTING, version = 0.1)
public class Powercutter extends Script implements ChatMessageListener {

    //TODO Set as customisable
    private static Area area = Area.rectangular(2983, 3191, 2994, 3181);


    private long START_TIME;
    private int START_XP;

    private static Position tile = new Position(2313,3595,0);

    @Override
    public void onStart() {
        Log.fine("Running Willow Cutter by Shteve");
        super.onStart();
    }

    @Override
    public int loop() {

        BronzeFeather.attemptToSolve();
        return 988;
    }

    private static void stuff(){
        buyItems();
    }

    private static void buyItems() {


        //TODO Timeout, wait for offers to finsih
        if (GrandExchange.getOffers(RSGrandExchangeOffer::isEmpty).length == 0)
            return;

        if (!GrandExchangeSetup.isOpen() && GrandExchange.createOffer(RSGrandExchangeOffer.Type.SELL))
            Time.sleepUntil(GrandExchangeSetup::isOpen, 2000);

        buySingleItem("Bread", 3);

    }

    private static boolean buySingleItem(String name, int amount) {
        if (name.equals("Coins"))
            return true;
        if (GrandExchangeSetup.setItem(name))
            Time.sleepUntil(() -> GrandExchangeSetup.getItem() != null, 2000);

        if (name.equals("Yellow dye"))
            GrandExchangeSetup.setPrice(2000);
        else {
            if (GrandExchangeSetup.getPricePerItem() < 200)
                GrandExchangeSetup.setPrice(250);
            else
                GrandExchangeSetup.increasePrice(5);
        }
        Time.sleep(500, 1000);

        if (GrandExchangeSetup.confirm())
            Time.sleepUntil(() -> !GrandExchangeSetup.isOpen(), 3000);
        return !GrandExchangeSetup.isOpen();
    }

    @Override
    public void onStop() {

        long millisecondsRan = System.currentTimeMillis() - START_TIME;

        int xpGained = Skills.getExperience(Skill.WOODCUTTING) - START_XP;
        String xpGainedString = NumberFormat.getIntegerInstance().format(xpGained);

        Log.fine("Run Time: " + formatTime(millisecondsRan) + " | XP Gained: " + xpGainedString + " | XP/HR: " +  getXPPerHour(xpGained,millisecondsRan));
        super.onStop();
    }

    private boolean attemptInitialisation() {
        int i = 0;
        while (i < 3) {
            if (Game.isLoggedIn()) {
                START_TIME = System.currentTimeMillis();
                START_XP = Skills.getExperience(Skill.WOODCUTTING);

                if (!playerHasAxe()) {
                    Log.severe("You need an axe to chop trees");
                    return false;
                }

                if (!Movement.isRunEnabled())
                    Movement.toggleRun(true);

                return true;
            } else {
                Log.info("Not logged in, waiting. Attempt #" + (i + 1));
                Time.sleepUntil(Game::isLoggedIn, 10000);
            }
            i++;
        }
        Log.severe("Sorry but we couldn't log you in. Stopping.");
        return false;
    }

    private boolean playerHasAxe(){
        //Wielding axe
        Item mainHandWeapon = EquipmentSlot.MAINHAND.getItem();
        if (mainHandWeapon != null && mainHandWeapon.getName().contains(" axe"))
            return true;

        //Got axe in Invent
        Item[] items = Inventory.getItems(x -> x.getName().contains(" axe"));
        if (items.length > 0)
            return true;

        //Not got axe
        return false;
    }


    private void chopTree() {
        //Get nearest Willow tree and if not null chop it and wait
        SceneObject tree = SceneObjects.getNearest("Willow");
        if (tree != null && tree.interact("Chop down")) {
            Time.sleepUntil(() -> Players.getLocal().getAnimation() != -1,2000);
        }
    }

    private void dropLogs() {
        //TODO Potentially hold down shift, if possible.

        //Drop from top to bottom left to right
        int[] order = {
                0, 4, 8, 12, 16, 20, 24,
                1, 5, 9, 13, 17, 21, 25,
                2, 6, 10, 14, 18, 22, 26,
                3, 7, 11, 15, 19, 23, 27
        };

        //If item isn't an axe, drop it and wait.
        for (int i : order) {
            Item item = Inventory.getItemAt(i);
            if (item != null && !item.getName().contains("axe")) {
                item.interact("Drop");
                Time.sleep(52, 213);
            }
        }
    }


    private String getXPPerHour(int inXPGained, long inMillisecondsRan){
        double xpPerMillisecond = (inXPGained / (double)(inMillisecondsRan));
        return NumberFormat.getIntegerInstance().format((int)(xpPerMillisecond * 1000 * 60 * 60));
    }

    private String formatTime(long inMilliseconds){
        long second = (inMilliseconds / 1000) % 60;
        long minute = (inMilliseconds / (1000 * 60)) % 60;
        long hour = (inMilliseconds / (1000 * 60 * 60)) % 24;

        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    @Override
    public void notify(ChatMessageEvent chatMessageEvent) {
        String message = chatMessageEvent.getMessage();
        if (chatMessageEvent.getType() == ChatMessageType.CLAN_CHANNEL) {
            String muleName = chatMessageEvent.getSource();
            int muleWorld = -1;

            RSClanMember[] member = ClanChat.getMembers(x->x.getDisplayName().getClean().equals(muleName));
            if (member.length > 0 && member[0] != null){
                muleWorld = member[0].getWorld();
            }
        }
    }
}
