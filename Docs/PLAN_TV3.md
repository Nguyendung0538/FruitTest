# KẾ HOẠCH CHI TIẾT DÀNH CHO THÀNH VIÊN 3 (TV3)

> **Vai trò**: Phụ trách toàn bộ Chức năng phía Customer (Xem danh sách sản phẩm còn hàng, Tìm kiếm, Xem chi tiết & HSD, Đặt hàng, Xem lịch sử đơn hàng).
> **Package chính**: `com.example.fruittest`
> **Điều kiện bắt đầu**: Nhận code nền tảng (P0) từ TV1 trên nhánh `main`.

---

## BẢNG TÓM TẮT MỤC TIÊU & FILE SỞ HỮU

| STT | Chức năng | File sở hữu / Cần chỉnh sửa |
|---|---|---|
| **3.1** | Màn hình chính Customer (Danh sách sản phẩm) | `ui/customer/CustomerMainActivity.kt`, `res/layout/activity_customer_main.xml` |
| **3.2** | Adapter Trái cây Customer & Badge HSD | `ui/customer/CustomerFruitAdapter.kt`, `res/layout/item_fruit_customer.xml` |
| **3.3** | Màn hình Chi tiết trái cây & Đặt hàng | `ui/customer/FruitDetailActivity.kt`, `res/layout/activity_fruit_detail.xml` |
| **3.4** | Màn hình Lịch sử đơn hàng | `ui/customer/OrderHistoryActivity.kt`, `OrderHistoryAdapter.kt`, `res/layout/activity_order_history.xml`, `res/layout/item_order_history.xml`, `res/values/colors_customer.xml` |

---

## CHI TIẾT CÁC BƯỚC THỰC HIỆN

### BƯỚC 1: MÀN HÌNH CHÍNH CUSTOMER (DANH SÁCH & TÌM KIẾM)

#### Step 1.1: `CustomerMainActivity.kt` & `activity_customer_main.xml`
* **Giao diện**:
  * EditText Tìm kiếm trái cây (theo tên/loại).
  * RecyclerView hiển thị danh sách trái cây khả dụng (`quantity > 0`).
  * Options Menu: *Lịch sử đơn hàng*, *Đăng xuất*.
* **Xử lý Logic**:
  * Trong `onResume()`: Gọi `FruitDao.getAvailable()` để lấy danh sách trái cây còn hàng và đẩy vào `CustomerFruitAdapter`.
  * Ô tìm kiếm: Dùng `doAfterTextChanged` gọi `FruitDao.searchAvailable(keyword)` để tìm kiếm realtime.
  * Menu *Lịch sử đơn hàng*: Mở `OrderHistoryActivity`.
  * Menu *Đăng xuất*: Gọi `logout()` (dùng hàm tiện ích từ `AuthExt.kt`).

---

#### Step 1.2: `CustomerFruitAdapter.kt` & `item_fruit_customer.xml`
* **Giao diện Item**:
  * Tên trái cây, Giá bán, Tồn kho, Hạn sử dụng.
  * Badge HSD:
    * `Expired` (Badge Đỏ): Nếu `DateUtil.isExpired(expiryDate)`.
    * `Expiring soon` (Badge Cam): Nếu `DateUtil.isExpiringSoon(expiryDate)`.
  * Nếu sản phẩm đã hết hạn (`isExpired == true`): Làm mờ item (set opacity / alpha = 0.5f) và hiển thị Badge "Đã hết hạn".
* **Xử lý Event**:
  * Click vào Item -> Mở `FruitDetailActivity` truyền extra `EXTRA_FRUIT_ID = fruit.id`.

---

### BƯỚC 2: MÀN HÌNH CHI TIẾT SẢN PHẨM & ĐẶT HÀNG

#### Step 2.1: `FruitDetailActivity.kt` & `activity_fruit_detail.xml`
* **Giao diện**:
  * Hiển thị đầy đủ thông tin trái cây: Tên, Loại, Giá, Tồn kho, Hạn sử dụng, Mô tả.
  * Badge cảnh báo nếu hết hạn hoặc sắp hết hạn.
  * EditText / NumberPicker nhập Số lượng muốn mua (mặc định = 1).
  * TextView Hiển thị **Tổng tiền** tạm tính realtime (`Giá x Số lượng`).
  * Nút "Đặt hàng".
* **Xử lý Logic**:
  * Lấy `fruitId = intent.getIntExtra("fruit_id", -1)`. Lấy thông tin qua `FruitDao.getById(fruitId)`.
  * Lắng nghe sự thay đổi của ô nhập Số lượng -> Tính lại Tổng tiền hiển thị trên UI.
  * **Kiểm tra điều kiện đặt hàng**:
    * Nếu sản phẩm đã hết hạn (`DateUtil.isExpired(expiryDate)`): Disable nút "Đặt hàng", hiển thị thông báo "Sản phẩm đã hết hạn, không thể đặt hàng".
    * Nếu tồn kho = 0: Disable nút "Đặt hàng".
  * **Bấm "Đặt hàng"**:
    1. Lấy `customerId` từ `SessionManager(this).getCustomerId()`.
    2. Validate số lượng nhập: `1 <= quantity <= fruit.quantity`.
    3. Gọi `OrderDao.placeOrder(customerId, fruit.id, quantity)`.
    4. Bắt ngoại lệ:
       * `IllegalStateException("EXPIRED")`: Toast "Sản phẩm đã hết hạn!".
       * `IllegalStateException("OUT_OF_STOCK")`: Toast "Số lượng tồn kho không đủ!".
       * `IllegalStateException("INVALID_QTY")`: Toast "Số lượng đặt không hợp lệ!".
    5. Nếu thành công: Toast "Đặt hàng thành công!", gọi `finish()` quay lại màn hình chính.

---

### BƯỚC 3: MÀN HÌNH LỊCH SỬ ĐƠN HÀNG

#### Step 3.1: `OrderHistoryActivity.kt`, `OrderHistoryAdapter.kt` & Layouts
* **Giao diện Activity & Item (`item_order_history.xml`)**:
  * RecyclerView danh sách các đơn hàng của Customer đang đăng nhập.
  * Item gồm: Mã đơn hàng (#ID), Ngày đặt hàng, Tổng tiền, Trạng thái đơn hàng.
  * Màu sắc Badge trạng thái (`res/values/colors_customer.xml`):
    * `Pending`: Màu Vàng.
    * `Confirmed`: Màu Xanh Dương.
    * `Completed`: Màu Xanh Lá.
* **Xử lý Logic**:
  * Lấy `customerId = SessionManager(this).getCustomerId()`.
  * Tải danh sách đơn hàng qua `OrderDao.getByCustomer(customerId)`.
  * Đẩy danh sách vào `OrderHistoryAdapter` để hiển thị.

---

## QUY TRÌNH NỘP CODE (GIT)
1. Tạo nhánh `feature/customer` từ `main` mới nhất (sau khi TV1 đã hoàn thành P0).
2. Viết toàn bộ code tính năng phía Customer trên nhánh `feature/customer`.
3. Test kỹ luồng Đặt hàng & Lịch sử đơn hàng.
4. Push nhánh `feature/customer` lên GitHub và tạo Pull Request (PR) gửi cho TV1 review.
