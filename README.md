# Project Final Muslih (SBL - Sky Ball League)

Aplikasi mobile Android untuk menjelajahi daftar liga olahraga, melihat tim-tim dalam liga tertentu, melihat detail tim, dan menandai tim favorit untuk akses offline.

## Deskripsi Aplikasi

Aplikasi ini dirancang untuk penggemar olahraga yang ingin dengan mudah mengakses informasi tentang liga dan tim favorit mereka. Fitur utamanya meliputi:

* **Daftar Liga:** Menampilkan daftar liga olahraga yang tersedia, diambil dari API TheSportsDB.
* **Daftar Tim:** Saat memilih liga, aplikasi akan menampilkan daftar tim yang tergabung dalam liga tersebut. Pengguna juga dapat mencari tim spesifik dalam daftar ini.
* **Detail Tim:** Menampilkan informasi detail tentang tim, termasuk logo tim, nama, dan deskripsi singkat.
* **Fitur Favorit:** Pengguna dapat menandai tim sebagai favorit dari halaman detail tim. Tim favorit akan disimpan secara lokal di perangkat.
* **Akses Offline (Favorit):** Tim yang ditambahkan ke favorit dapat dilihat bahkan tanpa koneksi internet, berkat penyimpanan data lokal.
* **Pengaturan Tema:** Pengguna dapat beralih antara mode terang dan mode gelap untuk kenyamanan visual.

## Cara Penggunaan

1.  **Melihat Daftar Liga:**
    * Setelah membuka aplikasi, Anda akan langsung disajikan dengan daftar liga yang tersedia.
    * Gunakan kolom pencarian di bagian atas untuk mencari liga berdasarkan nama.
    * Geser layar ke bawah (pull-to-refresh) untuk memperbarui daftar liga.

2.  **Melihat Daftar Tim di Liga:**
    * Ketuk salah satu liga dari daftar untuk melihat tim-tim yang tergabung dalam liga tersebut.
    * Gunakan kolom pencarian di bagian atas untuk mencari tim berdasarkan nama.
    * Geser layar ke bawah (pull-to-refresh) untuk memperbarui daftar tim.

3.  **Melihat Detail Tim:**
    * Ketuk salah satu tim dari daftar tim untuk melihat detail lengkap tim tersebut, termasuk logo dan deskripsi.

4.  **Menambahkan/Menghapus Tim ke Favorit:**
    * Pada halaman detail tim, Anda akan melihat ikon hati (favorit) di sudut kanan bawah (Floating Action Button).
    * Ketuk ikon hati tersebut untuk menambahkan tim ke daftar favorit Anda. Ikon akan berubah menjadi hati yang terisi.
    * Ketuk kembali ikon tersebut untuk menghapus tim dari daftar favorit Anda. Ikon akan kembali menjadi hati outline.

5.  **Melihat Tim Favorit:**
    * Navigasi ke tab "Favorites" dari bilah navigasi bawah.
    * Anda akan melihat daftar semua tim yang telah Anda tandai sebagai favorit. Tim ini akan tetap tersedia bahkan saat offline.
    * Ketuk tim favorit untuk melihat detailnya kembali.

6.  **Mengubah Tema Aplikasi:**
    * Navigasi ke tab "Settings" dari bilah navigasi bawah.
    * Gunakan sakelar "Dark Mode" untuk beralih antara tema terang dan tema gelap. Pengaturan ini akan disimpan untuk penggunaan aplikasi selanjutnya.

## Penjelasan Singkat Implementasi Teknis

Aplikasi ini dibangun menggunakan bahasa pemrograman Java dengan arsitektur MVVM sederhana dan memanfaatkan teknologi serta pustaka Android modern:

* **Bahasa Pemrograman:** Java
* **Gradle:** Digunakan sebagai sistem build.
* **Navigation Component:** Mengelola navigasi antar fragment dalam aplikasi, termasuk passing argumen antar tujuan.
* **Retrofit:** Pustaka HTTP client untuk melakukan permintaan jaringan ke TheSportsDB API.
* **Gson Converter:** Digunakan bersama Retrofit untuk mengonversi respons JSON dari API menjadi objek Java.
* **OkHttp Logging Interceptor:** Untuk logging permintaan dan respons HTTP, sangat membantu dalam debugging jaringan.
* **Glide:** Pustaka untuk memuat dan menampilkan gambar dari URL secara efisien (misalnya logo liga dan tim).
* **Room Persistence Library:** Digunakan sebagai lapisan abstraksi di atas SQLite untuk penyimpanan data lokal (tim favorit). Ini memungkinkan akses offline ke data favorit.
* **RecyclerView:** Menampilkan daftar liga dan tim secara efisien.
* **SwipeRefreshLayout:** Untuk fungsionalitas pull-to-refresh pada daftar liga dan tim.
* **Shared Preferences:** Digunakan untuk menyimpan preferensi pengguna seperti pengaturan tema (mode gelap).
* **Concurrency (ExecutorService & Handler):** Operasi jaringan dan database dilakukan di background thread menggunakan `ExecutorService` dan hasilnya diposting kembali ke UI thread menggunakan `Handler` untuk menjaga responsivitas aplikasi.
* **Material Design Components:** Menggunakan komponen UI dari Material Design untuk tampilan yang modern dan konsisten.

---
