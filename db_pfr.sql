-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: May 08, 2022 at 12:09 PM
-- Server version: 10.3.16-MariaDB
-- PHP Version: 7.3.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_pfr`
--

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `id` int(11) NOT NULL,
  `type` varchar(15) NOT NULL,
  `name` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`id`, `type`, `name`) VALUES
(1, 'spending', 'food / baverage'),
(2, 'spending', 'transportation'),
(3, 'spending', 'entertainment'),
(4, 'spending', 'tax'),
(5, 'spending', 'education'),
(6, 'spending', 'other'),
(7, 'income', 'salary'),
(8, 'income', 'transfer'),
(9, 'income', 'investation'),
(10, 'income', 'other');

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  `description` varchar(15) DEFAULT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `transactions`
--

INSERT INTO `transactions` (`id`, `user_id`, `category_id`, `amount`, `description`, `date`) VALUES
(53, 1, 7, 7000, 'dwadawd', '2022-05-28'),
(57, 1, 7, 5000, 'dawdawd', '2022-05-21'),
(62, 1, 4, 10000, 'dwadwa', '2022-05-05'),
(65, 1, 4, 12000, 'dwadwa', '2022-05-13'),
(66, 1, 6, 5000, 'dawdaw', '2022-05-20'),
(67, 1, 6, 5000, 'dwdwqdq', '2022-05-19'),
(68, 1, 6, 5000, 'dawdwad', '2022-05-12'),
(69, 1, 4, 7000, 'dwdwq', '2022-05-12'),
(70, 1, 6, 20000, 'dawdawd', '2022-05-12'),
(71, 1, 6, 8000, 'adawdwa', '2022-05-12'),
(80, 1, 7, 5000, 'dawdwad', '2022-05-13'),
(81, 1, 10, 5000, 'dwadwa', '2022-05-21'),
(84, 1, 6, 30000, 'dwadawd', '2022-05-12'),
(85, 5, 9, 100000, 'testing', '2022-05-19'),
(89, 2, 7, 500, 'dadwadwa', '2022-05-10'),
(90, 6, 7, 50000, 'testing', '2022-05-05'),
(93, 6, 6, 5000, 'dwad', '2022-05-04');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(15) NOT NULL,
  `password` varchar(15) NOT NULL,
  `saldo` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `saldo`) VALUES
(1, 'user1', 'password', 45000),
(2, 'user2', 'password', 20500),
(3, 'eqeq2e2qe2e', 'e2qe2qe', 0),
(4, 'user3', 'password', 0),
(5, 'user4', 'password', 100000),
(6, 'user5', 'password', 45000),
(7, 'user9', 'password', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_users` (`user_id`),
  ADD KEY `fk_categories` (`category_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `transactions`
--
ALTER TABLE `transactions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=95;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `fk_categories` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
  ADD CONSTRAINT `fk_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
