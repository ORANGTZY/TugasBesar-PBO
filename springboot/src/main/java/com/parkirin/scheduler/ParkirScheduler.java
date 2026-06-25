package com.parkirin.scheduler; // Nama paket/folder tempat file ini berada

import com.parkirin.model.Reservasi; // Mengimpor model Reservasi untuk mengelola data tiket parkir
import com.parkirin.repository.ReservasiRepository; // Mengimpor repository untuk operasi database Reservasi
import com.parkirin.repository.SlotParkirRepository; // Mengimpor repository untuk operasi database SlotParkir
import lombok.RequiredArgsConstructor; // Otomatis membuat konstruktor yang menerima semua field final
import lombok.extern.slf4j.Slf4j; // Mengaktifkan fitur pencatatan log (log.info, log.error, dll.)
import org.springframework.scheduling.annotation.Scheduled; // Alat untuk menjalankan fungsi secara otomatis berdasarkan jadwal
import org.springframework.stereotype.Component; // Penanda bahwa kelas ini adalah komponen yang dikelola Spring
import jakarta.transaction.Transactional; // Memastikan operasi database dilakukan semuanya atau tidak sama sekali
import java.time.LocalDateTime; // Mengimpor tipe data tanggal dan waktu
import java.time.LocalTime; // Mengimpor tipe data khusus waktu (jam, menit, detik)
import java.util.List; // Mengimpor tipe data List (daftar)

/**
 * Sistem otomasi parkir — berjalan setiap 30 detik.
 * Memindahkan logika setInterval dari backend Node.js asli ke Spring @Scheduled.
 */
@Component // Menandai kelas ini sebagai komponen yang dikelola dan dijalankan otomatis oleh Spring
@RequiredArgsConstructor // Otomatis membuat konstruktor berdasarkan field yang bersifat final
@Slf4j // Mengaktifkan fitur log agar program bisa mencatat aktivitas ke konsol
public class ParkirScheduler { // Kelas yang bertugas menjalankan tugas otomatis terkait manajemen parkir

    private final ReservasiRepository reservasiRepository; // Alat untuk mengakses dan mengubah data reservasi di database
    private final SlotParkirRepository slotParkirRepository; // Alat untuk mengakses dan mengubah data slot parkir di database

    private boolean hasResetToday = false; // Penanda apakah reset harian sudah dilakukan hari ini (mencegah reset berulang)

    @Scheduled(fixedRate = 30000)  // Fungsi ini dijalankan otomatis setiap 30 detik (30.000 milidetik)
    @Transactional // Semua operasi database di dalam fungsi ini dilakukan bersama-sama
    public void autoManageParking() { // Fungsi utama yang mengelola parkir secara otomatis
        LocalDateTime now = LocalDateTime.now(); // Ambil waktu saat ini
        int jam = now.getHour(); // Ambil jam saat ini (0-23)
        int menit = now.getMinute(); // Ambil menit saat ini (0-59)

        // A. LOGIKA RESET JAM 08:00 PAGI
        if (jam == 8 && menit == 0 && !hasResetToday) { // Cek apakah sekarang tepat jam 08:00 dan reset belum dilakukan hari ini
            try {
                slotParkirRepository.resetAllSlots(); // Ubah status semua slot parkir menjadi "Tersedia"
                reservasiRepository.cancelAllActive(); // Batalkan semua reservasi yang masih aktif atau menunggu
                log.info("[SISTEM] JAM 08:00 Pagi - Lokasi Parkir Dibuka, Semua Slot Kembali Tersedia."); // Catat ke log bahwa reset berhasil
                hasResetToday = true; // Tandai bahwa reset sudah dilakukan hari ini
            } catch (Exception e) {
                log.error("Gagal reset parkiran:", e); // Catat ke log jika ada error saat reset
            }
        }
        if (jam == 8 && menit > 0) { // Setelah jam 08:00 lewat (menit > 0)
            hasResetToday = false; // Reset penanda agar reset bisa jalan lagi besok
        }

        // B. LOGIKA AUTO-KEDALUWARSA TIKET (Lewat dari 2 Jam tanpa check-in)
        try {
            List<Reservasi> expired = reservasiRepository.findExpiredActiveReservations(now); // Cari semua tiket yang sudah melewati batas waktu tanpa check-in
            if (!expired.isEmpty()) { // Jika ada tiket yang kedaluwarsa
                for (Reservasi r : expired) { // Proses setiap tiket yang kedaluwarsa satu per satu
                    slotParkirRepository.findById(r.getSlotParkirId()).ifPresent(s -> { // Cari slot parkir yang terkait dengan tiket ini
                        s.setStatus("Tersedia"); // Ubah status slot menjadi "Tersedia" lagi
                        slotParkirRepository.save(s); // Simpan perubahan slot ke database
                    });
                    r.setStatus("Dibatalkan"); // Ubah status tiket menjadi "Dibatalkan"
                    reservasiRepository.save(r); // Simpan perubahan tiket ke database
                }
                log.info("[SISTEM] {} Tiket kedaluwarsa telah dihapus dan slot kembali tersedia.", expired.size()); // Catat ke log berapa tiket yang dibatalkan
            }
        } catch (Exception e) {
            log.error("Gagal membersihkan tiket kedaluwarsa:", e); // Catat ke log jika ada error saat membersihkan tiket kedaluwarsa
        }
    }
}
