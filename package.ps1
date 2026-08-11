$ErrorActionPreference = 'Stop'

$appName = 'Zipzipismail'
$appVersion = '1.0.0'
$jdkHome = $env:JAVA_HOME

if (-not $jdkHome) {
    throw 'JAVA_HOME tanımlı değil. JDK 17 veya üzeri kurun ve JAVA_HOME ayarlayın.'
}

$jpackage = Join-Path $jdkHome 'bin\jpackage.exe'
if (-not (Test-Path -LiteralPath $jpackage -PathType Leaf)) {
    throw "jpackage bulunamadı: $jpackage"
}

$mavenCommand = Get-Command mvn.cmd -ErrorAction SilentlyContinue
if (-not $mavenCommand) {
    throw 'Maven bulunamadı. Maven 3.9 veya üzerini PATH değişkenine ekleyin.'
}

& $mavenCommand.Source -B clean package

$appInput = Join-Path (Get-Location) 'target\app'
$distDirectory = Join-Path (Get-Location) 'dist'
$appImage = Join-Path $distDirectory $appName
$jarName = "$appName.jar"

New-Item -ItemType Directory -Force -Path $appInput | Out-Null
New-Item -ItemType Directory -Force -Path $distDirectory | Out-Null
Copy-Item -LiteralPath 'target\zipzipismail-1.0.0.jar' -Destination (Join-Path $appInput $jarName) -Force

if (Test-Path -LiteralPath $appImage) {
    Remove-Item -LiteralPath $appImage -Recurse -Force
}

& $jpackage `
    --type app-image `
    --name $appName `
    --app-version $appVersion `
    --input $appInput `
    --main-jar $jarName `
    --main-class app.GameApplication `
    --dest $distDirectory `
    --vendor Zipzipismail `
    --description 'Otomatik zıplayan top mekanikli 2D platform oyunu'

$portableZip = Join-Path $distDirectory "$appName-portable.zip"
if (Test-Path -LiteralPath $portableZip) {
    Remove-Item -LiteralPath $portableZip -Force
}
Compress-Archive -Path $appImage -DestinationPath $portableZip -Force

Write-Host "EXE hazır: $appImage\$appName.exe"
Write-Host "Portable ZIP hazır: $portableZip"
Write-Host "Geliştirici JAR hazır: target\zipzipismail-1.0.0.jar"
