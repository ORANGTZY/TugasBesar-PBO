package com.parkirin.repository; // Nama paket/folder tempat file ini berada

import com.parkirin.model.UserPin; // Mengimpor kelas model UserPin
import org.springframework.data.jpa.repository.JpaRepository; // Mengimpor alat bawaan Spring yang menyediakan fungsi CRUD otomatis
import org.springframework.stereotype.Repository; // Penanda bahwa ini adalah kelas repository
import java.util.Optional; // Mengimpor Optional, digunakan saat hasil pencarian bisa ada atau tidak ada

@Repository // Menandai interface ini sebagai komponen repository yang dikelola Spring
public interface UserPinRepository extends JpaRepository<UserPin, Integer> { // Interface ini mengelola data tabel user_pins
    Optional<UserPin> findByUserId(Integer userId); // Mencari data PIN berdasarkan ID pengguna — hasilnya bisa ada (jika PIN sudah dibuat) atau tidak ada
}
