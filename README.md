# Мессенджер - Android приложение

Простое Android-приложение "Мессенджер" с использованием архитектуры на основе Activity и Fragment, а также встроенной навигации.

## Функциональность

- **Главная активность (MainActivity)**: Точка входа в приложение
- **Bottom Navigation**: Навигация между экранами через нижнюю панель
- **Три основных экрана**:
  - **Лента**: Новостная лента (заглушка с текстом)
  - **Профиль**: Отображение основных данных пользователя
  - **Настройки**: Базовые настройки приложения с переключателем темы

## Технологии

- **Язык**: Kotlin
- **Архитектура**: Activity + Fragment
- **Навигация**: Android Navigation Component (NavController, Navigation Graph)
- **UI**: Material Design Components
- **View Binding**: Для работы с layout файлами

## Структура проекта

```
app/
├── src/main/
│   ├── java/com/margoslabs/messenger/
│   │   ├── MainActivity.kt          # Главная активность
│   │   └── fragments/
│   │       ├── FeedFragment.kt      # Фрагмент новостной ленты
│   │       ├── ProfileFragment.kt   # Фрагмент профиля
│   │       └── SettingsFragment.kt  # Фрагмент настроек
│   ├── res/
│   │   ├── layout/                  # Layout файлы
│   │   ├── navigation/              # Navigation Graph
│   │   ├── menu/                    # Меню для Bottom Navigation
│   │   └── values/                  # Ресурсы (strings, colors, themes)
│   └── AndroidManifest.xml
```

## Сборка проекта

### Требования
- Android Studio Hedgehog или новее
- JDK 8 или выше
- Android SDK (minSdk: 24, targetSdk: 34)

### Важно: Настройка Android SDK

Перед сборкой необходимо настроить Android SDK. См. подробные инструкции в файле `BUILD_INSTRUCTIONS.md`.

### Сборка через Android Studio (Рекомендуется)
1. Откройте проект в Android Studio
2. Дождитесь синхронизации Gradle (Android Studio автоматически настроит SDK)
3. Выберите `Build > Build Bundle(s) / APK(s) > Build APK(s)`
4. После сборки APK будет находиться в `app/build/outputs/apk/release/app-release.apk`
5. Скопируйте APK в корень проекта для удобства проверки

### Сборка через командную строку
1. Настройте Android SDK (см. `BUILD_INSTRUCTIONS.md`)
2. Создайте файл `local.properties` с путем к SDK (используйте `local.properties.template` как пример)
3. Выполните сборку:
```bash
# Windows
.\gradlew.bat assembleRelease

# Linux/Mac
./gradlew assembleRelease
```

APK файл будет находиться в `app/build/outputs/apk/release/app-release.apk`

## Особенности реализации

1. **Логирование жизненного цикла**: Все компоненты (Activity и Fragments) логируют ключевые события жизненного цикла в Logcat
2. **Навигация**: Используется Navigation Component с Navigation Graph для управления переходами между экранами
3. **Темная тема**: Реализован переключатель темы в настройках с сохранением состояния через SharedPreferences
4. **View Binding**: Использован View Binding для безопасной работы с view элементами

## Логирование

Все события жизненного цикла выводятся в Logcat с тегами:
- `MainActivity` - для главной активности
- `FeedFragment` - для фрагмента ленты
- `ProfileFragment` - для фрагмента профиля
- `SettingsFragment` - для фрагмента настроек

## Установка APK

1. Скачайте `app-release.apk` из корня проекта
2. На Android устройстве включите "Установка из неизвестных источников"
3. Установите APK файл

## Лицензия

Этот проект создан в учебных целях.

