# 🔐 E2EE Messenger Backend API

Це бекенд для E2EE (end-to-end encrypted) месенджера, створений на основі Flask та REST API. Забезпечує безпечну передачу повідомлень між користувачами з використанням симетричного чи асиметричного шифрування.

---

## ⚙️ Встановлення

1. **Клонуй репозиторій:**
   ```bash
   git clone https://github.com/yourusername/e2ee-messenger-api.git
   cd e2ee-messenger-api
   ```

2. **Встанови залежності:**
   ```bash
   pip install -r requirements.txt
   ```

3. **Налаштуй середовище (`.env`):**
   Створи файл `.env` у корені проєкту та додай туди:
   ```
   DATABASE_URI = mysql+pymysql://username:password@localhost/database
   SECRET_KEY = your_super_secret_key
   ```

---

## 🗄️ Робота з базою даних

1. Ініціалізація міграцій:
   ```bash
   flask db init
   ```

2. Створення нової міграції:
   ```bash
   flask db migrate -m "Initial migration."
   ```

3. Застосування міграцій:
   ```bash
   flask db upgrade
   ```

---

## 🚀 Запуск сервера

```bash
python run.py
```

---

## 📁 Структура проєкту (приклад)

```
e2ee-messenger-api/
│
├── app/
│   ├── __init__.py
│   ├── routes/
│   ├── models/
│   ├── services/
│   └── utils/
│
├── migrations/
│
├── run.py
├── requirements.txt
└── .env
```

---

## 🔐 Основні фічі (можна доповнювати)
- Реєстрація/авторизація користувачів
- Обмін E2EE-повідомленнями
- Публічні ключі користувачів
- Актуальні сесії/токени

