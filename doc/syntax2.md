# syntax

```
expression  = poly-add
poly-add    = [ '-' ] poly-mult { ( '+' | '-' ) poly-mult }
poly-mult   = polynomial { [ '/' ] polynomial } 
polynomial = term { ( '+' | '-' ) term }
term       = [ int ] { factor }
factor     = primary [ '^' int ]
primary    = variable | '(' polynomial ')'
```

## 事前条件

polynomial: int または 変数 または '(' で始まっている。
factor: variable または '(' で始まっている。