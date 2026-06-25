package com.parkirin.controller.api; // Nama paket/folder tempat file ini berada

import java.time.LocalDateTime; // Mengimpor tipe data untuk menyimpan tanggal dan waktu
import java.time.temporal.ChronoUnit; // Alat untuk menghitung selisih antara dua waktu (menit, jam, dll.)
import java.util.ArrayList; // Mengimpor tipe data ArrayList (daftar yang bisa berubah ukurannya)
import java.util.LinkedHashMap; // Mengimpor tipe data Map yang menjaga urutan data
import java.util.List; // Mengimpor tipe data List (daftar)
import java.util.Map; // Mengimpor tipe data Map (pasangan kunci-nilai)
import java.util.Optional; // Mengimpor Optional, digunakan saat hasil pencarian bisa ada atau tidak ada

import org.springframework.beans.factory.annotation.Value; // Alat untuk membaca nilai konfigurasi dari file properties
import org.springframework.http.ResponseEntity; // Alat untuk membuat respons HTTP dengan status dan data
import org.springframework.web.bind.annotation.GetMapping; // Menangani permintaan HTTP GET
import org.springframework.web.bind.annotation.PathVariable; // Mengambil nilai dari bagian URL
import org.springframework.web.bind.annotation.PostMapping; // Menangani permintaan HTTP POST
import org.springframework.web.bind.annotation.RequestBody; // Mengambil data dari isi permintaan (body)
import org.springframework.web.bind.annotation.RequestMapping; // Menentukan awalan URL untuk semua fungsi
import org.springframework.web.bind.annotation.RestController; // Menandai kelas sebagai controller API

import com.midtrans.Config; // Konfigurasi koneksi ke layanan pembayaran Midtrans
import com.midtrans.ConfigFactory; // Pembuat objek konfigurasi Midtrans
import com.midtrans.service.MidtransSnapApi; // API Midtrans Snap untuk membuat halaman pembayaran
import com.parkirin.model.Reservasi; // Model data reservasi parkir
import com.parkirin.model.SlotParkir; // Model data slot parkir
import com.parkirin.model.Tarif; // Model data tarif parkir
import com.parkirin.model.TransaksiPembayaran; // Model data transaksi pembayaran
import com.parkirin.model.UlasanParkir; // Model data ulasan parkir
import com.parkirin.model.User; // Model data pengguna
import com.parkirin.repository.AreaParkirRepository; // Repository untuk akses data area parkir
import com.parkirin.repository.ReservasiRepository; // Repository untuk akses data reservasi
import com.parkirin.repository.SlotParkirRepository; // Repository untuk akses data slot parkir
import com.parkirin.repository.TarifRepository; // Repository untuk akses data tarif
import com.parkirin.repository.TransaksiPembayaranRepository; // Repository untuk akses data transaksi
import com.parkirin.repository.UlasanParkirRepository; // Repository untuk akses data ulasan
import com.parkirin.repository.UserRepository; // Repository untuk akses data pengguna

import jakarta.annotation.PostConstruct; // Menandai fungsi yang dijalankan setelah objek dibuat
import jakarta.transaction.Transactional; // Memastikan operasi database dilakukan semuanya atau tidak sama sekali

@RestController // Menandai kelas ini sebagai controller API yang mengirim data (bukan halaman HTML)
@RequestMapping("/api/parkir") // Semua URL di kelas ini diawali dengan "/api/parkir"
public class ParkirApiController { // Kelas utama yang menangani semua operasi parkir: booking, pembayaran, ulasan, dll.

    private final SlotParkirRepository slotParkirRepository; // Alat untuk mengakses data slot parkir di database
    private final AreaParkirRepository areaParkirRepository; // Alat untuk mengakses data area/gedung parkir di database
    private final ReservasiRepository reservasiRepository; // Alat untuk mengakses data reservasi di database
    private final TarifRepository tarifRepository; // Alat untuk mengakses data tarif parkir di database
    private final UserRepository userRepository; // Alat untuk mengakses data pengguna di database
    private final TransaksiPembayaranRepository transaksiRepository; // Alat untuk mengakses data transaksi pembayaran di database
    private final UlasanParkirRepository ulasanRepository; // Alat untuk mengakses data ulasan parkir di database

    @Value("${midtrans.server-key}") // Baca nilai server-key Midtrans dari file konfigurasi (application.properties)
    private String midtransServerKey; // Server key Midtrans untuk autentikasi di sisi server

    @Value("${midtrans.client-key}") // Baca nilai client-key Midtrans dari file konfigurasi
    private String midtransClientKey; // Client key Midtrans untuk dipakai di sisi frontend

    @Value("${midtrans.is-production}") // Baca nilai mode produksi/sandbox Midtrans dari file konfigurasi
    private boolean midtransIsProduction; // true = mode produksi (uang sungguhan), false = mode sandbox (uji coba)

    private MidtransSnapApi snapApi; // Objek API Midtrans Snap untuk membuat token pembayaran

    public ParkirApiController(SlotParkirRepository slotParkirRepository, // Konstruktor untuk menerima semua dependency yang dibutuhkan
                               AreaParkirRepository areaParkirRepository, // Menerima repository area parkir
                               ReservasiRepository reservasiRepository, // Menerima repository reservasi
                               TarifRepository tarifRepository, // Menerima repository tarif
                               UserRepository userRepository, // Menerima repository pengguna
                               TransaksiPembayaranRepository transaksiRepository, // Menerima repository transaksi
                               UlasanParkirRepository ulasanRepository) { // Menerima repository ulasan
        this.slotParkirRepository = slotParkirRepository; // Simpan repository slot parkir
        this.areaParkirRepository = areaParkirRepository; // Simpan repository area parkir
        this.reservasiRepository = reservasiRepository; // Simpan repository reservasi
        this.tarifRepository = tarifRepository; // Simpan repository tarif
        this.userRepository = userRepository; // Simpan repository pengguna
        this.transaksiRepository = transaksiRepository; // Simpan repository transaksi
        this.ulasanRepository = ulasanRepository; // Simpan repository ulasan
    }

    @PostConstruct // Fungsi ini otomatis dijalankan setelah seluruh objek di kelas ini selesai dibuat
    public void initMidtrans() { // Fungsi untuk menginisialisasi koneksi ke Midtrans
        snapApi = new ConfigFactory(new Config(midtransServerKey, midtransClientKey, midtransIsProduction)) // Buat konfigurasi Midtrans dengan server key, client key, dan mode produksi
                .getSnapApi(); // Ambil objek API Snap dari konfigurasi tersebut
    }

    /** Safe helper: converts value from JSON (could be Number or String) to Integer */
    private Integer toInt(Object val) { // Fungsi pembantu untuk mengubah nilai JSON ke Integer dengan aman
        if (val == null) return null; // Jika nilainya kosong, kembalikan null
        if (val instanceof Number) return ((Number) val).intValue(); // Jika sudah angka, langsung ubah ke Integer
        try { return Integer.parseInt(val.toString().trim()); } catch (NumberFormatException e) { return null; } // Coba ubah teks ke Integer, jika gagal kembalikan null
    }

    /** Safe helper: converts value from JSON (could be Number or String) to Float */
    private Float toFloat(Object val) { // Fungsi pembantu untuk mengubah nilai JSON ke Float dengan aman
        if (val == null) return null; // Jika nilainya kosong, kembalikan null
        if (val instanceof Number) return ((Number) val).floatValue(); // Jika sudah angka, langsung ubah ke Float
        try { return Float.parseFloat(val.toString().trim()); } catch (NumberFormatException e) { return null; } // Coba ubah teks ke Float, jika gagal kembalikan null
    }

    // 1. SEEDER
    @GetMapping("/seed") // Menangani permintaan GET ke URL /api/parkir/seed (isi data awal ke database)
    @Transactional // Operasi database dilakukan semuanya atau tidak sama sekali
    public ResponseEntity<?> seed() { // Fungsi untuk mengisi data awal (slot parkir, area parkir) ke database
        try {
            ulasanRepository.deleteAll(); // Hapus semua data ulasan yang lama
            transaksiRepository.deleteAll(); // Hapus semua data transaksi yang lama
            reservasiRepository.deleteAll(); // Hapus semua data reservasi yang lama
            slotParkirRepository.deleteAll(); // Hapus semua data slot parkir yang lama

            if (!areaParkirRepository.existsById(1)) { // Cek apakah area parkir ID 1 sudah ada
                areaParkirRepository.insertWithId(1, "RITA Supermall", 200, "Purwokerto (Beroperasi)"); // Tambahkan area RITA Supermall jika belum ada
            }
            if (!areaParkirRepository.existsById(2)) { // Cek apakah area parkir ID 2 sudah ada
                areaParkirRepository.insertWithId(2, "Grand Indonesia", 500, "Jakarta (Belum Beroperasi)"); // Tambahkan area Grand Indonesia jika belum ada
            }

            String[] lantaiList = {"Lantai B1", "Lantai P1", "Lantai P2", "Lantai P3"}; // Daftar lantai yang akan dibuat slot parkirnya
            String[] blokMotor = {"Blok A", "Blok B", "Blok C", "Blok D"}; // Daftar blok untuk slot motor

            for (String l : lantaiList) { // Ulangi untuk setiap lantai
                String prefix = l.split(" ")[1] + "-"; // Ambil nama lantai pendek sebagai awalan nomor slot (contoh: "B1-")
                for (int i = 1; i <= 10; i++) { // Buat 10 slot mobil di setiap lantai
                    SlotParkir slot = new SlotParkir(); // Buat objek slot parkir baru
                    slot.setLantai(l); // Isi lantai
                    slot.setBlok("Mobil"); // Blok ini khusus untuk mobil
                    slot.setNoSlot(prefix + i); // Isi nomor slot (contoh: B1-1, B1-2, dst.)
                    slot.setStatus("Tersedia"); // Status awal adalah "Tersedia"
                    slot.setAreaParkirId(1); // Slot ini milik area parkir ID 1 (RITA Supermall)
                    slotParkirRepository.save(slot); // Simpan slot ke database
                }
                for (String b : blokMotor) { // Ulangi untuk setiap blok motor
                    for (int i = 1; i <= 10; i++) { // Buat 10 slot di setiap blok motor
                        SlotParkir slot = new SlotParkir(); // Buat objek slot parkir baru
                        slot.setLantai(l); // Isi lantai
                        slot.setBlok(b); // Isi blok (Blok A, B, C, atau D)
                        slot.setNoSlot(b + "-" + i); // Isi nomor slot (contoh: Blok A-1, Blok A-2, dst.)
                        slot.setStatus("Tersedia"); // Status awal adalah "Tersedia"
                        slot.setAreaParkirId(1); // Slot ini milik area parkir ID 1
                        slotParkirRepository.save(slot); // Simpan slot ke database
                    }
                }
            }
            return ResponseEntity.ok(Map.of("status", "success", "message", "BERHASIL! Semua Slot & Lantai (B1, P1, P2, P3) telah masuk ke Database.")); // Kirim respons sukses
        } catch (Exception e) {
            e.printStackTrace(); // Tampilkan detail error di konsol
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage())); // Kirim respons error
        }
    }

    // 2. GET SLOT MOBIL
    @GetMapping("/slots/mobil/{areaId}/{lantai}") // Menangani GET ke URL /api/parkir/slots/mobil/{areaId}/{lantai}
    public ResponseEntity<?> getSlotMobil(@PathVariable Integer areaId, @PathVariable String lantai) { // Menerima ID area dan nama lantai dari URL
        List<SlotParkir> slots = slotParkirRepository.findByAreaParkirIdAndLantaiAndBlok(areaId, lantai, "Mobil"); // Ambil semua slot mobil di area dan lantai yang diminta
        return ResponseEntity.ok(Map.of("status", "success", "data", slots)); // Kirim daftar slot sebagai respons
    }

    // 3. GET SLOT MOTOR
    @GetMapping("/slots/motor/{areaId}/{lantai}") // Menangani GET ke URL /api/parkir/slots/motor/{areaId}/{lantai}
    public ResponseEntity<?> getSlotMotor(@PathVariable Integer areaId, @PathVariable String lantai) { // Menerima ID area dan nama lantai dari URL
        List<Object[]> results = slotParkirRepository.findMotorBlokSummary(areaId, lantai); // Ambil ringkasan ketersediaan slot motor per blok
        List<Map<String, Object>> data = new ArrayList<>(); // Buat daftar kosong untuk menampung hasil
        for (Object[] row : results) { // Proses setiap baris data
            Map<String, Object> item = new LinkedHashMap<>(); // Buat wadah untuk satu blok motor
            item.put("blok", row[0]); // Nama blok (contoh: Blok A)
            item.put("total", row[1]); // Total slot di blok ini
            item.put("tersedia", row[2]); // Jumlah slot yang masih tersedia di blok ini
            data.add(item); // Tambahkan ke daftar
        }
        return ResponseEntity.ok(Map.of("status", "success", "data", data)); // Kirim data ringkasan blok motor sebagai respons
    }

    // 4. BOOKING
    @PostMapping("/booking") // Menangani POST ke URL /api/parkir/booking (proses pemesanan parkir)
    @Transactional // Operasi database dilakukan semuanya atau tidak sama sekali
    public ResponseEntity<?> booking(@RequestBody Map<String, Object> body) { // Menerima data pemesanan dalam format JSON
        try {
            Integer userId = toInt(body.get("user_id")); // Ambil ID pengguna yang memesan
            Integer areaId = toInt(body.get("area_id")); // Ambil ID area parkir yang dipilih
            String jenisKendaraan = (String) body.get("jenis_kendaraan"); // Ambil jenis kendaraan (mobil atau motor)
            String lantai = (String) body.get("lantai"); // Ambil lantai yang dipilih
            String slotName = (String) body.get("slot_name"); // Ambil nama slot atau blok yang dipilih

            SlotParkir slot; // Variabel untuk menyimpan slot yang akan dipesan
            if ("mobil".equals(jenisKendaraan)) { // Jika jenis kendaraan adalah mobil
                Optional<SlotParkir> slotOpt = slotParkirRepository.findByAreaParkirIdAndLantaiAndNoSlotAndStatus(areaId, lantai, slotName, "Tersedia"); // Cari slot spesifik yang diminta dan pastikan masih tersedia
                if (slotOpt.isEmpty()) { // Jika slot sudah tidak tersedia
                    return ResponseEntity.badRequest().body(Map.of("error", "Maaf, slot ini baru saja dipesan orang lain!")); // Kirim pesan error
                }
                slot = slotOpt.get(); // Ambil slot yang ditemukan
            } else { // Jika jenis kendaraan adalah motor
                Optional<SlotParkir> slotOpt = slotParkirRepository.findFirstByAreaParkirIdAndLantaiAndBlokAndStatus(areaId, lantai, slotName, "Tersedia"); // Cari slot pertama yang tersedia di blok yang diminta
                if (slotOpt.isEmpty()) { // Jika blok sudah penuh
                    return ResponseEntity.badRequest().body(Map.of("error", "Maaf, blok ini sudah terlanjur penuh!")); // Kirim pesan error
                }
                slot = slotOpt.get(); // Ambil slot yang ditemukan
            }

            slot.setStatus("Terisi"); // Ubah status slot menjadi "Terisi"
            slotParkirRepository.save(slot); // Simpan perubahan status slot ke database

            String kodeUnik = "TKT" + (int)(Math.random() * 100000000); // Buat kode tiket unik secara acak (contoh: TKT12345678)
            LocalDateTime estimasi = LocalDateTime.now().plusHours(2); // Estimasi kedatangan adalah 2 jam dari sekarang
            int tarifId = "mobil".equals(jenisKendaraan) ? 2 : 1; // Tentukan ID tarif: 2 untuk mobil, 1 untuk motor

            Reservasi reservasi = new Reservasi(); // Buat objek reservasi baru
            reservasi.setKodeUnik(kodeUnik); // Isi kode tiket unik
            reservasi.setEstimasiKedatangan(estimasi); // Isi estimasi kedatangan
            reservasi.setUserId(userId); // Isi ID pengguna
            reservasi.setSlotParkirId(slot.getId()); // Isi ID slot yang dipesan
            reservasi.setTarifId(tarifId); // Isi ID tarif yang digunakan
            reservasi.setStatus("Aktif"); // Status reservasi awal adalah "Aktif"
            reservasiRepository.save(reservasi); // Simpan reservasi baru ke database

            return ResponseEntity.ok(Map.of("status", "success", "kode_unik", kodeUnik)); // Kirim respons sukses beserta kode tiket
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage())); // Kirim respons error jika terjadi masalah
        }
    }

    // 5. GET RESERVASI USER
    @GetMapping("/reservasi/user/{userId}") // Menangani GET ke URL /api/parkir/reservasi/user/{userId} (riwayat parkir)
    public ResponseEntity<?> getReservasiUser(@PathVariable Integer userId) { // Menerima ID pengguna dari URL
        List<Reservasi> reservations = reservasiRepository.findByUserIdOrderByWaktuPesanDesc(userId); // Ambil semua riwayat parkir pengguna, terbaru duluan
        List<Map<String, Object>> data = new ArrayList<>(); // Buat daftar kosong untuk menampung hasil
        for (Reservasi r : reservations) { // Proses setiap reservasi satu per satu
            Map<String, Object> item = new LinkedHashMap<>(); // Buat wadah untuk satu reservasi
            item.put("id", r.getId()); // Masukkan ID reservasi
            item.put("kode_unik", r.getKodeUnik()); // Masukkan kode tiket unik
            item.put("waktu_pesan", r.getWaktuPesan()); // Masukkan waktu pemesanan
            item.put("estimasi_kedatangan", r.getEstimasiKedatangan()); // Masukkan estimasi kedatangan
            item.put("waktu_check_in", r.getWaktuCheckIn()); // Masukkan waktu check-in
            item.put("waktu_check_out", r.getWaktuCheckOut()); // Masukkan waktu check-out
            item.put("status", r.getStatus()); // Masukkan status reservasi
            item.put("total_durasi", r.getTotalDurasi()); // Masukkan total durasi parkir
            item.put("total_tarif", r.getTotalTarif()); // Masukkan total biaya parkir
            item.put("user_id", r.getUserId()); // Masukkan ID pengguna
            item.put("slot_parkir_id", r.getSlotParkirId()); // Masukkan ID slot parkir
            item.put("tarif_id", r.getTarifId()); // Masukkan ID tarif

            slotParkirRepository.findById(r.getSlotParkirId()).ifPresent(s -> { // Cari data slot parkir yang terkait
                item.put("lantai", s.getLantai()); // Masukkan lantai slot parkir
                item.put("blok", s.getBlok()); // Masukkan blok slot parkir
                item.put("no_slot", s.getNoSlot()); // Masukkan nomor slot parkir
                areaParkirRepository.findById(s.getAreaParkirId()).ifPresent(a -> { // Cari data area parkir yang terkait
                    item.put("nama_area", a.getNama()); // Masukkan nama area parkir
                });
            });
            data.add(item); // Tambahkan data reservasi ini ke daftar
        }
        return ResponseEntity.ok(Map.of("status", "success", "data", data)); // Kirim daftar riwayat parkir sebagai respons
    }

    // 6. BYPASS MASUK
    @PostMapping("/bypass-masuk") // Menangani POST ke URL /api/parkir/bypass-masuk (simulasi masuk parkir untuk pengujian)
    @Transactional // Operasi database dilakukan semuanya atau tidak sama sekali
    public ResponseEntity<?> bypassMasuk(@RequestBody Map<String, Object> body) { // Menerima kode tiket dan durasi yang dimundurkan
        String kodeUnik = (String) body.get("kode_unik"); // Ambil kode tiket dari data yang dikirim
        int menit = toInt(body.get("menit_parkir")) != null ? toInt(body.get("menit_parkir")) : 0; // Ambil berapa menit yang akan dimundurkan (default 0)

        Optional<Reservasi> rOpt = reservasiRepository.findByKodeUnik(kodeUnik); // Cari reservasi berdasarkan kode tiket
        if (rOpt.isPresent()) { // Jika reservasi ditemukan
            Reservasi r = rOpt.get(); // Ambil data reservasi
            r.setWaktuCheckIn(LocalDateTime.now().minusMinutes(menit)); // Atur waktu check-in dimundurkan sejumlah menit (untuk simulasi durasi parkir)
            reservasiRepository.save(r); // Simpan perubahan ke database
        }
        return ResponseEntity.ok(Map.of("status", "success", "message", "Berhasil Scan. Waktu dimundurkan " + menit + " menit.")); // Kirim respons sukses
    }

    // 7. CHECKOUT INFO
    @GetMapping("/checkout-info/{kodeUnik}") // Menangani GET ke URL /api/parkir/checkout-info/{kodeUnik} (info tagihan saat keluar)
    public ResponseEntity<?> checkoutInfo(@PathVariable String kodeUnik) { // Menerima kode tiket dari URL
        Optional<Reservasi> rOpt = reservasiRepository.findByKodeUnik(kodeUnik); // Cari reservasi berdasarkan kode tiket
        if (rOpt.isEmpty()) { // Jika tiket tidak ditemukan
            return ResponseEntity.status(404).body(Map.of("error", "Tiket tidak ditemukan")); // Kirim pesan error 404
        }

        Reservasi r = rOpt.get(); // Ambil data reservasi
        Optional<Tarif> tOpt = tarifRepository.findById(r.getTarifId()); // Cari tarif yang sesuai dengan reservasi ini
        if (tOpt.isEmpty()) { // Jika tarif tidak ditemukan
            return ResponseEntity.status(500).body(Map.of("error", "Tarif tidak ditemukan")); // Kirim pesan error 500
        }

        Tarif t = tOpt.get(); // Ambil data tarif
        LocalDateTime checkIn = r.getWaktuCheckIn(); // Ambil waktu check-in
        long diffMins = ChronoUnit.MINUTES.between(checkIn, LocalDateTime.now()); // Hitung selisih menit antara check-in dan sekarang
        long hours = diffMins / 60; // Hitung berapa jam penuh
        long mins = diffMins % 60; // Hitung sisa menit

        long totalBlok = (long) Math.ceil(diffMins / 5.0); // Hitung total blok 5 menit (tidak digunakan dalam kalkulasi akhir)
        if (totalBlok < 1) totalBlok = 1; // Minimal 1 blok

        // Logika per jam dengan aturan 30 menit
        float totalHarga = t.getTarifJamPertama(); // Harga awal selalu berdasarkan tarif jam pertama
        if (diffMins > 60) { // Jika durasi parkir lebih dari 1 jam
            long sisaMenit = diffMins - 60; // Hitung sisa menit setelah jam pertama
            long jamPenuh = sisaMenit / 60; // Hitung berapa jam penuh setelah jam pertama
            long sisaDiJamIni = sisaMenit % 60; // Hitung sisa menit di jam yang sedang berjalan

            totalHarga += jamPenuh * t.getTarifJamBerikutnya(); // Tambahkan biaya untuk setiap jam penuh tambahan
            if (sisaDiJamIni > 30) { // Jika sisa menit lebih dari 30 menit, dibulatkan ke jam berikutnya
                totalHarga += t.getTarifJamBerikutnya(); // Tambahkan biaya satu jam lagi
            }
        }

        Map<String, Object> response = new LinkedHashMap<>(); // Buat wadah untuk respons
        response.put("status", "success"); // Tandai status berhasil
        response.put("waktu_masuk", checkIn); // Masukkan waktu check-in
        response.put("durasi_jam", hours); // Masukkan durasi jam
        response.put("durasi_menit", mins); // Masukkan sisa durasi menit
        response.put("total_harga", totalHarga); // Masukkan total biaya yang harus dibayar
        return ResponseEntity.ok(response); // Kirim informasi tagihan sebagai respons
    }

    // 8. BAYAR
    @PostMapping("/bayar") // Menangani POST ke URL /api/parkir/bayar (konfirmasi pembayaran selesai)
    @Transactional // Operasi database dilakukan semuanya atau tidak sama sekali
    public ResponseEntity<?> bayar(@RequestBody Map<String, Object> body) { // Menerima data konfirmasi pembayaran
        try {
            String kodeUnik = (String) body.get("kode_unik"); // Ambil kode tiket
            Integer totalDurasi = toInt(body.get("total_durasi")); // Ambil total durasi parkir
            Float totalTarif = toFloat(body.get("total_tarif")); // Ambil total biaya parkir
            String orderId = (String) body.get("order_id"); // Ambil ID order dari Midtrans
            String paymentType = (String) body.get("payment_type"); // Ambil metode pembayaran yang digunakan

            transaksiRepository.findById(orderId).ifPresent(tp -> { // Cari data transaksi berdasarkan order ID
                tp.setStatusPembayaran("settlement"); // Ubah status pembayaran menjadi "settlement" (berhasil)
                tp.setMetodePembayaran(paymentType); // Simpan metode pembayaran yang digunakan
                transaksiRepository.save(tp); // Simpan perubahan ke database
            });

            Optional<Reservasi> rOpt = reservasiRepository.findByKodeUnik(kodeUnik); // Cari reservasi berdasarkan kode tiket
            if (rOpt.isPresent()) { // Jika reservasi ditemukan
                Reservasi r = rOpt.get(); // Ambil data reservasi
                slotParkirRepository.findById(r.getSlotParkirId()).ifPresent(s -> { // Cari slot parkir yang terkait
                    s.setStatus("Tersedia"); // Ubah status slot kembali menjadi "Tersedia" setelah pengguna keluar
                    slotParkirRepository.save(s); // Simpan perubahan slot ke database
                });
                int pointsToAdd = (r.getTarifId() == 2) ? 4 : 2; // Tentukan poin reward: 4 poin untuk mobil (tarifId=2), 2 poin untuk motor
                userRepository.addPoints(r.getUserId(), pointsToAdd); // Tambahkan poin reward ke pengguna
                r.setStatus("Selesai"); // Ubah status reservasi menjadi "Selesai"
                r.setWaktuCheckOut(LocalDateTime.now()); // Isi waktu check-out dengan waktu saat ini
                r.setTotalDurasi(totalDurasi); // Isi total durasi parkir
                r.setTotalTarif(totalTarif); // Isi total biaya parkir
                reservasiRepository.save(r); // Simpan perubahan reservasi ke database
            }
            return ResponseEntity.ok(Map.of("status", "success")); // Kirim respons sukses
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage())); // Kirim respons error jika terjadi masalah
        }
    }

    // 9. AVAILABILITY
    @GetMapping("/availability/{areaId}") // Menangani GET ke URL /api/parkir/availability/{areaId} (cek ketersediaan slot)
    public ResponseEntity<?> availability(@PathVariable Integer areaId) { // Menerima ID area parkir dari URL
        Long total = slotParkirRepository.countTotalByAreaId(areaId); // Hitung total slot di area ini
        Long available = slotParkirRepository.countAvailableByAreaId(areaId); // Hitung slot yang masih tersedia
        Map<String, Object> data = new LinkedHashMap<>(); // Buat wadah untuk data ketersediaan
        data.put("total_slots", total != null ? total : 0); // Masukkan total slot (0 jika null)
        data.put("available_slots", available != null ? available : 0); // Masukkan slot tersedia (0 jika null)
        return ResponseEntity.ok(Map.of("status", "success", "data", data)); // Kirim informasi ketersediaan sebagai respons
    }

    // 10. GET SNAP TOKEN (Real Midtrans SDK)
    @PostMapping("/get-snap-token") // Menangani POST ke URL /api/parkir/get-snap-token (buat token pembayaran Midtrans)
    @Transactional // Operasi database dilakukan semuanya atau tidak sama sekali
    public ResponseEntity<?> getSnapToken(@RequestBody Map<String, Object> body) { // Menerima kode tiket dan total biaya
        try {
            String kodeUnik = (String) body.get("kode_unik"); // Ambil kode tiket dari data yang dikirim
            Integer totalTarif = toInt(body.get("total_tarif")); // Ambil total biaya yang akan dibayar
            String orderId = kodeUnik + "-" + System.currentTimeMillis(); // Buat ID order unik dengan menggabungkan kode tiket dan timestamp

            // 1. Tarik data user dari database berdasarkan tiket (kode_unik)
            Optional<Reservasi> rOpt = reservasiRepository.findByKodeUnik(kodeUnik); // Cari reservasi berdasarkan kode tiket
            String firstName = "Parkirin"; // Nama depan default jika data pengguna tidak ditemukan
            String lastName = "User"; // Nama belakang default
            String email = "user@parkirin.com"; // Email default
            String phone = "080000000000"; // Nomor telepon default

            if (rOpt.isPresent()) { // Jika reservasi ditemukan
                Optional<User> uOpt = userRepository.findById(rOpt.get().getUserId()); // Cari data pengguna berdasarkan ID di reservasi
                if (uOpt.isPresent()) { // Jika pengguna ditemukan
                    User u = uOpt.get(); // Ambil data pengguna
                    String[] nameParts = u.getNama().trim().split(" ", 2); // Pisahkan nama menjadi nama depan dan belakang
                    firstName = nameParts[0]; // Ambil nama depan
                    lastName = nameParts.length > 1 ? nameParts[1] : ""; // Ambil nama belakang (jika ada)
                    email = u.getEmail() != null ? u.getEmail() : email; // Gunakan email pengguna (atau default jika null)
                    phone = u.getNoTelepon() != null ? u.getNoTelepon() : phone; // Gunakan nomor telepon pengguna (atau default jika null)
                }
            }

            // 2. Build Midtrans parameters
            Map<String, Object> transactionDetails = new LinkedHashMap<>(); // Buat wadah untuk detail transaksi Midtrans
            transactionDetails.put("order_id", orderId); // Masukkan ID order
            transactionDetails.put("gross_amount", totalTarif); // Masukkan jumlah yang harus dibayar

            Map<String, Object> creditCard = new LinkedHashMap<>(); // Buat konfigurasi untuk kartu kredit
            creditCard.put("secure", true); // Aktifkan mode aman untuk transaksi kartu kredit

            Map<String, String> customerDetails = new LinkedHashMap<>(); // Buat wadah untuk data pelanggan
            customerDetails.put("first_name", firstName); // Masukkan nama depan pelanggan
            customerDetails.put("last_name", lastName); // Masukkan nama belakang pelanggan
            customerDetails.put("email", email); // Masukkan email pelanggan
            customerDetails.put("phone", phone); // Masukkan nomor telepon pelanggan

            Map<String, Object> params = new LinkedHashMap<>(); // Buat parameter lengkap untuk Midtrans
            params.put("transaction_details", transactionDetails); // Masukkan detail transaksi
            params.put("credit_card", creditCard); // Masukkan konfigurasi kartu kredit
            params.put("customer_details", customerDetails); // Masukkan data pelanggan

            // 3. Create transaction via Midtrans
            String snapToken = snapApi.createTransactionToken(params); // Kirim data ke Midtrans dan dapatkan token pembayaran

            // 4. Clean up pending transactions & save new one
            transaksiRepository.deletePendingByKodeUnik(kodeUnik); // Hapus transaksi lama yang masih pending untuk tiket ini

            TransaksiPembayaran tp = new TransaksiPembayaran(); // Buat objek transaksi pembayaran baru
            tp.setId(orderId); // Isi ID transaksi (dari order ID)
            tp.setReservasiKodeUnik(kodeUnik); // Isi kode tiket terkait
            tp.setJumlahBayar(totalTarif); // Isi jumlah yang harus dibayar
            tp.setStatusPembayaran("pending"); // Status awal transaksi adalah "pending" (menunggu pembayaran)
            transaksiRepository.save(tp); // Simpan transaksi baru ke database

            Map<String, Object> response = new LinkedHashMap<>(); // Buat wadah untuk respons
            response.put("status", "success"); // Tandai status berhasil
            response.put("token", snapToken); // Masukkan token Midtrans Snap
            response.put("order_id", orderId); // Masukkan ID order
            return ResponseEntity.ok(response); // Kirim respons dengan token pembayaran
        } catch (Exception e) {
            e.printStackTrace(); // Tampilkan detail error di konsol
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage())); // Kirim respons error
        }
    }

    // 11. ULASAN
    @PostMapping("/ulasan") // Menangani POST ke URL /api/parkir/ulasan (kirim ulasan parkir)
    public ResponseEntity<?> createUlasan(@RequestBody Map<String, Object> body) { // Menerima data ulasan dari pengguna
        String kodeUnik = (String) body.get("kode_unik"); // Ambil kode tiket yang diulas
        Integer userId = toInt(body.get("user_id")); // Ambil ID pengguna yang memberikan ulasan
        Integer rating = toInt(body.get("rating")); // Ambil nilai rating (bintang)
        String komentar = (String) body.get("komentar"); // Ambil komentar/teks ulasan

        if (kodeUnik == null || userId == null || rating == null) { // Cek apakah data wajib sudah lengkap
            return ResponseEntity.badRequest().body(Map.of("error", "Data ulasan tidak lengkap!")); // Kirim pesan error jika ada data yang kurang
        }

        UlasanParkir ulasan = new UlasanParkir(); // Buat objek ulasan baru
        ulasan.setReservasiKodeUnik(kodeUnik); // Isi kode tiket yang diulas
        ulasan.setUserId(userId); // Isi ID pengguna
        ulasan.setRating(rating); // Isi nilai rating
        ulasan.setKomentar(komentar); // Isi komentar ulasan
        ulasanRepository.save(ulasan); // Simpan ulasan ke database

        return ResponseEntity.ok(Map.of("status", "success", "message", "Ulasan berhasil disimpan!")); // Kirim respons sukses
    }

    // 12. GET POIN
    @GetMapping("/poin/{userId}") // Menangani GET ke URL /api/parkir/poin/{userId} (ambil poin reward pengguna)
    public ResponseEntity<?> getPoin(@PathVariable Integer userId) { // Menerima ID pengguna dari URL
        Optional<User> userOpt = userRepository.findById(userId); // Cari pengguna berdasarkan ID
        int points = userOpt.map(User::getPoints).orElse(0); // Ambil poin pengguna, atau 0 jika pengguna tidak ditemukan
        return ResponseEntity.ok(Map.of("status", "success", "points", points)); // Kirim total poin pengguna sebagai respons
    }
}
