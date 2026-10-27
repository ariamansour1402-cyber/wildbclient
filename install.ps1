# Custom PvP Client - one-click installer (Windows)
# Installs Forge 1.8.9 into the official Minecraft Launcher and copies the mod.
$ErrorActionPreference = "Stop"
$ProgressPreference = "SilentlyContinue"
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12

try {
    $here = Split-Path -Parent $MyInvocation.MyCommand.Path
    $modJar = Get-ChildItem -Path $here -Filter "CustomPvPClient*.jar" | Select-Object -First 1
    if (-not $modJar) { throw "CustomPvPClient jar not found. Put it in the same folder as this installer." }

    $mc = Join-Path $env:APPDATA ".minecraft"
    $profiles = Join-Path $mc "launcher_profiles.json"
    if (-not (Test-Path $profiles)) {
        throw "Minecraft Launcher not found. Install it, open it once, sign in, then run this installer again."
    }

    $work = Join-Path $env:TEMP "customclient-setup"
    New-Item -ItemType Directory -Force -Path $work | Out-Null

    # 1) Portable Java 8 (needed to run the Forge installer)
    $javaDir = Join-Path $work "jre8"
    $java = Get-ChildItem $javaDir -Recurse -Filter java.exe -ErrorAction SilentlyContinue | Select-Object -First 1
    if (-not $java) {
        Write-Host "[1/3] Downloading Java 8..."
        $javaZip = Join-Path $work "jre8.zip"
        Invoke-WebRequest "https://api.adoptium.net/v3/binary/latest/8/ga/windows/x64/jre/hotspot/normal/eclipse" -OutFile $javaZip
        Expand-Archive $javaZip -DestinationPath $javaDir -Force
        $java = Get-ChildItem $javaDir -Recurse -Filter java.exe | Select-Object -First 1
    }

    # 2) Forge 1.8.9
    Write-Host "[2/3] Installing Forge 1.8.9..."
    $ver = "1.8.9-11.15.1.2318-1.8.9"
    $installer = Join-Path $work "forge-installer.jar"
    Invoke-WebRequest "https://maven.minecraftforge.net/net/minecraftforge/forge/$ver/forge-$ver-installer.jar" -OutFile $installer
    & $java.FullName -jar $installer --installClient
    if ($LASTEXITCODE -ne 0) { throw "Forge installer failed (code $LASTEXITCODE). Run Minecraft 1.8.9 once from the launcher, then try again." }

    # 3) Mod
    Write-Host "[3/3] Copying the client mod..."
    $mods = Join-Path $mc "mods"
    New-Item -ItemType Directory -Force -Path $mods | Out-Null
    Copy-Item $modJar.FullName $mods -Force

    Write-Host ""
    Write-Host "Done! Open Minecraft Launcher, choose the 'forge' profile and press Play." -ForegroundColor Green
}
catch {
    Write-Host ""
    Write-Host "ERROR: $($_.Exception.Message)" -ForegroundColor Red
}
