package com.parkirin.model; // Nama paket/folder tempat file ini berada

import jakarta.persistence.*; // Mengimpor semua alat untuk menghubungkan kelas ini ke tabel database
import lombok.*; // Mengimpor alat Lombok agar kode lebih ringkas
import java.time.LocalDateTime; // Mengimpor tipe data untuk menyimpan tanggal dan waktu

@Data // Otomatis membuat getter, setter, dan toString untuk semua field
@Entity // Menandai bahwa kelas ini mewakili sebuah tabel di database
@Table(name = "user_pins") // Nama tabel di database yang diwakili kelas ini adalah "user_pins"
@NoArgsConstructor // Otomatis membuat konstruktor tanpa parameter
@AllArgsConstructor // Otomatis membuat konstruktor dengan semua parameter
public class UserPin { // Kelas yang merepresentasikan data PIN keamanan pengguna
    @Id // Menandai field ini sebagai kunci utama tabel
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID dibuat otomatis oleh database
    private Integer id; // Nomor unik pengenal setiap data PIN

    @Column(name = "user_id", nullable = false, unique = true) // Kolom "user_id", wajib diisi, satu user hanya boleh punya satu PIN
    private Integer userId; // ID pengguna pemilik PIN ini

    @Column(nullable = false, length = 60) // PIN wajib diisi, disimpan sebagai hash BCrypt (60 karakter)
    private String pin; // Kode PIN 6 angka milik pengguna (disimpan dalam bentuk hash BCrypt)

    @Column(name = "created_at", updatable = false) // Kolom tanggal dibuat, tidak bisa diubah setelah tersimpan
    private LocalDateTime createdAt; // Waktu saat PIN pertama kali dibuat

    @Column(name = "updated_at") // Nama kolom di database adalah "updated_at"
    private LocalDateTime updatedAt; // Waktu saat PIN terakhir kali diubah

    @PrePersist // Metode ini otomatis dijalankan SEBELUM data pertama kali disimpan ke database
    protected void onCreate() {
        createdAt = LocalDateTime.now(); // Isi tanggal dibuat dengan waktu saat ini
        updatedAt = LocalDateTime.now(); // Isi tanggal diubah dengan waktu saat ini (sama saat pertama dibuat)
    }

    @PreUpdate // Metode ini otomatis dijalankan SEBELUM data yang sudah ada diperbarui di database
    protected void onUpdate() {
        updatedAt = LocalDateTime.now(); // Perbarui tanggal diubah dengan waktu saat ini
    }
}
