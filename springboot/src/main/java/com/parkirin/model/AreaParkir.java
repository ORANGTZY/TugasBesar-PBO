package com.parkirin.model; // Nama paket/folder tempat file ini berada

import jakarta.persistence.*; // Mengimpor semua alat untuk menghubungkan kelas ini ke tabel database
import lombok.*; // Mengimpor alat Lombok agar kode lebih ringkas

@Data // Otomatis membuat getter, setter, dan toString untuk semua field
@Entity // Menandai bahwa kelas ini mewakili sebuah tabel di database
@Table(name = "area_parkir") // Nama tabel di database yang diwakili kelas ini adalah "area_parkir"
@NoArgsConstructor // Otomatis membuat konstruktor tanpa parameter
@AllArgsConstructor // Otomatis membuat konstruktor dengan semua parameter
public class AreaParkir { // Kelas yang merepresentasikan data lokasi/gedung parkir
    @Id // Menandai field ini sebagai kunci utama tabel
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID dibuat otomatis oleh database
    private Integer id; // Nomor unik pengenal setiap area/gedung parkir

    @Column(nullable = false, length = 50) // Nama area wajib diisi, maksimal 50 karakter
    private String nama; // Nama gedung atau area parkir (contoh: RITA Supermall)

    @Column(name = "kapasitas_total", nullable = false) // Nama kolom "kapasitas_total", wajib diisi
    private Integer kapasitasTotal; // Total slot parkir yang tersedia di area ini

    @Column(length = 255) // Keterangan lokasi, maksimal 255 karakter
    private String lokasi; // Keterangan lokasi area parkir (contoh: Purwokerto - Beroperasi)
}
