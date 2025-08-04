# SpringSecurity4-Primefaces-CAS

A comprehensive Java web application demonstrating the integration of **Spring Security 4**, **PrimeFaces**, and **CAS (Central Authentication Service)** for Single Sign-On (SSO) authentication.

## 🚀 Overview

This project showcases how to build a secure web application using modern Java frameworks with enterprise-grade authentication capabilities. The application provides a complete SSO solution using CAS server for centralized authentication while maintaining a rich user interface with PrimeFaces components.

## 🏗️ Architecture

The application follows a layered architecture pattern with the following key components:

- **Presentation Layer**: JSF with PrimeFaces UI components
- **Security Layer**: Spring Security 4 with CAS integration
- **Business Logic Layer**: Spring Framework services
- **Data Access Layer**: JPA/Hibernate for database operations

## 🛠️ Technologies Used

- **Java 8+**
- **Spring Framework 4.x**
- **Spring Security 4.x**
- **JSF (JavaServer Faces)**
- **PrimeFaces** - Rich UI component library
- **CAS (Central Authentication Service)** - SSO solution
- **Maven** - Dependency management and build tool
- **Apache Tomcat** - Application server
- **JPA/Hibernate** - Data persistence
- **MySQL/PostgreSQL** - Database (configurable)

## ✨ Features

- **Single Sign-On (SSO)** authentication via CAS server
- **Role-based access control** with Spring Security
- **Responsive UI** with PrimeFaces components
- **Session management** and security
- **User authentication and authorization**
- **Protected resources** based on user roles
- **Logout functionality** with CAS integration
- **Remember-me authentication**
- **CSRF protection**

## 📋 Prerequisites

Before running this application, ensure you have:

- **Java 8 or higher** installed
- **Maven 3.6+** installed
- **Apache Tomcat 8+** or similar servlet container
- **CAS Server** running (version 4.x or 5.x)
- **Database** (MySQL, PostgreSQL, or H2 for development)
- **IDE** (Eclipse, IntelliJ IDEA, or similar)

## 🚀 Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/HenryXiloj/SpringSecurity4-Primefaces-CAS.git
cd SpringSecurity4-Primefaces-CAS
```

### 2. Configure CAS Server

Ensure your CAS server is running and accessible. Update the CAS server URL in the configuration files:

```properties
# application.properties
cas.server.url=https://your-cas-server:8443/cas
cas.service.url=http://localhost:8080/your-app
```

### 3. Database Configuration

Configure your database connection in `application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### 4. Build the Application

```bash
mvn clean compile
```

### 5. Run the Application

```bash
mvn tomcat7:run
# or
mvn spring-boot:run
```

### 6. Access the Application

Open your browser and navigate to:
```
http://localhost:8080/SpringSecurity4-Primefaces-CAS
```

You will be redirected to the CAS login page for authentication.

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           ├── config/          # Configuration classes
│   │           │   ├── SecurityConfig.java
│   │           │   ├── CasConfig.java
│   │           │   └── WebConfig.java
│   │           ├── controller/      # ManagedBeans/Controllers
│   │           ├── service/         # Business logic services
│   │           ├── repository/      # Data access layer
│   │           └── model/           # Entity classes
│   ├── resources/
│   │   ├── META-INF/
│   │   └── application.properties
│   └── webapp/
│       ├── WEB-INF/
│       │   ├── faces-config.xml
│       │   └── web.xml
│       └── resources/              # CSS, JS, Images
└── test/                          # Unit and integration tests
```

## ⚙️ Configuration

### Spring Security Configuration

The `SecurityConfig.java` class contains the main security configuration:

```java
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/login/**", "/resources/**").permitAll()
                .anyRequest().authenticated()
            .and()
            .cas()
                .casAuthenticationEntryPoint(casAuthenticationEntryPoint())
                .casAuthenticationProvider(casAuthenticationProvider());
    }
}
```

### CAS Configuration

Configure CAS integration in `CasConfig.java`:

```java
@Configuration
public class CasConfig {
    
    @Value("${cas.server.url}")
    private String casServerUrl;
    
    @Value("${cas.service.url}")
    private String casServiceUrl;
    
    // CAS configuration beans
}
```

## 🔐 Security Features

### Authentication Flow

1. User accesses a protected resource
2. Spring Security redirects to CAS login page
3. User authenticates with CAS server
4. CAS redirects back with authentication ticket
5. Spring Security validates ticket with CAS server
6. User is granted access based on roles

### Authorization

The application supports role-based access control:

- **ADMIN**: Full access to all resources
- **USER**: Limited access to user-specific resources
- **GUEST**: Read-only access to public resources

## 🧪 Testing

Run the test suite with:

```bash
mvn test
```

For integration tests:

```bash
mvn verify
```

## 📦 Deployment

### WAR Deployment

1. Build the WAR file:
```bash
mvn clean package
```

2. Deploy to Tomcat:
```bash
cp target/SpringSecurity4-Primefaces-CAS.war $TOMCAT_HOME/webapps/
```

### Docker Deployment

```dockerfile
FROM tomcat:8.5-jre8
COPY target/SpringSecurity4-Primefaces-CAS.war /usr/local/tomcat/webapps/
EXPOSE 8080
CMD ["catalina.sh", "run"]
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Troubleshooting

### Common Issues

1. **CAS Server Connection Issues**
   - Verify CAS server URL and certificate trust
   - Check network connectivity and firewall settings

2. **Database Connection Problems**
   - Verify database credentials and connection URL
   - Ensure database server is running

3. **PrimeFaces Components Not Loading**
   - Check resource mapping configuration
   - Verify PrimeFaces version compatibility

### Logging

Enable debug logging for troubleshooting:

```properties
logging.level.org.springframework.security=DEBUG
logging.level.org.jasig.cas.client=DEBUG
```

## 📚 Resources

- [Spring Security Documentation](https://docs.spring.io/spring-security/site/docs/4.2.x/reference/html/)
- [PrimeFaces Documentation](https://primefaces.org/documentation/)
- [CAS Protocol Documentation](https://apereo.github.io/cas/6.6.x/protocol/CAS-Protocol.html)
- [JSF Documentation](https://javaee.github.io/javaserverfaces-spec/)

## 👨‍💻 Author

**Henry Xiloj**
- GitHub: [@HenryXiloj](https://github.com/HenryXiloj)

## 🙏 Acknowledgments

- Spring Security team for excellent documentation
- PrimeFaces community for UI components
- CAS community for SSO solution
- All contributors and testers

---

For more information or support, please open an issue in the repository.
