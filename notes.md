**Notes of Success**
- Plan out the phases: Work on the current phase ahead of time, but also with balanced time.
- Commit to GitHub at *least* 10 times per phase. Be consistent, commit frequently.
- Prepare outlines. Map out small goals, pseudocode, and flow of data to accurately plan for structure and efficiency.

**Useful Commands to Remember**
- `git pull` - Pulls the most recent code from GitHub to IED.
- `git push` - Pushes staged changes from IED to GitHub.
- `git commit` - Saves staged changes. Adding `-m ""` Allows for a message to be added from the Terminal.
- `git add` - Stages changes of files in preparation for *commit*. Adding `.` adds all files listed under 'to be staged'.
- `git checkout` - Revisits a version of the code.


# JAVA FUNDAMENTALS

*Command Line Java*
- [NEW] `pbpaste {file.class}`: Takes from clipboard and pastes to file
- `javac {file.java}`: Compiles File
- `java {file}`: Calls file

*Tags*
 - Public: Access modifier, anyone can access it
 - Private: Access modifier, allows access only within class
 - Static: Belongs to the class

 __:: public static void main(String[] args) ::__  
A special line that lets Java call and activate whatever is in the command

*Objects*
- `toString()` - String   - Reads out memory address in byte code
- `equals()` - Bool       - Compares memory address
- `hashCode()` - Int      - Might be equal
- `clone()` - Object      - Makes a Copy
- `wait()`
- `notify()`

*Initializing arrays*
``` Java
String[] strings = new String[10];
int[] numbers = new int[5];

String[] names = {"James", "Joshua", "Time"};
int[] ages = {1,2,3};
```
- Java will __NOT__ let you overwrite array size

*Packages*
``` Java
package {name}
import {package name}
```
- Great for modularity


Check slides**
Class example
``` Java
//Field
private String name;

// Contructor
public Person(String name) {
    this. name = name;
}

// Method
public void sayName() {
    System.out.println(name);
}
``` 

__extends__ : Inherits from the parent class. Ex. public class GetSetExample extends Database { ... }  

``` Java
public class ConstructEx {
    private String value;

    //Default constructor
    public ConstructEx() {
        value = "default";
    }
    //Parameterized Constructor
    public ConstructEx(String value) {
        this.value = value;
    }

    //Copy Constructor
    public ConstructEx(ConstructorEx copy) {
        this(copy.value);
    }
}
```

Enum is like a fixed set.

``` Java
// ## Example ## //
public class GetSetExample {
    public int[] scores = new int[10];
}

// Is concise, however, you now have no controll

public class GetSetExample {
    public int[] getScores() {
        return scores;
    }

    public void setScores(int[] scores) {
        this.scores = scores;
    }

    private int[] scores = new int[10];
}

// More controll, can restrict to functions
```

*Object class*
- toString(): Often overwritten. Can be overwritten with just about anything as long as it returns a string  
- equals(): Often overwritten. Can be overwritten with different '=' ideas
- hashCode(): Often overwritten. Can be overwritten to get a value in order to distribute their values  

*Records*
Greatly simplifies the classes constructrion by taking values and not allowing mutability  


*Polymorphism* : Many Forms  
- Achieved by many interfaces and inheritance
- You only need to expose what others need to know

- Interfaces:
You define specialization

Interface Example
``` Java
public interface SymbolIterator {
    boolean hasNext();

    char next();
}
```
Implementing the Interface
``` Java
public class CharIterator implements SymbolIterator {
    int current = 0;
    String charString = 'abcdefg';

    @Override
    public boolean hasNext() {
        return current < charString.length();

    @Override
    public char next() {
        return charString.charAt(current++);
    }
    }
}
```
Provides a limiter of the class. Examples includes taking a computer and focusing/limiting the keyboard only.  


*Inheritance*
Extending what a parent is.
SuperClass is over the SubClass.  
``` Java
public class InheritanceExample {
    public static void main(String[] args) {
        var sub = new SubClass();
        System.out.println(sub);
    }

    public static class SuperClass extends Object { 
        string name = "super";

        public String toString() {
            return name;
        }
    }

    public static class SubClass extends SuperClass {
        public String toString() {
            return "Sub-class of " + super.toString();
        }
    }
}
```

*Abstract Classes*
Extending a parent & defining partial interface

``` Java
public static abstract class CharIterator implements Iterator { 
    int current = 0;
    String charString = "abcdefg";

    public boolean hasNext() {
        return current < charString.length();
    }

    public Object next() {
        return charString.substring(current, ++current)
    }

    public abstract String newWithPrefix(String prefix);
}
```
Child must implement the `newWithPrefix` method  
Child can inherit the methods `hasNext` and `next`  
The methods are the inheritance parts. The interface part is the abstract  


Create an interface with a funciton that isn't defined, so you can have different implementations
Create an inheritance provides all the methods  

`*instanceOf*`
- It's a test to see if something is an instance of [String, Object, etc...]  

*ShallowCopy*
You keep a pointer to the copy of the original data

*DeepCopy*
Same exact values, but it is in two different memory place


Feilded abstractions
``` Java
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public collection<ChessMove> pieceMoves(Board, Position);
}
```

| ChessPiece |
|------------|
| pieceColor |
| pieceType  |
| pieceMove()|

**JDK Collections**
Be familiar with these:
Package: java.util
Interfaces: List, Map, Set, Iterator
Implementations: ArrayList, HashMpa, HashSet

*Exceptions*
``` Java
// Try / Catch
try {
    // Code that might throw exceptions
} catch ('SpecificException' ex) {
    // If there's a specific exception, it can be caught
} catch (Exception ex) {
    // A General exception catch for anything
}

// Throw exeptions
void C() throws Exception {
    throw new Exception("Forces declarations");
}

// Try / Finally
try {
    // Code that may throw an exception
} finally {
    // Always call this code 
}
```

**Inner Class**  
Static Inner Class  
- Static: It can be its own thing and doesn't pay too much attention to context  
Inner Class  
- Similar to *Static Inner* but it can reference outter code or outter class values  
Local Inner Class  
- Similar to *Inner Class*, however, it's created within a method and can reference variables form the method  
Annonymous Class  
- No class is beind declared, but new instances of interfaces can be declared  

*Closure* - Close around surrounding creation state  
*Labmbda functions* - `function(() -> "return")`  
*Functional Interface* - Single method interface  

**IO**  
IO streams can be read, write, or both  
Reader <- Read something in  
Writer -> Write something out  

**Generics**  
< Type > - Allows a type to be specified with Object as a defuault item  
 - Similar to Templates  
``` Java
public class GenericExample {
    static class Storage<T> {
        List<T> items = new ArrayList<>{};

        void add(T item) { items.add(item); }

        T get(int i) { return items.get(i); }
    }

    public static void main(String[] args) {
        var intStorage = new Storage<Integer>();
        var stringStorage = new Storage<String>();
    }
}

```

**Domain Driven Design**  
Who are the *actors/users* in the system?  
What *tasks* do the actors want to accomplish?  
What are the *objects* that the actors use?  
What are the *interactions* between actors and objects?  

Object Relationships
- Is-A  :: Inheritance. Often represented by extending :: A Programmer is a Person  
- Has-A :: Encapsulation. Often represented by a field :: A Programmer has a Computer  
- Use-A :: Transient association. Often represented with a method parameter :: A Person uses a Car to travel  

Design Priciples:
- Encapsulation: Hiding the details
Prevents external code from being concerned with the internal workings of an object.  
- Immutability: Cannot Change
Immutable objects are simple. They are safe to share and publish freely without the need to make defensive copies.  
- Abstraction: Simplifying to Purpose
Allows to focus on specific points
1 - Customer Today (Works)
2 - Customer Tomorrow (Exstensability)
- Decomposition: Decomposing larger pieces to smaller bits to prevent external code from being concerned with internal workings of an object.  
- Simplicity: Keep it simple!!!
- YAGNI: You're not going to need it! Always implement what you need, never when you just foresee that
- DRY: Don't repeat yourself!
- High Cohesion Low Coupling: Modules that belong with each other should be together, but reliance outside it should be minimal in connection
- POLA: Principle of least astonishment. They should not surprise you!
**SOLID** - Bob Martin
- Single Responsibility: A module should be responsible to one, and only one, actor
- Open Closed: You should be able to extend the behavior of a system without having to modify that system
- Liskov Substitution: If a subclass violates the expecations of an interface, (throws errors and exceptions), it breaks LSP
- Interface Segregation: Clients should not be forced to depend on methods they do not use
- Dependency Inversion: High-level modules should not depend on low-level modules. Both should depend on abstractions  

# Phase 2 / 3
**Endpoints & Descriptions**  
Clear - Clears the database. Removes everything  
Register - Register a new user ( returns a new authToken )
Login - Logs in an existing user ( returns a new authToken )  
Logout - Logs out the user represented by the prodivded authToken  
List Games - Verifies the provided authToken and gives a list of all games  
Create Game - Verifies provided authToken and creates a new game  
Join Game - Verifies the provided authToken. Checks that a specific game exists, specified colors, or observer etc.  

**Objects**  
UserData
```
{
    "user": "name",
    "pass": "secret",
    "email": "@this,com"
}
```

AuthData
```
{
    "authToken": "asdoifh390u4",
    "username": "name"
}
```

GameData
```
{
    "gameID": #,
    "whitePlayer": "name",
    "blackPlayer": null,
    "GameName": "chess",
    "game": "serialized_data"
}
```


**HTTP**  
- Client to server protocol  

*URL* - Uniform Resource Locator
Scheme - https://
Domain - minijosh.click
Port - :443
^^ Address  
Path - /api/city
Parameters - ?q=pro
Anchor - #3

**Request**
Method  
POST, GET, DELETE, etc  
Path  
/session /db /etc  
Version  
HTTP/3   
Headers   
`Host: minijosh.click`  
`User-Agent: curl/7.77.0`  
`Content Length: 14 `   
Body    
`{"user":"tim"}`  

**Methods**  
GET - Get an existing resource  
POST - Create a new resource  
PUT - Update an existing resource  
DELETE - Delete a resource  
OPTIONS - Get information about a resource  

**Response**  
Version  
HTTPS/3  
Status code  
200, 401, 403, ...  
Status message  
OK, ERROR, ...  
Headers  
`Connection: keep-alive`  
Body  
`{"user":"tim"}`  

**Status Codes**  
2xx - 200 Success, 204 No Content  
3xx - 301/302 Redirect, 394 Not Modified  
4xx - 400 Bad Request, 403 Forbidden, 404 Not Found, 429 Too many requests  
5xx - 500 Server Error, 503 Not Available  

**Serialization**  
- Simplified JavaScript Object
- Serialized to text
- Unicode

*JSON*
| Type     | Example                    |
|:---------|---------------------------:|
| string   | "crockford"                |
| number   | 43                         |
| boolen   | true                       |
| array    |  [null, 43, "crockford"]   |
| object   | {"a":1, "b":"crockford"}   |
| null     | null                       |  

*GSON*
Serializes to and from JSON
 - Initialize: `var serializer = new Gson();`  
 - To JSON   : `serializer.toJson(obj);`  
 - From JSON : `serializer.fromJson(json, Map.class);`  

 **Code Quality Tools**  
 Use conventions - Naming, team, language  
  - Be consistent
  - Reduce Parameters
  - Single Return
  - Use and idiomized formatter
 Simplicity over clever and concise  
 Clarity over verbosity  
 Decomposition, abstraction, and encapsulation

 **Test Driven Development (TDD)**
 1. Add Tests while coding  
 2. Run all the tests (repeat to 1)  
 3. Refactor/cleanup  
 4. Run all the tests (repeat to 4)  

 *TTD Attributes*  
- Easy to read  
- Cohesive  
- Quick to execute  
- Does not repeat  
- Stable  
- Automated  

*JUnit*  

# Phase 4  
*Relational Databases*  
There are column relationships and row relationships  
Column -> Fields
Rows -> Objects

Example:
| id | name    | type |
|:--:|:-------:|:----:|
| 92 | Fido    | dog  |
| 14 | Puddles | cat  |
| 44 | Pubbles | fish | 

Primary key is unique to objects/tables  
Secondary keys or foreign keys aren't originally part of the table, but references  
Good Key Characteristsics
Unique - No duplicates  
Stable - Doesn't change  
Simple - Avoid composites  

Teminology
Problmems - PDBS
Verified - SQP
MySQL - Implementation


**MySQL, SQL**  
*Structure Query Language*  
Declaritive language to work with relational data  

**Java Database Connections - JDBC**  
Connecting over the network to a database  

**Security Advice**  
Make sure you know the packages that you are importing do what you think they do.
 - They execute using the credentials that you have!
Don't let the user create any of the SQL statements  
 - They can input or find a lot using those strings

**Securing Passwords**  
They hash the passwords and store the key in the new hashedpassword. More info to come in next lecture.  

**Logging and Debugging**  
Desirable Characteristics  
- Persistent  
- Immutable  
- Aggregated  
- Accessible  
- Performant  

Benefits  
- Efficient debugging  
- Security  
- Auditing  
- Performance Monitoring  

You can usually output as much of the logging you'd like as memory is cheaper.  
It can help you see the system's path to an issue or user path.  

Level Warnings :: Off, Severe, Warning, Info, Fine, Finer, Finest, All  

# Phase 6  

**WebSocket**  
*HTTP Highlights*  
- Client initiates, server responds  
- Methods, paths, headers, body  
- Extensive caching  

*HTTP Shortcomings*  
- Inefficient
- High use of resources  

*WebSocket*  
- HTTP upgrade  
- Bidirectional communication  
- More effecient communication  
- Ping/Pong for detecting dropped connections  



# Enrichment Topics
*Computer Security*  
Authentication: Who am I
Authorization: What can I do
- In Chess current example, the authorization is embedded deep within object classes, but it should be closer ot surface level
Data Integrity: Non-counterfeit
- Verifies that data is not changed over server calls
Non-Repudiation: I did this

*Cryptographic Hashing*
`echo -n "Fox" | shasum -a 256`

Desired Hash Chars
- Fixed-Size
Always the same size
- Deterministic
Same in, same out
- One-Way
Can't get the original text
- Resistance to collisions
One to one
- Preimage Resistance
Can't guess anything about the original text

Hash Functions
MD5 :: Simple, Fast, Widely used, Widely available :: Collision attacks have been found
SHA - 1 :: More Secure than MD5, Widely used, Widely available :: Collision attacks found
SHA - 256 :: Secure against known attacks :: Slower than the other two
Bcrypt :: Secure against known attacks :: Slower than SHA-256

Why do I want Unique IDs?
Quick to compare
Reduce storage

Secure Credentials
Safer to Store
Quick to Compare

Rainbow Table Attack
Collects common passwords, and then checks to see if it's in rainbow table

Salted Hash Passwords
Adds randomness to the beginning of the hash and is stored with the hash

**Encryption**
Makes data unreadable 

*Symmetric Key Encryption*
User A has a message to pass over public pathway
User B has a similar key to recieve and decrypt

Plain Text - Unencrypted Data
Key
Key Size
Cipher Text


AES - Advanced Encryption Standard  
Adds a couple more things to the encryption rules

Adv: Fast and hard to crack :: DisAdv: How do you distribute the key?  

*Asymmetric Key Encryption*  
1. Generate a key pair
2. Keep one of the keys secret (private key)
3. Give the other key to the world (public key)
4. Someone uses public key to send you data
5. Decrypt with private key

Standard Encryptions
RSA
ECC

Adv: Open key distribution :: DisAdv: Size restriction and slow  

How the Web works:
A mix of both
- A private and public key is created. Public is passed
- A symmetric key is made, passed (encrypted with public), and decrypt with private
- Data sent with symmetric key after that

*Digital Signatures and Web Certificates*  


**Decryption**
Makes data readable again

**Concurrency**  
Sequential -> One after the other  
--> -->  

Concurrent -> Time slicing  
-> ---> --> -> --> ->  

Parallel -> Same time  
---->  
---->  
Concurrent and parallel -> Same time, but sime slicing  
--> ---> ->  
--> ---> ->  

Processes - OS level concurrency  
Threads - Process level concurrency  

Negatives  
*Overhead*  
Bookkeeping cost  

*Resource Synchronization*  
When a task can run  

*Starvation*  
One task never gets scheduled  

*Deadlock*  
Two tasks each hold a key resource


Executor Services  
Task Queues, Thread Pool, and Task Submitters  

newSingleThreadExecutor - Uses a single thread and switches the callable task. Goof for removing thread context switching overhead.  
newFixedThreadPool - Reuses threads. Good for saving on thread creation overhead.  
newCachedThreadPool - Reuses threads. Good for saving on thread creation where max number of needed thread is unknown.  
newScheduledThreadPool - Runs threads periodically. Goof for scheduled tasks without creating a new thread every time.


Critcal Resource - Shared resource  
Critical Section - Code that accesses the critical resource  
Race Condition - Potentially two threads hitting programms at the same time  

Synchronization
synchronized void func() {
    // critical section
}

void func() {
    synchronized(this) {
        // Critical section
    }
}

Synchronization protects the functions/objects until the person who is using it is done using it.


# Maven - Command Line Builds  
*Maven Structure*  

Lifecycle - Collection of Phases for building or distributing an artifact  
Phase - A specific step in a lifecycle  
Goal - Code actually compiles to  
Plugin - Code  
