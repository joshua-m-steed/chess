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

