param([int]$Requests = 20)
$ErrorActionPreference = "Stop"
$measurements = 1..$Requests | ForEach-Object {
    $clock = [System.Diagnostics.Stopwatch]::StartNew()
    Invoke-RestMethod http://localhost:8087/work | Out-Null
    $clock.Stop()
    $clock.Elapsed.TotalMilliseconds
}
$sorted = @($measurements | Sort-Object)
[pscustomobject]@{ Requests=$Requests; MeanMs=($measurements | Measure-Object -Average).Average; P95Ms=$sorted[[Math]::Ceiling($Requests * 0.95)-1] }
