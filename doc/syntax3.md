# syntax

```
expression  = polynomial
polynomial = [ '-' ] term { ( '+' | '-' ) term }
term       = [ int ] factor { [ '/' ] factor }
factor     = primary [ '^' int ]
primary    = variable | '(' polynomial ')'
```

## 事前条件

### factor
Variable または '(' で始まっている。