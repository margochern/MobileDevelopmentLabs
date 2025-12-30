# Скрипт для настройки Gradle Wrapper
# Этот скрипт скачивает gradle-wrapper.jar, необходимый для работы Gradle Wrapper

Write-Host "Настройка Gradle Wrapper..." -ForegroundColor Green

$wrapperDir = "gradle\wrapper"
$jarPath = "$wrapperDir\gradle-wrapper.jar"
$jarUrl = "https://raw.githubusercontent.com/gradle/gradle/v8.2.0/gradle/wrapper/gradle-wrapper.jar"

# Проверяем, существует ли директория
if (-not (Test-Path $wrapperDir)) {
    New-Item -ItemType Directory -Path $wrapperDir -Force | Out-Null
    Write-Host "Создана директория: $wrapperDir" -ForegroundColor Yellow
}

# Проверяем, существует ли уже jar файл
if (Test-Path $jarPath) {
    Write-Host "gradle-wrapper.jar уже существует." -ForegroundColor Yellow
    exit 0
}

Write-Host "Скачивание gradle-wrapper.jar..." -ForegroundColor Yellow
try {
    # Используем встроенный метод для скачивания
    $ProgressPreference = 'SilentlyContinue'
    Invoke-WebRequest -Uri $jarUrl -OutFile $jarPath -UseBasicParsing
    Write-Host "gradle-wrapper.jar успешно скачан!" -ForegroundColor Green
    Write-Host "Теперь вы можете использовать: .\gradlew.bat assembleRelease" -ForegroundColor Green
} catch {
    Write-Host "Ошибка при скачивании: $_" -ForegroundColor Red
    Write-Host "Пожалуйста, скачайте gradle-wrapper.jar вручную:" -ForegroundColor Yellow
    Write-Host "1. Откройте: https://github.com/gradle/gradle/tree/v8.2.0/gradle/wrapper" -ForegroundColor Yellow
    Write-Host "2. Скачайте gradle-wrapper.jar" -ForegroundColor Yellow
    Write-Host "3. Поместите его в папку: $wrapperDir" -ForegroundColor Yellow
    exit 1
}

