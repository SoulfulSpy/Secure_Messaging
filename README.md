# Secure Microservices Messaging System

A comprehensive end-to-end encrypted messaging system built with Spring Boot microservices architecture, featuring JWT authentication and AES/RSA encryption.

## 🏗️ Architecture Overview

This project consists of three independent microservices that work together to provide secure messaging capabilities:

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Auth Service  │    │Encryption Service│    │ Message Service │
│   (Port 8080)   │    │   (Port 8081)   │    │   (Port 8082)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         │                       │                       │
         ▼                       ▼                       ▼
   JWT Token Generation    AES/RSA Encryption    Message Storage &
   User Authentication     & Decryption         Access Control
```

## 🚀 Features

- **JWT Authentication**: Secure token-based authentication
- **End-to-End Encryption**: Messages encrypted using AES/RSA algorithms
- **Role-Based Access Control**: Users can only access their own messages
- **Microservices Architecture**: Independent, scalable services
- **H2 Database**: In-memory database for development
- **RESTful APIs**: Clean and well-documented endpoints

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Git

## 🛠️ Project Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd Microservices
```

### 2. Build the Project
```bash
mvn clean compile
```

### 3. Run the Services

You need to run all three services. Open **3 separate terminal windows**:

#### Terminal 1 - Auth Service
```bash
cd auth-service
mvn spring-boot:run
```

#### Terminal 2 - Encryption Service
```bash
cd encryption-service
mvn spring-boot:run
```

#### Terminal 3 - Message Service
```bash
cd message-service
mvn spring-boot:run
```

### 4. Verify Services are Running
```bash
# Check if all services are running
netstat -an | findstr :808
```

You should see ports 8080, 8081, and 8082 listening.

## 🔐 Default Users

| Username | Password | Role |
|----------|----------|------|
| `user` | `user_pass` | USER |
| `admin` | `admin_pass` | ADMIN |

## 📡 API Endpoints

### Auth Service (Port 8080)

#### Get JWT Token
```http
POST /api/auth/token
Content-Type: application/json

{
    "username": "user",
    "password": "user_pass"
}
```

**Response:**
```json
{
    "token": "eyJhbGciOiJIUzM4NCJ9..."
}
```

### Encryption Service (Port 8081)

#### Encrypt Message
```http
POST /api/encrypt
Content-Type: application/json

{
    "message": "Hello World",
    "encryptionType": "AES"
}
```

#### Decrypt Message
```http
POST /api/decrypt
Content-Type: application/json

{
    "message": "encrypted_data_here",
    "encryptionType": "AES"
}
```

### Message Service (Port 8082)

#### Send Message
```http
POST /api/messages/send
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
    "recipient": "admin",
    "content": "Hello from message service!"
}
```

#### Get All Messages (Inbox)
```http
GET /api/messages/inbox
Authorization: Bearer YOUR_JWT_TOKEN
```

#### Get Sent Messages
```http
GET /api/messages/sent
Authorization: Bearer YOUR_JWT_TOKEN
```

#### Get Received Messages
```http
GET /api/messages/received
Authorization: Bearer YOUR_JWT_TOKEN
```

#### Test Service
```http
GET /api/messages/test
```

## 🧪 Testing with Postman

A Postman collection is included in the project root: `Secure_Messaging_Service.postman_collection.json`

### Import the Collection
1. Open Postman
2. Click "Import"
3. Select the `Secure_Messaging_Service.postman_collection.json` file

### Testing Workflow
1. **Get Authentication Token**: Use the auth endpoint to get a JWT token
2. **Test Encryption**: Verify encryption service is working
3. **Send Message**: Send an encrypted message
4. **Retrieve Messages**: Check inbox, sent, and received messages

## 🔒 Security Features

### Message Access Control
- Users can only see messages where they are the sender or recipient
- Separate endpoints for sent vs received messages
- JWT-based authentication for all message operations

### Encryption
- **AES Encryption**: Symmetric encryption for message content
- **RSA Encryption**: Asymmetric encryption (available)
- Messages are encrypted before storage in database
- Decryption happens only when authorized users access messages

### Database Security
- Messages stored in encrypted format
- H2 console available for development: `http://localhost:8080/h2-console` and `http://localhost:8082/h2-console`

## 🗄️ Database Configuration

### Auth Service Database
- **URL**: `jdbc:h2:mem:authdb`
- **Console**: `http://localhost:8080/h2-console`
- **Username**: `sa`
- **Password**: `password`

### Message Service Database
- **URL**: `jdbc:h2:mem:messagedb`
- **Console**: `http://localhost:8082/h2-console`
- **Username**: `sa`
- **Password**: `password`

## 🔧 Configuration

### Encryption Keys
- **AES Key**: 32-byte key for symmetric encryption
- **RSA Keys**: Public/private key pair for asymmetric encryption
- Keys are configured in `encryption-service/src/main/resources/application.properties`

### JWT Configuration
- **Secret**: Shared secret key between auth and message services
- **Expiration**: Configurable token expiration time

## 🚨 Troubleshooting

### Common Issues

#### 1. Port Already in Use
```bash
# Find process using port
netstat -ano | findstr :8080
# Kill process (replace PID)
taskkill /PID <PID> /F
```

#### 2. Service Won't Start
- Ensure Java 17+ is installed
- Check if all dependencies are resolved: `mvn clean compile`
- Verify no other services are using the ports

#### 3. 404 Errors on Endpoints
- Ensure all services are running
- Check if you're using the correct port
- Verify JWT token is valid and included in Authorization header

#### 4. Encryption Errors
- Restart encryption service after any configuration changes
- Verify encryption service is running on port 8081

### Debug Mode
Enable debug logging by adding to `application.properties`:
```properties
logging.level.com.greatkapital=DEBUG
```

## 📁 Project Structure

```
Microservices/
├── auth-service/                 # Authentication microservice
│   ├── src/main/java/
│   │   └── com/greatkapital/authservice/
│   │       ├── controller/       # REST controllers
│   │       ├── service/          # Business logic
│   │       ├── config/           # Security configuration
│   │       ├── entity/           # JPA entities
│   │       └── util/             # JWT utilities
│   └── src/main/resources/
│       └── application.properties
├── encryption-service/           # Encryption microservice
│   ├── src/main/java/
│   │   └── com/greatkapital/encryptionservice/
│   │       ├── controller/       # Encryption endpoints
│   │       ├── service/          # AES/RSA strategies
│   │       └── dto/              # Data transfer objects
│   └── src/main/resources/
│       └── application.properties
├── message-service/              # Messaging microservice
│   ├── src/main/java/
│   │   └── com/greatkapital/messageservice/
│   │       ├── controller/       # Message endpoints
│   │       ├── service/          # Message business logic
│   │       ├── entity/           # Message entity
│   │       ├── repository/       # Data access layer
│   │       ├── config/           # Security & REST config
│   │       └── filter/           # JWT filter
│   └── src/main/resources/
│       └── application.properties
├── pom.xml                       # Parent POM
├── public_key_base64.txt         # RSA public key
├── private_key_base64.txt        # RSA private key
└── README.md                     # This file
```

## 🔄 Service Communication Flow

1. **Authentication**: User authenticates with auth service
2. **Token Generation**: Auth service returns JWT token
3. **Message Sending**: 
   - User sends message to message service
   - Message service calls encryption service to encrypt content
   - Encrypted message stored in database
4. **Message Retrieval**:
   - User requests messages from message service
   - Message service retrieves encrypted messages
   - Message service calls encryption service to decrypt content
   - Decrypted messages returned to user

## 🚀 Production Considerations

### Security
- Use proper key management service (not hardcoded keys)
- Implement HTTPS/TLS
- Use production-grade databases (PostgreSQL, MySQL)
- Implement proper logging and monitoring

### Scalability
- Use service discovery (Eureka, Consul)
- Implement API Gateway (Spring Cloud Gateway)
- Use message queues for async communication
- Implement circuit breakers and retry mechanisms

### Deployment
- Containerize with Docker
- Use Kubernetes for orchestration
- Implement health checks and readiness probes
- Use configuration management (Spring Cloud Config)

## 📝 License

This project is for educational and development purposes.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📞 Support

For issues and questions, please create an issue in the repository.

---

**Happy Coding! 🎉**
