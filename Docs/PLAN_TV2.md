# KẾ HOẠCH CHI TIẾT DÀNH CHO THÀNH VIÊN 2 (TV2)

> **Vai trò**: Đăng nhập/Đăng ký + Toàn bộ chức năng Quản lý phía Admin (DS trái cây, Thêm/Sửa/Xóa trái cây, Quản lý đơn hàng).
> **Package chính**: `com.example.fruittest`
> **Điều kiện bắt đầu**: Nhận code nền tảng (P0) từ TV1 trên nhánh `main`.

---

## BẢNG TÓM TẮT MỤC TIÊU & FILE SỞ HỮU

| STT | Chức năng | File sở hữu / Cần chỉnh sửa |
|---|---|---|
| **2.1** | Màn hình Đăng nhập (Login) | `ui/auth/LoginActivity.kt`, `res/layout/activity_login.xml` |
| **2.2** | Màn hình Đăng ký (Register) | `ui/auth/RegisterActivity.kt`, `res/layout/activity_register.xml` |
| **2.3** | Tiện ích Logout chung | `ui/auth/AuthExt.kt` |
| **2.4** | Màn hình chính Admin (Danh sách + Tìm kiếm) | `ui/admin/AdminMainActivity.kt`, `res/layout/activity_admin_main.xml` |
| **2.5** | Adapter Trái cây Admin & Badge HSD | `ui/admin/AdminFruitAdapter.kt`, `res/layout/item_fruit_admin.xml`, `res/values/colors_admin.xml` |
| **2.6** | Form Thêm / Sửa trái cây | `ui/admin/FruitFormActivity.kt`, `res/layout/activity_fruit_form.xml` |
| **2.7** | Màn hình Quản lý đơn hàng Admin | `ui/admin/AdminOrderActivity.kt`, `AdminOrderAdapter.kt`, `res/layout/activity_admin_order.xml`, `res/layout/item_order_admin.xml`, `res/values/colors_order.xml` |

---

## CHI TIẾT CÁC BƯỚC THỰC HIỆN

### BƯỚC 1: ĐĂNG NHẬP, ĐĂNG KÝ & THOÁT (AUTH)

#### Step 1.1: `LoginActivity.kt` & `activity_login.xml`
* **Giao diện**:
  * EditText cho Username, Password.
  * CheckBox "Remember Me".
  * Nút "Đăng nhập", TextView "Đăng ký tài khoản mới".
* **Xử lý Logic**:
  * Khi mở Activity: Kiểm tra `SessionManager.getRememberUser()` và `getRememberPass()`. Nếu có -> tự điền vào EditText và tích CheckBox.
  * Khi bấm "Đăng nhập":
    1. Validate không để rỗng Username/Password.
    2. Gọi `HashUtil.sha256(password)` để mã hóa mật khẩu.
    3. Gọi `AccountDao.login(username, passwordHash)`.
    4. Nếu trả về `null`: Toast thông báo sai tài khoản/mật khẩu.
    5. Nếu trả về `Account`:
       * Nếu checkbox tích: `SessionManager.saveRemember(username, password)`. Nếu không: `SessionManager.clearRemember()`.
       * Nếu `role == "ADMIN"`: Gọi `SessionManager.saveSession(account.id, username, "ADMIN", null)`, điều hướng sang `AdminMainActivity`.
       * Nếu `role == "CUSTOMER"`: Query `CustomerDao.findByAccountId(account.id)` lấy `customerId`, gọi `SessionManager.saveSession(account.id, username, "CUSTOMER", customer.id)`, điều hướng sang `CustomerMainActivity`.
       * Gọi `finish()` để không quay lại màn Login khi bấm Back.

---

#### Step 1.2: `RegisterActivity.kt` & `activity_register.xml`
* **Giao diện**:
  * EditText: Username, Password, Mật khẩu nhập lại, Họ tên, Số điện thoại, Email.
  * Nút "Đăng ký", TextView "Đã có tài khoản? Đăng nhập".
* **Xử lý Logic**:
  * Validate: Không rỗng, mật khẩu trùng khớp, định dạng Email/SĐT hợp lệ.
  * Gọi `AccountDao.findByUsername(username)` -> Nếu tồn tại báo lỗi "Tên đăng nhập đã được sử dụng".
  * Nếu hợp lệ:
    1. Tạo `Account(username = username, password = HashUtil.sha256(password), role = Constants.ROLE_CUSTOMER)`.
    2. Gọi `AccountDao.insert(account)` lấy `accountId`.
    3. Tạo `Customer(fullname = fullname, phone = phone, email = email, accountId = accountId)`.
    4. Gọi `CustomerDao.insert(customer)`.
    5. Toast "Đăng ký thành công", gọi `finish()` quay lại `LoginActivity`.

---

#### Step 1.3: `AuthExt.kt` (Logout dùng chung)
* Tạo Extension function trong `ui/auth/AuthExt.kt`:
```kotlin
fun Activity.logout() {
    SessionManager(this).logout()
    val intent = Intent(this, LoginActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    startActivity(intent)
    finish()
}
```

---

### BƯỚC 2: QUẢN LÝ TRÁI CÂY PHÍA ADMIN

#### Step 2.1: `AdminMainActivity.kt` & `activity_admin_main.xml`
* **Giao diện**:
  * EditText Tìm kiếm (theo tên/loại).
  * RecyclerView danh sách trái cây.
  * FloatingActionButton (FAB) "+" để thêm mới trái cây.
  * Options Menu: *Quản lý đơn hàng*, *Đăng xuất*.
* **Xử lý Logic**:
  * Trong `onResume()`: Tải lại danh sách bằng `FruitDao.getAll()` và đẩy vào `AdminFruitAdapter`.
  * Ô tìm kiếm: Dùng `doAfterTextChanged` gọi `FruitDao.search(keyword)` để lọc danh sách realtime.
  * FAB "+": Mở `FruitFormActivity` (không truyền extra).
  * Menu *Quản lý đơn hàng*: Mở `AdminOrderActivity`.
  * Menu *Đăng xuất*: Gọi `logout()`.

---

#### Step 2.2: `AdminFruitAdapter.kt` & `item_fruit_admin.xml`
* **Giao diện Item**:
  * Tên trái cây, Loại, Giá, Số lượng tồn kho, Hạn sử dụng.
  * Badge trạng thái HSD:
    * `Expired` (Màu đỏ): Nếu `DateUtil.isExpired(expiryDate)`.
    * `Expiring soon` (Màu cam): Nếu `DateUtil.isExpiringSoon(expiryDate)`.
  * Nút "Sửa" (Icon/Button) và Nút "Xóa" (Icon/Button).
* **Xử lý Event**:
  * Nút "Sửa": Mở `FruitFormActivity` truyền extra `EXTRA_FRUIT_ID = fruit.id`.
  * Nút "Xóa": Mở `AlertDialog` xác nhận "Bạn có chắc muốn xóa trái cây này?". Nút "Đồng ý" gọi `FruitDao.delete(fruit.id)` và cập nhật lại danh sách.

---

#### Step 2.3: `FruitFormActivity.kt` & `activity_fruit_form.xml`
* **Giao diện**:
  * Input: Tên, Loại, Giá, Số lượng, Hạn sử dụng, Mô tả.
  * Ô HSD có icon Lịch / Click để bật `DatePickerDialog` chọn ngày (`yyyy-MM-dd`).
  * Nút "Lưu".
* **Xử lý Logic**:
  * Lấy `fruitId = intent.getIntExtra("fruit_id", -1)`.
  * Nếu `fruitId != -1`: Màn hình **Sửa** -> Gọi `FruitDao.getById(fruitId)` hiển thị dữ liệu lên các ô input.
  * Nếu `fruitId == -1`: Màn hình **Thêm mới**.
  * Validate: Tên không rỗng, Giá > 0, Số lượng >= 0, HSD không rỗng.
  * Bấm "Lưu":
    * Chế độ Thêm: Tạo `Fruit` mới -> `FruitDao.insert(fruit)`.
    * Chế độ Sửa: Tạo `Fruit` với ID cũ -> `FruitDao.update(fruit)`.
    * Toast thông báo thành công và gọi `finish()`.

---

### BƯỚC 3: QUẢN LÝ ĐƠN HÀNG PHÍA ADMIN

#### Step 3.1: `AdminOrderActivity.kt`, `AdminOrderAdapter.kt` & Layouts
* **Giao diện Activity & Item**:
  * RecyclerView hiển thị danh sách tất cả đơn hàng.
  * Item gồm: Mã đơn (#ID), ID Khách hàng, Ngày đặt, Tổng tiền, Badge Trạng thái.
  * Nút hành động chuyển trạng thái ("Xác nhận đơn" / "Hoàn thành").
* **Xử lý Logic**:
  * Tải danh sách đơn hàng từ `OrderDao.getAll()`.
  * Hiển thị màu Badge theo trạng thái:
    * `Pending`: Màu Vàng. Nút bấm ghi: "Xác nhận đơn".
    * `Confirmed`: Màu Xanh Dương. Nút bấm ghi: "Hoàn thành đơn".
    * `Completed`: Màu Xanh Lá. Ẩn nút hành động.
  * Khi bấm nút hành động:
    * Nếu trạng thái hiện tại = `Pending` -> Gọi `OrderDao.updateStatus(id, Constants.STATUS_CONFIRMED)`.
    * Nếu trạng thái hiện tại = `Confirmed` -> Gọi `OrderDao.updateStatus(id, Constants.STATUS_COMPLETED)`.
    * Tải lại danh sách đơn để cập nhật UI.

---

## QUY TRÌNH NỘP CODE (GIT)
1. Tạo nhánh `feature/auth` từ `main`: Thực hiện màn Login & Register -> Push & tạo PR.
2. Tạo nhánh `feature/admin-fruit` từ `main`: Thực hiện Màn hình Admin Fruit List, Adapter & Form -> Push & tạo PR.
3. Tạo nhánh `feature/admin-order` từ `main`: Thực hiện Màn hình Admin Order -> Push & tạo PR.
