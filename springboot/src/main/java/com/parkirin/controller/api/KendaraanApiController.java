package com.parkirin.controller.api; // Nama paket/folder tempat file ini berada

import com.parkirin.model.Kendaraan; // Mengimpor model Kendaraan untuk mengelola data kendaraan
import com.parkirin.repository.KendaraanRepository; // Mengimpor repository untuk operasi database Kendaraan
import lombok.RequiredArgsConstructor; // Otomatis membuat konstruktor berdasarkan field yang bersifat final
import org.springframework.http.ResponseEntity; // Alat untuk membuat respons HTTP dengan status dan data
import org.springframework.web.bind.annotation.*; // Mengimpor semua anotasi untuk menangani request API
import jakarta.transaction.Transactional; // Memastikan operasi database dilakukan semuanya atau tidak sama sekali
import java.util.*; // Mengimpor berbagai tipe data koleksi

@RestController // Menandai kelas ini sebagai controller API yang mengirim data (bukan halaman HTML)
@RequestMapping("/api/kendaraan") // Semua URL di kelas ini diawali dengan "/api/kendaraan"
@RequiredArgsConstructor // Otomatis membuat konstruktor berdasarkan field final
public class KendaraanApiController { // Kelas ini menangani semua operasi terkait data kendaraan pengguna

    private final KendaraanRepository kendaraanRepository; // Alat untuk mengakses data kendaraan di database

    /** Safe helper: converts value from JSON (could be Number or String) to Integer */
    private Integer toInt(Object val) { // Fungsi pembantu untuk mengubah nilai dari JSON menjadi Integer dengan aman
        if (val == null) return null; // Jika nilainya kosong, kembalikan null
        if (val instanceof Number) return ((Number) val).intValue(); // Jika sudah berupa angka, langsung ubah ke Integer
        String s = val.toString().trim(); // Ubah ke teks dan hapus spasi di pinggir
        if (s.isEmpty()) return null; // Jika teks kosong, kembalikan null
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return null; } // Coba ubah teks ke Integer, jika gagal kembalikan null
    }

    @GetMapping("/{userId}") // Menangani permintaan GET ke URL /api/kendaraan/{userId}
    public ResponseEntity<?> getByUserId(@PathVariable Integer userId) { // Menerima ID pengguna dari URL
        List<Kendaraan> vehicles = kendaraanRepository.findByUserId(userId); // Ambil semua kendaraan milik pengguna ini
        return ResponseEntity.ok(Map.of("status", "success", "data", vehicles)); // Kirim daftar kendaraan sebagai respons
    }

    @GetMapping("/detail/{id}") // Menangani permintaan GET ke URL /api/kendaraan/detail/{id}
    public ResponseEntity<?> getDetail(@PathVariable Integer id) { // Menerima ID kendaraan dari URL
        Optional<Kendaraan> vehicle = kendaraanRepository.findById(id); // Cari kendaraan berdasarkan ID
        Map<String, Object> response = new LinkedHashMap<>(); // Buat wadah untuk respons
        response.put("status", "success"); // Tandai status berhasil
        response.put("data", vehicle.orElse(null)); // Masukkan data kendaraan (atau null jika tidak ditemukan)
        return ResponseEntity.ok(response); // Kirim respons
    }

    @PostMapping // Menangani permintaan POST ke URL /api/kendaraan (tambah kendaraan baru)
    public ResponseEntity<?> create(@RequestBody Map<String, Object> body) { // Menerima data kendaraan baru dalam format JSON
        try {
            Integer userId = toInt(body.get("user_id")); // Ambil ID pengguna pemilik kendaraan
            boolean isUtama = kendaraanRepository.countByUserId(userId) == 0; // Kendaraan ini jadi utama jika belum punya kendaraan sebelumnya

            Kendaraan k = new Kendaraan(); // Buat objek kendaraan baru
            k.setUserId(userId); // Isi ID pengguna pemilik
            k.setJenis((String) body.get("jenis")); // Isi jenis kendaraan (mobil/motor)
            k.setPlat((String) body.get("plat")); // Isi nomor plat kendaraan
            k.setMerek((String) body.get("merek")); // Isi merek kendaraan
            k.setModel((String) body.get("model")); // Isi model/tipe kendaraan
            k.setTahun(toInt(body.get("tahun"))); // Isi tahun pembuatan kendaraan
            k.setWarna((String) body.get("warna")); // Isi warna kendaraan
            k.setMesin((String) body.get("mesin")); // Isi nomor mesin kendaraan
            k.setRangka((String) body.get("rangka")); // Isi nomor rangka kendaraan
            k.setIsUtama(isUtama); // Tandai apakah ini kendaraan utama
            kendaraanRepository.save(k); // Simpan kendaraan baru ke database

            return ResponseEntity.ok(Map.of("status", "success", "message", "Kendaraan ditambahkan!")); // Kirim respons sukses
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("status", "error", "message", "Gagal: " + e.getMessage())); // Kirim respons error jika terjadi masalah
        }
    }

    @PutMapping("/{id}") // Menangani permintaan PUT ke URL /api/kendaraan/{id} (edit data kendaraan)
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Map<String, Object> body) { // Menerima ID kendaraan dan data baru
        Optional<Kendaraan> existing = kendaraanRepository.findById(id); // Cari kendaraan yang akan diedit
        if (existing.isEmpty()) { // Jika kendaraan tidak ditemukan
            return ResponseEntity.status(404).body(Map.of("status", "error", "message", "Kendaraan tidak ditemukan")); // Kirim pesan error 404
        }
        Kendaraan k = existing.get(); // Ambil data kendaraan yang ditemukan
        k.setJenis((String) body.get("jenis")); // Perbarui jenis kendaraan
        k.setPlat((String) body.get("plat")); // Perbarui nomor plat
        k.setMerek((String) body.get("merek")); // Perbarui merek kendaraan
        k.setModel((String) body.get("model")); // Perbarui model kendaraan
        k.setTahun(toInt(body.get("tahun"))); // Perbarui tahun pembuatan
        k.setWarna((String) body.get("warna")); // Perbarui warna kendaraan
        k.setMesin((String) body.get("mesin")); // Perbarui nomor mesin
        k.setRangka((String) body.get("rangka")); // Perbarui nomor rangka
        kendaraanRepository.save(k); // Simpan perubahan ke database
        return ResponseEntity.ok(Map.of("status", "success", "message", "Kendaraan diperbarui!")); // Kirim respons sukses
    }

    @PutMapping("/utama/{id}") // Menangani permintaan PUT ke URL /api/kendaraan/utama/{id} (ganti kendaraan utama)
    @Transactional // Operasi database dilakukan semuanya atau tidak sama sekali
    public ResponseEntity<?> setUtama(@PathVariable Integer id, @RequestBody Map<String, Object> body) { // Menerima ID kendaraan baru yang akan dijadikan utama
        Integer userId = toInt(body.get("user_id")); // Ambil ID pengguna
        kendaraanRepository.resetUtamaByUserId(userId); // Hapus tanda "utama" dari semua kendaraan milik pengguna ini
        Optional<Kendaraan> kOpt = kendaraanRepository.findById(id); // Cari kendaraan yang akan dijadikan utama
        if (kOpt.isPresent()) { // Jika kendaraan ditemukan
            Kendaraan k = kOpt.get(); // Ambil data kendaraan
            k.setIsUtama(true); // Tandai sebagai kendaraan utama
            kendaraanRepository.save(k); // Simpan perubahan ke database
        }
        return ResponseEntity.ok(Map.of("status", "success", "message", "Kendaraan utama diubah!")); // Kirim respons sukses
    }

    @DeleteMapping("/{id}") // Menangani permintaan DELETE ke URL /api/kendaraan/{id} (hapus kendaraan)
    public ResponseEntity<?> delete(@PathVariable Integer id) { // Menerima ID kendaraan yang akan dihapus
        kendaraanRepository.deleteById(id); // Hapus kendaraan dari database
        return ResponseEntity.ok(Map.of("status", "success", "message", "Kendaraan dihapus!")); // Kirim respons sukses
    }
}
