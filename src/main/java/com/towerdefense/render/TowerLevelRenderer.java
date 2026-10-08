// package com.towerdefense.render;

// import com.towerdefense.core.Vector2;
// import com.towerdefense.entity.tower.TowerUpgrade;
// import javafx.scene.canvas.GraphicsContext;
// import javafx.scene.paint.Color;
// import javafx.scene.text.Font;
// import javafx.scene.text.FontWeight;
// import javafx.geometry.VPos;
// import javafx.scene.text.TextAlignment;

// public class TowerLevelRenderer extends RenderBehaviour {

//     private final Color color;
//     private final double size;
//     private TowerUpgrade upgrade;

//     public TowerLevelRenderer(Color color, double size) {
//         this.color = color;
//         this.size = size;
//     }

//     public void setUpgrade(TowerUpgrade upgrade) {
//         this.upgrade = upgrade;
//     }

//     @Override
//     public void render(GraphicsContext gc) {
//         Vector2 pos = gameObject.getTransform().getPosition();
//         double x = pos.x();
//         double y = pos.y();

//         gc.setFill(color);
//         gc.fillRect(x - size / 2, y - size / 2, size, size);

//         gc.setStroke(Color.BLACK);
//         gc.setLineWidth(1);
//         gc.strokeRect(x - size / 2, y - size / 2, size, size);

//         String label = toRoman(upgrade != null ? upgrade.getLevel() : 1);

//         gc.setFill(Color.WHITE);
//         gc.setFont(Font.font("Arial", FontWeight.BOLD, size * 0.4));
//         gc.setTextAlign(TextAlignment.CENTER);
//         gc.setTextBaseline(VPos.CENTER);
//         gc.fillText(label, x, y);
//     }

//     private String toRoman(int level) {
//         return switch (level) {
//             case 1 -> "I";
//             case 2 -> "II";
//             case 3 -> "III";
//             default -> String.valueOf(level);
//         };
//     }
// }