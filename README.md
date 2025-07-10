# 🍵 Matcha

✅ Cerințe

Pentru a rula aplicația, singurul lucru de care ai nevoie este Docker. Asigură-te că este instalat și pornit pe sistemul tău. Îl poți descărca de pe site-ul oficial Docker.

## 🚀 Cum pornești aplicația?

Urmează acești pași pentru a avea aplicația funcțională în câteva minute :

### 1️⃣ Descarcă proiectul

Navighează la secțiunea Releases a acestui proiect.

Descarcă cea mai recentă versiune, sub forma unei arhive proiect-final.tar.gz.

### 2️⃣ Dezarhivează

Extrage conținutul arhivei descărcate.

Deschide un terminal și navighează în folderul nou creat.

``` Bash
cd cale/catre/folderul-extras
```
### 3️⃣ Încarcă imaginea (doar o dată)

Rulează comanda de mai jos pentru a încărca imaginea Docker a aplicației.

Acest pas trebuie executat o singură dată după fiecare descărcare.

``` Bash
docker load -i matcha.tar
```
## ▶️ Utilizare

De fiecare dată când vrei să pornești aplicația, rulează comanda de mai jos în terminal:

``` Bash
docker run -it --rm matcha:latest
```
Gata! Meniul aplicației ar trebui să apară acum în terminalul tău. Spor! 🎉


-----------------------

Script pt Windows

✅ PowerShell Script: Build-DockerImage.ps1

$imageName = "matcha-app"
$tag = "v1"
$dockerfilePath = "Dockerfile"
$jarFile = "target/matcha-1.0-SNAPSHOT.jar"

FROM eclipse-temurin:24-jdk

WORKDIR /app

COPY $jarFile app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
"@ | Set-Content -Path $dockerfilePath -Encoding UTF8

Write-Host "Dockerfile created."
docker build -t "${imageName}:${tag}" .

if ($?) {
    Write-Host "Docker image '${imageName}:${tag}' built successfully." -ForegroundColor Green
} else {
    Write-Host "Docker build failed." -ForegroundColor Red
}

📁 Folder structure before running the script:
project-root/
├── target/
│   └── matcha.jar         <-- your JAR file must be here
└── Build-DockerImage.ps1

▶️ To run the script:
Open PowerShell

Navigate to your project directory

Run: .\Build-DockerImage.ps1

✅ Final Result
A Docker image named matcha-app:v1 will be available in your local Docker.
