# Đặc tả gốc — FRUIT SHOP MANAGEMENT

> Chép nguyên văn từ file đề bài (Test1.docx). Mọi chức năng trong repo bám theo đặc tả này.

Create a project with the following 4 tables:

- **Account**(id, username, password, role)
- **Fruit**(id, name, category, price, quantity, expiryDate, description)
- **Customer**(id, fullname, phone, email, accountId)
- **Order**(id, customerId, orderDate, totalAmount, status)

And required functionalities:

1. **Login**: Implement a login function with two roles: Admin and Customer, and provide the corresponding functions for each role.
2. **Login Account Storage**: Store login account information using SharedPreferences.
3. **Data Storage**: Store the tables using SQLite or Room Database.
4. **Admin Functions**: Allow administrators to add, update, delete, search, and view fruit information, including name, category, price, quantity, expiry date, and description.
5. **Expiry Date Management**: Display fruit expiry dates and identify expired fruits. Prevent customers from ordering expired fruits.
6. **Customer Functions**: Allow customers to view available fruits, search for fruits, check expiry dates, place orders, and view their order history.
7. **Order Management**: Allow customers to place orders and allow administrators to update order statuses, such as Pending, Confirmed, and Completed.
8. **Version Control and Collaboration**: Work together and manage the project code using GitHub or GitLab.
9. **Scoring**: The score is based on the time taken to complete the project. The less time a student takes, the higher the score.

## Quy ước nghiệp vụ nhóm đã chốt

| Vấn đề | Quy ước |
|---|---|
| Hết hạn | `expiryDate < hôm nay` → Expired. Đúng ngày hết hạn vẫn bán được. Còn ≤ 3 ngày → "Expiring soon". |
| Đặt hàng | Mỗi đơn = 1 loại trái cây × số lượng; `totalAmount = price × quantity`; trừ tồn kho ngay khi đặt; chặn ở cả UI và `OrderDao` nếu hết hạn / không đủ hàng. |
| Bảng Order | Giữ đúng 4 cột của đề (không có bảng chi tiết đơn) nên lịch sử đơn hiển thị mã đơn, ngày, tổng tiền, trạng thái. |
| Trạng thái đơn | `Pending → Confirmed → Completed` (chỉ đi tiến). |
| Mật khẩu | Lưu SHA-256 trong cột `password`. |
| SharedPreferences | Lưu phiên đăng nhập (id, username, role, customerId) + "Remember me" (username/password để tự điền và tự đăng nhập). |
