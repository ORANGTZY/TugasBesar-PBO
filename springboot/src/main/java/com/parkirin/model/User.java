package com.parkirin.model; // Nama paket/folder tempat file ini berada

import jakarta.persistence.*; // Mengimpor semua alat untuk menghubungkan kelas ini ke tabel database
import lombok.*; // Mengimpor alat Lombok agar kode lebih ringkas (otomatis buat getter/setter)

@Data // Otomatis membuat getter, setter, dan toString untuk semua field di bawah
@Entity // Menandai bahwa kelas ini mewakili sebuah tabel di database
@Table(name = "users") // Nama tabel di database yang diwakili kelas ini adalah "users"
@NoArgsConstructor // Otomatis membuat konstruktor kosong (tanpa parameter)
@AllArgsConstructor // Otomatis membuat konstruktor yang menerima semua field sebagai parameter
public class User { // Kelas yang merepresentasikan data pengguna aplikasi
    @Id // Menandai field ini sebagai kunci utama (primary key) tabel
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Nilai ID dibuat otomatis oleh database (auto-increment)
    private Integer id; // Nomor unik pengenal setiap pengguna

    @Column(nullable = false, length = 100) // Kolom nama tidak boleh kosong, maksimal 100 karakter
    private String nama; // Nama lengkap pengguna

    @Column(nullable = false, unique = true, length = 100) // Email wajib diisi, harus unik (tidak boleh sama), maks 100 karakter
    private String email; // Alamat email pengguna (dipakai untuk login)

    @Column(nullable = false, length = 255) // Kata sandi wajib diisi, maksimal 255 karakter
    private String password; // Kata sandi pengguna

    @Column(name = "no_telepon", length = 20) // Nama kolom di database adalah "no_telepon", maks 20 karakter
    private String noTelepon; // Nomor telepon pengguna

    @Column(columnDefinition = "INT DEFAULT 0") // Kolom poin, nilai awalnya 0 jika tidak diisi
    private Integer points = 0; // Total poin reward yang dimiliki pengguna

    @Column(columnDefinition = "VARCHAR(20) DEFAULT 'Pengguna'") // Kolom peran, nilai awalnya adalah "Pengguna"
    private String role = "Pengguna"; // Peran pengguna di aplikasi (contoh: Pengguna atau Admin)
}
