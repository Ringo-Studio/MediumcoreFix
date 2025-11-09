package com.ringoway.mediumcorefix.mixin;

import com.github.alexmodguy.mediumcore.GameRuleRegistry;
import com.github.alexmodguy.mediumcore.Mediumcore;
import com.ringoway.mediumcorefix.MediumcoreFix;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Миксин для исправления критического бага миграции в Mediumcore моде.
 *
 * === ПРОБЛЕМА ===
 * В оригинальном коде Mediumcore (CommonEvents.java, строки 51-54):
 *
 * if (!tag.contains(BASE_HEALTH_TAG)) {
 *     tag.putDouble(BASE_HEALTH_TAG, Mediumcore.CONFIG.startingPlayerHealth.get());
 *     tag.putDouble(HEALTH_MODIFIER_TAG, 0.0);  // ← СТИРАЕТ СТАРЫЕ ДАННЫЕ!
 *     player.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
 * }
 *
 * При обновлении со старой версии мода (которая использовала только HEALTH_MODIFIER_TAG)
 * на новую версию (с BASE_HEALTH_TAG), старые данные о потерянном здоровье СТИРАЮТСЯ в строке 53!
 *
 * Пример:
 * - Старая версия: игрок умер 5 раз, HEALTH_MODIFIER_TAG = -20, MaxHealth = 10 HP
 * - Обновление: BASE_HEALTH_TAG создается = 30, HEALTH_MODIFIER_TAG перезаписывается на 0
 * - Результат: MaxHealth = 30 HP (игрок восстановил всё здоровье!)
 *
 * === РЕШЕНИЕ ===
 * Этот миксин ИНЖЕКТИТ код ПОСЛЕ строки 49 (getPersistentData), но ДО строки 51 (if проверка).
 *
 * Логика:
 * 1. Если BASE_HEALTH_TAG отсутствует, НО есть старый HEALTH_MODIFIER_TAG
 * 2. Мигрируем: BASE_HEALTH = startingHealth + oldModifier
 * 3. Создаем BASE_HEALTH_TAG с этим значением
 * 4. Оригинальный код видит что BASE_HEALTH_TAG УЖЕ СУЩЕСТВУЕТ → НЕ входит в if блок (строка 51)
 * 5. Старое значение HEALTH_MODIFIER_TAG НЕ перезаписывается!
 *
 * === ПОЧЕМУ ЭТОГО МИКСИНА ДОСТАТОЧНО ===
 * Остальные методы (onLivingDeath, onPlayerRespawn, updateHealth) в новом коде уже правильно
 * работают с BASE_HEALTH_TAG. Проблема была ТОЛЬКО в onEntityJoinLevel при инициализации игрока.
 *
 * @author Ringoway
 */
@Mixin(value = com.github.alexmodguy.mediumcore.event.CommonEvents.class, remap = false)
public class MixinCommonEvents {

    private static final String HEALTH_MODIFIER_TAG = "MediumcoreHealthModifier";
    private static final String BASE_HEALTH_TAG = "MediumcoreBaseHealth";

    /**
     * Инжектим код миграции перед оригинальной проверкой BASE_HEALTH_TAG.
     *
     * Точка инжекта: сразу ПОСЛЕ вызова getPersistentData() (строка 49 оригинального кода),
     * но ДО проверки if (!tag.contains(BASE_HEALTH_TAG)) (строка 51).
     */
    @Inject(
        method = "onEntityJoinLevel(Lnet/minecraftforge/event/entity/EntityJoinLevelEvent;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;getPersistentData()Lnet/minecraft/nbt/CompoundTag;",
            ordinal = 0,
            shift = At.Shift.AFTER
        ),
        remap = false
    )
    private void fixMigrationData(EntityJoinLevelEvent event, CallbackInfo ci) {
        if (event.getEntity() instanceof Player player && !event.getLevel().isClientSide) {
            if (GameRuleRegistry.isMediumCoreMode(event.getLevel().getGameRules()) && player instanceof ServerPlayer) {
                CompoundTag tag = event.getEntity().getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);

                // МИГРАЦИЯ: Если BASE_HEALTH_TAG отсутствует, но есть старый HEALTH_MODIFIER_TAG
                if (!tag.contains(BASE_HEALTH_TAG) && tag.contains(HEALTH_MODIFIER_TAG)) {
                    double oldModifier = tag.getDouble(HEALTH_MODIFIER_TAG);
                    double startingHealth = Mediumcore.CONFIG.startingPlayerHealth.get();

                    // Вычисляем правильное базовое здоровье с учетом старых потерь
                    double migratedBaseHealth = startingHealth + oldModifier;

                    tag.putDouble(BASE_HEALTH_TAG, migratedBaseHealth);
                    // ВАЖНО: НЕ трогаем HEALTH_MODIFIER_TAG здесь! Оригинальный код тоже не будет,
                    // потому что BASE_HEALTH_TAG уже существует и if блок не выполнится

                    // Сохраняем изменения
                    player.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);

                    MediumcoreFix.LOGGER.info(
                        "Successfully migrated player '{}' health data: oldModifier={}, newBaseHealth={}",
                        player.getName().getString(),
                        oldModifier,
                        migratedBaseHealth
                    );
                }
            }
        }
    }
}
