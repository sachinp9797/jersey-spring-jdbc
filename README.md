# Jersey Spring JDBC

A hands-on exploration of wiring **Spring IoC**, **Jersey JAX-RS**, and **plain JDBC** together — without Spring Boot's abstractions removing the interesting parts.

The goal was to understand exactly how each layer connects: how Spring loads its context in a servlet container, how a JAX-RS resource that Spring doesn't instantiate can still reach Spring-managed beans, and how raw JDBC queries and updates work at the connection level.

---

## Stack

| Layer | Technology | Version |
|---|---|---|
| HTTP routing | Jersey (JAX-RS) | 2.13 |
| Dependency injection | Spring Context + Web | 5.2.2 |
| Database | Oracle JDBC (`ojdbc8`) | 12.2.0.1 |
| Build | Maven WAR | — |
| Java | JDK 8 | — |

---

## Architecture

Jersey and Spring are not natively aware of each other. `ApplicationContextProvider` bridges the gap — it implements `ApplicationContextAware` so Spring populates it on startup, and JAX-RS resources call `ApplicationContextProvider.getApplicationContext().getBean(...)` to resolve Spring-managed dependencies explicitly.

```
HTTP Request
    └── Jersey ServletContainer  (/apis/*)
            └── RestResource  (@Path /hello)
                    └── ApplicationContextProvider  ← Spring bridge
                            └── RestService  (@Service)
                                    └── FirstExample  (@Component)
                                            └── Oracle DB  (raw JDBC)
```

Spring manages `RestService` and `FirstExample` as beans via XML config (`spring-config.xml`) and component scanning. The REST resource layer lives outside Spring's container by design.

---

## Endpoints

All routes are served under `/apis/hello`.

| Method | Path | Returns | Description |
|---|---|---|---|
| `GET` | `/apis/hello` | `text/html` | Static HTML greeting |
| `GET` | `/apis/hello/{name}` | `text/html` | Passes `name` through `RestService.modifyStatement()` — exercises the Spring bean lookup |
| `GET` | `/apis/hello/names` | `text/html` | Queries Oracle `Employee` table via raw JDBC, returns first name found |
| `GET` | `/apis/hello/rdx/df` | `application/json` | Returns a static JSON map — tests Jersey's JSON serialization |

---

## Getting Started

### Prerequisites

- JDK 8+
- Apache Tomcat 8+ (or any Servlet 3.x container)
- Maven 3.5+
- Oracle Database instance

### Install the Oracle JDBC driver

`ojdbc8` is not on Maven Central. Install it to your local repository first:

```bash
mvn install:install-file \
  -Dfile=ojdbc8.jar \
  -DgroupId=com.oracle \
  -DartifactId=ojdbc8 \
  -Dversion=12.2.0.1 \
  -Dpackaging=jar
```

### Configure the database connection

Edit `web2/src/main/java/web2/DB/FirstExample.java`:

```java
static final String DB_URL = "jdbc:oracle:thin:@<host>:<port>:<sid>";
static final String USER   = "<username>";
static final String PASS   = "<password>";
```

> **Note:** Credentials are currently hardcoded as static constants. Move them to environment variables or a properties file before deploying beyond local use.

### Build and deploy

```bash
cd web2
mvn clean package
# Copy target/web2.war to Tomcat's webapps/ directory
```

### Test

```bash
# Basic greeting
curl http://localhost:8080/web2/apis/hello

# Spring bean bridge — returns "{name} is brave, he has tremendous patience"
curl http://localhost:8080/web2/apis/hello/sachin

# JDBC query (requires a running Oracle DB with Employee table)
curl http://localhost:8080/web2/apis/hello/names
```

---

## Project Structure

```
web2/
├── src/main/java/web2/
│   ├── rest/
│   │   └── RestResource.java          # JAX-RS resource, @Path /hello
│   ├── service/
│   │   └── RestService.java           # @Service — business logic
│   ├── DB/
│   │   ├── FirstExample.java          # @Component — raw JDBC queries
│   │   └── Student.java               # Model (partially explored, unused)
│   └── config/
│       └── ApplicationContextProvider.java  # Spring/Jersey bridge
├── src/main/resources/
│   └── spring-config.xml              # Spring beans + component scan
└── src/main/webapp/
    └── WEB-INF/
        └── web.xml                    # Jersey servlet + Spring listener
```

---

## Database configuration

Connection settings live in `web2/src/main/resources/db.properties` (excluded from version control via `.gitignore`):

```properties
db.url=jdbc:oracle:thin:@localhost:1521:orcl
db.user=your_username
db.password=your_password
```

---

## License

MIT — see [LICENSE](LICENSE).
