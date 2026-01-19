# Inventory Management System (IMS)

A robust and modern full-stack web application designed to streamline inventory tracking, product management, and transaction recording. This system integrates a high-performance Spring Boot backend with a responsive Angular frontend to provide a seamless experience for businesses.

## 🚀 Key Features

*   **Dashboard**: Tabbed interface for viewing stock status, product list, and transaction entries.
*   **Product Management**: Intuitive system to add, update, and delete products.
*   **Inventory Tracking**: Real-time monitoring of stock levels.
*   **Transaction Recording**: 
    *   **Inbound**: Record stock arrivals and purchases.
    *   **Outbound**: Track sales and stock usage.
*   **Modern UI**: Responsive and intuitive interface built with **Angular Material** and **Tailwind CSS**.
*   **Scalable Backend Architecture**: Powered by **Spring Boot** and **PostgreSQL**.

## 🛠️ Tech Stack

### Frontend (`ims-frontend`)
*   **Framework**: [Angular](https://angular.io/) v21
*   **UI Components**: Angular Material
*   **Styling**: Tailwind CSS v4
*   **Language**: TypeScript

### Backend (`ims-backend`)
*   **Framework**: [Spring Boot](https://spring.io/projects/spring-boot)
*   **Language**: Java 17
*   **Database**: PostgreSQL
*   **ORM**: Spring Data JPA (Hibernate)


## ⚙️ Getting Started

### Prerequisites
*   **Java 17 JDK**
*   **Node.js** (v18+)
*   **Docker Desktop** (Optional, for automatic database setup)

### Backend Setup (`ims-backend`)

The backend uses Spring Boot and supports Docker Compose for seamless database integration.

1.  **Navigate to the backend directory:**
    ```bash
    cd ims-backend
    ```

2.  **Database Configuration:**
    *   **With Docker (Recommended):** The project includes `spring-boot-docker-compose`. Ensure **Docker Desktop is running**, then start the application. It will automatically spin up the database defined in `compose.yaml`.
    *   **Without Docker:** Ensure you have a local PostgreSQL instance running on port `5432` with a database named `ims-database`.
        *   Credentials (default): `root` / `password123`
        *   Update `src/main/resources/application.properties` if needed.

3.  **Run the Application:**
    ```bash
    ./mvnw spring-boot:run
    ```

### Frontend Setup (`ims-frontend`)

1.  **Navigate to the frontend directory:**
    ```bash
    cd ims-frontend
    ```

2.  **Install Dependencies:**
    ```bash
    npm install
    ```

3.  **Run the Development Server:**
    ```bash
    ng serve
    ```

4.  **Access the Application:**
    Open your browser and navigate to `http://localhost:4200/`