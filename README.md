Проект написанный для изучения алгоритмов работы RESTful сервиса.

Используется:
* Tomcat сервер
* SQLite база данных
* Java Servlets
* Java DataBase Connection
* Tomcat native JDBC connection pool
* JSON
* Maven


### ОБМЕННИК ВАЛЮТ
в БД находятся две таблицы:

Currencies - хранит различные виды валют

    - id
    - буквенный код (3 буквы)
    - полное название
    - символ валюты

ExchangeRates - хранит различные виды отношений одних валют к другим

    - id
    - id базовой валюты в таблице Currencies
    - id целевой валюты в таблице Currencies
    - числовое отношение одной валюты к другой

API endpoints:

/currencies

    - GET возвращает все записи из таблицы Currencies в виде JSON DTOs
    - ответы 200 или 500

/currency/ABC

    - ABC вводимый код валюты для поиска в БД
    - код должен быть из трех букв в верхнем регистре
    - GET возвращает один JSON DTO если такой код есть в базе
    - ответы 200 / 400 / 404 / 500
    - POST в разработке
    - PUT в разработке
    - DELETE в разработке

/exchangeRates

    - GET возвращает все записи из таблицы ExchangeRates в виде JSON DTOs
    - ответы 200 или 500

/exchangeRate/ABCXYZ
    
    - ABC вводимый код базовой валюты расчета
    - XYZ вводимый код целевой валюты расчета
    - коды должны быть из трех букв в верхнем регистре
    - GET возвращает один JSON DTO если такое сочетание есть в базе
    - ответы 200 / 400 / 404 / 500
    - POST в разработке
    - PUT в разработке
    - DELETE в разработке
