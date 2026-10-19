# syntax

```
polynomial = term { ( '+' | '-' ) term }
term       = [ int ] factor { [ '/' ] factor }
factor     = primary [ '^' int ]
primary    = variable | '(' polynomial ')'
```

## 事前条件

factor: variable または '(' で始まっている。