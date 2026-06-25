package com.parkirin.model; // Nama paket/folder tempat file ini berada

import jakarta.persistence.*; // Mengimpor semua alat untuk menghubungkan kelas ini ke tabel database
import lombok.*; // Mengimpor alat Lombok agar kode lebih ringkas

@Data // Otomatis membuat getter, setter, dan toString untuk semua field
@Entity // Menandai bahwa kelas ini mewakili sebuah tabel di database
@Table(name = "slot_parkir") // Nama tabel di database yang diwakili kelas ini adalah "slot_parkir"
@NoArgsConstructor // Otomatis membuat konstruktor tanpa parameter
@AllArgsConstructor // Otomatis membuat konstruktor dengan semua parameter
public class SlotParkir { // Kelas yang merepresentasikan satu slot/tempat parkir
    @Id // Menandai field ini sebagai kunci utama tabel
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID dibuat otomatis oleh database
    private Integer id; // Nomor unik pengenal setiap slot parkir

    @Column(nullable = false, length = 10) // Nama lantai wajib diisi, maks 10 karakter
    private String lantai; // Lantai tempat slot parkir berada (contoh: Lantai B1, Lantai P1)

    @Column(nullable = false, length = 10) // Blok wajib diisi, maks 10 karakter
    private String blok; // Blok/zona slot parkir (contoh: Mobil, Blok A, Blok B)

    @Column(name = "no_slot", nullable = false, length = 10) // Nama kolom "no_slot", wajib diisi, maks 10 karakter
    private String noSlot; // Nomor slot parkir (contoh: B1-1, P1-3)

    @Column(columnDefinition = "VARCHAR(10) DEFAULT 'Tersedia'") // Status slot, nilai awalnya "Tersedia"
    private String status = "Tersedia"; // Status slot saat ini: "Tersedia" atau "Terisi"

    @Column(name = "area_parkir_id", nullable = false) // Kolom "area_parkir_id", wajib diisi
    private Integer areaParkirId; // ID area/gedung parkir tempat slot ini berada
}
