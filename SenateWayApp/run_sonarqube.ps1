# SonarQube Analysis Script for Senate Way Guesthouse
# This script runs SonarQube analysis and provides the results link

Write-Host "================================================" -ForegroundColor Cyan
Write-Host "SonarQube Analysis for Senate Way Guesthouse" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

# Set JAVA_HOME if not already set
if (-not $env:JAVA_HOME) {
    $env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
    Write-Host "Set JAVA_HOME to: $env:JAVA_HOME" -ForegroundColor Yellow
}

# Check if Gradle wrapper exists
if (Test-Path ".\gradlew.bat") {
    $gradleCommand = ".\gradlew.bat"
} elseif (Test-Path ".\gradlew") {
    $gradleCommand = ".\gradlew"
} elseif (Get-Command gradle -ErrorAction SilentlyContinue) {
    $gradleCommand = "gradle"
} else {
    Write-Host "ERROR: Gradle not found. Please ensure gradlew.bat exists or gradle is in PATH." -ForegroundColor Red
    exit 1
}

Write-Host "Using Gradle command: $gradleCommand" -ForegroundColor Green
Write-Host ""

# Check for SonarCloud organization key
if (-not $env:SONAR_ORGANIZATION) {
    Write-Host "WARNING: SONAR_ORGANIZATION environment variable not set." -ForegroundColor Yellow
    Write-Host "For SonarCloud, you need to provide your organization key." -ForegroundColor Yellow
    Write-Host "You can:" -ForegroundColor Yellow
    Write-Host "  1. Set environment variable: `$env:SONAR_ORGANIZATION = 'your-org-key'" -ForegroundColor White
    Write-Host "  2. Pass it via command line: -Dsonar.organization=your-org-key" -ForegroundColor White
    Write-Host "  3. Get your organization key from: https://sonarcloud.io/account/organizations" -ForegroundColor White
    Write-Host "  4. Create an organization at: https://sonarcloud.io/organizations/create" -ForegroundColor White
    Write-Host ""
    $orgKey = Read-Host "Enter your SonarCloud organization key (or press Enter to skip and use default)"
    if ($orgKey) {
        $env:SONAR_ORGANIZATION = $orgKey
    }
}

Write-Host "Starting SonarQube analysis..." -ForegroundColor Green
Write-Host ""

# Build Gradle command with organization key if provided
$gradleArgs = @("clean", "sonar")
if ($env:SONAR_ORGANIZATION) {
    $gradleArgs += "-Dsonar.organization=$env:SONAR_ORGANIZATION"
}

# Run SonarQube analysis
try {
    & $gradleCommand $gradleArgs
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "================================================" -ForegroundColor Green
        Write-Host "SonarQube Analysis Completed Successfully!" -ForegroundColor Green
        Write-Host "================================================" -ForegroundColor Green
        Write-Host ""
        Write-Host "Results Links:" -ForegroundColor Cyan
        Write-Host ""
        Write-Host "For SonarCloud (Cloud):" -ForegroundColor Yellow
        Write-Host "  https://sonarcloud.io/project/overview?id=senateway-guesthouse" -ForegroundColor White
        Write-Host ""
        Write-Host "Direct Dashboard Link:" -ForegroundColor Cyan
        Write-Host "  https://sonarcloud.io/dashboard?id=senateway-guesthouse" -ForegroundColor Green
        Write-Host ""
        Write-Host "For Local SonarQube Server:" -ForegroundColor Yellow
        Write-Host "  http://localhost:9000/dashboard?id=senateway-guesthouse" -ForegroundColor White
        Write-Host ""
        Write-Host "IMPORTANT: If this is your first analysis, you may need to:" -ForegroundColor Yellow
        Write-Host "  1. Go to https://sonarcloud.io" -ForegroundColor White
        Write-Host "  2. Navigate to your organization: st10356506" -ForegroundColor White
        Write-Host "  3. The project should be automatically created, but if not:" -ForegroundColor White
        Write-Host "     - Click 'Add Project' > 'Analyze a new project'" -ForegroundColor White
        Write-Host "     - Project key: senateway-guesthouse" -ForegroundColor White
        Write-Host "  4. Wait 1-5 minutes for processing to complete" -ForegroundColor White
        Write-Host ""
        Write-Host "Check analysis status at:" -ForegroundColor Cyan
        Write-Host "  https://sonarcloud.io/dashboard?id=senateway-guesthouse" -ForegroundColor Green
        Write-Host ""
    } else {
        Write-Host "ERROR: SonarQube analysis failed. Check the output above for details." -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "ERROR: Failed to run SonarQube analysis: $_" -ForegroundColor Red
    exit 1
}

