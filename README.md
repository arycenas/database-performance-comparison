# Database Performance Comparison: PostgreSQL vs MongoDB

## Overview

This project compares the performance of **PostgreSQL** and **MongoDB** for various CRUD operations
and aggregations. It measures execution times for inserting, retrieving, updating, deleting, and
aggregating data from both databases. The results provide insights into how relational (PostgreSQL)
and NoSQL (MongoDB) databases handle large-scale data operations.

## Features

- **CSV Import**: Bulk inserts data into both databases and records execution time.
- **CRUD Operations**: Create, Read, Update, and Delete operations with time tracking.
- **Aggregation Functions**: Sum, Average, Min, and Max operations compared for both databases.
- **Performance Logging**: Execution time for each operation is logged to analyze efficiency.

## Technologies Used

- **Spring Boot** (Java)
- **PostgreSQL**
- **MongoDB**
- **Spring Data JPA** (for PostgreSQL)
- **Spring Data MongoDB**
- **OpenCSV** (for CSV parsing)
- **Lombok** (for reducing boilerplate code)

## How It Works

### 1️⃣ Importing CSV Data

- Reads CSV files from a specified directory.
- Parses and maps data into PostgreSQL and MongoDB models.
- Inserts data into both databases while tracking execution time.

### 2️⃣ CRUD Operations

- **Retrieve Data**: Fetches all records from both databases and logs execution time.
- **Update Data**: Updates specific fields in both databases based on provided IDs.
- **Delete Data**: Deletes records from both databases and measures performance.

### 3️⃣ Aggregation Functions

- Performs **SUM, AVERAGE, MIN, and MAX** calculations on the `totalNetAmount` field.
- Compares execution times between PostgreSQL and MongoDB.

## API Endpoints

| Method   | Endpoint                            | Description                              |
|----------|-------------------------------------|------------------------------------------|
| `POST`   | `/data/import`                      | Import CSV data into both databases      |
| `GET`    | `/data/fetch`                       | Retrieve all records from both databases |
| `PATCH`  | `/data/edit/{postgresId}/{mongoId}` | Update data in both databases            |
| `DELETE` | `/data/edit/{postgresId}/{mongoId}` | Delete records from both databases       |
| `GET`    | `/data/sum`                         | Get sum of `totalNetAmount`              |
| `GET`    | `/data/average`                     | Get average of `totalNetAmount`          |
| `GET`    | `/data/min`                         | Get minimum `totalNetAmount`             |
| `GET`    | `/data/max`                         | Get maximum `totalNetAmount`             |

## Performance Insights

- **PostgreSQL** excels at complex queries due to indexing and structured schema.
- **MongoDB** handles large-scale inserts faster due to its document-based storage.
- Aggregation performance varies depending on data size and indexing strategy.

## Setup Instructions

### Prerequisites

- **Java 17+**
- **PostgreSQL 15+**
- **MongoDB 6+**
- **Docker (optional)**

### Running Locally

1. Clone the repository:
   ```sh
   git clone https://github.com/your-username/database-comparison.git
   cd database-comparison

2. Configure CSV files local directory, PostgreSQL & MongoDB in `application.properties`:
   ```properties
   # Postgres
   spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
   spring.datasource.username=your_postgresql_username
   spring.datasource.password=your_postgresql_password
   spring.jpa.hibernate.ddl-auto=update
   # MongoDB
   spring.data.mongodb.uri=mongodb://localhost:27017/your_collection_name
   file.scan.directory=your_file_directory
   ```   
   
3. Run the application:
   ```
   mvn spring-boot:run
   ```
   
4. Access APIs at:
   - http://localhost:8080/data/import
   - http://localhost:8080/data/fetch
   - http://localhost:8080/data/sum