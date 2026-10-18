# Thiết kế UI luồng game: Main Menu, Pause và Game Over

## Mục tiêu

Thiết kế lại các màn hình luồng game theo các mục C2 và C3 trong
[kế hoạch cải thiện UI/UX](../../ui-ux-improvement-plan.md), đồng thời bổ sung
Main Menu tối thiểu để các nút quay về menu có đích đến hoạt động được.
Giao diện cần nhất quán với các panel dark bo góc hiện có, giữ gameplay và
State Pattern hiện tại làm nền.

## Bối cảnh hiện tại

- `App` tạo `GameScene`, `UIManager`, tower selection và game loop bên trong
  `start`; game loop đang giữ trực tiếp cùng một `GameScene`.
- `GameScene` quản lý các phase gameplay `PlayingState`, `PausedState` và
  `GameOverState`.
- Nhấn Esc trong lúc chơi gọi `GameScene.togglePause()`. Khi game over, trạng
  thái terminal không resume bằng Esc.
- UI được vẽ bằng `GraphicsContext` qua `UIScreen`; `UIManager` render các
  screen theo thứ tự thêm vào và phân phối click từ screen trên cùng xuống.
- Pause và game-over overlay hiện tại chỉ là text đơn giản; chưa có Main Menu,
  restart flow hoặc thống kê theo từng ván.
- `GameState` là singleton chứa gold/lives; khi `GameScene` được tạo, gold và
  lives được reset.
- Enemy chết thật phát `DeathEvent` chứa reward. Enemy chạm goal không được
  phát reward death event.
- Chưa có wave system.

## Quyết định đã thống nhất

- Phạm vi chính là C2 Pause Menu redesign và C3 Game Over redesign.
- Có Main Menu tối thiểu gồm Play và Quit để hỗ trợ điều hướng từ Pause/Game
  Over. Không có Settings.
- Ứng dụng mở vào Main Menu. Play bắt đầu một ván mới.
- Restart tạo một `GameScene` mới, reset gold/lives và thống kê ván, đồng thời
  chọn tower mặc định.
- Game Over hiển thị số enemy bị hạ và gold kiếm được. Không hiển thị waves
  cleared vì chưa có wave system.
- Gold kiếm được là tổng reward nhận qua `DeathEvent` trong ván; không tính
  100 gold ban đầu và không phải số gold còn lại sau khi mua tower.
- Giữ HUD và tower-selection hiện có; không redesign các khu vực này.
- Giữ `GamePhase` cho gameplay phases. Main Menu là luồng cấp ứng dụng, không
  thêm vào `GamePhase`.

## Thiết kế

### Điều phối luồng ứng dụng

Tách điều phối giữa Main Menu và một ván chơi ra khỏi `GamePhase`. Một flow
controller cấp ứng dụng sở hữu trạng thái điều hướng và tham chiếu tới
`GameScene` hiện hành:

- Khi ở Main Menu, không chạy gameplay simulation.
- Play tạo session mới, khởi tạo `GameScene` và game screens.
- Khi game đang Playing, Esc chuyển phase sang Paused và hiển thị Pause Menu.
- Resume đóng Pause Menu và tiếp tục cùng session, không reset simulation.
- Restart từ Pause hoặc Game Over tạo session mới.
- Main Menu từ Pause hoặc Game Over kết thúc session hiện tại và trở về menu.
- Khi `GameScene` vào GameOver, flow hiển thị Game Over screen; Game Over vẫn
  terminal và Esc không tiếp tục gameplay.
- Quit đóng ứng dụng.

`App` tiếp tục là nơi chạy animation/render loop và chuyển input. Loop, renderer,
HUD, FPS và F3 debug panel tiếp tục hoạt động theo hành vi đã có trong session
game; khi không có session active, không gọi update gameplay.

### Main Menu

- Hiển thị title “TOWER DEFENSE”.
- Có hai nút: Play và Quit.
- Play bắt đầu ván mới và chọn tower mặc định.
- Quit đóng ứng dụng.
- Dùng nền tối và panel/nút bo góc theo phong cách các UI panel hiện tại.

### Pause Menu

- Là overlay modal ở giữa màn hình trên gameplay đã pause.
- Có các nút theo thứ tự: Resume, Restart, Main Menu.
- Resume là hành động chính, dùng màu accent xanh hiện tại.
- Restart tạo ván mới và reset tower selection về mặc định.
- Main Menu trở về Main Menu; session gameplay hiện tại không tiếp tục chạy.
- Click trong overlay được xử lý như modal, không truyền xuống tower selection
  hoặc thao tác đặt tháp.
- Esc khi đang pause tiếp tục resume như hiện tại.
- Debug panel vẫn nhận F3 khi pause.

### Game Over screen

- Hiện overlay modal khi `GameScene` vào `GameOverState`.
- Có tiêu đề “GAME OVER”.
- Hiển thị:
  - Enemies defeated trong ván.
  - Gold earned từ reward của enemy bị hạ trong ván.
- Không hiển thị wave count, vì game chưa có wave system.
- Có các nút Restart và Main Menu.
- Restart bắt đầu session mới; Main Menu kết thúc session hiện tại.
- Click trong overlay không truyền xuống gameplay.
- Esc trong GameOverState không resume và không thay đổi terminal state.

### Thống kê theo ván

Mỗi session có thống kê riêng, bắt đầu bằng zero:

- `enemiesDefeated` tăng một lần cho mỗi `DeathEvent` đại diện enemy chết.
- `goldEarned` tăng theo `DeathEvent.reward()`.
- Hai số liệu không dùng chung với gold hiện còn trong `GameState`; mua tower
  không làm giảm `goldEarned`.
- Khi Restart hoặc bắt đầu session mới, thống kê tạo lại từ zero.
- Goal death không tăng `enemiesDefeated` hoặc `goldEarned`.

Thực thi nên đặt thống kê theo session/`GameScene` thay vì biến toàn cục để
không rò dữ liệu qua lần chơi hoặc lần restart.

### Visual và tương tác

- Tái sử dụng phong cách dark background, rounded panels và màu xanh selection
  của UI hiện có.
- Pause Menu dùng xanh cho Resume; các hành động phụ ít nổi bật hơn.
- Game Over dùng điểm nhấn đỏ/cam vừa phải cho trạng thái kết thúc, giữ tương
  phản chữ rõ ràng.
- Main Menu, Pause Menu và Game Over dùng các vùng click rõ ràng, phù hợp
  `UIScreen.handleClick`.
- Các overlay modal phải chặn click xuống các screen gameplay.
- Không thêm animation chuyển cảnh trong phạm vi này.

## Kiểm thử và tiêu chí chấp nhận

### Kiểm thử tự động

- Test thống kê session:
  - bắt đầu từ zero;
  - tăng kill count và gold earned từ enemy death reward;
  - không tăng khi goal chết;
  - restart tạo thống kê mới.
- Test flow:
  - app flow bắt đầu ở Main Menu;
  - Play vào gameplay;
  - Esc mở Pause Menu và Resume trở về cùng session;
  - Restart tạo session mới, reset gold/lives/stats và tower selection;
  - Main Menu từ Pause/Game Over trở về menu;
  - Quit gọi hành động đóng ứng dụng;
  - Game Over không resume khi Esc.
- Test hit-test/callback của các nút, bao gồm modal click không lọt xuống
  gameplay.
- Chạy focused test tương ứng, sau đó chạy `mvn test`.

### Kiểm tra thủ công

- Khởi động app thấy Main Menu; Play bắt đầu game.
- Esc mở Pause Menu; Resume tiếp tục cùng ván.
- Restart reset enemy/map/session, gold/lives/stats và tower selection.
- Main Menu từ Pause ngừng session hiện tại; Play bắt đầu session mới.
- Để enemy chết, Game Over hiển thị đúng kill count và gold earned.
- Restart/Main Menu hoạt động từ Game Over; Esc ở Game Over không resume.
- Click overlay không đặt tháp xuyên xuống gameplay.
- F3 vẫn bật/tắt debug panel trong gameplay và pause.
- Quit đóng ứng dụng.

## Phạm vi loại trừ

- Không thêm Settings, Main Menu chỉ có Play/Quit.
- Không thêm wave system, wave statistics hoặc Victory screen.
- Không thay đổi cân bằng gameplay, AI enemy, đường đi hoặc luật đặt tháp.
- Không redesign HUD, tower selection, map/entity rendering.
- Không thêm save/load, xác nhận quit, xác nhận main menu, hoặc animation
  chuyển screen.
- Không đưa Main Menu vào `GamePhase` và không thay đổi trách nhiệm
  `GameState` về gold/lives.

## File dự kiến cần xem xét khi lập implementation plan

Danh sách chính xác sẽ được chốt trong implementation plan sau khi spec được
duyệt. Phạm vi dự kiến gồm:

- `src/main/java/com/towerdefense/App.java`
- `src/main/java/com/towerdefense/scene/GameScene.java`
- `src/main/java/com/towerdefense/game/` — flow controller và thống kê theo ván
- `src/main/java/com/towerdefense/ui/` — Main Menu, Pause Menu, Game Over screens
- `src/main/java/com/towerdefense/ui/UIManager.java` nếu cần composition APIs
- `src/test/java/com/towerdefense/` — test flow, statistics, transitions
- `src/test/java/com/towerdefense/ui/` — test hit-test/callbacks nếu phù hợp
- `docs/ui-ux-improvement-plan.md`, `docs/ui.md`, `docs/game.md`
