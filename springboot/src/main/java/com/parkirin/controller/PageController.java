package com.parkirin.controller; // Nama paket/folder tempat file ini berada

import org.springframework.stereotype.Controller; // Penanda bahwa kelas ini adalah controller halaman web
import org.springframework.web.bind.annotation.GetMapping; // Alat untuk menangani permintaan HTTP GET (saat pengguna membuka URL)

@Controller // Menandai kelas ini sebagai controller yang mengarahkan pengguna ke halaman HTML
public class PageController { // Kelas ini bertugas mengarahkan setiap URL ke file HTML yang sesuai

    @GetMapping("/") // Saat pengguna membuka halaman utama (contoh: localhost:8080)
    public String index() { return "index"; } // Tampilkan halaman index.html

    @GetMapping("/onboarding-1") // Saat pengguna membuka URL /onboarding-1
    public String onboarding1() { return "onboarding-1"; } // Tampilkan halaman onboarding-1.html

    @GetMapping("/onboarding-2") // Saat pengguna membuka URL /onboarding-2
    public String onboarding2() { return "onboarding-2"; } // Tampilkan halaman onboarding-2.html

    @GetMapping("/login") // Saat pengguna membuka URL /login
    public String login() { return "login"; } // Tampilkan halaman login.html

    @GetMapping("/register") // Saat pengguna membuka URL /register
    public String register() { return "register"; } // Tampilkan halaman register.html

    @GetMapping("/register-success") // Saat pengguna membuka URL /register-success
    public String registerSuccess() { return "register-success"; } // Tampilkan halaman register-success.html

    @GetMapping("/forgot-password") // Saat pengguna membuka URL /forgot-password
    public String forgotPassword() { return "forgot-password"; } // Tampilkan halaman forgot-password.html

    @GetMapping("/user-dashboard") // Saat pengguna membuka URL /user-dashboard
    public String userDashboard() { return "user-dashboard"; } // Tampilkan halaman dashboard utama pengguna

    @GetMapping("/booking") // Saat pengguna membuka URL /booking
    public String booking() { return "booking"; } // Tampilkan halaman pemesanan parkir

    @GetMapping("/booking-pilih-kendaraan") // Saat pengguna membuka URL /booking-pilih-kendaraan
    public String bookingPilihKendaraan() { return "booking-pilih-kendaraan"; } // Tampilkan halaman pilih kendaraan saat booking

    @GetMapping("/pilih-slot-mobil") // Saat pengguna membuka URL /pilih-slot-mobil
    public String pilihSlotMobil() { return "pilih-slot-mobil"; } // Tampilkan halaman pilih slot parkir untuk mobil

    @GetMapping("/pilih-slot-motor") // Saat pengguna membuka URL /pilih-slot-motor
    public String pilihSlotMotor() { return "pilih-slot-motor"; } // Tampilkan halaman pilih slot parkir untuk motor

    @GetMapping("/ringkasan-pesanan") // Saat pengguna membuka URL /ringkasan-pesanan
    public String ringkasanPesanan() { return "ringkasan-pesanan"; } // Tampilkan halaman ringkasan detail pesanan parkir

    @GetMapping("/konfirmasi-pin") // Saat pengguna membuka URL /konfirmasi-pin
    public String konfirmasiPin() { return "konfirmasi-pin"; } // Tampilkan halaman konfirmasi PIN sebelum booking dikonfirmasi

    @GetMapping("/tiket") // Saat pengguna membuka URL /tiket
    public String tiket() { return "tiket"; } // Tampilkan halaman daftar tiket parkir

    @GetMapping("/tiket-aktif") // Saat pengguna membuka URL /tiket-aktif
    public String tiketAktif() { return "tiket-aktif"; } // Tampilkan halaman tiket yang sedang aktif/digunakan

    @GetMapping("/tiket-pembayaran") // Saat pengguna membuka URL /tiket-pembayaran
    public String tiketPembayaran() { return "tiket-pembayaran"; } // Tampilkan halaman pembayaran tiket parkir

    @GetMapping("/tiket-qr") // Saat pengguna membuka URL /tiket-qr
    public String tiketQr() { return "tiket-qr"; } // Tampilkan halaman QR code tiket parkir

    @GetMapping("/profil") // Saat pengguna membuka URL /profil
    public String profil() { return "profil"; } // Tampilkan halaman profil pengguna

    @GetMapping("/edit-profil") // Saat pengguna membuka URL /edit-profil
    public String editProfil() { return "edit-profil"; } // Tampilkan halaman edit profil pengguna

    @GetMapping("/dompet") // Saat pengguna membuka URL /dompet
    public String dompet() { return "dompet"; } // Tampilkan halaman dompet digital pengguna

    @GetMapping("/lokasi") // Saat pengguna membuka URL /lokasi
    public String lokasi() { return "lokasi"; } // Tampilkan halaman peta/detail lokasi parkir

    @GetMapping("/lokasi-list") // Saat pengguna membuka URL /lokasi-list
    public String lokasiList() { return "lokasi-list"; } // Tampilkan halaman daftar lokasi parkir yang tersedia

    @GetMapping("/kendaraan") // Saat pengguna membuka URL /kendaraan
    public String kendaraan() { return "kendaraan"; } // Tampilkan halaman daftar kendaraan milik pengguna

    @GetMapping("/tambah-kendaraan") // Saat pengguna membuka URL /tambah-kendaraan
    public String tambahKendaraan() { return "tambah-kendaraan"; } // Tampilkan halaman formulir tambah kendaraan baru

    @GetMapping("/edit-kendaraan") // Saat pengguna membuka URL /edit-kendaraan
    public String editKendaraan() { return "edit-kendaraan"; } // Tampilkan halaman edit data kendaraan

    @GetMapping("/aktivitas") // Saat pengguna membuka URL /aktivitas
    public String aktivitas() { return "aktivitas"; } // Tampilkan halaman riwayat aktivitas parkir pengguna

    @GetMapping("/atur-pin") // Saat pengguna membuka URL /atur-pin
    public String aturPin() { return "atur-pin"; } // Tampilkan halaman pengaturan PIN keamanan

    @GetMapping("/coupon") // Saat pengguna membuka URL /coupon
    public String coupon() { return "coupon"; } // Tampilkan halaman kupon/voucher diskon

    @GetMapping("/faq") // Saat pengguna membuka URL /faq
    public String faq() { return "faq"; } // Tampilkan halaman pertanyaan yang sering ditanyakan

    @GetMapping("/jadwal-parkir") // Saat pengguna membuka URL /jadwal-parkir
    public String jadwalParkir() { return "jadwal-parkir"; } // Tampilkan halaman jadwal atau jam operasional parkir

    @GetMapping("/langganan") // Saat pengguna membuka URL /langganan
    public String langganan() { return "langganan"; } // Tampilkan halaman paket langganan parkir

    @GetMapping("/layanan-pelanggan") // Saat pengguna membuka URL /layanan-pelanggan
    public String layananPelanggan() { return "layanan-pelanggan"; } // Tampilkan halaman layanan pelanggan/bantuan

    @GetMapping("/notification") // Saat pengguna membuka URL /notification
    public String notification() { return "notification"; } // Tampilkan halaman notifikasi pengguna

    @GetMapping("/statistik") // Saat pengguna membuka URL /statistik
    public String statistik() { return "statistik"; } // Tampilkan halaman statistik penggunaan parkir

    @GetMapping("/ulasan") // Saat pengguna membuka URL /ulasan
    public String ulasan() { return "ulasan"; } // Tampilkan halaman formulir ulasan/review parkir

    @GetMapping("/undang") // Saat pengguna membuka URL /undang
    public String undang() { return "undang"; } // Tampilkan halaman program undang teman

    @GetMapping("/undang-detail") // Saat pengguna membuka URL /undang-detail
    public String undangDetail() { return "undang-detail"; } // Tampilkan halaman detail referral/undang teman
}
