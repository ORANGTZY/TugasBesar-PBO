package com.parkirin.model; // Nama paket/folder tempat file ini berada

import jakarta.persistence.*; // Mengimpor semua alat untuk menghubungkan kelas ini ke tabel database
import lombok.*; // Mengimpor alat Lombok agar kode lebih ringkas
import java.time.LocalDateTime; // Mengimpor tipe data untuk menyimpan tanggal dan waktu

@Data // Otomatis membuat getter, setter, dan toString untuk semua field
@Entity // Menandai bahwa kelas ini mewakili sebuah tabel di database
@Table(name = "reservasi") // Nama tabel di database yang diwakili kelas ini adalah "reservasi"
@NoArgsConstructor // Otomatis membuat konstruktor tanpa parameter
@AllArgsConstructor // Otomatis membuat konstruktor dengan semua parameter
public class Reservasi { // Kelas yang merepresentasikan data pemesanan/reservasi parkir
    @Id // Menandai field ini sebagai kunci utama tabel
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID dibuat otomatis oleh database
    private Integer id; // Nomor unik pengenal setiap reservasi

    @Column(name = "kode_unik", unique = true, nullable = false, length = 20) // Kode unik tiket, tidak boleh sama, wajib diisi
    private String kodeUnik; // Kode tiket unik pengguna (contoh: TKT12345678)

    @Column(name = "waktu_pesan") // Nama kolom di database adalah "waktu_pesan"
    private LocalDateTime waktuPesan; // Waktu saat pengguna melakukan pemesanan

    @Column(name = "estimasi_kedatangan", nullable = false) // Kolom "estimasi_kedatangan", wajib diisi
    private LocalDateTime estimasiKedatangan; // Perkiraan waktu pengguna tiba di lokasi parkir

    @Column(name = "waktu_check_in") // Nama kolom di database adalah "waktu_check_in"
    private LocalDateTime waktuCheckIn; // Waktu saat pengguna benar-benar masuk ke area parkir

    @Column(name = "waktu_check_out") // Nama kolom di database adalah "waktu_check_out"
    private LocalDateTime waktuCheckOut; // Waktu saat pengguna selesai dan keluar dari area parkir

    @Column(columnDefinition = "VARCHAR(20) DEFAULT 'Menunggu'") // Status reservasi, nilai awalnya "Menunggu"
    private String status = "Menunggu"; // Status reservasi saat ini: Menunggu, Aktif, Selesai, atau Dibatalkan

    @Column(name = "total_durasi") // Nama kolom di database adalah "total_durasi"
    private Integer totalDurasi; // Total lama parkir dalam menit

    @Column(name = "total_tarif") // Nama kolom di database adalah "total_tarif"
    private Float totalTarif; // Total biaya parkir yang harus dibayar (dalam rupiah)

    @Column(name = "user_id", nullable = false) // Kolom "user_id", wajib diisi
    private Integer userId; // ID pengguna yang membuat reservasi ini

    @Column(name = "slot_parkir_id", nullable = false) // Kolom "slot_parkir_id", wajib diisi
    private Integer slotParkirId; // ID slot parkir yang dipesan

    @Column(name = "tarif_id", nullable = false) // Kolom "tarif_id", wajib diisi
    private Integer tarifId; // ID tarif yang digunakan untuk menghitung biaya

    @PrePersist // Metode ini otomatis dijalankan SEBELUM data pertama kali disimpan ke database
    protected void onCreate() {
        if (waktuPesan == null) waktuPesan = LocalDateTime.now(); // Jika waktu pesan belum diisi, isi dengan waktu saat ini
    }
}
