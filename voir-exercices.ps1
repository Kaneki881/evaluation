param(
    [ValidateSet('1', '2', '3', 'tous')]
    [string]$Exercice = 'tous'
)

chcp 65001 > $null
[Console]::OutputEncoding = [System.Text.UTF8Encoding]::new($false)
$OutputEncoding = [System.Text.UTF8Encoding]::new($false)

$maven = Get-Command mvn -ErrorAction SilentlyContinue
if ($maven) {
    $mavenPath = $maven.Source
} else {
    $mavenPath = 'C:\Program Files\maven-mvnd-1.0.3-windows-amd64\mvn\bin\mvn.cmd'
    if (-not (Test-Path -LiteralPath $mavenPath)) {
        Write-Error 'Maven est introuvable. Installez Maven ou ajoutez mvn à PATH.'
        exit 1
    }
}

$modules = @{
    '1' = 'exercice1-stock'
    '2' = 'exercice2-projets'
    '3' = 'exercice3-etat-civil'
}

Push-Location $PSScriptRoot
try {
    if ($Exercice -eq 'tous') {
        & $mavenPath test -q
    } else {
        & $mavenPath -pl $modules[$Exercice] test -q
    }
    exit $LASTEXITCODE
} finally {
    Pop-Location
}
