# Тема
Автоматизация процессов регистрации и авторизации пользователей

Описание предметной области
---
Данный проект предназначен для первого впечатления о фреймворке Ktro и Exposed
В проекте присутствует база данных состоящая из следующих таблиц:
 - UserTable - таблица, хрянящаяя в себе данные о клиентах;
 - CardTable - таблица, хрянящаяя в себе данные о карточках;

Автор
---
Николаев Вячеслав Алексеевич студент группы ИП 20-3

## Схема базы данных
```mermaid
erDiagram

    BaseAuditEntity {
        Guid Id
        DateTimeOffset CreatedAt
        string CreatedBy
        DateTimeOffset UpdatedAt
        string UpdatedBy
        DateTimeOffset DeleteddAt
    }
    UserTable {
        string surname
        string name
        string email
        string email
        enum roleType
        enum statusType
    }

    CardTable {
        Guid userId FK
        string title
        string description "null"
        bool isVerified
    }


    CardTable ||--o{ UserTable: is

    BaseAuditEntity ||--o{ UserTable: allows
    BaseAuditEntity ||--o{ CardTable: allows
 ```
