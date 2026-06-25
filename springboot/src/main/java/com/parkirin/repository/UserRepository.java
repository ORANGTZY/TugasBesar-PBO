package com.parkirin.repository; // Nama paket/folder tempat file ini berada

import com.parkirin.model.User; // Mengimpor kelas model User
import org.springframework.data.jpa.repository.JpaRepository; // Mengimpor alat bawaan Spring yang menyediakan fungsi CRUD otomatis
import org.springframework.data.jpa.repository.Modifying; // Untuk menandai query yang mengubah data
import org.springframework.data.jpa.repository.Query; // Untuk menulis query khusus
import org.springframework.stereotype.Repository; // Penanda bahwa ini adalah kelas repository
import java.util.Optional; // Mengimpor Optional, digunakan saat hasil pencarian bisa ada atau tidak ada

@Repository // Menandai interface ini sebagai komponen repository yang dikelola Spring
public interface UserRepository extends JpaRepository<User, Integer> { // Interface ini mengelola data tabel users
    Optional<User> findByEmail(String email); // Mencari pengguna berdasarkan alamat email (untuk cek apakah email sudah terdaftar)

    Optional<User> findByEmailAndNoTelepon(String email, String noTelepon); // Mencari pengguna berdasarkan kombinasi email dan nomor telepon (dipakai saat login)

    @Modifying // Menandai bahwa query ini akan mengubah data di database
    @Query("UPDATE User u SET u.points = u.points + ?2 WHERE u.id = ?1") // Query: tambahkan poin ke pengguna tertentu
    void addPoints(Integer userId, Integer points); // Menambahkan poin reward ke pengguna setelah selesai parkir
}
