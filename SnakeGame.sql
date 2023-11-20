-- Use your database or create a new one
CREATE DATABASE IF NOT EXISTS snakegame;
create user 'scott'@'localhost' identified by 'tiger';
grant select, insert, update, delete, create, create view, drop,
 execute, references on snakegame.* to 'scott'@'localhost';
USE snakegame;

-- Create Players Table
CREATE TABLE IF NOT EXISTS Players (
    PlayerID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(255) UNIQUE NOT NULL,
    Password VARCHAR(255) NOT NULL,
    Email VARCHAR(255) UNIQUE
);

-- Create Scores Table
CREATE TABLE IF NOT EXISTS Scores (
    ScoreID INT AUTO_INCREMENT PRIMARY KEY,
    PlayerID INT,
    Score INT NOT NULL,
    DateAchieved DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (PlayerID) REFERENCES Players(PlayerID)
);
