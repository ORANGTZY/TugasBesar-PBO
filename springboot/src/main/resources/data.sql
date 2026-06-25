-- Seed tarif data (tarif per jam, aturan 30 menit)
INSERT IGNORE INTO tarif (id, jenis_kendaraan, tarif_jam_pertama, tarif_jam_berikutnya) 
VALUES 
(1, 'Motor', 3000, 1000), 
(2, 'Mobil', 10000, 2000);
