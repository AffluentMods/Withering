package me.affluent.decay.manager;

import me.affluent.decay.event.*;
import me.affluent.decay.listener.EventListener;

import java.util.ArrayList;
import java.util.List;

public class EventManager {

    private static List<EventListener> eventListeners = new ArrayList<>();

    public static void registerListener(EventListener eventListener) {
        if (eventListeners.contains(eventListener)) {
            System.out.println(
                    "[FATAL ERROR] !!! Tried to add event listener [name=" + eventListener.getClass().getName() +
                    "] which is already added !!!");
            return;
        }
        eventListeners.add(eventListener);
    }

    public static void callEvent(Event event) {
        for (EventListener eventListener : eventListeners) {
            if (event instanceof AttackEvent) eventListener.onAttackEvent((AttackEvent) event);
            if (event instanceof FightEndEvent) eventListener.onFightEndEvent((FightEndEvent) event);
            if (event instanceof ChestOpenEvent) eventListener.onChestOpenEvent((ChestOpenEvent) event);
            if (event instanceof TavernSpinEvent) eventListener.onTavernSpinEvent((TavernSpinEvent) event);
            if (event instanceof GoldenTavernSpinEvent) eventListener.onGoldenTavernSpinEvent((GoldenTavernSpinEvent) event);
            if (event instanceof LevelUpEvent) eventListener.onLevelUpEvent((LevelUpEvent) event);
            if (event instanceof ConquestWinEvent) eventListener.onConquestWinEvent((ConquestWinEvent) event);
            if (event instanceof HolidayWinEvent) eventListener.onHolidayWinEvent((HolidayWinEvent) event);
            if (event instanceof ItemLevelUpEvent) eventListener.onItemLevelUpEvent((ItemLevelUpEvent) event);
            if (event instanceof PetLevelUpEvent) eventListener.onPetLevelUpEvent((PetLevelUpEvent) event);
            if (event instanceof MiningEvent) eventListener.onMiningEvent((MiningEvent) event);
            if (event instanceof GiftingEvent) eventListener.onGiftingEvent((GiftingEvent) event);
            if (event instanceof EquipEvent) eventListener.onEquipEvent((EquipEvent) event);
            if (event instanceof UnequipEvent) eventListener.onUnequipEvent((UnequipEvent) event);
            if (event instanceof PetStarEvent) eventListener.onPetStarEvent((PetStarEvent) event);
            if (event instanceof ItemStarEvent) eventListener.onItemStarEvent((ItemStarEvent) event);
            if (event instanceof ForgeEvent) eventListener.onForgeEvent((ForgeEvent) event);
            if (event instanceof ScrollEvent) eventListener.onScrollEvent((ScrollEvent) event);
            else eventListener.onEvent(event);
        }
    }
}