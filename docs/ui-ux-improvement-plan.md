# Kế hoạch cải thiện UI/UX — Tower Defense

> **Mục đích file này**: Bàn bạc & chốt ý tưởng. Khi hai bên đồng ý hết, lấy file này làm spec để code.

---

## 1. Phân tích hiện trạng

### Render hiện tại
| Thành phần | Cách vẽ hiện tại | Vấn đề |
|---|---|---|
| **Map** | `fillRect` đơn sắc (SANDYBROWN / FORESTGREEN / DIMGRAY) | Phẳng, không có texture, không phân biệt rõ ô xây được |
| **Tower** | `DiscRenderer` — hình tròn viền + fill mờ | Không phân biệt rõ loại tower, chỉ khác màu |
| **Enemy** | `DiscRenderer` — hình tròn nhỏ hơn | Tất cả enemy trông giống nhau, chỉ khác size |
| **Projectile** | `DiscRenderer` solid nhỏ | OK cho prototype, nhưng đơn điệu |
| **Health bar** | Thanh ngang DARKRED/LIMEGREEN | Quá cơ bản, không có transition |
| **Explosion** | Vòng tròn cam mờ dần | Chấp nhận được nhưng có thể đẹp hơn |

### UI hiện tại
| Thành phần | Mô tả | Vấn đề |
|---|---|---|
| **HUD (góc trái trên)** | Rect đen 70% alpha, text trắng: Gold / Tower / Base HP | Không có icon, font mặc định, layout cứng |
| **Tower Selection (góc phải)** | Panel dark, list button chọn tower + giá | Không có icon tower, không mô tả tower, không tooltip |
| **Pause overlay** | Fullscreen đen + text "PAUSED" | Thiếu menu (Resume/Restart/Quit) |
| **Game Over overlay** | Fullscreen đen + text "GAME OVER" | Không có nút Restart, không có thống kê |
| **FPS overlay** | Góc phải trên, nhỏ | OK |
| **Debug panel** | F3 toggle, góc phải dưới | OK — chỉ dùng dev |

### Thiếu hoàn toàn
- ❌ Main Menu screen
- ❌ Wave system UI (số wave, countdown)
- ❌ Tower info panel khi click vào tower đã đặt (xem stats, bán tower)
- ❌ Hover effect khi di chuột qua ô xây được
- ❌ Range indicator khi chọn tower / hover tower
- ❌ Sound effects & background music
- ❌ Animation khi đặt tower, khi enemy chết
- ❌ Particle system

---

## 2. Đề xuất cải thiện

### 🎨 A. Visual — Map & Entity rendering

| # | Đề xuất | Mức ưu tiên | Trạng thái |
|---|---|---|---|
| A1 | **Map tile có pattern/texture**: grid lines nhẹ cho ô xây được, vệt đường cho PATH, đá cho BLOCKED | 🔴 Cao | 🟡 Đã code thử nghiệm |
| A2 | **Tower vẽ bằng hình vuông/polygon** thay vì tròn, mỗi loại có shape riêng (basic=vuông, spread=ngôi sao, slow=lục giác, explosive=kim cương) | 🟡 TB | 🟡 Đã code thử nghiệm (Ụ súng Sci-Fi xoay nòng theo quái) |
| A3 | **Enemy sprite**: Vẽ các hình dạng khác nhau cho mỗi loại enemy (Grunt=drone 4 cánh, Scout=phi thuyền tam giác, Tank=thiết giáp bát giác) | 🟡 TB | 🟡 Đã code thử nghiệm |
| A4 | **Projectile trail effect**: vệt sáng theo đạn | 🟢 Thấp | ⬜ Chưa bàn |
| A5 | **Health bar cải tiến**: gradient đỏ→vàng→xanh theo %, bo tròn, viền mỏng | 🔴 Cao | 🟡 Đã code thử nghiệm |
| A6 | **Hover highlight ô xây được**: viền sáng hoặc fill nhẹ khi chuột di qua ô buildable | 🔴 Cao | 🟡 Đã code thử nghiệm |
| A7 | **Range indicator**: vòng tròn range khi hover/chọn tower | 🔴 Cao | 🟡 Đã code thử nghiệm |
| A8 | **Death animation**: enemy fade out hoặc vỡ ra khi chết | 🟡 TB | ⬜ Chưa bàn |
| A9 | **Tower placement animation**: hiệu ứng "rơi xuống" hoặc scale up khi đặt tower | 🟢 Thấp | ⬜ Chưa bàn |

### 🖥️ B. HUD & Panels

| # | Đề xuất | Mức ưu tiên | Trạng thái |
| B1 | **Redesign HUD**: panel góc trái bo tròn đồng bộ thiết kế với bảng Choose Tower | 🔴 Cao | 🟡 Đã code thử nghiệm |
| B2 | **Tower Selection sidebar**: panel bên phải cố định, mỗi tower có icon + tên + giá, chuyển xuống góc dưới-phải tránh che ụ súng | 🔴 Cao | 🟡 Đã code thử nghiệm |
| B3 | **Tower Info popup**: click vào tower đã đặt → hiện panel stats (damage, range, fire rate) + nút Sell + nút Upgrade | 🟡 TB | ⬜ Chưa bàn |
| B4 | **Tooltip on hover**: hover lên tower trong selection → hiện tooltip chi tiết | 🟢 Thấp | ⬜ Chưa bàn |
| B5 | **Gold change animation**: +10 💰 popup khi enemy chết, -25 💰 khi mua tower | 🟡 TB | ⬜ Chưa bàn |

### 📋 C. Game Flow Screens

| # | Đề xuất | Mức ưu tiên | Trạng thái |
|---|---|---|---|
| C1 | **Main Menu**: background + title "TOWER DEFENSE" + nút Play, Quit (không có Settings) | 🔴 Cao | ✅ Đã triển khai; tương tác runtime chưa xác nhận |
| C2 | **Pause Menu redesign**: overlay + 3 nút: Resume / Restart / Main Menu | 🔴 Cao | ✅ Đã triển khai; tương tác runtime chưa xác nhận |
| C3 | **Game Over screen redesign**: hiện enemies defeated và gold earned (không có waves cleared) + nút Restart / Main Menu | 🔴 Cao | ✅ Đã triển khai; tương tác runtime chưa xác nhận |
| C4 | **Victory screen**: khi clear tất cả wave | 🟡 TB | ⬜ Chưa bàn |

### 🌊 D. Wave System UI

| # | Đề xuất | Mức ưu tiên | Trạng thái |
|---|---|---|---|
| D1 | **Wave counter trên HUD**: "Wave 3/10" | 🔴 Cao | ⬜ Chưa bàn |
| D2 | **Next wave button**: chủ động bắt đầu wave tiếp (thay vì auto spawn liên tục) | 🔴 Cao | ⬜ Chưa bàn |
| D3 | **Wave preview**: hiện loại enemy sắp tới trong wave tiếp theo | 🟢 Thấp | ⬜ Chưa bàn |
| D4 | **Wave transition animation**: text lớn "WAVE 3" fade in/out giữa màn hình | 🟡 TB | ⬜ Chưa bàn |

### 🎵 E. Polish & Feel

| # | Đề xuất | Mức ưu tiên | Trạng thái |
|---|---|---|---|
| E1 | **Color palette thống nhất**: định nghĩa bảng màu chung cho toàn game (dark theme) | 🔴 Cao | ⬜ Chưa bàn |
| E2 | **Font tốt hơn**: dùng font pixel art hoặc font game chuyên dụng thay vì system default | 🟡 TB | ⬜ Chưa bàn |
| E3 | **Button hover/press effect**: đổi màu khi hover, animation nhẹ khi press | 🔴 Cao | ⬜ Chưa bàn |
| E4 | **Smooth transitions**: fade in/out giữa các screen | 🟢 Thấp | ⬜ Chưa bàn |
| E5 | **Camera shake nhẹ** khi base bị đánh | 🟢 Thấp | ⬜ Chưa bàn |

---

## 3. Thứ tự triển khai và tiến độ

**Phase 1 — Nền tảng UI** (làm trước, ảnh hưởng tất cả)
- E1: Color palette
- E3: Button component (hover/press)
- B1: HUD redesign
- B2: Tower Selection sidebar

**Phase 2 — Game Flow**
- C1–C3: Main Menu, Pause Menu và Game Over — đã triển khai; tương tác runtime chưa được xác nhận.
- D1 + D2: Wave system UI cơ bản — chưa triển khai.

**Phase 3 — Visual Polish**
- A1: Map texture
- A5: Health bar
- A6: Hover highlight ô
- A7: Range indicator

**Phase 4 — Extra Polish**
- A2, A3: Tower/Enemy shapes
- B3: Tower info popup
- B5: Gold animation
- D4: Wave transition
- A4, A8, A9: Trail, death anim, placement anim

---

## 4. Thảo luận

> Bạn comment ý kiến ở đây, mình sẽ cập nhật bảng trên.

### Câu hỏi mở:
1. **Bạn muốn giữ art style nào?** Pure geometric (vẽ shape bằng code) hay muốn chuyển sang dùng sprite/image?
2. **Wave system**: hiện tại enemy spawn liên tục (mỗi 1.6s). Bạn muốn chuyển sang hệ wave (có nút Next Wave) hay giữ auto-spawn?
3. **Có muốn thêm âm thanh không?** Hay chỉ tập trung visual?
4. **Mức độ cải thiện map**: chỉ cần grid lines + phân biệt rõ hơn, hay muốn texture phức tạp hơn?
5. **Tower info panel khi click tower**: có cần thiết không, hay chỉ cần Selection panel là đủ?

---

## 5. Kết quả triển khai C1–C3

### Phạm vi và hành vi đã thống nhất

- Main Menu là màn hình đầu tiên, chỉ có Play và Quit; không có Settings.
- Pause Menu có Resume, Restart và Main Menu.
- Game Over hiển thị số enemy bị hạ và tổng gold kiếm được trong ván, cùng Restart và Main Menu. Không hiển thị wave vì game chưa có wave system.
- Restart tạo một ván mới, reset gold/lives/thống kê và chọn tower mặc định. Resume tiếp tục đúng ván đang chơi.
- `GamePhase` chỉ quản lý các phase gameplay; luồng Main Menu và điều hướng màn hình do `GameFlowController` cấp ứng dụng quản lý.
- Pause và Game Over là modal, chặn click xuyên xuống gameplay; F3 tiếp tục hoạt động khi đang chơi hoặc pause.

### Kết quả và kiểm tra

- Đã triển khai màn hình, điều hướng và thống kê theo ván; giữ nguyên HUD và tower-selection hiện có.
- Bộ kiểm thử Maven gần nhất: **57 tests, 0 failures, 0 errors, 0 skipped**.
- Chưa xác nhận thủ công các tương tác của giao diện mới trong lúc chạy game. Các mục C1–C3 chỉ được coi là đã kiểm tra runtime sau khi thử trực tiếp trong ứng dụng.

---

*Cập nhật lần cuối: 2026-10-18*
