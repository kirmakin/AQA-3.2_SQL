Программа предназначена для тестирования входа в систему через веб-интерфейс.

**Окружение:**
- IntelliJ IDEA
- AdoptOpenJDK jdk-11.0.9
- Docker, версии не ниже 3.8.

**Инструкция по установке:**
1. Разверните базу, используя команду `docker-compose up -d`
2. Запустите приложение с помощью команды `java -jar ./artifacts/app-deadline.jar -P:jdbc.url=jdbc:mysql://localhost:3306/app -P:jdbc.user=kirmakin -P:jdbc.password=pass`
3. Tесты готовы к запуску. Перед повторным запуском автотестов приложение должно быть перезапущено.
