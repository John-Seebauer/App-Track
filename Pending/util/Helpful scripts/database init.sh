#!/bin/bash

mysql -Bse "CREATE USER normal IDENTIFIED BY 'coUt?-p9OaT4Ev8anIafiujoUt$=ml+b';"
mysql -Bse "CREATE USER super IDENTIFIED BY 'N<S\{kvZG3\D@_6WMMrVe?qcVqmFup/FQd6=MHj8y<J-x,VqK?Kh\!2b~\FE,PR+,u';"
mysql -Bse "CREATE DATABASE project;"
mysql -Bse --database=project "CREATE TABLE user ( username VARCHAR(20) UNIQUE NOT NULL, name VARCHAR(20), password VARCHAR(20), language CHAR(3), PRIMARY KEY (username) );"
mysql -Bse --database=project "CREATE TABLE movie ( title VARCHAR(20), `release date` DATE, genre VARCHAR(20), `avg rating` FLOAT, PRIMARY KEY (title, `release date`) );"
mysql -Bse --database=project "CREATE TABLE director ( name VARCHAR(20), birthday DATE, PRIMARY KEY (name, birthday) );"
mysql -Bse --database=project "CREATE TABLE actor ( name VARCHAR(20), birthday DATE, PRIMARY KEY (name, birthday) );"
mysql -Bse --database=project "CREATE TABLE writer ( name VARCHAR(20), birthday DATE, PRIMARY KEY (name, birthday) );"
mysql -Bse --database=project "CREATE TABLE rates ( username VARCHAR(20), title VARCHAR(20), `release date` DATE, rating FLOAT, PRIMARY KEY ( username, title, `release date`) );"
mysql -Bse --database=project "CREATE TABLE directs ( name VARCHAR(20), birthday DATE, title VARCHAR(20), `release date` DATE, PRIMARY KEY (name, birthday) );"
mysql -Bse --database=project "CREATE TABLE `acts in` ( name VARCHAR(20), birthday DATE, title VARCHAR(20), `release date` DATE, PRIMARY KEY (name, birthday) );"
mysql -Bse --database=project "CREATE TABLE writes ( name VARCHAR(20), birthday DATE, title VARCHAR(20), `release date` DATE, PRIMARY KEY (name, birthday) );"
mysql -Bse "GRANT all ON project.* TO normal@localhost;"
mysql -Bse "GRANT all ON project.* TO normal@'%';"
mysql -Bse "GRANT all ON *.* TO super;"
mysql -Bse "GRANT all ON *.* TO super@'%;"
mysql -Bse "GRANT all ON *.* TO super@localhost;"
mysql -Bse "GRANT SUPER ON *.* TO super@localhost;"
mysql -Bse "GRANT SUPER ON *.* TO super@'%';"

