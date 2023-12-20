-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Dec 20, 2023 at 04:58 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `flight_booking`
--

-- --------------------------------------------------------

--
-- Table structure for table `bookings`
--

CREATE TABLE `bookings` (
  `booking_id` int(20) NOT NULL,
  `user_id` int(20) DEFAULT NULL,
  `flight_id` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bookings`
--

INSERT INTO `bookings` (`booking_id`, `user_id`, `flight_id`) VALUES
(1, 1, 'AZ101'),
(2, 2, 'PG789'),
(3, 2, 'TG123'),
(4, 1, 'SQ456'),
(5, 1, 'TG101'),
(6, 1, 'AZ101');

-- --------------------------------------------------------

--
-- Table structure for table `flights`
--

CREATE TABLE `flights` (
  `flight_id` varchar(100) NOT NULL,
  `origin` varchar(100) NOT NULL,
  `destination` varchar(100) NOT NULL,
  `date` date NOT NULL,
  `time` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `flights`
--

INSERT INTO `flights` (`flight_id`, `origin`, `destination`, `date`, `time`) VALUES
('AZ101', 'Thailand', 'Brazil', '2024-05-20', '13:15:00'),
('FD456', 'Thailand', 'Singapore', '2024-01-02', '12:15:00'),
('NZ234', 'Thailand', 'New Zealand', '2024-06-08', '08:00:00'),
('PG789', 'Thailand', 'Australia', '2023-12-28', '18:00:00'),
('SQ456', 'Thailand', 'South Korea', '2024-03-10', '10:00:00'),
('TG101', 'Thailand', 'Japan', '2023-12-25', '08:45:00'),
('TG123', 'Thailand', 'Germany', '2024-02-15', '15:30:00'),
('UA789', 'Thailand', 'United States', '2024-04-05', '20:45:00'),
('VZ567', 'Thailand', 'Malaysia', '2023-12-31', '10:20:00'),
('WE203', 'Thailand', 'China', '2023-12-24', '14:45:00');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` int(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `name`, `email`, `password`) VALUES
(1, 'Kennedy Smith', 'ksmith@gmail.com', '12345'),
(2, 'Thomas Hudson', 'thudson@gmail.com', '23456'),
(3, 'Jane Lillies ', 'janelillies@gmail.com', '34567'),
(4, 'Tanya Jacobs', 'tjacobs@gmail.com', '10000');

-- --------------------------------------------------------

--
-- Table structure for table `user_logs`
--

CREATE TABLE `user_logs` (
  `user_email` varchar(100) NOT NULL,
  `log_status` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_logs`
--

INSERT INTO `user_logs` (`user_email`, `log_status`) VALUES
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'FAILURE'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'FAILURE'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('thudson@gmail.com', 'SUCCESS'),
('thudson@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('thudson@gmail.com', 'SUCCESS'),
('thudson@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('thudson@gmail.com', 'SUCCESS'),
('thudson@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('thudson@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('ksmith@gmail.com', 'SUCCESS'),
('hdsk@gmail.com', 'FAILURE'),
('ksmith@gmail.com', 'SUCCESS');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bookings`
--
ALTER TABLE `bookings`
  ADD PRIMARY KEY (`booking_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `flight_id` (`flight_id`);

--
-- Indexes for table `flights`
--
ALTER TABLE `flights`
  ADD PRIMARY KEY (`flight_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `bookings`
--
ALTER TABLE `bookings`
  ADD CONSTRAINT `bookings_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  ADD CONSTRAINT `bookings_ibfk_2` FOREIGN KEY (`flight_id`) REFERENCES `flights` (`flight_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
