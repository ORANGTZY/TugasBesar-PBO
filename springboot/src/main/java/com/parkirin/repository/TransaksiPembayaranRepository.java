package com.parkirin.repository; // Nama paket/folder tempat file ini berada

import com.parkirin.model.TransaksiPembayaran; // Mengimpor kelas model TransaksiPembayaran
import org.springframework.data.jpa.repository.JpaRepository; // Mengimpor alat bawaan Spring yang menyediakan fungsi CRUD otomatis
import org.springframework.data.jpa.repository.Modifying; // Untuk menandai query yang mengubah data
import org.springframework.data.jpa.repository.Query; // Untuk menulis query khusus
import org.springframework.stereotype.Repository; // Penanda bahwa ini adalah kelas repository

@Repository // Menandai interface ini sebagai komponen repository yang dikelola Spring
public interface TransaksiPembayaranRepository extends JpaRepository<TransaksiPembayaran, String> { // Interface ini mengelola data tabel transaksi_pembayaran, ID-nya bertipe String (bukan angka)
    @Modifying // Menandai bahwa query ini akan mengubah data di database
    @Query("DELETE FROM TransaksiPembayaran t WHERE t.reservasiKodeUnik = ?1 AND t.statusPembayaran = 'pending'") // Query: hapus transaksi yang masih "pending" untuk kode tiket tertentu
    void deletePendingByKodeUnik(String kodeUnik); // Menghapus transaksi lama yang belum selesai sebelum membuat transaksi baru
}
