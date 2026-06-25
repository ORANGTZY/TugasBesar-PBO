package com.parkirin.model; // Nama paket/folder tempat file ini berada

import jakarta.persistence.*; // Mengimpor semua alat untuk menghubungkan kelas ini ke tabel database
import lombok.*; // Mengimpor alat Lombok agar kode lebih ringkas

@Data // Otomatis membuat getter, setter, dan toString untuk semua field
@Entity // Menandai bahwa kelas ini mewakili sebuah tabel di database
@Table(name = "tarif") // Nama tabel di database yang diwakili kelas ini adalah "tarif"
@NoArgsConstructor // Otomatis membuat konstruktor tanpa parameter
@AllArgsConstructor // Otomatis membuat konstruktor dengan semua parameter
public class Tarif { // Kelas yang merepresentasikan data harga/tarif parkir
    @Id // Menandai field ini sebagai kunci utama tabel
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID dibuat otomatis oleh database
    private Integer id; // Nomor unik pengenal setiap tarif

    @Column(name = "jenis_kendaraan", nullable = false, length = 10) // Kolom "jenis_kendaraan", wajib diisi, maks 10 karakter
    private String jenisKendaraan; // Jenis kendaraan yang dikenai tarif ini: "mobil" atau "motor"

    @Column(name = "tarif_jam_pertama", nullable = false) // Kolom "tarif_jam_pertama", wajib diisi
    private Float tarifJamPertama; // Harga parkir untuk jam pertama (dalam rupiah)

    @Column(name = "tarif_jam_berikutnya", nullable = false) // Kolom "tarif_jam_berikutnya", wajib diisi
    private Float tarifJamBerikutnya; // Harga parkir untuk setiap jam berikutnya setelah jam pertama
}
