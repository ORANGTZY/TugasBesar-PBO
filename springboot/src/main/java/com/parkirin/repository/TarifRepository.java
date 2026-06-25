package com.parkirin.repository; // Nama paket/folder tempat file ini berada

import com.parkirin.model.Tarif; // Mengimpor kelas model Tarif yang akan diakses ke database
import org.springframework.data.jpa.repository.JpaRepository; // Mengimpor alat bawaan Spring yang menyediakan fungsi CRUD otomatis
import org.springframework.stereotype.Repository; // Penanda bahwa ini adalah kelas repository

@Repository // Menandai interface ini sebagai komponen repository yang dikelola Spring
public interface TarifRepository extends JpaRepository<Tarif, Integer> { // Interface ini mengelola data tabel tarif — fungsi dasar (simpan, cari, hapus) sudah tersedia otomatis
}
