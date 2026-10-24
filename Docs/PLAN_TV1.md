# KẾ HOẠCH CHI TIẾT DÀNH CHO THÀNH VIÊN 1 (TV1 - LEADER)

> **Vai trò**: Leader · Nền tảng Core/Data/Util + Màn hình Stubs + Review & Tích hợp.
> **Package chính**: `com.example.fruittest`
> **Mục tiêu**: Hoàn thành nền tảng (P0) trong ~45-50 phút đầu tiên để TV2 và TV3 có thể clone và làm việc song song không bị nghẽn hay xung đột.

---

## BẢNG TÓM TẮT MỤC TIÊU & FILE SỞ HỮU

| Giai đoạn | Hạng mục công việc | File sở hữu / Cần tạo |
|---|---|---|
| **P0: Setup** | Cấu hình Gradle, ViewBinding | `app/build.gradle.kts` |
| **P0: Data** | Models & SQLite DatabaseHelper + Seed | `data/model/*.kt`, `data/DatabaseHelper.kt` |
| **P0: DAO** | 4 DAO Contract (Account, Customer, Fruit, Order) | `data/dao/*.kt` |
| **P0: Util** | Constants, Hash, Date, SessionManager | `util/*.kt` |
| **P0: Stubs** | Tạo Activity stub + Đăng ký Manifest | `AndroidManifest.xml`, mọi Activity stub |
| **P2: Merge** | Review PR, tích hợp & test chéo | Quản lý repository GitHub |

---

## CHI TIẾT CÁC BƯỚC THỰC HIỆN

### PHẦN 0: KHỞI TẠO NỀN TẢNG (P0: 0:00 – 0:50)

#### Step 0.1: Cấu hình Build & ViewBinding
* **File**: `app/build.gradle.kts`
* **Nhiệm vụ**:
  * Kiểm tra `namespace = "com.example.fruittest"` và `applicationId = "com.example.fruittest"`.
  * Thêm `buildFeatures { viewBinding = true }`.
  * Khai báo dependency Material Components & RecyclerView nếu chưa có.

```kotlin
android {
    namespace = "com.example.fruittest"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.fruittest"
        minSdk = 24
        targetSdk = 35
        ...
    }

    buildFeatures {
        viewBinding = true
    }
}
```

---

#### Step 0.2: Các Util dùng chung (`util/`)
* **`util/Constants.kt`**:
  * `ROLE_ADMIN = "ADMIN"`, `ROLE_CUSTOMER = "CUSTOMER"`
  * `STATUS_PENDING = "Pending"`, `STATUS_CONFIRMED = "Confirmed"`, `STATUS_COMPLETED = "Completed"`
* **`util/HashUtil.kt`**:
  * Hàm `sha256(input: String): String` mã hóa mật khẩu.
* **`util/DateUtil.kt`**:
  * Định dạng ngày: `yyyy-MM-dd` (cho HSD) và `yyyy-MM-dd HH:mm` (cho đơn hàng).
  * `today(): String`, `now(): String`.
  * `isExpired(expiryDate: String): Boolean` (`expiryDate < today()`).
  * `isExpiringSoon(expiryDate: String): Boolean` (`today() <= expiryDate && expiryDate <= today + 3 days`). Dùng `Calendar` để cộng 3 ngày.
* **`util/SessionManager.kt`**:
  * Dùng `SharedPreferences` (`fruit_shop_session`).
  * `saveSession(id: Int, username: String, role: String, customerId: Int? = null)` *(Lưu ý: customerId nullable vì Admin không có customerId)*.
  * `isLoggedIn(): Boolean`, `getRole(): String?`, `getCustomerId(): Int`.
  * `saveRemember(username: String, pass: String)`, `clearRemember()`, `getRememberUser()`, `getRememberPass()`.
  * `logout()`.

---

#### Step 0.3: Data Models (`data/model/`)
Tạo 4 `data class`:
* `Account(val id: Int = 0, val username: String, val password: String, val role: String)`
* `Fruit(val id: Int = 0, val name: String, val category: String, val price: Double, val quantity: Int, val expiryDate: String, val description: String)`
* `Customer(val id: Int = 0, val fullname: String, val phone: String, val email: String, val accountId: Int)`
* `Order(val id: Int = 0, val customerId: Int, val orderDate: String, val totalAmount: Double, val status: String)`

---

#### Step 0.4: DatabaseHelper & Seed Data (`data/DatabaseHelper.kt`)
* Inherit `SQLiteOpenHelper(context, "fruit_shop.db", null, 1)`.
* `onConfigure(db)`: Bật `db.setForeignKeyConstraintsEnabled(true)`.
* `onCreate(db)`: Tạo 4 bảng theo schema SPEC:
  ```sql
  CREATE TABLE Account (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL UNIQUE, password TEXT NOT NULL, role TEXT NOT NULL);
  CREATE TABLE Customer (id INTEGER PRIMARY KEY AUTOINCREMENT, fullname TEXT NOT NULL, phone TEXT, email TEXT, accountId INTEGER NOT NULL REFERENCES Account(id) ON DELETE CASCADE);
  CREATE TABLE Fruit (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, category TEXT, price REAL NOT NULL, quantity INTEGER NOT NULL DEFAULT 0, expiryDate TEXT NOT NULL, description TEXT);
  CREATE TABLE Orders (id INTEGER PRIMARY KEY AUTOINCREMENT, customerId INTEGER NOT NULL REFERENCES Customer(id), orderDate TEXT NOT NULL, totalAmount REAL NOT NULL, status TEXT NOT NULL DEFAULT 'Pending');
  ```
* Seed Data (chèn cuối `onCreate`):
  * **Admin**: `admin` / `admin123` (SHA-256).
  * **Customer**: `customer` / `123456` (SHA-256) + 1 record `Customer` (Họ tên: Nguyễn Văn A, SĐT: 0901234567).
  * **Fruit**: ≥ 6 món (Táo Envy, Cam Sành, Chuối Laba, Dưa Hấu, Nho Mẫu Đơn, Mango):
    * 3 loại bình thường.
    * 1 loại sắp hết hạn (HSD = hôm nay + 2 ngày).
    * 1 loại đã hết hạn (HSD = hôm nay - 2 ngày).
    * 1 loại hết hàng (quantity = 0).

---

#### Step 0.5: DAOs (`data/dao/`)
Tạo 4 DAO nhận vào `Context` và dùng `DatabaseHelper.getInstance(context)`:

1. **`AccountDao`**:
   * `insert(account: Account): Long`
   * `findByUsername(username: String): Account?`
   * `login(username: String, passwordHash: String): Account?`
2. **`CustomerDao`**:
   * `insert(customer: Customer): Long`
   * `findByAccountId(accountId: Int): Customer?`
3. **`FruitDao`**:
   * `insert(fruit: Fruit): Long`
   * `update(fruit: Fruit): Int`
   * `delete(id: Int): Int`
   * `getById(id: Int): Fruit?`
   * `getAll(): List<Fruit>`
   * `search(keyword: String): List<Fruit>` (Lọc theo name LIKE %?% OR category LIKE %?%)
   * `getAvailable(): List<Fruit>` (Lấy trái cây có `quantity > 0`)
   * `searchAvailable(keyword: String): List<Fruit>`
4. **`OrderDao`**:
   * `getAll(): List<Order>` (ORDER BY id DESC)
   * `getByCustomer(customerId: Int): List<Order>`
   * `updateStatus(id: Int, newStatus: String): Int`
   * `placeOrder(customerId: Int, fruitId: Int, qty: Int): Long`:
     * Bắt đầu transaction: `db.beginTransaction()`.
     * Kiểm tra tồn kho & HSD của trái cây. Thỏa điều kiện thì trừ tồn kho bằng `db.update("Fruit", cv, "id=?", ...)` và chèn đơn hàng mới.

---

#### Step 0.6: Stubs & AndroidManifest
* Tạo tất cả Activity file rỗng kế thừa `AppCompatActivity`:
  * `ui/auth/LoginActivity.kt`
  * `ui/auth/RegisterActivity.kt`
  * `ui/admin/AdminMainActivity.kt`
  * `ui/admin/FruitFormActivity.kt`
  * `ui/admin/AdminOrderActivity.kt`
  * `ui/customer/CustomerMainActivity.kt`
  * `ui/customer/FruitDetailActivity.kt`
  * `ui/customer/OrderHistoryActivity.kt`
* Đăng ký đầy đủ trong `AndroidManifest.xml` (LoginActivity là LAUNCHER).

---

### PHẦN 1: HỖ TRỢ & CHUẨN BỊ TÍCH HỢP (P1: 0:50 – 2:00)

* Đẩy nhánh `main` lên GitHub để TV2 và TV3 clone/pull về.
* Sẵn sàng hỗ trợ giải đáp thắc mắc về DAO/Database hoặc cấu trúc cho TV2 và TV3.

---

### PHẦN 2: TÍCH HỢP, REVIEW PR VÀ KIỂM THỬ (P2: 2:00 – 3:00)

1. **Review & Merge PR**:
   * Review `feature/auth` từ TV2 -> Merge vào `main`.
   * Review `feature/admin-fruit` và `feature/admin-order` từ TV2 -> Merge vào `main`.
   * Review `feature/customer` từ TV3 -> Merge vào `main`.
2. **Kiểm thử tích hợp (Checklist nghiệm thu)**:
   * Kiểm tra Database qua **App Inspection / Database Inspector** trong Android Studio (Đảm bảo 4 bảng đầy đủ, password dạng SHA-256).
   * Test luồng Đăng nhập -> Admin -> Thêm trái cây -> Customer đặt hàng -> Admin duyệt đơn.
   * Chạy ứng dụng mượt mà, không crash.

---

## LƯU Ý QUAN TRỌNG ĐỂ TRÁNH BUG & MERGE CONFLICT
1. **Chỉ chỉnh sửa file thuộc quyền sở hữu của TV1**.
2. Tất cả Date formatting nên dùng `Locale.US` để chuỗi ngày dạng `yyyy-MM-dd` đồng nhất tuyệt đối.
