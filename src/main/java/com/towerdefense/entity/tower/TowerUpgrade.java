// package com.towerdefense.entity.tower;

// import com.towerdefense.behaviour.FireControl;
// import com.towerdefense.core.Behaviour;
// import com.towerdefense.core.ObjectPool;
// import com.towerdefense.entity.tower.fire.FireStrategy;
// import com.towerdefense.entity.tower.fire.SpreadShotDecorator;
// import com.towerdefense.game.GameState;

// public class TowerUpgrade extends Behaviour {

//     private static final int MAX_LEVEL = 3;

//     private final FireControl fireControl;
//     private final UpgradeType type;
//     private final FireStrategy baseStrategy;

//     private int level = 1;

//     public TowerUpgrade(
//             FireControl fireControl,
//             ObjectPool projectilePool,
//             UpgradeType type,
//             FireStrategy baseStrategy) {
//         this.fireControl = fireControl;
//         this.type = type;
//         this.baseStrategy = baseStrategy;
//     }

//     public int getLevel() {
//         return level;
//     }

//     public boolean isMaxLevel() {
//         return level >= MAX_LEVEL;
//     }

//     public int getUpgradeCost() {
//         return level * 50;
//     }

//     public boolean tryUpgrade(GameState gameState) {
//         if (isMaxLevel())
//             return false;

//         int cost = getUpgradeCost();
//         if (!gameState.spendGold(cost))
//             return false;

//         level++;
//         applyUpgrade();
//         return true;
//     }

//     private void applyUpgrade() {
//         FireStrategy upgraded = switch (type) {
//             case BASIC -> baseStrategy; // Basic không có decorator, chỉ số liệu gốc đã đủ (nâng cấp qua fireInterval
//                                         // bên dưới)
//             case SPREAD -> new SpreadShotDecorator(baseStrategy, level - 1, 15, 300, 15);
//             case SLOW -> new SlowEffectDecorator(baseStrategy, 0.3 * level, 1.5);
//         };

//         fireControl.setFireStrategy(upgraded);
//         fireControl.setFireInterval(0.5 - (level - 1) * 0.1);
//     }
// }