-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 25, 2026 at 07:04 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `smartpark_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `area_parkir`
--

CREATE TABLE `area_parkir` (
  `id` int(11) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `kapasitas_total` int(11) NOT NULL,
  `lokasi` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `area_parkir`
--

INSERT INTO `area_parkir` (`id`, `nama`, `kapasitas_total`, `lokasi`) VALUES
(1, 'RITA Supermall', 200, 'Purwokerto (Beroperasi)'),
(2, 'Grand Indonesia', 500, 'Jakarta (Belum Beroperasi)');

-- --------------------------------------------------------

--
-- Table structure for table `kendaraan`
--

CREATE TABLE `kendaraan` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `jenis` varchar(10) NOT NULL,
  `plat` varchar(20) NOT NULL,
  `merek` varchar(50) DEFAULT NULL,
  `model` varchar(50) DEFAULT NULL,
  `tahun` int(11) DEFAULT NULL,
  `warna` varchar(30) DEFAULT NULL,
  `mesin` varchar(50) DEFAULT NULL,
  `rangka` varchar(50) DEFAULT NULL,
  `is_utama` tinyint(1) DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `kendaraan`
--

INSERT INTO `kendaraan` (`id`, `user_id`, `jenis`, `plat`, `merek`, `model`, `tahun`, `warna`, `mesin`, `rangka`, `is_utama`, `created_at`) VALUES
(1, 2, 'motor', 'B 0000 S', 'Yamaha', 'Aerox', 2025, 'Hitam', 'B5612345', 'MH3SEF510KJ000XXX', 1, '2026-05-10 05:04:45'),
(2, 2, 'mobil', 'BK 1 GD', 'Toyota', 'Fortuner', 2025, 'Hitam', '1GDFTV12345', 'MHFZR69G9B3024738', 0, '2026-05-10 05:18:13'),
(3, 2, 'mobil', 'R 150 L', 'BMW ', 'X7', 2025, 'Putih', 'B68A1B2C3', 'MHJ12345678', 0, '2026-05-22 07:09:00'),
(4, 2, 'motor', 'D 04 IBU', 'Honda', 'CBR150R', 2025, 'Merah', 'KC51E1XXXXXX', 'MH1K45G1XXXXXX', 0, '2026-05-22 07:11:05');

-- --------------------------------------------------------

--
-- Table structure for table `reservasi`
--

CREATE TABLE `reservasi` (
  `id` int(11) NOT NULL,
  `kode_unik` varchar(20) NOT NULL,
  `waktu_pesan` datetime DEFAULT current_timestamp(),
  `estimasi_kedatangan` datetime NOT NULL,
  `waktu_check_in` datetime DEFAULT NULL,
  `waktu_check_out` datetime DEFAULT NULL,
  `status` varchar(20) DEFAULT 'Menunggu',
  `total_durasi` int(11) DEFAULT NULL,
  `total_tarif` float DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `slot_parkir_id` int(11) NOT NULL,
  `tarif_id` int(11) NOT NULL,
  `kendaraan_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `reservasi`
--

INSERT INTO `reservasi` (`id`, `kode_unik`, `waktu_pesan`, `estimasi_kedatangan`, `waktu_check_in`, `waktu_check_out`, `status`, `total_durasi`, `total_tarif`, `user_id`, `slot_parkir_id`, `tarif_id`, `kendaraan_id`) VALUES
(47, 'TKT46487746', '2026-06-11 14:36:27', '2026-06-11 16:36:27', '2026-06-11 14:36:40', '2026-06-11 14:43:45', 'Selesai', 7, 3000, 2, 1411, 1, NULL),
(48, 'TKT5694483', '2026-06-11 14:51:30', '2026-06-11 16:51:30', '2026-06-11 14:52:14', '2026-06-11 14:52:35', 'Selesai', 0, 10000, 2, 1401, 2, NULL),
(49, 'TKT47664858', '2026-06-11 14:53:46', '2026-06-11 16:53:46', '2026-06-11 14:53:57', '2026-06-11 14:58:18', 'Selesai', 4, 3000, 2, 1441, 1, NULL),
(50, 'TKT98130668', '2026-06-11 15:22:25', '2026-06-11 17:22:25', '2026-06-11 15:22:46', '2026-06-12 16:35:15', 'Selesai', 1512, 28000, 2, 1421, 1, 4),
(51, 'TKT34848671', '2026-06-11 15:23:43', '2026-06-11 17:23:43', NULL, NULL, 'Dibatalkan', NULL, NULL, 2, 1560, 2, 3),
(52, 'TKT88933506', '2026-06-11 15:36:33', '2026-06-11 17:36:33', '2026-06-11 15:36:36', '2026-06-11 15:36:51', 'Selesai', 0, 3000, 2, 1422, 1, 1),
(53, 'TKT29624187', '2026-06-12 16:44:37', '2026-06-12 18:44:37', '2026-06-12 16:50:35', '2026-06-12 16:51:04', 'Selesai', 0, 3000, 2, 1441, 1, 4),
(54, 'TKT52798428', '2026-06-12 16:57:17', '2026-06-12 18:57:17', '2026-06-14 14:26:27', '2026-06-15 10:37:22', 'Selesai', 1210, 50000, 2, 1408, 2, 3),
(55, 'TKT89068339', '2026-06-12 19:52:33', '2026-06-12 21:52:33', NULL, NULL, 'Dibatalkan', NULL, NULL, 2, 1591, 1, 4),
(56, 'TKT83566070', '2026-06-13 10:47:27', '2026-06-13 12:47:27', '2026-06-13 10:47:27', '2026-06-13 10:50:01', 'Selesai', 2, 3000, 2, 1441, 1, 1),
(57, 'TKT4358218', '2026-06-14 15:30:44', '2026-06-14 17:30:44', '2026-06-14 15:30:44', '2026-06-14 15:37:44', 'Selesai', 6, 3000, 2, 1441, 1, 4),
(58, 'TKT98129850', '2026-06-14 15:38:42', '2026-06-14 17:38:42', '2026-06-14 15:38:42', '2026-06-14 15:39:19', 'Selesai', 0, 3000, 2, 1471, 1, 4),
(59, 'TKT75489502', '2026-06-14 15:39:43', '2026-06-14 17:39:43', '2026-06-14 15:39:58', '2026-06-14 15:41:05', 'Selesai', 1, 10000, 2, 1560, 2, 2),
(60, 'TKT80556455', '2026-06-14 19:27:32', '2026-06-14 21:27:32', '2026-06-14 19:27:34', '2026-06-14 19:27:52', 'Selesai', 0, 3000, 2, 1441, 1, 4),
(61, 'TKT32535200', '2026-06-14 19:30:30', '2026-06-14 21:30:30', '2026-06-14 19:30:40', '2026-06-14 19:31:56', 'Selesai', 1, 10000, 2, 1510, 2, 2),
(62, 'TKT84014145', '2026-06-14 19:44:01', '2026-06-14 21:44:01', '2026-06-14 19:44:03', '2026-06-14 19:45:19', 'Selesai', 1, 10000, 2, 1560, 2, 2),
(63, 'TKT56000638', '2026-06-14 19:50:02', '2026-06-14 21:50:02', '2026-06-14 19:50:05', '2026-06-14 19:50:52', 'Selesai', 0, 3000, 2, 1431, 1, 1),
(64, 'TKT66589869', '2026-06-14 19:54:22', '2026-06-14 21:54:22', '2026-06-14 19:54:25', '2026-06-14 19:55:41', 'Selesai', 1, 3000, 2, 1441, 1, 4),
(65, 'TKT73994931', '2026-06-14 20:01:47', '2026-06-14 22:01:47', '2026-06-14 20:01:52', '2026-06-14 20:02:09', 'Selesai', 0, 3000, 2, 1441, 1, 4),
(66, 'TKT8806488', '2026-06-15 08:21:36', '2026-06-15 10:21:36', '2026-06-15 08:21:39', '2026-06-15 08:21:56', 'Selesai', 0, 3000, 2, 1441, 1, 4),
(67, 'TKT42326277', '2026-06-15 08:45:01', '2026-06-15 10:45:01', '2026-06-15 08:45:13', '2026-06-15 08:45:38', 'Selesai', 0, 10000, 2, 1560, 2, 2),
(68, 'TKT17266512', '2026-06-15 08:56:35', '2026-06-15 10:56:35', '2026-06-15 08:56:45', '2026-06-15 08:57:16', 'Selesai', 0, 3000, 2, 1441, 1, 4),
(69, 'TKT21418155', '2026-06-15 10:22:08', '2026-06-15 12:22:08', '2026-06-15 10:22:08', '2026-06-15 10:39:47', 'Selesai', 17, 10000, 2, 1510, 2, 2),
(70, 'TKT25256626', '2026-06-15 10:22:45', '2026-06-15 12:22:45', '2026-06-15 10:23:14', '2026-06-15 10:23:56', 'Selesai', 0, 3000, 2, 1441, 1, 4),
(71, 'TKT70314285', '2026-06-15 10:28:49', '2026-06-15 12:28:49', '2026-06-15 10:29:08', '2026-06-15 10:34:53', 'Selesai', 5, 3000, 2, 1541, 1, 4),
(72, 'TKT24182686', '2026-06-15 10:40:27', '2026-06-15 12:40:27', '2026-06-15 10:40:59', '2026-06-15 10:41:34', 'Selesai', 0, 10000, 2, 1560, 2, 2),
(73, 'TKT37172635', '2026-06-16 11:04:55', '2026-06-16 13:04:55', '2026-06-16 11:05:45', '2026-06-16 14:54:03', 'Selesai', 228, 16000, 2, 1408, 2, 3),
(74, 'TKT64812072', '2026-06-24 17:09:13', '2026-06-24 19:09:13', '2026-06-24 17:09:22', '2026-06-24 17:09:55', 'Selesai', 0, 3000, 2, 1441, 1, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `slot_parkir`
--

CREATE TABLE `slot_parkir` (
  `id` int(11) NOT NULL,
  `lantai` varchar(10) NOT NULL,
  `blok` varchar(10) NOT NULL,
  `no_slot` varchar(10) NOT NULL,
  `status` varchar(10) DEFAULT 'Tersedia',
  `area_parkir_id` int(11) NOT NULL,
  `tipe_kendaraan` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `slot_parkir`
--

INSERT INTO `slot_parkir` (`id`, `lantai`, `blok`, `no_slot`, `status`, `area_parkir_id`, `tipe_kendaraan`) VALUES
(1401, 'Lantai B1', 'Mobil', 'B1-1', 'Tersedia', 1, ''),
(1402, 'Lantai B1', 'Mobil', 'B1-2', 'Tersedia', 1, ''),
(1403, 'Lantai B1', 'Mobil', 'B1-3', 'Tersedia', 1, 'Mobil'),
(1404, 'Lantai B1', 'Mobil', 'B1-4', 'Tersedia', 1, 'Mobil'),
(1405, 'Lantai B1', 'Mobil', 'B1-5', 'Tersedia', 1, 'Mobil'),
(1406, 'Lantai B1', 'Mobil', 'B1-6', 'Tersedia', 1, NULL),
(1407, 'Lantai B1', 'Mobil', 'B1-7', 'Tersedia', 1, NULL),
(1408, 'Lantai B1', 'Mobil', 'B1-8', 'Tersedia', 1, NULL),
(1409, 'Lantai B1', 'Mobil', 'B1-9', 'Tersedia', 1, NULL),
(1410, 'Lantai B1', 'Mobil', 'B1-10', 'Tersedia', 1, NULL),
(1411, 'Lantai B1', 'Blok A', 'Blok A-1', 'Tersedia', 1, 'Motor'),
(1412, 'Lantai B1', 'Blok A', 'Blok A-2', 'Tersedia', 1, 'Motor'),
(1413, 'Lantai B1', 'Blok A', 'Blok A-3', 'Tersedia', 1, NULL),
(1414, 'Lantai B1', 'Blok A', 'Blok A-4', 'Tersedia', 1, NULL),
(1415, 'Lantai B1', 'Blok A', 'Blok A-5', 'Tersedia', 1, NULL),
(1416, 'Lantai B1', 'Blok A', 'Blok A-6', 'Tersedia', 1, NULL),
(1417, 'Lantai B1', 'Blok A', 'Blok A-7', 'Tersedia', 1, NULL),
(1418, 'Lantai B1', 'Blok A', 'Blok A-8', 'Tersedia', 1, NULL),
(1419, 'Lantai B1', 'Blok A', 'Blok A-9', 'Tersedia', 1, NULL),
(1420, 'Lantai B1', 'Blok A', 'Blok A-10', 'Tersedia', 1, NULL),
(1421, 'Lantai B1', 'Blok B', 'Blok B-1', 'Tersedia', 1, NULL),
(1422, 'Lantai B1', 'Blok B', 'Blok B-2', 'Tersedia', 1, NULL),
(1423, 'Lantai B1', 'Blok B', 'Blok B-3', 'Tersedia', 1, NULL),
(1424, 'Lantai B1', 'Blok B', 'Blok B-4', 'Tersedia', 1, NULL),
(1425, 'Lantai B1', 'Blok B', 'Blok B-5', 'Tersedia', 1, NULL),
(1426, 'Lantai B1', 'Blok B', 'Blok B-6', 'Tersedia', 1, NULL),
(1427, 'Lantai B1', 'Blok B', 'Blok B-7', 'Tersedia', 1, NULL),
(1428, 'Lantai B1', 'Blok B', 'Blok B-8', 'Tersedia', 1, NULL),
(1429, 'Lantai B1', 'Blok B', 'Blok B-9', 'Tersedia', 1, NULL),
(1430, 'Lantai B1', 'Blok B', 'Blok B-10', 'Tersedia', 1, NULL),
(1431, 'Lantai B1', 'Blok C', 'Blok C-1', 'Tersedia', 1, NULL),
(1432, 'Lantai B1', 'Blok C', 'Blok C-2', 'Tersedia', 1, NULL),
(1433, 'Lantai B1', 'Blok C', 'Blok C-3', 'Tersedia', 1, NULL),
(1434, 'Lantai B1', 'Blok C', 'Blok C-4', 'Tersedia', 1, NULL),
(1435, 'Lantai B1', 'Blok C', 'Blok C-5', 'Tersedia', 1, NULL),
(1436, 'Lantai B1', 'Blok C', 'Blok C-6', 'Tersedia', 1, NULL),
(1437, 'Lantai B1', 'Blok C', 'Blok C-7', 'Tersedia', 1, NULL),
(1438, 'Lantai B1', 'Blok C', 'Blok C-8', 'Tersedia', 1, NULL),
(1439, 'Lantai B1', 'Blok C', 'Blok C-9', 'Tersedia', 1, NULL),
(1440, 'Lantai B1', 'Blok C', 'Blok C-10', 'Tersedia', 1, NULL),
(1441, 'Lantai B1', 'Blok D', 'Blok D-1', 'Tersedia', 1, NULL),
(1442, 'Lantai B1', 'Blok D', 'Blok D-2', 'Tersedia', 1, NULL),
(1443, 'Lantai B1', 'Blok D', 'Blok D-3', 'Tersedia', 1, NULL),
(1444, 'Lantai B1', 'Blok D', 'Blok D-4', 'Tersedia', 1, NULL),
(1445, 'Lantai B1', 'Blok D', 'Blok D-5', 'Tersedia', 1, NULL),
(1446, 'Lantai B1', 'Blok D', 'Blok D-6', 'Tersedia', 1, NULL),
(1447, 'Lantai B1', 'Blok D', 'Blok D-7', 'Tersedia', 1, NULL),
(1448, 'Lantai B1', 'Blok D', 'Blok D-8', 'Tersedia', 1, NULL),
(1449, 'Lantai B1', 'Blok D', 'Blok D-9', 'Tersedia', 1, NULL),
(1450, 'Lantai B1', 'Blok D', 'Blok D-10', 'Tersedia', 1, NULL),
(1451, 'Lantai P1', 'Mobil', 'P1-1', 'Tersedia', 1, NULL),
(1452, 'Lantai P1', 'Mobil', 'P1-2', 'Tersedia', 1, NULL),
(1453, 'Lantai P1', 'Mobil', 'P1-3', 'Tersedia', 1, NULL),
(1454, 'Lantai P1', 'Mobil', 'P1-4', 'Tersedia', 1, NULL),
(1455, 'Lantai P1', 'Mobil', 'P1-5', 'Tersedia', 1, NULL),
(1456, 'Lantai P1', 'Mobil', 'P1-6', 'Tersedia', 1, NULL),
(1457, 'Lantai P1', 'Mobil', 'P1-7', 'Tersedia', 1, NULL),
(1458, 'Lantai P1', 'Mobil', 'P1-8', 'Tersedia', 1, NULL),
(1459, 'Lantai P1', 'Mobil', 'P1-9', 'Tersedia', 1, NULL),
(1460, 'Lantai P1', 'Mobil', 'P1-10', 'Tersedia', 1, NULL),
(1461, 'Lantai P1', 'Blok A', 'Blok A-1', 'Tersedia', 1, NULL),
(1462, 'Lantai P1', 'Blok A', 'Blok A-2', 'Tersedia', 1, NULL),
(1463, 'Lantai P1', 'Blok A', 'Blok A-3', 'Tersedia', 1, NULL),
(1464, 'Lantai P1', 'Blok A', 'Blok A-4', 'Tersedia', 1, NULL),
(1465, 'Lantai P1', 'Blok A', 'Blok A-5', 'Tersedia', 1, NULL),
(1466, 'Lantai P1', 'Blok A', 'Blok A-6', 'Tersedia', 1, NULL),
(1467, 'Lantai P1', 'Blok A', 'Blok A-7', 'Tersedia', 1, NULL),
(1468, 'Lantai P1', 'Blok A', 'Blok A-8', 'Tersedia', 1, NULL),
(1469, 'Lantai P1', 'Blok A', 'Blok A-9', 'Tersedia', 1, NULL),
(1470, 'Lantai P1', 'Blok A', 'Blok A-10', 'Tersedia', 1, NULL),
(1471, 'Lantai P1', 'Blok B', 'Blok B-1', 'Tersedia', 1, NULL),
(1472, 'Lantai P1', 'Blok B', 'Blok B-2', 'Tersedia', 1, NULL),
(1473, 'Lantai P1', 'Blok B', 'Blok B-3', 'Tersedia', 1, NULL),
(1474, 'Lantai P1', 'Blok B', 'Blok B-4', 'Tersedia', 1, NULL),
(1475, 'Lantai P1', 'Blok B', 'Blok B-5', 'Tersedia', 1, NULL),
(1476, 'Lantai P1', 'Blok B', 'Blok B-6', 'Tersedia', 1, NULL),
(1477, 'Lantai P1', 'Blok B', 'Blok B-7', 'Tersedia', 1, NULL),
(1478, 'Lantai P1', 'Blok B', 'Blok B-8', 'Tersedia', 1, NULL),
(1479, 'Lantai P1', 'Blok B', 'Blok B-9', 'Tersedia', 1, NULL),
(1480, 'Lantai P1', 'Blok B', 'Blok B-10', 'Tersedia', 1, NULL),
(1481, 'Lantai P1', 'Blok C', 'Blok C-1', 'Tersedia', 1, NULL),
(1482, 'Lantai P1', 'Blok C', 'Blok C-2', 'Tersedia', 1, NULL),
(1483, 'Lantai P1', 'Blok C', 'Blok C-3', 'Tersedia', 1, NULL),
(1484, 'Lantai P1', 'Blok C', 'Blok C-4', 'Tersedia', 1, NULL),
(1485, 'Lantai P1', 'Blok C', 'Blok C-5', 'Tersedia', 1, NULL),
(1486, 'Lantai P1', 'Blok C', 'Blok C-6', 'Tersedia', 1, NULL),
(1487, 'Lantai P1', 'Blok C', 'Blok C-7', 'Tersedia', 1, NULL),
(1488, 'Lantai P1', 'Blok C', 'Blok C-8', 'Tersedia', 1, NULL),
(1489, 'Lantai P1', 'Blok C', 'Blok C-9', 'Tersedia', 1, NULL),
(1490, 'Lantai P1', 'Blok C', 'Blok C-10', 'Tersedia', 1, NULL),
(1491, 'Lantai P1', 'Blok D', 'Blok D-1', 'Tersedia', 1, NULL),
(1492, 'Lantai P1', 'Blok D', 'Blok D-2', 'Tersedia', 1, NULL),
(1493, 'Lantai P1', 'Blok D', 'Blok D-3', 'Tersedia', 1, NULL),
(1494, 'Lantai P1', 'Blok D', 'Blok D-4', 'Tersedia', 1, NULL),
(1495, 'Lantai P1', 'Blok D', 'Blok D-5', 'Tersedia', 1, NULL),
(1496, 'Lantai P1', 'Blok D', 'Blok D-6', 'Tersedia', 1, NULL),
(1497, 'Lantai P1', 'Blok D', 'Blok D-7', 'Tersedia', 1, NULL),
(1498, 'Lantai P1', 'Blok D', 'Blok D-8', 'Tersedia', 1, NULL),
(1499, 'Lantai P1', 'Blok D', 'Blok D-9', 'Tersedia', 1, NULL),
(1500, 'Lantai P1', 'Blok D', 'Blok D-10', 'Tersedia', 1, NULL),
(1501, 'Lantai P2', 'Mobil', 'P2-1', 'Tersedia', 1, NULL),
(1502, 'Lantai P2', 'Mobil', 'P2-2', 'Tersedia', 1, NULL),
(1503, 'Lantai P2', 'Mobil', 'P2-3', 'Tersedia', 1, NULL),
(1504, 'Lantai P2', 'Mobil', 'P2-4', 'Tersedia', 1, NULL),
(1505, 'Lantai P2', 'Mobil', 'P2-5', 'Tersedia', 1, NULL),
(1506, 'Lantai P2', 'Mobil', 'P2-6', 'Tersedia', 1, NULL),
(1507, 'Lantai P2', 'Mobil', 'P2-7', 'Tersedia', 1, NULL),
(1508, 'Lantai P2', 'Mobil', 'P2-8', 'Tersedia', 1, NULL),
(1509, 'Lantai P2', 'Mobil', 'P2-9', 'Tersedia', 1, NULL),
(1510, 'Lantai P2', 'Mobil', 'P2-10', 'Tersedia', 1, NULL),
(1511, 'Lantai P2', 'Blok A', 'Blok A-1', 'Tersedia', 1, NULL),
(1512, 'Lantai P2', 'Blok A', 'Blok A-2', 'Tersedia', 1, NULL),
(1513, 'Lantai P2', 'Blok A', 'Blok A-3', 'Tersedia', 1, NULL),
(1514, 'Lantai P2', 'Blok A', 'Blok A-4', 'Tersedia', 1, NULL),
(1515, 'Lantai P2', 'Blok A', 'Blok A-5', 'Tersedia', 1, NULL),
(1516, 'Lantai P2', 'Blok A', 'Blok A-6', 'Tersedia', 1, NULL),
(1517, 'Lantai P2', 'Blok A', 'Blok A-7', 'Tersedia', 1, NULL),
(1518, 'Lantai P2', 'Blok A', 'Blok A-8', 'Tersedia', 1, NULL),
(1519, 'Lantai P2', 'Blok A', 'Blok A-9', 'Tersedia', 1, NULL),
(1520, 'Lantai P2', 'Blok A', 'Blok A-10', 'Tersedia', 1, NULL),
(1521, 'Lantai P2', 'Blok B', 'Blok B-1', 'Tersedia', 1, NULL),
(1522, 'Lantai P2', 'Blok B', 'Blok B-2', 'Tersedia', 1, NULL),
(1523, 'Lantai P2', 'Blok B', 'Blok B-3', 'Tersedia', 1, NULL),
(1524, 'Lantai P2', 'Blok B', 'Blok B-4', 'Tersedia', 1, NULL),
(1525, 'Lantai P2', 'Blok B', 'Blok B-5', 'Tersedia', 1, NULL),
(1526, 'Lantai P2', 'Blok B', 'Blok B-6', 'Tersedia', 1, NULL),
(1527, 'Lantai P2', 'Blok B', 'Blok B-7', 'Tersedia', 1, NULL),
(1528, 'Lantai P2', 'Blok B', 'Blok B-8', 'Tersedia', 1, NULL),
(1529, 'Lantai P2', 'Blok B', 'Blok B-9', 'Tersedia', 1, NULL),
(1530, 'Lantai P2', 'Blok B', 'Blok B-10', 'Tersedia', 1, NULL),
(1531, 'Lantai P2', 'Blok C', 'Blok C-1', 'Tersedia', 1, NULL),
(1532, 'Lantai P2', 'Blok C', 'Blok C-2', 'Tersedia', 1, NULL),
(1533, 'Lantai P2', 'Blok C', 'Blok C-3', 'Tersedia', 1, NULL),
(1534, 'Lantai P2', 'Blok C', 'Blok C-4', 'Tersedia', 1, NULL),
(1535, 'Lantai P2', 'Blok C', 'Blok C-5', 'Tersedia', 1, NULL),
(1536, 'Lantai P2', 'Blok C', 'Blok C-6', 'Tersedia', 1, NULL),
(1537, 'Lantai P2', 'Blok C', 'Blok C-7', 'Tersedia', 1, NULL),
(1538, 'Lantai P2', 'Blok C', 'Blok C-8', 'Tersedia', 1, NULL),
(1539, 'Lantai P2', 'Blok C', 'Blok C-9', 'Tersedia', 1, NULL),
(1540, 'Lantai P2', 'Blok C', 'Blok C-10', 'Tersedia', 1, NULL),
(1541, 'Lantai P2', 'Blok D', 'Blok D-1', 'Tersedia', 1, NULL),
(1542, 'Lantai P2', 'Blok D', 'Blok D-2', 'Tersedia', 1, NULL),
(1543, 'Lantai P2', 'Blok D', 'Blok D-3', 'Tersedia', 1, NULL),
(1544, 'Lantai P2', 'Blok D', 'Blok D-4', 'Tersedia', 1, NULL),
(1545, 'Lantai P2', 'Blok D', 'Blok D-5', 'Tersedia', 1, NULL),
(1546, 'Lantai P2', 'Blok D', 'Blok D-6', 'Tersedia', 1, NULL),
(1547, 'Lantai P2', 'Blok D', 'Blok D-7', 'Tersedia', 1, NULL),
(1548, 'Lantai P2', 'Blok D', 'Blok D-8', 'Tersedia', 1, NULL),
(1549, 'Lantai P2', 'Blok D', 'Blok D-9', 'Tersedia', 1, NULL),
(1550, 'Lantai P2', 'Blok D', 'Blok D-10', 'Tersedia', 1, NULL),
(1551, 'Lantai P3', 'Mobil', 'P3-1', 'Tersedia', 1, NULL),
(1552, 'Lantai P3', 'Mobil', 'P3-2', 'Tersedia', 1, NULL),
(1553, 'Lantai P3', 'Mobil', 'P3-3', 'Tersedia', 1, NULL),
(1554, 'Lantai P3', 'Mobil', 'P3-4', 'Tersedia', 1, NULL),
(1555, 'Lantai P3', 'Mobil', 'P3-5', 'Tersedia', 1, NULL),
(1556, 'Lantai P3', 'Mobil', 'P3-6', 'Tersedia', 1, NULL),
(1557, 'Lantai P3', 'Mobil', 'P3-7', 'Tersedia', 1, NULL),
(1558, 'Lantai P3', 'Mobil', 'P3-8', 'Tersedia', 1, NULL),
(1559, 'Lantai P3', 'Mobil', 'P3-9', 'Tersedia', 1, NULL),
(1560, 'Lantai P3', 'Mobil', 'P3-10', 'Tersedia', 1, NULL),
(1561, 'Lantai P3', 'Blok A', 'Blok A-1', 'Tersedia', 1, NULL),
(1562, 'Lantai P3', 'Blok A', 'Blok A-2', 'Tersedia', 1, NULL),
(1563, 'Lantai P3', 'Blok A', 'Blok A-3', 'Tersedia', 1, NULL),
(1564, 'Lantai P3', 'Blok A', 'Blok A-4', 'Tersedia', 1, NULL),
(1565, 'Lantai P3', 'Blok A', 'Blok A-5', 'Tersedia', 1, NULL),
(1566, 'Lantai P3', 'Blok A', 'Blok A-6', 'Tersedia', 1, NULL),
(1567, 'Lantai P3', 'Blok A', 'Blok A-7', 'Tersedia', 1, NULL),
(1568, 'Lantai P3', 'Blok A', 'Blok A-8', 'Tersedia', 1, NULL),
(1569, 'Lantai P3', 'Blok A', 'Blok A-9', 'Tersedia', 1, NULL),
(1570, 'Lantai P3', 'Blok A', 'Blok A-10', 'Tersedia', 1, NULL),
(1571, 'Lantai P3', 'Blok B', 'Blok B-1', 'Tersedia', 1, NULL),
(1572, 'Lantai P3', 'Blok B', 'Blok B-2', 'Tersedia', 1, NULL),
(1573, 'Lantai P3', 'Blok B', 'Blok B-3', 'Tersedia', 1, NULL),
(1574, 'Lantai P3', 'Blok B', 'Blok B-4', 'Tersedia', 1, NULL),
(1575, 'Lantai P3', 'Blok B', 'Blok B-5', 'Tersedia', 1, NULL),
(1576, 'Lantai P3', 'Blok B', 'Blok B-6', 'Tersedia', 1, NULL),
(1577, 'Lantai P3', 'Blok B', 'Blok B-7', 'Tersedia', 1, NULL),
(1578, 'Lantai P3', 'Blok B', 'Blok B-8', 'Tersedia', 1, NULL),
(1579, 'Lantai P3', 'Blok B', 'Blok B-9', 'Tersedia', 1, NULL),
(1580, 'Lantai P3', 'Blok B', 'Blok B-10', 'Tersedia', 1, NULL),
(1581, 'Lantai P3', 'Blok C', 'Blok C-1', 'Tersedia', 1, NULL),
(1582, 'Lantai P3', 'Blok C', 'Blok C-2', 'Tersedia', 1, NULL),
(1583, 'Lantai P3', 'Blok C', 'Blok C-3', 'Tersedia', 1, NULL),
(1584, 'Lantai P3', 'Blok C', 'Blok C-4', 'Tersedia', 1, NULL),
(1585, 'Lantai P3', 'Blok C', 'Blok C-5', 'Tersedia', 1, NULL),
(1586, 'Lantai P3', 'Blok C', 'Blok C-6', 'Tersedia', 1, NULL),
(1587, 'Lantai P3', 'Blok C', 'Blok C-7', 'Tersedia', 1, NULL),
(1588, 'Lantai P3', 'Blok C', 'Blok C-8', 'Tersedia', 1, NULL),
(1589, 'Lantai P3', 'Blok C', 'Blok C-9', 'Tersedia', 1, NULL),
(1590, 'Lantai P3', 'Blok C', 'Blok C-10', 'Tersedia', 1, NULL),
(1591, 'Lantai P3', 'Blok D', 'Blok D-1', 'Tersedia', 1, NULL),
(1592, 'Lantai P3', 'Blok D', 'Blok D-2', 'Tersedia', 1, NULL),
(1593, 'Lantai P3', 'Blok D', 'Blok D-3', 'Tersedia', 1, NULL),
(1594, 'Lantai P3', 'Blok D', 'Blok D-4', 'Tersedia', 1, NULL),
(1595, 'Lantai P3', 'Blok D', 'Blok D-5', 'Tersedia', 1, NULL),
(1596, 'Lantai P3', 'Blok D', 'Blok D-6', 'Tersedia', 1, NULL),
(1597, 'Lantai P3', 'Blok D', 'Blok D-7', 'Tersedia', 1, NULL),
(1598, 'Lantai P3', 'Blok D', 'Blok D-8', 'Tersedia', 1, NULL),
(1599, 'Lantai P3', 'Blok D', 'Blok D-9', 'Tersedia', 1, NULL),
(1600, 'Lantai P3', 'Blok D', 'Blok D-10', 'Tersedia', 1, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `tarif`
--

CREATE TABLE `tarif` (
  `id` int(11) NOT NULL,
  `jenis_kendaraan` varchar(10) NOT NULL,
  `tarif_jam_pertama` float NOT NULL,
  `tarif_jam_berikutnya` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tarif`
--

INSERT INTO `tarif` (`id`, `jenis_kendaraan`, `tarif_jam_pertama`, `tarif_jam_berikutnya`) VALUES
(1, 'Motor', 3000, 1000),
(2, 'Mobil', 10000, 2000);

-- --------------------------------------------------------

--
-- Table structure for table `transaksi_pembayaran`
--

CREATE TABLE `transaksi_pembayaran` (
  `id` varchar(50) NOT NULL,
  `reservasi_kode_unik` varchar(20) NOT NULL,
  `metode_pembayaran` varchar(50) DEFAULT NULL,
  `jumlah_bayar` int(11) NOT NULL,
  `status_pembayaran` varchar(20) DEFAULT 'pending',
  `waktu_transaksi` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transaksi_pembayaran`
--

INSERT INTO `transaksi_pembayaran` (`id`, `reservasi_kode_unik`, `metode_pembayaran`, `jumlah_bayar`, `status_pembayaran`, `waktu_transaksi`) VALUES
('TKT10464686-1779779721909', 'TKT10464686', 'bank_transfer', 10000, 'settlement', '2026-05-26 07:15:22'),
('TKT13483415-1779434916567', 'TKT13483415', NULL, 4000, 'pending', '2026-05-22 07:28:36'),
('TKT13483415-1779434928737', 'TKT13483415', 'bank_transfer', 4000, 'settlement', '2026-05-22 07:28:49'),
('TKT17266512-1781488624029', 'TKT17266512', 'bank_transfer', 3000, 'settlement', '2026-06-15 01:57:04'),
('TKT19152240-1779619601632', 'TKT19152240', 'bank_transfer', 8000, 'settlement', '2026-05-24 10:46:45'),
('TKT2112265-1779435386927', 'TKT2112265', 'bank_transfer', 10000, 'settlement', '2026-05-22 07:36:27'),
('TKT21418155-1781494735724', 'TKT21418155', 'bank_transfer', 10000, 'settlement', '2026-06-15 03:38:59'),
('TKT24182686-1781494871591', 'TKT24182686', 'bank_transfer', 10000, 'settlement', '2026-06-15 03:41:11'),
('TKT24291167-1779778770132', 'TKT24291167', 'bank_transfer', 10000, 'settlement', '2026-05-26 06:59:30'),
('TKT25256626-1781493815212', 'TKT25256626', 'bank_transfer', 3000, 'settlement', '2026-06-15 03:23:36'),
('TKT29624187-1781257850676', 'TKT29624187', 'bank_transfer', 3000, 'settlement', '2026-06-12 09:50:51'),
('TKT29901775-1779778202523', 'TKT29901775', 'bank_transfer', 12000, 'settlement', '2026-05-26 06:50:02'),
('TKT32055812-1779435118593', 'TKT32055812', 'bank_transfer', 3000, 'settlement', '2026-05-22 07:31:59'),
('TKT32535200-1781440304610', 'TKT32535200', 'bank_transfer', 10000, 'settlement', '2026-06-14 12:31:44'),
('TKT37172635-1781596416811', 'TKT37172635', 'bank_transfer', 16000, 'settlement', '2026-06-16 07:53:37'),
('TKT39391133-1779794414148', 'TKT39391133', 'bank_transfer', 10000, 'settlement', '2026-05-26 11:20:15'),
('TKT42326277-1781487926315', 'TKT42326277', 'bank_transfer', 10000, 'settlement', '2026-06-15 01:45:26'),
('TKT4358218-1781426249783', 'TKT4358218', 'bank_transfer', 3000, 'settlement', '2026-06-14 08:37:30'),
('TKT46487746-1781163809375', 'TKT46487746', 'bank_transfer', 3000, 'settlement', '2026-06-11 07:43:30'),
('TKT47664858-1781164686762', 'TKT47664858', 'bank_transfer', 3000, 'settlement', '2026-06-11 07:58:07'),
('TKT48211035-1779793378524', 'TKT48211035', 'bank_transfer', 16000, 'settlement', '2026-05-26 11:02:58'),
('TKT52798428-1781494599895', 'TKT52798428', 'bank_transfer', 50000, 'settlement', '2026-06-15 03:36:40'),
('TKT56000638-1781441435950', 'TKT56000638', 'bank_transfer', 3000, 'settlement', '2026-06-14 12:50:37'),
('TKT5694483-1781164339826', 'TKT5694483', 'bank_transfer', 10000, 'settlement', '2026-06-11 07:52:20'),
('TKT62572565-1779779135418', 'TKT62572565', 'bank_transfer', 3000, 'settlement', '2026-05-26 07:05:35'),
('TKT62981430-1779778738433', 'TKT62981430', 'bank_transfer', 3000, 'settlement', '2026-05-26 06:58:58'),
('TKT64812072-1782295777416', 'TKT64812072', 'bank_transfer', 3000, 'settlement', '2026-06-24 10:09:39'),
('TKT66589869-1781441731556', 'TKT66589869', 'bank_transfer', 3000, 'settlement', '2026-06-14 12:55:31'),
('TKT67003898-1781150005588', 'TKT67003898', 'bank_transfer', 379000, 'settlement', '2026-06-11 03:53:26'),
('TKT70314285-1781494425585', 'TKT70314285', 'bank_transfer', 3000, 'settlement', '2026-06-15 03:33:49'),
('TKT72760791-1779778264604', 'TKT72760791', 'bank_transfer', 16000, 'settlement', '2026-05-26 06:51:04'),
('TKT73994931-1781442116082', 'TKT73994931', 'bank_transfer', 3000, 'settlement', '2026-06-14 13:01:56'),
('TKT75489502-1781426449946', 'TKT75489502', 'bank_transfer', 10000, 'settlement', '2026-06-14 08:40:50'),
('TKT80556455-1781440057379', 'TKT80556455', 'bank_transfer', 3000, 'settlement', '2026-06-14 12:27:37'),
('TKT83566070-1781322588318', 'TKT83566070', 'bank_transfer', 3000, 'settlement', '2026-06-13 03:49:48'),
('TKT84014145-1781441106941', 'TKT84014145', 'bank_transfer', 10000, 'settlement', '2026-06-14 12:45:07'),
('TKT84014145-1781441422039', 'TKT84014145', NULL, 10000, 'pending', '2026-06-14 12:50:23'),
('TKT87617912-1779798475330', 'TKT87617912', 'bank_transfer', 10000, 'settlement', '2026-05-26 12:27:55'),
('TKT8806488-1781486502651', 'TKT8806488', 'bank_transfer', 3000, 'settlement', '2026-06-15 01:21:43'),
('TKT88933506-1781166999122', 'TKT88933506', 'bank_transfer', 3000, 'settlement', '2026-06-11 08:36:40'),
('TKT89578357-1779435634943', 'TKT89578357', 'bank_transfer', 26000, 'settlement', '2026-05-22 07:40:35'),
('TKT98129850-1781426336355', 'TKT98129850', 'bank_transfer', 3000, 'settlement', '2026-06-14 08:38:56'),
('TKT98130668-1781256881087', 'TKT98130668', 'bank_transfer', 28000, 'settlement', '2026-06-12 09:34:41');

-- --------------------------------------------------------

--
-- Table structure for table `ulasan_parkir`
--

CREATE TABLE `ulasan_parkir` (
  `id` int(11) NOT NULL,
  `reservasi_kode_unik` varchar(20) NOT NULL,
  `user_id` int(11) NOT NULL,
  `rating` int(11) NOT NULL CHECK (`rating` >= 1 and `rating` <= 5),
  `komentar` text DEFAULT NULL,
  `waktu_ulasan` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ulasan_parkir`
--

INSERT INTO `ulasan_parkir` (`id`, `reservasi_kode_unik`, `user_id`, `rating`, `komentar`, `waktu_ulasan`) VALUES
(1, 'TKT29552663', 2, 5, 'mantap', '2026-05-22 04:26:01'),
(2, 'TKT73044365', 2, 4, 'joss', '2026-05-22 04:32:05'),
(3, 'TKT57327140', 2, 5, '', '2026-05-22 06:08:45'),
(4, 'TKT25256626', 2, 4, 'manatap', '2026-06-15 03:24:32');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `no_telepon` varchar(20) DEFAULT NULL,
  `role` varchar(20) DEFAULT 'Pengguna',
  `points` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `nama`, `email`, `password`, `no_telepon`, `role`, `points`) VALUES
(2, 'Jamal Ganteng', 'jamal@gmail.com', 'jamal123', '081322223333', 'Pengguna', 106),
(3, 'Jamall G', 'jamal1@gmail.com', 'jamalg1', '081322223331', 'Pengguna', 0),
(4, 'upinn', 'upin@gmail.com', 'upin12', '081322220000', 'Pengguna', 0),
(10, 'ipin', 'ipin@gmail.com', '$2a$10$Tt36Y7.EnYRh8j3BM2ntrO7gxuYazotXzfCOIqfo72by65xDyjoDC', '081322334455', 'Pengguna', 0);

-- --------------------------------------------------------

--
-- Table structure for table `user_pins`
--

CREATE TABLE `user_pins` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `pin` varchar(60) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_pins`
--

INSERT INTO `user_pins` (`id`, `user_id`, `pin`, `created_at`, `updated_at`) VALUES
(1, 2, '123456', '2026-05-20 06:17:29', '2026-05-20 06:17:29'),
(2, 10, '$2a$10$8R.PHpC35QvLZ68TagLPOO/rqUSzlGcbUFo1W6mc/mbBze1iUfN2e', '2026-06-25 05:04:05', '2026-06-25 05:04:05');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `area_parkir`
--
ALTER TABLE `area_parkir`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `kendaraan`
--
ALTER TABLE `kendaraan`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `reservasi`
--
ALTER TABLE `reservasi`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `kode_unik` (`kode_unik`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `slot_parkir_id` (`slot_parkir_id`),
  ADD KEY `tarif_id` (`tarif_id`),
  ADD KEY `fk_reservasi_kendaraan` (`kendaraan_id`);

--
-- Indexes for table `slot_parkir`
--
ALTER TABLE `slot_parkir`
  ADD PRIMARY KEY (`id`),
  ADD KEY `area_parkir_id` (`area_parkir_id`);

--
-- Indexes for table `tarif`
--
ALTER TABLE `tarif`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `transaksi_pembayaran`
--
ALTER TABLE `transaksi_pembayaran`
  ADD PRIMARY KEY (`id`),
  ADD KEY `reservasi_kode_unik` (`reservasi_kode_unik`);

--
-- Indexes for table `ulasan_parkir`
--
ALTER TABLE `ulasan_parkir`
  ADD PRIMARY KEY (`id`),
  ADD KEY `reservasi_kode_unik` (`reservasi_kode_unik`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `user_pins`
--
ALTER TABLE `user_pins`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `user_id` (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `area_parkir`
--
ALTER TABLE `area_parkir`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `kendaraan`
--
ALTER TABLE `kendaraan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `reservasi`
--
ALTER TABLE `reservasi`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=75;

--
-- AUTO_INCREMENT for table `slot_parkir`
--
ALTER TABLE `slot_parkir`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1601;

--
-- AUTO_INCREMENT for table `tarif`
--
ALTER TABLE `tarif`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `ulasan_parkir`
--
ALTER TABLE `ulasan_parkir`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `user_pins`
--
ALTER TABLE `user_pins`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `reservasi`
--
ALTER TABLE `reservasi`
  ADD CONSTRAINT `fk_reservasi_kendaraan` FOREIGN KEY (`kendaraan_id`) REFERENCES `kendaraan` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `reservasi_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `reservasi_ibfk_2` FOREIGN KEY (`slot_parkir_id`) REFERENCES `slot_parkir` (`id`),
  ADD CONSTRAINT `reservasi_ibfk_3` FOREIGN KEY (`tarif_id`) REFERENCES `tarif` (`id`);

--
-- Constraints for table `slot_parkir`
--
ALTER TABLE `slot_parkir`
  ADD CONSTRAINT `slot_parkir_ibfk_1` FOREIGN KEY (`area_parkir_id`) REFERENCES `area_parkir` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `transaksi_pembayaran`
--
ALTER TABLE `transaksi_pembayaran`
  ADD CONSTRAINT `transaksi_pembayaran_ibfk_1` FOREIGN KEY (`reservasi_kode_unik`) REFERENCES `reservasi` (`kode_unik`) ON DELETE CASCADE;

--
-- Constraints for table `ulasan_parkir`
--
ALTER TABLE `ulasan_parkir`
  ADD CONSTRAINT `ulasan_parkir_ibfk_1` FOREIGN KEY (`reservasi_kode_unik`) REFERENCES `reservasi` (`kode_unik`) ON DELETE CASCADE,
  ADD CONSTRAINT `ulasan_parkir_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `user_pins`
--
ALTER TABLE `user_pins`
  ADD CONSTRAINT `user_pins_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
