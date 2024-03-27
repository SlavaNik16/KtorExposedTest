# Тема
Автоматизация процессов регистрации и авторизации пользователей

Описание предметной области
---
Данный проект предназначен для первого впечатления о фреймворке Ktro и Exposed
В проекте присутствует база данных состоящая из следующих таблиц:
 - UserTable - таблица, хрянящаяя в себе данные о клиентах;
 - CardTable - таблица, хрянящаяя в себе данные о карточках;

Описание тестового задания
---
1) Бриф
С клиента на сервер идет запрос, клиент генерирует хеш определенным образом и посылает в 
составе запроса. 
На сервере S3 этот хеш сравнивается со сгенеренным на сервере
Необходимо написать серверный код проверки токена, чтобы сравнивать с хешем пришедшим с 
клиента.
P.s Использовать AWS s3 как данный пример

Результат работы
---
1)Чтобы найти контроллер, нужно перейти по этому пути: API -> Controllers -> ValidateS3Controller

2)Чтобы посмотреть доп. классы, нужно перейти: Services -> Authentication -> AWS

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
