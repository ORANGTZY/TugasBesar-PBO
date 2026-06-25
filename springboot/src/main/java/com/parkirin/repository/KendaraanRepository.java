package com.parkirin.repository; // Nama paket/folder tempat file ini berada

import com.parkirin.model.Kendaraan; // Mengimpor kelas model Kendaraan yang akan diakses ke database
import org.springframework.data.jpa.repository.JpaRepository; // Mengimpor alat bawaan Spring yang menyediakan fungsi CRUD otomatis
import org.springframework.data.jpa.repository.Modifying; // Untuk menandai query yang mengubah data
import org.springframework.data.jpa.repository.Query; // Untuk menulis query khusus
import org.springframework.stereotype.Repository; // Penanda bahwa ini adalah kelas repository
import java.util.List; // Mengimpor tipe data List (daftar)

@Repository // Menandai interface ini sebagai komponen repository yang dikelola Spring
public interface KendaraanRepository extends JpaRepository<Kendaraan, Integer> { // Interface ini mengelola data tabel kendaraan
    List<Kendaraan> findByUserId(Integer userId); // Mencari semua kendaraan milik pengguna berdasarkan ID pengguna

    long countByUserId(Integer userId); // Menghitung jumlah kendaraan yang dimiliki pengguna

    @Modifying // Menandai bahwa query ini akan mengubah data di database
    @Query("UPDATE Kendaraan k SET k.isUtama = false WHERE k.userId = ?1") // Query: ubah semua kendaraan milik user menjadi bukan utama
    void resetUtamaByUserId(Integer userId); // Fungsi untuk menghapus tanda "utama" dari semua kendaraan milik user tertentu
}
