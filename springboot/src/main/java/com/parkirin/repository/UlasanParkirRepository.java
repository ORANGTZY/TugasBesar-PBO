package com.parkirin.repository; // Nama paket/folder tempat file ini berada

import com.parkirin.model.UlasanParkir; // Mengimpor kelas model UlasanParkir
import org.springframework.data.jpa.repository.JpaRepository; // Mengimpor alat bawaan Spring yang menyediakan fungsi CRUD otomatis
import org.springframework.stereotype.Repository; // Penanda bahwa ini adalah kelas repository

@Repository // Menandai interface ini sebagai komponen repository yang dikelola Spring
public interface UlasanParkirRepository extends JpaRepository<UlasanParkir, Integer> { // Interface ini mengelola data tabel ulasan_parkir — fungsi dasar sudah tersedia otomatis
}
