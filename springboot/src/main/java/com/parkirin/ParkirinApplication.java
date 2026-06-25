package com.parkirin; // Nama paket/folder tempat file ini berada

import org.springframework.boot.SpringApplication; // Alat untuk menjalankan aplikasi Spring Boot
import org.springframework.boot.autoconfigure.SpringBootApplication; // Penanda bahwa ini adalah aplikasi Spring Boot utama
import org.springframework.scheduling.annotation.EnableScheduling; // Mengaktifkan fitur penjadwalan otomatis (tugas terjadwal)

@SpringBootApplication // Menandai kelas ini sebagai titik awal aplikasi Spring Boot
@EnableScheduling // Mengizinkan program menjalankan tugas secara otomatis berdasarkan jadwal waktu
public class ParkirinApplication { // Kelas utama aplikasi Parkirin

    public static void main(String[] args) { // Metode utama yang pertama kali dijalankan saat aplikasi dinyalakan
        SpringApplication.run(ParkirinApplication.class, args); // Menjalankan seluruh aplikasi Spring Boot
    }
}
