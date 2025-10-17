
# 🧠 Spring JPA Query Visualizer

**Spring JPA Query Visualizer** là một công cụ mã nguồn mở giúp developers **xem trực tiếp SQL được sinh ra bởi Hibernate/JPA ngay trong IDE (IntelliJ & VSCode)**.  
Dừng việc đoán mò ORM đang làm gì. Hãy nhìn thấy nó.

---

## 🚀 Tổng quan

Khi làm việc với Spring Data JPA hoặc Hibernate, developers thường tự hỏi:

> "SQL thực sự mà ORM đang chạy là gì?"

Spring JPA Query Visualizer sẽ bắt, định dạng và hiển thị mọi SQL query được thực thi bởi Hibernate — **ngay tại nơi bạn code**.

### ✅ Tính năng chính

| Tính năng | Mô tả |
|----------|--------------|
| 🔍 **Xem trước SQL trực tiếp** | Xem chính xác SQL được sinh ra cho các method repository hoặc entity |
| 🧩 **Mapping Entity-to-Table** | Xem field nào của entity map với column nào của database |
| 🎨 **Định dạng & Highlight SQL** | Syntax highlighting và query beautifier |
| ⚡ **Phân tích Performance** | Phát hiện N+1 queries và các statement chạy lâu |
| 🧠 **Tích hợp IDE** | Hoạt động trực tiếp trong IntelliJ và VSCode |
| ☁️ **Cloud Dashboard (sắp có)** | Upload logs để phân tích performance sâu hơn |

---

## 🧩 Kiến trúc

```
[Spring Boot App]
|
└── JPA/Hibernate
└── SQL Interceptor (Core Library)
|
└── REST/WebSocket
|
└── [IDE Plugin: IntelliJ / VSCode]
└── Interactive SQL Mapping View
```

**Các thành phần chính:**
1. **Core Library (Java)** — hook vào Hibernate qua `StatementInspector` hoặc `DatasourceProxy`  
   → Bắt SQL + metadata  
2. **IDE Plugin** — hiển thị query & entity mapping trực tiếp trong IDE  
3. **(Tùy chọn)** Cloud Dashboard để phân tích performance và team analytics

---

## ⚙️ Tech Stack

| Layer | Công nghệ |
|--------|-------------|
| Core | Java 17+, Spring Boot, Hibernate `StatementInspector` |
| Data Transport | REST API / WebSocket |
| SQL Parser | [JSQLParser](https://github.com/JSQLParser/JSqlParser) |
| IntelliJ Plugin | JetBrains Plugin SDK (Kotlin) |
| VSCode Plugin | TypeScript + VSCode Extension API |
| UI Rendering | React (VSCode webview) / Kotlin Swing (IntelliJ) |

---

## 🧠 Cách hoạt động

1. Chạy Spring Boot app với dependency `spring-jpa-query-visualizer-core`.  
2. Tool sẽ intercept mọi SQL được thực thi bởi Hibernate.  
3. Queries và metadata (entity, method, execution time) được gửi đến local port (`7777` mặc định).  
4. IDE plugin lắng nghe port đó.  
5. Khi bạn mở repository hoặc entity, nó sẽ hiển thị SQL được sinh ra — được format đẹp.

Ví dụ:

```java
// UserRepository.java
User findByEmail(String email);
```

Tool sẽ hiển thị:

```sql
SELECT u.id, u.name, u.email
FROM users u
WHERE u.email = ?
```

---

## 🧰 Cài đặt (sắp có)

### Option 1 — Gradle (Core Library)

```gradle
implementation("com.spring-jpa-visualizer:spring-jpa-query-visualizer-core:0.1.0")
```

### Option 2 — Maven (Core Library)

```xml
<dependency>
  <groupId>com.spring-jpa-visualizer</groupId>
  <artifactId>spring-jpa-query-visualizer-core</artifactId>
  <version>0.1.0</version>
</dependency>
```

### Option 3 — IntelliJ Plugin

* Cài đặt qua **JetBrains Marketplace** → Tìm kiếm "Spring JPA Query Visualizer"

### Option 4 — VSCode Extension

* Cài đặt từ VSCode Marketplace → "Spring JPA Query Visualizer"

---

## 🧪 Roadmap hiện tại

| Phase   | Mô tả                                        | Trạng thái         |
| ------- | -------------------------------------------------- | -------------- |
| Phase 1 | Core interceptor (bắt SQL, ghi vào JSON/log)  | 🟢 Đang phát triển |
| Phase 2 | IntelliJ MVP plugin (hiển thị SQL popup)               | ⏳ Kế hoạch      |
| Phase 3 | VSCode plugin + SQL formatting                     | ⏳ Kế hoạch      |
| Phase 4 | Cloud dashboard (query performance)                | ⏳ Kế hoạch      |
| Phase 5 | Pro version (alerts, explain plan, team dashboard) | 🔜 Tương lai      |

---

## 🧑‍💻 Đóng góp

Chúng tôi rất hoan nghênh sự đóng góp!
Bạn có thể giúp bằng cách:

* Submit pull requests
* Mở feature ideas hoặc bug reports
* Cải thiện documentation hoặc translations

```bash
git clone https://github.com/yourusername/spring-jpa-query-visualizer.git
cd spring-jpa-query-visualizer
```

Vui lòng đọc [CONTRIBUTING.md](CONTRIBUTING.md) trước khi bắt đầu.

---

## 🌍 Cộng đồng

* 💬 Discord (sắp có)
* 🧵 Twitter/X: [@SpringJPAVisualizer](https://twitter.com/)
* 🧠 Blog: [https://spring-jpa-visualizer.dev](https://spring-jpa-visualizer.dev) *(tương lai)*

---

## 📜 License

Apache License 2.0 — miễn phí cho sử dụng cá nhân và thương mại.

---

## ❤️ Lời cảm ơn

Được truyền cảm hứng từ:

* [p6spy](https://github.com/p6spy/p6spy)
* [Datasource-Proxy](https://github.com/ttddyy/datasource-proxy)
* [JSQLParser](https://github.com/JSQLParser/JSqlParser)
* Cộng đồng Java & Spring developers 🙌

---

## 🌟 Hỗ trợ dự án

Nếu bạn thấy **Spring JPA Query Visualizer** hữu ích, hãy:

* ⭐ Star repo này
* 🐦 Chia sẻ trên Twitter / LinkedIn
* 💬 Đóng góp ý tưởng / code

> Hãy cùng làm cho việc debug Hibernate trở nên minh bạch và dễ dàng.

---

## 📌 Cấu trúc dự án đề xuất

```
spring-jpa-query-visualizer/
├── spring-jpa-query-visualizer-core/     # Core library
├── spring-jpa-query-visualizer-intellij/ # IntelliJ plugin
├── spring-jpa-query-visualizer-vscode/   # VSCode extension
├── docs/                                  # Documentation
├── examples/                             # Example projects
└── README.md
```
