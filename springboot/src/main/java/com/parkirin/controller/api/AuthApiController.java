package com.parkirin.controller.api; // Nama paket/folder tempat file ini berada

import com.parkirin.model.*; // Mengimpor semua kelas model (User, UserPin, dll.)
import com.parkirin.repository.*; // Mengimpor semua kelas repository untuk akses database
import lombok.RequiredArgsConstructor; // Otomatis membuat konstruktor berdasarkan field yang bersifat final
import org.springframework.http.ResponseEntity; // Alat untuk membuat respons HTTP dengan status dan data
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Alat untuk hashing password dan PIN dengan algoritma BCrypt
import org.springframework.web.bind.annotation.*; // Mengimpor semua anotasi untuk menangani request API
import jakarta.transaction.Transactional; // Memastikan operasi database dilakukan semuanya atau tidak sama sekali
import java.util.*; // Mengimpor berbagai tipe data koleksi (Map, List, Optional, dll.)

@RestController // Menandai kelas ini sebagai controller API yang mengirim data (bukan halaman HTML)
@RequestMapping("/api") // Semua URL di kelas ini diawali dengan "/api"
@RequiredArgsConstructor // Otomatis membuat konstruktor berdasarkan field final
public class AuthApiController { // Kelas ini menangani semua proses autentikasi pengguna (daftar, login, dll.)

    private final UserRepository userRepository; // Alat untuk mengakses data pengguna di database
    private final UserPinRepository userPinRepository; // Alat untuk mengakses data PIN pengguna di database
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // Encoder untuk hashing password dan PIN dengan BCrypt

    // Mengecek apakah suatu string sudah berformat hash BCrypt (diawali $2a$ atau $2b$)
    private boolean isBcryptHash(String value) {
        return value != null && (value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$"));
    }

    @PostMapping("/register") // Menangani permintaan POST ke URL /api/register (proses pendaftaran akun)
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) { // Menerima data pendaftaran dalam format JSON
        String nama = body.get("nama"); // Ambil nama dari data yang dikirim
        String email = body.get("email"); // Ambil email dari data yang dikirim
        String password = body.get("password"); // Ambil kata sandi dari data yang dikirim
        String noTelepon = body.get("no_telepon"); // Ambil nomor telepon dari data yang dikirim

        if (userRepository.findByEmail(email).isPresent()) { // Cek apakah email sudah pernah digunakan
            return ResponseEntity.badRequest().body(Map.of("message", "Email sudah terdaftar!")); // Jika email sudah ada, kirim pesan error
        }

        User user = new User(); // Buat objek pengguna baru
        user.setNama(nama); // Isi nama pengguna
        user.setEmail(email); // Isi email pengguna
        user.setPassword(passwordEncoder.encode(password)); // Hash password sebelum disimpan ke database (BCrypt)
        user.setNoTelepon(noTelepon); // Isi nomor telepon pengguna
        user.setRole("Pengguna"); // Atur peran pengguna sebagai "Pengguna" (bukan admin)
        user.setPoints(0); // Poin awal pengguna adalah 0
        userRepository.save(user); // Simpan data pengguna ke database

        return ResponseEntity.status(201).body(Map.of("message", "Registrasi berhasil!")); // Kirim respons sukses dengan kode 201 (Created)
    }

    @PostMapping("/login") // Menangani permintaan POST ke URL /api/login (proses masuk akun)
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) { // Menerima data login dalam format JSON
        String email = body.get("email"); // Ambil email dari data yang dikirim
        String noTelepon = body.get("no_telepon"); // Ambil nomor telepon dari data yang dikirim
        String password = body.get("password"); // Ambil kata sandi dari data yang dikirim

        Optional<User> userOpt = userRepository.findByEmailAndNoTelepon(email, noTelepon); // Cari pengguna berdasarkan email dan nomor telepon
        if (userOpt.isEmpty()) { // Jika pengguna tidak ditemukan
            return ResponseEntity.status(401).body(Map.of("message", "Email atau Nomor Telepon salah/tidak terdaftar!")); // Kirim pesan error dengan kode 401 (Unauthorized)
        }

        User user = userOpt.get(); // Ambil data pengguna yang ditemukan
        String storedPassword = user.getPassword(); // Ambil password yang tersimpan di database

        if (isBcryptHash(storedPassword)) {
            // Password sudah berformat BCrypt — verifikasi dengan passwordEncoder.matches()
            if (!passwordEncoder.matches(password, storedPassword)) {
                return ResponseEntity.status(401).body(Map.of("message", "Kata sandi salah!")); // Kata sandi tidak cocok
            }
        } else {
            // Password masih plaintext (data lama sebelum BCrypt) — cocokkan langsung
            if (!password.equals(storedPassword)) {
                return ResponseEntity.status(401).body(Map.of("message", "Kata sandi salah!")); // Kata sandi tidak cocok
            }
            // Migrasi otomatis: re-hash password plaintext dan simpan ulang ke database
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        }

        Map<String, Object> userData = new LinkedHashMap<>(); // Buat wadah untuk menyimpan data pengguna yang akan dikirim
        userData.put("id", user.getId()); // Masukkan ID pengguna
        userData.put("nama", user.getNama()); // Masukkan nama pengguna
        userData.put("email", user.getEmail()); // Masukkan email pengguna
        userData.put("no_telepon", user.getNoTelepon()); // Masukkan nomor telepon pengguna
        userData.put("role", user.getRole()); // Masukkan peran pengguna

        Map<String, Object> response = new LinkedHashMap<>(); // Buat wadah untuk respons keseluruhan
        response.put("message", "Login berhasil!"); // Pesan sukses login
        response.put("user", userData); // Masukkan data pengguna ke dalam respons
        return ResponseEntity.ok(response); // Kirim respons sukses dengan data pengguna
    }

    @PutMapping("/update-profile") // Menangani permintaan PUT ke URL /api/update-profile (perbarui profil)
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, Object> body) { // Menerima data profil yang diperbarui
        Integer id = body.get("id") instanceof Number ? ((Number) body.get("id")).intValue() : Integer.parseInt(body.get("id").toString()); // Ambil ID pengguna, konversi ke Integer apapun formatnya
        String nama = (String) body.get("nama"); // Ambil nama baru
        String noTelepon = (String) body.get("no_telepon"); // Ambil nomor telepon baru
        String email = (String) body.get("email"); // Ambil email baru

        Optional<User> userOpt = userRepository.findById(id); // Cari pengguna berdasarkan ID
        if (userOpt.isEmpty()) { // Jika pengguna tidak ditemukan
            return ResponseEntity.status(404).body(Map.of("status", "error", "message", "User dengan ID tersebut tidak ditemukan di database.")); // Kirim pesan error 404 (Not Found)
        }

        User user = userOpt.get(); // Ambil data pengguna yang ditemukan
        user.setNama(nama); // Perbarui nama pengguna
        user.setNoTelepon(noTelepon); // Perbarui nomor telepon pengguna
        user.setEmail(email); // Perbarui email pengguna
        userRepository.save(user); // Simpan perubahan ke database

        return ResponseEntity.ok(Map.of("status", "success", "message", "Profil pengguna berhasil diperbarui di database!")); // Kirim respons sukses
    }

    @DeleteMapping("/delete-account/{id}") // Menangani permintaan DELETE ke URL /api/delete-account/{id} (hapus akun)
    public ResponseEntity<?> deleteAccount(@PathVariable Integer id) { // Menerima ID pengguna dari URL
        if (!userRepository.existsById(id)) { // Cek apakah pengguna dengan ID ini ada di database
            return ResponseEntity.status(404).body(Map.of("status", "error", "message", "Akun sudah tidak ada di database.")); // Jika tidak ada, kirim pesan error
        }
        userRepository.deleteById(id); // Hapus data pengguna dari database
        return ResponseEntity.ok(Map.of("status", "success", "message", "Akun berhasil dihapus permanen!")); // Kirim respons sukses
    }

    @PutMapping("/auth/set-pin") // Menangani permintaan PUT ke URL /api/auth/set-pin (simpan atau perbarui PIN)
    @Transactional // Operasi database dilakukan semuanya atau tidak sama sekali
    public ResponseEntity<?> setPin(@RequestBody Map<String, Object> body) { // Menerima ID pengguna dan PIN baru
        Integer id = body.get("id") instanceof Number ? ((Number) body.get("id")).intValue() : Integer.parseInt(body.get("id").toString()); // Ambil ID pengguna
        String pin = (String) body.get("pin"); // Ambil PIN baru

        Optional<UserPin> existing = userPinRepository.findByUserId(id); // Cek apakah pengguna sudah punya PIN sebelumnya
        if (existing.isPresent()) { // Jika PIN sudah ada
            UserPin userPin = existing.get(); // Ambil data PIN yang lama
            userPin.setPin(passwordEncoder.encode(pin)); // Hash PIN baru sebelum disimpan (BCrypt)
            userPinRepository.save(userPin); // Simpan perubahan ke database
        } else { // Jika PIN belum pernah dibuat
            UserPin userPin = new UserPin(); // Buat objek PIN baru
            userPin.setUserId(id); // Isi ID pengguna
            userPin.setPin(passwordEncoder.encode(pin)); // Hash PIN sebelum disimpan ke database (BCrypt)
            userPinRepository.save(userPin); // Simpan PIN baru ke database
        }

        return ResponseEntity.ok(Map.of("status", "success", "message", "PIN berhasil disimpan secara permanen di tabel terpisah!")); // Kirim respons sukses
    }

    @PostMapping("/auth/verify-pin") // Menangani permintaan POST ke URL /api/auth/verify-pin (cek PIN saat konfirmasi)
    public ResponseEntity<?> verifyPin(@RequestBody Map<String, Object> body) { // Menerima ID pengguna dan PIN yang dimasukkan
        Integer userId = body.get("user_id") instanceof Number ? ((Number) body.get("user_id")).intValue() : Integer.parseInt(body.get("user_id").toString()); // Ambil ID pengguna
        String pin = (String) body.get("pin"); // Ambil PIN yang dimasukkan pengguna

        Optional<UserPin> userPinOpt = userPinRepository.findByUserId(userId); // Cari data PIN pengguna di database
        if (userPinOpt.isEmpty()) { // Jika pengguna belum pernah membuat PIN
            return ResponseEntity.status(404).body(Map.of("status", "error", "message", "PIN belum diatur untuk akun ini!")); // Kirim pesan error
        }

        String storedPin = userPinOpt.get().getPin(); // Ambil PIN yang tersimpan di database

        if (isBcryptHash(storedPin)) {
            // PIN sudah berformat BCrypt — verifikasi dengan passwordEncoder.matches()
            if (passwordEncoder.matches(pin, storedPin)) {
                return ResponseEntity.ok(Map.of("status", "success", "message", "PIN Benar!")); // Kirim respons sukses
            } else {
                return ResponseEntity.status(401).body(Map.of("status", "wrong", "message", "PIN yang Anda masukkan salah!")); // Kirim respons salah PIN
            }
        } else {
            // PIN masih plaintext (data lama sebelum BCrypt) — cocokkan langsung
            if (pin.equals(storedPin)) {
                // Migrasi otomatis: re-hash PIN plaintext dan simpan ulang ke database
                UserPin userPin = userPinOpt.get();
                userPin.setPin(passwordEncoder.encode(pin));
                userPinRepository.save(userPin);
                return ResponseEntity.ok(Map.of("status", "success", "message", "PIN Benar!")); // Kirim respons sukses
            } else {
                return ResponseEntity.status(401).body(Map.of("status", "wrong", "message", "PIN yang Anda masukkan salah!")); // Kirim respons salah PIN
            }
        }
    }
}
