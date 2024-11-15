Nama: Rafi Rizkullah  
Kelas: 5A REG BJB  
NPM: 2210010288

![image](https://github.com/user-attachments/assets/a8f39413-33db-44f4-ab4b-c5b548272d97)
![image](https://github.com/user-attachments/assets/25c442c0-1bfa-41ae-8d4c-4dc37febe56b)
![image](https://github.com/user-attachments/assets/7403b117-ccb5-41e9-a0f2-0fca6dd74424)
![image](https://github.com/user-attachments/assets/24e12f8a-e42e-4621-89ba-dabd0f1d8ba8)
![image](https://github.com/user-attachments/assets/8dbdc371-b894-4249-a66b-d479c2b7cc04)


# Aplikasi Cek Cuaca

Aplikasi ini memungkinkan pengguna untuk memeriksa kondisi cuaca terkini berdasarkan nama kota menggunakan API dari OpenWeatherMap. Selain itu, aplikasi ini juga memungkinkan pengguna untuk menyimpan dan memuat data cuaca dalam format CSV.

## Fitur Utama
1. **Cek Cuaca**: Mengambil data cuaca terkini berdasarkan nama kota yang dimasukkan.
2. **Favorit Kota**: Menyimpan kota favorit untuk memudahkan pengecekan cuaca di masa depan.
3. **Muat Data dari CSV**: Memuat data cuaca sebelumnya yang disimpan dalam format CSV ke dalam tabel.
4. **Simpan Data ke CSV**: Menyimpan data cuaca yang ditampilkan dalam aplikasi ke dalam file CSV.

## Prasyarat
Sebelum menjalankan aplikasi ini, pastikan Anda memiliki:
1. **Java Development Kit (JDK)** versi 8 atau lebih tinggi.
2. **Koneksi internet** untuk mengakses API OpenWeatherMap.
3. **API Key OpenWeatherMap**. Anda bisa mendapatkannya dengan mendaftar di [OpenWeatherMap](https://openweathermap.org/api) dan menggunakan API key yang diberikan.

## Instalasi
1. **Clone atau Download** kode sumber aplikasi ini.
2. **Buka dan kompilasi** kode menggunakan IDE seperti NetBeans atau IntelliJ IDEA, atau melalui terminal menggunakan `javac` jika Anda menginginkan proses manual.
3. Pastikan untuk mengganti **API Key** pada kode dengan API Key yang Anda dapatkan dari OpenWeatherMap.

   API Key dapat dimasukkan pada bagian konfigurasi aplikasi yang relevan.

## Penggunaan
### 1. Menyimpan Data Cuaca
   - Pilih kota yang ingin diperiksa melalui input text atau pilih dari daftar kota favorit.
   - Klik tombol **Cek** untuk mengambil data cuaca.
   - Setelah data ditampilkan, Anda dapat memilih untuk **menyimpan data** ke file CSV dengan mengklik tombol **Save**.

### 2. Memuat Data dari CSV
   - Klik tombol **Load** untuk memuat data cuaca yang telah disimpan sebelumnya dari file CSV.

### 3. Menambahkan Kota Favorit
   - Setelah memeriksa cuaca untuk sebuah kota, kota tersebut akan otomatis ditambahkan ke daftar **Kota Favorit** jika belum ada.

## Struktur Kode
Aplikasi ini menggunakan **Swing** untuk antarmuka grafis dan mengakses **OpenWeatherMap API** untuk mendapatkan data cuaca.

### Kelas Utama
- **FrameCekCuaca**: Kelas utama yang berfungsi untuk menampilkan antarmuka pengguna, mengambil data cuaca dari API OpenWeatherMap, serta memuat dan menyimpan data cuaca ke dalam file CSV.

## Masalah Umum
- **Tidak dapat mengambil data cuaca**: Pastikan Anda memiliki koneksi internet yang stabil dan API Key yang valid.
- **File CSV tidak dapat dimuat**: Periksa apakah format CSV sesuai dan file tidak rusak.

## Lisensi
Aplikasi ini dibuat untuk tujuan pembelajaran dan penggunaan pribadi. Lisensi untuk kode ini adalah **MIT License**. Anda bebas untuk menggunakan, memodifikasi, dan mendistribusikan aplikasi ini.

## Kontribusi
Jika Anda ingin berkontribusi pada proyek ini, silakan buat **Pull Request** atau laporkan masalah di halaman **Issues**.
