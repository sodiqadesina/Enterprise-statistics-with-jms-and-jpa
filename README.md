# Enterprise Statistics Platform (JMS + JPA)

Author: Sodiq Adesina 



This project extends the previous *Enterprise Computing Statistics Platform* by integrating **JMS messaging**, **Message-Driven Beans (MDBs)**, and **JPA persistence** into a full-stack enterprise system.  
It demonstrates how EJBs, Servlets, JMS Queues / Topics, and MySQL-backed DAOs cooperate inside a modular Jakarta EE application deployed on **WildFly 18**.


## Architecture
- **EJBs:** Stateless/Stateful/Singleton; one stateless bean exposes JMS helpers.
- **JMS:** Queue & Topic; **MDBs** consume messages to (a) add data, (b) store data, (c) save models.
- **JPA/DAO:** `User`, `Model` entities; DAOs wrap CRUD via the `primary` persistence unit.
- **Web:** Servlets for `add-data`, `get`, `save-model`, `get-model`.
- **Client:** CLI tools for JMS publish/produce and a remote Session Bean client.

### Structure
```bash
Enterprise-statistics-jms-jpa/
│
├── stats-ejb/ # EJBs + MDBs + JPA entities + DAO layer
├── stats-web/ # Servlets + HTML dashboards + JMS interfaces
├── stats-ear/ # EAR packaging and deployment descriptor
└── stats-client/ # Stand-alone JMS producers / publishers / DB scripts
```



## ⚙️ Technologies Used
Java 8 | Jakarta EE (EJB 3.2, JMS, Servlet 4.0, JPA 2.1) | MySQL 8 | WildFly 18 | Maven | JUnit | Log4j  


---

## 🧠 Core Features

- **JPA Persistence:** User and Model entities persisted to MySQL (`java:/MySqlDS`) using Hibernate.  
- **Asynchronous Messaging:** Queue (`StatsQueue`) and Topic (`StatsTopic`) enable parallel stat updates and model saving.  
- **Message-Driven Beans:** Automated message handling for data input, model storage, and file persistence.  
- **Web Integration:** Role-based dashboards for Admin, Developer, and User.  
- **Remote Clients:** Testable from command-line tools without a web UI.  

---

## 🧱 Part 1 – JPA Persistence and DAO Testing

### 🧾 Database CRUD Validation
`StatsDBCreate`, `StatsDBInsert`, `StatsDBSelect`, and `StatsDBDelete` validate database connectivity and DAO correctness.

![DB Client and CRUD Operations](images/db-cleint-and-crud-operation.PNG)

**Explanation:**  
This test demonstrates full CRUD capability on the `ecuser` and `ecmodel` tables.  
- `StatsDBCreate` generated the tables automatically via JPA.  
- Inserts and selections confirm the `EntityManager` is properly configured with `MySqlDS`.  
- Matching console and MySQL views prove successful DAO wiring and persistence lifecycle.

---

### 👤 User Entity and DAO

![User Entity Bean](images/User-Entity-Bean.PNG)  
![Using User Entity Beans](images/Using-User-Entity-Beans.PNG)

**Explanation:**  
The `User` entity defines fields like `id`, `username`, and `role`.  
In the second screenshot, DAO operations (`persist()`, `find()`, `remove()`) confirm entity lifecycle management works seamlessly through the JPA context.  
Automatic ID generation shows Hibernate’s schema synchronization (`hbm2ddl=update`) is active.

---

### 🧩 Model Entity and DAO

![Model Entity Bean](images/Model-Entity-Bean.PNG)  
![Using Model Entity Bean](images/Using-Model-Entity-Bean.PNG)

**Explanation:**  
The `Model` entity encapsulates computed statistics (`count`, `mean`, `std`, etc.) stored as a serialized `StatsSummary`.  
DAO test outputs confirm the entity is correctly inserted and retrieved from MySQL.  
SQL logs in WildFly show `insert` and `select` operations, verifying the `primary` persistence unit configuration.

---

## 📡 Part 2 – JMS Messaging and MDB Processing

### ✉️ JMS Client (Producer & Publisher)

![JMS Client Results](images/jms-client-results.PNG)

**Explanation:**  
Two JMS clients send messages:
- **Producer:** sends “save” to the Queue to trigger model persistence.  
- **Publisher:** sends “10” to the Topic to broadcast new statistical data.  
WildFly logs confirm both messages reach their respective MDBs, showing proper JNDI lookup and resource injection.

---

### 🧮 Message-Driven Beans (MDBs)

| MDB Class | Destination | Purpose |
|------------|--------------|---------|
| `StatsMDBAddData` | Topic | Adds incoming numeric data to statistics. |
| `StatsMDBSaveModel` | Queue | Saves computed model snapshots to DB. |
| `StatsMDBStoreData` | Queue | Logs numeric data to a text file for audit. |

#### Add Data MDB
![Message Queue MDB](images/message-queue-mdb.PNG)

#### Topic MDB
![Message Topic MDB](images/message-topic-mdb.PNG)

**Explanation:**  
The WildFly console shows the MDBs deployed and active (“Started message driven bean…”).  
When a topic message (“10”) is published, all subscribed MDBs respond — updating stats, saving models, and storing data files concurrently.  
This demonstrates asynchronous, decoupled event-driven processing in an enterprise environment.

---

## 💻 Part 3 – Web Dashboard & Servlet Integration

### 🌐 Dashboard Pages

![JMS Client Within Servlet](images/jms-client-within-servlet.PNG)

**Explanation:**  
The web interface exposes role-based dashboards:  
- **Admin:** manages users.  
- **Developer:** triggers data-saving operations.  
- **User:** retrieves live statistics and model summaries.  
Each page invokes a Servlet that communicates with an EJB or JMS helper bean behind the scenes.  
The screenshot shows real-time result confirmation from the servlet responses.

---

### ⚙️ Servlet and Stateless Bean Interaction

![JMS Client Within Session Beans](images/jms-client-within-session-beans.PNG)

**Explanation:**  
This verifies end-to-end communication between the web layer and backend logic.  
A stateless session bean sends Queue and Topic messages internally when a servlet request is made.  
The console log (visible above) proves that each web-triggered call successfully propagated to the JMS infrastructure, invoking the MDBs as designed.

---

## 🔄 Part 4 – End-to-End JMS Workflow

![Model Operations](images/Using-Model-Entity-Bean.PNG)

**Explanation:**  
This test validates the full workflow:
1. **Request:** `/producer?message=save` — pushes to `StatsMDBSaveModel`.  
2. **Broadcast:** `/publisher?message=10` — triggers both `StatsMDBAddData` and `StatsMDBStoreData`.  
3. **Outcome:** Log entries show “Data appended to file: 10”, while JPA confirms `Model` updates in MySQL.  
4. **Query:** Servlet fetches current statistics, returning `count=3`, `mean=20`.  

This proves successful orchestration between Servlets → EJBs → JMS → MDBs → JPA.

---

## ⚙️ Persistence Configuration

```xml
<persistence-unit name="primary" transaction-type="JTA">
  <jta-data-source>java:/MySqlDS</jta-data-source>
  <class>ec.stats.model.User</class>
  <class>ec.stats.model.Model</class>
  <properties>
    <property name="hibernate.dialect" value="org.hibernate.dialect.MySQL8Dialect"/>
    <property name="hibernate.hbm2ddl.auto" value="update"/>
    <property name="hibernate.show_sql" value="true"/>
  </properties>
</persistence-unit>



```
Explanation:
The persistence.xml links Hibernate to MySQL and automatically creates entity tables.

### 🚀 Build and Run Guide

```bash
# 1. Build and package
mvn clean package

# 2. Deploy EAR to WildFly
cd stats-app
mvn wildfly:deploy

# 3. Open dashboards
http://127.0.0.1:8080/stats-web/

# 4. Run JMS client tools
cd stats-client
java -cp target/stats-client.jar ec.stats.jms.StatsJMSProducer -m save
java -cp target/stats-client.jar ec.stats.jms.StatsJMSPublisher -m 10

```

🧩 Future Improvements

Add REST APIs (JAX-RS) for AJAX front-end integration.

Implement role-based login via Elytron.

Containerize WildFly and MySQL with Docker Compose.

Add unit tests for MDB consumers and DAO methods.
