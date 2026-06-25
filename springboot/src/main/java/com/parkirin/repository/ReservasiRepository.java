package com.parkirin.repository; // Nama paket/folder tempat file ini berada

import com.parkirin.model.Reservasi; // Mengimpor kelas model Reservasi yang akan diakses ke database
import org.springframework.data.jpa.repository.JpaRepository; // Mengimpor alat bawaan Spring yang menyediakan fungsi CRUD otomatis
import org.springframework.data.jpa.repository.Modifying; // Untuk menandai query yang mengubah data
import org.springframework.data.jpa.repository.Query; // Untuk menulis query khusus
import org.springframework.stereotype.Repository; // Penanda bahwa ini adalah kelas repository
import java.time.LocalDateTime; // Mengimpor tipe data tanggal dan waktu
import java.util.List; // Mengimpor tipe data List (daftar)
import java.util.Optional; // Mengimpor Optional, digunakan saat hasil pencarian bisa ada atau tidak ada

@Repository // Menandai interface ini sebagai komponen repository yang dikelola Spring
public interface ReservasiRepository extends JpaRepository<Reservasi, Integer> { // Interface ini mengelola data tabel reservasi
    Optional<Reservasi> findByKodeUnik(String kodeUnik); // Mencari satu reservasi berdasarkan kode tiket uniknya

    @Query("SELECT r FROM Reservasi r WHERE r.userId = ?1 ORDER BY r.waktuPesan DESC") // Query: ambil semua reservasi user tertentu, diurutkan dari yang terbaru
    List<Reservasi> findByUserIdOrderByWaktuPesanDesc(Integer userId); // Mendapatkan riwayat pemesanan parkir pengguna, terbaru di atas

    @Query("SELECT r FROM Reservasi r WHERE r.status = 'Aktif' AND r.waktuCheckIn IS NULL AND r.estimasiKedatangan < ?1") // Query: cari reservasi aktif yang sudah melewati estimasi kedatangan tapi belum check-in
    List<Reservasi> findExpiredActiveReservations(LocalDateTime now); // Mencari tiket yang sudah kedaluwarsa (tidak dipakai tepat waktu)

    @Modifying // Menandai bahwa query ini akan mengubah data di database
    @Query("UPDATE Reservasi r SET r.status = 'Dibatalkan' WHERE r.status = 'Aktif' OR r.status = 'Menunggu'") // Query: batalkan semua reservasi yang masih aktif atau menunggu
    void cancelAllActive(); // Membatalkan semua reservasi aktif (digunakan saat reset harian jam 08.00)
}
