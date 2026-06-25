package com.parkirin.repository; // Nama paket/folder tempat file ini berada

import com.parkirin.model.AreaParkir; // Mengimpor kelas model AreaParkir yang akan diakses ke database
import org.springframework.data.jpa.repository.JpaRepository; // Mengimpor alat bawaan Spring yang menyediakan fungsi CRUD otomatis
import org.springframework.stereotype.Repository; // Penanda bahwa ini adalah kelas repository (penghubung ke database)

import org.springframework.data.jpa.repository.Modifying; // Untuk menandai query yang mengubah data (INSERT, UPDATE, DELETE)
import org.springframework.data.jpa.repository.Query; // Untuk menulis query SQL manual

@Repository // Menandai interface ini sebagai komponen repository yang dikelola Spring
public interface AreaParkirRepository extends JpaRepository<AreaParkir, Integer> { // Interface ini mengelola data tabel area_parkir, ID-nya bertipe Integer
    @Modifying // Menandai bahwa query ini akan mengubah data di database
    @Query(value = "INSERT IGNORE INTO area_parkir (id, nama, kapasitas_total, lokasi) VALUES (?1, ?2, ?3, ?4)", nativeQuery = true) // Query SQL langsung: masukkan data ke tabel, abaikan jika ID sudah ada
    void insertWithId(Integer id, String nama, Integer kapasitasTotal, String lokasi); // Fungsi untuk menambahkan area parkir baru dengan ID yang sudah ditentukan
}
