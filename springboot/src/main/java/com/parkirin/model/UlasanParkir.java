package com.parkirin.model; // Nama paket/folder tempat file ini berada

import jakarta.persistence.*; // Mengimpor semua alat untuk menghubungkan kelas ini ke tabel database
import lombok.*; // Mengimpor alat Lombok agar kode lebih ringkas
import java.time.LocalDateTime; // Mengimpor tipe data untuk menyimpan tanggal dan waktu

@Data // Otomatis membuat getter, setter, dan toString untuk semua field
@Entity // Menandai bahwa kelas ini mewakili sebuah tabel di database
@Table(name = "ulasan_parkir") // Nama tabel di database yang diwakili kelas ini adalah "ulasan_parkir"
@NoArgsConstructor // Otomatis membuat konstruktor tanpa parameter
@AllArgsConstructor // Otomatis membuat konstruktor dengan semua parameter
public class UlasanParkir { // Kelas yang merepresentasikan data ulasan/review dari pengguna
    @Id // Menandai field ini sebagai kunci utama tabel
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID dibuat otomatis oleh database
    private Integer id; // Nomor unik pengenal setiap ulasan

    @Column(name = "reservasi_kode_unik", nullable = false, length = 20) // Kolom "reservasi_kode_unik", wajib diisi, maks 20 karakter
    private String reservasiKodeUnik; // Kode tiket reservasi yang sedang diulas

    @Column(name = "user_id", nullable = false) // Kolom "user_id", wajib diisi
    private Integer userId; // ID pengguna yang memberikan ulasan

    @Column(nullable = false) // Rating wajib diisi
    private Integer rating; // Nilai bintang yang diberikan pengguna (contoh: 1 sampai 5)

    @Column(columnDefinition = "TEXT") // Komentar bisa berupa teks panjang
    private String komentar; // Komentar/pendapat pengguna tentang layanan parkir

    @Column(name = "waktu_ulasan") // Nama kolom di database adalah "waktu_ulasan"
    private LocalDateTime waktuUlasan; // Waktu saat ulasan diberikan

    @PrePersist // Metode ini otomatis dijalankan SEBELUM data pertama kali disimpan ke database
    protected void onCreate() {
        if (waktuUlasan == null) waktuUlasan = LocalDateTime.now(); // Jika waktu ulasan belum diisi, isi dengan waktu saat ini
    }
}
