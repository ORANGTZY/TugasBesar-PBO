package com.parkirin.controller.api; // Nama paket/folder tempat file ini berada

import com.parkirin.model.Tarif; // Mengimpor model Tarif untuk mengelola data tarif
import com.parkirin.repository.TarifRepository; // Mengimpor repository untuk operasi database Tarif
import lombok.RequiredArgsConstructor; // Otomatis membuat konstruktor berdasarkan field yang bersifat final
import org.springframework.http.ResponseEntity; // Alat untuk membuat respons HTTP dengan status dan data
import org.springframework.web.bind.annotation.*; // Mengimpor semua anotasi untuk menangani request API
import java.util.*; // Mengimpor berbagai tipe data koleksi

@RestController // Menandai kelas ini sebagai controller API yang mengirim data (bukan halaman HTML)
@RequestMapping("/api/tarif") // Semua URL di kelas ini diawali dengan "/api/tarif"
@RequiredArgsConstructor // Otomatis membuat konstruktor berdasarkan field final
public class TarifApiController { // Kelas ini menangani permintaan terkait data tarif/harga parkir

    private final TarifRepository tarifRepository; // Alat untuk mengakses data tarif di database

    @GetMapping // Menangani permintaan GET ke URL /api/tarif (ambil semua tarif)
    public ResponseEntity<?> getAll() { // Fungsi untuk mengambil semua data tarif dari database
        List<Tarif> tarifs = tarifRepository.findAll(); // Ambil semua data tarif dari database
        List<Map<String, Object>> data = new ArrayList<>(); // Buat daftar kosong untuk menampung hasil
        for (Tarif t : tarifs) { // Proses setiap data tarif satu per satu
            Map<String, Object> item = new LinkedHashMap<>(); // Buat wadah untuk satu data tarif
            item.put("jenis_kendaraan", t.getJenisKendaraan()); // Masukkan jenis kendaraan ke wadah
            item.put("tarif_jam_pertama", t.getTarifJamPertama()); // Masukkan harga jam pertama ke wadah
            item.put("tarif_jam_berikutnya", t.getTarifJamBerikutnya()); // Masukkan harga jam berikutnya ke wadah
            data.add(item); // Tambahkan data tarif ini ke daftar
        }
        return ResponseEntity.ok(Map.of("status", "success", "data", data)); // Kirim semua data tarif sebagai respons
    }
}
