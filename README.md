# Zıpzıp İsmail

Java Swing ile geliştirilmiş, otomatik zıplayan top mekaniğine sahip 2D platform oyunudur. Oyuncu topu sağa ve sola yönlendirerek testerelerden kaçınır, potaya ulaşır ve özellikle 3. bölümde yıldızları toplamaya çalışır.

Bu proje, Kütahya Dumlupınar Üniversitesi Bilgisayar Mühendisliği bölümünde 1. sınıf uygulama ödevi olarak hazırlanmış bir Java 2D platform oyunudur. Proje daha sonra kaynak kodu, arayüzü, testleri ve Windows paketleme süreci geliştirilerek güncellenmiştir.

**[Windows portable paketini indir](https://github.com/emirkvrak/Zipzipismail/releases/latest/download/Zipzipismail-portable.zip)** · [Sürüm notları](https://github.com/emirkvrak/Zipzipismail/releases/latest)

ZIP dosyasını çıkarıp `Zipzipismail/Zipzipismail.exe` dosyasını çalıştırın. Paket Java runtime içerdiği için ayrıca Java kurmanız gerekmez.

<div align="center">
  <img src="assets/02-gameplay-demo.gif" alt="Zıpzıp İsmail 3. bölüm oynanış demosu" width="720">
</div>

## Temel özellikler

- Otomatik zıplama ve yatay oyuncu kontrolü
- Testere engelleri, bölüm sonu potası ve üç oynanabilir bölüm
- Süre takibi ile bölüm bazında en iyi süre ve tamamlanma kaydı
- 3. bölümde beş yıldız, iki checkpoint ve daha yükseğe sıçratan bloklar
- Duraklatma, kontrol yardımı, ses açma/kapatma ve tam ekran desteği

## Oynanış

Top zemine değdiğinde kendiliğinden seker; oyuncu yalnızca yatay yönü belirler. Testereye temas etmek veya haritadan düşmek oyunu bitirir, potaya ulaşmak bölümü tamamlar. İlk iki bölümün sonuç ekranından sıradaki bölüme geçilebilir.

3. bölümde etkinleştirilen checkpoint, kayıptan sonra aynı noktadan devam etmeyi sağlar. Bu bölümdeki yıldızlar isteğe bağlı olarak toplanır ve en yüksek yıldız sayısı yerel ilerleme kaydına yazılır.

## Kontroller

| Girdi | İşlev |
|---|---|
| `A / D` veya `← / →` | Topu sola veya sağa yönlendirir |
| `↑ / ↓` | Menü seçimini değiştirir |
| `Enter` | Seçimi onaylar; sonuç ekranında ilerler veya yeniden başlatır |
| `Esc` | Oyunu duraklatır veya önceki ekrana döner |
| `F10` | Sesi açar/kapatır |
| `F11` | Tam ekranı açar/kapatır |
| Sol tık | Menü ve duraklatma ekranındaki düğmeleri seçer |

## Kaynak koddan çalıştırma

JDK 17 veya üzeri ile Maven 3.9 veya üzeri gerekir.

```powershell
git clone https://github.com/emirkvrak/Zipzipismail.git
cd Zipzipismail
mvn clean package
java -jar target/zipzipismail-1.0.0.jar
```

Ana sınıf `app.GameApplication` sınıfıdır. Görseller, haritalar ve ses dosyası JAR içindeki classpath kaynaklarından yüklenir.

## Teknik yapı

- Java 17, Swing ve Java2D
- Maven ile derleme ve JUnit 5 ile test
- Sabit zaman adımlı oyun döngüsü ve ayrı durum, fizik, dünya, kaynak ve çizim sınıfları
- Tamamlanan bölümler, en iyi süreler, yıldızlar ve ses ayarı için Java Preferences tabanlı yerel kayıt
- `package.ps1` ve JDK `jpackage` ile runtime içeren Windows uygulama görüntüsü

Windows paketini yerel olarak üretmek için `JAVA_HOME` değişkenini JDK 17 veya daha yeni bir JDK'ya ayarlayın, Maven'ı `PATH` içine ekleyin ve çalıştırın:

```powershell
.\package.ps1
```

Betik `dist/Zipzipismail/Zipzipismail.exe` ile `dist/Zipzipismail-portable.zip` çıktılarını oluşturur.

## Testler

Test paketi; oyuncu ve çarpışma kurallarını, girdileri, harita yüklemeyi, kamera ve checkpoint davranışını, yıldızları, ilerleme kaydını, durum geçişlerini ve temel arayüz çizimini kapsayan 19 JUnit 5 testi içerir.

```powershell
mvn test
```

## Diğer ekran görüntüleri

<details>
<summary>Galeriyi göster</summary>

<div align="center">
<table>
  <tr>
    <td><img src="assets/01-level-selection.png" alt="Bölüm seçim ekranı" width="400"></td>
    <td><img src="assets/02-level-1-gameplay.png" alt="1. bölüm oynanışı" width="400"></td>
  </tr>
  <tr>
    <td align="center">Bölüm seçimi</td>
    <td align="center">1. bölüm</td>
  </tr>
  <tr>
    <td><img src="assets/03-level-2-completed.png" alt="2. bölüm sonuç ekranı" width="400"></td>
    <td><img src="assets/04-level-3-gameplay.png" alt="3. bölüm oynanışı" width="400"></td>
  </tr>
  <tr>
    <td align="center">2. bölüm sonucu</td>
    <td align="center">3. bölüm</td>
  </tr>
  <tr>
    <td><img src="assets/05-pause-menu.png" alt="Duraklatma menüsü" width="400"></td>
    <td><img src="assets/06-controls-menu.png" alt="Kontroller ekranı" width="400"></td>
  </tr>
  <tr>
    <td align="center">Duraklatma menüsü</td>
    <td align="center">Kontroller</td>
  </tr>
</table>

<img src="assets/07-game-over-checkpoint.png" alt="Checkpoint etkin durumdayken oyun sonu ekranı" width="520">

Checkpoint sonrası oyun sonu
</div>
</details>

## Bilinen sınırlamalar

- Oyun tek oyunculudur ve yalnızca masaüstünde çalışır.
- Çevrim içi skor tablosu, çok oyunculu mod ve bulut kaydı yoktur.
- İlerleme bilgisi kullanılan bilgisayardaki Java Preferences alanında saklanır; cihazlar arasında eşitlenmez.
- Grafik arayüzü ve tüm oynanış akışı otomatik testlerle uçtan uca sınanmaz.
