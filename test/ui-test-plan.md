# Blud UI Test Plan

Working directory: repository root  
Runtime: Java 25  
Prerequisite: the runner freshly compiles the application with `javac -d out src/main/java/*.java` before running the cases and stops if compilation fails.  
Runner: `.codex/skills/test-ui/scripts/run_ui_tests.py`  

The runner executes cases from top to bottom. `Inputs` are sent to standard input exactly as written. Output comparison normalizes line endings and ignores only the final newline; tabs, spaces, and all other characters are significant.

### Starts and exits cleanly

Aim: Verify that Blud displays its startup banner and greeting, then displays its departure message when the user exits immediately.

Command: `java -cp out Blud`

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

Command: `java -cp out Blud`

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

### Rejects an unknown command

Aim: Verify that Blud reports an error for an input that is not a supported command or task type.

Command: `java -cp out Blud`

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

Command: `java -cp out Blud`

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

Command: `java -cp out Blud`

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
