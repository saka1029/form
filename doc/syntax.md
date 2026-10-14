# syntax

```
 expression = [ '-' ] expr-mult { ( '+' | '-' ) expr-mult }
 expr-mult  = polynomial { [ '/' ] polynomial }
 polynomial = term { ( '+' | '-' ) term }
 term       = [ int ] { factor }
 factor     = primary [ '^' int ] 
 primary    = variable | '(' polynomial ')'
```

## 事前条件

polynomial: int または 変数 または '(' で始まっている。
factor: variable または '(' で始まっている。