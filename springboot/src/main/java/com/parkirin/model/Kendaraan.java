package com.parkirin.model; // Nama paket/folder tempat file ini berada

import jakarta.persistence.*; // Mengimpor semua alat untuk menghubungkan kelas ini ke tabel database
import lombok.*; // Mengimpor alat Lombok agar kode lebih ringkas
import java.time.LocalDateTime; // Mengimpor tipe data untuk menyimpan tanggal dan waktu

@Data // Otomatis membuat getter, setter, dan toString untuk semua field
@Entity // Menandai bahwa kelas ini mewakili sebuah tabel di database
@Table(name = "kendaraan") // Nama tabel di database yang diwakili kelas ini adalah "kendaraan"
@NoArgsConstructor // Otomatis membuat konstruktor tanpa parameter
@AllArgsConstructor // Otomatis membuat konstruktor dengan semua parameter
public class Kendaraan { // Kelas yang merepresentasikan data kendaraan milik pengguna
    @Id // Menandai field ini sebagai kunci utama tabel
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID dibuat otomatis oleh database
    private Integer id; // Nomor unik pengenal setiap kendaraan

    @Column(name = "user_id", nullable = false) // Kolom "user_id" di database, wajib diisi
    private Integer userId; // ID pengguna pemilik kendaraan ini

    @Column(nullable = false, length = 10) // Jenis kendaraan wajib diisi, maks 10 karakter
    private String jenis; // Jenis kendaraan: "mobil" atau "motor"

    @Column(nullable = false, length = 20) // Nomor plat wajib diisi, maks 20 karakter
    private String plat; // Nomor plat kendaraan (contoh: B 1234 XY)

    @Column(length = 50) // Nama merek, maks 50 karakter
    private String merek; // Merek kendaraan (contoh: Toyota, Honda)

    @Column(length = 50) // Tipe/model kendaraan, maks 50 karakter
    private String model; // Model kendaraan (contoh: Avanza, Vario)

    private Integer tahun; // Tahun pembuatan kendaraan

    @Column(length = 30) // Warna kendaraan, maks 30 karakter
    private String warna; // Warna kendaraan (contoh: Putih, Hitam)

    @Column(length = 50) // Nomor mesin, maks 50 karakter
    private String mesin; // Nomor seri mesin kendaraan

    @Column(length = 50) // Nomor rangka, maks 50 karakter
    private String rangka; // Nomor seri rangka kendaraan

    @Column(name = "is_utama", columnDefinition = "BOOLEAN DEFAULT FALSE") // Kolom "is_utama", nilai awal false
    private Boolean isUtama = false; // Menandai apakah ini kendaraan utama pengguna (true = utama)

    @Column(name = "created_at", updatable = false) // Kolom tanggal dibuat, tidak bisa diubah setelah tersimpan
    private LocalDateTime createdAt; // Waktu saat kendaraan pertama kali ditambahkan

    @PrePersist // Metode ini otomatis dijalankan SEBELUM data pertama kali disimpan ke database
    protected void onCreate() {
        createdAt = LocalDateTime.now(); // Isi tanggal dibuat dengan waktu saat ini
    }
}
