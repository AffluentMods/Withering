package me.affluent.decay.listener;

import me.affluent.decay.event.*;

public interface EventListener {

    default void onEvent(Event event) {}

    default void onAttackEvent(AttackEvent event) {}

    default void onFightEndEvent(FightEndEvent event) {}

    default void onChestOpenEvent(ChestOpenEvent event) {}

    default void onTavernSpinEvent(TavernSpinEvent event) {}

    default void onItemLevelUpEvent(ItemLevelUpEvent event) {}

    default void onGoldenTavernSpinEvent(GoldenTavernSpinEvent event) {}

    default void onLevelUpEvent(LevelUpEvent event) {}

    default void onConquestWinEvent(ConquestWinEvent event) {}

    default void onHolidayWinEvent(HolidayWinEvent event) {}

    default void onPetLevelUpEvent(PetLevelUpEvent event) {}

    default void onMiningEvent(MiningEvent event) {}

    default void onGiftingEvent(GiftingEvent event) {}

    default void onEquipEvent(EquipEvent event)  {}

    default void onUnequipEvent(UnequipEvent event) {}

    default void onItemStarEvent(ItemStarEvent event) {}

    default void onPetStarEvent(PetStarEvent event) {}

    default void onForgeEvent(ForgeEvent event) {}

    default void onScrollEvent(ScrollEvent event) {}
}