# Blud UI Test Plan

Working directory: repository root  
Runtime: Java 25  
Prerequisite: the runner resolves the Gradle main compile classpath, freshly compiles the application with `javac`, and stops if compilation fails.  
Runner: `.codex/skills/test-ui/scripts/run_ui_tests.py`  

The runner executes cases from top to bottom. `Inputs` are sent to standard input exactly as written. Output comparison normalizes line endings and ignores only the final newline; tabs, spaces, and all other characters are significant.

### Starts and exits cleanly

Aim: Verify that Blud displays its startup banner and greeting, then displays its departure message when the user exits immediately.

Command: `java -cp out duke.Blud`

Inputs:
```text
bye
```

Expected output:
```text
	-------------------------------
	 ____  _            _
	| __ )| |_   _  ___| |
	|  _ \| | | | |/ __| |
	| |_) | | |_| | (__|_|
	|____/|_|\__,_|\___(_)
	Hey! This is Blud, what can I do for you today?
	
	-------------------------------
	Thanks for the conversation, see you soon!
	-------------------------------
```

### Rejects an empty todo description

Aim: Verify that Blud reports an error when a todo command has no description and continues accepting commands.

Command: `java -cp out duke.Blud`

Inputs:
```text
todo
bye
```

Expected output:
```text
	-------------------------------
	 ____  _            _
	| __ )| |_   _  ___| |
	|  _ \| | | | |/ __| |
	| |_) | | |_| | (__|_|
	|____/|_|\__,_|\___(_)
	Hey! This is Blud, what can I do for you today?
	
	-------------------------------
	Missing description of todo task
	-------------------------------
	Thanks for the conversation, see you soon!
	-------------------------------
```

### Finds tasks by a description keyword

Aim: Verify that Blud finds tasks whose descriptions partially match a keyword, regardless of letter case.

Command: `java -cp out duke.Blud`

Inputs:
```text
todo read book
deadline return book /by 6/6/2026 1800
find BOOK
bye
```

Expected output:
```text
	-------------------------------
	 ____  _            _
	| __ )| |_   _  ___| |
	|  _ \| | | | |/ __| |
	| |_) | | |_| | (__|_|
	|____/|_|\__,_|\___(_)
	Hey! This is Blud, what can I do for you today?
	
	-------------------------------
	added: [T][ ] read book
	Now you have 1 tasks in the list
	-------------------------------
	added: [D][ ] return book (by: Jun 06 2026, 6:00 pm)
	Now you have 2 tasks in the list
	-------------------------------
	Here are the matching tasks in your list:
	1. [T][ ] read book
	2. [D][ ] return book (by: Jun 06 2026, 6:00 pm)
	-------------------------------
	Thanks for the conversation, see you soon!
	-------------------------------
```

### Rejects an unknown command

Aim: Verify that Blud reports an error for an input that is not a supported command or task type.

Command: `java -cp out duke.Blud`

Inputs:
```text
blah
bye
```

Expected output:
```text
	-------------------------------
	 ____  _            _
	| __ )| |_   _  ___| |
	|  _ \| | | | |/ __| |
	| |_) | | |_| | (__|_|
	|____/|_|\__,_|\___(_)
	Hey! This is Blud, what can I do for you today?
	
	-------------------------------
	invalid task type blah, please use one of todo, event or deadline task types
	-------------------------------
	Thanks for the conversation, see you soon!
	-------------------------------
```

### Rejects a deadline without a deadline field

Aim: Verify that Blud reports an error when a deadline task omits its `/by` field.

Command: `java -cp out duke.Blud`

Inputs:
```text
deadline submit report
bye
```

Expected output:
```text
	-------------------------------
	 ____  _            _
	| __ )| |_   _  ___| |
	|  _ \| | | | |/ __| |
	| |_) | | |_| | (__|_|
	|____/|_|\__,_|\___(_)
	Hey! This is Blud, what can I do for you today?
	
	-------------------------------
	Missing deadline (/by) for deadline task
	-------------------------------
	Thanks for the conversation, see you soon!
	-------------------------------
```

### Rejects an event without event dates

Aim: Verify that Blud reports an error when an event task omits its `/from` and `/to` fields.

Command: `java -cp out duke.Blud`

Inputs:
```text
event team meeting
bye
```

Expected output:
```text
	-------------------------------
	 ____  _            _
	| __ )| |_   _  ___| |
	|  _ \| | | | |/ __| |
	| |_) | | |_| | (__|_|
	|____/|_|\__,_|\___(_)
	Hey! This is Blud, what can I do for you today?
	
	-------------------------------
	Missing start (/from) and end (/to) dates for deadline task
	-------------------------------
	Thanks for the conversation, see you soon!
	-------------------------------
```
