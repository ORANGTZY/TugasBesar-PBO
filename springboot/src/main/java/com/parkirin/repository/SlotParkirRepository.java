package com.parkirin.repository; // Nama paket/folder tempat file ini berada

import com.parkirin.model.SlotParkir; // Mengimpor kelas model SlotParkir yang akan diakses ke database
import org.springframework.data.jpa.repository.JpaRepository; // Mengimpor alat bawaan Spring yang menyediakan fungsi CRUD otomatis
import org.springframework.data.jpa.repository.Modifying; // Untuk menandai query yang mengubah data
import org.springframework.data.jpa.repository.Query; // Untuk menulis query khusus
import org.springframework.stereotype.Repository; // Penanda bahwa ini adalah kelas repository
import java.util.List; // Mengimpor tipe data List (daftar)
import java.util.Optional; // Mengimpor Optional, digunakan saat hasil pencarian bisa ada atau tidak ada

@Repository // Menandai interface ini sebagai komponen repository yang dikelola Spring
public interface SlotParkirRepository extends JpaRepository<SlotParkir, Integer> { // Interface ini mengelola data tabel slot_parkir
    List<SlotParkir> findByAreaParkirIdAndLantaiAndBlok(Integer areaId, String lantai, String blok); // Mencari semua slot berdasarkan area, lantai, dan blok tertentu (untuk tampilan slot mobil)

    Optional<SlotParkir> findFirstByAreaParkirIdAndLantaiAndBlokAndStatus(Integer areaId, String lantai, String blok, String status); // Mencari slot PERTAMA yang tersedia di blok motor tertentu

    Optional<SlotParkir> findByAreaParkirIdAndLantaiAndNoSlotAndStatus(Integer areaId, String lantai, String noSlot, String status); // Mencari slot mobil spesifik berdasarkan nomor slot dan statusnya

    @Query("SELECT s.blok, COUNT(s) as total, SUM(CASE WHEN s.status = 'Tersedia' THEN 1 ELSE 0 END) as tersedia FROM SlotParkir s WHERE s.areaParkirId = ?1 AND s.lantai = ?2 AND s.blok != 'Mobil' GROUP BY s.blok") // Query: ringkasan slot motor per blok (total dan yang tersedia)
    List<Object[]> findMotorBlokSummary(Integer areaId, String lantai); // Mengambil ringkasan ketersediaan slot motor per blok di lantai tertentu

    @Query("SELECT COUNT(s) FROM SlotParkir s WHERE s.areaParkirId = ?1") // Query: hitung total semua slot di area tertentu
    Long countTotalByAreaId(Integer areaId); // Menghitung total jumlah slot parkir di sebuah area

    @Query("SELECT COUNT(s) FROM SlotParkir s WHERE s.areaParkirId = ?1 AND s.status = 'Tersedia'") // Query: hitung slot yang masih tersedia di area tertentu
    Long countAvailableByAreaId(Integer areaId); // Menghitung jumlah slot parkir yang masih kosong di sebuah area

    @Modifying // Menandai bahwa query ini akan mengubah data di database
    @Query("UPDATE SlotParkir s SET s.status = 'Tersedia'") // Query: ubah status semua slot menjadi "Tersedia"
    void resetAllSlots(); // Mengosongkan/mereset semua slot parkir (dijalankan otomatis setiap pagi jam 08.00)
}
