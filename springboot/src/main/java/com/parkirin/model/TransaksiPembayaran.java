package com.parkirin.model; // Nama paket/folder tempat file ini berada

import jakarta.persistence.*; // Mengimpor semua alat untuk menghubungkan kelas ini ke tabel database
import lombok.*; // Mengimpor alat Lombok agar kode lebih ringkas
import java.time.LocalDateTime; // Mengimpor tipe data untuk menyimpan tanggal dan waktu

@Data // Otomatis membuat getter, setter, dan toString untuk semua field
@Entity // Menandai bahwa kelas ini mewakili sebuah tabel di database
@Table(name = "transaksi_pembayaran") // Nama tabel di database yang diwakili kelas ini adalah "transaksi_pembayaran"
@NoArgsConstructor // Otomatis membuat konstruktor tanpa parameter
@AllArgsConstructor // Otomatis membuat konstruktor dengan semua parameter
public class TransaksiPembayaran { // Kelas yang merepresentasikan data transaksi pembayaran parkir
    @Id // Menandai field ini sebagai kunci utama tabel
    @Column(length = 50) // Panjang maksimal ID adalah 50 karakter
    private String id; // ID transaksi unik dari Midtrans (payment gateway)

    @Column(name = "reservasi_kode_unik", nullable = false, length = 20) // Kolom "reservasi_kode_unik", wajib diisi, maks 20 karakter
    private String reservasiKodeUnik; // Kode tiket reservasi yang terkait dengan transaksi ini

    @Column(name = "metode_pembayaran", length = 50) // Kolom "metode_pembayaran", maks 50 karakter
    private String metodePembayaran; // Cara pembayaran yang digunakan (contoh: QRIS, Transfer Bank)

    @Column(name = "jumlah_bayar", nullable = false) // Kolom "jumlah_bayar", wajib diisi
    private Integer jumlahBayar; // Jumlah uang yang dibayarkan (dalam rupiah)

    @Column(name = "status_pembayaran", length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'pending'") // Status pembayaran, nilai awalnya "pending"
    private String statusPembayaran = "pending"; // Status transaksi: "pending" (menunggu) atau "settlement" (berhasil)

    @Column(name = "waktu_transaksi") // Nama kolom di database adalah "waktu_transaksi"
    private LocalDateTime waktuTransaksi; // Waktu saat transaksi pembayaran dibuat

    @PrePersist // Metode ini otomatis dijalankan SEBELUM data pertama kali disimpan ke database
    protected void onCreate() {
        if (waktuTransaksi == null) waktuTransaksi = LocalDateTime.now(); // Jika waktu transaksi belum diisi, isi dengan waktu saat ini
    }
}
